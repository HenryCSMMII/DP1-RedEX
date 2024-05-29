import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';
import axios from 'axios';

const ModalContent = styled.div`
  background: #fff;
  padding: 20px;
  max-width: 900px;
  margin: 40px auto;
  border-radius: 10px;
`;

const Header = styled.div`
  text-align: center;
  margin-bottom: 20px;
`;

const FormGroup = styled.div`
  margin-bottom: 15px;
`;

const Label = styled.label`
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
`;

const Input = styled.input`
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

const Select = styled.select`
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

const ButtonGroup = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
`;

const Button = styled.button`
  background-color: ${(props) => (props.primary ? '#6ba292' : '#ccc')};
  color: ${(props) => (props.primary ? '#fff' : '#000')};
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;

  &:hover {
    background-color: ${(props) => (props.primary ? '#5a8a7d' : '#bbb')};
  }
`;

const NuevoEnvioPopup = ({ isOpen, onRequestClose, data }) => {
  const [selectedCountryRemitente, setSelectedCountryRemitente] = useState('');
  const [selectedCountryDestinatario, setSelectedCountryDestinatario] = useState('');
  const [citiesRemitente, setCitiesRemitente] = useState([]);
  const [citiesDestinatario, setCitiesDestinatario] = useState([]);
  const [remitente, setRemitente] = useState({
    nombre: '',
    email: '',
    telefono: '',
    pais: '',
    ciudad: '',
    tipoDocumento: '',
    numeroDocumento: '',
  });
  const [destinatario, setDestinatario] = useState({
    nombre: '',
    email: '',
    telefono: '',
    pais: '',
    ciudad: '',
    tipoDocumento: '',
    numeroDocumento: '',
  });
  const [tipoServicio, setTipoServicio] = useState('');
  const [cantidadPaquetes, setCantidadPaquetes] = useState(1);

  useEffect(() => {
    if (data && selectedCountryRemitente) {
      const filteredCities = data.cities.filter(city => city.countryId === parseInt(selectedCountryRemitente));
      setCitiesRemitente(filteredCities);
    } else {
      setCitiesRemitente([]);
    }
  }, [selectedCountryRemitente, data]);

  useEffect(() => {
    if (data && selectedCountryDestinatario) {
      const filteredCities = data.cities.filter(city => city.countryId === parseInt(selectedCountryDestinatario));
      setCitiesDestinatario(filteredCities);
    } else {
      setCitiesDestinatario([]);
    }
  }, [selectedCountryDestinatario, data]);

  if (!data) {
    return null;
  }

  const handleSubmit = async () => {
    try {
      const shipmentResponse = await axios.post('http://localhost:8080/shipment/', {
        cantidad: cantidadPaquetes,
        origenId: remitente.ciudad,
        destinoId: destinatario.ciudad,
        tipo: tipoServicio,
        fechaInicio: new Date().toISOString().split('T')[0],
        fechaFin: new Date().toISOString().split('T')[0],
        tiempoActivo: 0,
      });

      const newShipmentId = shipmentResponse.data.id;

      await axios.post('http://localhost:8080/package/', {
        originId: remitente.ciudad,
        destinationId: destinatario.ciudad,
        departureTime: '08:00:00',
        shipmentDateTime: new Date().toISOString().split('T')[0],
        packageId: `PKG${Math.floor(Math.random() * 100000)}`,
        quantity: cantidadPaquetes,
        assignedFlightId: 1,
        tiempoTotal: 0,
        airportId: remitente.ciudad,
        estadoPaqueteId: 1,
        shipmentId: newShipmentId,
      });

      alert('Envio creado exitosamente');
      onRequestClose();
    } catch (error) {
      console.error('Error creating envio:', error);
      alert('Error creando el envio');
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      ariaHideApp={false}
      style={{
        overlay: {
          backgroundColor: 'rgba(0, 0, 0, 0.75)',
        },
        content: {
          top: '50%',
          left: '50%',
          right: 'auto',
          bottom: 'auto',
          marginRight: '-50%',
          transform: 'translate(-50%, -50%)',
          padding: 0,
          border: 'none',
          background: 'none',
        },
      }}
    >
      <ModalContent>
        <Header>
          <h2>Nuevo envío</h2>
        </Header>
        <FormGroup>
          <Label>#Envio</Label>
          <Input type="text" value="2222" readOnly />
        </FormGroup>
        <FormGroup>
          <Label>Remitente</Label>
          <Input type="text" placeholder="Nombre completo" onChange={(e) => setRemitente({ ...remitente, nombre: e.target.value })} />
          <Input type="email" placeholder="Correo electrónico" onChange={(e) => setRemitente({ ...remitente, email: e.target.value })} />
          <Input type="tel" placeholder="Número de teléfono" onChange={(e) => setRemitente({ ...remitente, telefono: e.target.value })} />
          <Select onChange={(e) => setSelectedCountryRemitente(e.target.value)}>
            <option value="">Seleccione un país</option>
            {data.countries.map(country => (
              <option key={country.id} value={country.id}>
                {country.name}
              </option>
            ))}
          </Select>
          <Select onChange={(e) => setRemitente({ ...remitente, ciudad: e.target.value })}>
            <option value="">Seleccione una ciudad</option>
            {citiesRemitente.map(city => (
              <option key={city.id} value={city.id}>
                {city.nombre}
              </option>
            ))}
          </Select>
          <Select onChange={(e) => setRemitente({ ...remitente, tipoDocumento: e.target.value })}>
            <option>Tipo de documento</option>
            <option value="DNI">DNI</option>
            <option value="Pasaporte">Pasaporte</option>
          </Select>
          <Input type="text" placeholder="Número de documento" onChange={(e) => setRemitente({ ...remitente, numeroDocumento: e.target.value })} />
        </FormGroup>
        <FormGroup>
          <Label>Destinatario</Label>
          <Input type="text" placeholder="Nombre completo" onChange={(e) => setDestinatario({ ...destinatario, nombre: e.target.value })} />
          <Input type="email" placeholder="Correo electrónico" onChange={(e) => setDestinatario({ ...destinatario, email: e.target.value })} />
          <Input type="tel" placeholder="Número de teléfono" onChange={(e) => setDestinatario({ ...destinatario, telefono: e.target.value })} />
          <Select onChange={(e) => setSelectedCountryDestinatario(e.target.value)}>
            <option value="">Seleccione un país</option>
            {data.countries.map(country => (
              <option key={country.id} value={country.id}>
                {country.name}
              </option>
            ))}
          </Select>
          <Select onChange={(e) => setDestinatario({ ...destinatario, ciudad: e.target.value })}>
            <option value="">Seleccione una ciudad</option>
            {citiesDestinatario.map(city => (
              <option key={city.id} value={city.id}>
                {city.nombre}
              </option>
            ))}
          </Select>
          <Select onChange={(e) => setDestinatario({ ...destinatario, tipoDocumento: e.target.value })}>
            <option>Tipo de documento</option>
            <option value="DNI">DNI</option>
            <option value="Pasaporte">Pasaporte</option>
          </Select>
          <Input type="text" placeholder="Número de documento" onChange={(e) => setDestinatario({ ...destinatario, numeroDocumento: e.target.value })} />
        </FormGroup>
        <FormGroup>
          <Label>Información adicional</Label>
          <Select onChange={(e) => setTipoServicio(e.target.value)}>
            <option>Tipo de servicio</option>
            <option value="1">Estándar</option>
            <option value="2">Express</option>
          </Select>
          <Input type="number" placeholder="Cantidad de paquetes" value={cantidadPaquetes} onChange={(e) => setCantidadPaquetes(e.target.value)} />
        </FormGroup>
        <ButtonGroup>
          <Button onClick={onRequestClose}>Cancelar</Button>
          <Button primary onClick={handleSubmit}>Crear envío</Button>
        </ButtonGroup>
      </ModalContent>
    </Modal>
  );
};

export default NuevoEnvioPopup;
