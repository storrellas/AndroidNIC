package com.neuroelectrics.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;

import com.neuroelectrics.IScanDiscoveryFinishedHandler;
import com.neuroelectrics.deviceManager.DeviceManager;
import com.neuroelectrics.util.Logger;
import com.neuroelectrics.util.Reference;

public class BluetoothManager implements INICBluetooth{

    // Attributtes
    private static BluetoothManager instance = null;
	private Logger logger;
    
	// Member fields
	private BluetoothAdapter mBtAdapter;
	private ArrayList<String> mPairedDevicesArray;
	private ArrayList<String> mNewDevicesArray;
	
	// Bluetooth variables
	private UUID RFCOMM_UUID = 
			UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); 	// UUID for RFCOMM
	private BluetoothSocket mmSocket  = null;
	private InputStream  mmInStream   = null;
	private OutputStream mmOutStream  = null;
	private boolean      _isConnected = false;
	
	// Handle raised when ScanDiscovery is finished
	private IScanDiscoveryFinishedHandler scanDiscoveryFinishedHandler;
	
	// Intent Filters
	private IntentFilter filterActionFound;
	private IntentFilter filterDiscoveryFinished; 
	
    /*!
     * Singleton instance
     */
    public static BluetoothManager getInstance(Activity activity, IScanDiscoveryFinishedHandler _scanDiscoveryFinishedHandler) {
    	Logger logger = Logger.getInstance();
        if(instance == null) {        
        	logger.info("New object BluetoothManager", Logger.LOG_FILE_ON);
        	instance = new BluetoothManager(activity, _scanDiscoveryFinishedHandler);
        	
        }        
        return instance;
     }


	
	
    /*!
     * Class Constructor
     */
	private BluetoothManager(Activity activity, IScanDiscoveryFinishedHandler _scanDiscoveryFinishedHandler) {
		
		// Create Logger
		logger = Logger.getInstance();
		
		// Store handler for discovery finished
		scanDiscoveryFinishedHandler = _scanDiscoveryFinishedHandler;
		
		// Initialise arrays
		mPairedDevicesArray = new ArrayList<String>();
		mNewDevicesArray   = new ArrayList<String>();
		
		// Get the local Bluetooth adapter
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
				

		
		// Register receivers
		filterActionFound = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filterDiscoveryFinished = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(activity);
		
		
		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				mPairedDevicesArray.add(device.getName() + "\n" + device.getAddress());
			}
		}
		logger.info("Created Bluetooth Manager", Logger.LOG_FILE_ON);
	}
	
	/*!
	 * Registers the receivers
	 */
	public void registerReceiver(Activity activity){
		
		// Register for broadcasts when a device is discovered
		activity.registerReceiver(mReceiver, filterActionFound);

		// Register for broadcasts when discovery has finished
		activity.registerReceiver(mReceiver, filterDiscoveryFinished);
	}

	/*!
	 * Registers the receivers
	 */
	public void unregisterReceiver(Activity activity){
		
		// Register for broadcasts when a device is discovered
		activity.unregisterReceiver(mReceiver);

	}
	
	/**
	 * BroadcastReceiver class handler for BluetoothDevice
	 */
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// If it's already paired, skip it, because it's been listed
				// already
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					mNewDevicesArray.add(device.getName() + "\n" + device.getAddress());
				}
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            	// Launch an event when Bluetooth scan is finished
        		if( scanDiscoveryFinishedHandler != null ) 
        			scanDiscoveryFinishedHandler.onScanNeighborhoodFinished(mPairedDevicesArray, mNewDevicesArray);
			}
        }
    };

	/*!
	 * Returns whether scan is currently ongoing or if it was finished
	 *
	 * \return true on scanOngoing false otherwise
	 */
    public boolean isScanOngoing(){
    	//logger.info( "Scan is ongoing " + mBtAdapter.isDiscovering(), Logger.LOG_FILE_ON );    	
    	return mBtAdapter.isDiscovering();
    }
   	
	/*!
	 * The application calls this function for discovering new devices. The
	 * information is retrieved by calling getNumNotPairedDevices and 
	 * getInfoNotPairedDevice.
	 *
	 * \return A positive value is returned when the scans successfully finishes.
	 * It returns 0 if there where some error during the discovery procedure.
	 */
	public int scanNeighborhood (){
		return scanNeighborhood(false);
	}
    
    public int scanNeighborhood ( boolean onlyUpdatePaired ){
		logger.info( "Clearing remembered devices ..." , Logger.LOG_FILE_ON);
		this.mNewDevicesArray.clear();
		this.mPairedDevicesArray.clear();
		
		
		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
		
		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				mPairedDevicesArray.add(device.getName() + "\n" + device.getAddress());
        		logger.info( "Paired device " + device.getAddress(), Logger.LOG_FILE_ON);
			}
		} else {
    		logger.info( "No devices were found", Logger.LOG_FILE_ON );
		}
		
		// Look for new devices
		if(!onlyUpdatePaired){

			// If we're already discovering, stop it
			logger.info("Starting discovery ...", Logger.LOG_FILE_ON);
			if (mBtAdapter.isDiscovering()) {
				mBtAdapter.cancelDiscovery();
			}

			// Request discover from BluetoothAdapter
			mBtAdapter.startDiscovery();
			
		}
		

		
		return 1;
	}
	
	/*!
	 * It gets the already paired devices in the system.
	 *
	 * \return Number of already paired devices in the system.
	 */
	public int getNumPairedDevices(){
		if( isScanOngoing() ) return -1;
		return mPairedDevicesArray.size();
	}
    
	/*!
	 * It gets the devices that are not paired in the system yet. The information
	 * is updated by calling scanNeighborhood.
	 *
	 * \return Number of devices in the neigborhood but not paired yet.
	 */
	public int getNumNotPairedDevices (){
		
		if( isScanOngoing() ) return -1;
		return mNewDevicesArray.size();
	}
    
	/*!
	 * It gets all the devices that are in the neigborhood. The information
	 * is updated by calling scanNeighborhood.
	 *
	 * \return Number of devices in the neigborhood.
	 */
	public int getNumAllDevices (int authenticated, int remembered, int unknown, int connected){	
		
		if( isScanOngoing() ) return -1;
		return ( getNumPairedDevices() + getNumNotPairedDevices() );
	}
	
	/*!
	 * This function provides the name and the mac address of a Bluetooth device
	 * already paired in the system.
	 *
	 * \param index 0-based index of the device which is desired to retrieve the
	 * information
	 *
	 * \param deviceName output parameter where the device name is copied.
	 *
	 * \param macAddress output parameter where the MAC address is copied.
	 *
	 * \return It return a positive value if the information has been copied to
	 * the output parameters. A zero value is returned if the provided index does
	 * not correspond to a valid paired Bluetooth device. A negative value is
	 * returned if there is some error while retrieving the information.
	 */
    public int getInfoPairedDevice(int index, Reference<String> deviceName, Reference<String> macAddress){
    	int res = 1;
    	
		if( isScanOngoing() ) return 0;
    	
    	if(index >= mPairedDevicesArray.size() ) return 0;
    	    	    	
    	String deviceDataStr = mPairedDevicesArray.get(index);
    	
    	//logger.info("Info for paired device " + deviceDataStr, Logger.LOG_FILE_ON);
    	
    	String[] deviceDataArray = deviceDataStr.split("\n");
    	
    	deviceName.set( deviceDataArray[0] );
    	macAddress.set( deviceDataArray[1] );
    	
    	return res;
    }
    
    /*!
     * This function provides the name and the mac address of a Bluetooth device.
     *
     * \param index 0-based index of the device which is desired to retrieve the
     * information
     *
     * \param deviceName output parameter where the device name is copied.
     *
     * \param macAddress output parameter where the MAC address is copied.
     *
     * \param authenticated input parameter indicating whether to look for authenticated devices.
     *
     * \param remembered input parameter indicating whether to look for remembered devices.
     *
     * \param unknown input parameter indicating whether to look for unknown devices.
     *
     * \param connected input parameter indicating whether to look for connected devices.
     *
     * \return It return a positive value if the information has been copied to
     * the output parameters. A zero value is returned if the provided index does
     * not correspond to a valid paired Bluetooth device. A negative value is
     * returned if there is some error while retrieving the information.
     */
    public int getInfoAllDevices(int index, Reference<String> deviceName, Reference<String> macAddress,
    								int authenticated, int remembered, int unknown, int connected){
   
		if( isScanOngoing() ) return 0;
    	
    	if(index >= (mPairedDevicesArray.size() + mNewDevicesArray.size()) ) return 0;
    	
    	String deviceDataStr = "";
    	if( index < getNumPairedDevices() ){
        	deviceDataStr = mPairedDevicesArray.get(index);    		
    	}else{
    		int conversion = index - mPairedDevicesArray.size();
    		deviceDataStr = mNewDevicesArray.get(conversion);
    	}
    	

    	
    	String[] deviceDataArray = deviceDataStr.split("\n");
    	
    	deviceName.set( deviceDataArray[0] );
    	macAddress.set( deviceDataArray[1] );
    	
    	return 1;
    }
    
    /*!
     * This function provides the name and the mac address of a Bluetooth device
     * that is persent in the neighborhood but is not paired yet. The information
     * is updated by calling scanNeighborhood.
     *
     * \param index 0-based index of the device which is desired to retrieve the
     * information
     *
     * \param deviceName output parameter where the device name is copied.
     *
     * \param macAddress output parameter where the MAC address is copied.
     *
     * \return It return a positive value if the information has been copied to
     * the output parameters. A zero value is returned if the provided index does
     * not correspond to a valid paired Bluetooth device. A negative value is
     * returned if there is some error while retrieving the information.
     */
    public int getInfoNotPairedDevice(int index, Reference<String> deviceName, Reference<String> macAddress){
    	int res = 1;
   
		if( isScanOngoing() ) return 0;
    	
    	if(index >= mNewDevicesArray.size() ) return 0;
    	
    	String deviceDataStr = mNewDevicesArray.get(index);
    	
    	String[] deviceDataArray = deviceDataStr.split("\n");
    	
    	deviceName.set( deviceDataArray[0] );
    	macAddress.set( deviceDataArray[1] );
    	
    	return res;

    }
    
    /*!
     * It performs the authentication procuder for pairing the system with the
     * device whose mac address is provided.
     *
     * \param macAddress MAC address of the remote device.
     *
     * \return It returns a positive value if the pairing is successfully done.
     * It returns zero when the provided mac does not match with any of the
     * devices detected after calling the scanNeighborhood function. A negative
     * value is returned if there were any problem during the authentication
     * procedure.
     */
    public int pairDevice(String macAddress, String pin){
    	
    	Boolean returnValue = false;
		try {
			// Get the BLuetoothDevice object
			BluetoothDevice btDevice = mBtAdapter.getRemoteDevice( macAddress );
			Class btClass = Class.forName("android.bluetooth.BluetoothDevice");
	        Method removeBondMethod = btClass.getMethod("createBond");  
	        returnValue = (Boolean) removeBondMethod.invoke(btDevice);
	        
	        logger.info( "Result from pairing:" + returnValue, Logger.LOG_FILE_ON);
	         
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
		}  
				
    	return (returnValue)?1:0;
    }
    
    /*!
     * It remove the authentication link between the computer an the device whose
     * mac address is provided.
     *
     * \param macAddress MAC address of the remote device to be unpaired.
     *
     * \return It return a positive value if the unpairing is successful. It
     * returns a negative value when the device could not be removed from the
     * system.
     */
    public int removeDevice (String macAddress){

    	Boolean returnValue = false;
		try {
			// Get the BLuetoothDevice object
			BluetoothDevice btDevice = mBtAdapter.getRemoteDevice( macAddress );
			Class btClass = Class.forName("android.bluetooth.BluetoothDevice");
	        Method removeBondMethod = btClass.getMethod("removeBond");  
	        returnValue = (Boolean) removeBondMethod.invoke(btDevice);
	        
	        logger.info( "Result from remove:" + returnValue, Logger.LOG_FILE_ON);
	        
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			logger.info(Logger.stack2string(e), Logger.LOG_FILE_ON);
		} 
    	
    	return (returnValue)?1:0;
    }
    
    public int iniBTSockets(){
    	return 1;
    }

    public void closeBTSockets(){
    	return;
    }
    

    public boolean autoconnect = false;
    
    public int openRFCOMM(String macAddress, Reference<Integer> handle) throws IOException{
		
		BluetoothDevice device = mBtAdapter.getRemoteDevice( macAddress );

    	logger.info("Before creating " + macAddress, Logger.ONLY_ANDROID_CONSOLE);
    	int state = device.getBondState();
    	if(state == BluetoothDevice.BOND_BONDED){
    		logger.info("is bonded", Logger.ONLY_ANDROID_CONSOLE);
    	}
    	if(state == BluetoothDevice.BOND_NONE){
    		logger.info("is NOT bonded", Logger.ONLY_ANDROID_CONSOLE);
    	}
    	if(state == BluetoothDevice.BOND_BONDING){
    		logger.info("Bonding", Logger.ONLY_ANDROID_CONSOLE);
    	}
    	
    	
		// Create socket
		mmSocket = device.createRfcommSocketToServiceRecord( RFCOMM_UUID );
		
   	
		
		// Connect socket
		mmSocket.connect();						
		
    	logger.info("Before creating", Logger.ONLY_ANDROID_CONSOLE);
		
		_isConnected = true;
		
		// Get IO Streams
		mmInStream  = mmSocket.getInputStream();
		mmOutStream = mmSocket.getOutputStream();							
		

		
    	return 1;    	
    }
    
    public int closeRFCOMM(int handle) throws IOException{
    	
		logger.info( "Closing BT Socket ...", Logger.LOG_FILE_ON);

		if(mmSocket != null){ 
			mmSocket.close(); 
			_isConnected = false;
		}

    	
    	return 1;
    }
    
    public int writeRFCOMM( int handle, ArrayList<Byte> buffer, long numberBytes) throws IOException{
    	
    	if(	!_isConnected ){
    		return 0;
    	}
    	
    	if( mmOutStream == null){
    		logger.error("Output stream was not initialised!" );
    		return 0;
    	}
    	
		//logger.info("Writing to device ...", Logger.LOG_FILE_ON);
		byte[] byteArray = new byte[buffer.size()];
		for(int i = 0; i < buffer.size(); i++) byteArray[i] = buffer.get(i);			
		mmOutStream.write( byteArray );
    	
    	return 1;
    }
    

    public int readRFCOMM( int handle, ArrayList<Byte> buffer, long numberBytes, int timeout ) throws IOException{
		int bytes  = 0;

	    byte[] bufferRead = new byte[200];
		
		if( !_isConnected ){
			return 0;
		}
		
		
		if( mmInStream == null ){
    		logger.error("Input stream was not initialised!" );
			return 0;
		}
				
/*
		// Read from the InputStream a certain number of bytes		
		logger.info("Before Read", Logger.LOG_FILE_ON);
		bytes = mmInStream.read(bufferRead, 0, (int) numberBytes);		
		for(int j = 0; j < bytes; j++ ) buffer.add( bufferRead[j] ); 
		logger.info("After Read:" + bytes + " bytes", Logger.LOG_FILE_ON);
/**/


		
		

		// Time out implementation 
		int byteCounter = 0;
		long initTime = SystemClock.currentThreadTimeMillis();
		
		for( int i = 0; i < numberBytes; i++){
			int availableBytes = mmInStream.available();
			if( availableBytes >= 0 ){
				//logger.info("Before Read", Logger.LOG_FILE_ON);
				byte readByte = (byte) mmInStream.read(); 
				//logger.info("After Read", Logger.LOG_FILE_ON);				
				buffer.add( readByte );
				//logger.info("Available " + i + " Bytes:" + availableBytes, Logger.LOG_FILE_ON );
				byteCounter++;					
			}
			// Break conditions
			if( byteCounter == numberBytes ) break;
			if( SystemClock.currentThreadTimeMillis() > ( initTime + 1000 ) ) break;
			
		}

		bytes = buffer.size();
		String threadId = Thread.currentThread().getName() + "(" + Thread.currentThread().getId() + ")";
		String reasonStr = "";
		if( buffer.size() == numberBytes ){
			reasonStr = "RequestedNumber completed. ";
			//logger.info( reasonStr + byteCounter + " " + threadId, Logger.LOG_FILE_ON);
		}else{
			reasonStr = "Timeout! ";
			logger.info( reasonStr + byteCounter + " " + threadId, Logger.LOG_FILE_ON);
		}

/**/			
			
    	return bytes;
    }

}
