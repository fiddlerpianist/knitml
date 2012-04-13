package com.knitml.core.units;

import javax.measure.quantity.Quantity;
import javax.measure.unit.ProductUnit;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

public interface Gauge extends Quantity {
 
    public static final Unit<Gauge> UNIT = new ProductUnit<Gauge>(SI.BIT.divide(SI.CENTIMETER));
	
}
