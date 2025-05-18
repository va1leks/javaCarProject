import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1/cars';

export const getCars = async () => {
    const response = await axios.get(API_URL);
    return response.data;
};

export const getCarById = async (id) => {
    const response = await axios.get(`${API_URL}/${id}`);
    return response.data;
};

export const createCar = async (carData) => {
    const response = await axios.post(API_URL, carData);
    return response.data;
};

export const updateCar = async (id, carData) => {
    const response = await axios.put(`${API_URL}/updateCar/${id}`, carData);
    return response.data;
};

export const deleteCar = async (id) => {
    const response = await axios.delete(`${API_URL}/${id}`);
    return response.data;
};

export const addToFavorites = async (carId, token) => {
    const response = await axios.patch(
        `${API_URL.replace('/cars', '/users')}/addCar/${carId}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
    );
    return response.data;
};