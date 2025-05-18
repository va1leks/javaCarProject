import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1';

export const login = async (phone, password) => {
    const response = await axios.post(`${API_URL}/auth`, { phone, password });
    return response.data;
};

export const register = async (userData) => {
    const response = await axios.post(`${API_URL}/registration`, userData);
    return response.data;
};