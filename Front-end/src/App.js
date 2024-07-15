import React, { useState, useEffect, useRef } from 'react';
import styled, { createGlobalStyle } from 'styled-components';
import { GoogleMap, LoadScript, MarkerF } from '@react-google-maps/api';
import { format, addMinutes, parseISO, differenceInSeconds, subHours } from 'date-fns';
import Sidebar from './components/Sidebar';
import SidebarSearch from './components/SidebarSearch';
import Legend from './components/Legend';
import EnviosPopup from './components/EnviosPopup';
import NuevoEnvioPopup from './components/NuevoEnvioPopup';
import VuelosPopup from './components/VuelosPopup';
import AeropuertosPopup from './components/AeropuertosPopup';
import ReportesPopup from './components/ReportesPopup';
import SimulacionSidebar from './components/SimulacionSidebar';
import axios from 'axios';
import Modal from 'react-modal';
import FinalSimulationPopup from './components/FinalSimulationPopup'; // Import the new component
import redDot from './images/red-dot.png';
import yellowDot from './images/yellow-dot.png';
import greenDot from './images/green-dot.png';
import planeRed from './images/planeRed.png';
import planeRedRotado from './images/planeRedRotado.png';
import planeYellow from './images/planeYellow.png';
import planeYellowRotado from './images/planeYellowRotado.png';
import planeGreen from './images/planeGreen.png';
import planeGreenRotado from './images/planeGreenRotado.png';
import redDotSelected from './images/red-dot-selected.png';
import yellowDotSelected from './images/yellow-dot-selected.png';
import greenDotSelected from './images/green-dot-selected.png';
import planeRedWithBorder from './images/planeRedWithBorder.png';
import planeRedRotadoWithBorder from './images/planeRedRotadoWithBorder.png';
import planeYellowWithBorder from './images/planeYellowWithBorder.png';
import planeYellowRotadoWithBorder from './images/planeYellowRotadoWithBorder.png';
import planeGreenWithBorder from './images/planeGreenWithBorder.png';
import planeGreenRotadoWithBorder from './images/planeGreenRotadoWithBorder.png';

const GlobalStyle = createGlobalStyle`
  body, html, #root {
    margin: 0;
    padding: 0;
    width: 100%;
    height: 100%;
    overflow: hidden;
  }
`;

const LocalTimeContainer = styled.div`
  position: absolute;
  font-size: 20px;
  top: 20px;
  right: 330px;
  z-index: 1;
  background: white;
  padding: 5px;
  border-radius: 5px;
  border: 2px solid black;
  box-shadow: 0px 0px 5px rgba(0,0,0,0.3);
  font-size: 13px;
`;

const selectedPlaneIcons = {
  red: planeRedWithBorder,
  redRotado: planeRedRotadoWithBorder,
  yellow: planeYellowWithBorder,
  yellowRotado: planeYellowRotadoWithBorder,
  green: planeGreenWithBorder,
  greenRotado: planeGreenRotadoWithBorder,
};

const AppContainer = styled.div`
  display: flex;
  width: 100vw;
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
  position: relative;
  z-index: 0;
`;

const libraries = ['places', 'marker'];

Modal.setAppElement('#root');

const InputContainer = styled.div`
  position: absolute;
  top: 10px;
  right: 1360px;
  z-index: 1;
  background: white;
  padding: 10px;
  border-radius: 5px;
  border: 2px solid black;
  box-shadow: 0px 0px 5px rgba(0,0,0,0.3);
  font-size: 12px;
`;

const InfoContainer = styled.div`
  position: absolute;
  bottom: 10px;
  left: 10px;
  z-index: 1;
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  border: 2px solid black;
  width: 260px;
  font-size: 12px;
`;

const InfoContainerVuelo = styled.div`
  position: absolute;
  bottom: 10px;
  left: 10px;
  z-index: 1;
  background: white;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  border: 2px solid black;
  width: 250px;
  font-size: 12px;
`;

const CloseButton = styled.button`
  margin-top: 10px;
  background: #808080;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
  width: 100%;
`;

const Header = styled.div`
  text-align: center;
  font-weight: bold;
  margin-bottom: 10px;
`;

const processedShipments = new Set();
const calculateCurrentCapacityPercentage = (currentLoad, capacity) => {
  if (capacity === 0) return '0%';
  return ((currentLoad / capacity) * 100).toFixed(2) + '%';
};

