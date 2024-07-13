import React from 'react';
import styled from 'styled-components';

const SidebarContainer = styled.div`
  width: ${({ isCollapsed }) => (isCollapsed ? '50px' : '250px')};
  background-color: #000;
  color: #fff;
  padding: 20px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow-y: auto;
  transition: width 0.3s ease;

  @media (min-width: 768px) {
    width: ${({ isCollapsed }) => (isCollapsed ? '50px' : '250px')};
  }
`;

const Title = styled.h1`
  text-align: center;
  margin-bottom: 30px;
  display: ${({ isCollapsed }) => (isCollapsed ? 'none' : 'block')};
`;

const MenuItem = styled.div`
  padding: 10px 0;
  cursor: pointer;
  text-align: left;
  padding-left: 20px;

  &:hover {
    background-color: #333;
  }

  display: ${({ isCollapsed }) => (isCollapsed ? 'none' : 'block')};
`;

const Profile = styled.div`
  margin-top: auto;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  display: ${({ isCollapsed }) => (isCollapsed ? 'none' : 'flex')};
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

const Sidebar = ({ isCollapsed, onCollapseToggle, onNuevoEnvioClick, onVuelosClick, onAeropuertosClick, onReportesClick, onSimulacionClick }) => {
  return (
    <SidebarContainer isCollapsed={isCollapsed}>
      <CollapseButton onClick={onCollapseToggle}>
        {isCollapsed ? 'Expandir' : 'Contraer'}
      </CollapseButton>
      <Title isCollapsed={isCollapsed}>RedEx</Title>
      <MenuItem isCollapsed={isCollapsed} onClick={onNuevoEnvioClick}>Crear Envio</MenuItem>
      <MenuItem isCollapsed={isCollapsed} onClick={onSimulacionClick}>Simulación</MenuItem>
      <Profile isCollapsed={isCollapsed}>
        <ProfileImage src="https://via.placeholder.com/40" alt="Profile" />
        <span>Raul Raulito</span>
        <Logout>Cerrar sesión</Logout>
      </Profile>
    </SidebarContainer>
  );
};

export default Sidebar;
