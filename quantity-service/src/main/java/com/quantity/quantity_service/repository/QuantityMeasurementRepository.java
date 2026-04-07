package com.quantity.quantity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quantity.quantity_service.model.QuantityMeasurementEntity;
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity,Long> {
    
}
