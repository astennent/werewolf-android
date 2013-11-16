package com.example.werewolf;

import org.apache.http.NameValuePair;

public class WerewolfNVP implements NameValuePair {

	private String name;
	private String value;

	public WerewolfNVP(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

}
