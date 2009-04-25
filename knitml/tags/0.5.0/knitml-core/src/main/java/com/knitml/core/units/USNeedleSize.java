package com.knitml.core.units;

import javax.measure.converter.ConversionException;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Length;
import javax.measure.unit.DerivedUnit;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.DualHashBidiMap;

public class USNeedleSize extends DerivedUnit<Length> {

	private static USNeedleSize instance = new USNeedleSize(); 
	
	public static USNeedleSize getInstance() {
		return instance;
	}
	
	private static BidiMap<Double, Double> usToStandardConversion = new DualHashBidiMap<Double, Double>();
	
	static {
		usToStandardConversion.put(-2d, 0.00150d);
		usToStandardConversion.put(-1d, 0.00175d);
		usToStandardConversion.put(0d, 0.00200d);
		usToStandardConversion.put(1d, 0.00225d);
		usToStandardConversion.put(1.5d, 0.00250d);
		usToStandardConversion.put(2d, 0.00275d);
		usToStandardConversion.put(2.5d, 0.00300d);
		usToStandardConversion.put(3d, 0.00325d);
		usToStandardConversion.put(4d, 0.00350d);
		usToStandardConversion.put(5d, 0.00375d);
		usToStandardConversion.put(6d, 0.00400d);
		usToStandardConversion.put(7d, 0.00450d);
		usToStandardConversion.put(8d, 0.00500d);
		usToStandardConversion.put(9d, 0.00550d);
		usToStandardConversion.put(10d, 0.00600d);
		usToStandardConversion.put(10.5d, 0.00650d);
		usToStandardConversion.put(10.65d, 0.00700d);
		usToStandardConversion.put(10.8d, 0.00750d);
		usToStandardConversion.put(11d, 0.00800d);
		usToStandardConversion.put(13d, 0.00900d);
		usToStandardConversion.put(15d, 0.01000d);
		usToStandardConversion.put(17d, 0.01200d);
		usToStandardConversion.put(19d, 0.01600d);
		usToStandardConversion.put(35d, 0.01900d);
		usToStandardConversion.put(50d, 0.02500d);
	}
	
	private USToStandardConverter usToStandardConverter = new USToStandardConverter();
	private StandardToUSConverter standardToUsConverter = new StandardToUSConverter();
	
	private USNeedleSize() {
        UnitFormat.getInstance().label(this, "US");
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof USNeedleSize)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return getStandardUnit().hashCode();
	}
	
	

	@Override
	public Unit<? super Length> getStandardUnit() {
		return SI.METER;
	}

	@Override
	public UnitConverter toStandardUnit() {
		return usToStandardConverter;
	}

	private class USToStandardConverter extends UnitConverter {

		@Override
		public double convert(double x) throws ConversionException {
			return usToStandardConversion.get(x);
		}

		@Override
		public UnitConverter inverse() {
			return standardToUsConverter;
		}

		@Override
		public boolean isLinear() {
			return false;
		}

	}

	private class StandardToUSConverter extends UnitConverter {

		@Override
		public double convert(double x) throws ConversionException {
			return usToStandardConversion.getKey(x);
		}

		@Override
		public UnitConverter inverse() {
			return usToStandardConverter;
		}

		@Override
		public boolean isLinear() {
			return false;
		}

	}
}