package com.quantity.quantity_service.service;
import com.quantity.quantity_service.exception.QuantityMeasurementException;
import com.quantity.quantity_service.model.QuantityMeasurementEntity;
import com.quantity.quantity_service.repository.QuantityMeasurementRepository;
import com.quantity.quantity_service.utils.IMeasurable;
import com.quantity.quantity_service.utils.Quantity;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final QuantityMeasurementRepository repository;

    @Override
    public Quantity<?> add(Quantity<?> q1, Quantity<?> q2, IMeasurable targetUnit) {

        Quantity result = ((Quantity) q1).add((Quantity) q2, targetUnit);

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setQuantityValue(result.getValue());
        entity.setUnit(result.getUnit().toString());
        entity.setType("ADD");

        repository.save(entity);

        return result;
    }

    @Override
    public Quantity<?> subtract(Quantity<?> q1, Quantity<?> q2, IMeasurable targetUnit) {

        Quantity result = ((Quantity) q1).subtract((Quantity) q2, targetUnit);

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setQuantityValue(result.getValue());
        entity.setUnit(result.getUnit().toString());
        entity.setType("SUBTRACT");

        repository.save(entity);

        return result;
    }

    @Override
    public Quantity<?> convert(Quantity<?> quantity, IMeasurable targetUnit) {

        Quantity result = ((Quantity) quantity).convertTo(targetUnit);

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setQuantityValue(result.getValue());
        entity.setUnit(result.getUnit().toString());
        entity.setType("CONVERT");

        repository.save(entity);

        return result;
    }

    @Override
    public boolean compare(Quantity<?> q1, Quantity<?> q2) {

        boolean result = ((Quantity) q1).equals(q2);

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setQuantityValue(result ? 1 : 0);
        entity.setUnit("BOOLEAN");
        entity.setType("COMPARE");

        repository.save(entity);

        return result;
    }

    @Override
    public double divide(Quantity<?> q1, Quantity<?> q2) {
        if (q2.getValue()== 0) {
            throw new QuantityMeasurementException("Division by zero is not allowed");
        }

        double result = ((Quantity) q1).divide((Quantity) q2);

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setQuantityValue(result);
        entity.setUnit("RATIO");
        entity.setType("DIVIDE");

        repository.save(entity);

        return result;
    }
}