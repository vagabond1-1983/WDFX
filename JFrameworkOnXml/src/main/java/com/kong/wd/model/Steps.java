package com.kong.wd.model;

import java.util.List;

public class Steps {
	private List<Step> steps;

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}
	
	public void addStep(Step step) {
		steps.add(step);
	}
	
}
