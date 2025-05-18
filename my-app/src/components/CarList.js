import { List, Card, Button, Tag, Space } from 'antd';
import { CarStatus, EngineType, Transmission } from './enums';

const CarList = ({ cars, onEdit, onDelete, onView, onAddFavorite, isAdmin }) => {
    return (
        <List
            grid={{ gutter: 16, column: 3 }}
            dataSource={cars}
            renderItem={(car) => (
                <List.Item>
                    <Card
                        title={`${car.brand} ${car.model} (${car.year})`}
                        actions={[
                            <Button type="link" onClick={() => onView(car.id)}>View</Button>,
                            onAddFavorite && <Button type="link" onClick={() => onAddFavorite(car.id)}>Add to Favorites</Button>,
                            isAdmin && <Button type="link" onClick={() => onEdit(car)}>Edit</Button>,
                            isAdmin && <Button type="link" danger onClick={() => onDelete(car.id)}>Delete</Button>,
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
    );
};

export default CarList;