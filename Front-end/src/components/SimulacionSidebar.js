import React, { useState } from 'react';
import styled, { css } from 'styled-components';
import AvionesAeropuertosPopup from './AvionesAeropuertosPopup';

const SidebarContainer = styled.div`
  width: 300px;
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
`;

const Title = styled.h2`
  margin-bottom: 20px;
`;

const Label = styled.label`
  margin-bottom: 10px;
  display: block;
  font-weight: bold;
`;

const Input = styled.input`
  padding: 5px;
  margin-bottom: 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 100%;
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
  box-sizing: border-box; /* Asegura que no se corte el contenido */
`;

const RadioGroup = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
`;

const SimulacionSidebar = ({ onClose, onStartSimulation, onStopSimulation, data, tiempo_simulacion }) => {
  const [tipoSimulacion, setTipoSimulacion] = useState('diario');
  const [fecha, setFecha] = useState('');
  const [hora, setHora] = useState('');
  const [isAvionesAeropuertosOpen, setIsAvionesAeropuertosOpen] = useState(false); // Estado para controlar el popup

  const handleStartSimulation = () => {
    const fechaInicio = `${fecha}T${hora}:00`;
    onStartSimulation(tipoSimulacion, fechaInicio);
  };

  const handleOpenAvionesAeropuertos = () => {
    console.log("Abriendo popup de Aviones y Aeropuertos");
    setIsAvionesAeropuertosOpen(true);
  };

  const handleCloseAvionesAeropuertos = () => {
    setIsAvionesAeropuertosOpen(false);
  };

  return (
    <SidebarContainer>
      <Title>Simulación</Title>
      <Label>Fecha de inicio</Label>
      <Input type="date" value={fecha} onChange={(e) => setFecha(e.target.value)} />
      <Label>Hora de inicio</Label>
      <Input type="time" value={hora} onChange={(e) => setHora(e.target.value)} />
      <Label>Tipo de simulación</Label>
      <RadioGroup>
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
      <Button primary onClick={handleStartSimulation}>Iniciar simulación</Button>
      <Button danger onClick={onStopSimulation}>Detener simulación</Button>
 
      <Label>Resultado</Label>
      <TextArea readOnly />
      <Button>Ampliar resultado</Button>

      {/* Popup para Aviones y Aeropuertos */}
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
