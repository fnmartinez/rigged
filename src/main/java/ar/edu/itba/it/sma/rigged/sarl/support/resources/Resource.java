package ar.edu.itba.it.sma.rigged.sarl.support.resources;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Resource {
	
	private final String name;
	private int value;
	
	public Resource(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void alterValue(int value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		return name.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
					.append("name", name)
					.append("value", value)
					.build();
	}
}
