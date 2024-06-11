import axios from 'axios';

const API_BASE_URL = 'http://localhost:5000';

export const getVuelos = async () => {
  const response = await axios.get(`${API_BASE_URL}/vuelo/`);
  return response.data;
};