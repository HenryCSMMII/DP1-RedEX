import React, { useRef, useEffect, useState } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';
import { Bar } from 'react-chartjs-2';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';

const Container = styled.div`
  padding: 20px;
  max-width: 1400px; // Ajusta este valor para hacer el popup más grande
  margin: auto;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #8110000; // Color de fondo plomo más claro
  border-radius: 10px;
  border: 2px solid #000; // Añadir contorno
  z-index: 1050; // Asegurar que el popup esté delante de todo
`;

const Title = styled.h2`
  margin-bottom: 15px;
  font-size: 1.2em;
`;

const Content = styled.div`
  display: flex;
  width: 100%;
  justify-content: space-between;
  margin-top: 20px;
`;

const ChartContainer = styled.div`
  flex: 1;
  margin-right: 40px; // Más separación entre el gráfico y la lista de itinerarios
  min-width: 600px; // Aumenta el tamaño mínimo del gráfico
`;

const ItinerariesContainer = styled.div`
  flex: 2;
  max-height: 500px; // Ajusta este valor según tus necesidades
  overflow-y: auto;
  text-align: left;
  border: 2px solid #ddd; // Agrega un marco alrededor del contenedor
  padding: 20px;
  background-color: #f9f9f9;
`;

const Itinerary = styled.div`
  margin-bottom: 10px;
  padding: 10px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  &:not(:last-child) {
    margin-bottom: 15px;
  }
`;

const ItineraryID = styled.p`
  font-weight: bold;
  font-size: 1em;
  color: #007bff;
`;

const ButtonsContainer = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 30px;
`;

const Button = styled.button`
  padding: 10px 20px;
  background-color: #005bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  &:hover {
    background-color: #0056b3;
  }
  margin: 0 15px;
`;

const customStyles = {
  overlay: {
    backgroundColor: 'rgba(0, 0, 0, 0.75)', // Fondo oscuro que cubre toda la pantalla
    zIndex: 1040, // Asegurar que el fondo esté detrás del popup
  },
  content: {
    top: '50%',
    left: '50%',
    right: 'auto',
    bottom: 'auto',
    marginRight: '-50%',
    transform: 'translate(-50%, -50%)',
    maxWidth: '1440px', // Ajusta este valor para hacer el popup más grande
    textAlign: 'center',
    zIndex: 1050, // Asegurar que el contenido esté delante de todo
  },
};

const FinalSimulationPopup = ({ isOpen, onRequestClose, itineraries = [], currentItineraries = [] }) => {
  const reportRef = useRef();
  const [topFlights, setTopFlights] = useState([]);

  useEffect(() => {
    const sortedFlights = [...currentItineraries].sort((a, b) => (b.current_load / b.capacity) - (a.current_load / a.capacity));
    setTopFlights(sortedFlights.slice(0, 5));
  }, [currentItineraries]);

  const getChartData = (flights) => {
    const labels = flights.map((flight) => `Vuelo ${flight.id}`);
    const loads = flights.map((flight) => ((flight.current_load / flight.capacity) * 100).toFixed(2));

    return {
      labels,
      datasets: [
        {
          label: 'Porcentaje de Saturación',
          data: loads,
          backgroundColor: 'rgba(255, 99, 132, 0.6)',
        },
      ],
    };
  };

  const downloadPDF = () => {
    const input = reportRef.current;
    const originalHeight = input.style.height;
    input.style.height = 'auto'; // Expandir el contenedor para capturar todo el contenido

    html2canvas(input, { scale: 2 })
      .then((canvas) => {
        const imgData = canvas.toDataURL('image/png');
        const pdf = new jsPDF('landscape');
        const imgProps = pdf.getImageProperties(imgData);
        const pdfWidth = pdf.internal.pageSize.getWidth();
        const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;
        pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
        pdf.save('reporte_vuelos.pdf');
        input.style.height = originalHeight; // Restaurar la altura original del contenedor
      })
      .catch((error) => {
        console.error('Error generating PDF: ', error);
        input.style.height = originalHeight; // Restaurar la altura original del contenedor
      });
  };

  return (
    <Modal isOpen={isOpen} onRequestClose={onRequestClose} style={customStyles}>
      <Container ref={reportRef}>
        <Title>Reporte de Planificación de Vuelos</Title>
        <Content>
          <ChartContainer>
            <Title>Top 5 Vuelos más Saturados</Title>
            <Bar 
              data={getChartData(topFlights)} 
              options={{ 
                responsive: true, 
                scales: {
                  x: {
                    ticks: {
                      callback: function(value) {
                        return this.getLabelForValue(value);
                      }
                    }
                  }
                }
              }} 
            />
          </ChartContainer>
          <ItinerariesContainer>
            <Title>Vuelos de Planificación   </Title>
            {itineraries.length > 0 ? (
              itineraries.map((flight, index) => (
                <Itinerary key={index}>
                  <ItineraryID>ID del vuelo: {flight.id}</ItineraryID>
                  <p><strong>Aeropuerto de salida:</strong> {flight.origin}</p>
                  <p><strong>Aeropuerto de llegada:</strong> {flight.destination}</p>
                  <p><strong>Salida:</strong> {new Date(`${flight.departure_date}T${flight.salida_hora}`).toLocaleString()}</p>
                  <p><strong>Llegada:</strong> {new Date(`${flight.arrival_date}T${flight.llegada_hora}`).toLocaleString()}</p>
                  <p><strong>Capacidad:</strong> {flight.current_load}/{flight.capacity} ({((flight.current_load / flight.capacity) * 100).toFixed(2)}%)</p>
                </Itinerary>
              ))
            ) : (
              <p>No hay itinerarios en las últimas 6 horas.</p>
            )}
          </ItinerariesContainer>
        </Content>
        <ButtonsContainer>
          <Button onClick={downloadPDF}>Descargar Reporte en PDF</Button>
          <Button onClick={onRequestClose}>Cerrar</Button>
        </ButtonsContainer>
      </Container>
    </Modal>
  );
};

export default FinalSimulationPopup;
