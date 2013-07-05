package com.kong.wd.model;

import java.util.List;

public class Suite {
	private String name;
	private Settings settings;
	private List<Case> cases;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addSettings(Settings settings) {
		this.settings = settings;
	}
	
	public void addCase(Case cs) {
		cases.add(cs);
	}
	
	
	public Settings getSettings() {
		return settings;
	}

	public List<Case> getCases() {
		return cases;
	}

	public String toString() {
		String newline = System.getProperty("line.separator");
		StringBuffer buf = new StringBuffer();

		buf.append("--- Settings ---").append(newline);
		buf.append(settings).append(newline);

		buf.append("--- CASES ---").append(newline);
		for (int i = 0; i < cases.size(); i++) {
			buf.append(cases.get(i)).append(newline);
		}

		return buf.toString();
	}
}
