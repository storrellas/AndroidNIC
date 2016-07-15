package com.neuroelectrics.plotmanager;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Observable;
import java.util.Observer;

import android.graphics.Color;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.neuroelectrics.util.Logger;

public class PlotManager {
	
	/**
	 * This class redraws a plot whenever an update is received 
	 * @author sergi
	 *
	 */
    private class PlotUpdater implements Observer {
    	
        private XYPlot plot;
        private Datasource datasource;
        public PlotUpdater(XYPlot _plot) {
            this.plot = _plot;
        }
        @Override
        public void update(Observable o, Object arg) {
        	// Redraw the graph
        	plot.redraw( );
        	        	
        }               
        
        public void clearPlot(){
        	plot.clear();
        }
        
    }

    /**
     * Labels for the X Axis
     * @author sergi
     *
     */
    private class GraphXLabelFormat extends Format {

        private String[] LABELS;// = {"0", "Fs/6", "2Fs/6", "3Fs/6"};


        public GraphXLabelFormat(){
        	super();
        	LABELS = new String[260];
        	
        	for(int i = 0; i < 260; i++) LABELS[i] = "NONE" + i;
        	LABELS[0]   = "0";
        	LABELS[64]  = "Fs/6";
        	LABELS[128] = "Fs/4";
        	LABELS[192] = "3Fs/8";
        	LABELS[256] = "Fs/2";
        }
        
		@Override
		public StringBuffer format(Object object, StringBuffer buffer, FieldPosition field) {
            int parsedInt = Math.round(Float.parseFloat(object.toString()));
            String labelString = LABELS[parsedInt];

            buffer.append(labelString);
            return buffer;
		}

		@Override
		public Object parseObject(String string, ParsePosition position) {
            return java.util.Arrays.asList(LABELS).indexOf(string);
		}
    }
    
	//  ----------------
	//  -- ATTRIBUTES --
	//  ----------------
    
	// UI logger
	private Logger logger;	
    
	// EEG Plot
    private XYPlot eegPlot;    
    public  EEGDatasource eegDataSource;
    private PlotUpdater eegPlotUpdater;
    
	// FFT Plot    
    private XYPlot fftPlot;
    public  FFTDatasource fftDataSource;
    private PlotUpdater fftPlotUpdater;
    
    // Counts the number of samples
    private int countSample;
    
	/**
	 * Public constructor
	 */
    public PlotManager(XYPlot _eegPlot, XYPlot _fftPlot){
    	
    	logger = Logger.getInstance();

    	countSample = 0;
    	
		// Get the plot for EEG and its FFT
        eegPlot = _eegPlot;
        fftPlot = _fftPlot;

        // creates plot updater object handling
        eegPlotUpdater = new PlotUpdater(eegPlot);
        fftPlotUpdater = new PlotUpdater(fftPlot);
        
        // Configure plot        
        this.configureEEGPlot();
        this.configureFFTPlot();
        
        
    }
    
    /**
     * Configures both eeg and fft plots
     */
    public void configureEEGPlot(){
    	
    	// Clear EEG plot
		eegPlotUpdater.clearPlot();

		// Title for the EEG Plot
        eegPlot.setTitle("Temporal EEG");
		
        // only display whole numbers in domain labels
        eegPlot.getGraphWidget().setDomainValueFormat(new DecimalFormat("0"));

        // Initialise datasource with number of points
        eegDataSource = new EEGDatasource(512);
        
		// Add the series to the plot
        LineAndPointFormatter line = new LineAndPointFormatter(Color.parseColor("#CD6600"), null, null, null) ;
        line.getLinePaint().setStrokeWidth(3);
        eegPlot.addSeries(eegDataSource, line);

        // create a series using a formatter with some transparency applied:
        eegPlot.setGridPadding(5, 0, 5, 0);

        // hook up the plotUpdater to the data model:
        eegDataSource.addObserver(eegPlotUpdater);

        eegPlot.setDomainStepMode(XYStepMode.SUBDIVIDE);
        eegPlot.setDomainStepValue(eegDataSource.size());

        // Domain step value and 
        eegPlot.setDomainStepValue(16);
        eegPlot.setTicksPerDomainLabel(5);
        
        // Thin out domain/range tick labels so they dont overlap each other:
        eegPlot.setRangeStepValue(10);        
        eegPlot.setTicksPerRangeLabel(3);
                
        // Set the range boundaries (Y values)
        eegPlot.setRangeBoundaries(-5000, 5000, BoundaryMode.FIXED);        
        // Set the range Domain (X values)
        eegPlot.setDomainBoundaries(0, 512, BoundaryMode.FIXED);

        
        // Remove legend
        eegPlot.getLayoutManager().remove(eegPlot.getLegendWidget());
        eegPlot.getLayoutManager().remove(eegPlot.getDomainLabelWidget());


        // Leave some space for the axis to fit in
        eegPlot.getGraphWidget().setMarginLeft(30);
        eegPlot.getGraphWidget().setMarginRight(10);
        eegPlot.getGraphWidget().setMarginTop(10);
        
        // Changes the color
        // -------------------
        
        // This gets rid of the black border (up to the graph) there is no black border around the labels
        eegPlot.getBackgroundPaint().setColor(Color.TRANSPARENT);

        // This gets rid of the black behind the graph
        eegPlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
                
        // Remove border
        eegPlot.getBorderPaint().setColor(Color.TRANSPARENT);
    	
                        
    }
    
