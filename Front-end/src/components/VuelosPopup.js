import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';
import axios from 'axios';
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

const VuelosPopup = ({ isOpen, onRequestClose, data }) => {
  const [isResumenOpen, setIsResumenOpen] = useState(false);
  const [selectedVuelo, setSelectedVuelo] = useState(null);
  const [vuelos, setVuelos] = useState([]);
  const [filteredVuelos, setFilteredVuelos] = useState([]);
  const [filters, setFilters] = useState({
    vuelo: '',
    origen: '',
    destino: '',
    estado: '',
    fechaDesde: '',
    fechaHasta: ''
  });
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 5;

  const handleRowClick = (vuelo) => {
    setSelectedVuelo(vuelo);
    setIsResumenOpen(true);
  };

  const handleCloseResumen = () => {
    setIsResumenOpen(false);
  };

  useEffect(() => {
    const fetchVuelos = async () => {
      try {
        setLoading(true);
        const response = await axios.get('http://localhost:8080/vuelo/');
        setVuelos(response.data);
        setFilteredVuelos(response.data);
      } catch (error) {
        console.error('Error fetching vuelos:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchVuelos();
  }, []);

  useEffect(() => {
    filterVuelos();
  }, [filters, vuelos]);

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({
      ...filters,
      [name]: value
    });
  };

  const filterVuelos = () => {
    let filtered = vuelos;
    if (filters.vuelo) {
      filtered = filtered.filter(vuelo => vuelo.id.toString().includes(filters.vuelo));
    }
    if (filters.origen) {
      filtered = filtered.filter(vuelo => {
        const origin = vuelo.code.split('-')[0];
        return origin.toLowerCase().includes(filters.origen.toLowerCase());
      });
    }
    if (filters.destino) {
      filtered = filtered.filter(vuelo => {
        const destination = vuelo.code.split('-')[1];
        return destination.toLowerCase().includes(filters.destino.toLowerCase());
      });
    }
    if (filters.estado) {
      filtered = filtered.filter(vuelo => vuelo.state?.toLowerCase().includes(filters.estado.toLowerCase()));
    }
    if (filters.fechaDesde) {
      filtered = filtered.filter(vuelo => new Date(vuelo.departure_date_time) >= new Date(filters.fechaDesde));
    }
    if (filters.fechaHasta) {
      filtered = filtered.filter(vuelo => new Date(vuelo.departure_date_time) <= new Date(filters.fechaHasta));
    }

    setFilteredVuelos(filtered);
  };

  const totalPages = Math.ceil(filteredVuelos.length / itemsPerPage);

  const currentItems = filteredVuelos.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);

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
        },
      }}
    >
      <ModalContent>
        <HeaderContainer>
          <h2>Vuelos</h2>
        </HeaderContainer>
        <FiltersContainer>
          <FiltersTitle>
            Filtros <FilterIcon>▼</FilterIcon>
          </FiltersTitle>
          <FiltersRow>
            <FilterInput type="text" placeholder="#Vuelo" name="vuelo" value={filters.vuelo} onChange={handleFilterChange} />
            <FilterInput type="text" placeholder="Origen" name="origen" value={filters.origen} onChange={handleFilterChange} />
            <FilterInput type="text" placeholder="Destino" name="destino" value={filters.destino} onChange={handleFilterChange} />
            <FilterInput type="text" placeholder="Estado" name="estado" value={filters.estado} onChange={handleFilterChange} />
            <DateInput type="date" placeholder="Desde" name="fechaDesde" value={filters.fechaDesde} onChange={handleFilterChange} />
            <DateInput type="date" placeholder="Hasta" name="fechaHasta" value={filters.fechaHasta} onChange={handleFilterChange} />
          </FiltersRow>
        </FiltersContainer>
        {loading ? (
          <p>Cargando...</p>
        ) : (
          <>
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
                {currentItems.map((vuelo) => {
                  const [origin, destination] = vuelo.code.split('-');
                  return (
                    <tr key={vuelo.id} onClick={() => handleRowClick(vuelo)}>
                      <td>{vuelo.id}</td>
                      <td>{origin}</td>
                      <td>{destination}</td>
                      <td>{new Date(vuelo.departure_date_time).toLocaleDateString()}</td>
                      <td>{new Date(vuelo.departure_date_time).toLocaleTimeString()}</td>
                      <td>{vuelo.state || 'Desconocido'}</td>
                      <td>{vuelo.max_capacity}</td>
                    </tr>
                  );
                })}
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