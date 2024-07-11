import React, { useState, useEffect } from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';

const SidebarSearch = ({ onSearch, airports, capacities, allShipments }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedTab, setSelectedTab] = useState('Envios');
  const [filteredShipments, setFilteredShipments] = useState([]);

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

  useEffect(() => {
    const currentDateTime = new Date(); // Asume que tienes acceso al tiempo actual simulado

    // Filtrar los envíos que están en vuelo o han llegado a un aeropuerto pero aún no a su destino final
    const activeShipments = allShipments.filter(shipment => {
      const arrivalDateTime = new Date(shipment.arrival_date_time_plane);
      return currentDateTime <= arrivalDateTime && shipment.status !== 'delivered';
    });

    // Filtrar por el término de búsqueda
    const result = activeShipments.filter(shipment => shipment.code.toLowerCase().includes(searchTerm.toLowerCase()));

    setFilteredShipments(result);
  }, [allShipments, searchTerm]);

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
              placeholder="Buscar envíos"
              value={searchTerm}
              onChange={handleSearchChange}
              style={{ width: '80%', padding: '5px', marginBottom: '10px' }}
            />
            <button type="submit" style={{ width: '80%', padding: '5px' }}>Buscar</button>
          </form>
          {filteredShipments.map((shipment) => (
            <div key={shipment.id} style={{ marginBottom: '10px', textAlign: 'left', width: '80%' }}>
              <p><strong>Código:</strong> {shipment.code}</p>
              <p><strong>Origen:</strong> {shipment.departure_airport_plane}</p>
              <p><strong>Destino:</strong> {shipment.arrival_airport_plane}</p>
              <p><strong>Cantidad de paquetes:</strong> {shipment.packageQuantity}</p>
              <p><strong>Estado:</strong> {shipment.status}</p>
            </div>
          ))}
        </TabPanel>

        <TabPanel>
          {selectedTab === 'Aeropuertos' && airports.map((airport) => (
            <div key={airport.code} style={{ marginBottom: '10px', textAlign: 'left', width: '80%' }}>
              <p><strong>{airport.name} ({airport.code})</strong></p>
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
