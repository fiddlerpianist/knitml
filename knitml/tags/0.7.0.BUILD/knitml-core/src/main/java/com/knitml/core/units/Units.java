package com.knitml.core.units;

import javax.measure.quantity.DataAmount;
import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

public final class Units {
	
	private Units() {
	}
	
	static {
		// relabel "oz" since the framework overwrites the weight ounce with the fluid ounce
		UnitFormat.getInstance().label(NonSI.OUNCE, "oz");
	}

	public static final Unit<DataAmount> STITCH = SI.BIT.alternate("st");
	public static final Unit<DataAmount> ROW = SI.BIT.alternate("row");
	public static final Unit<DataAmount> WRAP = SI.BIT.alternate("wrap");
	public static final Unit<StitchGauge> STITCHES_PER_INCH = STITCH.divide(NonSI.INCH).asType(StitchGauge.class);
	public static final Unit<StitchGauge> STITCHES_PER_CENTIMETER = STITCH.divide(SI.CENTIMETER).asType(StitchGauge.class);
	public static final Unit<RowGauge> ROWS_PER_INCH = ROW.divide(NonSI.INCH).asType(RowGauge.class);
	public static final Unit<RowGauge> ROWS_PER_CENTIMETER = ROW.divide(SI.CENTIMETER).asType(RowGauge.class);
	public static final Unit<YarnThickness> WRAPS_PER_INCH = WRAP.divide(NonSI.INCH).asType(YarnThickness.class);
	public static final Unit<YarnThickness> WRAPS_PER_CENTIMETER = WRAP.divide(SI.CENTIMETER).asType(YarnThickness.class);
	public static final Unit<Length> YARD = NonSI.YARD;
	public static final Unit<Length> INCH = NonSI.INCH;
	public static final Unit<Length> METER = SI.METER;
	public static final Unit<Length> CENTIMETER = SI.CENTIMETER;
    public static final Unit<Length> MILLIMETER = SI.MILLIMETER;
	public static final Unit<Length> NEEDLE_SIZE_MM = SI.MILLIMETER;
	public static final Unit<Length> NEEDLE_SIZE_US = USNeedleSize.getInstance();
	public static final Unit<Mass> GRAM = SI.GRAM;
    public static final Unit<Mass> OUNCE = NonSI.OUNCE;
	
	public static Unit<?> valueOf(String unit) {
		return Unit.valueOf(unit);
	}
	
}
