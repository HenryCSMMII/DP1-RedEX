import React, { useState, useEffect, useRef } from 'react';
import styled from 'styled-components';
import { GoogleMap, LoadScript, MarkerF } from '@react-google-maps/api';
import { format, addMinutes, parseISO } from 'date-fns';
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
import yellowDot from './images/yellow-dot.png';
import greenDot from './images/green-dot.png';
import planeRed from './images/planeRed.png';
import planeRedRotado from './images/planeRedRotado.png';
import planeYellow from './images/planeYellow.png';
import planeYellowRotado from './images/planeYellowRotado.png';
import planeGreen from './images/planeGreen.png';
import planeGreenRotado from './images/planeGreenRotado.png';

const AppContainer = styled.div`
  display: flex;
  height: 100vh;
  flex-direction: row;
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

const InfoContainer = styled.div`
  position: absolute;
  bottom: 10px;
  left: 10px;
  z-index: 1;
  background: white;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0px 0px 5px rgba(0,0,0,0.3);
`;

const calculateSaturation = (currentLoad, capacity) => {
  if (capacity === 0) return 'N/A';
  return ((currentLoad / capacity) * 100).toFixed(2) + '%';
};

function App() {
  const [activePopup, setActivePopup] = useState('');
  const [isNuevoEnvioOpen, setIsNuevoEnvioOpen] = useState(false);
  const [data, setData] = useState({
    airports: [],
    continents: [],
    countries: [],
    ciudad: [],
    estadoVuelo: [],
    flights: [],
  });
  const [loading, setLoading] = useState(true);
  const [isMapLoaded, setIsMapLoaded] = useState(false);
  const mapRef = useRef(null);
  const [tiempo_simulacion, setTiempoSimulacion] = useState({
    dia_actual: '',
    tiempo_actual: '',
  });
  const simulationIntervalRef = useRef(null);
  const [selectedFlight, setSelectedFlight] = useState(null);
  const [selectedAirport, setSelectedAirport] = useState(null);
  const [tipoSimulacion, setTipoSimulacion] = useState(null);
  const [allShipments, setAllShipments] = useState([]);
  const [airportCapacities, setAirportCapacities] = useState({});

  const fetchData = async () => {
    try {
      const [airports, continents, countries, ciudad] = await Promise.all([
        axios.get('http://localhost:8080/airport/'),
        axios.get('http://localhost:8080/continent/'),
        axios.get('http://localhost:8080/country/'),
        axios.get('http://localhost:8080/ciudad/')
      ]);

      console.log('Airports data:', airports.data);

      // Inicializamos la capacidad actual de cada aeropuerto
      const initialCapacities = airports.data.reduce((acc, airport) => {
        acc[airport.code] = { max_capacity: airport.max_capacity, current_capacity: 0 };
        return acc;
      }, {});

      setAirportCapacities(initialCapacities);

      setData((prevData) => ({
        ...prevData,
        airports: airports.data,
        continents: continents.data,
        countries: countries.data,
        ciudad: ciudad.data
      }));
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const runAlgorithm = async (url, fechaInicio) => {
    try {
      const response = await axios.post(url, { fecha_inicio: fechaInicio });
      const flightsResponse = response.data;

      if (response.status !== 500 && flightsResponse && Array.isArray(flightsResponse)) {
        const flights = flightsResponse.map((flight) => {
          const departureDateTime = parseISO(flight.departure_date_time);
          const arrivalDateTime = parseISO(flight.arrival_date_time);
          return {
            id: flight.id,
            activo: 1,
            fecha_creacion: flight.fecha_creacion,
            fecha_modificacion: flight.fecha_modificacion,
            arrival_time: format(arrivalDateTime, 'HH:mm:ss'),
            capacity: flight.max_capacity,
            current_load: flight.used_capacity[0],
            departure_time: format(departureDateTime, 'HH:mm:ss'),
            destination: flight.arrival_airport.code,
            duration: (arrivalDateTime - departureDateTime) / 60000,
            flight_number: flight.code,
            origin: flight.departure_airport.code,
            estado_vuelo_id: 1,
            arrival_date: format(arrivalDateTime, 'yyyy-MM-dd'),
            departure_date: format(departureDateTime, 'yyyy-MM-dd'),
          };
        });

        setTiempoSimulacion({
          dia_actual: fechaInicio,
          tiempo_actual: '00:00:00',
        });

        setData((prevData) => ({
          ...prevData,
          flights: flights,
        }));

        startSimulationInterval();

        // Extraemos y agrupamos todos los shipments
        const shipments = [];
        flightsResponse.forEach((flight) => {
          if (flight.shipments && Array.isArray(flight.shipments)) {
            flight.shipments.forEach((shipment) => {
              shipments.push({
                ...shipment,
                departure_date_time_plane: flight.departure_date_time,
                arrival_date_time_plane: flight.arrival_date_time,
                departure_airport_plane: flight.departure_airport.code,
                arrival_airport_plane: flight.arrival_airport.code,
              });
            });
          }
        });

        setAllShipments(shipments);

      } else {
        console.error('Internal Server Error', response);
      }
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const startSimulationInterval = () => {
    if (simulationIntervalRef.current) {
      clearInterval(simulationIntervalRef.current);
    }
    simulationIntervalRef.current = setInterval(() => {
      setTiempoSimulacion((prev) => {
        const currentDateTime = parseISO(`${prev.dia_actual}T${prev.tiempo_actual}`);
        const newDateTime = addMinutes(currentDateTime, 30);

        const newDate = format(newDateTime, 'yyyy-MM-dd');
        const newTime = format(newDateTime, 'HH:mm:ss');
        return {
          dia_actual: newDate,
          tiempo_actual: newTime,
        };
      });
    }, 1000);
  };

  const stopSimulationInterval = () => {
    if (simulationIntervalRef.current) {
      clearInterval(simulationIntervalRef.current);
    }
    setTipoSimulacion(null);
    setTiempoSimulacion({
      dia_actual: '',
      tiempo_actual: '',
    });
    setData((prevData) => ({
      ...prevData,
      flights: [],
    }));
    setAllShipments([]);
    fetchData();
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
  };

  const handleOpenPopup = (popupName) => {
    setActivePopup(popupName);
  };

  const handleClosePopup = () => {
    setActivePopup('');
  };

  const handleOpenNuevoEnvio = () => {
    if (tipoSimulacion) {
      setIsNuevoEnvioOpen(true);
    } else {
      alert('Por favor inicia un tipo de simulación');
    }
  };

  const handleCloseNuevoEnvio = () => {
    setIsNuevoEnvioOpen(false);
  };

  const handleSimulacionChange = (event) => {
    const { name, value } = event.target;
    setTiempoSimulacion((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const calculatePlanePosition = (flight, dia_actual, tiempo_actual) => {
    const departureDateTime = parseISO(`${flight.departure_date}T${flight.departure_time}`);
    const arrivalDateTime = parseISO(`${flight.arrival_date}T${flight.arrival_time}`);
    const currentDateTime = parseISO(`${dia_actual}T${tiempo_actual}`);

    if (currentDateTime >= departureDateTime && currentDateTime <= arrivalDateTime) {
      const totalDuration = arrivalDateTime - departureDateTime;
      const elapsedDuration = currentDateTime - departureDateTime;
      const progress = elapsedDuration / totalDuration;

      const originAirport = data.airports.find((airport) => airport.code === flight.origin);
      const destinationAirport = data.airports.find((airport) => airport.code === flight.destination);

      if (originAirport && destinationAirport) {
        const currentLat =
          parseFloat(originAirport.latitude) +
          progress * (parseFloat(destinationAirport.latitude) - parseFloat(originAirport.latitude));
        const currentLng =
          parseFloat(originAirport.longitude) +
          progress * (parseFloat(destinationAirport.longitude) - parseFloat(originAirport.longitude));

        let angle =
          Math.atan2(
            destinationAirport.latitude - originAirport.latitude,
            destinationAirport.longitude - originAirport.longitude
          ) *
          (180 / Math.PI);

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
    setSelectedFlight(flight);
    setSelectedAirport(null); // Limpiar la selección de aeropuerto
  };

  const handleAirportClick = (airport) => {
    setSelectedAirport(airport);
    setSelectedFlight(null); // Limpiar la selección de vuelo
  };

  const renderMapContent = () => {
    if (!window.google || !window.google.maps) {
      return null;
    }

    const currentDateTime = parseISO(`${tiempo_simulacion.dia_actual}T${tiempo_simulacion.tiempo_actual}`);

    const activeFlights = data.flights.filter((flight) => {
      const departureDateTime = parseISO(`${flight.departure_date}T${flight.departure_time}`);
      const arrivalDateTime = parseISO(`${flight.arrival_date}T${flight.arrival_time}`);
      return currentDateTime >= departureDateTime && currentDateTime <= arrivalDateTime;
    });

    const updatedAirports = { ...airportCapacities };

    allShipments.forEach((shipment) => {
      const { departure_date_time_plane, arrival_date_time_plane, departure_airport_plane, arrival_airport_plane, packageQuantity } = shipment;

      const departureDateTime = parseISO(departure_date_time_plane);
      const arrivalDateTime = parseISO(arrival_date_time_plane);

      // Manejo del registro de envíos en el aeropuerto de origen
      if (currentDateTime.getTime() === parseISO(shipment.registerDateTime).getTime()) {
        if (updatedAirports[shipment.departure_airport]) {
          updatedAirports[shipment.departure_airport].current_capacity += shipment.packageQuantity;
          console.log(`Envio ID: ${shipment.id} registrado en ${shipment.departure_airport} con ${shipment.packageQuantity} paquetes. Capacidad actual: ${updatedAirports[shipment.departure_airport].current_capacity}`);
        } else {
          console.error(`Error: Aeropuerto ${shipment.departure_airport} no encontrado.`);
        }
      }

      if (currentDateTime >= departureDateTime && currentDateTime <= arrivalDateTime) {
        // El avión recoge los paquetes del aeropuerto de salida
        if (currentDateTime.getTime() === departureDateTime.getTime()) {
          if (updatedAirports[departure_airport_plane] && updatedAirports[departure_airport_plane].current_capacity >= packageQuantity) {
            updatedAirports[departure_airport_plane].current_capacity -= packageQuantity;
            console.log(`Envio ID: ${shipment.id} con ${packageQuantity} paquetes se fue de ${departure_airport_plane}. Capacidad actual: ${updatedAirports[departure_airport_plane].current_capacity}`);
          } else {
            console.error(`Error: Capacidad insuficiente en ${departure_airport_plane} para retirar ${packageQuantity} paquetes.`);
          }
        }
        // El avión deja los paquetes en el aeropuerto de llegada (excepto si es el destino final)
        if (currentDateTime.getTime() === arrivalDateTime.getTime() && shipment.arrival_airport !== arrival_airport_plane) {
          if (updatedAirports[arrival_airport_plane]) {
            updatedAirports[arrival_airport_plane].current_capacity += packageQuantity;
            console.log(`Envio ID: ${shipment.id} con ${packageQuantity} paquetes llegó a ${arrival_airport_plane}. Capacidad actual: ${updatedAirports[arrival_airport_plane].current_capacity}`);
          } else {
            console.error(`Error: Aeropuerto ${arrival_airport_plane} no encontrado.`);
          }
        }
      }
    });

    const getDotIcon = (airportCode) => {
      const airport = updatedAirports[airportCode];
      if (airport) {
        const usage = airport.current_capacity / airport.max_capacity;
        if (usage > 0.8) {
          return redDot;
        } else if (usage > 0.3) {
          return yellowDot;
        } else {
          return greenDot;
        }
      }
      return greenDot; // Valor por defecto si el aeropuerto no se encuentra
    };

    return (
      <>
        {data.airports.length > 0 &&
          data.airports.map((airport) => (
            <MarkerF
              key={airport.id}
              position={{ lat: parseFloat(airport.latitude), lng: parseFloat(airport.longitude) }}
              title={airport.code}
              icon={{
                url: getDotIcon(airport.code),
                scaledSize: new window.google.maps.Size(35, 35),
              }}
              onClick={() => handleAirportClick(airport)}
            />
          ))}
        {activeFlights.length > 0 &&
          activeFlights.map((flight) => {
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
                    scaledSize: new window.google.maps.Size(25, 25),
                    rotation: planePosition.angle,
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
    setData((prevData) => ({
      ...prevData,
      flights: [],
    }));
  };

  const handleStartSimulation = (tipoSimulacion, fechaInicio) => {
    let url = '';
    if (tipoSimulacion === 'diario') {
      url = 'http://localhost:8080/api/algorithm/runDiaDia/';
    } else if (tipoSimulacion === 'semanal') {
      url = 'http://localhost:8080/api/algorithm/runSemanal/';
    } else if (tipoSimulacion === 'colapso') {
      url = 'http://localhost:8080/api/algorithm/run/';
    }

    setTipoSimulacion(tipoSimulacion);
    runAlgorithm(url, fechaInicio);
  };

  return (
    <AppContainer>
      <Sidebar
        onNuevoEnvioClick={handleOpenNuevoEnvio}
        onVuelosClick={() => handleOpenPopup('Vuelos')}
        onAeropuertosClick={() => handleOpenPopup('Aeropuertos')}
        onReportesClick={() => handleOpenPopup('Reportes')}
        onSimulacionClick={() => handleOpenPopup('Simulacion')}
        onDetenerSimulacionClick={stopSimulationInterval}
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
                  center={{ lat: 5.7942, lng: 60.8822 }}
                  zoom={3}
                  options={{
                    zoomControl: false,
                    streetViewControl: false,
                    mapTypeControl: false,
                    fullscreenControl: false,
                    mapTypeId: 'roadmap',
                    disableDefaultUI: true,
                    gestureHandling: 'none',
                  }}
                  onLoad={handleMapLoad}
                  mapId="56d2948ec3b0b447"
                >
                  {!loading && renderMapContent()}
                </GoogleMap>
              )}
            </LoadScript>
          </MapContainer>
          {activePopup === 'Simulacion' && (
            <SimulacionSidebar
              onClose={handleClosePopup}
              onStartSimulation={handleStartSimulation}
              onStopSimulation={stopSimulationInterval}
            />
          )}
          {(selectedFlight || selectedAirport) && (
            <InfoContainer>
              <button onClick={() => { setSelectedFlight(null); setSelectedAirport(null); }}>Cerrar</button>
              {selectedFlight && (
                <div>
                  {selectedFlight.id && <p><strong>ID del vuelo:</strong> {selectedFlight.id}</p>}
                  {selectedFlight.origin && <p><strong>Aeropuerto de salida:</strong> {selectedFlight.origin}</p>}
                  {selectedFlight.destination && <p><strong>Aeropuerto de llegada:</strong> {selectedFlight.destination}</p>}
                  {selectedFlight.departure_date && (
                    <p><strong>Fecha y hora de salida:</strong> {new Date(`${selectedFlight.departure_date}T${selectedFlight.departure_time}`).toLocaleString()}</p>
                  )}
                  {selectedFlight.arrival_date && (
                    <p><strong>Fecha y hora de llegada:</strong> {new Date(`${selectedFlight.arrival_date}T${selectedFlight.arrival_time}`).toLocaleString()}</p>
                  )}
                  {<p><strong>Capacidad máxima:</strong> {selectedFlight.current_load}/{selectedFlight.capacity}</p>}
                  {<p><strong>Saturación:</strong> {calculateSaturation(selectedFlight.current_load, selectedFlight.capacity)}</p>}
                </div>
              )}
              {selectedAirport && (
                <div>
                  {selectedAirport.code && <p><strong>Código del aeropuerto:</strong> {selectedAirport.code}</p>}
                  {selectedAirport.name && <p><strong>Nombre del aeropuerto:</strong> {selectedAirport.name}</p>}
                  {selectedAirport.city && <p><strong>Ciudad:</strong> {selectedAirport.city}</p>}
                  {selectedAirport.country && <p><strong>País:</strong> {selectedAirport.country}</p>}
                  {selectedAirport.latitude && <p><strong>Latitud:</strong> {selectedAirport.latitude}</p>}
                  {selectedAirport.longitude && <p><strong>Longitud:</strong> {selectedAirport.longitude}</p>}
                  {airportCapacities[selectedAirport.code] && (
                    <>
                      <p><strong>Capacidad actual de paquetes:</strong> {airportCapacities[selectedAirport.code].current_capacity}/{airportCapacities[selectedAirport.code].max_capacity}</p>
                      <p><strong>Saturación:</strong> {calculateSaturation(airportCapacities[selectedAirport.code].current_capacity, airportCapacities[selectedAirport.code].max_capacity)}</p>
                    </>
                  )}
                </div>
              )}
            </InfoContainer>
          )}
        </MainContent>
        <Legend />
      </Content>
      <InputContainer>
        <label>
          Fecha:
          <input type="date" name="dia_actual" value={tiempo_simulacion.dia_actual} onChange={handleSimulacionChange} />
        </label>
        <label>
          Hora:
          <input type="time" name="tiempo_actual" value={tiempo_simulacion.tiempo_actual} onChange={handleSimulacionChange} />
        </label>
      </InputContainer>
      <NuevoEnvioPopup
        isOpen={isNuevoEnvioOpen}
        onRequestClose={handleCloseNuevoEnvio}
        data={data}
        tipoSimulacion={tipoSimulacion}
        runAlgorithm={runAlgorithm}
        tiempoSimulacion={tiempo_simulacion}
      />
      <VuelosPopup isOpen={activePopup === 'Vuelos'} onRequestClose={handleClosePopup} data={data} />
      <AeropuertosPopup isOpen={activePopup === 'Aeropuertos'} onRequestClose={handleClosePopup} data={data} />
      <ReportesPopup isOpen={activePopup === 'Reportes'} onRequestClose={handleClosePopup} data={data} />
    </AppContainer>
  );
}

export default App;