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
  const [filteredAeropuertos, setFilteredAeropuertos] = useState([]);
  const [filters, setFilters] = useState({
    nombre: '',
    continente: ''
  });
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 5;

  useEffect(() => {
    if (isOpen) {
      filterAeropuertos();
    }
  }, [filters, data, isOpen]);

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({
      ...filters,
      [name]: value
    });
  };

  const filterAeropuertos = () => {
    if (!data.airports || !data.countries || !data.continents) return;

    let filtered = data.airports.map(aeropuerto => {
      const country = data.countries.find(country => country.id === aeropuerto.countryId);
      const continentName = data.continents.find(continent => continent.id === country.continentId)?.name.split('/')[0] || 'Desconocido';

      return {
        ...aeropuerto,
        country,
        continent: continentName
      };
    });

    if (filters.nombre) {
      filtered = filtered.filter(aeropuerto => aeropuerto.code?.toLowerCase().includes(filters.nombre.toLowerCase()));
    }
    if (filters.continente) {
      filtered = filtered.filter(aeropuerto => aeropuerto.continent.toLowerCase().includes(filters.continente.toLowerCase()));
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

  const getUniqueContinents = () => {
    const uniqueContinents = Array.from(new Set(data.countries.map(country => {
      const continent = data.continents.find(continent => continent.id === country.continentId);
      return continent.name.split('/')[0];
    })));
    return uniqueContinents;
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
        ) : (
          <>
            <HeaderContainer>
              <h2>Aeropuertos</h2>
              <ButtonsContainer>
                
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
                  {getUniqueContinents().map(continent => (
                    <option key={continent} value={continent}>{continent}</option>
                  ))}
                </Select>
              </FiltersRow>
            </FiltersContainer>
            <Table>
              <thead>
                <tr>
                  <th>Aeropuerto</th>
                  <th>País</th>
                  <th>Capacidad</th>
                </tr>
              </thead>
              <tbody>
                {currentItems.map((aeropuerto) => (
                  <tr key={aeropuerto.id}>
                    <td>{aeropuerto.code}</td>
                    <td>{aeropuerto.country?.name}</td>
                    <td>{aeropuerto.currentLoad}/{aeropuerto.max_capacity}</td>
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
        )}
      </ModalContent>
    </Modal>
  );
};

export default AeropuertosPopup;