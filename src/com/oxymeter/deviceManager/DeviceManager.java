package com.icognos.deviceManager;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.os.SystemClock;

import com.icognos.IenzoHandler;
import com.icognos.IScanDiscoveryFinishedHandler;
import com.icognos.bluetooth.BluetoothManager;
import com.icognos.util.Logger;
import com.icognos.util.Reference;

public class DeviceManager implements Runnable {


	
	// -- Attributtes --
	// -----------------

	private static final String DEVICE_NAME = "NE-";

	// Constants for number of registers
	public static final int ACCEL_NUM_REGS = 4;
	public static final int EEG_NUM_REGS = 128;
	public static final int SDCARD_NUM_REGS = 133;
	public static final int STM_NUM_REGS = 128;
	
	//public static final int MAX_LENGTH_RX_BUFFER = 150;
	public static final int  MAX_LENGTH_RX_BUFFER = 200;
	private static final int MAXNODATACOUNTER     = 200;
	
	// Version constanst
	public static final int FWVERSION_SDCARD = 1200;
	public static final int FWVERSION_ANDROID_COMPATIBLE = 1225;

	public static boolean ENABLE_POLLTHREAD = true;
	
	/*! 
	 * \property Activity
	 * 
	 * Logger of the application
	 */
	private Activity _activity;
	private IScanDiscoveryFinishedHandler _scanDiscoveryFinishedHandler;
	private IenzoHandler _enzoHandler;
	
	
	/*
	 * ! \property Logger
	 * 
	 * Logger of the application
	 */
	private Logger logger;

	/*
	 * ! \property WinBluetoothAPIWrapper
	 */	
    private BluetoothManager _btManager;

	
	/*
	 * ! \enum StarStimRegisterFamily
	 * 
	 * Description of the different register families.
	 */
	public enum StarStimRegisterFamily {
		STIM_REGISTERS, EEG_REGISTERS, ACCELEROMETER_REGISTERS, SDCARD_REGISTERS
	};

	/*
	 * ! \enum StimulationMode
	 * 
	 * Enumeration of the different stimulation modes
	 */
	public enum StimulationMode {
		STIM_REGULAR, STIM_ONLINE
	};

	/*
	 * ! \enum StatusByteBits
	 * 
	 * Enumeration of the bit positions in the status byte.
	 */
	public enum StatusByteBits {
		// STM_BIT = 0x04,
		// EEG_BIT = 0x02,
		// IMP_BIT = 0x01,
		// SDCARD_BIT = 4
		STM_BIT(0x08), EEG_BIT(0x04), IMP_BIT(0x02), SDCARD_BIT(0x01);

		private int numVal;

		StatusByteBits(int numVal) {
			this.numVal = numVal;
		}

		public int getVal() {
			return numVal;
		}

	};

	/** DEBUG INFO **/
	public ArrayList<Integer> _EEGHistogram;
	public ArrayList<Integer> _EEGTimesValues;
	public ArrayList<Integer> _EEGTimesValuesMsecs;
	public ArrayList<Integer> _processDataTimesValuesMsecs;
	public ArrayList<Integer> _packetsHistogram;
	public int _maxPacketsInPacket;
	public int _minPacketsInPacket;
	public int _totalPacketsInPacket;
	public int _countPacketsInPacket;
	public int _countCurrentPacketsInPacket;
	public int _countPacketLost;
	/** DEBUG INFO **/

	public int _countPacketsLostPer30Seconds;

	public int _countPacketsPer30Seconds;

	/*
	 * ! \property DeviceManager::_device
	 * 
	 * Instance of the bluetooth rfcomm device to communicate with the
	 * enzo3G/StarStim device.
	 */
	private RFCOMMDevice _device;

	/*
	 * ! \property DeviceManager::_protocol
	 * 
	 * Parser of the enzo3G/StarStim protocol.
	 */
	public StarStimProtocol _protocol;

	/*
	 * ! \property DeviceManager::_rxBuffer
	 * 
	 * Buffer that stores the received bytes from rfcomm device.
	 */
	private ArrayList<Byte> _rxBuffer;

	/*
	 * ! \property DeviceManager::_isDevicePresent
	 * 
	 * Boolean that informs whether an enzo3G/StarStim is detected on the
	 * rfcomm device.
	 */
	private boolean _isDevicePresent;

	/*
	 * ! \property DeviceManager::_ackReceived
	 * 
	 * Boolean that informs if the last command sent has been processed and
	 * acknoledged by the device.
	 */
	private boolean _ackReceived;

	/*
	 * ! \property DeviceManager::_eegRegisters
	 * 
	 * Vector of StarStimRegister objects for the EEG configuration parameters.
	 */
	private StarStimRegister[] _eegRegisters;

	/*
	 * ! \property DeviceManager::_stimRegisters
	 * 
	 * Vector of StarStimRegister objects for the stimulation configuration
	 * parameters.
	 */
	private StarStimRegister[] _stimRegisters;

	/*
	 * ! \property DeviceManager::_accRegisters
	 * 
	 * Vector of StarStimRegister objects for the accelerometer configuration
	 * parameters.
	 */
	private StarStimRegister[] _accRegisters;

	/*
	 * ! \property DeviceManager::_threadResult
	 * 
	 * This is used to control the inner thread of the class. This thread is
	 * responsible for the maintenance of the communication with the device.
	 */
	private Thread _threadResult;
	
	/*
	 * ! \property DeviceManager::_terminatePollThread
	 * 
	 * This is a boolean to control the polling loop. When set to false the
	 * operation on the poolling loop is terminated.
	 */
	private boolean _terminatePollThread;

	/*
	 * ! \property DeviceManager::_terminatePollThread
	 * 
	 * This is a boolean to control the polling loop. When set to false the
	 * operation on the poolling loop is terminated.
	 */
	private boolean _isRunningPollThread;
	
	/*
	 * ! \property DeviceManager::_isPausedPollThread
	 * 
	 *  Informs whether Poll thread is paused
	 */	
	boolean _isPausedPollThread;
	
	/*
	 * ! \property DeviceManager::_pausePollThread
	 * 
	 *  Request thread to be paused
	 */	
	boolean _pausePollThread;
	
	/*
	 * ! \property DeviceManager::_deviceStatus
	 * 
	 * This keeps the reported status of the device.
	 */
	private int _deviceStatus;

	/*
	 * ! \property DeviceManager::_stimulationMode
	 * 
	 * It keeps the type of stimulation that is performed whenever a stimulation
	 * is requested.
	 */
	private StimulationMode _stimulationMode;

	/*
	 * ! \property DeviceManager::_isOnlineStimulationRunning
	 * 
	 * This is a boolean that informs if an online stimulation is going on.
	 */
	private boolean _isOnlineStimulationRunning;

	/*
	 * ! \property DeviceManager:_numOfChannels
	 * 
	 * This stores the number of channels of the connected device.
	 */
	private int _numOfChannels;

	/*
	 * ! \enum onlineStimulationStatus
	 * 
	 * Enumeration for the online stimulation state machine
	 */
	enum onlineStimulationStatus {
		ONLINESTIM_PRE_START_0, ONLINESTIM_PRE_START_1, ONLINESTIM_START, ONLINESTIM_WRITE_EVERY_60MS
	};

	/*
	 * ! \property DeviceManager::_onlineStimStatus
	 * 
	 * This property is used for the online stimulation state machine
	 */
	private onlineStimulationStatus _onlineStimStatus;

	/*
	 * ! \property DeviceManager::_beaconCounterBattery
	 * 
	 * Counter used for periodically send the battery measurement request.
	 */
	private int _beaconCounterBattery;

	/*
	 * ! \property DeviceManager::_beaconCounterOnlineStimulation
	 * 
	 * Counter used for periodically send the online stimulation signal.
	 */
	private int _beaconCounterOnlineStimulation;

	/*
	 * ! \property DeviceManager::_beaconCounterStayAlive
	 * 
	 * Counter used for periodically send the null request for staying alive the
	 * communication with the device.
	 */
	private int _beaconCounterStayAlive;

	/*
	 * ! \property DeviceManager::_sampleRateCorrector
	 * 
	 * Sampling rate corrector to correct the deviation the original sampling
	 * rate might have.
	 */
	private SampleRateCorrector _sampleRateCorrector;

	/*
	 * ! \property DeviceManager::_macAddress
	 * 
	 * Vector for storing the bluetooth mac addres of the device.
	 */
	private String _macAddress;

	/*
	 * ! \property DeviceManager::_waitingFirstEEGSample
	 * 
	 * This boolean informs whether an EEG sample has been received after
	 * requesting the start EEG streaming.
	 */
	private boolean _waitingFirstEEGSample;

	/*
	 * ! \property DeviceManager::_firstStimSampleReceived
	 * 
	 * This boolean informs whether a Stimulation sample has been received after
	 * requesting the start stimulating.
	 */
	private boolean _firstStimSampleReceived;

	/*
	 * ! \property DeviceManager::_firstEEGTimeStamp
	 * 
	 * This variable keeps the time stamp of the first EEG sample.
	 */
	private long _firstEEGTimeStamp;

	/*
	 * ! \property DeviceManager::_timeRequestFirstSample
	 * 
	 * This variable is used for knowing when the start streaming was requested
	 * so it can be known the intial latency.
	 */
	private long _timeRequestFirstEEGSample;

	/*
	 * ! \property DeviceManager::_timeRequestFirstStimSample
	 * 
	 * This variable is used for knowing when the start stimulation was
	 * requested so it can be known the intial latency.
	 */
	private long _timeRequestFirstStimSample;

	/*
	 * ! \property DeviceManager::_currentTimestamp
	 * 
	 * It keeps the currentTimestamp to be set to the received EEG samples.
	 */
	private long _currentTimestamp;

	/*
	 * ! \property DeviceManager::_currentStimTimestamp
	 * 
	 * It keeps the currentTimestamp to be set to the received Stimulation
	 * samples.
	 */
	private long _currentStimTimestamp;

	/*
	 * ! \property DeviceManager::_currentEEGStamp
	 * 
	 * It keeps the current EEG stamp which is used to identify lost packets in
	 * the EEG streaming.
	 */
	private int _currentEEGStamp;

	/*
	 * ! \property DeviceManager::_lastEEGData
	 * 
	 * It keeps the last processed EEG sample.
	 */
	private ChannelData _lastEEGData;

	/*
	 * ! \property DeviceManager::_lastEEGDataReceived
	 * 
	 * It keeps the QTime of the last processed EEG sample.
	 */
	// QTime _lastEEGDataReceived;

	/*
	 * ! \property DeviceManager::_lastAccelerometerData
	 * 
	 * It keeps the last processed Accelerometer sample.
	 */
	private ChannelData _lastAccelerometerData;

	/*
	 * ! \property DeviceManager::_lastStimData
	 * 
	 * It keeps the last processed Stimulation data.
	 */
	private ChannelData _lastStimData;

	/*
	 * ! \property DeviceManager::_isOpen
	 * 
	 * this boolean tells if the device is open or not.
	 */
	private boolean _isOpen;

	/*
	 * ! \property DeviceManager::_isStimulationRunning
	 * 
	 * This is a boolean that informs if an stimulation is going on.
	 */
	private boolean _isStimulationRunning;

	/*
	 * ! \property DeviceManager::_isStreaming
	 * 
	 * This is a boolean that informs if the device is streaming EEG.
	 */
	private boolean _isStreaming;

	/*
	 * ! \property DeviceManager::_isStreamingAccelerometer
	 * 
	 * This is a boolean that informs if the device is streaming Accelerometer.
	 */
	private boolean _isStreamingAccelerometer;

	/*
	 * ! \property This boolean tells if the application is working on demo
	 * mode.
	 */
	private boolean _isDemoMode;

	/*
	 * ! \property DeviceManager::_firmwareVersion
	 * 
	 * Integer that informs of the firmware version (if available)
	 */
	private int _firmwareVersion;

	/*
	 * ! \property DeviceManager::_is1000SPS
	 * 
	 * Integer whether the device is 1000SPS
	 */
	private int _is1000SPS;

	/*
	 * ! A boolean to determine if is a stimulation device or not
	 */
	private boolean _isaStimulationDevice;

	/*
	 * ! Indicates the number of EEG samples per beacon received
	 */
	private int _samplesPerBeacon;

	private boolean _lookForStarStimFailed;

	private DrifftLocalClockCalculator _drifftClock;

	private boolean _isSampleRateEEG;

	/**
	 * OpenErrorTypes represent the different values for the error returning from the openDeviceOperation
	 */
	public enum OpenErrorTypes {
		ERROR_NO_ERROR(0), ERROR_PAIRING_ON_PROGRESS(1), ERROR_FAIL(2);

