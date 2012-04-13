package com.knitml.core.units;

import static com.knitml.core.units.Units.WRAP;

import javax.measure.quantity.Quantity;
import javax.measure.unit.ProductUnit;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

public interface YarnThickness extends Quantity {
 
    public static final Unit<YarnThickness> UNIT = new ProductUnit<YarnThickness>(WRAP.divide(SI.CENTIMETER));
	
}
