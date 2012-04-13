package com.knitml.core.model.pattern;

import java.util.List;

public class Import {
	
	protected String location;
	protected List<Assignment> assignments;
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public List<Assignment> getAssignments() {
		return assignments;
	}
	public void setAssignments(List<Assignment> assignments) {
		this.assignments = assignments;
	}
	
}
