package com.icognos.bluetooth;

import java.io.IOException;
import java.util.ArrayList;

import com.icognos.util.Reference;

public interface INICBluetooth {

	/*!
	 * The application calls this function for discovering new devices. The
	 * information is retrieved by calling getNumNotPairedDevices and 
	 * getInfoNotPairedDevice.
	 *
	 * \return A positive value is returned when the scans successfully finishes.
	 * It returns 0 if there where some error during the discovery procedure.
	 */
	public int scanNeighborhood ();
	
	/*!
	 * It gets the already paired devices in the system.
	 *
	 * \return Number of already paired devices in the system.
	 */
	public int getNumPairedDevices();
    
	/*!
	 * It gets the devices that are not paired in the system yet. The information
	 * is updated by calling scanNeighborhood.
	 *
	 * \return Number of devices in the neigborhood but not paired yet.
	 */
	public int getNumNotPairedDevices ();
    
	/*!
	 * It gets all the devices that are in the neigborhood. The information
	 * is updated by calling scanNeighborhood.
	 *
	 * \return Number of devices in the neigborhood.
	 */
	public int getNumAllDevices (int authenticated, int remembered, int unknown, int connected);
	
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
    public int getInfoPairedDevice(int index, Reference<String> deviceName, Reference<String> macAddress);
    
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
    								int authenticated, int remembered, int unknown, int connected);
    
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
    public int getInfoNotPairedDevice(int index, Reference<String> deviceName, Reference<String> macAddress);
    
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
    public int pairDevice(String macAddress, String pin);
    
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
    public int removeDevice (String macAddress);
    
    
    /*!
     * Initialises the necessary ressources for the BT
     * 
     * \returns 1 on success 0 on failure
     */
    public int iniBTSockets();

    /*!
     * Removes the ressrouces for BT communication
     */
    public void closeBTSockets();
    
    /*!
     * Opens up a communication channel with a specific device
     * 
     * \param macAddress String in form of "00:07:80:64:EB:B6"
     * 
     * \param handle 
     * 
     * \returns 1 on success 0 on failure
     */
    public int openRFCOMM(String macAddress, Reference<Integer> handle) throws IOException;
        
    /*!
     * Closes the RFCOMM communcation channel
     */
    public int closeRFCOMM(int handle) throws IOException;
    
    
    /*!
     * Writes buffer to the BT device 
     * 
     * \param handle id of the BT socket to be used
     * 
     * \param numberBytes number of bytes to be written to the socket
     * 
     * \returns 1 on success 0 on failure
     */
    public int writeRFCOMM( int handle, ArrayList<Byte> buffer, long numberBytes) throws IOException;
    
    /*!
     * Reads the incoming data from the BT device
     * 
     * \param handle id of the BT socket to be used
     * 
     * \param numberBytes number of bytes to be written to the socket
     * 
     * \param timeout timeNecessary to abort operation
     * 
     * \returns 1 on success 0 on failure
     */
    public int readRFCOMM( int handle, ArrayList<Byte> buffer, long numberBytes, int timeout) throws IOException;
	
}
