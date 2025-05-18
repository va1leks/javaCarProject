import { useState, useEffect } from 'react';
import { List, Card, Button, Modal, Form, Input, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';

const DealershipsPage = ({ token, isAdmin }) => {
    const [dealerships, setDealerships] = useState([]);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [editingDealership, setEditingDealership] = useState(null);
    const [form] = Form.useForm();
    const API_URL = process.env.REACT_APP_API_URL;

    useEffect(() => {
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

    const handleSubmit = async (values) => {
        try {
            const url = editingDealership
                ? `${API_URL}/dealerships`
                : `${API_URL}/dealerships`;

            const method = editingDealership ? 'PUT' : 'POST';

            const response = await fetch(url, {
                method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(editingDealership ? { ...values, id: editingDealership.id } : values)
            });

            const data = await response.json();
            message.success(editingDealership ? 'Dealership updated' : 'Dealership added');
            fetchDealerships();
            setIsModalVisible(false);
            setEditingDealership(null);
        } catch (error) {
            message.error('Operation failed');
        }
    };

    const handleDelete = async (id) => {
        try {
            await fetch(`${API_URL}/dealerships/${id}`, {
                method: 'DELETE',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            message.success('Dealership deleted');
            fetchDealerships();
        } catch (error) {
            message.error('Failed to delete dealership');
        }
    };

    return (
        <div>
            {isAdmin && (
                <Button
                    type="primary"
                    icon={<PlusOutlined />}
                    onClick={() => {
                        setEditingDealership(null);
                        setIsModalVisible(true);
                    }}
                    style={{ marginBottom: 16 }}
                >
                    Add Dealership
                </Button>
            )}

            <List
                grid={{ gutter: 16, column: 3 }}
                dataSource={dealerships}
                renderItem={(dealership) => (
                    <List.Item>
                        <Card
                            hoverable
                            onClick={() => window.location.href = `/dealerships/${dealership.id}`}
                            title={dealership.name}
                            style={{ cursor: 'pointer' }}
                            actions={[
                                isAdmin && (
                                    <Button
                                        type="link"
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            setEditingDealership(dealership);
                                            setIsModalVisible(true);
                                        }}
                                    >
                                        Edit
                                    </Button>
                                ),
                                isAdmin && (
                                    <Button
                                        type="link"
                                        danger
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            handleDelete(dealership.id);
                                        }}
                                    >
                                        Delete
                                    </Button>
                                )
                            ].filter(Boolean)}
                        >
                            <p>Address: {dealership.address}</p>
                            <p>Cars: {dealership.cars?.length || 0}</p>
                        </Card>
                    </List.Item>
                )}
            />

            <Modal
                title={editingDealership ? 'Edit Dealership' : 'Add Dealership'}
                visible={isModalVisible}
                onCancel={() => {
                    setIsModalVisible(false);
                    setEditingDealership(null);
                }}
                onOk={() => form.submit()}
            >
                <Form
                    form={form}
                    initialValues={editingDealership || {}}
                    onFinish={handleSubmit}
                    layout="vertical"
                >
                    <Form.Item name="name" label="Name" rules={[{ required: true }]}>
                        <Input />
                    </Form.Item>

                    <Form.Item name="address" label="Address" rules={[{ required: true }]}>
                        <Input />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};

export default DealershipsPage;