		private int numVal;

		OpenErrorTypes(int value) {
			this.numVal = value;
		}

		public int getVal() {
			return numVal;
		}

	};
	
	// -- METHODS --
	// --------------

	/*
	 * ! Public Constructor
	 */
	public DeviceManager( Activity activity, IScanDiscoveryFinishedHandler scanDiscoveryFinishedHandler, IenzoHandler enzoHandler ) {
		logger = Logger.getInstance();

		// Initialised in the begginning

		_stimulationMode = StimulationMode.STIM_REGULAR;
		_isOnlineStimulationRunning = false;
		_onlineStimStatus = onlineStimulationStatus.ONLINESTIM_PRE_START_0;
		_beaconCounterBattery = 10000;
		_beaconCounterOnlineStimulation = 0;
		_beaconCounterStayAlive = 0;
		_sampleRateCorrector = new SampleRateCorrector(500.0);
		_waitingFirstEEGSample = true;
		_firstStimSampleReceived = false;
		_currentStimTimestamp = 0;
		_isOpen = false;
		_isStimulationRunning = false;
		_isStreaming = false;
		_isStreamingAccelerometer = false;
		_isDemoMode = false;
		_firmwareVersion = 0;
		_is1000SPS = 0;
		_samplesPerBeacon = 1;
		_isaStimulationDevice = false;
		_lookForStarStimFailed = false;
		_isSampleRateEEG = false;


		// Initialise thread variables
		_isPausedPollThread = false;
		_pausePollThread = false;		
		_isRunningPollThread = false;
		_terminatePollThread = false; 
		// ------------------------------

		_device = new RFCOMMDevice(activity, scanDiscoveryFinishedHandler);
		_protocol = new StarStimProtocol( StarStimProtocol.EEGCompressionType.EEG_NO_COMPRESSION );
		_btManager = BluetoothManager.getInstance(activity, scanDiscoveryFinishedHandler);		
		_enzoHandler = enzoHandler;
		_activity      = activity;
		_scanDiscoveryFinishedHandler = scanDiscoveryFinishedHandler;

		// Initialise ArrayLists
		_rxBuffer = new ArrayList<Byte>();
		_eegRegisters  = new StarStimRegister[DeviceManager.EEG_NUM_REGS];
		for(int i = 0; i < DeviceManager.EEG_NUM_REGS; i ++) _eegRegisters[i] = new StarStimRegister();
		_stimRegisters = new StarStimRegister[DeviceManager.STM_NUM_REGS];
		for(int i = 0; i < DeviceManager.STM_NUM_REGS; i ++) _stimRegisters[i] = new StarStimRegister();
		_accRegisters  = new StarStimRegister[DeviceManager.ACCEL_NUM_REGS];
		for(int i = 0; i < DeviceManager.ACCEL_NUM_REGS; i ++) _accRegisters[i] = new StarStimRegister();

		// _protocol.reset();
		_isDevicePresent = false;
		_deviceStatus = 255; // unknown value
		_macAddress = "";


		_maxPacketsInPacket = 0;
		_minPacketsInPacket = 10000;
		_totalPacketsInPacket = 0;
		_countPacketsInPacket = 0;

		_drifftClock = new DrifftLocalClockCalculator();

		

		// Declare the thread
		_threadResult = new Thread( this );
		
		
	}

	/*
	 * ! It performs the required operations to open and initialize the
	 * hardware.
	 * 
	 * \param macAddress Pointer to a vector with the six bytes of the Bluetooth
	 * mac address. macAddress[0] is the byte on the right from the mac address
	 * label on the device.
	 * 
	 * \param forcePairing if true the method will try to pair the device
	 * although it is not visible by the time the method is called If false the
	 * pairing is performed only when the device has been earlier detected on
	 * the neighbourhood.
	 * 
	 * \return If the device opens correctly the functionn returns true, false
	 * otherwise. This method returning false might be because either the device
	 * is turned off or it is out of battery or it is out of range.
	 */	
	public OpenErrorTypes openDevice(String macAddress, boolean forcePairing) {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

//		if (_isDemoMode) {
//			return true;
//		}

		logger.info("Opening device ... " + macAddress , Logger.LOG_FILE_ON);
		
		// We want to avoid pairing a device that is not visible
		if ( !_isDevicePaired(macAddress)
				&& (forcePairing || _isDeviceNotPaired(macAddress))) {
			logger.info("DeviceManager::openDevice --> We do pair device", Logger.LOG_FILE_ON);
			int result = _btManager.pairDevice(macAddress, null);
			logger.info("DeviceManager::openDevice pair device result --> "+ result, Logger.LOG_FILE_ON);
			
			return OpenErrorTypes.ERROR_PAIRING_ON_PROGRESS;
		}

		if (_isDevicePaired(macAddress) || _isDeviceRemembered(macAddress)) {
			logger.info("_device.open started " + sdf.format(cal.getTime()), Logger.LOG_FILE_ON);
			
			RFCOMMDevice.errType errType = RFCOMMDevice.errType.ERR_DEVICE_NOT_OPENED;				
			try {
				errType = _device.open(macAddress);
			} catch (IOException e) {
				e.printStackTrace();			
				logger.info( Logger.stack2string(e) , Logger.LOG_FILE_ON);
			}
			
			// If no error
			if ( errType ==  RFCOMMDevice.errType.ERR_NO_ERROR) {
				logger.info("_device.open finished " + sdf.format(cal.getTime()), Logger.LOG_FILE_ON);

				 _protocol.reset();
				_drifftClock.reset(1000.0F);		
				if ( _lookForStarStim() ){


					_macAddress = macAddress;
					_isOpen = true;

					if( ENABLE_POLLTHREAD ) _startPollThread();
					logger.info("We do the _device.open-->is open (doFirmwareVersionBatteryRequest)", Logger.LOG_FILE_ON);
					doFirmwareVersionBatteryRequest();
					logger.info("We do doFirmwareVersionBatteryRequest", Logger.LOG_FILE_ON);
					// _doBatteryMeasurement();
					// qDebug()<<"We do a Battery request";
					return OpenErrorTypes.ERROR_NO_ERROR;
				} else {
					logger.info("ERROR _lookForStarStim() failed", Logger.LOG_FILE_ON);
					try {
						_device.close();
					} catch (IOException e) {
						e.printStackTrace();			
						logger.info( Logger.stack2string(e) , Logger.LOG_FILE_ON);
					}
				}
				
			
				
				
				
			}
			logger.info("_device.open finished " + sdf.format(cal.getTime()), Logger.LOG_FILE_ON);
		}

		return OpenErrorTypes.ERROR_FAIL;

	}


	/*
	 * ! It performs the required operations to close the hardware.
	 * 
	 * \param isSDCardRecording whether the device is recording to the SD card
	 * and streaming should not be stopped
	 * 
	 * \return True if the device is closed correctly, false otherwise.
	 */
	public boolean closeDevice(boolean isSDCardRecording) {

		_macAddress = "";
		
		logger.info("Closing device ...", Logger.LOG_FILE_ON);
		
		if (isSDCardRecording) // I don't want to stop anything else
			return true;
		
		RFCOMMDevice.errType errType = RFCOMMDevice.errType.ERR_DEVICE_NOT_CONNECTED;
		try {
	
			 if (_isOpen)
			 {
				stopStreaming( true, true );
				// stopStimulation();
				_isOpen = false;
				
				if( ENABLE_POLLTHREAD ){
					logger.info("Stopping _poll thread", Logger.LOG_FILE_ON);
					_stopPollThread();					
				}
				ArrayList<Byte> command = StarStimProtocol.buildStopBeaconRequest();
				if( _device.write(command, command.size() ) < 0 ){
					return false;
				}
				
	
				 ArrayList<Byte> txBuffer = StarStimProtocol.buildStopBeaconRequest();			 
			     if (_device.write(txBuffer, txBuffer.size()) < 0)
			     {
			    	 return false;
				 }

			 }

			 // Close device
		     logger.info("Calling to close BT socket ", Logger.LOG_FILE_ON);
			 errType = _device.close();
		} catch (IOException e) {
			e.printStackTrace();					
			logger.info( Logger.stack2string(e) , Logger.LOG_FILE_ON);
			return false;
		}
		
		return (errType == RFCOMMDevice.errType.ERR_NO_ERROR);
	}

	/*
	 * ! It performs the required operations to remove the hardware.
	 * 
	 * \param macAddress Pointer to a vector with the six bytes of the Bluetooth
	 * mac address. macAddress[0] is the byte on the right from the mac address
	 * label on the device.
	 * 
	 * \return It return a positive value if the unpairing is successful. It
	 * returns a negative value when the device could not be removed from the
	 * system.
	 */
	public boolean removePairedDevice(String macAddress) {
		logger.info("DeviceManager::removePairedDevice" + macAddress, Logger.LOG_FILE_ON);

		if (_isDevicePaired(macAddress) || _isDeviceRemembered(macAddress)) {
			int result = _btManager.removeDevice(macAddress);
			if (result == 1)
				return true;
		}

		return false;
	}

	/*
	 * ! It performs the renameDevice operation
	 * 
	 * \param name targetted name for the radio
	 * 
	 * \return True if the device is closed correctly, false otherwise.
	 */
	public boolean renameDevice( String name ){
		ArrayList<Byte> txBuffer  = StarStimProtocol.buildRenameRadioRequest( name );
	    // Stops the polling thread
	    _stopPollThread();
	    try{
		    if (_device.write(txBuffer, txBuffer.size()) < 0) return false;	    
		} catch (IOException e) {
			e.printStackTrace();					
			logger.info( Logger.stack2string(e) , Logger.LOG_FILE_ON);
			return false;
		}
	    
	    return true;
	    	    
	}

	/*
	 * ! Closes the RFComm socket. It is used when renaming device only. \return
	 * True if the device is closed correctly, false otherwise.
	 */
	// bool closeDeviceWithoutPollStop();

	/*
	 * ! It sends a request to the enzo3G/StarStim to start the EEG streaming.
	 * 
	 * \return True if the request has been correctly received by the device,
	 * false otherwise.
	 */
	public boolean startStreaming (){
	    if (_isDemoMode)
	    {
	        //return _demoModeStartStreaming();
	    }

	    ArrayList<Byte> txBuffer = StarStimProtocol.buildStartEEGFrame();	    

	    _waitingFirstEEGSample = true;
	    _timeRequestFirstEEGSample = System.currentTimeMillis();
	    
	    if( ENABLE_POLLTHREAD ){
		    //logger.info("_pausePollThread call", Logger.LOG_FILE_ON);
		    _pausePollThread();
	    }
	    boolean result = _sendRequest(txBuffer);
	    if( ENABLE_POLLTHREAD ){
		    //logger.info("_resumePollThread call", Logger.LOG_FILE_ON);
		    _resumePollThread();	    	
	    }
	    return result;
	    /* THIS WAS THE PREVIOUS IMPLEMENATATION
		_stopPollThread();	     
	    if (!_sendRequest(txBuffer))
	    {	
	        _startPollThread();
	        return false;
	    }
	    _startPollThread();	    
	    return true;
	    /**/
	}

	/*
	 * ! It sends a request to the enzo3G/StarStim to stop the EEG streaming.
	 * 
	 * \param doStopAccelerometer In case that the Accelerometer is on,
	 * indicates if it should be stopped or not (useful for SDCard recording)
	 * 
	 * \return True if the request has been correctly received by the device,
	 * false otherwise.
	 */
	/*
	 * ! It sends a request to the enzo3G/StarStim to stop the EEG streaming.
	 * 
	 * \param doStopAccelerometer In case that the Accelerometer is on,
	 * indicates if it should be stopped or not (useful for SDCard recording)
	 * 
	 * \return True if the request has been correctly received by the device,
	 * false otherwise.
	 */
	public boolean stopStreaming ( boolean doStopAccelerometer ){
		return this.stopStreaming(doStopAccelerometer, false);
	}
	
	// This method is only used in closing
	public boolean stopStreaming (boolean doStopAccelerometer, boolean isClosing){
//	    if (_isDemoMode)
//	    {
////	        return _demoModeStopStreaming();
//	    }

		if( !isDeviceStreaming() ){
			logger.info("Device is not Streaming", Logger.LOG_FILE_ON);
		    if( ENABLE_POLLTHREAD ) _stopPollThread();
			return false;
		}
		
	    if (getFirmwareVersion() >=593)
	    {
	        if ((isDeviceStreamingAccelerometer())&&(doStopAccelerometer))
	        {
	            stopAccelerometer();
	        }
	    }

	    // Create Frame to stopEEG
	    logger.info("Sending Stop Polling EEG ...", Logger.LOG_FILE_ON);
	    ArrayList<Byte> txBuffer = StarStimProtocol.buildStopEEGFrame();

	    boolean result = false;
	    if( !isClosing ){
	    	if( ENABLE_POLLTHREAD ) _pausePollThread();
		    _isOnlineStimulationRunning = false;	    
		    result = _sendRequest(txBuffer);
		    if( ENABLE_POLLTHREAD ) _resumePollThread();	    	
	    }else{
	    	if( ENABLE_POLLTHREAD ) _stopPollThread();
		    _isOnlineStimulationRunning = false;	    
		    result = _sendRequest(txBuffer);
	    }
	    return result;    
	}

