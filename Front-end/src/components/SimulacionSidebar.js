import React, { useState } from 'react';
import styled from 'styled-components';

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

const Select = styled.select`
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

  ${props => props.primary && `
    background-color: #6ba292;
    color: white;
  `}

  ${props => props.danger && `
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
`;

const RadioGroup = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
`;

const N_HORAS = 5;

const SimulacionSidebar = ({ onClose, onStartSimulation }) => {
  const [tipoSimulacion, setTipoSimulacion] = useState('diario');
  const [fechaInicio, setFechaInicio] = useState('');

  const handleStartSimulation = () => {
    if (tipoSimulacion === 'semanal') {
      const payload = {
        nHoras: N_HORAS.toString(),
        fecha_inicio: fechaInicio,
      };
      onStartSimulation(tipoSimulacion, payload);
    } else {
      onStartSimulation(tipoSimulacion);
    }
  };

  return (
    <SidebarContainer>
      <Title>Simulación</Title>
      <Label>Fecha de inicio</Label>
      <Input type="date" value={fechaInicio} onChange={(e) => setFechaInicio(e.target.value)} />
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
      <Button danger>Detener simulación</Button>
      <Label>Consola de sucesos</Label>
      <TextArea readOnly />
      <Label>Resultado</Label>
      <TextArea readOnly />
      <Button>Ampliar resultado</Button>
    </SidebarContainer>
  );
};

export default SimulacionSidebar;
