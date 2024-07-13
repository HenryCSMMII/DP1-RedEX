import React, { useState, useEffect } from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import styled from 'styled-components';
import { parseISO, isWithinInterval } from 'date-fns';
import axios from 'axios';

const SidebarContainer = styled.div`
  padding: 10px;
  background: #fff;
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow-y: auto;
  box-shadow: 2px 0px 5px rgba(0,0,0,0.1);
  transition: width 0.3s;
  width: ${({ isCollapsed }) => (isCollapsed ? '60px' : '300px')};
`;

const ToggleButton = styled.button`
  width: 100%;
  padding: 5px;
  background-color: #007bff;
  color: #fff;
  border: none;
  cursor: pointer;
  margin-bottom: 10px;
`;

const Form = styled.form`
  width: 100%;
  display: ${({ isCollapsed }) => (isCollapsed ? 'none' : 'flex')};
  flex-direction: column;
  align-items: center;
  padding: 10px 0;
`;

const Input = styled.input`
  width: 80%;
  padding: 5px;
  margin-bottom: 10px;
`;

const Button = styled.button`
  width: 80%;
  padding: 5px;
`;

const AirportInfo = styled.div`
  margin-bottom: 10px;
  text-align: left;
  width: 80%;
`;

const FlightInfo = styled.div`
  margin-bottom: 10px;
  text-align: left;
  width: 80%;
`;

const ShipmentInfo = styled.div`
  margin-bottom: 10px;
  text-align: left;
  width: 80%;
`;