	/*
	 * ! It sends a request to the StarStim to start the stimulation.
	 * 
	 * \return True if the request has been correctly received by the device,
	 * false otherwise.
	 */
	// bool startStimulation ();

	/*
	 * ! It sends a request to the StarStim to stop the stimulation.
	 * 
	 * \return True if the request has been correctly received by the device,
	 * false otherwise.
	 */
	// bool stopStimulation ();

	/*
	 * ! It sends a request to the device to start streaming accelerometer data.
	 * 
	 * \return True if the request has been correctly received by the device,
	 * false otherwise.
	 */
	public boolean startAccelerometer(){

	    Byte[] REG_ACC = {0x01};
	    boolean result;
	    logger.info( "startAccelerometer()", Logger.LOG_FILE_ON );
	    if (!_isStreamingAccelerometer)
	    {
	        result = writeRegister(DeviceManager.StarStimRegisterFamily.ACCELEROMETER_REGISTERS, (byte) 0, REG_ACC, (byte) 1);
	        logger.info ( "startAccelerometer() done" + result, Logger.LOG_FILE_ON);
	    }
	    else
	        logger.info( "startAccelerometer() not done. It was already streamming accelerometer", Logger.LOG_FILE_ON );

	    _isStreamingAccelerometer=true;

	    return true;
	}

	/*
	 * ! It sends a request to the device to stop streaming accelerometer data..
	 * 
	 * \return True if the request has been correctly received by the device,
	 * false otherwise.
	 */
	public boolean stopAccelerometer(){

	    
	    Byte[] REG_ACC = {0x00};
	    Byte[] REG_ACCRead = new Byte[1];
	    
	    boolean result;
	    logger.info ( "stopAccelerometer()", Logger.LOG_FILE_ON );
	    result = writeRegister(DeviceManager.StarStimRegisterFamily.ACCELEROMETER_REGISTERS, (byte) 0, REG_ACC, (byte) 1);
	    logger.info ("stopAccelerometer() write result " + result, Logger.LOG_FILE_ON);
//	    result=false;
//	    int count=0;
//	    while (!result)
//	    {
//	        count++;
//	        logger.info( "We tried DeviceManager::stopAccelerometer readRegister " + count + " times", Logger.LOG_FILE_ON);
//	        result = readRegister(DeviceManager::ACCELEROMETER_REGISTERS, 0,&REG_ACCRead, 1);
//	    }

//	    logger.info( "stopAccelerometer() read result " + result + REG_ACCRead[0], Logger.LOG_FILE_ON);

	    _isStreamingAccelerometer=false;

	    return true;
	}

	/*
	 * ! It sends a request to the StarStim to start sending the impedance
	 * measurements.
	 * 
	 * \return True if the request has been correctly received by the device,
	 * false otherwise.
	 */
	// bool startImpedanceMeasurement ();

	/*
	 * ! It sends a request to the StarStim to stop sending the impedance
	 * measurements.
	 * 
	 * \return True if the request has been correctly received by the device,
	 * false otherwise.
	 */
	// bool stopImpedanceMeasurement ();

	/*
	 * ! It reads a configuration register.
	 * 
	 * \param family Identification of the family of registers to be read.
	 * 
	 * \param address Address of the register within the family.
	 * 
	 * \param reg Read value. If the fucntion returns false this value is not.
	 * 
	 * \param length Number of consecutive register to be read
	 * 
	 * \return True if the register has been read, false otherwise.
	 */
	 public boolean readRegister (StarStimRegisterFamily family, Byte address, Byte[] reg, Byte length){
	    if (_isDemoMode)
	        return false;

	    ArrayList<Byte> txBuffer;
	    StarStimRegister[] ptrRegister;
	    switch (family)
	    {
	    case STIM_REGISTERS:
	        txBuffer = StarStimProtocol.buildReadStimRegisterFrame(address, length);
	        ptrRegister = _stimRegisters;
	        break;
	    case EEG_REGISTERS:
	        txBuffer = StarStimProtocol.buildReadEEGRegisterFrame(address, length);
	        ptrRegister = _eegRegisters;
	        break;
	    case ACCELEROMETER_REGISTERS:
	        txBuffer = StarStimProtocol.buildReadAccRegisterFrame(address, length);
	        ptrRegister= _accRegisters;
	        break;
	    default:		
	    	logger.info( "invalid address", Logger.LOG_FILE_ON );
        return false;
    }

///////////////////////////////////
//String str = "";
//for( Byte b: txBuffer){
//str += b + " ";
//}
//logger.info("Value: " +  str, Logger.LOG_FILE_ON);
///////////////////////////////////	 
		    
		    if (txBuffer.size() <= 0)
		    {		
		        logger.info ( "error building request", Logger.LOG_FILE_ON );
		        return false;
		    }
		    
		    if( ENABLE_POLLTHREAD ){
			    //logger.info("Pausing polling thread ...", Logger.LOG_FILE_ON);
			    _pausePollThread();		    	
		    }
		    
		    if (!_sendRequest(txBuffer))
		    {
		    	if( ENABLE_POLLTHREAD ) _resumePollThread();
		        logger.info( "error sending request", Logger.LOG_FILE_ON );
		        return false;
		    }
		    // wait for response
		    int retries = 0;
		    while (!ptrRegister[address].updated() && (retries++ < 30))
		    {
		        //logger.info("DeviceManager::readRegister " + retries, Logger.LOG_FILE_ON  );
		        if (_processData(MAX_LENGTH_RX_BUFFER) < 0)
		        {	
		        	if( ENABLE_POLLTHREAD ) _resumePollThread();
		            return false;
		        }
		    }
		    if( ENABLE_POLLTHREAD ) _resumePollThread();
		    
/*
		    if (!_sendRequest(txBuffer))
		    {
		    	_resumePollThread();
		        logger.info( "error sending request", Logger.LOG_FILE_ON );
		        return false;
		    }
		    // wait for response
		    int retries = 0;
		    while (!ptrRegister[address].updated() && (retries++ < 30))
		    {
		        //logger.info("DeviceManager::readRegister " + retries, Logger.LOG_FILE_ON  );
		        if (_processData(MAX_LENGTH_RX_BUFFER) < 0)
		        {	
		            _resumePollThread();
		            return false;
		        }
		    }
		    _resumePollThread();
/**/		    
		    
		    
		    if (!ptrRegister[address].updated())
		    {
		    	logger.info("DeviceManager::readRegister failed because the acknowledgment was not received " + retries
		    					, Logger.LOG_FILE_ON);
		        return false;
		    }
		    for (int i=0;i<length;i++)
		    {
		        reg[i] = (byte) (ptrRegister[address+i].value() & 0xFF);
		    }

		    return true;
	 }

	/*
	 * ! It writes a configuration register.
	 * 
	 * \param family Identification of the family of registers to be writen.
	 * 
	 * \param address Address of the register within the family.
	 * 
	 * \param value Value to be written.
	 * 
	 * \return True if the register has been written, false otherwise.
	 */
	public boolean writeRegister (StarStimRegisterFamily family, byte address, byte value){
		Byte[] aux = new Byte[1];
		aux[0] = value;
	    return writeRegister(family, address, aux, (byte) 1);
	}

	/*
	 * ! It writes a set of configuration register with consecutive addresses.
	 * 
	 * \param family Identification of the family of registers to be writen.
	 * 
	 * \param address Address of the register within the family.
	 * 
	 * \param value Vector with the values to be written.
	 * 
	 * \param length Length of the value vector.
	 * 
	 * \return True if the register has been written, false otherwise.
	 */
	public boolean writeRegister (StarStimRegisterFamily family, byte address, Byte[] value, byte length){
	    if (_isDemoMode)
	    {
	        return false;
	    }

	    ArrayList<Byte> txBuffer;
	    switch (family)
	    {
	    case STIM_REGISTERS:
	        txBuffer = StarStimProtocol.buildWriteStimRegisterFrame(address, value, length);
	        break;
	    case EEG_REGISTERS:
	        txBuffer = StarStimProtocol.buildWriteEEGRegisterFrame( address, value, length);
	        break;
	    case ACCELEROMETER_REGISTERS:
	        txBuffer = StarStimProtocol.buildWriteAccRegisterFrame(address, value, length);
	        break;
	    case SDCARD_REGISTERS:
	        txBuffer = StarStimProtocol.buildWriteSDCardRegisterFrame(address, value, length);
	        break;
	    default:
	        logger.info( "invalid address", Logger.LOG_FILE_ON);
	        return false;
	    }
	    
	   	    
	    if (txBuffer.size() <= 0){
	        return false;
	    }

///////////////////////////////////
//String str = "";
//for( Byte b: txBuffer){
//str += b + " ";
//}
//logger.info("Value: " +  str, Logger.LOG_FILE_ON);
///////////////////////////////////	     

	    
	    if( ENABLE_POLLTHREAD ){
		    //logger.info("_pausePollThread call", Logger.LOG_FILE_ON);
		    _pausePollThread();
	    }
	    boolean result = _sendRequest(txBuffer);
	    if( ENABLE_POLLTHREAD ){
		    //logger.info("_resumePollThread call", Logger.LOG_FILE_ON);
		    _resumePollThread();	    	
	    }
	    return result;    
	}

	/*
	 * ! This method sets the stimulation mode. Depending on the selected mode
	 * the signal that is set in the stimulation electrodes is built by the
	 * device or it is sent online by the API. In that case the signal shall be
	 * provided in advance through the DeviceManager::setStimulationSignal
	 * method.
	 * 
	 * \param mode Stimulation mode
	 */
	// void setStimulationMode (StimulationMode mode);

	/*
	 * ! This method provides the file that has the signals that will be sent to
	 * the device when the stimulation mode is configured to be online.
	 * 
	 * \param fileName Name of the file that contents the signals that will be
	 * sent to the device in order to perform the online stimulation. The file
	 * shall have tab-separated values where the columns have the values for
	 * every channel. The channels that are not sent shall have the asterisk
	 * ('*') character instead of the sample value.
	 */
	// void setStimulationSignal (QString fileName);

	/*
	 * ! This method sets the number of recording channels that the device has.
	 * 
	 * \param channels Integer that indicates the number of channels.
	 */
	public void setNumOfChannels(int value){
		_numOfChannels = value;
	}

	/*
	 * ! This method gets the number of recording channels that the device has.
	 * 
	 * \param Number of channels.
	 */
	public int getNumOfChannels(){
		return _numOfChannels;
	}

	/*
	 * ! This method provides the Bluetooth mac address of the device.
	 * 
	 * \return Pointer to the vector that contains the Bluetooth mac address of
	 * the device. macAddress[0] is the byte on the right from the mac address
	 * label on the device.
	 */
	public String getMacAddress (){
		return _macAddress;
	}

	/*
	 * ! This static method performs a scan of the neigborhood for on-line
	 * enzo2G/StarStim devices. The result of the scannig can be obtained from
	 * the DeviceManager::getNumberOfNotPairedDevices() and
	 * DeviceManager::getPairedDeviceInfo() methods.
	 * 
	 * \return True if the scanning has been performed successfully, false
	 * otherwise
	 */
	public static boolean scanBT(Activity activity, IScanDiscoveryFinishedHandler _scanDiscoveryFinished) {
		BluetoothManager _wrapper = BluetoothManager.getInstance(activity, _scanDiscoveryFinished);
		Logger logger = Logger.getInstance();

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		int result = 0;
		logger.info("DeviceManager::scanBT starts " + sdf.format(cal.getTime()), Logger.LOG_FILE_ON);
		result = _wrapper.scanNeighborhood();
		
/////////////////////////////////////////////////////////
//		SystemClock.sleep(1000);
//		while( (_wrapper.isScanOngoing() == true) );
/////////////////////////////////////////////////////////
		
		logger.info("DeviceManager::scanBT finishes " + sdf.format(cal.getTime()), Logger.LOG_FILE_ON);
		return (result > 0);
	}

