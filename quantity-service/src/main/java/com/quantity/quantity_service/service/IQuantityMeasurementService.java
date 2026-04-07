package com.quantity.quantity_service.service;
import com.quantity.quantity_service.utils.Quantity;
import com.quantity.quantity_service.utils.IMeasurable;

public interface IQuantityMeasurementService {

    Quantity<?> add(
            Quantity<?> q1,
            Quantity<?> q2,
            IMeasurable targetUnit);

    Quantity<?> subtract(
            Quantity<?> q1,
            Quantity<?> q2,
            IMeasurable targetUnit);

    Quantity<?> convert(
            Quantity<?> quantity,
            IMeasurable targetUnit);

    boolean compare(
            Quantity<?> q1,
            Quantity<?> q2);

    double divide(
            Quantity<?> q1,
            Quantity<?> q2);
}