import React, { useState, useEffect, useRef } from 'react';
import styled, { createGlobalStyle } from 'styled-components';
import { GoogleMap, LoadScript, MarkerF } from '@react-google-maps/api';
import { format, addMinutes, parseISO } from 'date-fns';
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
        <p><strong>Código del aeropuerto:</strong> {airport.code}</p>
        <p><strong>Nombre del aeropuerto:</strong> {airport.name}</p>
        <p><strong>Latitud:</strong> {airport.latitude}</p>
        <p><strong>Longitud:</strong> {airport.longitude}</p>
        {capacities[airport.code] && (
          <>
            <p><strong>Capacidad actual:</strong> {capacities[airport.code].current_capacity}/{capacities[airport.code].max_capacity} (<span style={{ color: 'orange' }}>{calculateCurrentCapacityPercentage(capacities[airport.code].current_capacity, capacities[airport.code].max_capacity)}</span>)</p>
          </>
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
          <p><strong>Fecha-hora de Salida:</strong> {new Date(`${flight.departure_date}T${flight.departure_time}`).toLocaleString()}</p>
        )}
        {flight.arrival_date && (
          <p><strong>Fecha-hora de Llegada:</strong> {new Date(`${flight.arrival_date}T${flight.arrival_time}`).toLocaleString()}</p>
        )}
        {<p><strong>Capacidad máxima:</strong> {flight.current_load}/{flight.capacity}</p>}
        {<p><strong>Saturación:</strong> {calculateSaturation(flight.current_load, flight.capacity)}</p>}
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
  const [elapsedTime, setElapsedTime] = useState({ days: 0, hours: 0, minutes: 0, seconds: 0 });

  useEffect(() => {
    if (startDateTime) {
      const intervalId = setInterval(() => {
        const now = new Date();
        const diff = now - startDateTime;

        const days = Math.floor(diff / (1000 * 60 * 60 * 24));
        const hours = Math.floor((diff / (1000 * 60 * 60)) % 24);
        const minutes = Math.floor((diff / 1000 / 60) % 60);
        const seconds = Math.floor((diff / 1000) % 60);

        setElapsedTime({ days, hours, minutes, seconds });
      }, 1000);

      return () => clearInterval(intervalId);
    }
  }, [startDateTime]);

  const ElapsedTimeDisplay = ({ elapsedTime }) => (
    <div>
      <p><strong>Tiempo transcurrido:</strong> {elapsedTime.days} días {elapsedTime.hours} h {elapsedTime.minutes} min {elapsedTime.seconds} s</p>
    </div>
  );

  useEffect(() => {
    const updateLocalTime = () => {
      const now = new Date();
      const date = now.toISOString().split('T')[0];
      const time = now.toTimeString().split(' ')[0];
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
  const [allShipments, setAllShipments] = useState([]);
  const [airportCapacities, setAirportCapacities] = useState({});

  const fetchData = async () => {
    try {
      const [airports, continents, countries] = await Promise.all([
        axios.get('http://localhost:8080/airport/'),
        axios.get('http://localhost:8080/continent/'),
        axios.get('http://localhost:8080/country/'),
      ]);

      console.log('Airports data:', airports.data);

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
          city: country ? country.name : 'Desconocido'
        };
      });

      setAirportCapacities(initialCapacities);

      setData((prevData) => ({
        ...prevData,
        airports: updatedAirports,
        continents: continents.data,
        countries: countries.data,
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
            current_load: flight.used_capacity[0] * 2,
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
          dia_actual: fechaInicio.split('T')[0],
          tiempo_actual: fechaInicio.split('T')[1],
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
const updateFlights = (flights, currentDateTime) => {
  return flights.map(flight => {
    return { ...flight };
  });
};

const updateAirportCapacities = (airportCapacities, allShipments, currentDateTime) => {
  const updatedCapacities = { ...airportCapacities };

  allShipments.forEach((shipment) => {
    const { departure_date_time_plane, arrival_date_time_plane, departure_airport_plane, arrival_airport_plane, packageQuantity } = shipment;

    const departureDateTime = parseISO(departure_date_time_plane);
    const arrivalDateTime = parseISO(arrival_date_time_plane);    

    // Manejo del registro de envíos en el aeropuerto de origen
    if (currentDateTime.getTime() === parseISO(shipment.registerDateTime).getTime()) {
      if (updatedCapacities[shipment.departure_airport]) {
        updatedCapacities[shipment.departure_airport].current_capacity += shipment.packageQuantity * 1;
      }
    }

    if (currentDateTime >= departureDateTime && currentDateTime <= arrivalDateTime) {
      // El avión recoge los paquetes del aeropuerto de salida
      if (currentDateTime.getTime() === departureDateTime.getTime()) {
        if (updatedCapacities[departure_airport_plane] && updatedCapacities[departure_airport_plane].current_capacity >= packageQuantity) {
          updatedCapacities[departure_airport_plane].current_capacity -= packageQuantity * 1;
        }
      }
      // El avión deja los paquetes en el aeropuerto de llegada (excepto si es el destino final)
      if (currentDateTime.getTime() === arrivalDateTime.getTime() && shipment.arrival_airport !== arrival_airport_plane) {
        if (updatedCapacities[arrival_airport_plane]) {
          updatedCapacities[arrival_airport_plane].current_capacity += packageQuantity * 1;
        }
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
      const newDateTime = addMinutes(currentDateTime, 30);

      const newDate = format(newDateTime, 'yyyy-MM-dd');
      const newTime = format(newDateTime, 'HH:mm:ss');

      // Actualizar las capacidades de aeropuertos y vuelos aquí basado en la nueva fecha y hora de simulación
      const updatedFlights = updateFlights(data.flights, newDateTime);
      const updatedAirportCapacities = updateAirportCapacities(airportCapacities, allShipments, newDateTime);

      // Calcular saturaciones
      const fleetSaturation = calculateFleetSaturation(updatedFlights, newDateTime);
      const airportSaturation = calculateAirportSaturation(updatedAirportCapacities);
      
      // Actualizar estados
      setPlaneSaturation(fleetSaturation);
      setAirportSaturation(airportSaturation);

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

      // Manejo del registro de envíos en el aeropuerto de origen
      if (currentDateTime.getTime() === parseISO(shipment.registerDateTime).getTime()) {
        if (updatedAirports[shipment.departure_airport]) {
          updatedAirports[shipment.departure_airport].current_capacity += shipment.packageQuantity * 1;
          console.log(`Envio ID: ${shipment.id} registrado en ${shipment.departure_airport} con ${shipment.packageQuantity} paquetes. Capacidad actual: ${updatedAirports[shipment.departure_airport].current_capacity}`);
        } else {
          console.error(`Error: Aeropuerto ${shipment.departure_airport} no encontrado.`);
        }
      }

      if (currentDateTime >= departureDateTime && currentDateTime <= arrivalDateTime) {
        // El avión recoge los paquetes del aeropuerto de salida
        if (currentDateTime.getTime() === departureDateTime.getTime()) {
          if (updatedAirports[departure_airport_plane] && updatedAirports[departure_airport_plane].current_capacity >= packageQuantity) {
            updatedAirports[departure_airport_plane].current_capacity -= packageQuantity * 1;
            console.log(`Envio ID: ${shipment.id} con ${packageQuantity} paquetes se fue de ${departure_airport_plane}. Capacidad actual: ${updatedAirports[departure_airport_plane].current_capacity}`);
          } else {
            console.error(`Error: Capacidad insuficiente en ${departure_airport_plane} para retirar ${packageQuantity} paquetes.`);
          }
        }
        // El avión deja los paquetes en el aeropuerto de llegada (excepto si es el destino final)
        if (currentDateTime.getTime() === arrivalDateTime.getTime() && shipment.arrival_airport !== arrival_airport_plane) {
          if (updatedAirports[arrival_airport_plane]) {
            updatedAirports[arrival_airport_plane].current_capacity += packageQuantity * 1;
            console.log(`Envio ID: ${shipment.id} con ${packageQuantity} paquetes llegó a ${arrival_airport_plane}. Capacidad actual: ${updatedAirports[arrival_airport_plane].current_capacity}`);
          } else {
            console.error(`Error: Aeropuerto ${arrival_airport_plane} no encontrado.`);
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
          onNuevoEnvioClick={handleOpenNuevoEnvio}
          onVuelosClick={() => handleOpenPopup('Vuelos')}
          onAeropuertosClick={() => handleOpenPopup('Aeropuertos')}
          onReportesClick={() => handleOpenPopup('Reportes')}
          onSimulacionClick={() => handleOpenPopup('Simulacion')}
          onDetenerSimulacionClick={stopSimulationInterval}
        />
        <SidebarSearch
          onSearch={handleSearch}
          airports={data.airports}
          countries={data.countries} // Pasar los países a SidebarSearch
          capacities={airportCapacities}
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
          <ElapsedTimeDisplay elapsedTime={elapsedTime} />
        </InputContainer>
        <LocalTimeContainer>
          <p><strong>Fecha actual:</strong> {localTime.currentDate}</p>
          <p><strong>Hora actual:</strong> {localTime.currentTime}</p>
        </LocalTimeContainer>
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
        <ReportesPopup
		  isOpen={activePopup === 'Reportes'}
		  onRequestClose={handleClosePopup}
		  data={data}
		  airportCapacities={airportCapacities}
		/>
      </AppContainer>
    </>
  );
}

export default App;
