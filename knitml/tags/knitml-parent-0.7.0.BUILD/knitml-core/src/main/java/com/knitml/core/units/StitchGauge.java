package com.knitml.core.units;

import javax.measure.unit.ProductUnit;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

public interface StitchGauge extends Gauge {
 
    public static final Unit<StitchGauge> UNIT = new ProductUnit<StitchGauge>(Units.STITCH.divide(SI.CENTIMETER));
	
}
