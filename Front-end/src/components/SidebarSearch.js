// SidebarSearch.js
import React, { useState } from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';

const SidebarSearch = ({ onSearch }) => {
  const [searchTerm, setSearchTerm] = useState('');

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    onSearch(searchTerm);
  };

  return (
    <div style={{ padding: '10px', background: '#fff', height: '100%' }}>
      <Tabs>
        <TabList>
          <Tab>Envios</Tab>
          <Tab>Aeropuertos</Tab>
          <Tab>Vuelos</Tab>
        </TabList>

        <TabPanel>
          <form onSubmit={handleSearchSubmit}>
            <input
              type="text"
              placeholder="Buscar envios"
              value={searchTerm}
              onChange={handleSearchChange}
              style={{ width: '100%', padding: '5px', marginBottom: '10px' }}
            />
            <button type="submit" style={{ width: '100%', padding: '5px' }}>Buscar</button>
          </form>
        </TabPanel>
        <TabPanel>
          <form onSubmit={handleSearchSubmit}>
            <input
              type="text"
              placeholder="Buscar aeropuertos"
              value={searchTerm}
              onChange={handleSearchChange}
              style={{ width: '100%', padding: '5px', marginBottom: '10px' }}
            />
            <button type="submit" style={{ width: '100%', padding: '5px' }}>Buscar</button>
          </form>
        </TabPanel>
        <TabPanel>
          <form onSubmit={handleSearchSubmit}>
            <input
              type="text"
              placeholder="Buscar vuelos"
              value={searchTerm}
              onChange={handleSearchChange}
              style={{ width: '100%', padding: '5px', marginBottom: '10px' }}
            />
            <button type="submit" style={{ width: '100%', padding: '5px' }}>Buscar</button>
          </form>
        </TabPanel>
      </Tabs>
    </div>
  );
};

export default SidebarSearch;
