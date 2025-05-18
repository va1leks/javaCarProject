import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL;

export const getProfile = async (token) => {
    const response = await axios.get(`${API_URL}/profile`, {
        headers: { Authorization: `Bearer ${token}` }
    });
    return response.data;
};