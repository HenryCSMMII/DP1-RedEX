import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';

const ModalContent = styled.div`
  background: #fff;
  padding: 20px;
  max-width: 1000px;
  width: 90%;
  margin: 40px auto;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  overflow-x: hidden; /* Evitar el desbordamiento horizontal */
`;

const Table = styled.table`
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;

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
  flex: 1;
  min-width: 100px;
`;

const Select = styled.select`
  padding: 5px;
  margin: 0 10px 10px 0;
  border: 1px solid #ccc;
  border-radius: 4px;
  flex: 1;
  min-width: 100px;
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

const DetailsContainer = styled.div`
  display: flex;
  flex-direction: column;
  background: #f9f9f9;
  padding: 20px;
  border-radius: 10px;
  margin-top: 20px;
`;

const DetailItem = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
`;

const RecepcionesTable = styled.table`
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

const PopupHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
`;

const PopupTitle = styled.h2`
  margin: 0;
`;

const StatusDot = styled.div`
  width: 10px;
  height: 10px;
  background-color: ${props => props.saturated ? 'red' : 'green'};
  border-radius: 50%;
`;

const BackButton = styled.button`
  background-color: #ccc;
  color: black;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
  margin-bottom: 20px;

  &:hover {
    background-color: #bbb;
  }
`;

const PaginationContainer = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 10px;
`;

const PaginationButton = styled.button`
  background-color: #6ba292;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
  margin: 0 5px;

  &:hover {
    background-color: #5a8a7d;
  }

  &:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }
