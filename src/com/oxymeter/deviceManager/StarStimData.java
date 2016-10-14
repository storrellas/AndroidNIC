package com.icognos.deviceManager;

import java.util.ArrayList;


import com.icognos.util.Logger;

public class StarStimData {

    private Logger logger;
	//  -- Attributtes --
	//  -----------------
	
    /*!
     * \property StarStimData::_deviceStatus
     *
     * It stores the received device's status.
     */
    private int _deviceStatus;

    /*!
     * \property StarStimData::_deviceError
     *
     * It stores the received device's error status.
     */
    private int _deviceError;

    /*!
     * \property StarStimData::_isCommandToggled
     *
     * It stores the boolean that indicates whether the command bit has been
     * toggled.
     */
    private boolean _isCommandToggled;

    /*!
     * \property StarStimData::_sdCardRecording
     *
     * It stores the boolean that indicates whether the SD card is being written.
     */
    private boolean _isSDCardRecording;

    /*!
     * \property StarStimData::_isEEGDataPresent
     *
     * It informs whether the current frame contains the EEG data block.
     */
    private boolean _isEEGDataPresent;

    /*!
     * \property StarStimData::_isEEGConfigPresent
     *
     * It informs whether the current frame contains the Configuration EEG
     * register data block.
     */
    private boolean _isEEGConfigPresent;

    /*!
     * \property StarStimData::_isStimDataPresent
     *
     * It informs whether the current frame contains the Stimulation data
     * block.
     */
    private boolean _isStimDataPresent;

    /*!
     * \propertu StarStimData::_isStimImpedance
     *
     * It informs whether the current frame contains the Impedance data.
     */
    private boolean _isStimImpedancePresent;

    /*!
     * \property StarStimData::_isStimConfigPresent
     *
     * It informs whether the current frame contains the Configuration
     * stimulation register data block.
     */
    private boolean _isStimConfigPresent;

    /*!
     * \property StarStimData::_isAccelerometerPresent
     *
     * It informs whether the accelerometer data block is present in the
     * current frame.
     */
    private boolean _isAccelerometerPresent;

    /*!
     * \property StarStimData::_isFirmwareVersionPresent
     *
     * It informs whether the firmware version data block is present in the
     * current frame.
     */
    private boolean _isFirmwareVersionPresent;

    /*!
     * \property StarStimData::_isBatteryPresent
     *
     * It informs whether the battery data block is present in the
     * current frame.
     */
    private boolean _isBatteryPresent;

    /*!
     * \property StarStimData::_isEEGChPresent
     *
     * It keeps if each of the 8 channels are present in the EEG data block.
     */
    //private boolean _isEEGChPresent[32];
    private boolean[] _isEEGChPresent;

    /*!
     * \property StarStimData::_isStimChPresent
     *
     * It keeps if each of the 8 channels are present in the StarStim data
     * block.
     */
    //private boolean _isStimChPresent[8];
    private boolean[] _isStimChPresent;
    
    /*!
     * \property StarStimData::_isStimChPresent
     *
     * It keeps if each of the 8 channels are present in the StarStim data
     * block.
     */
    //private boolean _isStimImpedanceChPresent[8];
    private boolean[] _isStimImpedanceChPresent;
    
    /*!
     * \property StarStimData::eegDataArray
     *
     * It contains the EEG Samples in the present beacon
     */
    private ArrayList<ChannelData> _eegDataArray;

    /*!
     * \property StarStimData::_eegChecksum
     *
     * It keeps the EEG checksum.
     */
    private char _eegChecksum;

    /*!
     * \property StarStimData::_eegStamp
     *
     * It keeps the EEG stamp.
     */
     private int   _eegStamp;

    /*!
     * \property StarStimData::_eegStartAddress
     *
     * It stores the address of the first EEG register that appears in the
     * received frame.
     */
    private int _eegStartAddress;

    /*!
     * \property StarStimData::_eegNumRegs
     *
     * It stores the number of EEG registers that are present in the received
     * frame.
     */
    private int _eegNumRegs;

    /*!
     * \property StarStimData::_eegReg
     *
     * It is a vector of bytes that contains the value of the EEG configuration
     * registers that are present in the received frame
     */
    //private char _eegReg[EEG_NUM_REGS];
    private char[] _eegReg;
    
