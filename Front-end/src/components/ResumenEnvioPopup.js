import React, { useEffect, useState } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';
import axios from 'axios';

const ModalContent = styled.div`
  background: #fff;
  padding: 20px;
  max-width: 900px;
  margin: 40px auto;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
`;

const Header = styled.div`
  text-align: center;
  margin-bottom: 20px;
`;

const Content = styled.div`
  display: flex;
  justify-content: space-between;
`;

const FlightInfo = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex: 2;
`;

const FlightDetails = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const InfoBlock = styled.div`
  background: #f0f0f0;
  padding: 20px;
  border-radius: 10px;
  margin-left: 20px;
  flex: 1;
`;

const InfoTitle = styled.h4`
  margin-top: 0;
`;

const TimeInfo = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
  padding: 10px;
  background-color: #f0f0f0;
  border-radius: 5px;
`;

const ProgressBarContainer = styled.div`
  width: 100%;
  background-color: #e0e0e0;
  border-radius: 5px;
  overflow: hidden;
  margin-top: 10px;
`;

const ProgressBar = styled.div`
  width: ${(props) => props.progress}%;
  background-color: #76c7c0;
  height: 10px;
`;

const TimeBox = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
`;

const Table = styled.table`
  width: 100%;
  border-collapse: collapse;

  th, td {
    border: 1px solid #ddd;
    padding: 8px;
  }

  th {
    background-color: #f2f2f2;
    text-align: left;
  }
`;

const ResumenEnvioPopup = ({ isOpen, onRequestClose, envioId, data }) => {
  const [envio, setEnvio] = useState(null);
  const progreso = envio ? (envio.tiempoActivo / envio.tiempoEstimadoLlegada) * 100 : 0;

  useEffect(() => {
    const fetchEnvio = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/shipment/${envioId}`);
        const envioData = response.data;

        const origen = data.airports.find(airport => airport.id === envioData.origenId)?.codigoIATA || 'Desconocido';
        const destino = data.airports.find(airport => airport.id === envioData.destinoId)?.codigoIATA || 'Desconocido';
        const estado = data.estadoPaquete.find(estado => estado.id === envioData.tipo)?.estado || 'Desconocido';

        setEnvio({
          ...envioData,
          origen,
          destino,
          estado,
        });
      } catch (error) {
        console.error('Error fetching envio:', error);
      }
    };

    if (isOpen) {
      fetchEnvio();
    }
  }, [envioId, isOpen, data]);

  if (!envio) return null;

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      style={{
        overlay: {
          backgroundColor: 'rgba(0, 0, 0, 0.75)',
        },
        content: {
          top: '50%',
          left: '50%',
          right: 'auto',
          bottom: 'auto',
          marginRight: '-50%',
          transform: 'translate(-50%, -50%)',
          padding: 0,
          border: 'none',
          background: 'none',
        },
      }}
    >
      <ModalContent>
        <Header>
          <h2>Resumen de envío</h2>
          <h3>Envio {envio.id}</h3>
          <p>Estado actual: {envio.estado}</p>
        </Header>
        <Content>
          <div style={{ flex: 2 }}>
            <FlightInfo>
              <FlightDetails>
                <h4>{envio.origen}</h4>
                <p>{data.cities.find(city => city.id === envio.origenId)?.nombre || 'Desconocido'}</p>
                <p>{data.countries.find(country => country.id === envio.origenId)?.shortname || 'Desconocido'}</p>
                <p>PROGRAMADO para {new Date(envio.fechaInicio).toLocaleDateString()}</p>
              </FlightDetails>
              <FlightDetails>
                <h4>{envio.destino}</h4>
                <p>{data.cities.find(city => city.id === envio.destinoId)?.nombre || 'Desconocido'}</p>
                <p>{data.countries.find(country => country.id === envio.destinoId)?.shortname || 'Desconocido'}</p>
                <p>PROGRAMADO para {new Date(envio.fechaFin).toLocaleDateString()}</p>
              </FlightDetails>
            </FlightInfo>
            <TimeInfo>
              <TimeBox>
                <p>Tiempo activo: {envio.tiempoActivo} min</p>
              </TimeBox>
              <ProgressBarContainer>
                <ProgressBar progress={progreso} />
              </ProgressBarContainer>
            </TimeInfo>
            <Table>
              <thead>
                <tr>
                  <th>Punto de partida</th>
                  <th>Punto de destino</th>
                  <th>Hora de partida</th>
                  <th>Hora de llegada</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                {envio.itinerario?.map((item, index) => (
                  <tr key={index}>
                    <td>{item.partida}</td>
                    <td>{item.destino}</td>
                    <td>{item.horaPartida}</td>
                    <td>{item.horaLlegada}</td>
                    <td>{item.estado}</td>
                  </tr>
                )) || <tr><td colSpan="5">No hay itinerarios disponibles.</td></tr>}
              </tbody>
            </Table>
          </div>
          <InfoBlock>
            <InfoTitle>Cantidad de paquetes: {envio.cantidad}</InfoTitle>
          </InfoBlock>
        </Content>
      </ModalContent>
    </Modal>
  );
};

export default ResumenEnvioPopup;