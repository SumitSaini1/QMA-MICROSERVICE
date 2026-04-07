package com.quantity.quantity_service.dto;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class QuantityRequest {

    @NotNull
    private Double value1;

    @NotBlank
    private String unit1;

    @NotNull
    private Double value2;

    @NotBlank
    private String unit2;
}