    /*!
     * \property StarStimData::_stimData
     *
     * It contains the Stimulation samples of the received frame.
     */
    private ChannelData _stimData;

    /*!
     * \property StarStimData::_stimImpedance
     *
     * It contains the Impedance samples of the received frame.
     */
    private ChannelData _stimImpedance;

    /*!
     * \property StarStimData::_stimStartAddress
     *
     * It stores the address of the first stimulation register that appears in
     * the received frame.
     */
    private char _stimStartAddress;

    /*!
     * \property StarStimData::_stimNumRegs
     *
     * It stores the number of stimulation registers that are present in the
     * received frame.
     */
    private char _stimNumRegs;

    /*!
     * \property StarStimData::_stimReg
     *
     * It is a vector of bytes that contains the value of the stimulation
     * configuration registers that are present in the received frame
     */
    //private char _stimReg[STM_NUM_REGS];
    private char[] _stimReg;
    
    /*!
     * \property StarStimData::_accelerometer
     *
     * It stores the accelerometer value present in the received frame.
     */
    private ChannelData _accelerometer;

    /*!
     * \property StarStimData::_firmwareVersion
     *
     * It stores the firmware version present in the received frame.
     */
    private int _firmwareVersion;

    /*!
     * \property StarStimData::is100SPS
     *
     * It stores whether the current device is 1000SPS
     */
    private int _is1000SPS;

    /*!
     * \property StarStimData::nSamples
     *
     * It stores the number of samples in the current beacon
     */
    private int _nSamples;

    /*!
     * \property StarStimData::_battery
     *
     * It stores the battery value present in the received frame.
     */
    private int _battery;
    
    /*!
     * \property StarStimData::NumEEGConfigReg
     *
     * Number of EEG configuration registers.
     */
    public static final char NumEEGConfigReg = DeviceManager.EEG_NUM_REGS;

    /*!
     * \property StarStimData::NumStimConfigReg
     *
     * Number of stimulation configuration registers.
     */
    public static final char NumStimConfigReg = DeviceManager.STM_NUM_REGS;

    /*!
     * \property StarStimData::NumAccelerometerConfigReg
     *
     * Number of accelerometer configuration registers.
     */
    public static final char NumAccelerometerConfigReg = DeviceManager.ACCEL_NUM_REGS;

    /*!
     * \property StarStimData::NumSDCardConfigReg
     *
     * Number of SDCard configuration registers.
     */
    public static char NumSDCardConfigReg = DeviceManager.SDCARD_NUM_REGS;
	
    //  -- METHODS --
    // --------------
	
    /*!
     * Public constructor
     */
    public StarStimData(){
    	logger = Logger.getInstance();
    	
       	_isStimChPresent = new boolean[8];
    	_isEEGChPresent  = new boolean[32];
    	_isStimImpedanceChPresent  = new boolean[8];
    	_eegReg = new char[DeviceManager.EEG_NUM_REGS];
    	_stimReg = new char[DeviceManager.STM_NUM_REGS];
    
    	_stimData      = new ChannelData();
    	_stimImpedance = new ChannelData();
    	_accelerometer = new ChannelData();
    	
    	_eegDataArray = new ArrayList<ChannelData>(); 
    	
    	this.empty();
    }
    
    /*!
     * This function resets all the fields so the object can be reused.
     */
    public void empty(){
        _isEEGDataPresent = false;
        _isEEGConfigPresent = false;
        _isStimDataPresent = false;
        _isStimConfigPresent = false;
        _isStimImpedancePresent = false;
        _isAccelerometerPresent = false;
        _isFirmwareVersionPresent = false;
        _deviceStatus = 0;
        _deviceError = 0;
        _isCommandToggled = false;
        _isSDCardRecording = false;
        //_statusByte1 = 0;
        //_statusByte0 = 0;
        for (int i = 0; i < 32; i++)
        {
            _isEEGChPresent[i] = false;
        }
        _eegChecksum = 0;
        _eegStamp = 0;
        _eegStartAddress = 0;
        _eegNumRegs = 0;
        for (int i = 0; i < NumEEGConfigReg; i++)
        {
            _eegReg[i] = 0;
        }
        //_stimChInfo = 0;
        _stimData.setChannelInfo(0);
        for (int i = 0; i < 8; i++)
        {
            //_stimData[i] = 0;
            _stimData.data()[i] = 0;
            _isStimChPresent[i] = false;
        }
        _stimStartAddress = 0;
        _stimNumRegs = 0;
        for (int i = 0; i < NumStimConfigReg; i++)
        {
            _stimReg[i] = 0;
        }
        //_stimImpedanceChInfo = 0;
        _stimImpedance.setChannelInfo(0);
        for (int i = 0; i < 8; i++)
        {
            _stimImpedance.data()[i] = 0;
            _isStimImpedanceChPresent[i] = false;
        }

        for (int i = 1; i < 4; i++)
            _accelerometer.data()[i] = 0;

        _firmwareVersion = 0;
        _is1000SPS = 0;
        _battery = 0;
    }

