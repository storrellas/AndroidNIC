package com.neuroelectrics.deviceManager;


import java.io.IOException;
import java.util.ArrayList;


import android.app.Activity;

import com.neuroelectrics.IScanDiscoveryFinishedHandler;
import com.neuroelectrics.bluetooth.BluetoothManager;
import com.neuroelectrics.util.Reference;
import com.neuroelectrics.util.Logger;


public class RFCOMMDevice {

	//  -- Attributtes --
	//  -----------------
	
    /*!
     * \enum errType
     *
     * Description of the possible error types.
     */
    public enum errType
    {

        ERR_NO_ERROR(0),
        ERR_DEVICE_NOT_CONNECTED(1),
        ERR_CLOSING_DEVICE(2),
        ERR_RESETING_DEVICE(3),
        ERR_DEVICE_NOT_OPENED(4),
        ERR_READING_DEVICE(5),
        ERR_WRITING_DEVICE(6),
        ERR_CONFIGURING_DEVICE(7),
        ERR_CONFIGURATION_VALUE(8),
        ERR_DEVICE_NOT_CONNECTED_WSAEINVAL(9);


        private int numVal;

        errType(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }
    }
	
    /*!
     * \property Logger
     *
     * Logger of the application
     */
    private Logger logger;
    
    /*!
     * \property AndroidBluetoothManager
     *
     * Wrapper for the API bluetooth
     */    
    private BluetoothManager _btManager;
    
    /*!
     * \property RFCOMMDevice::_lastError
     *
     * Last error that might have happened
     */
    private errType _lastError;

    /*!
     * \property RFCOMMDevice::_handle
     *
     * Internal handle used in the hardwre operations.
     */
    private int _handle;

    /*!
     * \property RFCOMMDevice::_rfcommTimeout
     *
     * This property holds the timeout for the reading operations.
     */
    private  int _rfcommTimeout;

    /*!
     * \property RFCOMMDevice::_numberOfInstances
     *
     * Counter of the number of instances of the class. It is used for the
     * Bluetooh initializations and closing operations.
     */
    static int _numberOfInstances;

    //  -- METHODS --
    // --------------
    
	/*!
	 * Public Constructor
	 */
	public RFCOMMDevice(Activity activity, IScanDiscoveryFinishedHandler _scanDiscoveryFinished){
		logger = Logger.getInstance();
		
		_lastError = errType.ERR_NO_ERROR;
		_handle = 0;
		_rfcommTimeout = 100000;
		
		_btManager = BluetoothManager.getInstance(activity, _scanDiscoveryFinished);
		
	    if (_numberOfInstances++ == 0)
	    {
	        int ret = 0;
	        ret = _btManager.iniBTSockets();
	    }
		
	}

	


	void logDebugBluetooth (String string)
	{
//	    writeLogDebugBluetooth(string.toAscii().data());
//	    qDebug()<<string;
	}

	
    /*!
     * It opens the connection with the device identified by the provided
     * Bluetooth mac address.
     *
     * \param macAddress Bluetooth mac address of the device.
     *
     * \return It returns ERR_DEVICE_NOT_CONNECTED if the hardware can not be
     * opened.
     */
    public errType open (String macAddress) throws IOException{
    	
    	Reference<Integer> handleRef = new Reference<Integer>(0);

    	logger.info("Opening device ...", Logger.LOG_FILE_ON);
		int returnValue = _btManager.openRFCOMM(macAddress, handleRef);
		this._handle = handleRef.get();
		if (returnValue != 1)
		{
		    if (returnValue == 10022)
		        _lastError = errType.ERR_DEVICE_NOT_CONNECTED_WSAEINVAL;
		    else
		        _lastError = errType.ERR_DEVICE_NOT_CONNECTED;
		
		    logger.info("ERROR IN RFCOMMDevice::open" + returnValue, Logger.LOG_FILE_ON);
		    return _lastError;
		}
		
		
		return errType.ERR_NO_ERROR;    	    	
    }

    /*!
     * It performs the operations for closing the hardware device.
     *
     * \return It returns ERR_CLOSING_DEVICE if the hardware could not be
     * closed.
     */
    public errType close () throws IOException{
    	
		logDebugBluetooth("[RFD] Calling close device");
		
    	logger.info("Closing device ...", Logger.LOG_FILE_ON);
		if (_btManager.closeRFCOMM(_handle) <= 0)
		{
		    _lastError = errType.ERR_CLOSING_DEVICE;
		    return _lastError;
		}
		return errType.ERR_NO_ERROR;
    }

