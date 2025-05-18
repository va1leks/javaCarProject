import { Card, Form, Input, Button, message } from 'antd';
import { useNavigate } from 'react-router-dom';

const AuthPage = ({ setToken }) => {
    const navigate = useNavigate();

    const onFinish = async (values) => {
        try {
            const response = await fetch('http://localhost:8080/api/v1/auth', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    phone: values.phone,
                    password: values.password
                })
            });

            if (!response.ok) {
                throw new Error('Login failed');
            }

            const data = await response.json();
            localStorage.setItem('token', data.token);
            setToken(data.token);
            navigate('/');
            message.success('Login successful');
        } catch (error) {
            message.error('Login failed. Please check your credentials.');
        }
    };

    return (
        <div style={{ display: 'flex', justifyContent: 'center', paddingTop: 100,paddingBottom: 200 }}>
            <Card title="Login" style={{ width: 400, margin: '0 20px' }}>
                <Form onFinish={onFinish}>
                    <Form.Item
                        name="phone"
                        rules={[{ required: true, message: 'Please input your phone!' }]}
                    >
                        <Input placeholder="Phone" />
                    </Form.Item>

                    <Form.Item
                        name="password"
                        rules={[{ required: true, message: 'Please input your password!' }]}
                    >
                        <Input.Password placeholder="Password" />
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" htmlType="submit" block>
                            Login
                        </Button>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    );
};

export default AuthPage;