    /*!
     * It compares the two StarStimData instances.
     *
     * \param ssd1 First StarStimData instance to be compared.
     * \param ssd2 Second StarStimData instance to be compared.
     *
     * \return True if all the fields of both StarStimData instances have the
     * same values.
     */
    public static boolean compare (StarStimData ssd1, StarStimData ssd2){
    	// This function is empty as it is not used by anyone
    	return true;
    }

    /*!
     * It returns the value of all the filds into a QString.
     *
     * \return Formatted QString with all StarStim field values.
     */
    //public String toString ();


    /*!
     * It returns the device status value.
     *
     * \return The device status.
     */
    public int deviceStatus(){
    	return _deviceStatus;
    }

    /*!
     * It returns the device error.
     *
     * \return The device error.
     */
    public int deviceError(){
    	return _deviceError;
    }

    /*!
     * It returns if the acknowledge command bit is toggled.
     *
     * \return True if the acknowledge command bit has toggled regarding the
     * previous data.
     */
    public boolean isCommandToggled(){
        return _isCommandToggled;
    }

    /*!
     * It returns if the SD card is being recorded.
     *
     * \return True if the SD card is being recorded.
     */
    public boolean isSDCardRecording(){
        return _isSDCardRecording;
    }

    /*!
     * It returns whether the current StarStim data has information regarding
     * EEG.
     *
     * \return True if EEG data is present in the current enzo3G/StarStim
     * data, false otherwise.
     */
    public boolean isEEGDataPresent(){
        return _isEEGDataPresent;
    }

    /*!
     * It returns whether the current enzo3G/StarStim data has information
     * regarding EEG configuration registers.
     *
     * \return True if EEG configuration data is present in the current
     * enzo3G/StarStim data, false otherwise.
     */
    public boolean isEEGConfigPresent(){
        return _isEEGConfigPresent;
    }

    /*!
     * It returns whether the current StarStim data has information regarding
     * stimulation data.
     *
     * \return True if stimulation data is present in the current StarStim
     * data, false otherwise.
     */
    public boolean isStimDataPresent(){
        return _isStimDataPresent;
    }

    /*!
     * It returns whether the current StarStim data has information regarding
     * Impedance measurements.
     *
     * \return True if Impedance data is present in the current StarStim data,
     * false otherwise.
     */
    public boolean isStimImpedancePresent(){
        return _isStimImpedancePresent;
    }

    /*!
     * It returns whether the current StarStim data has information regarding
     * stimulation configuration registers
     *
     * \return True if stimulation configuration data is present in the current
     * StarStim data, false otherwise.
     */
    public boolean isStimConfigPresent(){
        return _isStimConfigPresent;
    }

    /*!
     * It returns whether the current enzo3G/StarStim data has information
     * regarding the Accelerometer.
     *
     * \return True if the data from the Accelerometer is present in the
     * current enzo3G/StarStim data, false otherwise.
     */
    public boolean isAccelerometerPresent(){
        return _isAccelerometerPresent;
    }

    /*!
     * It returns whether the current enzo3G/StarStim data has information
     * regarding the firmware version.
     *
     * \return True if the data from the firmware version is present in the
     * current enzo3G/StarStim data, false otherwise.
     */
    public boolean isFirmwareVersionPresent(){
        return _isFirmwareVersionPresent;
    }

    /*!
     * It returns whether the current enzo3G/StarStim data has information
     * regarding the battery charging level.
     *
     * \return True if the data from the battery is present in the current
     * enzo3G/StarStim data, false otherwise.
     */
    public boolean isBatteryPresent(){
        return _isBatteryPresent;
    }

