package com.knitml.core.units;

import javax.measure.unit.ProductUnit;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

public interface RowGauge extends Gauge {
 
    public static final Unit<RowGauge> UNIT = new ProductUnit<RowGauge>(Units.ROW.divide(SI.CENTIMETER));
	
}
