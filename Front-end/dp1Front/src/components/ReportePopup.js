import React from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';

const ModalContent = styled.div`
  background: #fff;
  padding: 20px;
  max-width: 1200px; /* Ajusta el ancho según sea necesario */
  width: 90%;
  margin: 40px auto;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
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
  width: 200px; /* Ajustar el ancho */
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

const ChartContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: space-between;

  > div {
    flex: 1;
    min-width: 250px; /* Ajusta el ancho mínimo de cada gráfico */
    max-width: 48%; /* Ajusta el ancho máximo de cada gráfico */
  }
`;

const ReportesPopup = ({ isOpen, onRequestClose }) => {
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
        },
      }}
    >
      <ModalContent>
        <HeaderContainer>
          <h2>Reportes</h2>
        </HeaderContainer>
        <FiltersContainer>
          <FiltersTitle>
            Filtros <FilterIcon>▼</FilterIcon>
          </FiltersTitle>
          <FiltersRow>
            <FilterInput type="date" placeholder="Fecha de inicio" />
            <FilterInput type="date" placeholder="Fecha de fin" />
          </FiltersRow>
        </FiltersContainer>
        <ChartContainer>
          <div>
            <h3>Total de envíos por día</h3>
            <img src="https://via.placeholder.com/300x200" alt="Total de envíos por día" />
          </div>
          <div>
            <h3>Destinos más frecuentes</h3>
            <img src="https://via.placeholder.com/300x200" alt="Destinos más frecuentes" />
          </div>
          <div>
            <h3>Volumen de envío por categoría de paquete</h3>
            <img src="https://via.placeholder.com/300x200" alt="Volumen de envío por categoría de paquete" />
          </div>
          <div>
            <h3>Tasa de cumplimiento de entrega a tiempo</h3>
            <img src="https://via.placeholder.com/300x200" alt="Tasa de cumplimiento de entrega a tiempo" />
          </div>
        </ChartContainer>
      </ModalContent>
    </Modal>
  );
};

export default ReportesPopup;
