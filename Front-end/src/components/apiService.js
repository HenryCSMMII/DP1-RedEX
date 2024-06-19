import axios from 'axios';

const API_BASE_URL = 'http://inf226-982-5e.inf.pucp.edu.pe/back';

export const getVuelos = async () => {
  const response = await axios.get(`${API_BASE_URL}/vuelo/`);
  return response.data;
};