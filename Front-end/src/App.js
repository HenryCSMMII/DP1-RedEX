import React, { useState, useEffect, useRef } from 'react';
import styled from 'styled-components';
import { GoogleMap, LoadScript, MarkerF, Polyline } from '@react-google-maps/api';
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
import redDot from './images/red-dot.png';
import planeIcon from './images/plane.png'; // Añade un icono de avión

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
  position: relative;
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
    flights: [
      {
        id: 1693,
        activo: 1,
        fecha_creacion: "2024-05-31 15:40:56",
        fecha_modificacion: "2024-05-31 15:40:56",
        arrival_time: "16:00:00",
        capacity: 180,
        current_load: 150,
        departure_time: "08:00:00",
        destination: "SKBO",
        duration: 480,
        flight_number: "AA100",
        origin: "SABE",
        estado_vuelo_id: 1,
        arrival_date: "2024-06-02",
        departure_date: "2024-06-01"
      },
      {
        id: 1694,
        activo: 1,
        fecha_creacion: "2024-06-01 10:00:00",
        fecha_modificacion: "2024-06-01 10:00:00",
        arrival_time: "18:00:00",
        capacity: 200,
        current_load: 160,
        departure_time: "10:00:00",
        destination: "LDZA",
        duration: 480,
        flight_number: "UA101",
        origin: "ZBAA",
        estado_vuelo_id: 1,
        arrival_date: "2024-06-03",
        departure_date: "2024-06-01"
      }
    ]
  });
  const [loading, setLoading] = useState(true);
  const [isMapLoaded, setIsMapLoaded] = useState(false);
  const mapRef = useRef(null);

  const [tiempo_simulacion, setTiempoSimulacion] = useState({
    dia_actual: "2024-06-02",
    tiempo_actual: "06:00:00"
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const airports = await axios.get('http://localhost:8080/airport/');
        const cities = await axios.get('http://localhost:8080/city/');
        const continents = await axios.get('http://localhost:8080/continent/');
        const countries = await axios.get('http://localhost:8080/country/');
        const estadoPaquete = await axios.get('http://localhost:8080/estadoPaquete/');
        const estadoVuelo = await axios.get('http://localhost:8080/estadoVuelo/');

        setData((prevData) => ({
          ...prevData,
          airports: airports.data,
          cities: cities.data,
          continents: continents.data,
          countries: countries.data,
          estadoPaquete: estadoPaquete.data,
          estadoVuelo: estadoVuelo.data,
        }));
        console.log('Airports data fetched:', airports.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleMapLoad = (map) => {
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

  const calculatePlanePosition = (flight, dia_actual, tiempo_actual) => {
    const departureDateTime = new Date(`${flight.departure_date}T${flight.departure_time}`);
    const arrivalDateTime = new Date(`${flight.arrival_date}T${flight.arrival_time}`);
    const currentDateTime = new Date(`${dia_actual}T${tiempo_actual}`);
    
    if (currentDateTime >= departureDateTime && currentDateTime <= arrivalDateTime) {
      const totalDuration = arrivalDateTime - departureDateTime;
      const elapsedDuration = currentDateTime - departureDateTime;
      const progress = elapsedDuration / totalDuration;

      const originAirport = data.airports.find(airport => airport.codigoIATA === flight.origin);
      const destinationAirport = data.airports.find(airport => airport.codigoIATA === flight.destination);

      if (originAirport && destinationAirport) {
        const currentLat = originAirport.latitude + progress * (destinationAirport.latitude - originAirport.latitude);
        const currentLng = originAirport.longitude + progress * (destinationAirport.longitude - originAirport.longitude);
        return { lat: currentLat, lng: currentLng };
      }
    }
    return null;
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
              googleMapsApiKey="AIzaSyBf1-gcqUmtphkPx7qe7jB-pn8sItv_xpc" // Reemplaza con tu clave de API
              libraries={libraries}
              onLoad={handleMapLoad}
              loadingElement={<div>Loading...</div>}
            >
              <GoogleMap
                mapContainerStyle={{ width: '100%', height: '100%' }}
                center={{ lat: -3.745, lng: -38.523 }}
                zoom={3}
                onLoad={handleMapLoad}
                mapId="56d2948ec3b0b447" // Reemplaza con tu Map ID sin espacios adicionales
              >
                {isMapLoaded && data.airports.length > 0 && (
                  <>
                    {data.airports.map((airport) => (
                      <MarkerF
                        key={airport.id}
                        position={{ lat: airport.latitude, lng: airport.longitude }}
                        title={airport.codigoIATA}
                        icon={{
                          url: redDot,
                          scaledSize: new window.google.maps.Size(32, 32),
                        }}
                      />
                    ))}
                    {data.flights.map((flight) => {
                      const planePosition = calculatePlanePosition(flight, tiempo_simulacion.dia_actual, tiempo_simulacion.tiempo_actual);
                      const originAirport = data.airports.find(airport => airport.codigoIATA === flight.origin);
                      const destinationAirport = data.airports.find(airport => airport.codigoIATA === flight.destination);
                      if (originAirport && destinationAirport) {
                        console.log(`Drawing line from ${originAirport.codigoIATA} to ${destinationAirport.codigoIATA}`);
                        return (
                          <React.Fragment key={flight.id}>
                            <Polyline
                              path={[
                                { lat: originAirport.latitude, lng: originAirport.longitude },
                                { lat: destinationAirport.latitude, lng: destinationAirport.longitude }
                              ]}
                              options={{ strokeColor: "#FF0000", strokeOpacity: 1.0, strokeWeight: 2 }}
                            />
                            {planePosition && (
                              <MarkerF
                                position={planePosition}
                                icon={{
                                  url: planeIcon,
                                  scaledSize: new window.google.maps.Size(32, 32),
                                  rotation: Math.atan2(
                                    destinationAirport.latitude - originAirport.latitude,
                                    destinationAirport.longitude - originAirport.longitude
                                  ) * (180 / Math.PI)
                                }}
                              />
                            )}
                          </React.Fragment>
                        );
                      }
                      return null;
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

export default App;