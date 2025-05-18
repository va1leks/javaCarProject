import { Card, Form, Input, Button, message } from 'antd';
import { register } from '../api/auth';
import { useNavigate } from 'react-router-dom';

const RegisterPage = () => {
    const navigate = useNavigate();
    const API_URL = process.env.REACT_APP_API_URL;

    const onFinish = async (values) => {
        try {
            if (values.password !== values.confirmPassword) {
                message.error('Passwords do not match');
                return;
            }

            await register(values);
            navigate('/login');
            message.success('Registration successful. Please login.');
        } catch (error) {
            message.error('Registration failed');
        }
    };

    return (
        <div style={{ display: 'flex', justifyContent: 'center', paddingTop: 50 }}>
            <Card title="Register" style={{ width: 400 }}>
                <Form onFinish={onFinish}>
                    <Form.Item
                        name="name"
                        rules={[{ required: true, message: 'Please input your name!' }]}
                    >
                        <Input placeholder="Name" />
                    </Form.Item>

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

                    <Form.Item
                        name="confirmPassword"
                        rules={[{ required: true, message: 'Please confirm your password!' }]}
                    >
                        <Input.Password placeholder="Confirm Password" />
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" htmlType="submit" block>
                            Register
                        </Button>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    );
};

export default RegisterPage;