    /**
     * Configures the plot for the FFT
     */
    public void configureFFTPlot(){

    	// Clear EEG plot
		fftPlotUpdater.clearPlot();

		// Title for the EEG Plot
        fftPlot.setTitle("Fast Fourier Transform EEG");
		
        // only display whole numbers in domain labels
        //fftPlot.getGraphWidget().setDomainValueFormat(new DecimalFormat("0"));
        fftPlot.getGraphWidget().setDomainValueFormat(new GraphXLabelFormat());
        
		// Create datasource objects
        fftDataSource = new FFTDatasource(512);
		
		// Add the series to the plot
        LineAndPointFormatter line = new LineAndPointFormatter(Color.parseColor("#CD6600"), null, null, null) ;
        line.getLinePaint().setStrokeWidth(3);
        fftPlot.addSeries(fftDataSource, line);
        
        // create a series using a formatter with some transparency applied:
        fftPlot.setGridPadding(5, 0, 5, 0);

        // hook up the plotUpdater to the data model:
        fftDataSource.addObserver(fftPlotUpdater);

        fftPlot.setDomainStepMode(XYStepMode.SUBDIVIDE);
        fftPlot.setDomainStepValue(eegDataSource.size());

        // Domain step value and 
        fftPlot.setDomainStepValue(9);
        fftPlot.setTicksPerDomainLabel(2);
        
        // thin out domain/range tick labels so they dont overlap each other:
        fftPlot.setRangeStepValue(10);   
        fftPlot.setTicksPerRangeLabel(3);
        
        // Set the range boundaries (Y values)
        fftPlot.setRangeBoundaries(0, 500, BoundaryMode.FIXED);        
        // Set the range Domain (X values)
        fftPlot.setDomainBoundaries(0, 256, BoundaryMode.FIXED);

        
        // Remove legend
        fftPlot.getLayoutManager().remove(fftPlot.getLegendWidget());
        fftPlot.getLayoutManager().remove(fftPlot.getDomainLabelWidget());
        
        // Leave some space for the axis to fit in
        fftPlot.getGraphWidget().setMarginLeft(30);
        fftPlot.getGraphWidget().setMarginRight(10);
        fftPlot.getGraphWidget().setMarginTop(10);
        
        // Changes the color
        // -------------------
        
        // This gets rid of the black border (up to the graph) there is no black border around the labels
        fftPlot.getBackgroundPaint().setColor(Color.TRANSPARENT);

        // This gets rid of the black behind the graph
        fftPlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
                
        // Remove border
        fftPlot.getBorderPaint().setColor(Color.TRANSPARENT);
        
    }
    
	/**
	 * Starts plotting procedure for EEG
	 */
	public void start(){
        		
		// Clears the plot
		this.configureEEGPlot();
		this.configureFFTPlot();
		
        // kick off the data generating thread:
        new Thread(eegDataSource).start();        
        new Thread(fftDataSource).start();	
	}
    
	
	
	/**
	 * Stops the plot
	 */
	public void stop(){
		
		// Terminates the plot thread
		eegDataSource.terminateThread();
		fftDataSource.terminateThread();
		
	}
	
	/**
	 * Adds a sample to the plot
	 */
	public void addSample(int sample){
		if( eegDataSource != null ){
			eegDataSource.addSample(sample);
			fftDataSource.addSample(sample);
			countSample++;
		}else{
			logger.info("eegData source is null", Logger.LOG_FILE_ON);
		}
		
		// Adjust the EEG Plot axis every 512 samples when data is ready
		if( eegDataSource.isDataReady() && fftDataSource.isDataReady() && countSample > 512){

			// EEG Axis adjust
			double maxEEG = (double) eegDataSource.getMax();
			double minEEG = (double) eegDataSource.getMin();				
			eegPlot.setRangeBoundaries(minEEG*1.1, maxEEG*1.1, BoundaryMode.FIXED);
			logger.info("EEG MAX: " + maxEEG*1.1 + " MIN: " + minEEG*1.1, Logger.LOG_FILE_ON);
	        
			// FFT Axis adjust
			double maxFFT = (double) fftDataSource.getMax();
			double minFFT = (double) fftDataSource.getMin();			
			fftPlot.setRangeBoundaries(minFFT*1.1, maxFFT*1.1, BoundaryMode.FIXED);
			logger.info("FFT MAX: " + maxFFT*1.1 + " MIN: " + minFFT*1.1, Logger.LOG_FILE_ON);
			
			// Restart sample counter
	        countSample = 0;
		}
		
		
	}
		
}
