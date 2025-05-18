import { useState, useEffect } from 'react';
import { List, Card, Button, Tag, Space, Modal, message, Input } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import CarForm from '../components/CarForm';
import { Link } from 'react-router-dom';

const CarsPage = ({ isAdmin, token }) => {
    const [cars, setCars] = useState([]);
    const [filteredCars, setFilteredCars] = useState([]);
    const [searchText, setSearchText] = useState(''); // состояние для поискового запроса
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [editingCar, setEditingCar] = useState(null);
    const [dealerships, setDealerships] = useState([]);
    const API_URL = process.env.REACT_APP_API_URL;

    useEffect(() => {
        fetchCars();
        fetchDealerships();
    }, []);

    const fetchDealerships = async () => {
        try {
            const response = await fetch(`${API_URL}/dealerships`);
            const data = await response.json();
            setDealerships(data);
        } catch (error) {
            console.error('Failed to fetch dealerships', error);
        }
    };

    const fetchCars = async () => {
        try {
            const response = await fetch(`${API_URL}/cars`, {
                headers: token ? { 'Authorization': `Bearer ${token}` } : {}
            });
            const data = await response.json();
            setCars(data);
            setFilteredCars(data); // инициализируем фильтрованный список всех автомобилей
        } catch (error) {
            console.error('Failed to fetch cars', error);
        }
    };

    const handleDelete = async (id) => {
        try {
            await fetch(`${API_URL}/cars/${id}`, {
                method: 'DELETE',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            message.success('Car deleted successfully');
            fetchCars();
        } catch (error) {
            message.error('Failed to delete car');
        }
    };

    const handleAddFavorite = async (carId) => {
        try {
            const response = await fetch(`${API_URL}/users/addCar/${carId}`, {
                method: 'PATCH',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({}) // обязателен даже если бэкенд его не использует
            });

            if (!response.ok) {
                const errorText = await response.text();
                console.error('Ошибка:', response.status, errorText);
                message.error('Не удалось добавить в избранное');
                return;
            }

            message.success('Машина добавлена в избранное');
        } catch (error) {
            console.error('Ошибка запроса:', error);
            message.error('Ошибка подключения к серверу');
        }
    };

    const handleSearchChange = (e) => {
        const value = e.target.value;
        setSearchText(value);

        // Фильтруем автомобили по названию бренда или модели
        const filtered = cars.filter((car) =>
            `${car.brand} ${car.model}`.toLowerCase().includes(value.toLowerCase())
        );
        setFilteredCars(filtered); // обновляем отображаемые автомобили
    };

    const handleSubmit = async (values) => {
        try {
            const url = editingCar
                ? `${API_URL}/cars/updateCar/${editingCar.id}`
                : `${API_URL}/cars`;

            const method = editingCar ? 'PUT' : 'POST';

            const response = await fetch(url, {
                method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(values)
            });

            if (!response.ok) {
                const errorData = await response.json();
                if (errorData && errorData.vin) {
                    // Вызов ошибки в форме по VIN
                    throw { fieldErrors: { vin: errorData.vin } };
                } else {
                    throw new Error('Unknown error');
                }
            }

            const data = await response.json();
            message.success(editingCar ? 'Car updated successfully' : 'Car added successfully');
            fetchCars();
            setIsModalVisible(false);
            setEditingCar(null);
        } catch (error) {
            if (error.fieldErrors) {
                // Пробрасываем ошибки в форму
                window.dispatchEvent(new CustomEvent('carFormErrors', { detail: error.fieldErrors }));
            } else {
                message.error('Operation failed');
            }
        }
    };

    return (
        <div>
            {isAdmin && (
                <Button
                    type="primary"
                    icon={<PlusOutlined />}
                    onClick={() => {
                        setEditingCar(null);
                        setIsModalVisible(true);
                    }}
                    style={{ marginBottom: 16 }}
                >
                    Add Car
                </Button>
            )}

            {/* Поле для поиска */}
            <Input
                placeholder="Search by brand or model"
                value={searchText}
                onChange={handleSearchChange}
                style={{ marginBottom: 20, width: '500px',marginLeft: 20 }}
            />

            <List
                grid={{ gutter: 16, column: 3 }}
                dataSource={filteredCars}
                pagination={{
                    pageSize: 12, // Количество машин на одной странице
                    showSizeChanger: false, // отключает возможность менять размер страницы
                    position: 'bottom',
                }}
                renderItem={(car) => (
                    <List.Item>
                        <Card
                            hoverable
                            onClick={() => window.location.href = `/cars/${car.id}`}
                            title={`${car.brand} ${car.model} (${car.year})`}
                            style={{ cursor: 'pointer' }}
                            actions={[
                                token && <a onClick={(e) => { e.stopPropagation(); handleAddFavorite(car.id); }}>Add to Favorites</a>,
                                isAdmin && <a onClick={(e) => { e.stopPropagation(); setEditingCar(car); setIsModalVisible(true); }}>Edit</a>,
                                isAdmin && <a onClick={(e) => { e.stopPropagation(); handleDelete(car.id); }} style={{ color: 'red' }}>Delete</a>
                            ]}
                        >
                            <p>Price: ${car.price}</p>
                            <p>Mileage: {car.mileage} km</p>
                            <Space>
                                <Tag color={car.status === 'AVAILABLE' ? 'green' : 'red'}>{car.status}</Tag>
                                <Tag>{car.engineType}</Tag>
                                <Tag>{car.transmission}</Tag>
                            </Space>
                        </Card>
                    </List.Item>
                )}
            />

            <Modal
                title={editingCar ? 'Edit Car' : 'Add Car'}
                visible={isModalVisible}
                onCancel={() => {
                    setIsModalVisible(false);
                    setEditingCar(null);
                }}
                footer={null}
            >
                <CarForm
                    initialValues={{
                        ...editingCar,
                        dealershipName: editingCar?.dealershipId?.name || null,
                    }}
                    onSubmit={handleSubmit}
                    dealerships={dealerships}
                />
            </Modal>
        </div>
    );
};

export default CarsPage;