`;

const AeropuertosPopup = ({ isOpen, onRequestClose, data }) => {
  const [isDetailsOpen, setIsDetailsOpen] = useState(false);
  const [selectedAeropuerto, setSelectedAeropuerto] = useState(null);
  const [filteredAeropuertos, setFilteredAeropuertos] = useState([]);
  const [filters, setFilters] = useState({
    nombre: '',
    continente: '',
    capacidad: ''
  });
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 5;

  const handleRowClick = (aeropuerto) => {
    setSelectedAeropuerto(aeropuerto);
    setIsDetailsOpen(true);
  };

  const handleBackClick = () => {
    setIsDetailsOpen(false);
    setSelectedAeropuerto(null);
  };

  useEffect(() => {
    if (data.airports.length > 0 && data.cities.length > 0 && data.countries.length > 0 && data.continents.length > 0) {
      filterAeropuertos();
    }
  }, [filters, data]);

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({
      ...filters,
      [name]: value
    });
  };

  const filterAeropuertos = () => {
    let filtered = data.airports.map(airport => {
      const city = data.cities.find(city => city.id === airport.cityId);
      const country = data.countries.find(country => country.id === city.countryId);
      const continent = data.continents.find(continent => continent.id === country.continentId);

      return {
        ...airport,
        city,
        country,
        continent
      };
    });

    if (filters.nombre) {
      filtered = filtered.filter(aeropuerto => aeropuerto.codigoIATA?.toLowerCase().includes(filters.nombre.toLowerCase()));
    }
    if (filters.continente) {
      filtered = filtered.filter(aeropuerto => aeropuerto.continent.name?.toLowerCase().includes(filters.continente.toLowerCase()));
    }
    if (filters.capacidad) {
      filtered = filtered.filter(aeropuerto => {
        const used = aeropuerto.currentLoad || 0;
        const total = aeropuerto.capacity || 1; // default value to avoid division by zero
        switch (filters.capacidad) {
          case 'Baja':
            return used / total < 0.33;
          case 'Media':
            return used / total >= 0.33 && used / total <= 0.66;
          case 'Alta':
            return used / total > 0.66;
          default:
            return true;
        }
      });
    }

    setFilteredAeropuertos(filtered);
  };

  const totalPages = Math.ceil(filteredAeropuertos.length / itemsPerPage);

  const currentItems = filteredAeropuertos.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);

  const handlePreviousPage = () => {
    setCurrentPage(prev => Math.max(prev - 1, 1));
  };

  const handleNextPage = () => {
    setCurrentPage(prev => Math.min(prev + 1, totalPages));
  };

  if (!isOpen) return null;

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
          width: '1000px', // Aumentar el ancho del contenido del modal
          maxWidth: '1000px', // Asegurar que no se sobrepase este ancho
          overflowX: 'hidden', // Evitar el desbordamiento horizontal
        },
      }}
    >
      <ModalContent>
        {filteredAeropuertos.length === 0 ? (
          <p>Cargando...</p>
        ) : !isDetailsOpen ? (
          <>
            <HeaderContainer>
              <h2>Aeropuertos</h2>
              <ButtonsContainer>
                <Button>Importar aeropuerto(s)</Button>
              </ButtonsContainer>
            </HeaderContainer>
            <FiltersContainer>
              <FiltersTitle>
                Filtros <FilterIcon>▼</FilterIcon>
              </FiltersTitle>
              <FiltersRow>
                <FilterInput type="text" placeholder="Nombre del aeropuerto" name="nombre" value={filters.nombre} onChange={handleFilterChange} />
                <Select name="continente" value={filters.continente} onChange={handleFilterChange}>
                  <option value="">Continente</option>
                  {data.continents.map(continent => (
                    <option key={continent.id} value={continent.name}>{continent.name}</option>
                  ))}
                </Select>
                <Select name="capacidad" value={filters.capacidad} onChange={handleFilterChange}>
                  <option value="">Capacidad</option>
                  <option value="Baja">Baja</option>
                  <option value="Media">Media</option>
                  <option value="Alta">Alta</option>
                </Select>
              </FiltersRow>
            </FiltersContainer>
            <Table>
              <thead>
                <tr>
                  <th>Aeropuerto</th>
                  <th>País</th>
                  <th>Ciudad</th>
                  <th>Capacidad</th>
                </tr>
              </thead>
              <tbody>
                {currentItems.map((aeropuerto) => (
                  <tr key={aeropuerto.id} onClick={() => handleRowClick(aeropuerto)}>
                    <td>{aeropuerto.codigoIATA}</td>
                    <td>{aeropuerto.country.name}</td>
                    <td>{aeropuerto.city.nombre}</td>
                    <td>{aeropuerto.currentLoad}/{aeropuerto.capacity}</td>
                  </tr>
                ))}
              </tbody>
            </Table>
            <PaginationContainer>
              <PaginationButton onClick={handlePreviousPage} disabled={currentPage === 1}>
                Anterior
              </PaginationButton>
              <PaginationButton onClick={handleNextPage} disabled={currentPage === totalPages}>
                Siguiente
              </PaginationButton>
            </PaginationContainer>
          </>
        ) : (
          <DetailsContainer>
            <BackButton onClick={handleBackClick}>Atrás</BackButton>
            <PopupHeader>
              <PopupTitle>Aeropuerto {selectedAeropuerto.codigoIATA}</PopupTitle>
              <StatusDot saturated={selectedAeropuerto.saturated} />
            </PopupHeader>
            <DetailItem>
              <span>Almacen: {selectedAeropuerto.currentLoad}/{selectedAeropuerto.capacity}</span>
              <span>Saturado: {selectedAeropuerto.saturated ? 'Sí' : 'No'}</span>
            </DetailItem>
            <h3>Últimas recepciones</h3>
            <RecepcionesTable>
              <thead>
                <tr>
                  <th>#Paquete</th>
                  <th>#Envio</th>
                  <th>Hora de llegada</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                {selectedAeropuerto.recepciones.map((recepcion, index) => (
                  <tr key={index}>
                    <td>{recepcion.idPaquete}</td>
                    <td>{recepcion.idEnvio}</td>
                    <td>{recepcion.horaLlegada}</td>
                    <td>{recepcion.estado}</td>
                  </tr>
                ))}
              </tbody>
            </RecepcionesTable>
          </DetailsContainer>
        )}
      </ModalContent>
    </Modal>
  );
};

export default AeropuertosPopup;