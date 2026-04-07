package com.quantity.quantity_service.utils;

import com.quantity.quantity_service.enums.LengthUnit;
import com.quantity.quantity_service.enums.WeightUnit;
import com.quantity.quantity_service.enums.TemperatureUnit;
import com.quantity.quantity_service.enums.VolumeUnit;

public class UnitConverter {

    public static IMeasurable fromString(String unit) {

        try {
            return LengthUnit.valueOf(unit.toUpperCase());
        } catch (Exception ignored) {}

        try {
            return WeightUnit.valueOf(unit.toUpperCase());
        } catch (Exception ignored) {}

        try {
            return TemperatureUnit.valueOf(unit.toUpperCase());
        } catch (Exception ignored) {}

        try {
            return VolumeUnit.valueOf(unit.toUpperCase());
        } catch (Exception ignored) {}

        throw new IllegalArgumentException("Invalid unit: " + unit);
    }
}