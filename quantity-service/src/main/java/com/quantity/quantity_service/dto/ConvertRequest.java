package com.quantity.quantity_service.dto;

import lombok.Data;

@Data
public class ConvertRequest {
    private double value;
    private String fromUnit;
    private String toUnit;
}