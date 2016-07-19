package com.example.spring.boot;


import com.example.spring.boot.domain.ActualOrder;
import com.example.spring.boot.domain.ActualOrderState;
import com.example.spring.boot.domain.Shipment;
import com.example.spring.boot.repository.OrderRepository;
import com.example.spring.boot.repository.ShipmentRepository;
import com.example.spring.boot.service.impl.OrderServiceImpl;
import com.example.spring.boot.service.impl.ShipmentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ActualOrderShipmentTest {
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
        when(orderRepository.findOne(orderId)).thenAnswer(new Answer<ActualOrder>() {
            @Override
            public ActualOrder answer(InvocationOnMock invocation) throws Throwable {
                ActualOrder actualOrder = new ActualOrder();
                actualOrder.setId(orderId);
                actualOrder.setState(ActualOrderState.WAIT_SHIPMENT);
                return actualOrder;
            }
        });
        when(orderRepository.findOne(orderIdWithPaymentState)).thenAnswer(new Answer<ActualOrder>() {
            @Override
            public ActualOrder answer(InvocationOnMock invocation) throws Throwable {
                ActualOrder actualOrder = new ActualOrder();
                actualOrder.setId(orderIdWithPaymentState);
                actualOrder.setState(ActualOrderState.SUCCESS);
                return actualOrder;
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
        assertThat(shipment.getOrder().getState()).isEqualTo(ActualOrderState.WAIT_CLAIM);
    }

    @Test(expected = IllegalStateException.class)
    public void shipOrderWithWaitPaymentState() throws Exception {
        Shipment shipment = orderService.ship(orderIdWithPaymentState, shipmentCompany, shipmentSerial);
    }
}
