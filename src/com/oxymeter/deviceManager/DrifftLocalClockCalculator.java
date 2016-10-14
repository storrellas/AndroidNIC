package com.icognos.deviceManager;

import java.util.Calendar;
import java.util.TimeZone;

import com.icognos.util.Reference;
import com.icognos.util.Logger;

public class DrifftLocalClockCalculator {

	private static final int BUFFER_SIZE    = 2400;
	private static final int AMOUNT_SAMPLES_NEW_CALCULATION    = 1000;
	private static final int SAMPLE_RATE    = 1000;
//	private static final int TIME_CONVERSION    = 1000/ _sampleRate;

	
	//  -- Attributtes --
	//  -----------------
    private int _currentSize;
    private int _currentPosition;
    private boolean _isFirstSample;
    private double _timeFirstSample;
    private int _counterSamples;
    private double[] _bufferY;
    private double[] _bufferX;
    private int _maxDiff;
    private float _b;
    private int _lastTotalNumberSamples;
    private float _sampleRate;
	
    //  -- METHODS --
    // --------------
	
    /*!
     * Default Constructor
     */
    public DrifftLocalClockCalculator(){
        _bufferY = new double[BUFFER_SIZE];
        _bufferX = new double[BUFFER_SIZE];
        
        reset(SAMPLE_RATE);        
    }
    


    public void reset (float sampleRate){
        _sampleRate = sampleRate;
        _currentSize = 0;
        _currentPosition = 0;
        _isFirstSample = true;
        _timeFirstSample = 0;
        _counterSamples = 0;
        _maxDiff = 0;
        _b = 0;
        _lastTotalNumberSamples = 0;
    }
    
    public double fit(double[] x, double[] y, int ndata, int position)
	 // Given a set of data points y[1..ndata], fit them to a straight line
	 // y = a + bx by minimizing ?2. Returned is b.
	 {
	     int i;
	     double t,sxoss,sx=0.0,sy=0.0,st2=0.0,ss;
	     int indx = position;
	     double b = 0.0;
	
	     for (i = 0;i < ndata; i++) { //...or without weights.
	         sx += x[indx];
	         sy += y[indx];
	         indx = (indx + 1) % ndata;
	     }
	     ss=ndata;
	     sxoss=sx/ss;
	
	     indx = position;
	     for (i = 0; i < ndata; i++) {
	         t=x[indx]-sxoss;
	         st2 += t*t;
	         b += t*y[indx];
	         indx = (indx + 1) % ndata;
	     }
	     b /= st2; //Solve for a, b, sa, and sb.
	     return b;
	 }
    
    void newSample (){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        if (_isFirstSample)
        {
            _isFirstSample = false;
            //			// Implementation with LSL
            //_timeFirstSample = ApplicationTime::currentTime() * 1000;            

            // Old implementation
            //_timeFirstSample = QDateTime::currentMSecsSinceEpoch();
    		

    		_timeFirstSample = calendar.getTimeInMillis() ;
            
            
        }
        _counterSamples++;
        if ((_counterSamples % AMOUNT_SAMPLES_NEW_CALCULATION) == 0)
        {

//			// Implementation with LSL
//            int diff = ((int)((_counterSamples * TIME_CONVERSION) -
//                        ((ApplicationTime::currentTime() * 1000) -
//                                                            _timeFirstSample)));
//            if (0) /*diff < 0 || ((double)_maxDiff - 10) >= diff */

//        	// Old implementation
//            int diff = ((signed int)((_counterSamples * TIME_CONVERSION) -
//                    (QDateTime::currentMSecsSinceEpoch() - _timeFirstSample)));
        	
        	int diff = ((int)((_counterSamples * (1000/ _sampleRate) ) -
        				(calendar.getTimeInMillis() - _timeFirstSample)));        	
            //qDebug() << "diff = " << diff;
            if (diff < 0 || ((double)_maxDiff - 10) >= diff)                        
            {
                // we assume that pc clock goes behind device's
                // we compute only samples around the max
            }
            else
            {
                if (_maxDiff < diff)
                {
                    _maxDiff = diff;
                }
                if (_counterSamples > (30 * _sampleRate))
                {
                    _bufferY[_currentPosition] = diff;
                    _bufferX[_currentPosition] = _counterSamples;

                    //qDebug() << "x = " << _bufferX[_currentPosition] <<
                    //                        "\t\t\t\t\t\tdiff = " <<
                    //                                    _bufferY[_currentPosition];
                    _currentSize = (_currentSize < BUFFER_SIZE) ?
                                                        _currentSize + 1:
                                                                    _currentSize;
                    _currentPosition = (_currentPosition + 1) % BUFFER_SIZE;
                    int firstSamplePosition = 0;
                    if (_currentPosition < _currentSize)
                    {
                        firstSamplePosition = _currentPosition;
                    }
                    _lastTotalNumberSamples = _counterSamples;                    
                    _b = (float) fit(_bufferX, _bufferY, _currentSize, firstSamplePosition) * _sampleRate;
                    
                    //qDebug() << "b =" << _b << "currentPosition = " <<
                    //                                              _currentPosition;
                }
            }
        }
    }
    
    float getDrifft (Reference<Integer> totalSamples){
        if (totalSamples != null)
        {
            totalSamples.set( _counterSamples );
        }
        return _b;
    }
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		long MsecondsSinceEpoch = calendar.getTimeInMillis() ;
		Logger logger = Logger.getInstance();
		
		logger.info("MS since Epoch " +  MsecondsSinceEpoch, Logger.LOG_FILE_ON);

	}

}
