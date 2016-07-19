package com.example.spring.boot;

import com.example.spring.boot.domain.ActualOrder;
import com.example.spring.boot.domain.Shipment;
import com.example.spring.boot.repository.ShipmentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
@RunWith(SpringJUnit4ClassRunner.class)
public class ShipmentRepositoryTest {
    @Resource
    private ShipmentRepository shipmentRepository;

    @Test
    @Sql(statements = {
        "insert into orders (id, order_type) values (1, 'ACTUAL')"
    })
    public void saveShipment() throws Exception {
        ActualOrder actualOrder = new ActualOrder();
        actualOrder.setId(1L);
        Shipment shipment = new Shipment();
        shipment.setCompany("顺丰快递");
        shipment.setSerial("15800826960");
        shipment.setOrder(actualOrder);
        shipmentRepository.save(shipment);
        assertThat(shipment.getId()).isNotNull();
    }


}