    /*!
     * It returns a pointer to a vector of booleans. The values inform whether
     * the corresponding channel is present in the EEG data. The first value in
     * the vector corresponds to channel 0.
     *
     * \return Pointer to the boolean vector with the channels present in the
     * EEG data.
     */
    public boolean[] isEEGChPresent(){    	
    	return _isEEGChPresent;
    }

    /*!
     * It returns a pointer to a vector of booleans. The values inform whether
     * the corresponding channel is present in the stimulation data. The first
     * value in the vector corresponds to channel 0.
     *
     * \return Pointer to the boolean vector with the channels present in the
     * stimulation data.
     */
    public boolean[] isStimChPresent(){
        return _isStimChPresent;
    }

    /*!
     * It returns a pointer to a vector of booleans. The values inform whether
     * the corresponding channel is present in the Impedance data. The first
     * value in the vector corresponds to channel 0.
     *
     * \return Pointer to the boolean vector with the channels present in the
     * impedance data.
     */
    public boolean[] isStimImpedanceChPresent(){
        return _isStimImpedanceChPresent;
    }

    /*!
     * It returns the status byte 0 which holds the information of the type of
     * data that is present in the enzo3G/StarStim data.
     *
     * \return The status byte 0.
     */
    //public char statusByte0();

    /*!
     * It returns the status byte 1 which holds the information of the status
     * of the device and the acknowledge of the previous actions.
     *
     * \return The status byte 1.
     */
    //public char statusByte1();

    /*!
     * It returns the EEG channel info byte that is sent before the EEG data to
     * know which channels are reported.
     *
     * \return The EEG channel info byte.
     */
    public int eegChInfo(){
        // NOTE: All elements in _eegDataArray.channelInfo() should be equal
        return _eegDataArray.get(0).channelInfo();
    }

    /*!
     * It returns the EEG checksum byte that is sent after the EEG data to
     * check the data integrity.
     *
     * \return The EEG checksum.
     */
    public char eegChecksum (){
        return _eegChecksum;
    }

    /*!
     * It returns the EEG stamp byte that is sent after the EEG data to check
     * if there have been lost packets.
     *
     * \return The EEG stamp.
     */
    public int eegStamp (){
        return _eegStamp;
    }

    /*!
     * It returns the start address of the EEG configuration registers that are
     * send in the enzo3G/StarStim beacon frame.
     *
     * \return EEG start address byte.
     */
    public int eegStartAddress(){
        return _eegStartAddress;
    }

    /*!
     * It returns the number of EEG configuration registers that are sent in
     * the enzo3G/StarStim beacon frame.
     *
     * \return Number of EEG configuration registers in the enzo3G/StarStim
     * beacon frame.
     */
    public int eegNumRegs(){
        return _eegNumRegs;
    }

    /*!
     * It returns the pointer to the EEG configuration registers that are sent
     * in the enzo3G/StarStim beacon frame.
     *
     * \return Pointer to the EEG configuration registers in the
     * enzo3G/StarStim beacon frame.
     */
    public char[] eegReg(){
        return _eegReg;
    }

    /*!
     * It returns the stimulation channel info byte that is sent before the
     * stimulation data to know which channels are reported.
     *
     * \return The stimulation channel info byte.
     */
    public int stimChInfo(){
        return _stimData.channelInfo();
    }

    /*!
     * It returns the impedance channel info byte that is sent before the
     * impedance data to know which channels are reported.
     *
     * \return The impedance channel info byte.
     */
    public int stimImpedanceChInfo(){
        return _stimImpedance.channelInfo();
    }

    /*!
     * It returns the start address of the stimulation configuration registers
     * that are send in the StarStim beacon frame.
     *
     * \return Stimulation start address byte.
     */
    public char stimStartAddress(){
        return _stimStartAddress;
    }

    /*!
     * It returns the number of stimulation configuration registers that are
     * send in the StarStim beacon frame.
     *
     * \return Number of stimulation configuration registers in the StarStim
     * beacon frame.
     */
    public char stimNumRegs(){
        return _stimNumRegs;
    }

    /*!
     * It returns the pointer to the stimulation configuration registers that
     * are send in the StarStim beacon frame.
     *
     * \return Pointer to the stimulation configuration registers in the
     * StarStim beacon frame.
     */
    public char[] stimReg(){
        return _stimReg;
    }

    /*!
     * It returns the acceleroemter value sent in the enzo3G/StarStim beacon
     * frame.
     *
     * \return Acceleremoter value in the enzo3G/StarStim beacon frame.
     */
    public ChannelData accelerometer(){
        return _accelerometer;
    }

