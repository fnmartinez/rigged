package ar.edu.itba.it.sma.rigged.api.beans;

public abstract class Alignment {

	private final Domain xDomain;
	private final Domain yDomain;
	
	public Alignment(int[] xRange, int[] yRange) {
		this.xDomain = new Domain(xRange[0], xRange[1]);
		this.yDomain = new Domain(yRange[0], yRange[1]);
	}
	
	public abstract Object getAgents();
	
	public Domain getXDomain() {
		return xDomain;
	}

	public Domain getYDomain() {
		return yDomain;
	}

	public static class Domain {
		private final int min;
		private final int max;
		
		public Domain(int min, int max) {
			this.min = min;
			this.max = max;
		}
		
		public int getMin() {
			return min;
		}
		
		public int getMax() {
			return max;
		}
	}
	
}
