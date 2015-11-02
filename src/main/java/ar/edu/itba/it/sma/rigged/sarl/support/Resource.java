package ar.edu.itba.it.sma.rigged.sarl.support;

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

}
