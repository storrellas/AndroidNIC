package com.neuroelectrics.plotmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import com.androidplot.xy.XYSeries;
import com.neuroelectrics.util.Logger;

public class FFTDatasource implements Runnable, XYSeries, Datasource{

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
    private ArrayList<Integer> fftSampleArray;
    
    // FFT Attributes
    private Complex[] fftTrans;
    private double[] fftTransModule;
    private FastFourierTransformer transformer;    
    
    // Regression
    SimpleRegression eegRegression;
    double _LR_a, _LR_b;
    
    // Thread control
    boolean terminate;
    
    // Window size in which FFT is performed
    final int WINDOW_SIZE = 512;
    
    private boolean dataReady;
    
	//  -------------
	//  -- METHODS --
	//  -------------
    
    public FFTDatasource(Integer _maxNumberOfPoints){
    	// instantiate objects
    	notifier       = new MyObservable();    	
    	fftSampleArray = new ArrayList<Integer>();
    	eegRegression  = new SimpleRegression();
    	
    	maxNumbeOfPoints = _maxNumberOfPoints;
    	dataReady = false;
    	
    	// Counter
    	logger = Logger.getInstance();
    	
    	// Initalise terminate thread
    	terminate = false;
    	
    	// Initialise FFT variables
    	transformer = new FastFourierTransformer(DftNormalization.UNITARY);
    	
    	fftTransModule = new double[ WINDOW_SIZE ];
    }
    
    //@Override
    public void run() {

    	logger.info( "Initialise Plot thread (FFT)", Logger.LOG_FILE_ON);
    	
    	try{
	    	while( !terminate ){
	            Thread.sleep(2000); // decrease or remove to speed up the refresh rate
	            dataReady = false;
	            
	            // Only print FFT when buffer is full
	            if( fftSampleArray.size() >= WINDOW_SIZE ){

		            
		            // fftArray
		            double[] fftArray = new double[WINDOW_SIZE];
		            for( int i = 0; i < WINDOW_SIZE; i++) 
		            	// Detrended data
		            	fftArray[i] = (double) ( fftSampleArray.get(i) - (this.eegRegression.getSlope()*i + this.eegRegression.getIntercept()) );

		            
		            // Calculate FFT		    		
		    		fftTrans = transformer.transform(fftArray, TransformType.FORWARD);
    		
		    		for( int i = 0; i < WINDOW_SIZE; i++)
		    			fftTransModule[i] = this.getModule( this.fftTrans[i] ); 
		    		
		    		dataReady = true;
		    		
		            notifier.notifyObservers();
	            }

	    	}
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }


    	logger.info( "Terminate Plot thread", Logger.LOG_FILE_ON);
    	
    }
    
    public void addObserver(Observer observer) {
        notifier.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        notifier.deleteObserver(observer);
    }

    
    /**
     * Obtains the module of a complex number
     */
    public double getModule(Complex c){
    	return (Math.sqrt( c.getReal()*c.getReal() + c.getImaginary()*c.getImaginary()));    			
    }
    
    /**
     * Adds a EEG Sample to the array list
     * @param eegSample
     */
    public void addSample(int eegSample){
    	
    	    	
    	this.fftSampleArray.add( eegSample );
    	    	    	
		// Clear the array
		if( fftSampleArray.size() > this.maxNumbeOfPoints ){
			//eegData.clear();
			eegRegression.removeData(0, fftSampleArray.get(0));
			fftSampleArray.remove(0);
		}
		
		// Clear regression
		eegRegression.clear();
		for(int i = 0; i < fftSampleArray.size(); i++){
			eegRegression.addData(i, fftSampleArray.get(i));
		}
		
		
    }
    
    /**
     * Indicates when data is ready to perform the axis scaling
     */
	@Override
	public boolean isDataReady() {
		return dataReady;
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
		
		if( index > fftSampleArray.size() || fftSampleArray.size() == 0) return 1;
		else return fftTransModule[index];		
		
	}

	@Override
	public int size() {		
		if(fftSampleArray.size() == 0) return 1;
		else return WINDOW_SIZE;		 	
	}

	/**
	 * Terminates the current thread
	 */
	public void terminateThread(){
		terminate = true;
	}
	
	/**
	 * Gets the maximum of the array
	 * @return
	 */
	public int getMax(){
		double max = 0, candidate = 0;
		for (int i = 0; i < WINDOW_SIZE; i++){
			candidate = getModule( fftTrans[i] ); 
			if( max < candidate ) max = candidate; 
		}
		
		return (int) max;
	}
	
	/**
	 * Gets the minimum of the EEG array
	 * @return
	 */
	public int getMin(){
		return 0;
	}
	
	/**
	 * Updates the regression parameters (this function is not currently used)
	 */
	public void updateRegression(){
		int i;
		
		// Y-values
		ArrayList<Integer> Y = fftSampleArray;
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
