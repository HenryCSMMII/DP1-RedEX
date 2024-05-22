import React, { useState } from 'react';
import styled from 'styled-components';
import Sidebar from './components/Sidebar';
import Legend from './components/Legend';
import EnviosPopup from './components/EnviosPopup';
import VuelosPopup from './components/VuelosPopup';
import AeropuertosPopup from './components/AeropuertosPopup';
import ReportesPopup from './components/ReportesPopup';
import SimulacionSidebar from './components/SimulacionSidebar';

const AppContainer = styled.div`
  display: flex;
  height: 100vh;
  flex-direction: column;

  @media (min-width: 768px) {
    flex-direction: row;
  }
`;

const Content = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;

const MainContent = styled.div`
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
`;

function App() {
  const [isEnviosPopupOpen, setIsEnviosPopupOpen] = useState(false);
  const [isVuelosPopupOpen, setIsVuelosPopupOpen] = useState(false);
  const [isAeropuertosPopupOpen, setIsAeropuertosPopupOpen] = useState(false);
  const [isReportesPopupOpen, setIsReportesPopupOpen] = useState(false);
  const [isSimulacionSidebarOpen, setIsSimulacionSidebarOpen] = useState(false);

  const handleOpenEnviosPopup = () => {
    setIsEnviosPopupOpen(true);
    setIsSimulacionSidebarOpen(false);
  };

  const handleCloseEnviosPopup = () => {
    setIsEnviosPopupOpen(false);
  };

  const handleOpenVuelosPopup = () => {
    setIsVuelosPopupOpen(true);
    setIsSimulacionSidebarOpen(false);
  };

  const handleCloseVuelosPopup = () => {
    setIsVuelosPopupOpen(false);
  };

  const handleOpenAeropuertosPopup = () => {
    setIsAeropuertosPopupOpen(true);
    setIsSimulacionSidebarOpen(false);
  };

  const handleCloseAeropuertosPopup = () => {
    setIsAeropuertosPopupOpen(false);
  };

  const handleOpenReportesPopup = () => {
    setIsReportesPopupOpen(true);
    setIsSimulacionSidebarOpen(false);
  };

  const handleCloseReportesPopup = () => {
    setIsReportesPopupOpen(false);
  };

  const handleOpenSimulacionSidebar = () => {
    setIsSimulacionSidebarOpen(true);
    setIsEnviosPopupOpen(false);
    setIsVuelosPopupOpen(false);
    setIsAeropuertosPopupOpen(false);
    setIsReportesPopupOpen(false);
  };

  const handleCloseSimulacionSidebar = () => {
    setIsSimulacionSidebarOpen(false);
  };

  return (
    <AppContainer>
      <Sidebar 
        onEnviosClick={handleOpenEnviosPopup} 
        onVuelosClick={handleOpenVuelosPopup}
        onAeropuertosClick={handleOpenAeropuertosPopup}
        onReportesClick={handleOpenReportesPopup}
        onSimulacionClick={handleOpenSimulacionSidebar}
      />
      <Content>
        <MainContent>
          {/* Aquí se insertaría el mapa */}
          {isSimulacionSidebarOpen && <SimulacionSidebar onClose={handleCloseSimulacionSidebar} />}
        </MainContent>
        <Legend />
      </Content>
      <EnviosPopup isOpen={isEnviosPopupOpen} onRequestClose={handleCloseEnviosPopup} />
      <VuelosPopup isOpen={isVuelosPopupOpen} onRequestClose={handleCloseVuelosPopup} />
      <AeropuertosPopup isOpen={isAeropuertosPopupOpen} onRequestClose={handleCloseAeropuertosPopup} />
      <ReportesPopup isOpen={isReportesPopupOpen} onRequestClose={handleCloseReportesPopup} />
    </AppContainer>
  );
}

export default App;