	/*
	 * ! This static method provides the number of enzo3G/StarStim devices
	 * that are already paired with local Bluetooth stack.
	 * 
	 * \return Number of already paired devices on the system.
	 */
	public static int getNumberOfPairedDevices(Activity activity, IScanDiscoveryFinishedHandler scanDiscoveryFinished) {
		BluetoothManager _wrapper = BluetoothManager.getInstance(activity, scanDiscoveryFinished);


		int ret = 0;

		int nPairedDevices = _wrapper.getNumPairedDevices();
		for (int i = 0; i < nPairedDevices; i++) {
			Reference<String> deviceName = new Reference<String>("");
			Reference<String> macAddress = new Reference<String>("");
			if (_wrapper.getInfoPairedDevice(i, deviceName, macAddress) > 0) {
				if (deviceName.get().contains(DEVICE_NAME)) {
					ret++;
				}

			}

		}

		return ret;
	}

	/*
	 * ! This static method provides the number of enzo3G/StarStim devices
	 * that are remembered by local Bluetooth stack.
	 * 
	 * \return Number of already paired devices on the system.
	 */
	public static int getNumberOfRememberedDevices(Activity activity, IScanDiscoveryFinishedHandler scanDiscoveryFinished) {
		BluetoothManager _wrapper = BluetoothManager.getInstance(activity, scanDiscoveryFinished);
		Logger logger = Logger.getInstance();


		int ret = 0;

		int authenticated = 0;
		int remembered = 1;
		int unknown = 0;
		int connected = 0;

		int nRemDevices = _wrapper.getNumAllDevices(authenticated, remembered,
				unknown, connected);
		Reference<String> deviceName = new Reference<String>("");
		Reference<String> deviceMAC = new Reference<String>("");
		for (int i = 0; i < nRemDevices; i++) {
			if (_wrapper.getInfoAllDevices(i, deviceName, deviceMAC,
					authenticated, remembered, unknown, connected) > 0) {
				// qDebug()<<"DeviceManager::getNumberOfRememberedDevices"<<
				// QString::number(i) + QString(" ")+
				// QString::number(deviceMAC[5],16) + QString(":")+
				// QString::number(deviceMAC[4],16) + QString(":")+
				// QString::number(deviceMAC[3],16) + QString(":")+
				// QString::number(deviceMAC[2],16) + QString(":")+
				// QString::number(deviceMAC[1],16) + QString(":")+
				// QString::number(deviceMAC[0],16);

				if (deviceName.get().contains(DEVICE_NAME)) {
					ret++;
				}

			}
		}
		return ret;
	}

	public static int getNumberOfAllDevices(boolean authenticated, boolean remembered,
			boolean unknown, boolean connected, Activity activity, IScanDiscoveryFinishedHandler scanDiscoveryFinished) {

		BluetoothManager _wrapper = BluetoothManager.getInstance(activity, scanDiscoveryFinished);
		Logger logger = Logger.getInstance();


		int ret = 0;
		int authenticatedInt = (authenticated) ? 1 : 0;
		int rememberedInt = (remembered) ? 1 : 0;
		int unknownInt = (unknown) ? 1 : 0;
		int connectedInt = (connected) ? 1 : 0;
		int nAllDevices = _wrapper.getNumAllDevices(authenticatedInt,
				rememberedInt, unknownInt, connectedInt);

		// qDebug()<<"DeviceManager::getNumberOfAllDevices"<<authenticated <<
		// remembered << unknown << connected;
		Reference<String> deviceName = new Reference<String>("");
		Reference<String> deviceMAC = new Reference<String>("");
		for (int i = 0; i < nAllDevices; i++) {
			if (_wrapper.getInfoAllDevices(i, deviceName, deviceMAC,
					authenticatedInt, rememberedInt, unknownInt, connectedInt) > 0) {
				// qDebug()<<"\t\tdevice found "<< QString::number(i) <<
				// QString::number(deviceMAC[5],16) + QString(":")+
				// QString::number(deviceMAC[4],16) + QString(":")+
				// QString::number(deviceMAC[3],16) + QString(":")+
				// QString::number(deviceMAC[2],16) + QString(":")+
				// QString::number(deviceMAC[1],16) + QString(":")+
				// QString::number(deviceMAC[0],16);

				if (deviceName.get().contains(DEVICE_NAME)) {
					ret++;
				}
			}
		}
		return ret;

	}

	public static int getAnyDeviceInfo(int index, Reference<String> deviceName,
			Reference<String> macAddress, boolean authenticated,
			boolean remembered, boolean unknown, boolean connected,
			boolean onlyNEDevices, Activity activity, IScanDiscoveryFinishedHandler scanDiscoveryFinished) {

		BluetoothManager _wrapper = BluetoothManager.getInstance(activity, scanDiscoveryFinished);
		Logger logger = Logger.getInstance();

		int ret = 0;
		int internalIndex = 0;
		int i = 0;
		int authenticatedInt = (authenticated) ? 1 : 0;
		int rememberedInt = (remembered) ? 1 : 0;
		int unknownInt = (unknown) ? 1 : 0;
		int connectedInt = (connected) ? 1 : 0;

		while ((ret = _wrapper.getInfoAllDevices(i++, deviceName, macAddress,
				authenticatedInt, rememberedInt, unknownInt, connectedInt)) > 0) {
			if (onlyNEDevices) {

				if (deviceName.get().contains(DEVICE_NAME)) {
					if (internalIndex++ == index)
						return 1;
				}
			} else {
				if (internalIndex++ == index)
					return 1;
			}
		}
		return ret;

	}

	/*
	 * ! This static method provided the number of not paired enzo3G/StarStim
	 * devices that have been detected in an early call to the
	 * DeviceManager::scanBT() method.
	 * 
	 * \return Number of not paired devices that were detected in the
	 * neigborhood.
	 */
	public static int getNumberOfNotPairedDevices(Activity activity, IScanDiscoveryFinishedHandler _scanDiscoveryFinished) {
		BluetoothManager _wrapper = BluetoothManager.getInstance(activity, _scanDiscoveryFinished);
		Logger logger = Logger.getInstance();

		int ret = 0;

		int nNotPairedDevices = _wrapper.getNumNotPairedDevices();
		for (int i = 0; i < nNotPairedDevices; i++) {
			Reference<String> deviceName = new Reference<String>("");
			Reference<String> macAddress = new Reference<String>("");

			if (_wrapper.getInfoNotPairedDevice(i, deviceName, macAddress) > 0) {
				if (deviceName.get().contains(DEVICE_NAME)) {
					ret++;
				}

			}

		}

		return ret;

	}

	/*
	 * ! This static method provides the information of an already paired
	 * device.
	 * 
	 * \param index Zero-based index of the device to obtain its details.
	 * 
	 * \param deviceName Pointer to memory where the mehtod will set the
	 * device's name if the index provided corresponds to a valid device. This
	 * parameter might be null.
	 * 
	 * \param macAddress Pointer to the vector where the method will set the
	 * Bluetooth mac address of the device if the index provided corresponds to
	 * a valid device. macAddress[0] is the byte on the right from the mac
	 * address label on the device. This parameter might be null.
	 * 
	 * \return It returns 1 if the deviceName and macAddress parameters are
	 * correctly set. It returns 0 when the index provided does not correspond
	 * to any already paired device. So there are less paired devices than the
	 * provided index. It returns a negative value when it is not possible to
	 * communicate with the Bluetooth stack to retrieve the device information.
	 */
	public static int getPairedDeviceInfo(int index,
			Reference<String> deviceName, Reference<String> macAddress,
			Activity activity, IScanDiscoveryFinishedHandler _scanDiscoveryFinished) {

		BluetoothManager _wrapper = BluetoothManager.getInstance(activity, _scanDiscoveryFinished);
		Logger logger = Logger.getInstance();

		int ret = 0;
		int internalIndex = 0;
		int i = 0;

		while ((ret = _wrapper.getInfoPairedDevice(i++, deviceName, macAddress)) > 0) {
			String strDevicename = deviceName.get();
			if (strDevicename.contains(DEVICE_NAME)) {
				if (internalIndex++ == index) {
					return 1;
				}
			}

		}

		logger.info("DeviceManager::getPairedDeviceInfo exists with value 0", Logger.LOG_FILE_ON);
		return ret;

	}

	/*
	 * ! This static method provides the information of a device that has not
	 * been paired yet.
	 * 
	 * \param index Zero-based index of the device to obtain its details.
	 * 
	 * \param deviceName Pointer to memory where the mehtod will set the
	 * device's name if the index provided corresponds to a valid device. This
	 * parameter might be null.
	 * 
	 * \param macAddress Pointer to the vector where the method will set the
	 * Bluetooth mac address of the device if the index provided corresponds to
	 * a valid device. macAddress[0] is the byte on the right from the mac
	 * address label on the device. This parameter might be null.
	 * 
	 * \return It returns 1 if the deviceName and macAddress parameters are
	 * correctly set. It returns 0 when the index provided does not correspond
	 * to a device that has not been paired yet. So there are less not paired
	 * devices than the provided index. It returns a negative value when it is
	 * not possible to communicate with the Bluetooth stack to retrieve the
	 * device information.
	 */
	public static int getNotPairedDeviceInfo(int index,
			Reference<String> deviceName, Reference<String> macAddress,
			Activity activity, IScanDiscoveryFinishedHandler _scanDiscoveryFinished) {
		
		BluetoothManager _wrapper = BluetoothManager.getInstance(activity, _scanDiscoveryFinished);
		Logger logger = Logger.getInstance();

		int ret = 0;
		int internalIndex = 0;
		int i = 0;
		while ((ret = _wrapper.getInfoNotPairedDevice(i++, deviceName,
				macAddress)) > 0) {
			String strDevicename = deviceName.get();
			if (strDevicename.contains(DEVICE_NAME)) {
				if (internalIndex++ == index) {
					return 1;
				}
			}
		}
		return ret;

	}

	/*
	 * ! This static method pairs the device identified by the provided mac
	 * address and the local Bluetooth stack.
	 * 
	 * \param macAddress Pointer to a vector with the six bytes of the Bluetooth
	 * mac address. macAddress[0] is the byte on the right from the mac address
	 * label on the device.
	 */
	public static int pairWithDevice(String macAddress, Activity activity, IScanDiscoveryFinishedHandler scanDiscoveryFinished) {

		BluetoothManager _wrapper = BluetoothManager.getInstance(activity, scanDiscoveryFinished);
		Logger logger = Logger.getInstance();


		return _wrapper.pairDevice(macAddress, null);
	}

	/*
	 * ! Indicates whether the device is stimulating.
	 */
	// ORIPOLLES
	public boolean isDeviceStimulating(){
	    //qDebug()<<"DeviceManager::isDeviceStimulating"<<_isStimulationRunning;
	    return _isStimulationRunning;
	}

	/*
	 * ! Indicates whether the device is streaming.
	 */
	// ORIPOLLES
	public boolean isDeviceStreaming(){
	    return _isStreaming;
	}
	public boolean isDeviceStreamingAccelerometer(){
	    return _isStreamingAccelerometer;
	}

	/*
	 * ! This method sets whether the device is stimulating.
	 * 
	 * \param input Boolean indicating the status of the stimulation.
	 */
	// ORIPOLLES
	public void setDeviceStimulating(boolean input){
	    logger.info("DeviceManager::setDeviceStimulating " + input, Logger.LOG_FILE_ON);
	    _isStimulationRunning=input;
	    _protocol.setIsStimulating(input);
	}

	/*
	 * ! This method sets whether the device is streaming.
	 * 
	 * \param input Boolean indicating the status of the streaming.
	 */
	public void setDeviceStreaming(boolean input){
	    _isStreaming = input;
	}

	/*
	 * ! This method does the operations for requesting the firmware version
	 * 
	 * \return True if the operations has been successfully performed, false
	 * otherwise.
	 */
	public boolean doFirmwareVersionRequest(){
	    //QByteArray txBuffer = StarStimProtocol::buildFirmwareVersionFrame();
		ArrayList<Byte> txBuffer = StarStimProtocol.buildFirmwareVersionFrame();
	    _stopPollThread();
	    if (!_sendRequest(txBuffer))
	    {	
	        logger.info( "error sending firmware version battery request" , Logger.LOG_FILE_ON);
	        _startPollThread();
	        return false;
	    }
	    _startPollThread();
	    return true;
	}

