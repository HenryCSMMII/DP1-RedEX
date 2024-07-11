import React, { useState } from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';

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
    <div style={{ padding: '10px', background: '#fff', height: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <Tabs onSelect={handleTabSelect} style={{ width: '100%' }}>
        <TabList style={{ display: 'flex', justifyContent: 'center' }}>
          <Tab>Envios</Tab>
          <Tab>Aeropuertos</Tab>
          <Tab>Vuelos</Tab>
        </TabList>

        <TabPanel>
          <form onSubmit={handleSearchSubmit} style={{ width: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <input
              type="text"
              placeholder="Buscar envios"
              value={searchTerm}
              onChange={handleSearchChange}
              style={{ width: '80%', padding: '5px', marginBottom: '10px' }}
            />
            <button type="submit" style={{ width: '80%', padding: '5px' }}>Buscar</button>
          </form>
        </TabPanel>
        <TabPanel>
          {selectedTab === 'Aeropuertos' && airports.map((airport) => (
            <div key={airport.code} style={{ marginBottom: '10px', textAlign: 'left', width: '80%' }}>
              <p><strong>{airport.name} ({airport.code})</strong></p>
              <p>País: {[airport.city]}</p>
              <p>Capacidad: {capacities[airport.code]?.current_capacity}/{capacities[airport.code]?.max_capacity}</p>
            </div>
          ))}
        </TabPanel>
        <TabPanel>
          <form onSubmit={handleSearchSubmit} style={{ width: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <input
              type="text"
              placeholder="Buscar vuelos"
              value={searchTerm}
              onChange={handleSearchChange}
              style={{ width: '80%', padding: '5px', marginBottom: '10px' }}
            />
            <button type="submit" style={{ width: '80%', padding: '5px' }}>Buscar</button>
          </form>
        </TabPanel>
      </Tabs>
    </div>
  );
};

export default SidebarSearch;
