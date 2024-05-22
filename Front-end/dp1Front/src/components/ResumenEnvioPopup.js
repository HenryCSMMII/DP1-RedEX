import React from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';

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

const ResumenEnvioPopup = ({ isOpen, onRequestClose, envio }) => {
  const progreso = (envio.tiempoTranscurrido / envio.tiempoEstimadoLlegada) * 100;

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
                <p>Colombia</p>
                <p>EST(UTC -05:00)</p>
                <p>PROGRAMADO para {envio.fechaPartida} a las {envio.horaPartida}</p>
              </FlightDetails>
              <FlightDetails>
                <h4>{envio.destino}</h4>
                <p>Estados Unidos</p>
                <p>EST(UTC -05:00)</p>
                <p>PROGRAMADO para {envio.fechaLlegada} a las {envio.horaLlegada}</p>
              </FlightDetails>
            </FlightInfo>
            <TimeInfo>
              <TimeBox>
                <p>Tiempo transcurrido: {envio.tiempoTranscurrido} min</p>
                <p>Tiempo estimado de llegada: {envio.tiempoEstimadoLlegada} min</p>
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
                {envio.itinerario.map((item, index) => (
                  <tr key={index}>
                    <td>{item.partida}</td>
                    <td>{item.destino}</td>
                    <td>{item.horaPartida}</td>
                    <td>{item.horaLlegada}</td>
                    <td>{item.estado}</td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </div>
          <InfoBlock>
            <InfoTitle>Cantidad de paquetes: {envio.cantidadPaquetes} paquetes</InfoTitle>
            <InfoTitle>Información del remitente:</InfoTitle>
            {envio.remitente && (
              <>
                <p>Nombre: {envio.remitente.nombre}</p>
                <p>Correo electrónico: {envio.remitente.email}</p>
                <p>Teléfono: {envio.remitente.telefono}</p>
                <p>Tipo de documento: {envio.remitente.tipoDocumento}</p>
                <p>Número de documento: {envio.remitente.numeroDocumento}</p>
              </>
            )}
            <InfoTitle>Información del destinatario:</InfoTitle>
            {envio.destinatario && (
              <>
                <p>Nombre: {envio.destinatario.nombre}</p>
                <p>Correo electrónico: {envio.destinatario.email}</p>
                <p>Teléfono: {envio.destinatario.telefono}</p>
                <p>Tipo de documento: {envio.destinatario.tipoDocumento}</p>
                <p>Número de documento: {envio.destinatario.numeroDocumento}</p>
              </>
            )}
          </InfoBlock>
        </Content>
      </ModalContent>
    </Modal>
  );
};

export default ResumenEnvioPopup;
