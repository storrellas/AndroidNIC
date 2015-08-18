package com.neuroelectrics.plotmanager;

public interface Datasource {
	
	public int getMax();
	
	public int getMin();
	
	public boolean isDataReady();
}
