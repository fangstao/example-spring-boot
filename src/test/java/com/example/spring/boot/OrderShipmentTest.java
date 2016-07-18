package com.example.spring.boot;


import com.example.spring.boot.domain.Order;
import com.example.spring.boot.domain.OrderState;
import com.example.spring.boot.domain.QShipment;
import com.example.spring.boot.domain.Shipment;
import com.example.spring.boot.repository.OrderRepository;
import com.example.spring.boot.repository.ShipmentRepository;
import com.example.spring.boot.service.ShipmentService;
import com.example.spring.boot.service.impl.OrderServiceImpl;
import com.example.spring.boot.service.impl.ShipmentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderShipmentTest {
    private OrderServiceImpl orderService;

    private ShipmentServiceImpl shipmentService;

    private ShipmentRepository shipmentRepository;

    private OrderRepository orderRepository;

    private long orderId = 1L;
    private Long orderIdWithPaymentState = 2L;
    private String shipmentSerial = "15800826960";
    private String shipmentCompany = "顺丰快递";
    private Long shipmentId = 1L;

    @Before
    public void setUp() throws Exception {
        orderService = new OrderServiceImpl();
        shipmentService = new ShipmentServiceImpl();

        shipmentRepository = mockShipmentRepository();
        orderService.setShipmentRepository(shipmentRepository);

        orderRepository = mockOrderRepository();
        orderService.setOrderRepository(orderRepository);

        shipmentService.setShipmentRepository(shipmentRepository);
    }

    private OrderRepository mockOrderRepository() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findOne(orderId)).thenAnswer(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = new Order();
                order.setId(orderId);
                order.setState(OrderState.WAIT_SHIPMENT);
                return order;
            }
        });
        when(orderRepository.findOne(orderIdWithPaymentState)).thenAnswer(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = new Order();
                order.setId(orderIdWithPaymentState);
                order.setState(OrderState.SUCCESS);
                return order;
            }
        });
        return orderRepository;
    }

    private ShipmentRepository mockShipmentRepository() {
        ShipmentRepository shipmentRepository = mock(ShipmentRepository.class);
        when(shipmentRepository.save(any(Shipment.class))).then(new Answer<Shipment>() {
            @Override
            public Shipment answer(InvocationOnMock invocation) throws Throwable {
                Shipment shipment = invocation.getArgumentAt(0, Shipment.class);
                shipment.setId(shipmentId);
                return shipment;
            }
        });
        return shipmentRepository;
    }

    @Test
    public void shipOrder() throws Exception {
        Shipment shipment = orderService.ship(orderId, shipmentCompany, shipmentSerial);

        // check shipment
        assertThat(shipment.getId()).isNotNull();

        // check order status
        assertThat(shipment.getOrder().getState()).isEqualTo(OrderState.WAIT_CLAIM);
    }

    @Test(expected = IllegalStateException.class)
    public void shipOrderWithWaitPaymentState() throws Exception {
        Shipment shipment = orderService.ship(orderIdWithPaymentState, shipmentCompany, shipmentSerial);
    }
}
