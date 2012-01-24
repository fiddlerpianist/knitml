package com.knitml.core.model.header;

import java.util.ArrayList;
import java.util.List;

import javax.measure.Measurable;
import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;

import com.knitml.core.units.YarnThickness;


/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class YarnType {
	protected String id;
	protected String brand;
	protected String category;
	protected String subcategory;
	protected Measurable<Length> ballLength;
	protected Measurable<Mass> ballWeight;
	protected Measurable<YarnThickness> thickness;
	protected String catalogId;
	protected String weight;
	protected List<Yarn> yarns = new ArrayList<Yarn>();
	
	public String getId() {
		return id;
	}
	public void setYarns(List<Yarn> yarns) {
		this.yarns = yarns;
	}
	public String getBrand() {
		return brand;
	}
	public String getCategory() {
		return category;
	}
	public String getSubcategory() {
		return subcategory;
	}
	public Measurable<Length> getBallLength() {
		return ballLength;
	}
	public Measurable<Mass> getBallWeight() {
		return ballWeight;
	}
	public Measurable<YarnThickness> getThickness() {
		return thickness;
	}
	public String getCatalogId() {
		return catalogId;
	}
	public String getWeight() {
		return weight;
	}
	public List<Yarn> getYarns() {
		return yarns;
	}
	public void afterPropertiesSet() {
		for (Yarn yarn : yarns) {
			yarn.setYarnType(this);
		}
	}
	
}