    /*!
     * It returns the firmware version value sent in the enzo3G/StarStim beacon
     * frame.
     *
     * \return firmwareVersion value in the enzo3G/StarStim beacon frame.
     */
    public int firmwareVersion(){
    	return _firmwareVersion;
    }

    /*!
     * returns whether the device is 1000 SPS
     *
     * \param value The 1000SPS value.
     */
    public int get1000SPS(){
    	return _is1000SPS;
    }

    /*!
     * It returns the battery value sent in the enzo3G/StarStim beacon
     * frame.
     *
     * \return Battery value in the enzo3G/StarStim beacon frame.
     */
    public int battery(){
        return _battery;
    }

    /*!
     * It returns the stimulation data sample.
     *
     * \return Stimulation data of the channels that are sent in the beacon
     * frame.
     */
    public ChannelData stimulationData(){
        return _stimData;
    }

    /*!
     * It returns the EEG Data Sample Array
     *
     * \return EEG Data Array of the channels
     */
    public ArrayList<ChannelData> eegDataArray(){
        return _eegDataArray;
    }

    /*!
     * It returns the number of samples present in the beacon
     *
     * \return Number of samples present in the beacon
     */
    public int nSamples(){
        return _nSamples;
    }

    /*!
     * It returns the impedance data sample.
     *
     * \return Impedance data of the channels that are sent in the beacon
     * frame.
     */
    public ChannelData stimImpedanceData(){
        return _stimImpedance;
    }

    /*!
     * It sets the device status value.
     *
     * \param deviceStatus The device status value.
     */
    public void deviceStatus(int value){
        _deviceStatus = value;
    }

    /*!
     * It sets the device error value.
     *
     * \param deviceError The device error value.
     */
    public void deviceError(int value){
    	_deviceError = value;
    }

    /*!
     * It sets whether the current enzo3G/StarStim beacon frame has toggled
     * the acknowledge command bit.
     *
     * \param status True if the acknowledge command bit is toggled, false
     * otherwise.
     */
    public void isCommandToggled(boolean status){
    	_isCommandToggled = status;
    }

    /*!
     * It sets whether the SD Card is recording.
     *
     * \param status True if the SD card is recording, false otherwise.
     */
    public void isSDCardRecording(boolean status){
    	_isSDCardRecording = status;
    }

    /*!
     * It sets whether the EEG data is present in the current enzo3G/StarStim
     * beacon frame.
     *
     * \param status True if the EEG data is present, false otherwise.
     */
    public void isEEGDataPresent(boolean status){
    	_isEEGDataPresent = status;
    }

    /*!
     * It sets whether the EEG configuration data is present in the current
     * enzo3G/StarStim beacon frame.
     *
     * \param status True if the EEG configuration data is present, false
     * otherwise.
     */
    public void isEEGConfigPresent(boolean status){
    	_isEEGConfigPresent = status;
    }

    /*!
     * It sets whether the stimulation data is present in the current StarStim
     * beacon frame.
     *
     * \param status True if the stimulation data is present, false otherwise.
     */
    public void isStimDataPresent(boolean status){
    	_isStimDataPresent = status;
    }

    /*!
     * It sets whether the impedance data is present in the current StarStim
     * beacon frame.
     *
     * \param status True if the impedance data is present, false otherwise.
     */
    public void isStimImpedancePresent(boolean status){
    	_isStimImpedancePresent = status;
    }

    /*!
     * It sets whether the stimulation configuration data is present in the
     * current StarStim beacon frame.
     *
     * \param status True if the stimulation configuration data is present,
     * false otherwise.
     */
    public void isStimConfigPresent(boolean status){
    	_isStimConfigPresent = status;
    }

    /*!
     * It sets whether the accelerometer data is present in the current
     * enzo3G/StarStim beacon frame.
     *
     * \param status True if the accelerometer data is present, false
     * otherwise.
     */
    public void isAccelerometerPresent(boolean status){
    	_isAccelerometerPresent = status;
    }

    /*!
     * It sets whether the firmware version data is present in the current
     * enzo3G/StarStim beacon frame.
     *
     * \param status True if the firmware version data is present, false
     * otherwise.
     */
    public void isFirmwareVersionPresent(boolean status){
    	_isFirmwareVersionPresent = status;
    }

