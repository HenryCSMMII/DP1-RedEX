import React, { useState } from 'react';
import styled, { css } from 'styled-components';
import AvionesAeropuertosPopup from './AvionesAeropuertosPopup';

const SidebarContainer = styled.div`
  width: ${({ isCollapsed }) => (isCollapsed ? '50px' : '300px')};
  background-color: #000;
  color: #fff;
  padding: 20px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  position: fixed;
  top: 0;
  right: 0;
  height: 100%;
  transition: width 0.3s ease;
`;

const Title = styled.h2`
  margin-bottom: 20px;
  display: ${({ isCollapsed }) => (isCollapsed ? 'none' : 'block')};
`;

const Label = styled.label`
  margin-bottom: 10px;
  display: block;
  font-weight: bold;
  display: ${({ isCollapsed }) => (isCollapsed ? 'none' : 'block')};
`;

const Input = styled.input`
  padding: 5px;
  margin-bottom: 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 100%;
  display: ${({ isCollapsed }) => (isCollapsed ? 'none' : 'block')};
`;

const Button = styled.button`
  padding: 10px 20px;
  margin-bottom: 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: #555;
  }

  ${props =>
    props.primary &&
    css`
      background-color: #6ba292;
      color: white;
    `}

  ${props =>
    props.danger &&
    css`
      background-color: #e74c3c;
      color: white;
    `}
`;

const TextArea = styled.textarea`
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 100%;
  height: 100px;
  resize: none;
  margin-bottom: 20px;
  box-sizing: border-box;
  display: ${({ isCollapsed }) => (isCollapsed ? 'none' : 'block')};
`;

const RadioGroup = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  display: ${({ isCollapsed }) => (isCollapsed ? 'none' : 'flex')};
`;

const CollapseButton = styled.button`
  background: none;
  border: none;
  color: #fff;
  cursor: pointer;
  margin-bottom: 10px;

  &:hover {
    color: #aaa;
  }
`;

const SimulacionSidebar = ({ isCollapsed, onCollapseToggle, onClose, onStartSimulation, onStopSimulation, data, tiempo_simulacion, planeSaturation, airportSaturation }) => {
  const [tipoSimulacion, setTipoSimulacion] = useState('diario');
  const [fecha, setFecha] = useState('');
  const [hora, setHora] = useState('');
  const [isAvionesAeropuertosOpen, setIsAvionesAeropuertosOpen] = useState(false);

  const handleStartSimulation = () => {
    const fechaInicio = `${fecha}T${hora}:00`;
    onStartSimulation(tipoSimulacion, fechaInicio);
  };

  const handleOpenAvionesAeropuertos = () => {
    setIsAvionesAeropuertosOpen(true);
  };

  const handleCloseAvionesAeropuertos = () => {
    setIsAvionesAeropuertosOpen(false);
  };

  return (
    <SidebarContainer isCollapsed={isCollapsed}>
      <CollapseButton onClick={onCollapseToggle}>
        {isCollapsed ? 'Expandir' : 'Contraer'}
      </CollapseButton>
      <Title isCollapsed={isCollapsed}>Simulación</Title>
      <Label isCollapsed={isCollapsed}>Fecha de inicio</Label>
      <Input type="date" value={fecha} onChange={(e) => setFecha(e.target.value)} isCollapsed={isCollapsed} />
      <Label isCollapsed={isCollapsed}>Hora de inicio</Label>
      <Input type="time" value={hora} onChange={(e) => setHora(e.target.value)} isCollapsed={isCollapsed} />
      <Label isCollapsed={isCollapsed}>Tipo de simulación</Label>
      <RadioGroup isCollapsed={isCollapsed}>
        <Label>
          <Input 
            type="radio" 
            name="tipoSimulacion" 
            value="diario" 
            checked={tipoSimulacion === 'diario'} 
            onChange={() => setTipoSimulacion('diario')} 
          />
          Diario
        </Label>
        <Label>
          <Input 
            type="radio" 
            name="tipoSimulacion" 
            value="semanal" 
            checked={tipoSimulacion === 'semanal'} 
            onChange={() => setTipoSimulacion('semanal')} 
          />
          Semanal
        </Label>
        <Label>
          <Input 
            type="radio" 
            name="tipoSimulacion" 
            value="colapso" 
            checked={tipoSimulacion === 'colapso'} 
            onChange={() => setTipoSimulacion('colapso')} 
          />
          Al colapso
        </Label>
      </RadioGroup>
      <Button primary onClick={handleStartSimulation} isCollapsed={isCollapsed}>Iniciar simulación</Button>
      <Button danger onClick={onStopSimulation} isCollapsed={isCollapsed}>Detener simulación</Button>
      {/* <Label isCollapsed={isCollapsed}>Resultado</Label>
      <TextArea readOnly isCollapsed={isCollapsed} />
      <Button isCollapsed={isCollapsed}>Ampliar resultado</Button> */}
      <p></p>
      <Label isCollapsed={isCollapsed}>Saturación de la flota de aviones:</Label>
      <p isCollapsed={isCollapsed}>{planeSaturation}%</p>
      <Label isCollapsed={isCollapsed}>Saturación de la flota de almacenes:</Label>
      <p isCollapsed={isCollapsed}>{airportSaturation}%</p>
      <AvionesAeropuertosPopup
        isOpen={isAvionesAeropuertosOpen}
        onRequestClose={handleCloseAvionesAeropuertos}
        data={data}
        tiempo_simulacion={tiempo_simulacion}
      />
    </SidebarContainer>
  );
};

export default SimulacionSidebar;
