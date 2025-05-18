import { useParams, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import {
    Card,
    Descriptions,
    Tag,
    Button,
    message,
    Space,
    Typography,
    Modal,
    List
} from 'antd';

const { Title, Text } = Typography;

const CarDetailPage = ({ token, isAdmin }) => {
    const { id } = useParams();
    const [car, setCar] = useState(null);
    const [loading, setLoading] = useState(true);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [interestedUsersDetails, setInterestedUsersDetails] = useState([]);
    const navigate = useNavigate();
    const API_URL = process.env.REACT_APP_API_URL;

    useEffect(() => {
        const fetchCarDetails = async () => {
            try {
                const response = await fetch(`${API_URL}/cars/${id}`);
                const data = await response.json();
                setCar(data);
            } catch (error) {
                console.error('Error fetching car details:', error);
                message.error('Failed to load car details');
            } finally {
                setLoading(false);
            }
        };

        fetchCarDetails();
    }, [id]);

    useEffect(() => {
        const fetchInterestedUsers = async () => {
            if (car?.interestedUsers?.length > 0 && isAdmin) {
                try {
                    const users = await Promise.all(
                        car.interestedUsers.map(userId =>
                            fetch(`${API_URL}/users/${userId}`)
                                .then(res => res.json())
                        )
                    );
                    setInterestedUsersDetails(users);
                } catch (err) {
                    console.error('Failed to load interested users', err);
                    message.error('Failed to load interested users');
                }
            }
        };

        fetchInterestedUsers();
    }, [car, isAdmin]);

    if (loading) return <div>Loading...</div>;
    if (!car) return <div>Car not found</div>;

    return (
        <>
            <Card
                title={<Title level={2}>{car.brand} {car.model} ({car.year})</Title>}
                loading={loading}
                extra={
                    <Space>
                        <Button onClick={() => navigate(-1)}>Back to list</Button>
                    </Space>
                }
            >
                <Descriptions bordered column={1}>
                    <Descriptions.Item label="Price"><Text strong>${car.price}</Text></Descriptions.Item>
                    <Descriptions.Item label="Mileage">{car.mileage} km</Descriptions.Item>
                    <Descriptions.Item label="VIN">{car.vin}</Descriptions.Item>
                    <Descriptions.Item label="Color">{car.color}</Descriptions.Item>
                    <Descriptions.Item label="Status">
                        <Tag color={car.status === 'AVAILABLE' ? 'green' : 'red'}>{car.status}</Tag>
                    </Descriptions.Item>
                    <Descriptions.Item label="Engine Type">{car.engineType}</Descriptions.Item>
                    <Descriptions.Item label="Transmission">{car.transmission}</Descriptions.Item>

                    {car.dealershipId && (
                        <Descriptions.Item label="Dealership">
                            <Title level={4}>{car.dealershipId.name}</Title>
                            <Text>{car.dealershipId.address}</Text>
                            <div style={{ marginTop: 8 }}>
                                <Button
                                    type="link"
                                    onClick={() => navigate(`/dealerships/${car.dealershipId.id}`)}
                                >
                                    View Dealership Details
                                </Button>
                            </div>
                        </Descriptions.Item>
                    )}

                    <Descriptions.Item label="Interested Users">
                        <Text strong>{car.interestedUsers?.length || 0}</Text> users added this car to favorites
                        {car.interestedUsers?.length > 0 && isAdmin && (
                            <div style={{ marginTop: 8 }}>
                                <Button
                                    type="link"
                                    onClick={() => setIsModalVisible(true)}
                                >
                                    View List
                                </Button>
                            </div>
                        )}
                    </Descriptions.Item>
                </Descriptions>
            </Card>

            <Modal
                title="Interested Users"
                visible={isModalVisible}
                onCancel={() => setIsModalVisible(false)}
                footer={null}
            >
                <List
                    dataSource={interestedUsersDetails}
                    renderItem={(user) => (
                        <List.Item>
                            <List.Item.Meta
                                title={user.name || `User ID: ${user.id}`}
                                description={user.phone || 'No phone'}
                            />
                        </List.Item>
                    )}
                />
            </Modal>
        </>
    );
};

export default CarDetailPage;