    /*!
     * It sets whether the battery data is present in the current
     * enzo3G/StarStim beacon frame.
     *
     * \param status True if the battery data is present, false otherwise.
     */
    public void isBatteryPresent (boolean status){
    	_isBatteryPresent = status;
    }
    

    /*!
     * It sets whether the EEG data from an specific channel is present in the
     * enzo3G/StarStim beacon frame.
     *
     * \param index Zero-based index of the channel which its presence status
     * is set.
     *
     * \param status True if the channel is present in the EEG data, false
     * otherwise.
     */
    public void isEEGChPresent(int index, boolean value){
        if (index >= 8) index = 7;
        _isEEGChPresent[index] = value;
    }

    /*!
     * It sets whether the stimulation data from an specific channel is present
     * in the StarStim beacon frame.
     *
     * \param index Zero-based index of the channel which its presence status
     * is set.
     *
     * \param status True if the channel is present in the stimulation data,
     * false otherwise.
     */
    public void isStimChPresent(int index, boolean value){
        if (index >= 8) index = 7;
        _isStimChPresent[index] = value;
    }

    /*!
     * It sets whether the impedance data from an specific channel is present
     * in the StarStim beacon frame.
     *
     * \param index Zero-based index of the channel which its presence status
     * is set.
     *
     * \param status True if the channel is present in the impedance data,
     * false otherwise.
     */
    public void isStimImpedanceChPresent(int index, boolean value){
        if (index >= 8) index = 7;
        _isStimImpedanceChPresent[index] = value;
    }


//  /*!
//  * It sets the value of the status byte 0.
//  *
//  * \param value Status byte 0.
//  */
// void statusByte0(unsigned char value);

// /*!
//  * It sets the value of the status byte 1.
//  *
//  * \param value Status byte 1.
//  */
// void statusByte1(unsigned char value);

    /*!
     * It sets the EEG channel info.
     *
     * \param chInfo The EEG channel info.
     */
    public void eegChInfo(int value){
    	
        for(int i = 0; i < _eegDataArray.size(); i++)
        {
            _eegDataArray.get(i).setChannelInfo(value);
        }


        for (int i = 0; i < 32; i++)
        {
            _isEEGChPresent[i] = ((value & (1 << i)) > 0);
        }
        
    }

    /*!
     * It sets the EEG data corresponding to a specific channel.
     *
     * \param index Zero-based index of the channel.
     *
     * \param value EEG data value of the channel.
     *
     * \param sample number of sample to be stored
     */
    public void eegData(int channel, int value, int sample){
            	
    	if (channel >= 32) channel = 31;
        ChannelData channelData = _eegDataArray.get(sample);
        channelData.data()[channel] = value;
        
        _eegDataArray.set(sample, channelData);
        

    }

    /*!
     * Sets the number of samples in the current Beacon
     *
     * \param nSamples number of samples in the current beacon
     */
    public void nSamples( int value ){
        
    	
    	_nSamples = value;

        _eegDataArray.clear();
        /*
        ChannelData channelData = new ChannelData();
        channelData.setChannelInfo(0);
        for (int i = 0; i < 32; i++)
        {
            channelData.data()[i] = 0;
        }
		/**/


        for(int i = 0; i < value; i++){            
            _eegDataArray.add( new ChannelData() );
        }
                
    }


    /*!
     * It sets the EEG checksum.
     *
     * \param value The EEG checksum.
     */
    public void eegChecksum (char value){
        _eegChecksum = value;
    }

    /*!
     * It sets the EEG stamp.
     *
     * \param value The EEG stamp.
     */
    public void eegStamp (int value){
        _eegStamp = value;
    }

    /*!
     * It sets the start address of the EEG configuration registers that are
     * present in the enzo3G/StarStim beacon frame.
     *
     * \param startAddress The EEG start address.
     */
    public void eegStartAddress(int value){
        _eegStartAddress = value;
    }

    /*!
     * It sets the number of EEG configuration registers that are present in
     * the enzo3G/StarStim beacon frame.
     *
     * \param numRegs The number of EEG configuration registers.
     */
    public void eegNumRegs(int value){
        _eegNumRegs = value;
    }