const SidebarSearch = ({ onSearch, airports = [], flights = [], capacities = {}, currentSimulationDate, currentSimulationTime, countries = [] }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedTab, setSelectedTab] = useState('Aeropuertos');
  const [filteredAirports, setFilteredAirports] = useState(airports);
  const [filteredFlights, setFilteredFlights] = useState([]);
  const [filteredShipments, setFilteredShipments] = useState([]);
  const [shipments, setShipments] = useState([]);
  const [isCollapsed, setIsCollapsed] = useState(false);
  const [flightIdSearchTerm, setFlightIdSearchTerm] = useState('');
  const [shipmentIdSearchTerm, setShipmentIdSearchTerm] = useState('');

  useEffect(() => {
    const fetchShipments = async () => {
      try {
        const response = await axios.get('http://localhost:8080/shipment/');
        setShipments(response.data);
      } catch (error) {
        console.error('Error fetching shipments:', error);
      }
    };

    fetchShipments();

    const intervalId = setInterval(fetchShipments, 5000); // Actualizar cada 5 segundos

    return () => clearInterval(intervalId); // Limpiar el intervalo al desmontar el componente
  }, []);

  const handleSearchChange = (e) => {
    if (selectedTab === 'Vuelos') {
      setFlightIdSearchTerm(e.target.value);
    } else if (selectedTab === 'Envios') {
      setShipmentIdSearchTerm(e.target.value);
    } else {
      setSearchTerm(e.target.value);
    }
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    if (selectedTab === 'Vuelos') {
      const filtered = flights.filter(
        (flight) => {
          const departureDateTime = parseISO(`${flight.departure_date}T${flight.departure_time}`);
          const arrivalDateTime = parseISO(`${flight.arrival_date}T${flight.arrival_time}`);
          const isFlightIdMatch = String(flight.id).toLowerCase().includes(flightIdSearchTerm.toLowerCase());
          const isWithinTimeFrame = isWithinInterval(parseISO(`${currentSimulationDate}T${currentSimulationTime}`), { start: departureDateTime, end: arrivalDateTime });
          return isFlightIdMatch && isWithinTimeFrame;
        }
      );
      setFilteredFlights(filtered);
    } else if (selectedTab === 'Envios') {
      const filtered = shipments.filter(
        (shipment) => String(shipment.id).toLowerCase().includes(shipmentIdSearchTerm.toLowerCase())
      );
      setFilteredShipments(filtered);
    } else {
      onSearch(searchTerm, selectedTab);
    }
  };

  const handleTabSelect = (index) => {
    const tabNames = ['Aeropuertos', 'Vuelos', 'Envios'];
    setSelectedTab(tabNames[index]);
  };

  const toggleSidebar = () => {
    setIsCollapsed(!isCollapsed);
  };
 useEffect(() => {
    if (selectedTab === 'Aeropuertos') {
      setFilteredAirports(
        airports.filter(
          (airport) =>
            (airport.name && airport.name.toLowerCase().includes(searchTerm.toLowerCase())) ||
            (airport.code && airport.code.toLowerCase().includes(searchTerm.toLowerCase())) ||
            (airport.city && airport.city.toLowerCase().includes(searchTerm.toLowerCase()))
        )
      );
    } else if (selectedTab === 'Vuelos') {
      const currentDateTime = parseISO(`${currentSimulationDate}T${currentSimulationTime}`);

      const filtered = flights.filter(
        (flight) => {
          const departureDateTime = parseISO(`${flight.departure_date}T${flight.departure_time}`);
          const arrivalDateTime = parseISO(`${flight.arrival_date}T${flight.arrival_time}`);
          return isWithinInterval(currentDateTime, { start: departureDateTime, end: arrivalDateTime });
        }
      );
      setFilteredFlights(filtered);
    } else if (selectedTab === 'Envios') {
      setFilteredShipments(shipments);
    }
  }, [searchTerm, selectedTab, airports, flights, shipments, currentSimulationDate, currentSimulationTime]);

  const getCountryName = (airportCode) => {
    const airport = airports.find((a) => a.id === airportCode);
    return airport ? airport.city : 'Desconocido';
  };

  return (
    <SidebarContainer isCollapsed={isCollapsed}>
      <ToggleButton onClick={toggleSidebar}>
        {isCollapsed ? 'Expandir' : 'Contraer'}
      </ToggleButton>
      <Tabs onSelect={handleTabSelect} style={{ width: '100%' }}>
        <TabList style={{ display: 'flex', justifyContent: 'center' }}>
          <Tab>Aeropuertos</Tab>
          <Tab>Vuelos</Tab>
          <Tab>Envios</Tab>
        </TabList>
        <TabPanel>
          <Form onSubmit={handleSearchSubmit} isCollapsed={isCollapsed}>
            <Input
              type="text"
              placeholder="Buscar aeropuertos"
              value={searchTerm}
              onChange={handleSearchChange}
            />
            <Button type="submit">Buscar</Button>
          </Form>
          {!isCollapsed && filteredAirports.map((airport) => (
            <AirportInfo key={airport.code}>
              <p><strong>{airport.name} ({airport.code})</strong></p>
              <p>País: {airport.city}</p>
              <p>Capacidad: {capacities[airport.code]?.current_capacity}/{capacities[airport.code]?.max_capacity}</p>
            </AirportInfo>
          ))}
        </TabPanel>
        <TabPanel>
          <Form onSubmit={handleSearchSubmit} isCollapsed={isCollapsed}>
            <Input
              type="text"
              placeholder="Buscar vuelos"
              value={flightIdSearchTerm}
              onChange={handleSearchChange}
            />
            <Button type="submit">Buscar</Button>
          </Form>
          {!isCollapsed && (
            <>
              <h3>Vuelos filtrados:</h3>
              {filteredFlights.map((flight) => (
                <FlightInfo key={flight.id}>
                  <p><strong>ID del vuelo: {flight.id}</strong></p>
                  <p>Origen: {flight.origin}</p>
                  <p>Destino: {flight.destination}</p>
                  <p>Salida: {flight.departure_date} {flight.departure_time}</p>
                  <p>Llegada: {flight.arrival_date} {flight.arrival_time}</p>
                  <p>Capacidad: {flight.current_load}/{flight.capacity}</p>
                </FlightInfo>
              ))}
            </>
          )}
        </TabPanel>
        <TabPanel>
          <Form onSubmit={handleSearchSubmit} isCollapsed={isCollapsed}>
            <Input
              type="text"
              placeholder="Buscar envios"
              value={shipmentIdSearchTerm}
              onChange={handleSearchChange}
            />
            <Button type="submit">Buscar</Button>
          </Form>
          {!isCollapsed && (
            <>
              <h3>Envios filtrados:</h3>
              {filteredShipments.map((shipment) => (
                <ShipmentInfo key={shipment.id}>
                  <p><strong>ID del envio: {shipment.id}</strong></p>
                  <p>Cantidad de paquetes: {shipment.packageQuantity}</p>
                  <p>País de salida: {getCountryName(shipment.departureAirportId)}</p>
                  <p>País de llegada: {getCountryName(shipment.arrivalAirportId)}</p>
                  <p>Estado: {shipment.state}</p>
                  <p>Fecha-hora de registro: {new Date(shipment.registerDateTime).toLocaleString()}</p>
                </ShipmentInfo>
              ))}
            </>
          )}
        </TabPanel>
      </Tabs>
    </SidebarContainer>
  );
};

export default SidebarSearch;