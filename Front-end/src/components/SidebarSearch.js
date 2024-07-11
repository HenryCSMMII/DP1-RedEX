import React, { useState } from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import styled from 'styled-components';

const SidebarContainer = styled.div`
  padding: 10px;
  background: #fff;
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow-y: auto;
  box-shadow: 2px 0px 5px rgba(0,0,0,0.1);
`;

const Form = styled.form`
  width: 100%;
  display: flex;
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

const SidebarSearch = ({ onSearch, airports, capacities }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedTab, setSelectedTab] = useState('Envios');

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    onSearch(searchTerm);
  };

  const handleTabSelect = (index) => {
    const tabNames = ['Envios', 'Aeropuertos', 'Vuelos'];
    setSelectedTab(tabNames[index]);
  };

  return (
    <SidebarContainer>
      <Tabs onSelect={handleTabSelect} style={{ width: '100%' }}>
        <TabList style={{ display: 'flex', justifyContent: 'center' }}>
          <Tab>Envios</Tab>
          <Tab>Aeropuertos</Tab>
          <Tab>Vuelos</Tab>
        </TabList>

        <TabPanel>
          <Form onSubmit={handleSearchSubmit}>
            <Input
              type="text"
              placeholder="Buscar envios"
              value={searchTerm}
              onChange={handleSearchChange}
            />
            <Button type="submit">Buscar</Button>
          </Form>
        </TabPanel>
        <TabPanel>
          {selectedTab === 'Aeropuertos' && airports.map((airport) => (
            <AirportInfo key={airport.code}>
              <p><strong>{airport.name} ({airport.code})</strong></p>
              <p>País: {[airport.city]}</p>
              <p>Capacidad: {capacities[airport.code]?.current_capacity}/{capacities[airport.code]?.max_capacity}</p>
            </AirportInfo>
          ))}
        </TabPanel>
        <TabPanel>
          <Form onSubmit={handleSearchSubmit}>
            <Input
              type="text"
              placeholder="Buscar vuelos"
              value={searchTerm}
              onChange={handleSearchChange}
            />
            <Button type="submit">Buscar</Button>
          </Form>
        </TabPanel>
      </Tabs>
    </SidebarContainer>
  );
};

export default SidebarSearch;
