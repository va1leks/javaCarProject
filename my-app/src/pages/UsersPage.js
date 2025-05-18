import { useState, useEffect } from 'react';
import { Table, Tag, Card, Descriptions, Button, Modal, Space, Typography, message, Input, Form, Select } from 'antd';
import { useNavigate } from 'react-router-dom';

const { Title, Text } = Typography;

const UsersPage = ({ token, isAdmin }) => {
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [editingUser, setEditingUser] = useState(null);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [loading, setLoading] = useState(true);
    const [form] = Form.useForm();
    const navigate = useNavigate();
    const API_URL = process.env.REACT_APP_API_URL;

    useEffect(() => {
        if (!isAdmin) {
            navigate('/');
            return;
        }
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        setLoading(true);
        try {
            const response = await fetch(`${API_URL}/users`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            const data = await response.json();
            setUsers(data);
        } catch (error) {
            console.error('Failed to fetch users:', error);
            message.error('Failed to load users');
        } finally {
            setLoading(false);
        }
    };

    const showUserDetails = (user) => {
        setSelectedUser(user);
        setEditingUser({ ...user });
        setIsModalVisible(true);
        setIsEditing(false);
        form.setFieldsValue({
            name: user.name,
            phone: user.phone,
            roles: user.roles || []
        });
    };

    const handleEdit = (user) => {
        setSelectedUser(user);
        setEditingUser({ ...user });
        setIsModalVisible(true);
        setIsEditing(true);
        form.setFieldsValue({
            name: user.name,
            phone: user.phone,
            roles: user.roles || []
        });
    };

    const handleSave = async () => {
        try {
            const values = await form.validateFields();
            const updatedUser = {
                ...editingUser,
                name: values.name,
                phone: values.phone,
                roles: values.roles
            };

            const response = await fetch(`${API_URL}/users/${updatedUser.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(updatedUser)
            });

            if (!response.ok) throw new Error('Failed to update user');

            message.success('User updated successfully');
            fetchUsers();
            setIsModalVisible(false);
            setIsEditing(false);
        } catch (error) {
            console.error('Error:', error);
            message.error(error.message || 'Failed to update user');
        }
    };

    const handleDeleteUser = (userId) => {
        Modal.confirm({
            title: 'Confirm Deletion',
            content: 'Are you sure you want to delete this user?',
            okText: 'Yes',
            okType: 'danger',
            cancelText: 'No',
            onOk: async () => {
                try {
                    await fetch(`${API_URL}/users/${userId}`, {
                        method: 'DELETE',
                        headers: {
                            'Authorization': `Bearer ${token}`
                        }
                    });
                    message.success('User deleted successfully');
                    fetchUsers();
                } catch (error) {
                    console.error('Failed to delete user:', error);
                    message.error('Failed to delete user');
                }
            }
        });
    };

    const columns = [
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
            render: (text, record) => (
                <Button type="link" onClick={() => showUserDetails(record)}>
                    {text}
                </Button>
            ),
        },
        {
            title: 'Phone',
            dataIndex: 'phone',
            key: 'phone',
        },
        {
            title: 'Roles',
            dataIndex: 'roles',
            key: 'roles',
            render: roles => (
                <Space>
                    {roles?.map(role => (
                        <Tag color={role === 'ADMIN' ? 'red' : 'blue'} key={role}>
                            {role}
                        </Tag>
                    ))}
                </Space>
            ),
        },
        {
            title: 'Favorite Cars',
            dataIndex: 'interestedCars',
            key: 'interestedCars',
            render: (cars) => (
                <Text>{cars?.length || 0} cars</Text>
            ),
        },
        {
            title: 'Actions',
            key: 'actions',
            render: (_, record) => (
                <Space>
                    <Button onClick={() => handleEdit(record)}>Edit</Button>
                    <Button danger onClick={() => handleDeleteUser(record.id)}>
                        Delete
                    </Button>
                </Space>
            )
        }
    ];

    return (
        <Card
            title={<Title level={2}>Users Management</Title>}
            bordered={false}
            loading={loading}
        >
            <Table
                dataSource={users}
                columns={columns}
                rowKey="id"
                pagination={{ pageSize: 10 }}
            />

            <Modal
                title={isEditing ? 'Edit User' : 'User Details'}
                visible={isModalVisible}
                onCancel={() => {
                    setIsModalVisible(false);
                    setIsEditing(false);
                }}
                footer={[
                    isEditing && (
                        <Button key="save" type="primary" onClick={handleSave}>
                            Save
                        </Button>
                    ),
                    <Button key="close" onClick={() => {
                        setIsModalVisible(false);
                        setIsEditing(false);
                    }}>
                        Close
                    </Button>
                ]}
                width={700}
            >
                {isEditing ? (
                    <Form form={form} layout="vertical">
                        <Form.Item
                            name="name"
                            label="Name"
                            rules={[{ required: true, message: 'Please input user name!' }]}
                        >
                            <Input />
                        </Form.Item>
                        <Form.Item
                            name="phone"
                            label="Phone"
                            rules={[{ required: true, message: 'Please input user phone!' }]}
                        >
                            <Input />
                        </Form.Item>
                        <Form.Item
                            name="roles"
                            label="Roles"
                            rules={[{ required: true, message: 'Please select user roles!' }]}
                        >
                            <Select mode="multiple" placeholder="Select roles">
                                <Select.Option value="ADMIN">ADMIN</Select.Option>
                                <Select.Option value="USER">USER</Select.Option>
                            </Select>
                        </Form.Item>
                    </Form>
                ) : selectedUser && (
                    <Descriptions bordered column={1}>
                        <Descriptions.Item label="Name">
                            <Text strong>{selectedUser.name}</Text>
                        </Descriptions.Item>
                        <Descriptions.Item label="Phone">
                            {selectedUser.phone}
                        </Descriptions.Item>
                        <Descriptions.Item label="Roles">
                            <Space>
                                {selectedUser.roles?.map(role => (
                                    <Tag color={role === 'ADMIN' ? 'red' : 'blue'} key={role}>
                                        {role}
                                    </Tag>
                                ))}
                            </Space>
                        </Descriptions.Item>
                        <Descriptions.Item label="Favorite Cars Count">
                            <Button
                                type="link"
                                onClick={() => {
                                    navigate(`/users/${selectedUser.id}/cars`);
                                    setIsModalVisible(false);
                                }}
                            >
                                {selectedUser.interestedCars?.length || 0} cars
                            </Button>
                        </Descriptions.Item>
                    </Descriptions>
                )}
            </Modal>
        </Card>
    );
};

export default UsersPage;
