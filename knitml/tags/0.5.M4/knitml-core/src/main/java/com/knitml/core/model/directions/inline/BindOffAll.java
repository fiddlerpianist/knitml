package com.knitml.core.model.directions.inline;

import com.knitml.core.common.Wise;
import com.knitml.core.model.directions.InlineOperation;


public class BindOffAll implements InlineOperation {
	
	protected Wise type;
	protected String yarnIdRef;
	protected boolean fastenOffLastStitch;
	
	public boolean isFastenOffLastStitch() {
		return fastenOffLastStitch;
	}
	public void setFastenOffLastStitch(boolean fastenOffLastStitch) {
		this.fastenOffLastStitch = fastenOffLastStitch;
	}
	public void setType(Wise type) {
		this.type = type;
	}
	public void setYarnIdRef(String yarnIdRef) {
		this.yarnIdRef = yarnIdRef;
	}
	public Wise getType() {
		return type;
	}
	public String getYarnIdRef() {
		return yarnIdRef;
	}
	
}