	/*
	 * ! This method does the operations for requesting the firmware version and
	 * the battery
	 * 
	 * \return True if the operations has been successfully performed, false
	 * otherwise.
	 */
	public boolean doFirmwareVersionBatteryRequest(){				
		
		ArrayList<Byte> txBuffer = StarStimProtocol.buildFirmwareVersionBatteryFrame();		
		if( ENABLE_POLLTHREAD ) _pausePollThread();
	    _isOnlineStimulationRunning = false;	    
	    boolean result = _sendRequest(txBuffer);
	    if( ENABLE_POLLTHREAD ) _resumePollThread();	
		return result;
		
		/* THIS IS THE PREVIOUS IMPLEMENTATION
	    _stopPollThread();
	    if (!_sendRequest(txBuffer))
	    {	
	        logger.info( "error sending firmware version battery request" , Logger.LOG_FILE_ON);
	        _startPollThread();
	        return false;
	    }
	    _startPollThread();
		return true;
	    /**/

	}

	/*
	 * ! It sets the application in demo mode so the signals reported by the
	 * data producer are the ones present in a file pass as a parameter.
	 * 
	 * \param b If true the application turns to demo mode, otherwise it backs
	 * to normal operation.
	 * 
	 * \param signalFile File with the signals to be reported by the data
	 * producer. Each row on the file shall have the channel samples separated
	 * by tabulators.
	 */
	// void setDemoMode (bool b, const char * signalFile);

	/*
	 * ! It sets whether the application should turn the line noise filter on.
	 * 
	 * \param value If true the application applies the line noise filter.
	 */
	// void setNoiseFilter (bool value);

	/*
	 * ! It returns the firmware version.
	 */
	public int getFirmwareVersion (){
	   //qDebug()<<"DeviceManager::getFirmwareVersion"<<_firmwareVersion;
	   return _firmwareVersion;
	}

	/*
	 * ! It returns whether the system is 1000SPS
	 */
	public int is1000SPS(){
		return _is1000SPS;
	}

	/*
	 * ! It returns the last device error.
	 */
	// RFCOMMDevice::errType getLastError();

	public float getClockDrift (Reference<Integer> numSamples){
	    return _drifftClock.getDrifft(numSamples);
	}

	public double getActualSampleRate (){
	    return _sampleRateCorrector.getActualSampleRate();
	}

	public boolean isAStimulationDevice(){
	    logger.info( "DeviceManager::_isaStimulationDevice" + _isaStimulationDevice, Logger.LOG_FILE_ON);
	    return _isaStimulationDevice;
	}

	public void setIsStarStim(boolean value){
	    logger.info(  "_isaStimulationDevice to " + value , Logger.LOG_FILE_ON);
	    _isaStimulationDevice=value;
	}

	/*
	 * ! It indicates whether lookForStarStim failed.
	 * 
	 * \return True if lookForStarStim failed.
	 */
	// bool lookForStarStimFailed();

	/*
	 * ! It indicates the reported status of the device.
	 * 
	 * \return Status
	 */
	// int currentDeviceStatus();

	/*
	 * ! Configures the number of samples per beacon
	 */
	public void samplesPerBeacon(int value){
	    // Set StarStimProtocol to compressionType
	    _samplesPerBeacon = value;

		// Write Registers	    
	    Byte[] sampleBeaconREG = new Byte[1];
	    sampleBeaconREG[0] = (byte) value;
	    
	    logger.info( "Configure Samples per beacon to  " + sampleBeaconREG[0], Logger.LOG_FILE_ON);
	    writeRegister(DeviceManager.StarStimRegisterFamily.EEG_REGISTERS, (byte) 0x51, sampleBeaconREG, (byte) 1);

	    Byte[] REGRead = new Byte[1];
	    readRegister(DeviceManager.StarStimRegisterFamily.EEG_REGISTERS, (byte) 0x51, REGRead, (byte) 1);
        logger.info( "Reading register EEG_REG(" + 0x51 + ")="  + 
    			String.format("0x%02X Expected=0x%02X", REGRead[0], sampleBeaconREG[0]), Logger.LOG_FILE_ON );
	}

	/*
	 * ! Returns the number of samples per beacon configured
	 */
	public int samplesPerBeacon( ){
		return _samplesPerBeacon;
	}

	/*
	 * ! Configures the multipleSample feature
	 */
	public void multipleSample(int value){
	    _protocol.multipleSample( value );

		// Write Registers	    
	    Byte[] multiSampleREG = new Byte[1];
	    multiSampleREG[0] = (byte) value;
	    
	    logger.info( "Configure Samples per beacon to  " + multiSampleREG[0], Logger.LOG_FILE_ON);
	    writeRegister(DeviceManager.StarStimRegisterFamily.EEG_REGISTERS, (byte) 0x50, multiSampleREG, (byte) 1);

	    Byte[] REGRead = new Byte[1];
	    readRegister(DeviceManager.StarStimRegisterFamily.EEG_REGISTERS, (byte) 0x50, REGRead, (byte) 1);	    
        logger.info( "Reading register EEG_REG(" + 0x50 + ")="  + 
        			String.format("0x%02X Expected=0x%02X", REGRead[0], multiSampleREG[0]), Logger.LOG_FILE_ON );
	    
	}
	
	

	/*
	 * ! Returns whether the multipleSample feature
	 */
	public int multipleSample( ){
		return _protocol.multipleSample();
	}

	/*
	 * ! Configures the compressionType feature
	 */
	public void eegCompressionType(StarStimProtocol.EEGCompressionType value){
	    // Set StarStimProtocol to compressionType
	    _protocol.eegCompressionType(value);

		// Write Registers	    
	    Byte[] compressionTypeREG = new Byte[1];
	    compressionTypeREG[0] = (byte) value.getNumVal();
	    
	    logger.info( "Enabling Compression " + compressionTypeREG[0], Logger.LOG_FILE_ON);
	    writeRegister(DeviceManager.StarStimRegisterFamily.EEG_REGISTERS, (byte) 0x52, compressionTypeREG, (byte) 1);

	    Byte[] REGRead = new Byte[1];
	    readRegister(DeviceManager.StarStimRegisterFamily.EEG_REGISTERS, (byte) 0x52, REGRead, (byte) 1);	    
        logger.info( "Reading register EEG_REG(" + 0x52 + ")="  + 
    			String.format("0x%02X Expected=0x%02X", REGRead[0], compressionTypeREG[0]), Logger.LOG_FILE_ON );
	    
	}

	/*
	 * ! Returns whether the compression is Enabled
	 */
	public StarStimProtocol.EEGCompressionType eegCompressionType( ){
	    return _protocol.eegCompressionType();
	}

	// int getPacketsLostPer30Seconds();

	// -------------------------------PRIVATE:

	/*
	 * ! It determines whether there is an enzo3G/StarStim device at the other
	 * side of the rfcomm connection.
	 * 
	 * \return True if an enzo3G/StarStim device is detected , false
	 * otherwise.
	 */
	public boolean _lookForStarStim() {
		logger.info("DeviceManager::_lookForStarStim ()", Logger.LOG_FILE_ON);
		_isDevicePresent = false;

		// Send StartBeacon Request
		try{
			ArrayList<Byte> txBuffer = StarStimProtocol.buildStartBeaconRequest();		
			if (_device.write(txBuffer, txBuffer.size()) < 0) return false;
		} catch (IOException e) {
			e.printStackTrace();					
			logger.info( Logger.stack2string(e) , Logger.LOG_FILE_ON);
			return false;
		}

		// Process reply to ensure device is there
		int retries = 0;
		while ((!_isDevicePresent) && (retries++ < 300)) {
			if (_processData( MAX_LENGTH_RX_BUFFER ) < 0)
				break;
		}

		if (!_isDevicePresent)
			_lookForStarStimFailed = true;
		else
			_lookForStarStimFailed = false;

		
		logger.info( "Ending _lookForStarStim " + _isDevicePresent, Logger.LOG_FILE_ON);
		return (_isDevicePresent);
	}

