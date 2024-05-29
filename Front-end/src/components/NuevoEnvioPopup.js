import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';

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
          <Input type="text" placeholder="Nombre completo" />
          <Input type="email" placeholder="Correo electrónico" />
          <Input type="tel" placeholder="Número de teléfono" />
          <Select onChange={(e) => setSelectedCountryRemitente(e.target.value)}>
            <option value="">Seleccione un país</option>
            {data.countries.map(country => (
              <option key={country.id} value={country.id}>
                {country.name}
              </option>
            ))}
          </Select>
          <Select>
            <option value="">Seleccione una ciudad</option>
            {citiesRemitente.map(city => (
              <option key={city.id} value={city.id}>
                {city.nombre}
              </option>
            ))}
          </Select>
          <Select>
            <option>Tipo de documento</option>
            <option>DNI</option>
            <option>Pasaporte</option>
          </Select>
          <Input type="text" placeholder="Número de documento" />
        </FormGroup>
        <FormGroup>
          <Label>Destinatario</Label>
          <Input type="text" placeholder="Nombre completo" />
          <Input type="email" placeholder="Correo electrónico" />
          <Input type="tel" placeholder="Número de teléfono" />
          <Select onChange={(e) => setSelectedCountryDestinatario(e.target.value)}>
            <option value="">Seleccione un país</option>
            {data.countries.map(country => (
              <option key={country.id} value={country.id}>
                {country.name}
              </option>
            ))}
          </Select>
          <Select>
            <option value="">Seleccione una ciudad</option>
            {citiesDestinatario.map(city => (
              <option key={city.id} value={city.id}>
                {city.nombre}
              </option>
            ))}
          </Select>
          <Select>
            <option>Tipo de documento</option>
            <option>DNI</option>
            <option>Pasaporte</option>
          </Select>
          <Input type="text" placeholder="Número de documento" />
        </FormGroup>
        <FormGroup>
          <Label>Información adicional</Label>
          <Select>
            <option>Tipo de servicio</option>
            <option>Estándar</option>
            <option>Express</option>
          </Select>
          <Input type="number" placeholder="Cantidad de paquetes" />
        </FormGroup>
        <ButtonGroup>
          <Button onClick={onRequestClose}>Cancelar</Button>
          <Button primary>Crear envío</Button>
        </ButtonGroup>
      </ModalContent>
    </Modal>
  );
};

export default NuevoEnvioPopup;
