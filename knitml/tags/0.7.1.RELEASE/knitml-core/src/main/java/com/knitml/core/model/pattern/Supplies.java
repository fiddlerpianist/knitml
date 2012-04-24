package com.knitml.core.model.pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.knitml.core.model.common.Needle;
import com.knitml.core.model.common.NeedleType;
import com.knitml.core.model.common.StitchHolder;
import com.knitml.core.model.common.Yarn;
import com.knitml.core.model.common.YarnType;

public class Supplies {

	protected List<YarnType> yarnTypes = new ArrayList<YarnType>();
	protected List<NeedleType> needleTypes = new ArrayList<NeedleType>();
	protected List<StitchHolder> stitchHolders = new ArrayList<StitchHolder>();

	private List<Yarn> yarns;
	private List<Needle> needles;
	
	public List<YarnType> getYarnTypes() {
		return yarnTypes;
	}
	public List<NeedleType> getNeedleTypes() {
		return needleTypes;
	}
	public List<Needle> getNeedles() {
		return needles;
	}
	public List<Yarn> getYarns() {
		return yarns;
	}

	public List<StitchHolder> getStitchHolders() {
		return stitchHolders;
	}
	
	protected void afterPropertiesSet() {
		this.yarns = new ArrayList<Yarn>();
		for (YarnType yarnType : getYarnTypes()) {
			for (Yarn yarn : yarnType.getYarns()) {
				this.yarns.add(yarn);
			}
		}
		this.yarns = Collections.unmodifiableList(this.yarns);
		
		this.needles = new ArrayList<Needle>();
		for (NeedleType needleType : getNeedleTypes()) {
			for (Needle needle : needleType.getNeedles()) {
				this.needles.add(needle);
			}
		}
		this.needles = Collections.unmodifiableList(this.needles);
	}
	
	public boolean hasNeedles() {
		return needleTypes.size() > 0;
	}
	public boolean hasYarns() {
		return yarnTypes.size() > 0;
	}
	public boolean hasAccessories() {
		return hasStitchHolders();
	}
	public boolean hasStitchHolders() {
		return stitchHolders.size() > 0;
	}
}
