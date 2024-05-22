import React, { useState } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';

const ModalContent = styled.div`
  background: #fff;
  padding: 20px;
  max-width: 1000px; /* Aumentar el ancho máximo */
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

const AeropuertosPopup = ({ isOpen, onRequestClose }) => {
  const [isDetailsOpen, setIsDetailsOpen] = useState(false);
  const [selectedAeropuerto, setSelectedAeropuerto] = useState(null);

  const handleRowClick = (aeropuerto) => {
    setSelectedAeropuerto(aeropuerto);
    setIsDetailsOpen(true);
  };

  const handleBackClick = () => {
    setIsDetailsOpen(false);
    setSelectedAeropuerto(null);
  };

  const aeropuertos = [
    {
      id: 1,
      nombre: 'LIM',
      pais: 'Perú',
      ciudad: 'LIMA',
      capacidad: '40/50',
      saturado: true,
      recepciones: [
        {
          idPaquete: 32,
          idEnvio: 145,
          horaLlegada: '11:30',
          estado: 'Completado',
        },
        {
          idPaquete: 32,
          idEnvio: 145,
          horaLlegada: '11:30',
          estado: 'En tránsito',
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
          width: '1000px', // Aumentar el ancho del contenido del modal
          maxWidth: '1000px', // Asegurar que no se sobrepase este ancho
          overflowX: 'hidden', // Evitar el desbordamiento horizontal
        },
      }}
    >
      <ModalContent>
        {!isDetailsOpen && (
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
                <FilterInput type="text" placeholder="Nombre del aeropuerto" />
                <Select>
                  <option>Continente</option>
                  <option>América</option>
                  <option>Europa</option>
                  <option>Asia</option>
                  <option>África</option>
                  <option>Oceanía</option>
                </Select>
                <Select>
                  <option>Capacidad</option>
                  <option>Baja</option>
                  <option>Media</option>
                  <option>Alta</option>
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
                {aeropuertos.map((aeropuerto) => (
                  <tr key={aeropuerto.id} onClick={() => handleRowClick(aeropuerto)}>
                    <td>{aeropuerto.nombre}</td>
                    <td>{aeropuerto.pais}</td>
                    <td>{aeropuerto.ciudad}</td>
                    <td>{aeropuerto.capacidad}</td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </>
        )}
        {isDetailsOpen && selectedAeropuerto && (
          <DetailsContainer>
            <BackButton onClick={handleBackClick}>Atrás</BackButton>
            <PopupHeader>
              <PopupTitle>Aeropuerto {selectedAeropuerto.nombre}</PopupTitle>
              <StatusDot saturated={selectedAeropuerto.saturado} />
            </PopupHeader>
            <DetailItem>
              <span>Almacen: {selectedAeropuerto.capacidad}</span>
              <span>Saturado: {selectedAeropuerto.saturado ? 'Sí' : 'No'}</span>
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