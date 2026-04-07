package com.quantity.quantity_service.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class ResponseDto {
    private String operation;

    private Double value1;
    private String unit1;
    private Double value2;
    private String unit2;

    private double resultValue;
    private String resultUnit;

}
