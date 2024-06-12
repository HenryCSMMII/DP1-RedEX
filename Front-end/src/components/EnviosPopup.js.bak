import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';
import axios from 'axios';
import ResumenEnvioPopup from './ResumenEnvioPopup';
import NuevoEnvioPopup from './NuevoEnvioPopup';

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

const EnviosPopup = ({ isOpen, onRequestClose, data, onAddEnvio }) => {
  const [isResumenOpen, setIsResumenOpen] = useState(false);
  const [isNuevoEnvioOpen, setIsNuevoEnvioOpen] = useState(false);
  const [selectedEnvio, setSelectedEnvio] = useState(null);
  const [envios, setEnvios] = useState([]);
  const [filteredEnvios, setFilteredEnvios] = useState([]);
  const [filters, setFilters] = useState({
    numEnvio: '',
    origen: '',
    destino: '',
    estado: '',
    desde: '',
    hasta: ''
  });

  const handleRowClick = (envio) => {
    setSelectedEnvio(envio);
    setIsResumenOpen(true);
  };

  const handleCloseResumen = () => {
    setIsResumenOpen(false);
  };

  const handleEditClick = (e, envio) => {
    e.stopPropagation();
    setSelectedEnvio(envio);
    setIsNuevoEnvioOpen(true);
  };

  const handleAgregarClick = () => {
    setSelectedEnvio(null);
    setIsNuevoEnvioOpen(true);
  };

  const handleCloseNuevoEnvio = () => {
    setIsNuevoEnvioOpen(false);
  };

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({
      ...filters,
      [name]: value
    });
  };

  useEffect(() => {
    const fetchEnvios = async () => {
      try {
        const response = await axios.get('http://localhost:5000/shipment/');
        setEnvios(response.data);
        setFilteredEnvios(response.data);
      } catch (error) {
        console.error('Error fetching envios:', error);
      }
    };
    fetchEnvios();
  }, []);

  useEffect(() => {
    filterEnvios();
  }, [filters, envios]);

  const filterEnvios = () => {
    let filtered = envios;

    if (filters.numEnvio) {
      filtered = filtered.filter(envio => envio.id.toString().includes(filters.numEnvio));
    }
    if (filters.origen) {
      filtered = filtered.filter(envio => {
        const origen = data.airports.find(airport => airport.id === envio.departureAirportId)?.code || '';
        return origen.toLowerCase().includes(filters.origen.toLowerCase());
      });
    }
    if (filters.destino) {
      filtered = filtered.filter(envio => {
        const destino = data.airports.find(airport => airport.id === envio.arrivalAirportId)?.code || '';
        return destino.toLowerCase().includes(filters.destino.toLowerCase());
      });
    }
    if (filters.estado) {
      filtered = filtered.filter(envio => envio.state?.toLowerCase().includes(filters.estado.toLowerCase()));
    }
    if (filters.desde) {
      filtered = filtered.filter(envio => new Date(envio.departureTime) >= new Date(filters.desde));
    }
    if (filters.hasta) {
      filtered = filtered.filter(envio => new Date(envio.arrivalTime) <= new Date(filters.hasta));
    }

    setFilteredEnvios(filtered);
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
          <h2>Envios</h2>
          <ButtonsContainer>
            <Button>Importar envio(s)</Button>
            <Button onClick={handleAgregarClick}>Agregar envio</Button>
          </ButtonsContainer>
        </HeaderContainer>
        <FiltersContainer>
          <FiltersTitle>
            Filtros <FilterIcon>▼</FilterIcon>
          </FiltersTitle>
          <FiltersRow>
            <FilterInput type="text" placeholder="Núm. Envio" name="numEnvio" value={filters.numEnvio} onChange={handleFilterChange} />
            <FilterInput type="text" placeholder="Origen" name="origen" value={filters.origen} onChange={handleFilterChange} />
            <FilterInput type="text" placeholder="Destino" name="destino" value={filters.destino} onChange={handleFilterChange} />
            <FilterInput type="text" placeholder="Estado" name="estado" value={filters.estado} onChange={handleFilterChange} />
            <DateInput type="date" placeholder="Desde" name="desde" value={filters.desde} onChange={handleFilterChange} />
            <DateInput type="date" placeholder="Hasta" name="hasta" value={filters.hasta} onChange={handleFilterChange} />
          </FiltersRow>
        </FiltersContainer>
        <Table>
          <thead>
            <tr>
              <th>#Envio</th>
              <th>Origen</th>
              <th>Destino</th>
              <th>Fecha de partida</th>
              <th>Hora de partida</th>
              <th>#Paquetes</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {filteredEnvios.map((envio) => (
              <tr key={envio.id} onClick={() => handleRowClick(envio)}>
                <td>{envio.id}</td>
                <td>{data.airports.find(airport => airport.id === envio.departureAirportId)?.code || 'Desconocido'}</td>
                <td>{data.airports.find(airport => airport.id === envio.arrivalAirportId)?.code || 'Desconocido'}</td>
                <td>{envio.departureTime ? new Date(envio.departureTime).toLocaleDateString() : 'N/A'}</td>
                <td>{envio.arrivalTime ? new Date(envio.arrivalTime).toLocaleDateString() : 'N/A'}</td>
                <td>{envio.packageQuantity}</td>
                <td>{envio.state || 'Desconocido'}</td>
                <td onClick={(e) => handleEditClick(e, envio)}>
                  <EditButton>Editar</EditButton>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </ModalContent>
      {selectedEnvio && (
        <ResumenEnvioPopup
          isOpen={isResumenOpen}
          onRequestClose={handleCloseResumen}
          envioId={selectedEnvio?.id}
          data={data}
        />
      )}
      <NuevoEnvioPopup
        isOpen={isNuevoEnvioOpen}
        onRequestClose={handleCloseNuevoEnvio}
        data={data}
      />
    </Modal>
  );
};

export default EnviosPopup;