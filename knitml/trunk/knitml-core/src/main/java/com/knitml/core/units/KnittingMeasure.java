package com.knitml.core.units;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

public class KnittingMeasure<Q extends Quantity> extends Measure<String, Q> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static {
		@SuppressWarnings("unused")
		// to initialize the units specific to here
		Object obj = Units.WRAPS_PER_INCH;
	}

    private final String value;

    private final Unit<Q> unit;
    
    private NumberFormat numberFormat;

    public KnittingMeasure(String value, Unit<Q> unit) {
        this.value = value;
        this.unit = unit;
        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);
        this.numberFormat = format;
    }
    
	@Override
	public double doubleValue(Unit<Q> unit) {
        if ((unit == this.unit) || (unit.equals(this.unit)))
            return stringToDouble(this.value);
        return this.unit.getConverterTo(unit).convert(stringToDouble(this.value));
	}

	private double stringToDouble(String stringValue) {
		if (stringValue.equals("000")) {
			return -2;
		} else if (stringValue.equals("00")) {
			return -1;
		}
		return Double.valueOf(stringValue);
	}

	public String stringValue(Unit<Q> unit) {
		double doubleValue = doubleValue(unit);
		if (doubleValue == -1d) {
			return "00";
		} else if (doubleValue == -2d) {
			return "000";
		} else {
			return String.valueOf(numberFormat.format(doubleValue));
		}
	}
	
	@Override
	public Unit<Q> getUnit() {
		return this.unit;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public Measure<String, Q> to(Unit<Q> unit) {
        if ((unit == this.unit) || (unit.equals(this.unit)))
            return this;
        return new KnittingMeasure<Q>(stringValue(unit), unit);
	}

    public static <Q extends Quantity> Measure<String, Q> valueOf(
            String stringValue, Unit<Q> unit) {
        return new KnittingMeasure<Q>(stringValue, unit);
	}

}
