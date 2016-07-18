package com.example.spring.boot.service;


import com.example.spring.boot.domain.Shipment;

import java.util.Optional;

public interface ShipmentService {

    Optional<Shipment> findByOrder(long l);
}
