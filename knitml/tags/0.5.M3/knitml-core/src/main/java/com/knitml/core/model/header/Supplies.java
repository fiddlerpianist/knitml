package com.knitml.core.model.header;

import java.util.ArrayList;
import java.util.List;

public class Supplies {

	protected List<YarnType> yarnTypes = new ArrayList<YarnType>();
	protected List<Yarn> yarns = new ArrayList<Yarn>();
	protected List<NeedleType> needleTypes = new ArrayList<NeedleType>();
	protected List<Needle> needles = new ArrayList<Needle>();
	
	public List<YarnType> getYarnTypes() {
		return yarnTypes;
	}
	public List<Yarn> getYarns() {
		return yarns;
	}
	public List<NeedleType> getNeedleTypes() {
		return needleTypes;
	}
	public List<Needle> getNeedles() {
		return needles;
	}
	
	protected void afterPropertiesSet() {
		for (YarnType yarnType : getYarnTypes()) {
			List<Yarn> yarns = new ArrayList<Yarn>();
			for (Yarn yarn : getYarns()) {
				if (yarnType.equals(yarn.getYarnType())) {
					yarns.add(yarn);
				}
			}
			yarnType.setYarns(yarns);
		}
		for (NeedleType needleType : getNeedleTypes()) {
			List<Needle> needles = new ArrayList<Needle>();
			for (Needle needle : getNeedles()) {
				if (needleType.equals(needle.getType())) {
					needles.add(needle);
				}
			}
			needleType.setNeedles(needles);
		}
	}
	
	public boolean hasNeedles() {
		return needles.size() > 0 || needleTypes.size() > 0;
	}
	public boolean hasYarns() {
		return yarns.size() > 0 || yarnTypes.size() > 0;
	}
	public boolean hasAccessories() {
		return false;
	}
}
