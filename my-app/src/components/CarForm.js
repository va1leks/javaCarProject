import { useEffect } from 'react';
import { Form, Input, InputNumber, Select, Button } from 'antd';
import { CarStatus, EngineType, Transmission } from './enums';

const { Option } = Select;

const CarForm = ({ initialValues = {}, onSubmit, dealerships = [] }) => {
    const [form] = Form.useForm();

    useEffect(() => {
        form.setFieldsValue({
            brand: '',
            model: '',
            year: 2000,
            price: 0,
            mileage: 0,
            vin: '',
            status: CarStatus.AVAILABLE,
            transmission: Transmission.MANUAL,
            color: '',
            engineType: EngineType.PETROL,
            dealershipName: null,
            ...initialValues
        });
    }, [initialValues, form]);

    // Слушаем ошибку от родителя (CarsPage)
    useEffect(() => {
        const handleErrors = (e) => {
            const errors = e.detail;
            form.setFields(Object.entries(errors).map(([name, error]) => ({
                name,
                errors: [error]
            })));
        };
        window.addEventListener('carFormErrors', handleErrors);
        return () => window.removeEventListener('carFormErrors', handleErrors);
    }, [form]);

    return (
        <Form
            form={form}
            onFinish={onSubmit}
            layout="vertical"
        >
            <Form.Item name="brand" label="Brand" rules={[{ required: true }]}>
                <Input />
            </Form.Item>

            <Form.Item name="model" label="Model" rules={[{ required: true }]}>
                <Input />
            </Form.Item>

            <Form.Item name="year" label="Year" rules={[{ required: true, type: 'number', min: 1886 }]}>
                <InputNumber style={{ width: '100%' }} />
            </Form.Item>

            <Form.Item name="price" label="Price" rules={[{ required: true, type: 'number', min: 0 }]}>
                <InputNumber style={{ width: '100%' }} />
            </Form.Item>

            <Form.Item name="mileage" label="Mileage" rules={[{ required: true, type: 'number', min: 0 }]}>
                <InputNumber style={{ width: '100%' }} />
            </Form.Item>

            <Form.Item name="vin" label="VIN" rules={[{ required: true, len: 17 }]}>
                <Input />
            </Form.Item>

            <Form.Item name="status" label="Status" rules={[{ required: true }]}>
                <Select>
                    {Object.values(CarStatus).map(status => (
                        <Option key={status} value={status}>{status}</Option>
                    ))}
                </Select>
            </Form.Item>

            <Form.Item name="transmission" label="Transmission" rules={[{ required: true }]}>
                <Select>
                    {Object.values(Transmission).map(type => (
                        <Option key={type} value={type}>{type}</Option>
                    ))}
                </Select>
            </Form.Item>

            <Form.Item name="color" label="Color" rules={[{ required: true }]}>
                <Input />
            </Form.Item>

            <Form.Item name="engineType" label="Engine Type" rules={[{ required: true }]}>
                <Select>
                    {Object.values(EngineType).map(type => (
                        <Option key={type} value={type}>{type}</Option>
                    ))}
                </Select>
            </Form.Item>

            <Form.Item name="dealershipName" label="Dealership">
                <Select showSearch optionFilterProp="children">
                    <Option value={null}>None</Option>
                    {dealerships.map(d => (
                        <Option key={d.id} value={d.name}>{d.name}</Option>
                    ))}
                </Select>
            </Form.Item>

            <Form.Item>
                <Button type="primary" htmlType="submit">
                    Submit
                </Button>
            </Form.Item>
        </Form>
    );
};

export default CarForm;
