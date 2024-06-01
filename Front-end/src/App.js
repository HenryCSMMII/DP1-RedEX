import React, { useState, useEffect, useRef } from 'react';
import styled from 'styled-components';
import { GoogleMap, LoadScript } from '@react-google-maps/api';
import Sidebar from './components/Sidebar';
import Legend from './components/Legend';
import EnviosPopup from './components/EnviosPopup';
import NuevoEnvioPopup from './components/NuevoEnvioPopup';
import VuelosPopup from './components/VuelosPopup';
import AeropuertosPopup from './components/AeropuertosPopup';
import ReportesPopup from './components/ReportesPopup';
import SimulacionSidebar from './components/SimulacionSidebar';
import axios from 'axios';
import Modal from 'react-modal';

// Import the custom marker image
import redDot from './images/red-dot.png';

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

const MapContainer = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
`;

const libraries = ['places', 'marker'];

Modal.setAppElement('#root');

function App() {
  const [activePopup, setActivePopup] = useState('');
  const [isNuevoEnvioOpen, setIsNuevoEnvioOpen] = useState(false);
  const [data, setData] = useState({
    airports: [],
    cities: [],
    continents: [],
    countries: [],
    estadoPaquete: [],
    estadoVuelo: [],
  });
  const [loading, setLoading] = useState(true);
  const [isMapLoaded, setIsMapLoaded] = useState(false);
  const mapRef = useRef(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const airports = await axios.get('http://localhost:8080/airport/');
        const cities = await axios.get('http://localhost:8080/city/');
        const continents = await axios.get('http://localhost:8080/continent/');
        const countries = await axios.get('http://localhost:8080/country/');
        const estadoPaquete = await axios.get('http://localhost:8080/estadoPaquete/');
        const estadoVuelo = await axios.get('http://localhost:8080/estadoVuelo/');

        setData({
          airports: airports.data,
          cities: cities.data,
          continents: continents.data,
          countries: countries.data,
          estadoPaquete: estadoPaquete.data,
          estadoVuelo: estadoVuelo.data,
        });
        console.log('Airports data fetched:', airports.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  useEffect(() => {
    if (!loading && window.google && window.google.maps) {
      console.log('Google Maps API loaded');
    }
  }, [loading]);

  const handleMapLoad = async (map) => {
    mapRef.current = map;
    setIsMapLoaded(true);
    console.log('Map loaded');
  };

  const handleOpenPopup = (popupName) => {
    setActivePopup(popupName);
  };

  const handleClosePopup = () => {
    setActivePopup('');
  };

  const handleOpenNuevoEnvio = () => {
    setIsNuevoEnvioOpen(true);
  };

  const handleCloseNuevoEnvio = () => {
    setIsNuevoEnvioOpen(false);
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <AppContainer>
      <Sidebar 
        onEnviosClick={() => handleOpenPopup('Envios')}
        onVuelosClick={() => handleOpenPopup('Vuelos')}
        onAeropuertosClick={() => handleOpenPopup('Aeropuertos')}
        onReportesClick={() => handleOpenPopup('Reportes')}
        onSimulacionClick={() => handleOpenPopup('Simulacion')}
      />
      <Content>
        <MainContent>
          <MapContainer>
            <LoadScript
              googleMapsApiKey="AIzaSyD87S6pv73cvHVaw4wPsckU_7pLhlFlmN4" // Reemplaza con tu clave de API
              libraries={libraries}
              onLoad={handleMapLoad}
            >
              <GoogleMap
                mapContainerStyle={{ width: '100%', height: '100%' }}
                center={{ lat: -3.745, lng: -38.523 }}
                zoom={3}
                onLoad={handleMapLoad}
                mapId="56d2948ec3b0b447" // Reemplaza con tu Map ID
              >
                {isMapLoaded && mapRef.current && (
                  <>
                    <MarkerComponent
                      map={mapRef.current}
                      position={{ lat: -3.745, lng: -38.523 }}
                      title="Central Marker"
                      icon={redDot}
                    />
                    {data.airports.map((airport) => {
                      console.log(`Rendering marker for airport: ${airport.codigoIATA}, Lat: ${airport.latitude}, Lng: ${airport.longitude}`);
                      return (
                        <MarkerComponent
                          key={airport.id}
                          map={mapRef.current}
                          position={{ lat: airport.latitude, lng: airport.longitude }}
                          title={airport.codigoIATA}
                          icon={redDot}
                        />
                      );
                    })}
                  </>
                )}
              </GoogleMap>
            </LoadScript>
          </MapContainer>
          {activePopup === 'Simulacion' && <SimulacionSidebar onClose={handleClosePopup} />}
        </MainContent>
        <Legend />
      </Content>
      <EnviosPopup isOpen={activePopup === 'Envios'} onRequestClose={handleClosePopup} onAddEnvio={handleOpenNuevoEnvio} data={data} />
      <VuelosPopup isOpen={activePopup === 'Vuelos'} onRequestClose={handleClosePopup} data={data} />
      <AeropuertosPopup isOpen={activePopup === 'Aeropuertos'} onRequestClose={handleClosePopup} data={data} />
      <ReportesPopup isOpen={activePopup === 'Reportes'} onRequestClose={handleClosePopup} data={data} />
      <NuevoEnvioPopup isOpen={isNuevoEnvioOpen} onRequestClose={handleCloseNuevoEnvio} data={data} />
    </AppContainer>
  );
}

const MarkerComponent = ({ map, position, title, icon }) => {
  useEffect(() => {
    const addMarker = async () => {
      const { AdvancedMarkerElement } = await window.google.maps.importLibrary("marker");
      new AdvancedMarkerElement({
        map,
        position,
        title,
        icon: {
          url: icon,
          scaledSize: new window.google.maps.Size(32, 32)
        }
      });
    };
    addMarker();
  }, [map, position, title, icon]);

  return null;
};

export default App;
