import React, { useRef, useEffect, useState } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';
import { Bar } from 'react-chartjs-2';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';

const Container = styled.div`
  padding: 20px;
  max-width: 800px;
  margin: auto;
  text-align: center;
`;

const Title = styled.h2`
  margin-bottom: 15px;
  font-size: 1.5em;
`;

const Itinerary = styled.div`
  margin-bottom: 10px;
  text-align: left;
`;

const Button = styled.button`
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  &:hover {
    background-color: #0056b3;
  }
  display: block;
  margin: 10px auto;
`;

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
    html2canvas(input)
      .then((canvas) => {
        const imgData = canvas.toDataURL('image/png');
        const pdf = new jsPDF();
        const imgProps = pdf.getImageProperties(imgData);
        const pdfWidth = pdf.internal.pageSize.getWidth();
        const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;
        pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
        pdf.save('reporte_vuelos.pdf');
      })
      .catch((error) => console.error('Error generating PDF: ', error));
  };

  return (
    <Modal isOpen={isOpen} onRequestClose={onRequestClose} style={{ content: { inset: 'auto', margin: 'auto', maxWidth: '820px', textAlign: 'center' } }}>
      <Container ref={reportRef}>
        <Title>Reporte de Itinerarios de Vuelos</Title>
        {currentItineraries.length > 0 ? (
          <>
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
            <Title>Vuelos en Curso</Title>
            {currentItineraries.map((flight, index) => (
              <Itinerary key={index}>
                <p><strong>ID del vuelo:</strong> {flight.id}</p>
                <p><strong>Aeropuerto de salida:</strong> {flight.origin}</p>
                <p><strong>Aeropuerto de llegada:</strong> {flight.destination}</p>
                <p><strong>Salida:</strong> {new Date(`${flight.departure_date}T${flight.salida_hora}`).toLocaleString()}</p>
                <p><strong>Llegada:</strong> {new Date(`${flight.arrival_date}T${flight.llegada_hora}`).toLocaleString()}</p>
                <p><strong>Capacidad:</strong> {flight.current_load}/{flight.capacity} ({((flight.current_load / flight.capacity) * 100).toFixed(2)}%)</p>
              </Itinerary>
            ))}
          </>
        ) : (
          <p>No hay vuelos en curso.</p>
        )}
        <Title>Vuelos de las Últimas 6 Horas</Title>
        {itineraries.length > 0 ? (
          itineraries.map((flight, index) => (
            <Itinerary key={index}>
              <p><strong>ID del vuelo:</strong> {flight.id}</p>
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
        <Button onClick={downloadPDF}>Descargar Reporte en PDF</Button>
        <Button onClick={onRequestClose}>Cerrar</Button>
      </Container>
    </Modal>
  );
};

export default FinalSimulationPopup;
