import { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Layout } from 'antd';
import Header from './components/Header';
import AuthPage from './pages/AuthPage';
import RegisterPage from './pages/RegisterPage';
import CarsPage from './pages/CarsPage';
import CarDetailPage from './pages/CarDetailPage';
import ProfilePage from './pages/ProfilePage';
import DealershipsPage from './pages/DealershipsPage';
import './styles.css';
import UsersPage from './pages/UsersPage';
import DealershipDetailPage from './pages/DealershipDetailPage';

const { Content } = Layout;

function App() {
    const [token, setToken] = useState(localStorage.getItem('token'));
    const [user, setUser] = useState(null);
    const API_URL = process.env.REACT_APP_API_URL;

    useEffect(() => {
        if (token) {
            // Загружаем данные пользователя при наличии токена
            const fetchProfile = async () => {
                try {
                    const response = await fetch(`${API_URL}/users/profile`, {
                        headers: {
                            'Authorization': `Bearer ${token}`
                        }
                    });
                    const userData = await response.json();
                    setUser(userData);
                } catch (error) {
                    console.error('Failed to fetch user profile', error);
                }
            };
            fetchProfile();
        }
    }, [token]);

    const handleLogout = () => {
        localStorage.removeItem('token');
        setToken(null);
        setUser(null);
    };

    const isAdmin = user?.roles?.includes('ADMIN');
    return (
        <Router>
            <Layout>
                <Header
                    isAuthenticated={!!token}
                    user={user}
                    onLogout={handleLogout}
                />

                <Content style={{ padding: '0 50px', marginTop: 64 }}>
                    <Routes>
                        <Route path="/login" element={!token ? <AuthPage setToken={setToken} /> : <Navigate to="/" />} />
                        <Route path="/register" element={!token ? <RegisterPage /> : <Navigate to="/" />} />
                        <Route
                            path="/"
                            element={<CarsPage isAdmin={user?.roles?.includes('ADMIN')} token={token} />}
                        />
                        <Route path="/cars/:id" element={<CarDetailPage token={token} isAdmin={isAdmin}/>} />
                        <Route
                            path="/profile"
                            element={token ? <ProfilePage token={token} user={user} /> : <Navigate to="/login" />}
                        />
                        <Route path="/dealerships" element={<DealershipsPage token={token} isAdmin={isAdmin} />} />
                        <Route
                            path="/dealerships/:id"
                            element={<DealershipDetailPage token={token} isAdmin={isAdmin} />}
                        />
                        <Route
                            path="/admin/users"
                            element={isAdmin ? <UsersPage token={token} isAdmin={isAdmin} /> : <Navigate to="/" />}
                        />
                    </Routes>
                </Content>
            </Layout>
        </Router>
    );
}

export default App;