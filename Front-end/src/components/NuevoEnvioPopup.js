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
  background-color: ${(props) => (props.$primary ? '#6ba292' : '#ccc')};
  color: ${(props) => (props.$primary ? '#fff' : '#000')};
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;

  &:hover {
    background-color: ${(props) => (props.$primary ? '#5a8a7d' : '#bbb')};
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
  const [horaEnvio, setHoraEnvio] = useState('00:00');

  useEffect(() => {
    if (selectedCountryRemitente) {
      const filteredCities = data.ciudad.filter(city => city.countryId === parseInt(selectedCountryRemitente));
      setCitiesRemitente(filteredCities);
    } else {
      setCitiesRemitente([]);
    }
  }, [selectedCountryRemitente, data.ciudad]);

  useEffect(() => {
    if (selectedCountryDestinatario) {
      const filteredCities = data.ciudad.filter(city => city.countryId === parseInt(selectedCountryDestinatario));
      setCitiesDestinatario(filteredCities);
    } else {
      setCitiesDestinatario([]);
    }
  }, [selectedCountryDestinatario, data.ciudad]);

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
      const departureAirport = data.airports.find(airport => airport.countryId === parseInt(remitente.pais));
      const arrivalAirport = data.airports.find(airport => airport.countryId === parseInt(destinatario.pais));

      if (!departureAirport) {
        throw new Error(`No se encontró el aeropuerto correspondiente al país remitente con ID: ${remitente.pais}`);
      }
      if (!arrivalAirport) {
        throw new Error(`No se encontró el aeropuerto correspondiente al país destinatario con ID: ${destinatario.pais}`);
      }

      const shipmentPayload = {
        packageQuantity: cantidadPaquetes,
        departureAirport: {
          id: departureAirport.id
        },
        arrivalAirport: {
          id: arrivalAirport.id
        },
        departureTime: `${fechaEnvio}T${horaEnvio}:00`,
        arrivalTime: null,
      };

      const shipmentResponse = await axios.post('http://inf226-982-5e.inf.pucp.edu.pe/back/shipment/create/', shipmentPayload);

      alert('Envio creado exitosamente');
      onRequestClose();
    } catch (error) {
      console.error('Error creating envio:', error.message);
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

          <Select name="pais" onChange={(e) => { setSelectedCountryRemitente(e.target.value); handleInputChange(e, setRemitente, remitente); }}>
            <option value="">Seleccione un país</option>
            {data.countries.map((country) => (
              <option key={country.id} value={country.id}>
                {country.name}
              </option>
            ))}
          </Select>
          <Select name="ciudad" onChange={(e) => handleInputChange(e, setRemitente, remitente)}>
            <option value="">Seleccione una ciudad</option>
            {citiesRemitente.map((city) => (
              <option key={city.id} value={city.id}>
                {city.name}
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

          <Select name="pais" onChange={(e) => { setSelectedCountryDestinatario(e.target.value); handleInputChange(e, setDestinatario, destinatario); }}>
            <option value="">Seleccione un país</option>
            {data.countries.map((country) => (
              <option key={country.id} value={country.id}>
                {country.name}
              </option>
            ))}
          </Select>
          <Select name="ciudad" onChange={(e) => handleInputChange(e, setDestinatario, destinatario)}>
            <option value="">Seleccione una ciudad</option>
            {citiesDestinatario.map((city) => (
              <option key={city.id} value={city.id}>
                {city.name}
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
          <Label htmlFor="horaEnvio">Hora de envío</Label>
          <Input
            id="horaEnvio"
            type="time"
            value={horaEnvio}
            onChange={(e) => setHoraEnvio(e.target.value)}
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
          <Button $primary onClick={handleSubmit}>Crear envío</Button>
        </ButtonGroup>
      </ModalContent>
    </Modal>
  );
};

export default NuevoEnvioPopup;