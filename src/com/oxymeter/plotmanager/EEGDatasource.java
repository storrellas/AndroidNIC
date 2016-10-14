package com.icognos.plotmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import com.androidplot.xy.XYSeries;
import com.icognos.util.Logger;

public class EEGDatasource implements Runnable, XYSeries, Datasource{

    // encapsulates management of the observers watching this datasource for update events:
    class MyObservable extends Observable {
	    public void notifyObservers() {
	        setChanged();
	        super.notifyObservers();
	    }
    }

	//  ----------------
	//  -- ATTRIBUTES --
	//  ----------------
 
    private Logger logger;

    private MyObservable notifier;

    // Data Containers
    private Integer maxNumbeOfPoints;
    private ArrayList<Integer> eegSampleArray;
    
    // Regression
    SimpleRegression eegRegression;
    double _LR_a, _LR_b;
    
    // Thread control
    boolean terminate;
    
	//  -------------
	//  -- METHODS --
	//  -------------
    
    public EEGDatasource(Integer _maxNumberOfPoints){
    	// instantiate objects
    	notifier       = new MyObservable();    	
    	eegSampleArray = new ArrayList<Integer>();
    	eegRegression  = new SimpleRegression();
    	
    	maxNumbeOfPoints = _maxNumberOfPoints;
    	
    	logger = Logger.getInstance();
    	
    	terminate = false;
    	
    }
    
    //@Override
    public void run() {

    	logger.info( "Initialise Plot thread (EEG)", Logger.LOG_FILE_ON);
    	
    	try{
	    	while( !terminate ){
	            Thread.sleep(1000); // decrease or remove to speed up the refresh rate.
	            notifier.notifyObservers();
	    	}
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }


    	logger.info( "Terminate Plot thread", Logger.LOG_FILE_ON);
/**/
    	
    }

    public void addObserver(Observer observer) {
        notifier.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        notifier.deleteObserver(observer);
    }

    /**
     * Adds a EEG Sample to the array list
     * @param eegSample
     */
    public void addSample(int eegSample){
    	
    	
    	//eegRegression.addData(eegSampleArray.size(), eegSample);
    	this.eegSampleArray.add( eegSample );
    	
////////////////////////////////    	
//    	logger.info("Intercept:" + eegRegression.getIntercept() + " Slope:"+ eegRegression.getSlope(), Logger.LOG_FILE_ON);
////////////////////////////////
    	    	
		// Clear the array
		if( eegSampleArray.size() > this.maxNumbeOfPoints ){
			//eegData.clear();
			eegRegression.removeData(0, eegSampleArray.get(0));
			eegSampleArray.remove(0);
		}
		
		// Clear regression
		eegRegression.clear();
		for(int i = 0; i < eegSampleArray.size(); i++){
			eegRegression.addData(i, eegSampleArray.get(i));
		}
		
		
		
		// Updates the regression parameters
		//updateRegression();
    }
    
    /**
     * Indicates when data is ready to perform the axis scaling
     */
	@Override
	public boolean isDataReady() {
		if( eegSampleArray.size() > 0 ) return true;
		else return false;
	}
    
    
	@Override
	public String getTitle() {
		return "EEG";
	}

	@Override
	public Number getX(int index) {
		return index;
	}

	@Override
	public Number getY(int index) {
		
		if( index > eegSampleArray.size() || eegSampleArray.size() == 0){
			return 1;
		}else{
			double detrendedData = eegSampleArray.get(index) - (this.eegRegression.getSlope()*index + this.eegRegression.getIntercept());
			return (int) detrendedData;
		}
		
	}

	@Override
	public int size() {
		
		if(eegSampleArray.size() == 0) return 1;
		else return eegSampleArray.size();			

	}

	/**
	 * Terminates the current thread
	 */
	public void terminateThread(){
		terminate = true;
	}
	
	/**
	 * Gets the maximum of the EEG array
	 * @return
	 */
	public int getMax(){
		int maxEEG = Collections.max(eegSampleArray);
		int maxEEGIndex = eegSampleArray.indexOf(maxEEG);
		
		return (int) ( maxEEG - (this.eegRegression.getSlope()*maxEEGIndex + this.eegRegression.getIntercept()) );
	}
	
	/**
	 * Gets the minimum of the EEG array
	 * @return
	 */
	public int getMin(){
		int minEEG = Collections.min(eegSampleArray);
		int minEEGIndex = eegSampleArray.indexOf(minEEG);
				
		
		return (int) ( minEEG - (this.eegRegression.getSlope()*minEEGIndex + this.eegRegression.getIntercept()) );
	}
	
	/**
	 * Updates the regression parameters (this function is not currently used)
	 */
	public void updateRegression(){
		int i;
		
		// Y-values
		ArrayList<Integer> Y = eegSampleArray;
		int N = Y.size();
		
		// X-values
		ArrayList<Integer> X = new ArrayList<Integer>();
		for(i = 0; i < N; i++) X.add(i); 		// Create array with 1,2,3,4,5,6,7,8,9
		
		
		double Sx = 0.0;
		double Sy = 0.0;
		
		for( i = 0; i < N; i++ ){
			Sx += X.get(i); Sy += Y.get(i);			
		}
			
//	    Sxy = sum(X.*Y');
//	    Sx_2 = sum(X.^2);
		ArrayList<Double> XY = new ArrayList<Double>();
		ArrayList<Double> X2 = new ArrayList<Double>();
		for( i = 0; i < N; i++ ){
			XY.add( (double) (X.get(i) * Y.get(i)) );
			X2.add( (double) (X.get(i) * X.get(i)) );
		}

	    double Sx_2 = 0.0;
	    double Sxy = 0.0;
	    for ( i = 0; i < N; i++)
	    {
	        Sxy+=XY.get(i);
	        Sx_2+=X2.get(i);
	    }
		
	    _LR_b = (N*Sxy - Sx*Sy) / (N*Sx_2 - Sx*Sx);
	    _LR_a = (Sy - _LR_b * Sx)/N;
	    
	    logger.info("Values (ax+b): " + _LR_a + " " + _LR_b, Logger.LOG_FILE_ON);
	}


	
}
