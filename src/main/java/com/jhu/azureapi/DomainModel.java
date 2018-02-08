package com.jhu.azureapi;

public enum DomainModel {
	LANDMARKS("landmarks"),
	CELEBRITIES("celebrities");
	
	private String name;
	DomainModel(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
