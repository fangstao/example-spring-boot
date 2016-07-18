package com.example.spring.boot.service.impl;

import com.example.spring.boot.domain.QOrder;
import com.example.spring.boot.domain.QShipment;
import com.example.spring.boot.domain.Shipment;
import com.example.spring.boot.repository.ShipmentRepository;
import com.example.spring.boot.service.ShipmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    private ShipmentRepository shipmentRepository;

    @Override
    public Optional<Shipment> findByOrder(long orderId) {
        Shipment shipment = shipmentRepository.findOne(QShipment.shipment.order.id.eq(orderId));
        return Optional.ofNullable(shipment);
    }

    @Resource
    public void setShipmentRepository(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }
}