    /*!
     * It reads from the hardware device an specific number of bytes.
     *
     * \param buffer Pointer to buffer that receives the data from the device.
     *
     * \param numBytes Number of bytes to be read from the hardware device.
     *
     * \return Number of actual bytes read. If this number does not match with
     * the numBytes parameter, then check the getLastError method.
     */
    public int read (ArrayList<Byte> buffer, long numBytes) throws IOException{
        int nBytes = -1;
        nBytes = _btManager.readRFCOMM(_handle, buffer, numBytes, _rfcommTimeout);
        //logger.info ("RFCOMMDevice::read" + nBytes, Logger.LOG_FILE_ON);
        if (nBytes < 0)
        {
            _lastError = errType.ERR_READING_DEVICE;
        }
        return nBytes;
    }

    /*!
     * It writes to the hardware device the number of bytes specified in the
     * parameters.
     *
     * \param buffer Pointer to the buffer where the byte to be sen are placed.
     *
     * \param numBytes Number of bytes placed in buffer to be sent to the
     * device.
     *
     * \return Number of byte written to the device.
     */
    public int write (ArrayList<Byte> buffer, long numBytes) throws IOException{
    	    	
        int ret = -1;
        ret = _btManager.writeRFCOMM(_handle, buffer, numBytes);
        //logger.info ("RFCOMMDevice::write" + nBytes, Logger.LOG_FILE_ON);
        if (ret < 0)
        {
            _lastError = errType.ERR_WRITING_DEVICE;
        }
        return ret;
    	    	
    }

    /*!
     * It returns the last error that might have happened when accessing the
     * hardware device.
     *
     * \return Error type identifier.
     */
    public errType getLastError (){
        return _lastError;
    }

    /*!
     * It sets the timeout for reading operations.
     *
     * \param timeout time in microseconds before returning from a reading
     * operation
     */
    public void setReadTimeout (int timeout){
        _rfcommTimeout = timeout;
    }

    /*!
     * It returns the pending bytes in the reading buffer.
     *
     * \return Number of byte pending to be read.
     */
    public long pendingBytesOnReading (){
        return 0;
    }
	
	
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		System.out.println("-- Program start --");
//		Logger logger = LoggerUtil.getSdkLogger();
//		logger.info("Testing RFCOMMDevice");
//		
//		RFCOMMDevice rfcommdevice = new RFCOMMDevice();
//		
//		// Open device
//		logger.info("Opening device ...");
//		errType error = rfcommdevice.open("00:07:80:64:EB:B6");
//		if( error != errType.ERR_NO_ERROR){
//			logger.info("Error in rfcommDevice.open");
//		}else{
//			logger.info("rfcommDevice.open successful");
//		}
//
//		// Write Start Operation
//		logger.info("Writing Start Operation ...");
//		ArrayList<Byte> buffer = new ArrayList<Byte>();
//		buffer.add((byte) 83);
//		buffer.add((byte) 79);		
//		buffer.add((byte) 70);
//		buffer.add((byte) 9);
//		buffer.add((byte) 127);
//		buffer.add((byte) 127);
//		buffer.add((byte) 69);
//		buffer.add((byte) 79);
//		buffer.add((byte) 70);
//		rfcommdevice.write( buffer, buffer.size() );
//		if( error != errType.ERR_NO_ERROR){
//			logger.info("Error in rfcommDevice.write");
//		}else{
//			logger.info("rfcommDevice.write successful");
//		}
//		
//		
//		// Reading bytes
//		logger.info("Reading bytes ...");
//		for(int i = 0; i < 5; i++){
//			int numberBytes = 50;
//			buffer.clear();
//			rfcommdevice.read( buffer, numberBytes );
//			
//			for(int j = 0; j < numberBytes; j++){
//				logger.info("Read(" + i +")" + j + ":" + buffer.get(j));
//			}			
//		}	
//		
//		
//		
//		// Write Start Operation
//		logger.info("Writing Stop Operation ...");
//		buffer.clear();
//		buffer.add((byte) 83);
//		buffer.add((byte) 79);		
//		buffer.add((byte) 70);
//		buffer.add((byte) 9);
//		buffer.add((byte) 0);
//		buffer.add((byte) 0);
//		buffer.add((byte) 69);
//		buffer.add((byte) 79);
//		buffer.add((byte) 70);				
//		rfcommdevice.write( buffer, buffer.size() );
//		if( error != errType.ERR_NO_ERROR){
//			logger.info("Error in rfcommDevice.write");
//		}else{
//			logger.info("rfcommDevice.write successful");
//		}
//		
//		// Close device		
//		logger.info("Close device ...");
//		error = rfcommdevice.close();
//		if( error != errType.ERR_NO_ERROR){
//			logger.info("Error in rfcommDevice.close");
//		}else{
//			logger.info("rfcommDevice.close successful");
//		}
//		
//		
//		System.out.println("-- Program end --");
	}
    
}
