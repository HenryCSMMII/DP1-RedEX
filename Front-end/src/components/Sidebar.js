import React from 'react';
import styled from 'styled-components';

const SidebarContainer = styled.div`
  width: 100%;
  background-color: #000;
  color: #fff;
  padding: 20px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;

  @media (min-width: 768px) {
    width: 250px;
    height: 100vh;
  }
`;

const Title = styled.h1`
  text-align: center;
  margin-bottom: 30px;
`;

const MenuItem = styled.div`
  padding: 10px 0;
  cursor: pointer;
  text-align: left;
  padding-left: 20px;

  &:hover {
    background-color: #333;
  }
`;

const Profile = styled.div`
  margin-top: auto;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const ProfileImage = styled.img`
  border-radius: 50%;
  margin-bottom: 10px;
`;

const Logout = styled.div`
  margin-top: 10px;
  cursor: pointer;
  color: #aaa;

  &:hover {
    color: #fff;
  }
`;

const Sidebar = ({ onNuevoEnvioClick, onVuelosClick, onAeropuertosClick, onReportesClick, onSimulacionClick }) => {
  return (
    <SidebarContainer>
      <Title>RedEx</Title>
      <MenuItem onClick={onNuevoEnvioClick}>Crear Envio</MenuItem>
      <MenuItem onClick={onVuelosClick}>Vuelos</MenuItem>
      <MenuItem onClick={onAeropuertosClick}>Aeropuertos</MenuItem>
      <MenuItem onClick={onReportesClick}>Reportes</MenuItem>
      <MenuItem onClick={onSimulacionClick}>Simulación</MenuItem>
      <Profile>
        <ProfileImage src="https://via.placeholder.com/40" alt="Profile" />
        <span>Raul Raulito</span>
        <Logout>Cerrar sesión</Logout>
      </Profile>
    </SidebarContainer>
  );
};

export default Sidebar;