const calculateSaturation = (currentLoad, capacity) => {
  if (capacity === 0) return 'N/A';
  return ((currentLoad / capacity) * 100).toFixed(2) + '%';
};

const InfoBox = ({ airport, capacities, setSelectedFlight, setSelectedAirport, selected }) => {
  if (!airport) return null;
  return (
    <InfoContainer selected={selected}>
      <Header>Información del Aeropuerto</Header>
      <div>
        <p><strong style={{ color: 'green', fontSize: '16px' }}>País:</strong> <span style={{ color: 'green', fontSize: '16px' }}>{airport.city}</span></p>
        <p><strong>Ciudad del aeropuerto:</strong> {airport.city}</p>
        <p><strong>Nombre del aeropuerto:</strong> {airport.code}</p>
        <p><strong>Latitud:</strong> {airport.latitude}</p>
        <p><strong>Longitud:</strong> {airport.longitude}</p>
        {capacities[airport.code] && (
          <p>
            <strong>Capacidad:</strong> {capacities[airport.code].current_capacity}/{capacities[airport.code].max_capacity} (<span style={{ color: 'orange' }}>{calculateCurrentCapacityPercentage(capacities[airport.code].current_capacity, capacities[airport.code].max_capacity)}</span>)
          </p>
        )}
      </div>
      <CloseButton onClick={() => { setSelectedFlight(null); setSelectedAirport(null); }}>Cerrar</CloseButton>
    </InfoContainer>
  );
};

const FlightInfoBox = ({ flight, setSelectedFlight }) => {
  if (!flight) return null;

  return (
    <InfoContainerVuelo>
      <Header>Información del Vuelo</Header>
      <div>
        {flight.id && <p><strong>ID del vuelo:</strong> {flight.id}</p>}
        {flight.origin && <p><strong>Aeropuerto de salida:</strong> {flight.origin}</p>}
        {flight.destination && <p><strong>Aeropuerto de llegada:</strong> {flight.destination}</p>}
        {flight.departure_date && (
          <p><strong>Salida:</strong> {new Date(`${flight.departure_date}T${flight.salida_hora}`).toLocaleString()}</p>
        )}
        {flight.arrival_date && (
          <p><strong>Llegada:</strong> {new Date(`${flight.arrival_date}T${flight.llegada_hora}`).toLocaleString()}</p>
        )}
        <p>
          <strong>Capacidad:</strong> {flight.current_load}/{flight.capacity} (<span style={{ color: 'orange' }}>{calculateSaturation(flight.current_load, flight.capacity)}</span>)
        </p>
      </div>
      <CloseButton onClick={() => setSelectedFlight(null)}>Cerrar</CloseButton>
    </InfoContainerVuelo>
  );
};