	/*
	 * ! It processes the received data (EEG streaming, read configuration,
	 * acknowledgements).
	 * 
	 * \param nBytes Bytes to be processed from the rfcomm device.
	 * 
	 * \return Negative number if there was any problem while processing the
	 * information. Zero if the frame from the device has not been completely
	 * processed yet. A Positive number if a data frame has been successfully
	 * processed.
	 */
	 private int _processData (int nBytes){

		 int ret = -1;
		 int nBytesToRead;
		 int nBytesRead;

		 //qDebug()<<"_processData()"<<QThread::currentThreadId();

		 nBytesToRead = nBytes;
		 //logger.info( "_processData() nBytesToRead " + nBytesToRead , Logger.LOG_FILE_ON);
		 while (nBytesToRead > 0)
		 {

			 nBytesToRead = (nBytesToRead > MAX_LENGTH_RX_BUFFER) ?
					 MAX_LENGTH_RX_BUFFER : nBytesToRead;

			 //assert(_rxBuffer!=null);
			 _rxBuffer.clear();
			 try {
				nBytesRead = _device.read(_rxBuffer, nBytesToRead);
			} catch (IOException e) {
				e.printStackTrace();
				logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
				break;
			}
			 
			 
			 //logger.info(  "DeviceManager::_processData " + nBytesRead, Logger.LOG_FILE_ON);
			 //qDebug() << "_processData" << nBytesRead << "(" << QThread::currentThreadId() << ")";
			 if(nBytesRead==0)
			 {

			 }
			 if (nBytesRead < 0)
			 {
				 //	  printf(":");
				 break;
			 }
			 //fflush(stdout);

			 
			 // NOTE: Keep it for debug purposes
			 String aux = "Frame contents: ";
			 for (int i = 0; i < nBytesRead; i++){

				 if( (i + 2) < _rxBuffer.size()){
				 //if( nBytesRead > 2 ){
					 // Detect SOF
					 if( _rxBuffer.get(i) == 83 && _rxBuffer.get(i+1) == 79 && _rxBuffer.get(i+2) == 70 ) aux += "| ";
				 }

				 //aux += String.format("%02X ",_rxBuffer.get(i) );
				 aux += (_rxBuffer.get(i)&0xFF) + " ";

			 }

// 		        //if( _waitingFirstEEGSample )
// 		        //  if( nBytesRead != 0 )
// 		            if(_isStreaming == true || this.isDeviceStimulating() )
//		        logger.info ( aux, Logger.LOG_FILE_ON);
// 		        // ---------------------------------

			 ret = 0;

			 for (int i = 0; i < nBytesRead; i++)			 
			 {
				 // Do not process;
				 //if(true) continue;

				 //logger.info("Evaluating i:" + i, Logger.LOG_FILE_ON);
				 
				 // MAIN IF (parseByte)
				 if (_protocol.parseByte(_rxBuffer.get(i)))
				 {
					 _beaconCounterBattery++;
					 _beaconCounterStayAlive++;
					 _beaconCounterOnlineStimulation++;
					 StarStimData data = _protocol.getStarStimData();

					 //DEBUG drift clock
					 int deviceStatus = data.deviceStatus();
					 if ( ((deviceStatus & 0x02)!= 0x00) && !_isSampleRateEEG)
					 {
						 _isSampleRateEEG = true;
/*						 
						 if (is1000SPS())
						 {
							 _drifftClock.reset(1000.0);
							 qDebug() << "reset to 1000";
						 }
						 else
						 {
							 _drifftClock.reset(500.0);
							 qDebug() << "reset to 500";
						 }
/**/						 
					 }
					 else if (_isSampleRateEEG && (deviceStatus & 0x02) == 0)
					 {
						 _isSampleRateEEG = false;
						 //_drifftClock.reset(1000.0);
						 logger.info( "reset to 1000", Logger.LOG_FILE_ON );
					 }

					 if (!_isSampleRateEEG && _firmwareVersion >= 593)
					 {
						 _drifftClock.newSample();
					 }

					 //

					 if (data.isStimDataPresent())
					 {
						 if (!_firstStimSampleReceived)
						 {
							 _firstStimSampleReceived = true;

							 long timeFirstSample = Calendar.getInstance().getTimeInMillis();
							 long latency = timeFirstSample - _timeRequestFirstStimSample;
							 // we assume a symetric radio link
							 _currentStimTimestamp = timeFirstSample - (latency / 2);
							 _currentStimTimestamp -=1;
						 }

						 _currentStimTimestamp++;

						 _lastStimData = data.stimulationData();
						 _lastStimData.setTimestamp(_currentStimTimestamp);

						 //emit newStimulationData(_lastStimData);
					 } // END: data->isStimDataPresent

					 // data->isEEGDataPresent()
					 if (data.isEEGDataPresent())
				     //if (false)
					 {
						 

						 
						 
						 // with EEG the beacon rate is half the regular one
						 _beaconCounterStayAlive++;
						 int diff = 1;
						 if (_waitingFirstEEGSample)
						 {
			 
							 _countPacketLost=0;

							 _countPacketsPer30Seconds=0;
							 _countPacketsLostPer30Seconds=0;

							 _sampleRateCorrector.reset();
							 _waitingFirstEEGSample = false;

							 if (_firstStimSampleReceived)
							 {
								 _currentTimestamp=_currentStimTimestamp;
							 }
							 else
							 {
								 long timeFirstSample = Calendar.getInstance().getTimeInMillis();
								 long latency = timeFirstSample - _timeRequestFirstEEGSample;
								 // we assume a symetric radio link
								 _currentTimestamp = timeFirstSample - (latency / 2);
							 }
							 _firstEEGTimeStamp=_currentTimestamp;
							 
							 
						 }
						 else // !_waitingFirstEEGSample
						 {
							 							
							 diff = _diffBetweenStamps(_currentEEGStamp, data.eegStamp());

////////////////////			
//							 logger.info("data.eegStamp():" + data.eegStamp() + " _currentEEGStamp:" + _currentEEGStamp 
//									 + " diff:" + diff, Logger.LOG_FILE_ON);							 
//							 if(diff > 1){
//								 logger.info("PACKET LOSS " + diff, Logger.LOG_FILE_ON);								 
//							 }
////////////////						
								
/*
							 #ifdef __PACKETLOSSREPORTFILE__
							 packetLossReportFile.write(QString(QString::number(diff) + QString("\n")).toAscii());
							 #endif //__PACKETLOSSREPORTFILE__
/**/
							 _countPacketsPer30Seconds+=diff;
							 _countPacketsLostPer30Seconds+=diff-1;
							 _countPacketLost+=diff-1;

							 if (_countPacketsPer30Seconds>=15000) //We check packet loss each 15000 packets							 
							 {
								 logger.info ( "DeviceManager percentage of packets lost " + 
										 		_countPacketsLostPer30Seconds + " " + _countPacketsPer30Seconds + " " + (_countPacketsLostPer30Seconds*100.0/_countPacketsPer30Seconds)
										 		, Logger.LOG_FILE_ON);

								 //emit newPacketLossData(_countPacketsLostPer30Seconds*100.0/_countPacketsPer30Seconds);
//								 if( _enzoHandler != null){
//									 _enzoHandler.newPacketLossData(_countPacketsLostPer30Seconds*100.0/_countPacketsPer30Seconds);
//								 }
								 

								 _countPacketsPer30Seconds=0;
								 _countPacketsLostPer30Seconds=0;
							 }
/*							 
							 else{
								 logger.info("test", Logger.ONLY_ANDROID_CONSOLE);
							 }
/**/							 
	 
						 } // END _waitingFirstEEGSample


						 // Transform data from the instrument to meaningful information
						 // ------------------------------------------------------------
						 _currentEEGStamp = data.eegStamp();

						 
						 int[]  dataEEGRef = new int[32];

						 //////////////////////////////
						 int[] compressed = new int[32];
						 int[] decompressed = new int[32];
						 //////////////////////////////
						 for(int j = 0; j < data.eegDataArray().size(); j++){
							 int[] dataEEG = data.eegDataArray().get(j).data();

							 for (int k = 0; k < 32; k++)
							 {
								 ///////////////////////////////////
								 compressed[k] = dataEEG[k];								 								 
								 ///////////////////////////////////
								 if (dataEEG[k] >=0x800000)
								 {
									 dataEEG[k] = dataEEG[k] - 0x1000000;
								 }

								 // Store first sample
								 if( (eegCompressionType() != StarStimProtocol.EEGCompressionType.EEG_NO_COMPRESSION) && (j == 0) ){
									 dataEEGRef[k] = dataEEG[k];
								 }
								 // First EEG sample is not compressed
								 if( (eegCompressionType() != StarStimProtocol.EEGCompressionType.EEG_NO_COMPRESSION)  && j != 0){
									 if (dataEEG[k] >=0x8000 && eegCompressionType() == StarStimProtocol.EEGCompressionType.EEG_16BIT_COMPRESSION)
									 {
										 dataEEG[k] = dataEEG[k] - 0x10000;
									 }

									 if (dataEEG[k] >=0x800 && eegCompressionType() == StarStimProtocol.EEGCompressionType.EEG_12BIT_COMPRESSION)
									 {
										 dataEEG[k] = dataEEG[k] - 0x1000;
									 }

									 // Uncompression operation
									 dataEEG[k] = dataEEG[k] + dataEEGRef[k];
								 }

								 ///////////////////////////////////
								 decompressed[k] = dataEEG[k];
								 ///////////////////////////////////

								 dataEEG[k] = (int) ((dataEEG[k] * 2.4 * 1000000000) / 8388607.0 / 6.0);
							 }

							 //////////////////////////////////////////////
							 String str = "  com dataEEG" + j + " ";
							 for( int k = 0; k < 8; k++){
								 str += String.format(" [%d]=0x%02X ", k , compressed[k]);
							 }
							 //logger.info ( str, Logger.LOG_FILE_ON );

							 str = "decom dataEEG" + j + " ";
							 for( int k = 0; k < 8; k++){
								 str += String.format(" [%d]=0x%02X ", k , decompressed[k]);
							 }
							 //logger.info ( str , Logger.LOG_FILE_ON );

							 

							 //////////////////////////////////////////////

						 }


						 while(--diff > 0)
						 {
							 //If FW version is lower than 593, we need the sample rate corrector
							 int correct=0;
							 if (_firmwareVersion<593)
							 {
								 correct = _sampleRateCorrector.newSample();
/*								 
								 #ifdef __ERP_PROTOCOL_OXFORD__
								 correct = 0; // no sample rate correction in real time
								 #endif
/**/								 
							 }
							 else
							 {
								 _drifftClock.newSample();
							 }

							 while (correct-- >= 0) // 0 no correction, > 0 last sample repetitio, < 0 remove sample
							 {

								 // If 1000SPS (MultipleSample Mode) => 2 samples per lost beacon
								 //		                            int iterations = (is1000SPS() == 1) ? 2 : 1;
								 int iterations = (is1000SPS() == 1) ? 2 : _samplesPerBeacon;
								 for( int j = 0; j < iterations; j++){

//									 _currentTimestamp += (is1000SPS() == 1)?1:2;
									 _currentTimestamp += 8;  // This should be when using 125SPS
									 _lastEEGData.setTimestamp(_currentTimestamp);
									 _lastEEGData.setRepeated(true);
									 //emit newEEGData(_lastEEGData);
									 if( this._enzoHandler != null )
										 _enzoHandler.newEEGData( _lastEEGData );
								 }

							 }
						 }
						 //If FW version is lower than 593, we need the sample rate corrector
						 int correct = 0;
						 if (_firmwareVersion<593)
						 {
							 correct = _sampleRateCorrector.newSample();
/*							 
							 #ifdef __ERP_PROTOCOL_OXFORD__
							 correct = 0; // no sample rate correction in real time
							 #endif
/**/							 
						 }
						 else
						 {
							 // TODO: THIS SHOULD BE CALLED FOR EVERY SAMPLE
							 //for( j = 0 ; j < data->eegDataArray().count(); j ++)
							 _drifftClock.newSample();
						 }

						 while (correct-- >= 0) // 0 no correction, > 0 last sample repetition, < 0 remove sample
						 {

							 for( int j = 0 ; j < data.eegDataArray().size(); j ++){

								 //_currentTimestamp += is1000SPS() == 1?1:2;
								 _currentTimestamp += 8;  // This should be when using 125SPS
								 data.eegDataArray().get(j).setTimestamp(_currentTimestamp);
								 _lastEEGData = data.eegDataArray().get(j);
								 _lastEEGData.setRepeated(false);
								 //emit newEEGData(_lastEEGData);
								 if( this._enzoHandler != null ){
//									 _enzoHandler.newEEGData( new ChannelData() );
									 _enzoHandler.newEEGData( _lastEEGData );
//									 if( _lastEEGData == null ) logger.info("Last eeg data NULL", Logger.LOG_FILE_ON);
//									 else logger.info("Last eeg data NOT NULL", Logger.LOG_FILE_ON);
								 }
									 

							 }
						 }
					 } // END: data->isEEGDataPresent()

					 if (data.isStimImpedancePresent())
					 {
						 //emit newImpedanceData(data->stimImpedanceData(), _currentTimestamp);
					 }
					 if (data.isEEGConfigPresent())
					 {
						 int numReg = data.eegNumRegs();
						 int starAdd = data.eegStartAddress();
						 char[] reg = data.eegReg();						 
						 for (int j = 0; j < numReg; j++)
						 {
							 _eegRegisters[starAdd + j].setValue(reg[starAdd + j]);
						 }
					 } // END: data->isEEGConfigPresent()

					 if (data.isStimConfigPresent())
					 {
						 int numReg = (int) data.stimNumRegs();
						 int starAdd = data.stimStartAddress();
						 char[] reg = data.stimReg();
						 for (int j = 0; j < numReg; j++)
						 {
							 _stimRegisters[starAdd + j].setValue(reg[starAdd + j]);
						 }
						 
						 
					 } // END: data->isStimConfigPresent()

					 if (data.isBatteryPresent())
					 {
						 logger.info("Is battery present " + data.battery(), Logger.LOG_FILE_ON );
						 logger.info( "Is battery present " + String.format("0x%08X", data.battery() ), Logger.LOG_FILE_ON );
						 logger.info( "Is battery present " + _calcStOfCharge(data.battery()), Logger.LOG_FILE_ON );
						 //emit reportBatteryLevel(_calcStOfCharge(data->battery()));
						 if( _enzoHandler != null )
							 _enzoHandler.reportBatteryLevel( _calcStOfCharge(data.battery()) );						 						 						
					 } // END: data->isBatteryPresent()

					 if (data.isFirmwareVersionPresent())
					 {
						 _firmwareVersion = data.firmwareVersion();
						 _is1000SPS       = data.get1000SPS();
						 _protocol.setFirmwareVersion(_firmwareVersion);
						 logger.info( "Firmware version is " + _firmwareVersion, Logger.LOG_FILE_ON);
						 //emit reportFirmwareVersion(data->firmwareVersion(), data->get1000SPS());
						 if( _enzoHandler != null )
							 _enzoHandler.reportFirmwareVersion( data.firmwareVersion(), data.get1000SPS() );
					 } // END: data->isFirmwareVersionPresent()

					 if (data.isAccelerometerPresent())
					 {
						 //		                    int * acclData= data->accelerometer();

						 //		                    ChannelData aux;
						 //		                    aux.setTimestamp(_currentTimestamp);
						 //		                    aux.setChannelInfo(14);
						 //		                    aux.setData(1,acclData[0]);
						 //		                    aux.setData(2,acclData[1]);
						 //		                    aux.setData(3,acclData[2]);

						 logger.info("RECEIVED ACCEL", Logger.LOG_FILE_ON);
						 _lastAccelerometerData=data.accelerometer();

						 _lastAccelerometerData.setChannelInfo(7);
						 _lastAccelerometerData.setTimestamp(_currentTimestamp);

						 //logger.info( _lastAccelerometerData.timestamp() + "" );
						 //emit newAccelerometerData(_lastAccelerometerData);
					 } // END: data->isAccelerometerPresent()

					 if (data.isCommandToggled())
					 {
						 _ackReceived = true;
					 } // END: data->isCommandToggled()

					 if (_deviceStatus != data.deviceStatus() )
					 {
						 _deviceStatus = data.deviceStatus();
						 if( _enzoHandler != null)
			               	 _enzoHandler.newDeviceStatus(_deviceStatus);
						 //emit newDeviceStatus(_deviceStatus);
					 }
					 _isDevicePresent = true;
					 ret = 1;
				 } // END MAIN IF (parseByte)

			 }// End for loop _rxBuffer[nBytesRead]

			 if ((nBytesToRead == nBytesRead) &&
					 (nBytesRead == MAX_LENGTH_RX_BUFFER)) // last reading was the
				 // maximum amount of
				 // data per reading
			 {
				 // it might be pending data
				 nBytesToRead = (int) _device.pendingBytesOnReading();
			 }
			 else
			 {
				 nBytesToRead = 0;
			 }
		 } // end while (nbytesToRead)

		 //logger.info("_processData() end");

		 return (ret);		 		 
	 }

