import { useEffect, useState } from 'react';
import { Card, List, Button, message } from 'antd';
import { Link } from 'react-router-dom';

const ProfilePage = ({ token, user }) => {
    const [profile, setProfile] = useState(null);
    const API_URL = process.env.REACT_APP_API_URL;

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const response = await fetch(`${API_URL}/users/profile`, {
                    headers: { 'Authorization': `Bearer ${token}` }
                });
                const data = await response.json();
                setProfile(data);
            } catch (error) {
                console.error('Failed to fetch profile', error);
            }
        };
        fetchProfile();
    }, [token]);

    const handleRemoveFavorite = async (carId) => {
        try {
            await fetch(`${API_URL}/users/delCar/${carId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ userId: profile.id })
            });

            setProfile({
                ...profile,
                interestedCars: profile.interestedCars.filter(car => car.id !== carId)
            });

            message.success('Car removed from favorites');
        } catch (error) {
            message.error('Failed to remove from favorites');
        }
    };

    if (!profile) return <div>Loading...</div>;

    return (
        <Card title="Profile" style={{ paddingBottom: '20px', marginBottom: '20px' }}>
            <p>Name: {profile.name}</p>
            <p>Phone: {profile.phone}</p>

            <h3 style={{ marginTop: '20px' }}>Favorite Cars</h3>
            <List
                dataSource={Array.from(profile.interestedCars || [])}
                renderItem={(car) => (
                    <List.Item
                        actions={[
                            <Button
                                type="link"
                                danger
                                onClick={() => handleRemoveFavorite(car.id)}
                            >
                                Remove
                            </Button>
                        ]}
                    >
                        <Link to={`/cars/${car.id}`}>
                            {car.brand} {car.model} ({car.year}) - ${car.price}
                        </Link>
                    </List.Item>
                )}
            />
        </Card>
    );
};

export default ProfilePage;