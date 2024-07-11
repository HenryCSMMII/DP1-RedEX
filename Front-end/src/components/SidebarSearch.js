import React, { useState, useEffect } from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import styled from 'styled-components';
import { parseISO, isWithinInterval } from 'date-fns';

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

const SidebarSearch = ({ onSearch, airports = [], flights = [], capacities = {}, currentSimulationDate, currentSimulationTime }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedTab, setSelectedTab] = useState('Aeropuertos');
  const [filteredAirports, setFilteredAirports] = useState(airports);
  const [filteredFlights, setFilteredFlights] = useState([]);
  const [isCollapsed, setIsCollapsed] = useState(false);
	const [flightIdSearchTerm, setFlightIdSearchTerm] = useState('');

  const handleSearchChange = (e) => {
  if (selectedTab === 'Vuelos') {
    setFlightIdSearchTerm(e.target.value);
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
  } else {
    onSearch(searchTerm, selectedTab);
  }
};
  const handleTabSelect = (index) => {
    const tabNames = ['Aeropuertos', 'Vuelos'];
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
    }
  }, [searchTerm, selectedTab, airports, flights, currentSimulationDate, currentSimulationTime]);

  useEffect(() => {
    if (selectedTab === 'Vuelos') {
      const currentDateTime = parseISO(`${currentSimulationDate}T${currentSimulationTime}`);

      const filtered = flights.filter(
        (flight) => {
          const departureDateTime = parseISO(`${flight.departure_date}T${flight.departure_time}`);
          const arrivalDateTime = parseISO(`${flight.arrival_date}T${flight.arrival_time}`);
          return isWithinInterval(currentDateTime, { start: departureDateTime, end: arrivalDateTime });
        }
      );
      setFilteredFlights(filtered);
    }
  }, [selectedTab, flights, currentSimulationDate, currentSimulationTime]);

  return (
    <SidebarContainer isCollapsed={isCollapsed}>
      <ToggleButton onClick={toggleSidebar}>
        {isCollapsed ? 'Expandir' : 'Contraer'}
      </ToggleButton>
      <Tabs onSelect={handleTabSelect} style={{ width: '100%' }}>
        <TabList style={{ display: 'flex', justifyContent: 'center' }}>
          <Tab>Aeropuertos</Tab>
          <Tab>Vuelos</Tab>
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

      </Tabs>
    </SidebarContainer>
  );
};

export default SidebarSearch;
