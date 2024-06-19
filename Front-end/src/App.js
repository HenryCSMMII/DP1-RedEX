import React, { useState, useEffect, useRef } from 'react';
import styled from 'styled-components';
import { GoogleMap, LoadScript, MarkerF, InfoWindow } from '@react-google-maps/api';
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
import planeIcon from './images/plane.png';
import planeRotado from './images/planeRotado.png';
import planeRed from './images/planeRed.png';
import planeRedRotado from './images/planeRedRotado.png';
import planeYellow from './images/planeYellow.png';
import planeYellowRotado from './images/planeYellowRotado.png';
import planeGreen from './images/planeGreen.png';
import planeGreenRotado from './images/planeGreenRotado.png';

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

const InputContainer = styled.div`
  position: absolute; 
  top: 10px; 
  left: 10px; 
  z-index: 1; 
  background: white; 
  padding: 10px; 
  border-radius: 5px; 
  box-shadow: 0px 0px 5px rgba(0,0,0,0.3);
`;

const FlightInfoContainer = styled.div`
  position: absolute;
  bottom: 10px;
  left: 10px;
  z-index: 1;
  background: white;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0px 0px 5px rgba(0,0,0,0.3);
`;

let globalCounter = 0;

function App() {
  const [activePopup, setActivePopup] = useState('');
  const [isNuevoEnvioOpen, setIsNuevoEnvioOpen] = useState(false);
  const [data, setData] = useState({
    airports: [],
    continents: [],
    countries: [],
    estadoVuelo: [],
    flights: [],
  });
  const [loading, setLoading] = useState(true);
  const [isMapLoaded, setIsMapLoaded] = useState(false);
  const mapRef = useRef(null);
  const [tiempo_simulacion, setTiempoSimulacion] = useState({
    dia_actual: "",
    tiempo_actual: ""
  });
  const [retryTimer, setRetryTimer] = useState(null);
  const simulationIntervalRef = useRef(null);
  const [selectedFlight, setSelectedFlight] = useState(null);
  const [flightInfo, setFlightInfo] = useState(null);

  const fetchData = async () => {
    try {
      const [airports, continents, countries] = await Promise.all([
        axios.get('http://localhost:8080/airport/'),
        axios.get('http://localhost:8080/continent/'),
        axios.get('http://localhost:8080/country/')
      ]);

      setData(prevData => ({
        ...prevData,
        airports: airports.data,
        continents: continents.data,
        countries: countries.data
      }));
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const runAlgorithm = async (url) => {
    try {
      const flightsResponse = await axios.get(url);
      if (flightsResponse.status !== 500) {
        const flights = flightsResponse.data.map(flight => {
          const departureDateTime = new Date(flight.departure_date_time);
          const arrivalDateTime = new Date(flight.arrival_date_time);
          return {
            id: flight.id,
            activo: 1,
            fecha_creacion: flight.fecha_creacion,
            fecha_modificacion: flight.fecha_modificacion,
            arrival_time: arrivalDateTime.toTimeString().split(' ')[0],
            capacity: flight.max_capacity,
            current_load: flight.used_capacity.reduce((acc, val) => acc + val, 0),
            departure_time: departureDateTime.toTimeString().split(' ')[0],
            destination: flight.arrival_airport.code,
            duration: (arrivalDateTime - departureDateTime) / 60000, // duration in minutes
            flight_number: flight.code,
            origin: flight.departure_airport.code,
            estado_vuelo_id: 1,
            arrival_date: arrivalDateTime.toISOString().split('T')[0],
            departure_date: departureDateTime.toISOString().split('T')[0],
          };
        });

        const lastFlightDepartureDateTime = new Date(flightsResponse.data.slice(-1)[0].departure_date_time);

        setTiempoSimulacion(prev => ({
          ...prev,
          dia_actual: lastFlightDepartureDateTime.toISOString().split('T')[0],
          tiempo_actual: lastFlightDepartureDateTime.toTimeString().split(' ')[0]
        }));

        setData(prevData => ({
          ...prevData,
          flights: flights
        }));
        startSimulationInterval();
        console.log('Data fetched:', flights);
      } else {
        console.error('Internal Server Error', flightsResponse);
      }
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setRetryTimer(setTimeout(() => runAlgorithm(url), 999990000));
    }
  };

  const startSimulationInterval = () => {
    if (simulationIntervalRef.current) {
      clearInterval(simulationIntervalRef.current);
    }
    simulationIntervalRef.current = setInterval(() => {
      setTiempoSimulacion(prev => {
        const currentDateTime = new Date(`${prev.dia_actual}T${prev.tiempo_actual}`);
        const newDateTime = new Date(currentDateTime.getTime() + 30 * 60 * 1000); // Increment by 30 minutes

        const newDate = newDateTime.toISOString().split('T')[0];
        const newTime = newDateTime.toTimeString().split(' ')[0];

        return {
          dia_actual: newDate,
          tiempo_actual: newTime
        };
      });
    }, 1000);
  };

  useEffect(() => {
    fetchData();
  }, []);

  useEffect(() => {
    if (!loading) {
      runAlgorithm();
    }
  }, [loading]);

  useEffect(() => {
    if (isMapLoaded && !loading) {
      console.log('Map and data loaded, forcing re-render');
      setIsMapLoaded(false);
      setTimeout(() => setIsMapLoaded(true), 0);
    }
  }, [loading, data.flights]);

  useEffect(() => {
    if (tiempo_simulacion.dia_actual && tiempo_simulacion.tiempo_actual) {
      startSimulationInterval();
    }
    return () => {
      if (simulationIntervalRef.current) {
        clearInterval(simulationIntervalRef.current);
      }
    };
  }, [tiempo_simulacion.dia_actual, tiempo_simulacion.tiempo_actual]);

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

  const handleSimulacionChange = (event) => {
    const { name, value } = event.target;
    setTiempoSimulacion((prev) => ({
      ...prev,
      [name]: value
    }));
  };

  const calculatePlanePosition = (flight, dia_actual, tiempo_actual) => {
    const departureDateTime = new Date(`${flight.departure_date}T${flight.departure_time}`);
    const arrivalDateTime = new Date(`${flight.arrival_date}T${flight.arrival_time}`);
    const currentDateTime = new Date(`${dia_actual}T${tiempo_actual}`);
    
    if (currentDateTime >= departureDateTime && currentDateTime <= arrivalDateTime) {
      const totalDuration = arrivalDateTime - departureDateTime;
      const elapsedDuration = currentDateTime - departureDateTime;
      const progress = elapsedDuration / totalDuration;

      const originAirport = data.airports.find(airport => airport.code === flight.origin);
      const destinationAirport = data.airports.find(airport => airport.code === flight.destination);

      if (originAirport && destinationAirport) {
        const currentLat = parseFloat(originAirport.latitude) + progress * (parseFloat(destinationAirport.latitude) - parseFloat(originAirport.latitude));
        const currentLng = parseFloat(originAirport.longitude) + progress * (parseFloat(destinationAirport.longitude) - parseFloat(originAirport.longitude));
        console.log(`Calculated plane position: lat ${currentLat}, lng ${currentLng}`);
        
        let angle = Math.atan2(
          destinationAirport.latitude - originAirport.latitude,
          destinationAirport.longitude - originAirport.longitude
        ) * (180 / Math.PI);

        // Ajustar el ángulo si el avión se mueve de derecha a izquierda
        const movingLeft = parseFloat(originAirport.longitude) > parseFloat(destinationAirport.longitude);

        return { lat: currentLat, lng: currentLng, angle, movingLeft };
      }
    }
    return null;
  };

  const getPlaneIcon = (capacityUsed) => {
    if (capacityUsed >= 0.8) {
      return { icon: planeRed, rotatedIcon: planeRedRotado };
    } else if (capacityUsed >= 0.3) {
      return { icon: planeYellow, rotatedIcon: planeYellowRotado };
    } else {
      return { icon: planeGreen, rotatedIcon: planeGreenRotado };
    }
  };

  const handleFlightClick = (flight) => {
    setFlightInfo(flight);
  };

  const handleCloseFlightInfo = () => {
    setFlightInfo(null);
  };

  const renderMapContent = () => {
    const currentDateTime = new Date(`${tiempo_simulacion.dia_actual}T${tiempo_simulacion.tiempo_actual}`);

    const activeFlights = data.flights.filter(flight => {
      const departureDateTime = new Date(`${flight.departure_date}T${flight.departure_time}`);
      const arrivalDateTime = new Date(`${flight.arrival_date}T${flight.arrival_time}`);
      return currentDateTime >= departureDateTime && currentDateTime <= arrivalDateTime;
    });

    return (
      <>
        {data.airports.length > 0 && data.airports.map((airport) => (
          <MarkerF
            key={airport.id}
            position={{ lat: parseFloat(airport.latitude), lng: parseFloat(airport.longitude) }}
            title={airport.code}
            icon={{
              url: redDot,
              scaledSize: new window.google.maps.Size(32, 32),
            }}
          />
        ))}
        {activeFlights.length > 0 && activeFlights.map((flight) => {
          const planePosition = calculatePlanePosition(flight, tiempo_simulacion.dia_actual, tiempo_simulacion.tiempo_actual);
          const capacityUsed = flight.current_load / flight.capacity;
          const { icon, rotatedIcon } = getPlaneIcon(capacityUsed);
          
          if (planePosition) {
            return (
              <MarkerF
                key={flight.id}
                position={{ lat: planePosition.lat, lng: planePosition.lng }}
                icon={{
                  url: planePosition.movingLeft ? rotatedIcon : icon,
                  scaledSize: new window.google.maps.Size(10, 10),
                  rotation: planePosition.angle
                }}
                onClick={() => handleFlightClick(flight)}
              />
            );
          }
          return null;
        })}
      </>
    );
  };

  const clearMap = () => {
    setData(prevData => ({
      ...prevData,
      flights: []
    }));
  };

  const handleStartSimulation = (tipoSimulacion) => {
    let url = '';
    if (tipoSimulacion === 'diario') {
      url = 'http://localhost:8080/api/algorithm/runDiaDia/';
    } else if (tipoSimulacion === 'semanal') {
      url = 'http://localhost:8080/api/algorithm/runSemanal/';
    } else if (tipoSimulacion === 'colapso') {
      url = 'http://localhost:8080/api/algorithm/run/';
    }

    setTiempoSimulacion({
      dia_actual: new Date().toISOString().split('T')[0],
      tiempo_actual: '00:00:00'
    });

    runAlgorithm(url);
  };

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
              googleMapsApiKey="AIzaSyBdX7iNprWJj_wIt7mTBD2oCkJH5ewV6wI"
              libraries={libraries}
              onLoad={handleMapLoad}
            >
              {isMapLoaded && (
                <GoogleMap
                  mapContainerStyle={{ width: '100%', height: '100%' }}
                  center={{ lat: -3.745, lng: -38.523 }}
                  zoom={3}
                  onLoad={handleMapLoad}
                  mapId="56d2948ec3b0b447"
                >
                  {!loading && renderMapContent()}
                </GoogleMap>
              )}
            </LoadScript>
          </MapContainer>
          {activePopup === 'Simulacion' && <SimulacionSidebar onClose={handleClosePopup} onStartSimulation={handleStartSimulation} />}
          {flightInfo && (
			  <FlightInfoContainer>
				<button onClick={handleCloseFlightInfo}>Cerrar</button>
				<div>
				  {flightInfo.id && <p><strong>ID del vuelo:</strong> {flightInfo.id}</p>}
				 
				  {flightInfo.origin && <p><strong>Aeropuerto de salida:</strong> {flightInfo.origin}</p>}
				  {flightInfo.destination && <p><strong>Aeropuerto de llegada:</strong> {flightInfo.destination}</p>}
				  {(
					<p><strong>Fecha y hora de salida:</strong> {new Date(flightInfo.departure_date).toLocaleString()}</p>
				  )}
				  {(
					<p><strong>Fecha y hora de llegada:</strong> {new Date(flightInfo.arrival_date).toLocaleString()}</p>
				  )}
				  {<p><strong>Capacidad máxima:</strong> {flightInfo.capacity}</p>}
				 
				  
				</div>
			  </FlightInfoContainer>
			)}
        </MainContent>
        <Legend />
      </Content>
      <InputContainer>
        <label>
          Fecha:
          <input
            type="date"
            name="dia_actual"
            value={tiempo_simulacion.dia_actual}
            onChange={handleSimulacionChange}
          />
        </label>
        <label>
          Hora:
          <input
            type="time"
            name="tiempo_actual"
            value={tiempo_simulacion.tiempo_actual}
            onChange={handleSimulacionChange}
          />
        </label>
      </InputContainer>
      <EnviosPopup isOpen={activePopup === 'Envios'} onRequestClose={handleClosePopup} onAddEnvio={handleOpenNuevoEnvio} data={data} />
      <VuelosPopup isOpen={activePopup === 'Vuelos'} onRequestClose={handleClosePopup} data={data} />
      <AeropuertosPopup isOpen={activePopup === 'Aeropuertos'} onRequestClose={handleClosePopup} data={data} />
      <ReportesPopup isOpen={activePopup === 'Reportes'} onRequestClose={handleClosePopup} data={data} />
      <NuevoEnvioPopup isOpen={isNuevoEnvioOpen} onRequestClose={handleCloseNuevoEnvio} data={data} />
    </AppContainer>
  );
}

export default App;
