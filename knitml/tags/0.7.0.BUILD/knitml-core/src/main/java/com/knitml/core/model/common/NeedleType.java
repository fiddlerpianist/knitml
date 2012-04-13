package com.knitml.core.model.common;

import java.util.ArrayList;
import java.util.List;

import javax.measure.Measurable;
import javax.measure.quantity.Length;

import com.knitml.core.common.NeedleStyle;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class NeedleType {

	protected String id;
	protected Measurable<Length> length;
	protected Measurable<Length> needleSize;
	protected NeedleStyle style;
	protected String brand;
	protected List<Needle> needles = new ArrayList<Needle>();
	
	public NeedleType() {
		super();
	}
	
	public void setNeedles(List<Needle> needles) {
		this.needles = needles;
	}

	public List<Needle> getNeedles() {
		return needles;
	}

	public String getId() {
		return id;
	}

	public Measurable<Length> getLength() {
		return length;
	}

	public Measurable<Length> getNeedleSize() {
		return needleSize;
	}
	
	public NeedleStyle getStyle() {
		return style;
	}

	public String getBrand() {
		return brand;
	}
	public void afterPropertiesSet() {
		for (Needle needle : needles) {
			needle.setType(this);
		}
	}

}
