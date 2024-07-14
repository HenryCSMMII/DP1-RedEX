import React from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';

const PopupContainer = styled.div`
  background: white;
  padding: 20px;
  border-radius: 10px;
  max-width: 500px;
  margin: 0 auto;
`;

const CloseButton = styled.button`
  background: #808080;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
  float: right;
`;

const ItineraryList = styled.ul`
  list-style-type: none;
  padding: 0;
`;

const ItineraryItem = styled.li`
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
`;

const FinalSimulationPopup = ({ isOpen, onRequestClose, itineraries }) => {
  return (
    <Modal isOpen={isOpen} onRequestClose={onRequestClose} ariaHideApp={false}>
      <PopupContainer>
        <h2>Itinerarios de los últimos 3 horas</h2>
        <CloseButton onClick={onRequestClose}>Cerrar</CloseButton>
        <ItineraryList>
          {itineraries.map((itinerary) => (
            <ItineraryItem key={itinerary.id}>
              <p><strong>ID del vuelo:</strong> {itinerary.id}</p>
              <p><strong>Origen:</strong> {itinerary.origin}</p>
              <p><strong>Destino:</strong> {itinerary.destination}</p>
              <p><strong>Fecha de salida:</strong> {itinerary.departure_date}</p>
              <p><strong>Hora de salida:</strong> {itinerary.departure_time}</p>
              <p><strong>Fecha de llegada:</strong> {itinerary.arrival_date}</p>
              <p><strong>Hora de llegada:</strong> {itinerary.arrival_time}</p>
            </ItineraryItem>
          ))}
        </ItineraryList>
      </PopupContainer>
    </Modal>
  );
};

export default FinalSimulationPopup;
