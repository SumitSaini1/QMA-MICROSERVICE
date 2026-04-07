package com.quantity.quantity_service.utils;

public class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;
    private static final double EPSILON = 0.0001;

    public Quantity(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    public Quantity<U> convertTo(U targetUnit) {

        double base = unit.convertToBaseUnit(value);

        double result = targetUnit.convertFromBaseUnit(base);

        return new Quantity<>(result, targetUnit);
    }

    private enum ArithmeticOperation {
        ADD {
            @Override
            double compute(double a, double b) {
                return a + b;
            }
        },
        SUBTRACT

        {

            @Override
            double compute(double a, double b) {
                return a - b;
            }
        },
        DIVIDE

        {

            @Override
            double compute(double a, double b) {
                if (b == 0) {
                    throw new ArithmeticException("Division by zero is not possible");
                }
                return a / b;
            }
        };

        abstract double compute(double a, double b);

    }

    private void validateArithmeticOperands(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException("Other quantity cannot be null");
        }

        if (!this.unit.getClass().equals(other.unit.getClass())) {
            throw new IllegalArgumentException("Both units must belong to same category");
        }

        if (!Double.isFinite(this.value) || !Double.isFinite(other.value)) {
            throw new IllegalArgumentException("Values must be finite");
        }
    }

    private double performanceArithmetic(Quantity<U> other, ArithmeticOperation operation) {
        if (!unit.supportsArithmetic()) {
            throw new UnsupportedOperationException(
                    unit.getClass().getSimpleName() + " does not support arithmetic operations");
        }
        validateArithmeticOperands(other);

        double base1 = unit.convertToBaseUnit(value);
        double base2 = other.unit.convertToBaseUnit(other.value);
        return operation.compute(base1, base2);

    }

    public Quantity<U> add(Quantity<U> other) {

        double resultBase = performanceArithmetic(other, ArithmeticOperation.ADD);

        double result = unit.convertFromBaseUnit(resultBase);

        return new Quantity<>(result, unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double resultBase = performanceArithmetic(other, ArithmeticOperation.ADD);
        double result = targetUnit.convertFromBaseUnit(resultBase);

        return new Quantity<>(result, targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double resultBase = performanceArithmetic(other, ArithmeticOperation.SUBTRACT);
        double result = targetUnit.convertFromBaseUnit(resultBase);
        return new Quantity<>(result, targetUnit);

    }

    public Quantity<U> subtract(Quantity<U> other) {

        double resultBase = performanceArithmetic(other, ArithmeticOperation.SUBTRACT);

        double result = unit.convertFromBaseUnit(resultBase);
        return new Quantity<>(result, unit);

    }

    public double divide(Quantity<U> other) {

        return performanceArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    public static double convert(double value,
            IMeasurable source,
            IMeasurable target) {

        if (source == null || target == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }

        double baseValue = source.convertToBaseUnit(value);

        return target.convertFromBaseUnit(baseValue);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof Quantity<?> other))
            return false;

        if (unit == null || other.unit == null)
            return false;

        if (unit instanceof Enum && other.unit instanceof Enum) {
            Class<?> thisEnum = ((Enum<?>) unit).getDeclaringClass();
            Class<?> otherEnum = ((Enum<?>) other.unit).getDeclaringClass();

            if (!thisEnum.equals(otherEnum))
                return false;
        } else {
            return false;
        }

        double base1 = unit.convertToBaseUnit(value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        return Math.abs(base1 - base2) < EPSILON;
    }

    @Override
    public int hashCode() {

        double base = unit.convertToBaseUnit(value);

        return Double.hashCode(base);
    }
}