	/*
	 * ! It sends a request frame and waits for the acknowledge.
	 * 
	 * \param request Byte array with the request to be sent
	 * 
	 * \return True if the request has been sent and the acknowledge has been
	 * received, false otherwise
	 */
	 private boolean _sendRequest (ArrayList<Byte> request){
	    _ackReceived = false;
	    try {
			if (_device.write( request, request.size()) < 0) return false;
		} catch (IOException e) {
			e.printStackTrace();
			logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
		}

	    int retries = 0;
	    int noDataCounter=0;
	    int ret;
	    while (!_ackReceived && (retries++ < 1000))
	    {
	        ret = _processData( MAX_LENGTH_RX_BUFFER );
	        if (ret < 0)
	        {
	            return false;
	        }
	        else if (ret == 0)
	        {
	            noDataCounter++;

	            if (noDataCounter>10)
	            logger.info( "DeviceManager::_sendRequest noDataCounter" + noDataCounter, Logger.LOG_FILE_ON );

	            //if (noDataCounter==100)
	            //     emit startBlinking();
	        }
	        //logger.info("Retry:" + retries, Logger.LOG_FILE_ON);
	    }

	    if (!_ackReceived)
	    {	
	        logger.info( "_sendRequest failed because the acknowledgment was not received. Is device disconnected?", Logger.LOG_FILE_ON );
	        return false;
	    }
	    
	    
	    return true;
	}


	
	/*
	 * ! This method keeps alive the communication with the device and perform
	 * periodic operations like processing the incoming streamings, requesting
	 * the battery level or sending the online stimulation signal.
	 * 
	 * \return Zero or a negative number according to success or failure due to
	 * an error.
	 */
	private int _poll (){
	    logger.info( "DeviceManager::_poll", Logger.LOG_FILE_ON );
	    int processDataResult;
	    int noDataCounter = 0;

	    while(_terminatePollThread)
	    {
	    	if( _pausePollThread ){
	    		//logger.info("Polling thread is paused " + _isPausedPollThread, Logger.LOG_FILE_ON);
	    		_isPausedPollThread = true;
	    		try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					//logger.info("Sleep from _poll was interrupted. Was that controlled situation?", Logger.LOG_FILE_ON);
				}
	    		continue;
	    	}else{
	    		_isPausedPollThread = false;
	    	}
	    	
	    	
	    	/*
	        if (_isOnlineStimulationRunning)
	        {	        	
	            if (!_doOnlineStimulation())
	            {
	                _isOnlineStimulationRunning = false;
	            }	           
	        }
	        /**/
	        if (_beaconCounterBattery >= 30000) // Battery measurement
	        {
	        	logger.info("Performing battery measurement ... ", Logger.LOG_FILE_ON);
	            _beaconCounterBattery = 0;
	            _doBatteryMeasurement();
	        }
	        if (_isaStimulationDevice)
	        {
	            if (_beaconCounterStayAlive >= 200)
	            {
		        	logger.info("Sending null request ... ", Logger.LOG_FILE_ON);
	                _doNullRequest();
	                _beaconCounterStayAlive = 0;
	            }
	        }
	        
	        // Process data received in the buffer
	        processDataResult = _processData( MAX_LENGTH_RX_BUFFER );
	        if (processDataResult > 0)
	        {
	            //In case it was blinking but we recovered signal
	            if (noDataCounter>=20)
	            {
	                logger.info( "----------------------->Here", Logger.LOG_FILE_ON );
	                //emit stopBlinking();
	            }

	            noDataCounter = 0;
	        }
	        else
	        {
	            if (noDataCounter>10)
	                logger.info( "DeviceManager::_poll noDataCounter" + noDataCounter, Logger.LOG_FILE_ON );

	            if (noDataCounter==20)
	                //emit startBlinking();

	            if ((processDataResult < 0) || (++noDataCounter > /*0xfffff*/MAXNODATACOUNTER))
	            {
	                _deviceStatus = 255; // unknown value
	                try {
						_device.close();
					} catch (IOException e) {
						e.printStackTrace();
						logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
					}


	                logger.info( "Close device. DeviceManager::_poll noDataCounter" + noDataCounter, Logger.LOG_FILE_ON);
	                if( _enzoHandler != null)
	                	 _enzoHandler.newDeviceStatus(_deviceStatus);
	                //emit newDeviceStatus(_deviceStatus);
	
	                return -1;
	            }
	        }
	    } // End :  while(_isPollThreadRunning)

