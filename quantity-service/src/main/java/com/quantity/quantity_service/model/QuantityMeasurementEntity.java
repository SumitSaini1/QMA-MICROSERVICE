package com.quantity.quantity_service.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name="quantity_measurement")
@NoArgsConstructor
@AllArgsConstructor

public class QuantityMeasurementEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private double quantityValue;
    private String unit;
    private String type;
    

}