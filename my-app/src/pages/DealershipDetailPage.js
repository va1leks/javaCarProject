import { useParams } from 'react-router-dom';
import React, { useState, useEffect } from 'react';
import { Card, Descriptions, Tag, Button, Table, Typography, message, Modal, Select, Space } from 'antd';
import { useNavigate } from 'react-router-dom';
import { PlusOutlined, DeleteOutlined } from '@ant-design/icons';

const { Title, Text } = Typography;
const { Option } = Select;

const DealershipDetailPage = ({ token, isAdmin }) => {
    const { id } = useParams();
    const [dealership, setDealership] = useState(null);
    const [cars, setCars] = useState([]);
    const [allCars, setAllCars] = useState([]);
    const [loading, setLoading] = useState(true);
    const [addModalVisible, setAddModalVisible] = useState(false);
    const [selectedCarId, setSelectedCarId] = useState(null);
    const navigate = useNavigate();
    const API_URL = process.env.REACT_APP_API_URL;

    useEffect(() => {
        fetchData();
    }, [id]);

    const fetchData = async () => {
        try {
            setLoading(true);
            const dealershipResponse = await fetch(`${API_URL}/dealerships/${id}`);
            const dealershipData = await dealershipResponse.json();
            setDealership(dealershipData);

            const carsResponse = await fetch(`${API_URL}/cars/by-dealership/${id}`);
            const carsData = await carsResponse.json();
            setCars(carsData);

            const allCarsResponse = await fetch(`${API_URL}/cars`);
            const allCarsData = await allCarsResponse.json();
            setAllCars(allCarsData);
        } catch (error) {
            console.error('Error fetching data:', error);
            message.error('Failed to load data');
        } finally {
            setLoading(false);
        }
    };

    const handleAddCar = async () => {
        if (!selectedCarId) {
            message.warning('Please select a car');
            return;
        }

        try {
            const response = await fetch(`${API_URL}/dealerships/addCarToDealership/${id}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(selectedCarId)
            });

            if (response.ok) {
                message.success('Car added successfully');
                fetchData();
                setAddModalVisible(false);
                setSelectedCarId(null);
            } else {
                throw new Error('Failed to add car');
            }
        } catch (error) {
            message.error(error.message);
        }
    };

    const handleRemoveCar = async (carId) => {
        try {
            const response = await fetch(`${API_URL}/dealerships/deleteCarFromDs/${id}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(carId)
            });

            if (response.ok) {
                message.success('Car removed successfully');
                setCars(prevCars => prevCars.filter(car => car.id !== carId));
            } else {
                throw new Error('Failed to remove car');
            }
        } catch (error) {
            message.error(error.message);
        }
    };

    const columns = [
        {
            title: 'Brand',
            dataIndex: 'brand',
            key: 'brand',
        },
        {
            title: 'Model',
            dataIndex: 'model',
            key: 'model',
        },
        {
            title: 'Year',
            dataIndex: 'year',
            key: 'year',
        },
        {
            title: 'Price',
            dataIndex: 'price',
            key: 'price',
            render: price => `$${price.toLocaleString()}`,
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
            render: status => (
                <Tag color={status === 'AVAILABLE' ? 'green' : 'red'}>{status}</Tag>
            ),
        },
        {
            title: 'Action',
            key: 'action',
            render: (_, record) => (
                <Space>
                    <Button
                        type="link"
                        onClick={() => navigate(`/cars/${record.id}`)}
                    >
                        View
                    </Button>
                    {isAdmin && (
                        <Button
                            danger
                            icon={<DeleteOutlined />}
                            onClick={() => handleRemoveCar(record.id)}
                        />
                    )}
                </Space>
            ),
        },
    ];

    if (loading) return <div>Loading...</div>;
    if (!dealership) return <div>Dealership not found</div>;

    const availableCars = allCars.filter(car =>
        !cars.some(c => c.id === car.id)
    );

    return (
        <Card
            title={<Title level={2} style={{ marginBottom: '20px' }}>{dealership.name}</Title>}
            loading={loading}
            extra={
                <Space>
                    <Button onClick={() => navigate(-1)}>Back to list</Button>
                    {isAdmin && (
                        <Button
                            type="primary"
                            icon={<PlusOutlined />}
                            onClick={() => setAddModalVisible(true)}
                        >
                            Add Car
                        </Button>
                    )}
                </Space>
            }
            style={{ marginBottom: '20px' }} // Отступ снизу для карточки
        >
            <Descriptions bordered column={1} style={{ marginBottom: 24 }}>
                <Descriptions.Item label="Address">
                    <Text strong>{dealership.address}</Text>
                </Descriptions.Item>
                <Descriptions.Item label="Total Cars">
                    <Text strong>{cars.length}</Text>
                </Descriptions.Item>
            </Descriptions>

            <Title level={4} style={{ marginTop: '20px' }}>Available Cars</Title>
            <Table
                dataSource={cars}
                columns={columns}
                rowKey="id"
                pagination={{ pageSize: 5 }}
                bordered
                style={{ marginBottom: '20px' }} // Отступ снизу для таблицы
            />

            <Modal
                title="Add Car to Dealership"
                visible={addModalVisible}
                onOk={handleAddCar}
                onCancel={() => {
                    setAddModalVisible(false);
                    setSelectedCarId(null);
                }}
                okText="Add"
                cancelText="Cancel"
            >
                <Select
                    style={{ width: '100%' }}
                    placeholder="Select a car to add"
                    onChange={(value) => setSelectedCarId(value)}
                    showSearch
                    optionFilterProp="children"
                    filterOption={(input, option) => {
                        const car = option.props.carData;
                        const carText = `${car.brand} ${car.model} ${car.year} ${car.price} ${car.vin}`.toLowerCase();
                        return carText.indexOf(input.toLowerCase()) >= 0;
                    }}
                >
                    {availableCars.map(car => (
                        <Option key={car.id} value={car.id} carData={car}>
                            {car.brand} {car.model} ({car.year}) - ${car.price} - VIN: {car.vin}
                        </Option>
                    ))}
                </Select>
            </Modal>
        </Card>
    );
};

export default DealershipDetailPage;