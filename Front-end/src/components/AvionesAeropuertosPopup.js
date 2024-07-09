import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';
import { parseISO } from 'date-fns';

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

const FilterInput = styled.input`
  padding: 5px;
  margin: 0 10px 10px 0;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: calc(50% - 15px);
`;

const FiltersRow = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: space-between;
`;

const HeaderContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
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

const AvionesAeropuertosPopup = ({ isOpen, onRequestClose, data, tiempo_simulacion }) => {
  const [filters, setFilters] = useState({
    avion: '',
    aeropuerto: '',
  });

  useEffect(() => {
    if (isOpen) {
      console.log("Popup abierto, datos recibidos:", data, tiempo_simulacion);
    }
  }, [isOpen]);

  if (!tiempo_simulacion) {
    return null;
  }

  const currentDateTime = parseISO(`${tiempo_simulacion.dia_actual}T${tiempo_simulacion.tiempo_actual}`);

  const filteredAviones = data.flights.filter((flight) => {
    const departureDateTime = parseISO(`${flight.departure_date}T${flight.departure_time}`);
    const arrivalDateTime = parseISO(`${flight.arrival_date}T${flight.arrival_time}`);
    return currentDateTime >= departureDateTime && currentDateTime <= arrivalDateTime &&
      flight.flight_number.toLowerCase().includes(filters.avion.toLowerCase());
  });

  const filteredAeropuertos = data.airports.filter((airport) =>
    airport.code.toLowerCase().includes(filters.aeropuerto.toLowerCase())
  );

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters((prevFilters) => ({
      ...prevFilters,
      [name]: value,
    }));
  };

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
          <h2>Aviones y Aeropuertos en Circulación</h2>
          <Button onClick={onRequestClose}>Cerrar</Button>
        </HeaderContainer>
        <FiltersContainer>
          <FiltersTitle>Filtros</FiltersTitle>
          <FiltersRow>
            <FilterInput
              type="text"
              placeholder="Avión"
              name="avion"
              value={filters.avion}
              onChange={handleFilterChange}
            />
            <FilterInput
              type="text"
              placeholder="Aeropuerto"
              name="aeropuerto"
              value={filters.aeropuerto}
              onChange={handleFilterChange}
            />
          </FiltersRow>
        </FiltersContainer>
        <h3>Aviones</h3>
        <Table>
          <thead>
            <tr>
              <th>Código</th>
              <th>Origen</th>
              <th>Destino</th>
              <th>Capacidad Actual</th>
            </tr>
          </thead>
          <tbody>
            {filteredAviones.map((avion) => (
              <tr key={avion.id}>
                <td>{avion.flight_number}</td>
                <td>{avion.origin}</td>
                <td>{avion.destination}</td>
                <td>{avion.current_load}/{avion.capacity}</td>
              </tr>
            ))}
          </tbody>
        </Table>
        <h3>Aeropuertos</h3>
        <Table>
          <thead>
            <tr>
              <th>Código</th>
              <th>Nombre</th>
              <th>Ciudad</th>
              <th>País</th>
              <th>Capacidad Actual</th>
            </tr>
          </thead>
          <tbody>
            {filteredAeropuertos.map((aeropuerto) => (
              <tr key={aeropuerto.id}>
                <td>{aeropuerto.code}</td>
                <td>{aeropuerto.name}</td>
                <td>{aeropuerto.city}</td>
                <td>{aeropuerto.country}</td>
                <td>{aeropuerto.current_capacity}/{aeropuerto.max_capacity}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </ModalContent>
    </Modal>
  );
};

export default AvionesAeropuertosPopup;
