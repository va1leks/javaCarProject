import { Layout, Menu, Button, Avatar, Space } from 'antd';
import { Link, useNavigate } from 'react-router-dom';
const { Header } = Layout;

const AppHeader = ({ isAuthenticated, user, onLogout }) => {
    const navigate = useNavigate();
    const isAdmin = user?.roles?.includes('ADMIN');

    const handleAvatarClick = () => {
        // Переход в профиль по клику на аватар
        navigate('/profile');
    };

    return (
        <Header style={{ position: 'sticky', top: 0, zIndex: 1000, width: '100%' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Menu
                    theme="dark"
                    mode="horizontal"
                    defaultSelectedKeys={['1']}
                    style={{ lineHeight: '64px', flex: 1 }}
                >
                    <Menu.Item key="1">
                        <Link to="/">Cars</Link>
                    </Menu.Item>
                    <Menu.Item key="2">
                        <Link to="/dealerships">Dealerships</Link>
                    </Menu.Item>

                    {isAdmin && (
                        <Menu.Item key="3">
                            <Link to="/admin/users">Users</Link>
                        </Menu.Item>
                    )}
                </Menu>

                {/* Профиль */}
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    {isAuthenticated ? (
                        <Space>
                            <Avatar
                                style={{ backgroundColor: '#1890ff', color: '#fff', cursor: 'pointer' }}
                                onClick={handleAvatarClick}
                            >
                                {user?.name?.charAt(0)?.toUpperCase()}
                            </Avatar>
                            <span style={{ color: 'white' }}>{user?.name}</span>
                            <Button onClick={onLogout}>Logout</Button>
                        </Space>
                    ) : (
                        <Space>
                            <Button onClick={() => navigate('/login')}>Login</Button>
                            <Button type="primary" onClick={() => navigate('/register')}>
                                Register
                            </Button>
                        </Space>
                    )}
                </div>
            </div>
        </Header>
    );
};

export default AppHeader;
