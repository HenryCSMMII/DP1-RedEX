import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';
import axios from 'axios';
import moment from 'moment-timezone';

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
  font-size: 14px;
`;

const Input = styled.input`
  width: 100%;
  padding: 6px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 8px;
  font-size: 14px;
`;

const Select = styled.select`
  width: 100%;
  padding: 6px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 8px;
  font-size: 14px;
`;

const ButtonGroup = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
`;

const Button = styled.button`
  background-color: ${(props) => (props.$primary ? '#6ba292' : '#ccc')};
  color: ${(props) => (props.$primary ? '#fff' : '#000')};
  border: none;
  padding: 8px 16px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;

  &:hover {
    background-color: ${(props) => (props.$primary ? '#5a8a7d' : '#bbb')};
  }
`;

const FileInput = styled.input`
  display: block;
  margin: 20px 0;
`;

const OrSeparator = styled.div`
  margin: 0 20px;
  font-size: 16px;
  font-weight: bold;
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
  const [file, setFile] = useState(null);

  useEffect(() => {
    console.log('Countries:', data.countries);
    console.log('Cities:', data.ciudad);
  }, [data]);

  const handleCountryChange = (e, setState, setCities, setSelectedCountry) => {
    const { name, value } = e.target;
    console.log(`Country selected: ${value}`);

    // Actualizar el estado del país seleccionado
    setSelectedCountry(value);

    // Filtrar las ciudades basadas en el país seleccionado
    const filteredCities = data.ciudad.filter(city => {
      console.log(`Comparando ${city.countryId} con ${value}`);
      return city.countryId === parseInt(value);
    });

    console.log('Filtered Cities:', filteredCities);

    // Actualizar las ciudades en el estado
    setCities(filteredCities);
    setState(prevState => ({
      ...prevState,
      [name]: value,
      ciudad: '' // Reiniciar la ciudad seleccionada
    }));
  };

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

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleFileUpload = async () => {
    if (!file) {
      alert('Por favor, seleccione un archivo primero.');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await axios.post('http://localhost:8080/shipment/read/', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      alert('Archivo procesado correctamente');
    } catch (error) {
      console.error('Error procesando el archivo:', error.message);
      if (error.response) {
        console.error('Error response data:', error.response.data);
        console.error('Error response status:', error.response.status);
      }
      alert('Error procesando el archivo');
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
	const localDateTime = moment.tz(`${fechaEnvio}T${horaEnvio}:00`, 'YYYY-MM-DDTHH:mm:ss', 'America/Lima').format();
    const shipmentPayload = {
      packageQuantity: cantidadPaquetes,
      departureAirport: {
        id: departureAirport.id
      },
      arrivalAirport: {
        id: arrivalAirport.id
      },
      registerDateTime: localDateTime, // Cambiado a registerDateTime
      arrivalTime: null,
    };

    const shipmentResponse = await axios.post('http://localhost:8080/shipment/create/', shipmentPayload);

    alert('Envio creado exitosamente');
    onRequestClose();
  } catch (error) {
    console.error('Error creando envio:', error.message);
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
          <Select
            name="pais"
            value={remitente.pais}
            onChange={(e) => handleCountryChange(e, setRemitente, setCitiesRemitente, setSelectedCountryRemitente)}
          >
            <option value="">Seleccione un país</option>
            {data.countries.map((country) => (
              <option key={country.id} value={country.id}>
                {country.name}
              </option>
            ))}
          </Select>
          <Select
            name="ciudad"
            value={remitente.ciudad}
            onChange={(e) => handleInputChange(e, setRemitente, remitente)}
          >
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
          <Select
            name="pais"
            value={destinatario.pais}
            onChange={(e) => handleCountryChange(e, setDestinatario, setCitiesDestinatario, setSelectedCountryDestinatario)}
          >
            <option value="">Seleccione un país</option>
            {data.countries.map((country) => (
              <option key={country.id} value={country.id}>
                {country.name}
              </option>
            ))}
          </Select>
          <Select
            name="ciudad"
            value={destinatario.ciudad}
            onChange={(e) => handleInputChange(e, setDestinatario, destinatario)}
          >
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
          <div>
            <Button onClick={onRequestClose}>Cancelar</Button>
            <Button $primary onClick={handleSubmit}>Crear envío</Button>
          </div>
          <OrSeparator>O</OrSeparator>
          <div>
            <FileInput type="file" onChange={handleFileChange} />
            <Button $primary onClick={handleFileUpload}>Cargar archivo</Button>
          </div>
        </ButtonGroup>
      </ModalContent>
    </Modal>
  );
};

export default NuevoEnvioPopup;