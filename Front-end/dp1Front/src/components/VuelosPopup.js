import React, { useState } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';
import ResumenEnvioPopup from './ResumenEnvioPopup';

const ModalContent = styled.div`
  background: #fff;
  padding: 20px;
  max-width: 800px;
  margin: 40px auto;
  border-radius: 10px;
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

  tr {
    cursor: pointer;
  }
`;

const FiltersContainer = styled.div`
  margin-bottom: 20px;
`;

const FiltersTitle = styled.h3`
  margin: 0 0 10px 0;
  display: flex;
  align-items: center;
`;

const FilterIcon = styled.span`
  margin-left: 10px;
  transform: rotate(90deg);
  display: inline-block;
`;

const FilterInput = styled.input`
  padding: 5px;
  margin: 0 10px 10px 0;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 90px;
`;

const DateInput = styled.input`
  padding: 5px;
  margin: 0 10px 10px 0;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 120px;
`;

const FiltersRow = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: flex-start;
`;

const HeaderContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
`;

const ButtonsContainer = styled.div`
  display: flex;
  gap: 10px;
`;

const Button = styled.button`
  background-color: #6ba292;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;

  &:hover {
    background-color: #5a8a7d;
  }
`;

const EditButton = styled.button`
  background-color: #6ba292;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;

  &:hover {
    background-color: #5a8a7d;
  }
`;

const VuelosPopup = ({ isOpen, onRequestClose }) => {
  const [isResumenOpen, setIsResumenOpen] = useState(false);
  const [selectedVuelo, setSelectedVuelo] = useState(null);

  const handleRowClick = (vuelo) => {
    setSelectedVuelo(vuelo);
    setIsResumenOpen(true);
  };

  const handleCloseResumen = () => {
    setIsResumenOpen(false);
  };

  const vuelos = [
    {
      id: 1,
      origen: 'Miami',
      destino: 'Caracas',
      fechaPartida: '25/03/2024',
      horaPartida: '13:15',
      fechaLlegada: '25/03/2024',
      horaLlegada: '16:30',
      estado: 'Entregado',
      capacidad: '20/200',
      itinerario: [
        {
          partida: 'LIM',
          destino: 'MIA',
          horaPartida: '10:30',
          horaLlegada: '11:30',
          estado: 'Completado',
        },
        {
          partida: 'MIA',
          destino: 'DEC',
          horaPartida: '12:30',
          horaLlegada: '13:30',
          estado: 'En tránsito',
        },
        {
          partida: 'DEC',
          destino: 'TAE',
          horaPartida: '14:30',
          horaLlegada: '15:30',
          estado: 'Próximo',
        },
      ],
    },
  ];

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
        <HeaderContainer>
          <h2>Vuelos</h2>
          <ButtonsContainer>
            <Button>Importar vuelo(s)</Button>
          </ButtonsContainer>
        </HeaderContainer>
        <FiltersContainer>
          <FiltersTitle>
            Filtros <FilterIcon>▼</FilterIcon>
          </FiltersTitle>
          <FiltersRow>
            <FilterInput type="text" placeholder="#Vuelo" />
            <FilterInput type="text" placeholder="Origen" />
            <FilterInput type="text" placeholder="Destino" />
            <FilterInput type="text" placeholder="Estado" />
            <DateInput type="date" placeholder="Desde" />
            <DateInput type="date" placeholder="Hasta" />
          </FiltersRow>
        </FiltersContainer>
        <Table>
          <thead>
            <tr>
              <th>#Vuelo</th>
              <th>Origen</th>
              <th>Destino</th>
              <th>Fecha de partida</th>
              <th>Hora de partida</th>
              <th>Estado</th>
              <th>Capacidad</th>
            </tr>
          </thead>
          <tbody>
            {vuelos.map((vuelo) => (
              <tr key={vuelo.id} onClick={() => handleRowClick(vuelo)}>
                <td>{vuelo.id}</td>
                <td>{vuelo.origen}</td>
                <td>{vuelo.destino}</td>
                <td>{vuelo.fechaPartida}</td>
                <td>{vuelo.horaPartida}</td>
                <td>{vuelo.estado}</td>
                <td>{vuelo.capacidad}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </ModalContent>
      {selectedVuelo && (
        <ResumenEnvioPopup
          isOpen={isResumenOpen}
          onRequestClose={handleCloseResumen}
          envio={selectedVuelo}
        />
      )}
    </Modal>
  );
};

export default VuelosPopup;
