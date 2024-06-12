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
  margin-bottom: 10px;
`;

const Select = styled.select`
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 10px;
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
  const [cantidadPaquetes, setCantidadPaquetes] = useState(1);
  const [fechaEnvio, setFechaEnvio] = useState(new Date().toISOString().split('T')[0]);

  useEffect(() => {
    if (data && selectedCountryRemitente) {
      const filteredCities = data.airports.filter(airport => airport.countryId === parseInt(selectedCountryRemitente));
      setCitiesRemitente(filteredCities);
    } else {
      setCitiesRemitente([]);
    }
  }, [selectedCountryRemitente, data]);

  useEffect(() => {
    if (data && selectedCountryDestinatario) {
      const filteredCities = data.airports.filter(airport => airport.countryId === parseInt(selectedCountryDestinatario));
      setCitiesDestinatario(filteredCities);
    } else {
      setCitiesDestinatario([]);
    }
  }, [selectedCountryDestinatario, data]);

  if (!data) {
    return null;
  }

  const handleInputChange = (e, setState, state) => {
    const { name, value } = e.target;
    setState({ ...state, [name]: value });
  };

  const handleNumberInputChange = (e, setState, state) => {
    const { name, value } = e.target;
    if (/^\d*$/.test(value)) {
      setState({ ...state, [name]: value });
    }
  };

  const handleSubmit = async () => {
    try {
      const shipmentPayload = {
        packageQuantity: cantidadPaquetes,
        departureAirportId: remitente.ciudad,
        arrivalAirportId: destinatario.ciudad,
        state: null,
        departureTime: fechaEnvio,
        arrivalTime: fechaEnvio,
        clientSenderId: null,
      };

      // Verificar el payload de envío
      console.log("Shipment payload:", shipmentPayload);

      const shipmentResponse = await axios.post('http://localhost:8080/shipment/', shipmentPayload);
      const newShipmentId = shipmentResponse.data.id;

      // Crear múltiples paquetes según la cantidad de paquetes
      for (let i = 0; i < cantidadPaquetes; i++) {
        const packagePayload = {
          departureAirportId: remitente.ciudad,
          arrivalAirportId: destinatario.ciudad,
          departureTime: '08:00:00',
          arrivalTime: fechaEnvio,
          clientSenderId: null,
          shipmentId: newShipmentId,
        };

        // Verificar el payload de paquete
        console.log("Package payload:", packagePayload);

        await axios.post('http://localhost:8080/package/', packagePayload);
      }

      alert('Envio creado exitosamente');
      onRequestClose();
    } catch (error) {
      console.error('Error creating envio:', error);
      if (error.response) {
        console.error('Error response data:', error.response.data);
        console.error('Error response status:', error.response.status);
      }
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
          <Label>Remitente</Label>
          <Input
            type="text"
            name="nombre"
            placeholder="Nombre completo"
            onChange={(e) => handleInputChange(e, setRemitente, remitente)}
          />
          <Input
            type="email"
            name="email"
            placeholder="Correo electrónico"
            onChange={(e) => handleInputChange(e, setRemitente, remitente)}
          />
          <Input
            type="tel"
            name="telefono"
            placeholder="Número de teléfono"
            inputMode="numeric"
            onChange={(e) => handleNumberInputChange(e, setRemitente, remitente)}
          />
          <Select name="pais" onChange={(e) => setSelectedCountryRemitente(e.target.value)}>
            <option value="">Seleccione un país</option>
            {data.countries.map((country) => (
              <option key={country.id} value={country.id}>
                {country.name}
              </option>
            ))}
          </Select>
          <Select name="ciudad" onChange={(e) => handleInputChange(e, setRemitente, remitente)}>
            <option value="">Seleccione una ciudad</option>
            {citiesRemitente.map((airport) => (
              <option key={airport.id} value={airport.id}>
                {airport.code}
              </option>
            ))}
          </Select>
          <Select name="tipoDocumento" onChange={(e) => handleInputChange(e, setRemitente, remitente)}>
            <option>Tipo de documento</option>
            <option value="DNI">DNI</option>
            <option value="Pasaporte">Pasaporte</option>
          </Select>
          <Input
            type="text"
            name="numeroDocumento"
            placeholder="Número de documento"
            inputMode="numeric"
            onChange={(e) => handleNumberInputChange(e, setRemitente, remitente)}
          />
        </FormGroup>
        <FormGroup>
          <Label>Destinatario</Label>
          <Input
            type="text"
            name="nombre"
            placeholder="Nombre completo"
            onChange={(e) => handleInputChange(e, setDestinatario, destinatario)}
          />
          <Input
            type="email"
            name="email"
            placeholder="Correo electrónico"
            onChange={(e) => handleInputChange(e, setDestinatario, destinatario)}
          />
          <Input
            type="tel"
            name="telefono"
            placeholder="Número de teléfono"
            inputMode="numeric"
            onChange={(e) => handleNumberInputChange(e, setDestinatario, destinatario)}
          />
          <Select name="pais" onChange={(e) => setSelectedCountryDestinatario(e.target.value)}>
            <option value="">Seleccione un país</option>
            {data.countries.map((country) => (
              <option key={country.id} value={country.id}>
                {country.name}
              </option>
            ))}
          </Select>
          <Select name="ciudad" onChange={(e) => handleInputChange(e, setDestinatario, destinatario)}>
            <option value="">Seleccione una ciudad</option>
            {citiesDestinatario.map((airport) => (
              <option key={airport.id} value={airport.id}>
                {airport.code}
              </option>
            ))}
          </Select>
          <Select name="tipoDocumento" onChange={(e) => handleInputChange(e, setDestinatario, destinatario)}>
            <option>Tipo de documento</option>
            <option value="DNI">DNI</option>
            <option value="Pasaporte">Pasaporte</option>
          </Select>
          <Input
            type="text"
            name="numeroDocumento"
            placeholder="Número de documento"
            inputMode="numeric"
            onChange={(e) => handleNumberInputChange(e, setDestinatario, destinatario)}
          />
        </FormGroup>
        <FormGroup>
          <Label htmlFor="fechaEnvio">Fecha de envío</Label>
          <Input
            id="fechaEnvio"
            type="date"
            value={fechaEnvio}
            onChange={(e) => setFechaEnvio(e.target.value)}
          />
        </FormGroup>
        <FormGroup>
          <Label>Cantidad de paquetes</Label>
          <Input
            type="number"
            placeholder="Cantidad de paquetes"
            value={cantidadPaquetes}
            onChange={(e) => setCantidadPaquetes(e.target.value)}
          />
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