function App() {
  const [localTime, setLocalTime] = useState({
    currentDate: '',
    currentTime: '',
  });

  const [planeSaturation, setPlaneSaturation] = useState(0);
  const [airportSaturation, setAirportSaturation] = useState(0);

  const [startDateTime, setStartDateTime] = useState(null);
  const [currentFlights, setCurrentFlights] = useState([]); // Nuevo estado para vuelos en curso

  const [isSidebarCollapsed, setIsSidebarCollapsed] = useState(false);
  const [isSimulationSidebarCollapsed, setIsSimulationSidebarCollapsed] = useState(false);

  const [isFinalPopupOpen, setIsFinalPopupOpen] = useState(false); // Estado para el popup final
  const [finalItineraries, setFinalItineraries] = useState([]); // Estado para los itinerarios finales
  const [isSimulationEnded, setIsSimulationEnded] = useState(false); // Nuevo estado para controlar el final de la simulación
  const [currentItineraries, setCurrentItineraries] = useState([]); // Nuevo estado para itinerarios actuales
  const [originalFlights, setOriginalFlights] = useState([]); 
  const toggleSidebar = () => {
    setIsSidebarCollapsed(!isSidebarCollapsed);
  };

  const toggleSimulationSidebar = () => {
    setIsSimulationSidebarCollapsed(!isSimulationSidebarCollapsed);
  };

  const ElapsedTimeDisplay = ({ elapsedTime, startDate, startHour }) => {
    if (!tiempo_simulacion || !tiempo_simulacion.dia_actual || !tiempo_simulacion.tiempo_actual) {
      return (
        <div>
          <p><strong>Tiempo transcurrido:</strong> 0 días 0 h 0 min</p>
        </div>
      );
    }
    const simulationStartTime = parseISO(`${startDate}T${startHour}`);
    const currentTime = parseISO(`${elapsedTime.dia_actual}T${elapsedTime.tiempo_actual}`);
    const diffInSeconds = differenceInSeconds(currentTime, simulationStartTime);

    const days = Math.floor(diffInSeconds / (24 * 60 * 60));
    const hours = Math.floor((diffInSeconds % (24 * 60 * 60)) / (60 * 60));
    const minutes = Math.floor((diffInSeconds % (60 * 60)) / 60);

    return (
      <div>
        <p><strong>Tiempo transcurrido:</strong> {days} días {hours} h {minutes} min</p>
      </div>
    );
  };

  const formatDate = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  };

  const formatTime = (date) => {
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${hours}:${minutes}:${seconds}`;
  };

  function extractTime(isoString) {
    return isoString.split('T')[1].split('.')[0];
  }

  useEffect(() => {
    const updateLocalTime = () => {
      const now = new Date();
      const date = formatDate(now);
      const time = formatTime(now);
      setLocalTime({ currentDate: date, currentTime: time });
    };

    updateLocalTime(); // Actualiza inmediatamente
    const intervalId = setInterval(updateLocalTime, 1000); // Actualiza cada segundo

    return () => clearInterval(intervalId); // Limpia el intervalo al desmontar el componente
  }, []);

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
  const [startDate, setStartDate] = useState(null);
  const [startHour, setStartHour] = useState(null);
  const [allShipments, setAllShipments] = useState([]);
  const [airportCapacities, setAirportCapacities] = useState({});

  const fetchData = async () => {
    try {
      const [airports, continents, countries, ciudad] = await Promise.all([
        axios.get('http://localhost:8080/airport/'),
        axios.get('http://localhost:8080/continent/'),
        axios.get('http://localhost:8080/country/'),
        axios.get('http://localhost:8080/ciudad/'),
      ]);

      // Inicializamos la capacidad actual de cada aeropuerto
      const initialCapacities = airports.data.reduce((acc, airport) => {
        acc[airport.code] = { max_capacity: airport.max_capacity, current_capacity: 0 };
        return acc;
      }, {});

      // Combinar los datos de aeropuertos y países
      const updatedAirports = airports.data.map((airport) => {
        const country = countries.data.find((country) => country.id === airport.countryId);
        return {
          ...airport,
          city: country ? country.name : 'Desconocido', // Aquí se asigna el país
          country: country ? country.name : 'Desconocido', // Agregar país aquí
        };
      });

      setAirportCapacities(initialCapacities);

      setData((prevData) => ({
        ...prevData,
        airports: updatedAirports,
        continents: continents.data,
        countries: countries.data,
        ciudad: ciudad.data, // Agregamos las ciudades aquí
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

          const salidaAvion = flight.salida;
          const llegadaAvion = flight.llegada;
          return {
            id: flight.id,
            activo: 1,
            fecha_creacion: flight.fecha_creacion,
            fecha_modificacion: flight.fecha_modificacion,
            arrival_time: format(arrivalDateTime, 'HH:mm:ss'),
            capacity: flight.max_capacity,
            current_load: Math.trunc(flight.used_capacity[0] / 4),
            departure_time: format(departureDateTime, 'HH:mm:ss'),
            destination: flight.arrival_airport.code,
            duration: (arrivalDateTime - departureDateTime) / 60000,
            flight_number: flight.code,
            origin: flight.departure_airport.code,
            estado_vuelo_id: 1,
            arrival_date: format(arrivalDateTime, 'yyyy-MM-dd'),
            departure_date: format(departureDateTime, 'yyyy-MM-dd'),
            salida: flight.salida,
            llegada: flight.llegada,
            salida_hora: extractTime(salidaAvion),
            llegada_hora: extractTime(llegadaAvion),
            shipments: flight.shipments.map(shipment => ({
              ...shipment,
              departure_airport: flight.departure_airport.code,
              arrival_airport: flight.arrival_airport.code,
              departure_date_time_plane: flight.departure_date_time,
              arrival_date_time_plane: flight.arrival_date_time,
              registerDateTime: shipment.registerDateTime // Aquí se incluye el registerDateTime
            }))
          };
        });

        const allShipments = flights.reduce((acc, flight) => {
          return [...acc, ...flight.shipments];
        }, []);

        setOriginalFlights(flights); // Guardar copia del JSON original
        setAllShipments(allShipments); // Guardar los envíos en el estado

        setTiempoSimulacion({
          dia_actual: fechaInicio.split('T')[0],
          tiempo_actual: fechaInicio.split('T')[1],
        });

        setData((prevData) => ({
          ...prevData,
          flights: flights,
        }));

        startSimulationInterval();
      } else {
        console.error('Internal Server Error', response);
      }
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const updateFlights = (flights, currentDateTime) => {
    return flights.map(flight => {
      return { ...flight };
    });
  };

  useEffect(() => {
    if (allShipments.length > 0) {
      startSimulationInterval();
    }
  }, [allShipments]);

  const updateAirportCapacities = (airportCapacities, allShipments, currentDateTime, previousDateTime, processedShipments) => {
    const updatedCapacities = { ...airportCapacities };

    allShipments.forEach((shipment) => {
		console.log("hola")
      const { departure_date_time_plane, registerDateTime, packageQuantity, departure_airport, arrival_airport } = shipment;
	console.log("hola")
      const departureDateTime = parseISO(departure_date_time_plane);
      const registerDateTimeObj = parseISO(shipment.registerDateTime);

      // Evitar procesar un envío más de una vez
      if (processedShipments.has(shipment.id)) {
        return;
      }
console.log(registerDateTimeObj)
console.log(departureDateTime)
console.log(shipment.id)
      // Incrementar capacidad al registrar el envío
      if (registerDateTimeObj.getTime() === currentDateTime.getTime()) {
        if (updatedCapacities[departure_airport]) {
          updatedCapacities[departure_airport].current_capacity += packageQuantity;
          processedShipments.add(shipment.id); // Marcar el envío como procesado
        } else {
          console.error(`Error: Aeropuerto ${departure_airport} no encontrado.`);
        }
      }

      // Reducir capacidad al salir el vuelo
      if (departureDateTime.getTime() === currentDateTime.getTime()) {
        if (updatedCapacities[departure_airport] && updatedCapacities[departure_airport].current_capacity >= packageQuantity) {
          updatedCapacities[departure_airport].current_capacity -= packageQuantity;
          processedShipments.add(shipment.id); // Marcar el envío como procesado
        } else {
          console.error(`Error: Capacidad insuficiente en ${departure_airport} para retirar ${packageQuantity} paquetes.`);
        }
      }
    });

    return updatedCapacities;
  };


  const startSimulationInterval = () => {
    if (simulationIntervalRef.current) {
      clearInterval(simulationIntervalRef.current);
    }
    simulationIntervalRef.current = setInterval(() => {
      setTiempoSimulacion((prev) => {
        const currentDateTime = parseISO(`${prev.dia_actual}T${prev.tiempo_actual}`);
        const newDateTime = addMinutes(currentDateTime, 1);

        const newDate = format(newDateTime, 'yyyy-MM-dd');
        const newTime = format(newDateTime, 'HH:mm:ss');

        // Inicializar un conjunto para rastrear los envíos procesados
    

        // Actualizar las capacidades de aeropuertos y vuelos aquí basado en la nueva fecha y hora de simulación
        const updatedFlights = updateFlights(data.flights, newDateTime);
        const updatedAirportCapacities = updateAirportCapacities(airportCapacities, allShipments, newDateTime, currentDateTime, processedShipments);

        // Calcular saturaciones
        const fleetSaturation = calculateFleetSaturation(updatedFlights, newDateTime);
        const airportSaturation = calculateAirportSaturation(updatedAirportCapacities);

        // Filtrar vuelos en curso
        const currentFlights = updatedFlights.filter((flight) => {
          const departureDateTime = parseISO(`${flight.departure_date}T${flight.departure_time}`);
          const arrivalDateTime = parseISO(`${flight.arrival_date}T${flight.arrival_time}`);
          return currentDateTime >= departureDateTime && currentDateTime <= arrivalDateTime;
        });

        // Actualizar estados
        setPlaneSaturation(fleetSaturation);
        setAirportSaturation(airportSaturation);
        setCurrentFlights(currentFlights);
        setAirportCapacities(updatedAirportCapacities);

        // Calcular tiempo transcurrido
        const simulationStartTime = parseISO(`${startDate}T${startHour}`);
        const elapsedTimeInSeconds = differenceInSeconds(newDateTime, simulationStartTime);
        const elapsedDays = Math.floor(elapsedTimeInSeconds / (24 * 60 * 60));
        const elapsedHours = Math.floor((elapsedTimeInSeconds % (24 * 60 * 60)) / (60 * 60));

        // Detener simulación si ha transcurrido una semana (7 días)
        if (elapsedDays >= 8) {
          clearInterval(simulationIntervalRef.current);
          setIsSimulationEnded(true);
          setIsFinalPopupOpen(true);

          const currentItineraries = originalFlights.filter((flight) => {
            const departureDateTime = parseISO(`${flight.departure_date}T${flight.departure_time}`);
            const arrivalDateTime = parseISO(`${flight.arrival_date}T${flight.arrival_time}`);
            return currentDateTime >= departureDateTime && currentDateTime < arrivalDateTime;
          });

          setCurrentItineraries(currentItineraries);

          const endSimulationTime = newDateTime;
          const startSimulationTime = subHours(endSimulationTime, 6);
          const finalItineraries = updatedFlights.filter((flight) => {
            const arrivalDateTime = parseISO(`${flight.arrival_date}T${flight.arrival_time}`);
            return arrivalDateTime >= startSimulationTime && arrivalDateTime <= endSimulationTime;
          });

          setFinalItineraries(finalItineraries);
          return prev;
        }

        const allFlightsArrived = updatedFlights.every((flight) => {
          const arrivalDateTime = parseISO(`${flight.arrival_date}T${flight.arrival_time}`);
          return currentDateTime > arrivalDateTime;
        });

        if (allFlightsArrived) {
          clearInterval(simulationIntervalRef.current);
          setIsSimulationEnded(true);
          setIsFinalPopupOpen(true);

          const endSimulationTime = newDateTime;
          const startSimulationTime = subHours(endSimulationTime, 6);
          const finalItineraries = updatedFlights.filter((flight) => {
            const arrivalDateTime = parseISO(`${flight.arrival_date}T${flight.arrival_time}`);
            return arrivalDateTime >= startSimulationTime && arrivalDateTime <= endSimulationTime;
          });

          setFinalItineraries(finalItineraries);
        }

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

  useEffect(() => {
    if (data.flights.length > 0) {
      const fleetSaturation = calculateFleetSaturation(data.flights);
      const airportSaturation = calculateAirportSaturation(airportCapacities);
      setPlaneSaturation(fleetSaturation);
      setAirportSaturation(airportSaturation);
    }
  }, [data.flights, airportCapacities]);

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
    setIsNuevoEnvioOpen(true);
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

  const getPlaneIcon = (capacityUsed, isSelected) => {
    if (isSelected) {
      if (capacityUsed >= 0.8) {
        return { icon: selectedPlaneIcons.red, rotatedIcon: selectedPlaneIcons.redRotado };
      } else if (capacityUsed >= 0.3) {
        return { icon: selectedPlaneIcons.yellow, rotatedIcon: selectedPlaneIcons.yellowRotado };
      } else {
        return { icon: selectedPlaneIcons.green, rotatedIcon: selectedPlaneIcons.greenRotado };
      }
    } else {
      if (capacityUsed >= 0.8) {
        return { icon: planeRed, rotatedIcon: planeRedRotado };
      } else if (capacityUsed >= 0.3) {
        return { icon: planeYellow, rotatedIcon: planeYellowRotado };
      } else {
        return { icon: planeGreen, rotatedIcon: planeGreenRotado };
      }
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

  const handleSearch = (searchTerm) => {
    const filteredAirports = data.airports.filter((airport) =>
      airport.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      airport.city.toLowerCase().includes(searchTerm.toLowerCase()) ||
      airport.code.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setData((prevData) => ({
      ...prevData,
      airports: filteredAirports,
    }));
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

    // Solo procesamos el envío si no ha sido procesado antes
    if (!processedShipments.has(shipment.id)) {
      // Manejo del registro de envíos en el aeropuerto de origen
      if (currentDateTime.getTime() === parseISO(shipment.registerDateTime).getTime()) {
        if (updatedAirports[shipment.departure_airport]) {
          if (updatedAirports[shipment.departure_airport].current_capacity + packageQuantity <= updatedAirports[shipment.departure_airport].max_capacity) {
            updatedAirports[shipment.departure_airport].current_capacity += packageQuantity;
            processedShipments.add(shipment.id); // Marcar el envío como procesado
          } else {
            console.error(`Error: Capacidad máxima excedida en ${shipment.departure_airport} al intentar agregar ${packageQuantity} paquetes.`);
          }
        } else {
          console.error(`Error: Aeropuerto ${shipment.departure_airport} no encontrado.`);
        }
      }

      if (currentDateTime.getTime() === arrivalDateTime.getTime()) {
        // El avión recoge los paquetes del aeropuerto de salida
        if (currentDateTime.getTime() === departureDateTime.getTime()) {
          if (updatedAirports[departure_airport_plane] && updatedAirports[departure_airport_plane].current_capacity >= packageQuantity) {
            updatedAirports[departure_airport_plane].current_capacity -= packageQuantity;
            processedShipments.add(shipment.id); // Marcar el envío como procesado
          } else {
            console.error(`Error: Capacidad insuficiente en ${departure_airport_plane} para retirar ${packageQuantity} paquetes.`);
          }
        }
        // El avión deja los paquetes en el aeropuerto de llegada (excepto si es el destino final)
        if (shipment.arrival_airport !== arrival_airport_plane) {
          if (updatedAirports[arrival_airport_plane]) {
            if (updatedAirports[arrival_airport_plane].current_capacity + packageQuantity <= updatedAirports[arrival_airport_plane].max_capacity) {
              updatedAirports[arrival_airport_plane].current_capacity += packageQuantity;
              processedShipments.add(shipment.id); // Marcar el envío como procesado
            } else {
              console.error(`Error: Capacidad máxima excedida en ${arrival_airport_plane} al intentar agregar ${packageQuantity} paquetes.`);
            }
          } else {
            console.error(`Error: Aeropuerto ${arrival_airport_plane} no encontrado.`);
          }
        }
      }
    }
  });
  
  const getDotIcon = (airportCode) => {
    const airport = updatedAirports[airportCode];
    const isSelected = selectedAirport && selectedAirport.code === airportCode;

    if (airport) {
      const usage = (airport.current_capacity) / airport.max_capacity;
      if (usage > 0.8) {
        return isSelected ? redDotSelected : redDot;
      } else if (usage > 0.3) {
        return isSelected ? yellowDotSelected : yellowDot;
      } else {
        return isSelected ? greenDotSelected : greenDot;
      }
    }
    return greenDot; // Valor por defecto si el aeropuerto no se encuentra
  };

  const getDotIconWithBorder = (airportCode, isSelected) => {
    const airport = updatedAirports[airportCode];
    const baseIcon = getDotIcon(airportCode);
  
    if (isSelected) {
      return {
        url: baseIcon,
        scaledSize: new window.google.maps.Size(35, 35),
        labelOrigin: new window.google.maps.Point(17, 45),
        label: {
          text: '',
          color: 'red',
          fontWeight: 'bold',
          fontSize: '12px',
          borderWidth: '4px',
          borderColor: 'red'
        }
      };
    }

    return {
      url: baseIcon,
      scaledSize: new window.google.maps.Size(35, 35),
    };
  };

  return (
    <>
      {data.airports.length > 0 &&
        data.airports.map((airport) => (
          <MarkerF
            key={airport.id}
            position={{ lat: parseFloat(airport.latitude), lng: parseFloat(airport.longitude) }}
            title={airport.code}
            icon={getDotIconWithBorder(airport.code, selectedAirport && selectedAirport.code === airport.code)}
            onClick={() => handleAirportClick(airport)}
          />
        ))}
      {activeFlights.length > 0 &&
        activeFlights.map((flight) => {
          const planePosition = calculatePlanePosition(flight, tiempo_simulacion.dia_actual, tiempo_simulacion.tiempo_actual);
          const capacityUsed = flight.current_load / flight.capacity;
          const isSelected = selectedFlight && selectedFlight.id === flight.id;
          const { icon, rotatedIcon } = getPlaneIcon(capacityUsed, isSelected);

          if (planePosition) {
            return (
              <MarkerF
                key={flight.id}
                position={{ lat: planePosition.lat, lng: planePosition.lng }}
                icon={{
                  url: planePosition.movingLeft ? rotatedIcon : icon,
                  scaledSize: new window.google.maps.Size(20, 20),
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
    setStartDateTime(parseISO(fechaInicio)); // Establecer la fecha y hora de inicio
    runAlgorithm(url, fechaInicio);
    const startDate = format(parseISO(fechaInicio), 'yyyy-MM-dd'); // Extraer la fecha
    const startHour = format(parseISO(fechaInicio), 'HH:mm:ss');

    setStartDate(startDate); // Establecer la fecha de inicio
    setStartHour(startHour);
  };

  const calculateFleetSaturation = (flights, currentDateTime) => {
    const activeFlights = flights.filter((flight) => {
      const departureDateTime = parseISO(`${flight.departure_date}T${flight.departure_time}`);
      const arrivalDateTime = parseISO(`${flight.arrival_date}T${flight.arrival_time}`);
      return currentDateTime >= departureDateTime && currentDateTime <= arrivalDateTime;
    });

    const totalCapacity = activeFlights.reduce((acc, flight) => acc + flight.capacity, 0);
    const currentLoad = activeFlights.reduce((acc, flight) => acc + flight.current_load, 0);
    return totalCapacity ? ((currentLoad / totalCapacity) * 100).toFixed(2) : '0';
  };

  const calculateAirportSaturation = (airportCapacities) => {
    const airportCodes = Object.keys(airportCapacities);
    const totalCapacity = airportCodes.reduce((acc, code) => acc + airportCapacities[code].max_capacity, 0);
    const currentLoad = airportCodes.reduce((acc, code) => acc + airportCapacities[code].current_capacity, 0);
    return totalCapacity ? ((currentLoad / totalCapacity) * 100).toFixed(2) : '0';
  };

  return (
    <>
      <GlobalStyle />
      <AppContainer>
        <Sidebar
          isCollapsed={isSidebarCollapsed}
          onCollapseToggle={toggleSidebar}
          onNuevoEnvioClick={handleOpenNuevoEnvio}
          onSimulacionClick={() => handleOpenPopup('Simulacion')}
        />
        <SidebarSearch
          onSearch={handleSearch}
          airports={data.airports}
          flights={currentFlights} // Pasar los vuelos en curso
          capacities={airportCapacities}
          currentSimulationDate={tiempo_simulacion.dia_actual}
          currentSimulationTime={tiempo_simulacion.tiempo_actual}
          countries={data.countries} // Agregar países aquí
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
                    center={{ lat: 15.7942, lng: 5.8822 }}
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
                isCollapsed={isSimulationSidebarCollapsed}
                onCollapseToggle={toggleSimulationSidebar}
                onClose={handleClosePopup}
                onStartSimulation={handleStartSimulation}
                onStopSimulation={stopSimulationInterval}
                data={data}
                tiempo_simulacion={tiempo_simulacion}
                planeSaturation={planeSaturation}
                airportSaturation={airportSaturation}
              />
            )}
            {selectedFlight && (
              <FlightInfoBox flight={selectedFlight} setSelectedFlight={setSelectedFlight} />
            )}

            {selectedAirport && (
              <InfoBox airport={selectedAirport} capacities={airportCapacities} setSelectedFlight={setSelectedFlight} setSelectedAirport={setSelectedAirport} selected={true} />
            )}
          </MainContent>
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
          <ElapsedTimeDisplay elapsedTime={tiempo_simulacion} startDate={startDate} startHour={startHour} />
        </InputContainer>
        <LocalTimeContainer>
          <p><strong>Fecha actual:</strong> {localTime.currentDate}</p>
          <p><strong>Hora actual:</strong> {localTime.currentTime}</p>
        </LocalTimeContainer>
        <NuevoEnvioPopup
          isOpen={isNuevoEnvioOpen}
          onRequestClose={handleCloseNuevoEnvio}
          data={data}  // Asegúrate de que data incluye countries, ciudad y airports
          tipoSimulacion={tipoSimulacion}
          runAlgorithm={runAlgorithm}
          tiempoSimulacion={tiempo_simulacion}
        />
        <FinalSimulationPopup
          isOpen={isFinalPopupOpen}
          onRequestClose={() => setIsFinalPopupOpen(false)}
          itineraries={finalItineraries}
          currentItineraries={currentItineraries} // Asegúrate de pasar los itinerarios actuales
        />
      </AppContainer>
    </>
  );
}

export default App;