    /*!
     * It sets the value of the EEG configuration registers that are present in
     * the enzo3G/StarStim beacon frame.
     *
     * \param address The EEG register address.
     *
     * \param value The EEG register value.
     */
    public void eegReg(int index, char value){
        if (index >= NumEEGConfigReg) index = NumEEGConfigReg - 1;
        _eegReg[index] = value;
    }

    /*!
     * It sets the stimulation channel info.
     *
     * \param chInfo The stimulation channel info.
     */
    public void stimChInfo(char value){
        //_stimChInfo = value;
        _stimData.setChannelInfo(value);
        for (int i = 0; i < 8; i++)
        {
            _isStimChPresent[i] = ((value & (1 << i)) > 0);
        }
    }

    /*!
     * It sets the impedance channel info.
     *
     * \param chInfo The impedance channel info.
     */
    public void stimImpedanceChInfo(char value){
        //_stimImpedanceChInfo = value;
        _stimImpedance.setChannelInfo(value);
        for (int i = 0; i < 8; i++)
        {
            _isStimImpedanceChPresent[i] = ((value & (1 << i)) > 0);
        }
    }

    /*!
     * It sets the stimulation data corresponding to a specific channel.
     *
     * \param index Zero-based index of the channel.
     *
     * \param value The stimulation data value of the channel.
     */
    public void stimData(int index, int value){
        if (index >= 8) index = 7;
        //_stimData[index] = value;
        _stimData.setData(index, value);
    }

    /*!
     * It sets the impedance data corresponding to a specific channel.
     *
     * \param index Zero-based index of the channel.
     *
     * \param value The impedance data value of the channel.
     */
    public void stimImpedance(int index, int value){
        if (index >= 8) index = 7;
        _stimImpedance.data()[index] = value;
    }

    /*!
     * It sets the start address of the stimulation configuration registers
     * that are present in the StarStim beacon frame.
     *
     * \param startAddress The stimulation start address.
     */
    public void stimStartAddress(char value){    	    	
        _stimStartAddress = value;        
    }

    /*!
     * It sets the number of stimulation configuration registers that are
     * present in the StarStim beacon frame.
     *
     * \param numRegs The number of stimulation configuration registers.
     */
    public void stimNumRegs(char value){
        _stimNumRegs = value;
    }

    /*!
     * It sets the value of the stimulation configuration registers that are
     * present in the StarStim beacon frame.
     *
     * \param address The stimulation register address.
     *
     * \param value The stimulation register value.
     */
    public void stimReg(int address, char value){
        if (address >= NumStimConfigReg) address = NumStimConfigReg - 1;
        _stimReg[address] = value;
    }

    /*!
     * It sets the value of the accelerometer data that is present in the
     * enzo3G/StarStim beacon frame.
     *
     * \param index The index of the 3-dimension vector
     *
     * \param value The accelerometer value.
     */
    public void accelerometer(int index, int value){
        //Conversion to mg
        int convertedValue = (int) (value*3.9);

        //qDebug()<<"StarStimData::accelerometer"<<value<<convertedValue<<convertedValue*9.80665;

        //Conversion to mm/s^2
        convertedValue = (int) (convertedValue*9.80665);

        _accelerometer.data()[index] = convertedValue;
    }

    /*!
     * It sets the value of the firmware version data that is present in the
     * enzo3G/StarStim firmware version frame.
     *
     * \param value The firmwareVersion value.
     */
    public void firmwareVersion(int value){
        _firmwareVersion = value;
    }

    /*!
     * It sets the value of the 1000SPS variable which determines whether the system
     * runs at 1000SPS
     *
     * \param value The 1000SPS value.
     */
    public void set1000SPS(int value){
        _is1000SPS = value;
    }

    /*!
     * It sets the value of the battery data that is present in the
     * enzo3G/StarStim beacon frame.
     *
     * \param value The battery value.
     */
    public void battery(int value){
        _battery = value;
    }

    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Logger logger = Logger.getInstance();
		
		ArrayList<Character> list = new ArrayList<Character>();
		
		list.add('a');
		list.add('b');
		list.add('c');
		
		String str = "Contents: ";
		for(Character c : list){
			str += c + " ";
		}
		logger.info(str, Logger.LOG_FILE_ON);
		
		Character aux = list.get(1);
		aux = new Character('e');
		
		list.set(1, aux);
		
		str = "Contents: ";
		for(Character c : list){
			str += c + " ";
		}
		logger.info(str, Logger.LOG_FILE_ON);
		
		
	}

}