	    logger.info( "_poll: reading loop finished", Logger.LOG_FILE_ON ); 

	    
	    return 0;
	}

	@Override
	public void run() {

		synchronized (this) {
		    _isRunningPollThread = true;
		    logger.info("Starting _poll routine. Set isRunning to " + _isRunningPollThread, Logger.LOG_FILE_ON);
		}
		this._poll();

		synchronized (this) {
			_isRunningPollThread = false;
			logger.info("Stopping _poll routine. Set isRunning to " + _isRunningPollThread, Logger.LOG_FILE_ON);			
		}

	    
	} 
	

	
	/*
	 * ! It starts the thread where the DeviceManager::_poll works, so the
	 * incoming data is processed on the background.
	 */
	private void _startPollThread (){

		String threadId = Thread.currentThread().getName() + "(" + Thread.currentThread().getId() + ")";
		logger.info("About to launch _startPollThread " + _isRunningPollThread + " by "+ threadId, Logger.LOG_FILE_ON);
		

		// Wait whenever a thread was already started
		try{
			long initTime = SystemClock.currentThreadTimeMillis();
			//while( (_threadResult.getState() == Thread.State.RUNNABLE) || (isRunning == true) ){
			while( (_isRunningPollThread == true) ){
				logger.info("Waiting to start _poll thread" 
								+ SystemClock.currentThreadTimeMillis() + " " + initTime , Logger.LOG_FILE_ON);

				Thread.sleep(100);			
				if( SystemClock.currentThreadTimeMillis() > (initTime+1000) ) throw new InterruptedException();
			}						
		}catch(InterruptedException e){
			e.printStackTrace();
			logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
		}
		

		// Start new thread
		if( _threadResult.getState() != Thread.State.RUNNABLE || (_isRunningPollThread == false))
		{
			   logger.info( "DeviceManager._startPollThread...", Logger.LOG_FILE_ON );
			   _terminatePollThread = true;
			   _threadResult = new Thread(this);
			   _threadResult.setName("Polling Thread");
			   _threadResult.start();			   
		}		

		

		

		
		// Wait until thread is running
		try{
			long initTime = SystemClock.currentThreadTimeMillis();

			//while( (_threadResult.getState() != Thread.State.RUNNABLE) || (isRunning != true) ){
			while( (_isRunningPollThread == false) ){
				logger.info("Waiting to get _poll thread started " 
								+ SystemClock.currentThreadTimeMillis() + " " + initTime , Logger.LOG_FILE_ON);

				Thread.sleep(100);			
				if( SystemClock.currentThreadTimeMillis() > (initTime+1000) ) throw new InterruptedException();
			}						
		}catch(InterruptedException e){
			e.printStackTrace();
			logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
		}
		
		// Thread is not paused
		_pausePollThread = false;		
		logger.info("Setting _pausePollThread to " + _pausePollThread, Logger.LOG_FILE_ON);
	}
	 
	/*
	 * ! It stops the thread where the DeviceManager::_poll works, so the
	 * incoming data stops of being processed on the background.
	 */
	private void _stopPollThread (){
		
		logger.info("Stopping Polling thread ...", Logger.LOG_FILE_ON);
		
		if( _threadResult.getState() == Thread.State.RUNNABLE || _isRunningPollThread == true )
	    {
	        _terminatePollThread = false;
	        //_threadResult.result();
	        logger.info("Setting _isPollThreadRunning to false", Logger.LOG_FILE_ON);
	    }
		
		
		// Wait thread to stop
		try{
			long initTime = SystemClock.currentThreadTimeMillis();
			while( _isRunningPollThread == true ){
				logger.info("Waiting to stop _poll thread " 
						+ SystemClock.currentThreadTimeMillis() + " " + initTime + "(" + _isRunningPollThread + "/" + _terminatePollThread + ")", Logger.LOG_FILE_ON);

				Thread.sleep(100);			
				if( SystemClock.currentThreadTimeMillis() > (initTime+1000) ) throw new InterruptedException();
			}						
		}catch(InterruptedException e){
			e.printStackTrace();
			logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
		}
		
		logger.info("Thread ended! isRunning:" + _isRunningPollThread, Logger.LOG_FILE_ON);
		
		
		// Thread is not paused
		_isPausedPollThread = false;
		logger.info("Setting _isPausedPollThread to " + _isPausedPollThread, Logger.LOG_FILE_ON);
	}

	/*!
	 * Pauses the polling thread
	 */
	private void _pausePollThread(){
		_pausePollThread = true;		
		//logger.info("Setting _pausePollThread to " + _pausePollThread, Logger.LOG_FILE_ON);

		try {
		
			// While loop waiting for thread to be paused
			long initTime = SystemClock.currentThreadTimeMillis();	
			while(_isPausedPollThread == false ){
				//logger.info("_pausePollThread:" + _pausePollThread + " Waiting thread to be resumed ...", Logger.LOG_FILE_ON);
				if( SystemClock.currentThreadTimeMillis() > (initTime+1000) )throw new InterruptedException();
			}
		
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
		}
	}
	
	/*!
	 * Pauses the polling thread
	 */
	private void _resumePollThread(){
		_pausePollThread = false;
		//logger.info("Setting _pausePollThread to " + _pausePollThread, Logger.LOG_FILE_ON);
		
		// This causes the thread to abort sleep
		_threadResult.interrupt();
		
		try {
			long initTime = SystemClock.currentThreadTimeMillis();		
			while(_isPausedPollThread == true ){
				//logger.info("_pausePollThread:" + _pausePollThread + " Waiting thread to be resumed ...", Logger.LOG_FILE_ON);
				if( SystemClock.currentThreadTimeMillis() > (initTime+1000) ) throw new InterruptedException();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
		}
	}
	
	
	/*
	 * ! This method performs the operation for feeding the StarStim device with
	 * the user defined signal in real time.
	 * 
	 * \return True if the online stimulation operations are done successfully,
	 * false otherwise.
	 */
	// bool _doOnlineStimulation ();

	/*
	 * ! This method does the operations for requesting the battery measurement
	 * 
	 * \return True if the operations has been successfully performed, false
	 * otherwise.
	 */
	private boolean _doBatteryMeasurement (){
	    logger.info( "DeviceManager::_doBatteryMeasurement", Logger.LOG_FILE_ON );
	    ArrayList<Byte> txBuffer = StarStimProtocol.buildReadBatteryRequest();
	    if (!_sendRequest(txBuffer))
	    {
	        logger.info( "error sending battery measurement request", Logger.LOG_FILE_ON);
	        return false;
	    }
	    return true;
	}

	/*
	 * ! This method sends a null request which keeps the communication alive
	 * with the device.
	 */
	private boolean _doNullRequest (){
	    logger.info("DeviceManager::_doNullRequest", Logger.LOG_FILE_ON);
		ArrayList<Byte> txBuffer = StarStimProtocol.buildNullRequest();
	    if (!_sendRequest(txBuffer))
	    {
	        logger.info( "error sending _doNullRequest", Logger.LOG_FILE_ON );
	        return false;
	    }
	    return true;
	}

	/*
	 * ! It calculates the battery state of charge from the measured voltage.
	 * 
	 * \param measurement Measurement of the device battery in Volts.
	 * 
	 * \return Stimation of the percentage (from 0 to 100) of battery that still
	 * remains.
	 */
	private int _calcStOfCharge (int batteryMeasurement){
	    
		char b1 = (char) ((batteryMeasurement&0x0000ff00) >> 8);
	    char b0 = (char) (batteryMeasurement&0x000000ff);
	    float stateOfCharge = 0;

	    float vCell = (float) (((b0 << 8) + b1) >> 4) * 1.25F;

	    // 4200 mv = 100%
	    // 3630 mv = 6.25%
	    // 3040 mv = 0%
	    if (vCell > 3630)
	    {
	        stateOfCharge = 6.25F + (100 - 6.25F) / (4200 - 3630) * (vCell - 3630);
	        if (stateOfCharge > 100)
	        {
	            stateOfCharge = 100;
	        }
	    }
	    else
	    {
	        stateOfCharge = 6.25F / (3630 - 3040) * (vCell - 3040);
	        if (stateOfCharge < 0)
	        {
	            stateOfCharge = 0;
	        }

	    }
	    return (int)stateOfCharge;
	}

	/*
	 * ! It calculates the number of samples between two stamps. The stamps are
	 * defined as cyclic counters from 0 to 255.
	 * 
	 * \param previousStamp Previous stamp.
	 * 
	 * \param currentStamp Current stamp.
	 * 
	 * \return It returns the distance between the two provided stamps.
	 */
	private int _diffBetweenStamps (int previousStamp, int currentStamp){
	    if (_firmwareVersion<800) //EEGstamp has 1 byte
	    {
	        if (currentStamp < previousStamp)
	        {
	            return (currentStamp + 256 - previousStamp);
	        }
	    }

	    return (currentStamp - previousStamp) - (_samplesPerBeacon-1);
	}

	/*
	 * ! It informs whether the device identified with the provided mac address
	 * is already paired on the system.
	 * 
	 * \param macAddress Pointer to a vector with the six bytes of the Bluetooth
	 * mac address. macAddress[0] is the byte on the right from the mac address
	 * label on the device.
	 */
	public boolean _isDevicePaired(String macAddress) {
		
		//logger.info("Evaluating : DeviceManager::_isDevicePaired", Logger.LOG_FILE_ON);

		int numPairedDevices = DeviceManager.getNumberOfPairedDevices( this._activity, this._scanDiscoveryFinishedHandler);	
		for (int i = 0; i < numPairedDevices; i++) {

			Reference<String> deviceName = new Reference<String>("");
			Reference<String> deviceMAC = new Reference<String>("");
			DeviceManager.getPairedDeviceInfo(i, deviceName, deviceMAC, 
												_activity, _scanDiscoveryFinishedHandler);

			if (macAddress.equals(deviceMAC.get())) {
				logger.info("DeviceManager::_isDevicePaired true", Logger.LOG_FILE_ON);
				return true;
			}

		}
		logger.info("DeviceManager::_isDevicePaired false", Logger.LOG_FILE_ON);

		return false;
	}

	/*
	 * ! It informs whether the device identified with the provided mac address
	 * is not paired on the system but is visible.
	 * 
	 * \param macAddress Pointer to a vector with the six bytes of the Bluetooth
	 * mac address. macAddress[0] is the byte on the right from the mac address
	 * label on the device.
	 */
	private boolean _isDeviceNotPaired(String macAddress) {

		int numNotPairedDevices = getNumberOfNotPairedDevices(_activity, _scanDiscoveryFinishedHandler);
		
		// Iterate over the different devices
		for (int i = 0; i < numNotPairedDevices; i++) {
			Reference<String> deviceName = new Reference<String>("");
			Reference<String> deviceMAC = new Reference<String>("");
			logger.info("getNotPairedDeviceInfo"
					+ getNotPairedDeviceInfo(i, deviceName, deviceMAC, _activity, _scanDiscoveryFinishedHandler), Logger.LOG_FILE_ON);

			// logger.info( deviceMAC.get() );
			if (macAddress.equals(deviceMAC.get())) {
				logger.info("DeviceManager::_isDeviceNotPaired true", Logger.LOG_FILE_ON);
				return true;
			}

		}
		logger.info("DeviceManager::_isDeviceNotPaired false", Logger.LOG_FILE_ON);

		// Now we look in the remembered devices
		int numOfRememberedDevices = getNumberOfRememberedDevices(_activity, _scanDiscoveryFinishedHandler);

		for (int i = 0; i < numOfRememberedDevices; i++) {
			Reference<String> deviceName = new Reference<String>("");
			Reference<String> deviceMAC = new Reference<String>("");

			logger.info("getRememberedDeviceInfo"
					+ getAnyDeviceInfo(i, deviceName, deviceMAC, false, true,
							false, false, true, _activity, _scanDiscoveryFinishedHandler), Logger.LOG_FILE_ON);

			// logger.info( deviceMAC.get() + " " + macAddress, Logger.LOG_FILE_ON );
			if (macAddress.equals(deviceMAC.get())) {
				logger.info("DeviceManager::_isDeviceRemembered true", Logger.LOG_FILE_ON);
				return true;
			}

		}

		logger.info("DeviceManager::_isDeviceRemembered false", Logger.LOG_FILE_ON);

		return false;

	}

	/*
	 * ! It informs whether the device identified with the provided mac address
	 * is remembered on the system.
	 * 
	 * \param macAddress Pointer to a vector with the six bytes of the Bluetooth
	 * mac address. macAddress[0] is the byte on the right from the mac address
	 * label on the device.
	 */
	private boolean _isDeviceRemembered(String macAddress) {

		// Now we look in the remembered devices
		int numOfRememberedDevices = DeviceManager
				.getNumberOfRememberedDevices(_activity, _scanDiscoveryFinishedHandler);

		for (int i = 0; i < numOfRememberedDevices; i++) {

			Reference<String> deviceName = new Reference<String>("");
			Reference<String> deviceMAC = new Reference<String>("");
			logger.info("getRememberedDeviceInfo "
					+ DeviceManager.getAnyDeviceInfo(i, deviceName, deviceMAC,
							false, true, false, false, true, _activity, _scanDiscoveryFinishedHandler), Logger.LOG_FILE_ON);
			/*
			 * #ifdef __DEBUGBLUETOOTH__
			 * _device.logDebugBluetooth(QString("[DEV] ") + QString::number(i)
			 * + QString(" ")+ QString::number(deviceMAC[5],16) + QString(":")+
			 * QString::number(deviceMAC[4],16) + QString(":")+
			 * QString::number(deviceMAC[3],16) + QString(":")+
			 * QString::number(deviceMAC[2],16) + QString(":")+
			 * QString::number(deviceMAC[1],16) + QString(":")+
			 * QString::number(deviceMAC[0],16)); #endif /*
			 */
			if (macAddress.equals(deviceMAC.get())) {
				logger.info("DeviceManager::_isDeviceRemembered true", Logger.LOG_FILE_ON);
				return true;
			}

		}
		logger.info("DeviceManager::_isDeviceRemembered false", Logger.LOG_FILE_ON);
		return false;
		

	}


		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("-- Program Start --");
		Logger logger = Logger.getInstance();
		logger.info("This is my first log", Logger.LOG_FILE_ON);

		DeviceManager _deviceManager = new DeviceManager(null, null, null);

		// Testing paired/unpaired
		// Reference<String> deviceName = new Reference<String>("");
		// Reference<String> macAddress = new Reference<String>("");
		// _deviceManager.getPairedDeviceInfo(0, deviceName, macAddress);
		// logger.info("Retreived information " + deviceName.get() + " " +
		// macAddress.get());

		// _deviceManager.scanBT();
		// _deviceManager._isDevicePaired ("00:07:80:64:EB:B6");
		// _deviceManager._isDeviceRemembered ("00:07:80:64:EB:B6");
		// _deviceManager._isDeviceNotPaired ("00:07:80:64:EB:B6");
		// _deviceManager.removePairedDevice ("00:07:80:64:EB:B6");


/*		
		// Test openDevice&closeDevice
		// ---------------------------------
		 _deviceManager.openDevice ("00:07:80:64:EB:B6", false);
		
		 if( _deviceManager.closeDevice(false) == true ){
		 logger.info("Device closed sucessfully");
		 }else{
		 logger.info("Error in closing device");
		 }
/**/

/*
 		// Check commands are well performed
		// ---------------------------------		
		logger.info("StartBeaconRequest");
		ArrayList<Byte> test = StarStimProtocol.buildStartBeaconRequest();
		for (Byte b : test) {
			logger.info("Byte is -> " + String.format("%02X:", b) + " " + b);
		}

		logger.info("StopBeaconRequest");
		test = StarStimProtocol.buildStopBeaconRequest();
		for (Byte b : test) {
			logger.info("Byte is -> " + String.format("%02X:", b) + " " + b);
		}
/**/

/*		
		// Testing directly
		// ----------------
		_deviceManager._device.open("00:07:80:64:EB:B6");
		
		ArrayList<Byte> command = StarStimProtocol.buildStartBeaconRequest();
		_deviceManager._device.write(command, command.size() );
		
		
		ArrayList<Byte> bufferRead = new ArrayList<Byte>();
		for(int i = 0; i < 8; i++){
		
			int numberBytes = MAX_LENGTH_RX_BUFFER;
		
			bufferRead.clear();
	
			int readBytes = _deviceManager._device.read(bufferRead, numberBytes);
			String str = "";
			for(int j = 0; j < readBytes; j++){
				str += bufferRead.get(j) + " ";				
			}	
			
			logger.info("Read(" + i +"):" + str);
		}	
		
		
		
		command = StarStimProtocol.buildStopBeaconRequest();
		_deviceManager._device.write(command, command.size() );
		

		command = StarStimProtocol.buildStopBeaconRequest();
		_deviceManager._device.close();
/**/	
/*		
		// Testing Thread start
		// ----------------
		logger.info("Starting thrad 1" );
		_deviceManager._startPollThread();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Stopping thread ..." );
		_deviceManager._stopPollThread();

		logger.info("Wait 1 second");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("Starting thrad 2" );
		_deviceManager._startPollThread();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_deviceManager._stopPollThread();
/**/
		
/*		
		// Testing CommandConstruction
		// ----------------
		ArrayList<Byte> command = StarStimProtocol.buildFirmwareVersionBatteryFrame();
		String commandStr = "";
		for(Byte b : command){
			commandStr += b + " ";
		}
		logger.info( commandStr );
/**/
		/*
		
		byte byteRcv = 0x04;
		byte byteRcv2 = (byte) 0xB3;
		int _temp = 0x00;
		
		_temp = (byteRcv << 8)  & 0xFFFF;
		_temp = _temp | byteRcv2  & 0xFFFF; 

		
		logger.info(" --> Result is " + String.format( "0x%02X", _temp) );
		/**/
		
		/*
		ArrayList<Byte> test = StarStimProtocol.buildRenameRadioRequest("NE-ICOGNOS20");
		String str = "";
		for( byte b : test ){
			str += String.format("%02d ", b);
		}
		logger.info("Obtained: " + str);
		/**/
		
		
		// Test StartStreaming/StopStreaming
		// ---------------------------------
/*		
		_deviceManager.openDevice ("00:07:80:64:EB:B6", false);
		boolean result;


		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//_deviceManager.doFirmwareVersionRequest();
		

		// EEG Register
		logger.info("Trying to write an EEG Register");		 
		byte compressionEnableREG = 0x55;
		Byte[] eegWrite = new Byte[2];
		eegWrite[0] = 55;
		eegWrite[1] = 56;
		result = _deviceManager.writeRegister(DeviceManager.StarStimRegisterFamily.EEG_REGISTERS, (byte) 0x52, eegWrite, (byte) 2);
		if( !result ) logger.warning("Error in write EEG Register");

		logger.info("Trying to read an EEG Register");;
		compressionEnableREG = 0x55;
		Byte[] eegRead = new Byte[1];
		eegRead[0] = 0;
		result = _deviceManager.readRegister(DeviceManager.StarStimRegisterFamily.EEG_REGISTERS, (byte) 0x52, eegRead, (byte) 1);
		if( !result ) logger.warning("Error in read EEG Register");
		logger.info("Read from device " + eegRead[0] + " @EEG 0x52");
		result = _deviceManager.readRegister(DeviceManager.StarStimRegisterFamily.EEG_REGISTERS, (byte) 0x53, eegRead, (byte) 1);
		if( !result ) logger.warning("Error in read EEG Register");
		logger.info("Read from device " + eegRead[0] + " @EEG 0x52");


//		// Stim Register
//		logger.info("Trying to write an Stim Register");		 
//		Byte[] stimWrite = new Byte[1];
//		stimWrite[0] = 102;
//		result = _deviceManager.writeRegister(DeviceManager.StarStimRegisterFamily.STIM_REGISTERS, (byte) 0x04, stimWrite, (byte) 1);
//		if( !result ) logger.warning("Error in write STIM Register");
//		
//		logger.info("Trying to read an Stim Register");
//		Byte[] stimRead = new Byte[1];
//		stimRead[0] = 10;
//		result = _deviceManager.readRegister(DeviceManager.StarStimRegisterFamily.STIM_REGISTERS, (byte) 0x04, stimRead, (byte) 1);
//		if( !result ) logger.warning("Error in write STIM Register");
//		logger.info("Read from device " + stimRead[0] + " @STIM 0x00");
		

//		// ACCEL Register
//		logger.info("Trying to write an Accel Register");		 
//		Byte[] accelWrite = new Byte[1];
//		accelWrite[0] = 81;
//		_deviceManager.writeRegister(DeviceManager.StarStimRegisterFamily.ACCELEROMETER_REGISTERS, (byte) 0x03, accelWrite, (byte) 1);
//
//		// SDCARD Register
//		logger.info("Trying to write an SDCard Register");
//		Byte[] sdcardWrite = new Byte[1];
//		sdcardWrite[0] = 81;
//		_deviceManager.writeRegister(DeviceManager.StarStimRegisterFamily.SDCARD_REGISTERS, (byte) 0x02, sdcardWrite, (byte) 1);
		
		
		
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		if( _deviceManager.closeDevice(false) == true ){
			logger.info("Device closed sucessfully");
		}else{
			logger.info("Error in closing device");
		}
		
/**/
		
		
		System.out.println("-- Program Finished --");
	}





}
