package com.icognos.deviceManager;

import java.util.ArrayList;
import java.util.Arrays;

import android.util.Log;

import com.icognos.util.Logger;

public class StarStimProtocol {

    private Logger logger;
    
	//  -- Attributtes --
	//  -----------------
    
    private final static int SFD_LENGTH = 3;
    
    /*!
     * \property StarStimProtocol::SFD_0
     *
     * First byte of the Start of Frame Delimiter.
     */
    private final static int SFD_0 = 'S';

    /*!
     * \property StarStimProtocol::SFD_1
     *
     * Second byte of the Start of Frame Delimiter.
     */
    private final static int SFD_1 = 'O';

    /*!
     * \property StarStimProtocol::SFD_2
     *
     * Third byte of the Start of Frame Delimiter.
     */
    private final static int SFD_2 = 'F';

    /*!
     * \property StarStimProtocol::EFD_0
     *
     * First byte of the End of Frame Delimiter.
     */
    private final static int EFD_0 = 'E';

    /*!
     * \property StarStimProtocol::EFD_1
     *
     * Second byte of the End of Frame Delimiter.
     */
    private final static int EFD_1 = 'O';

    /*!
     * \property StarStimProtocol::EFD_2
     *
     * Third byte of the End of Frame Delimiter.
     */
    private final static int EFD_2 = 'F';
    
    /*!
     * \property _firmwareVersion
     *
     * Firmware version of the connected device
     */
    int _firmwareVersion;

    /*!
     * \property _isStimulating
     *
     * Booleand indicating if the device is stimulating
     */
    private boolean _isStimulating;

    /*!
     * \enum StatusProtocol
     *
     * State machine values
     */
    private enum StatusProtocol
    {
        ST_IDLE,
        ST_LENGTH0,
        ST_LENGTH1,
        ST_STATUS_0,
        ST_STATUS_1,
        ST_EEG_DATA,
        ST_EEG_CH_INFO_0,
        ST_EEG_CH_INFO_1,
        ST_EEG_CH_INFO_2,
        ST_EEG_CH_INFO_3,
        ST_EEG_DATA_RAW_MSB,
        ST_EEG_DATA_RAW_MSBLSB,
        ST_EEG_DATA_RAW_LSB,
        ST_EEG_COMPRESSED_16BIT_MSB,
        ST_EEG_COMPRESSED_16BIT_LSB,
        ST_EEG_COMPRESSED_12BIT_S1,
        ST_EEG_COMPRESSED_12BIT_S1S2,
        ST_EEG_COMPRESSED_12BIT_S2,
        ST_EEG_CHECKSUM,
        ST_EEG_STAMP,
        ST_EEG_STAMP_2,
        ST_EEG_STAMP_3,
        ST_EEG_STAMP_4,
        ST_EEG_CONFIG,
        ST_EEG_CONFIG_NUM_REGS,
        ST_EEG_CONFIG_REGS,
        ST_STIM_DATA,
        ST_STIM_DATA_MSB,
        ST_STIM_DATA_LSB,
        ST_STIM_CONFIG,
        ST_STIM_CONFIG_NUM_REGS,
        ST_STIM_CONFIG_REGS,
        ST_STIM_IMPEDANCE,
        ST_STIM_IMPEDANCE_DATA_MSB,
        ST_STIM_IMPEDANCE_DATA_MSBLSB,
        ST_STIM_IMPEDANCE_DATA_LSBMSB,
        ST_STIM_IMPEDANCE_DATA_LSB,
        ST_BATTERY,
        ST_BATTERY_1,
        ST_BATTERY_2,
        ST_BATTERY_3,
        ST_ACCELEROMETER,
        ST_ACCELEROMETER_1,
        ST_ACCELEROMETER_2,
        ST_ACCELEROMETER_3,
        ST_ACCELEROMETER_4,
        ST_ACCELEROMETER_5,
        ST_FIRMWARE,
        ST_FIRMWARE_1,
        ST_EOF,
        ST_EOF_1,
        ST_EOF_2,
        ST_EOF_3
    } ;

    /*!
     * \enum EEGCompressionType
     *
     * Different configurations for compression
     */
    public enum EEGCompressionType {
    	EEG_NO_COMPRESSION(0), EEG_16BIT_COMPRESSION(1), EEG_12BIT_COMPRESSION(2);
        
    	private int numVal;

    	EEGCompressionType(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }
    } ;
    
    /*!
     * \\property StarStimProtocol::StarStimData
     *
     * The parsed data is stored in that object.
     */
    public StarStimData _starStimData;

    /*!
     * \property StarStimProtocol::_dataLength
     *
     * It stores the length of the data frame.
     */
    private int _dataLength;

    /*!
     * \property StarStimProtocol::_state
     *
     * It keeps the value of the current state of the parser's state machine.
     */
    private StatusProtocol _state;

    /*!
     * \property StarStimProtocol::_previousCommandToggle
     *
     * It keeps track of the bit that indicates if the last command sent was
     * processed.
     */
    private char _previousCommandToggle;

    /*!
     * \property StarStimProtocol::_isEEGDataProcessed
     *
     * It marks whether the EEG data block has been processed or not.
     */
    private boolean _isEEGDataProcessed;

    /*!
     * \property StarStimProtocol::_isEEGConfigProcessed
     *
     * It marks whether the EEG configuration block has been processed or not.
     */
    private boolean _isEEGConfigProcessed;

    /*!
     * \property StarStimProtocol::_isStimDataProcessed
     *
     * It marks whether the StarStim data block has been processed or not.
     */
    private boolean _isStimDataProcessed;

    /*!
     * \property StarStimProtocol::_isStimConfigProcessed
     *
     * It marks whether the StarStim configuration block has been processed or
     * not.
     */
    private boolean _isStimConfigProcessed;

    /*!
     * \property StarStimProtocol::_isStimImpedanceProcessed
     *
     * It marks whether the StarStim Impedance configuration block has been
     * processed or not.
     */
    private boolean _isStimImpedanceProcessed;

    /*!
     * \property StarStimProtocol::_isBatteryProcessed
     *
     * It marks whether the battery data block has been processed or not.
     */
    private boolean _isBatteryProcessed;

    /*!
     * \property StarStimProtocol::_isAccelerometerProcessed
     *
     * It marks whether the accelerometer block has been processed or
     * not.
     */
    private boolean _isAccelerometerProcessed;

    /*!
     * \property StarStimProtocol::_isFirmWareProcessed
     *
     * It marks whether the firmware version block has been processed or
     * not.
     */
    private boolean _isFirmWareProcessed;

    /*!
     * \property StarStimProtocol::_currentChannel
     *
     * It stores the channel whose data is currently being parsed.
     */
    private byte _currentChannel;

    /*!
     * \property StarStimProtocol::_currentSample
     *
     * It stores the sample samples are collected.
     */
    private byte _currentSample;

    /*!
     * \property StarStimProtocol::_multipleSample
     *
     * Configures the StarStimProtocol to work in multiple sample mode
     */
    int _multipleSample;



    /*!
     * \property StarStimProtocol::_compressionEnabled
     *
     * Configures the StarStimProtocol to handle compressed EEG Samples
     */
    private EEGCompressionType _eegCompresssionType;

    /*!
     * \property StarStimProtocol::_currentConfigAddress
     *
     * It stores the address of the eeg/stimulation register that is currently
     * being processed.
     */ 
    private int _currentConfigAddress;

    /*!
     * \brief isLength2Bytes Holds whether the current device has length of two bytes
     */
    private boolean _isLength2Bytes;
    
    private int _artifactCheckerCounter;
    private int _lastArtifactCheckerCounter;
    private int[] _artifactLastValues;

    private int _status;

    private char[] _debugErrorFrame;
    private int _debugErrorFrameIndex;
    private int _lastChannelWithEEG;
    private char _lastDeviceStatus;

    private int _nBytes;

    private int _temp;
    private char  _checksum;
    private char _stamp;
    private char[] _sfd;
    private char[] _eof;
    private int _counter;

    private int _sdcardRecording;
    
    
    //  -- METHODS --
    // --------------
    public StarStimProtocol(EEGCompressionType eegCompresssionType) {
		logger = Logger.getInstance();
    	
    	// Initialise arrays
        _artifactLastValues = new int[32];
        _debugErrorFrame = new char[200];
        _sfd = new char[4];
        _eof = new char[5];

        _starStimData = new StarStimData();
        
        // Set the type of compression
        _eegCompresssionType = eegCompresssionType; 
        
        
        // initialise values
        _isStimulating  = false;
        _isLength2Bytes = false;
        _multipleSample = 0;
        reset();
        
        
        
	}
    
    /*!
     * It performs the operations that are needed to pass from the current
     * states to the next one
     *
     * \param status New status to which the state machine performs the
     * transition
     */
    private void _statusTransition (StatusProtocol status){
        // nothig to do but to update the status property
        _state = status;
    }

    /*!
     * It processes the status byte #0 that contents information regarding the
     * rest of the frame and the status of the device.
     *
     * \param status byte that contents the information of both the rest of the
     * frame and the device at bit-level.
     */
    private void _processStatusByte0 (byte status){
        _starStimData.isSDCardRecording((status & 0x08) > 0);

        if  (_status != ((status&0x70)>>4))
        {
            //qDebug() << "status = " << ((status&0x70)>>4) << " (" << _status_ << ")";
            _status = ((status&0x70)>>4);
        }

        boolean toggled = ((status & 0x80) != (_previousCommandToggle&0xFF));
        //if (toggled) logger.info( "toggled!! (status = " + (byte) status + ", previous = " + (byte) _previousCommandToggle + ")", Logger.LOG_FILE_ON);
        _starStimData.isCommandToggled(toggled);
        _previousCommandToggle = (char) ((status & 0x80) & 0xFF);
        //_starStimData.deviceStatus((status >> 4) & 0x07);
        _starStimData.deviceStatus((status >> 3) & 0x0f);
        _starStimData.deviceError((status & 0x0f)); 
        
        
    }

    /*!
     * It processes the status byte #1 that contents information regarding the
     * rest of the frame and the status of the device.
     *
     * \param status byte that contents the information of both the rest of the
     * frame and the device at bit-level.
     */
    private void _processStatusByte1 (byte status){
        
    	_starStimData.isEEGConfigPresent  ((status & 0x01) > 0);
        _starStimData.isEEGDataPresent    ((status & 0x02) > 0);
        _starStimData.isStimConfigPresent ((status & 0x04) > 0);
        _starStimData.isStimDataPresent   ((status & 0x08) > 0);
        _starStimData.isStimImpedancePresent((status & 0x10) > 0);
        _starStimData.isAccelerometerPresent((status & 0x20) > 0);
        _starStimData.isBatteryPresent    ((status & 0x40) > 0);
        _starStimData.isFirmwareVersionPresent((status & 0x80) > 0);
        
    }

    /*!
     * It returns the next channel present in the frame according to the
     * information channel byte passed as parameter and the channel currently
     * been processed.
     *
     * \param channelInfo Byte that contains the channels that are present in
     * the received frame at bit level.
     *
     * \return number of the next channel that is going to be processed. If
     * there is no more channels to be processed an out of range value is
     * returned.
     *
     * \post The channel currently been processed is updated accordingly.
     */
    private byte _findNextChannel (int channelInfo){

    	int i = 1;
        	
        i = i << _currentChannel;
        while (_currentChannel < 32)
        {
            if ( (channelInfo & i) != 0x00 )
            {
                break;
            }
            _currentChannel++;
            i = i << 1;
        }
                
        return _currentChannel;    
    }

    /*!
     * \return True if there is no more EEG channels to be processed in the
     * current frame.
     */
    private boolean _isLastEEGChannel (){
        _currentChannel++;
        return (_findNextChannel(_starStimData.eegChInfo()) >= 32);
    }

    /*!
     * It processes the byte that contains the informaion regarding the EEG
     * channels that are present in the current frame.
     *
     * \paragraph eegChInfo Byte that contains the EEG channels that are
     * present in the received frame at bit level.
     *
     * \return True if there is at least one channel present in the information
     * byte, false otherwise.
     */
    private boolean _processEEGChannelInfo (int eegChInfo){
    	    	
        eegChInfo = ~eegChInfo; // '0' means EEG and '1' STM
        _starStimData.eegChInfo(eegChInfo);
        if (eegChInfo == 0)
        {
            return false;
        }
        _currentChannel = 0;
        _findNextChannel (eegChInfo);
        return true;
    }

    /*!
     * It informs whether the current EEG register address is the last one
     * present in the frame.
     *
     * \return True if the current EEG register address that
     * has been processed is the last one present in the frame
     */
    private boolean _isLastEEGReg (){
        _currentConfigAddress++;
        return ((_currentConfigAddress - _starStimData.eegStartAddress()) >= _starStimData.eegNumRegs());
    }

    /*!
     * It informs whether the current stimulation channel is the last one
     * present in the frame.
     *
     * \return True if there is no more stimulation channels to be processed in
     * the current frame
     */
    private boolean _isLastStimChannel (){
        _currentChannel++;
        return (_findNextChannel(_starStimData.stimChInfo()) >= 8);
    }

    /*!
     * It informs whether the current impedance channel is the last one present
     * in the frame.
     *
     * \return True if there is no more channel where to read impedance from.
     */
    private boolean _isLastStimImpedanceChannel (){
        _currentChannel++;
        return (_findNextChannel(_starStimData.stimImpedanceChInfo()) >= 8);
    }

    /*!
     * It processes the byte that contains the informaion regarding the
     * stimulation channels that are present in the current frame.
     *
     * \param stimChInfo Byte that contains the stimulation channels that are
     * present in the received frame at bit level.
     *
     * \return True if there is at least one channel present in the information
     * byte, false otherwise.
     */
    private boolean _processStimChannelInfo (byte stimChInfo){
        _starStimData.stimChInfo( (char) stimChInfo);
        if (stimChInfo == 0)
        {
            return false;
        }
        _currentChannel = 0;
        _findNextChannel (stimChInfo);
        return true;
    }

    /*!
     * It processes the byte that contains the informaion regarding the
     * impedance of the stimulation channels that are present in the current
     * frame.
     *
     * \param chInfo Byte that contains the impedance of the stimulation
     * channels that are present in the received frame at bit level.
     *
     * \return True if there is at least one channel present in the information
     * byte, false otherwise.
     */
    private boolean _processStimImpedanceChannelInfo ( char chInfo ){
        _starStimData.stimImpedanceChInfo(chInfo);
        if (chInfo == 0)
        {
            return false;
        }
        _currentChannel = 0;
        _findNextChannel (chInfo);
        return true;
    }

    /*!
     * It informs whether the current stimulation register address is the last
     * one present in the frame.
     *
     * \return True if the current stimulation register addres been processed
     * is the last one present in the frame
     */
    private boolean _isLastStimReg (){
        _currentConfigAddress++;
        return ((_currentConfigAddress - _starStimData.stimStartAddress()) >=
                                                    _starStimData.stimNumRegs());
    }

    /*!
     * It updates the state machine to be receiving the nex block of data
     * according to the information that the status bytes carried and the
     * current processed block.
     *
     * \return True if there is any other data block to process, false
     * otherwise.
     */
    private boolean _transitionToNextBlock (){
        if (_starStimData.isEEGDataPresent() && !_isEEGDataProcessed)
        {
            _isEEGDataProcessed = true;
            _statusTransition(StatusProtocol.ST_EEG_DATA);
        }
        else if (_starStimData.isEEGConfigPresent() && !_isEEGConfigProcessed)
        {
            _isEEGConfigProcessed = true;
            _statusTransition(StatusProtocol.ST_EEG_CONFIG);
        }
        else if (_starStimData.isStimDataPresent() && !_isStimDataProcessed)
        {
            _isStimDataProcessed = true;
            _statusTransition(StatusProtocol.ST_STIM_DATA);
        }
        else if (_starStimData.isStimConfigPresent() && !_isStimConfigProcessed)
        {
            _isStimConfigProcessed = true;
            _statusTransition(StatusProtocol.ST_STIM_CONFIG);
        }
        else if (_starStimData.isStimImpedancePresent() && !_isStimImpedanceProcessed)
        {
            _isStimImpedanceProcessed = true;
            _statusTransition(StatusProtocol.ST_STIM_IMPEDANCE);
        }
        else if (_starStimData.isBatteryPresent() && !_isBatteryProcessed)
        {
            _isBatteryProcessed = true;
            _statusTransition(StatusProtocol.ST_BATTERY);
        }
        else if (_starStimData.isAccelerometerPresent() && !_isAccelerometerProcessed)
        {
            _isAccelerometerProcessed = true;
            _statusTransition(StatusProtocol.ST_ACCELEROMETER);
        }
        else if (_starStimData.isFirmwareVersionPresent() && !_isFirmWareProcessed)
        {
            _isFirmWareProcessed = true;
            _statusTransition(StatusProtocol.ST_FIRMWARE);
        }
        else // no data
        {
            return false;
        }
        return true;
    }

    /*!
     * It resets the state machine so a new frame can be received.
     */
    private void _resetStateMachine (){
        _state = StatusProtocol.ST_IDLE;
        _isEEGDataProcessed       = false;
        _isEEGConfigProcessed     = false;
        _isStimDataProcessed      = false;
        _isStimConfigProcessed    = false;
        _isStimImpedanceProcessed = false;
        _isBatteryProcessed       = false;
        _isAccelerometerProcessed = false;
        _isFirmWareProcessed      = false;
        _currentChannel           = 0;
        _currentConfigAddress     = 0;
        _dataLength               = 0;
        _artifactCheckerCounter   = 0;
    }

    /*!
     * It checks if the Start Of Frame delimites has been received.
     *
     * \param byte Last received byte.
     *
     * \return True if the Start Of Frame has been detected, false otherwise.
     */
    private boolean _isStartOfFrame (byte byteRcv){
        _sfd[0] = _sfd[1];
        _sfd[1] = _sfd[2];
        _sfd[2] = (char) byteRcv;
        
        _counter++;
        
        StringBuilder str = new StringBuilder();
        str.append(_sfd[0]);
        str.append(_sfd[1]);
        str.append(_sfd[2]);
        
        if ( str.toString().equals("SOF") )
        {
            if (_counter > 3)
            {
                logger.info( "SOF not found " + _counter, Logger.LOG_FILE_ON );
            }
            _counter = 0;
        }

        
        return (str.toString().equals("SOF"));
    }

    /*!
     * It checks if the EndOfFrame has been received.
     *
     * \param byte Last received byte.
     *
     * \return True if the EndOfDelimiter has been detected, false otherwise.
     */
    private boolean _isEndOfFrame (byte byteRcv){
        _eof[0] = _eof[1];
        _eof[1] = _eof[2];
        _eof[2] = _eof[3];
        _eof[3] = (char) byteRcv;
        
        StringBuilder str = new StringBuilder();
        str.append(_eof[0]);
        str.append(_eof[1]);
        str.append(_eof[2]);
        str.append(_eof[3]);
            	       
        return (str.toString().equals("EOF\n"));
    }
    
    
    /*!
     * It performs the parse of the protocol byte by byte.
     *
     * \param byte Byte to be parsed according to the StarStim protocol.
     *
     * \return It returns true when the processing of the byte leads to a
     * completion of some statement of the protocol. It returns false
     * otherwise.
     */
    public boolean _streamingMainClass = false;
    public boolean parseByte ( byte byteRcv ){

        String debugString = "";

        int auxArtifactCorrector;

        /* JUST WHILE DEVELOPING ****/
        //qDebug() << nBytes;
        //if (++nBytes >= 10)
        //{
        //    nBytes = 0;
        //    return true;
        //}
        //return false;
        /****************************/
        //qDebug() << "." << byte;
        
        _debugErrorFrame[_debugErrorFrameIndex++] = (char) (byteRcv&0xFF);
        if (_debugErrorFrameIndex >= 200)
        {
            _debugErrorFrameIndex = 0;
        }
        _nBytes++;
        switch (_state)
        {
        case ST_IDLE:
            if ( _isStartOfFrame( byteRcv ) ) // start frame delimiter
            {    
                _resetStateMachine();
                _starStimData.empty();
                _nBytes = SFD_LENGTH;
                _statusTransition(StatusProtocol.ST_LENGTH0);
            }
            break;
        case ST_LENGTH0:
        	
        	
            // When receiving first packet, decide whether device it length 2 byte
            // First byte of length in first packet should be 0 (length is 0x00 0x0B)
            if( byteRcv == 0 && _isLength2Bytes == false ){
            	logger.info("Length field is 2 bytes ", Logger.LOG_FILE_ON );
                _isLength2Bytes = true;
            }
        	
            if( _isLength2Bytes ){
            	_dataLength = ((byteRcv&0xFF) << 8*1);
                _statusTransition(StatusProtocol.ST_STATUS_1);
            }else{
                _dataLength = byteRcv;
                _statusTransition(StatusProtocol.ST_STATUS_0);            	
            }


        	
        	
    //#define LENGTH_2BYTES
/*        	
    #ifndef LENGTH_2BYTES
            _dataLength = byte;
            _statusTransition(StatusProtocol.ST_STATUS_0);
    #else
    ///////////////////////
            _dataLength = byte << 8*1;
            _statusTransition(StatusProtocol.ST_LENGTH1);
            break;
        case ST_LENGTH1:
            _dataLength += byte;
            _statusTransition(StatusProtocol.ST_STATUS_0);
    ////////////////////////
    #endif
/**/    
            break;
        case ST_STATUS_0:
            _processStatusByte0(byteRcv);
            _statusTransition(StatusProtocol.ST_STATUS_1);
            break;
        case ST_STATUS_1:
            _processStatusByte1(byteRcv);
            //if( _streamingMainClass )
            //	logger.info("Status is " + String.format("0x%02X", byteRcv) , Logger.LOG_FILE_ON );
            
            
            if (!_transitionToNextBlock()) // no more data
            {
                _statusTransition(StatusProtocol.ST_EOF);
            }
            break;
        case ST_EEG_DATA:
//            _statusTransition(StatusProtocol.ST_EEG_CH_INFO_0);
//            // Store number of samples per byte
//            _starStimData.nSamples( byte );
//            _currentSample = 0;


            if( _multipleSample == 1 ){ // Multiple Sample Mode ON!
                _statusTransition(StatusProtocol.ST_EEG_CH_INFO_0);
                // Store number of samples per byte
                _starStimData.nSamples( byteRcv );
                _currentSample = 0;

            }else{
                _temp = ((byteRcv&0xFF) << 24);
                _statusTransition(StatusProtocol.ST_EEG_CH_INFO_1);

                // Force to have 1 Sample per beacon
                _starStimData.nSamples( 1 );
                _currentSample = 0;

            }

            break;


        case ST_EEG_CH_INFO_0:

            /*******FOR 20 CHANNELS******/
            _temp = ((byteRcv&0xFF) << 24);
            _statusTransition(StatusProtocol.ST_EEG_CH_INFO_1);

            break;

        case ST_EEG_CH_INFO_1:
            //temp += (byte << 8);
            _temp += ((byteRcv&0xFF) << 16);
            _statusTransition(StatusProtocol.ST_EEG_CH_INFO_2);
            break;
        case ST_EEG_CH_INFO_2:
            //temp += (byte << 16);
            _temp += ((byteRcv&0xFF) << 8);
            _statusTransition(StatusProtocol.ST_EEG_CH_INFO_3);
            break;
        case ST_EEG_CH_INFO_3:
            //temp += (byte << 24);
            _temp += (byteRcv&0xFF);
            
            
            if (!_processEEGChannelInfo(_temp))
            {
                logger.info( "_processEEGChannelInfo Error", Logger.LOG_FILE_ON );

                if (!_transitionToNextBlock()) // no more data
                {
                    _statusTransition(StatusProtocol.ST_EOF);
                }
            }
            else
            {
                _statusTransition(StatusProtocol.ST_EEG_DATA_RAW_MSB);
            }

            break;
        case ST_EEG_DATA_RAW_MSB:
            _checksum += byteRcv;
            //_starStimData.pointerToEEGData()[_currentChannel] = byte << 16;
            _temp = (byteRcv&0xFF) << 8*2;
            //logger.info( "_temp:" + _temp + ":_byteRcv:" + byteRcv, Logger.LOG_FILE_ON);
            _statusTransition(StatusProtocol.ST_EEG_DATA_RAW_MSBLSB);
            break;
        case ST_EEG_DATA_RAW_MSBLSB:
            _checksum += (byteRcv&0xFF);
            //_starStimData.pointerToEEGData()[_currentChannel] += byte << 8;
            _temp += (byteRcv&0xFF) << 8*1;
            //logger.info( "_temp:" + _temp + ":_byteRcv:" + byteRcv, Logger.LOG_FILE_ON);
            auxArtifactCorrector = _temp>>8;
            if (_lastArtifactCheckerCounter >= 0)
            {
                _artifactCheckerCounter += Math.abs(auxArtifactCorrector - _artifactLastValues[_currentChannel]);
            }
            _artifactLastValues[_currentChannel] = auxArtifactCorrector;
            _lastChannelWithEEG = _currentChannel + 1;
            _statusTransition(StatusProtocol.ST_EEG_DATA_RAW_LSB);
            break;
        case ST_EEG_DATA_RAW_LSB:
            _checksum += (byteRcv&0xFF);
            //_starStimData.pointerToEEGData()[_currentChannel] += byte;
            _temp += (byteRcv&0xFF);
            //qDebug() << "_byteRcv" << byteRcv;
            _starStimData.eegData( _currentChannel, _temp, _currentSample );
            //logger.info( "_temp:" + _temp + ":_byteRcv:" + byteRcv + ":_currentChanel:" + _currentChannel + ":_currentSample:" + _currentSample, Logger.LOG_FILE_ON);
            if (_isLastEEGChannel())
            {
                _artifactCheckerCounter = _artifactCheckerCounter / _lastChannelWithEEG;
                                
                _currentSample ++;
                if( _currentSample == _starStimData.nSamples() ){
                    _statusTransition( StatusProtocol.ST_EEG_STAMP );
                }else{
                    _currentChannel = 0;
                    _findNextChannel ( _starStimData.eegChInfo() );

                    // NOTE: First sample is not compressed
                    // When compression enabled => first sample is 3 byte/channel, next ones are 2 byte/channel
                    if( _eegCompresssionType == StarStimProtocol.EEGCompressionType.EEG_16BIT_COMPRESSION && _currentSample > 0){
                        _statusTransition( StatusProtocol.ST_EEG_COMPRESSED_16BIT_MSB );
                    }else if( _eegCompresssionType == StarStimProtocol.EEGCompressionType.EEG_12BIT_COMPRESSION && _currentSample > 0){
                          _statusTransition( StatusProtocol.ST_EEG_COMPRESSED_12BIT_S1 );
                    }else{
                        _statusTransition( StatusProtocol.ST_EEG_DATA_RAW_MSB );
                    }
                                        

                    
                }
                
                // old
                //_statusTransition(StatusProtocol.ST_EEG_CHECKSUM);
            }
            else
            {
                _statusTransition(StatusProtocol.ST_EEG_DATA_RAW_MSB);
            }

            break;
        case ST_EEG_COMPRESSED_16BIT_MSB:
            _temp = (byteRcv&0xFF) << 8*1;
            _statusTransition( StatusProtocol.ST_EEG_COMPRESSED_16BIT_LSB );
            break;
        case ST_EEG_COMPRESSED_16BIT_LSB:

            _temp += (byteRcv&0xFF);
            _starStimData.eegData( _currentChannel, _temp, _currentSample );
            if (_isLastEEGChannel())
            {
                _artifactCheckerCounter = _artifactCheckerCounter / _lastChannelWithEEG;

                _currentSample ++;
                if( _currentSample == _starStimData.nSamples() ){
                    _statusTransition( StatusProtocol.ST_EEG_STAMP );
                }else{
                    _currentChannel = 0;
                    _findNextChannel ( _starStimData.eegChInfo() );

                    // NOTE: First sample is not compressed
                    // When compression enabled => first sample is 3 byte/channel, next ones are 2 byte/channel
                    _statusTransition( StatusProtocol.ST_EEG_COMPRESSED_16BIT_MSB );
                }

            }
            else
            {
                _statusTransition( StatusProtocol.ST_EEG_COMPRESSED_16BIT_MSB );
            }

            break;
        case ST_EEG_COMPRESSED_12BIT_S1:
            _temp = (byteRcv&0xFF) << 4;
            _statusTransition( StatusProtocol.ST_EEG_COMPRESSED_12BIT_S1S2 );
            break;
        case ST_EEG_COMPRESSED_12BIT_S1S2:

            // First EEG channel received
            _temp = _temp + ((byteRcv&0xFF) >> 4);
            _starStimData.eegData( _currentChannel, _temp, _currentSample );
            _currentChannel++;

            // Prepare for next sample
            _temp = ((byteRcv&0xFF)&0x0F) << 8;

            _statusTransition( StatusProtocol.ST_EEG_COMPRESSED_12BIT_S2 );
            break;
        case ST_EEG_COMPRESSED_12BIT_S2:

            // Second EEG channel received
            _temp = _temp + (byteRcv&0xFF);
            _starStimData.eegData( _currentChannel, _temp, _currentSample );
            _currentChannel++;
            if ( _findNextChannel(_starStimData.eegChInfo()) >= 32 )
            {
                // Number of samples received
                _currentSample ++;
                if( _currentSample == _starStimData.nSamples() ){
                    _statusTransition( StatusProtocol.ST_EEG_STAMP );
                }else{
                    _currentChannel = 0;
                    _findNextChannel ( _starStimData.eegChInfo() );
                    //_currentChannel ++;
                    _statusTransition( StatusProtocol.ST_EEG_COMPRESSED_12BIT_S1 );

                }
            }
            else // If not last channel, redo procedure
            {
                _statusTransition( StatusProtocol.ST_EEG_COMPRESSED_12BIT_S1 );
            }



            break;

        case ST_EEG_CHECKSUM:
            if (_checksum != (byteRcv&0xFF))
            {
                logger.info ( "Checksum incorrect"  +_checksum + " " + (byteRcv&0xFF), Logger.LOG_FILE_ON);
            }
            _starStimData.eegChecksum( (char) (byteRcv&0xFF));
            _statusTransition(StatusProtocol.ST_EEG_STAMP);
//            if (!_transitionToNextBlock()) // no more data
//            {
//                _statusTransition(StatusProtocol.ST_EOF);
//            }
            break;
        case ST_EEG_STAMP:
//            if (stamp != byte)
//            {
//                qDebug() << "error on stamp " << stamp << " " << byte;
//                stamp = byte;
//            }
            _stamp++;
            if (_firmwareVersion<800) //EEG_STAMP has 1 byte
            {
                _starStimData.eegStamp((byteRcv&0xFF));

                if (!_transitionToNextBlock()) // no more data
                {
                    _statusTransition(StatusProtocol.ST_EOF);
                }
            }
            else //EEG_STAMP has 4 bytes
            {
                _temp = ((byteRcv&0xFF) << 24);
                _statusTransition(StatusProtocol.ST_EEG_STAMP_2);
                //logger.info("TimeStamp is " + String.format("0x%08X 0x%02X", _temp, byteRcv) + " " + _temp, Logger.LOG_FILE_ON );
            }            
            break;
        case ST_EEG_STAMP_2:
            _temp += ((byteRcv&0xFF) << 16);
            _statusTransition(StatusProtocol.ST_EEG_STAMP_3);
            //logger.info("TimeStamp is " + String.format("0x%08X 0x%02X", _temp, byteRcv) + " " + _temp, Logger.LOG_FILE_ON );
            break;
        case ST_EEG_STAMP_3:
            _temp += ((byteRcv&0xFF) << 8);
            _statusTransition(StatusProtocol.ST_EEG_STAMP_4);
            //logger.info("TimeStamp is " + String.format("0x%08X 0x%02X", _temp, byteRcv) + " " + _temp, Logger.LOG_FILE_ON );
            break;
        case ST_EEG_STAMP_4:
            _temp += (byteRcv&0xFF);
            _starStimData.eegStamp(_temp);
            
            //logger.info("TimeStamp is " + String.format("0x%08X 0x%02X", _temp, byteRcv) + " " + _temp, Logger.LOG_FILE_ON );
            
            if (!_transitionToNextBlock()) // no more data
            {
                _statusTransition(StatusProtocol.ST_EOF);
            }
            break;
        case ST_EEG_CONFIG:   	
            _starStimData.eegStartAddress( (byteRcv&0xFF));
            _currentConfigAddress = _starStimData.eegStartAddress();
            _statusTransition(StatusProtocol.ST_EEG_CONFIG_NUM_REGS);
            break;
        case ST_EEG_CONFIG_NUM_REGS:
            _starStimData.eegNumRegs( (byteRcv&0xFF));
            if (_starStimData.eegNumRegs() == 0)
            {
                if (!_transitionToNextBlock()) // no more data
                {
                    _statusTransition(StatusProtocol.ST_EOF);
                }
            }
            else
            {
                _statusTransition(StatusProtocol.ST_EEG_CONFIG_REGS);
            }
            break;
        case ST_EEG_CONFIG_REGS:
        	logger.info("Store data internally " + _currentConfigAddress  + " " + _starStimData.eegStartAddress() + " " +
        						_starStimData.eegNumRegs() +  " " + String.format("0x%02X", byteRcv), Logger.LOG_FILE_ON);
            _starStimData.eegReg()[_currentConfigAddress] = (char) (byteRcv&0xFF);
            if (_isLastEEGReg())
            {     
                if (!_transitionToNextBlock()) // no more data
                {
                    _statusTransition(StatusProtocol.ST_EOF);
                }
            }
            else
            {
                _statusTransition(StatusProtocol.ST_EEG_CONFIG_REGS);
            }
            break;
        case ST_STIM_DATA:
            if (!_processStimChannelInfo((byte) (byteRcv&0xFF)))
            {
                if (!_transitionToNextBlock()) // no more data
                {
                    _statusTransition(StatusProtocol.ST_EOF);
                }
            }
            else
            {
                _statusTransition(StatusProtocol.ST_STIM_DATA_MSB);
            }
            break;
        case ST_STIM_DATA_MSB:
            //_starStimData.pointerToStimData()[_currentChannel] = (byte << 8);
            _temp = ((byteRcv&0xFF) << 8);
            _statusTransition(StatusProtocol.ST_STIM_DATA_LSB);
            break;
        case ST_STIM_DATA_LSB:
            //_starStimData.pointerToStimData()[_currentChannel] += byte;
            _temp += (byteRcv&0xFF);
            _starStimData.stimData(_currentChannel, _temp);
            if (_isLastStimChannel())
            {
                if (!_transitionToNextBlock()) // no more data
                {
                    _statusTransition(StatusProtocol.ST_EOF);
                }
            }
            else
            {
                _statusTransition(StatusProtocol.ST_STIM_DATA_MSB);
            }
            break;
        case ST_STIM_CONFIG:
            _starStimData.stimStartAddress( (char) byteRcv);
            _currentConfigAddress = _starStimData.stimStartAddress();
            _statusTransition(StatusProtocol.ST_STIM_CONFIG_NUM_REGS);
            
 
        	
            break;
        case ST_STIM_CONFIG_NUM_REGS:
            _starStimData.stimNumRegs( (char) byteRcv);            
            if (_starStimData.stimNumRegs() == 0)
            {
                if (!_transitionToNextBlock()) // no more data
                {
                    _statusTransition(StatusProtocol.ST_EOF);
                }
            }
            else
            {
                _statusTransition(StatusProtocol.ST_STIM_CONFIG_REGS);
            }
            break;
        case ST_STIM_CONFIG_REGS:        	            
            
            _starStimData.stimReg()[_currentConfigAddress] = (char) (byteRcv&0xFF);
            if (_isLastStimReg())
            {
                if (!_transitionToNextBlock()) // no more data
                {
                    _statusTransition(StatusProtocol.ST_EOF);
                }
            }
            else
            {
                _statusTransition(StatusProtocol.ST_STIM_CONFIG_REGS);
            }
            break;
        case ST_STIM_IMPEDANCE:
            if (!_processStimImpedanceChannelInfo( (char) (byteRcv&0xFF)))
            {
                if (!_transitionToNextBlock()) // no more data
                {
                    _statusTransition(StatusProtocol.ST_EOF);
                }
            }
            else
            {
                _statusTransition(StatusProtocol.ST_STIM_IMPEDANCE_DATA_MSB);
            }
            break;
        case ST_STIM_IMPEDANCE_DATA_MSB:
            //_starStimData.pointerToImpedanceData()[_currentChannel] = byte << 24;
            _temp = (byteRcv&0xFF) << 24;
            _statusTransition(StatusProtocol.ST_STIM_IMPEDANCE_DATA_MSBLSB);
            break;
        case ST_STIM_IMPEDANCE_DATA_MSBLSB:
            //_starStimData.pointerToImpedanceData()[_currentChannel] += byte << 16;
            _temp += (byteRcv&0xFF) << 16;
            _statusTransition(StatusProtocol.ST_STIM_IMPEDANCE_DATA_LSBMSB);
            break;
        case ST_STIM_IMPEDANCE_DATA_LSBMSB:
            //_starStimData.pointerToImpedanceData()[_currentChannel] += byte << 8;
            _temp += (byteRcv&0xFF) << 8;
            _statusTransition(StatusProtocol.ST_STIM_IMPEDANCE_DATA_LSB);
            break;
        case ST_STIM_IMPEDANCE_DATA_LSB:
            //_starStimData.pointerToImpedanceData()[_currentChannel] += byte;
            _temp += (byteRcv&0xFF);
            _starStimData.stimImpedance(_currentChannel, _temp);
            if (_isLastStimImpedanceChannel())
            {
                if (!_transitionToNextBlock()) // no more data
                {
                    _statusTransition(StatusProtocol.ST_EOF);
                }
            }
            else
            {
                _statusTransition(StatusProtocol.ST_STIM_IMPEDANCE_DATA_MSB);
            }
            break;
        case ST_BATTERY:
            _temp = (byteRcv&0xFF);
            _statusTransition(StatusProtocol.ST_BATTERY_1);
            break;
        case ST_BATTERY_1:
            _temp = ((byteRcv&0xFF) << 24) | _temp;
            _statusTransition(StatusProtocol.ST_BATTERY_2);
            break;
        case ST_BATTERY_2:
            _temp = ((byteRcv&0xFF) << 16) | _temp;
            _statusTransition(StatusProtocol.ST_BATTERY_3);
            break;
        case ST_BATTERY_3:
            _temp = ((byteRcv&0xFF) << 8) | _temp;
            _starStimData.battery(_temp);
            if (!_transitionToNextBlock()) // no more data
            {
                _statusTransition(StatusProtocol.ST_EOF);
            }
            break;
        case ST_ACCELEROMETER:
            _temp=(byteRcv&0xFF)<<8;
            _statusTransition(StatusProtocol.ST_ACCELEROMETER_1);
            break;
        case ST_ACCELEROMETER_1:
            _temp += ((byteRcv&0xFF));
            if (_temp >= 32768)
              _temp -= 65536;
            _starStimData.accelerometer(0,_temp);
            _statusTransition(StatusProtocol.ST_ACCELEROMETER_2);
            break;
        case ST_ACCELEROMETER_2:
            _temp=(byteRcv&0xFF)<<8;
            _statusTransition(StatusProtocol.ST_ACCELEROMETER_3);
            break;
        case ST_ACCELEROMETER_3:
            _temp += ((byteRcv&0xFF));
            if (_temp >= 32768)
              _temp -= 65536;
            _starStimData.accelerometer(1,_temp);
            _statusTransition(StatusProtocol.ST_ACCELEROMETER_4);
            break;
        case ST_ACCELEROMETER_4:
            _temp=(byteRcv&0xFF)<<8;
            _statusTransition(StatusProtocol.ST_ACCELEROMETER_5);
            break;
        case ST_ACCELEROMETER_5:
            _temp += ((byteRcv&0xFF));
            if (_temp >= 32768)
              _temp -= 65536;
            _starStimData.accelerometer(2,_temp);
            if (!_transitionToNextBlock()) // no more data
            {
                _statusTransition(StatusProtocol.ST_EOF);
            }
            break;
        case ST_FIRMWARE:
            _temp = (byteRcv&0xFF) << 8;
            _statusTransition(StatusProtocol.ST_FIRMWARE_1);
            break;
        case ST_FIRMWARE_1:    		
    		_temp = (_temp | (byteRcv & 0xFF));
            //logger.info( "parseByte StatusProtocol.ST_FIRMWARE_1 " + String.format( "0x%04X", byteRcv), Logger.LOG_FILE_ON );
            logger.info( "parseByte StatusProtocol.ST_FIRMWARE_1 val " + String.format( "0x%04X", _temp) + " " + _temp, Logger.LOG_FILE_ON);
            
            _starStimData.firmwareVersion( (_temp & 0x7FFF) );
            _starStimData.set1000SPS(     ((_temp & 0x8000)>>15) );
            // NOTE: Configure SW to work in MultipleSample Mode when 1000SPS
            _multipleSample = (_temp & 0x8000)>>15; // This should be removed as 1000SPS is always multisample
            //
            if (!_transitionToNextBlock()) // no more data
            {
                _statusTransition(StatusProtocol.ST_EOF);
            }
            break;
        case ST_EOF:
            _isEndOfFrame(byteRcv);
            _statusTransition(StatusProtocol.ST_EOF_1);
            break;
        case ST_EOF_1:
            _isEndOfFrame(byteRcv);
            _statusTransition(StatusProtocol.ST_EOF_2);
            break;
        case ST_EOF_2:
            _isEndOfFrame(byteRcv);
            _statusTransition(StatusProtocol.ST_EOF_3);
            break;
        case ST_EOF_3:
            _statusTransition(StatusProtocol.ST_IDLE);
            if (_isEndOfFrame(byteRcv) && _nBytes == _dataLength)
            {
          
/*
    #ifdef __DEBUGARTIFACTENOBIO20PROTOCOL__
                if (_starStimData.deviceStatus() & 0x02)
                {
                    for (unsigned int k = 0; k < _debugErrorFrameIndex; k++)
                    {
                        debugString.append(" ");
                        debugString.append(QString::number(_debugErrorFrame[k]));
                    }
                    debugString.append("\t" + QString::number(QDateTime::currentMSecsSinceEpoch()) +
                                       "\n");
                    debugProtocolFile.write(debugString.toAscii());
                }
    #endif
/**/
                _debugErrorFrameIndex = 0;
                if ((_firmwareVersion<593)&&(!_isStimulating))
                {
                    if (_lastArtifactCheckerCounter >= 0)
                    {
                        if (_artifactCheckerCounter > 1000)
                        {
                            _lastArtifactCheckerCounter = -1;
                            break;
                        }
                    }
                    if ( (_starStimData.deviceStatus() & 0x02) != 0x00)
                    {
                        int i;
                        for (i = 0; i < 32; i++)
                        {
                            if (_artifactLastValues[i] != 0)
                                break;
                        }
                        if (i < 32)
                        {
                            _lastArtifactCheckerCounter = _artifactCheckerCounter;
                        }
                    }
                    else if ( (_lastDeviceStatus & 0x02) != 0x00)
                    {
                        _lastArtifactCheckerCounter = -1;
                        for (int i = 0; i < 32; i++)
                        {
                            _artifactLastValues[i] = 0;
                        }
                    }
                }
                _lastDeviceStatus = (char) _starStimData.deviceStatus();
                return true;
            }
            logger.info( "Frame with errors: byte " + (byteRcv&0xFF) + " nBytes " + _nBytes + " _dataLength " 
            				+ _dataLength + " _state " + _state, Logger.LOG_FILE_ON);
            for (int k = 0; k < _debugErrorFrameIndex; k++)
            {
            	debugString += " " + new Integer(_debugErrorFrame[k]) ;
            }
            logger.info( debugString, Logger.LOG_FILE_ON );
            _debugErrorFrameIndex = 0;
            break;
        }
        return false;
    }

    /*!
     * It returns the last received StarStim data.
     *
     * \return pointer to the last received StarStim data.
     */
    StarStimData getStarStimData (){
    	return _starStimData;
    }

    /*!
     * It resets the protocol status so a new frame is ready to be parsed.
     */
    public void reset (){
        _status = 255;

        _firmwareVersion=0;

        _debugErrorFrameIndex = 0;
        _lastDeviceStatus = 0;
        _nBytes = 0;
        _sfd[0] = 'x';
        _sfd[1] = 'x';
        _sfd[2] = 'x';
        _sfd[3] = '\0';
        _counter = 0;

        _eof[0] = 'x';
        _eof[1] = 'x';
        _eof[2] = 'x';
        _eof[3] = 'x';
        _eof[4] = '\0';
        _previousCommandToggle = 0;
        _lastArtifactCheckerCounter = -1;
        for (int i = 0; i < 32; i++)
        {
            _artifactLastValues[i] = 0;
        }
        _resetStateMachine();
    }

    /*!
     * It builds a request frame.
     *
     * \param buffer Pointer to a memory space where the request frame will be
     * built. The caller to the function has to allocate space enough to hold
     * the entire request frame.
     *
     * \param isEEGStarting Boolean that indicates whether the request frame
     * performs the EEG Starting action.
     *
     * \param isEEGStopping Boolean that indicates whether the request frame
     * performs the EEG Stopping action.
     *
     * \param isStimStarting Boolean that indicates whether the request frame
     * performs the stimulation Starting action.
     *
     * \param isStimStopping Boolean that indicates whether the request frame
     * performs the stimulation Stopping action.
     *
     * \param isImpedanceStarting Boolean that indicates whether the request
     * frame performs the impedance reporting Starting action.
     *
     * \param isImpedanceStopping Boolean that indicates whether the request
     * frame performs the impedance reporting Stopping action.
     *
     * \param isEEGSaving Boolean that indicates whether the request frame
     * performs the action of saving the EEG in the local SD card.
     *
     * \param isStimSaving Boolean that indicates whether the request frame
     * performs the action of saving the stimulation in the local SD card.
     *
     * \param isEEGConfigWriting Boolean that indicates whether the frame
     * requests for an EEG configuration writing.
     *
     * \param isStimConfigWriting Boolean that indicates whether the frame
     * requests for a stimulation configuration writing.
     *
     * \param isEEGConfigReading Boolean that indicates whether the frame
     * requests for an EEG configuration reading.
     *
     * \param isStimConfigReading Boolean that indicates whether the frame
     * requests for a stimulation configuration reading.
     *
     * \param isStimDataReading Boolean that indicates whether the stimulation
     * data is sent to the host.
     *
     * \param isAccelerometerConfigReading Boolean that indicates whether the
     * frame requests for an accelerometer configuration reading.
     *
     * \param isAccelerometerConfigWriting Boolean that indicates whether the
     * frame requests for an accelerometer configuration writing.
     *
     * \param isRealTimeStimulationWriting Boolean that indicates whether the
     * frame requests for writing the real stimulation signal.
     *
     * \param isSDCardConfigWriting Boolean that indicates whether the
     * frame requests for writing SDCard configuration register.
     *
     * \param eegConfigStartAddress In case isEEGConfigReading or
     * isEEGConfigWriting is true this parameter indicates the first address of
     * the EEG configuration register to be read/written, otherwise this is
     * ignored.
     *
     * \param eegConfigNumRegs In case isEEGConfigWriting is true this
     * parameter indicates the number of EEG registers to be written, otherwise
     * this is ignored.
     *
     * \param eegConfigReg In case isEEGConfigWriting is true this parameter
     * points to the locatio where the values of the registers are set,
     * otherwise this is ignored.
     *
     * \param stimConfigStartAddress In case isStimConfigReading or
     * isStimConfigWriting is true this parameter indicates the first address
     * of the stimulation configuration register to be read/written, otherwise
     * this is ignored.
     *
     * \param stimConfigNumRegs In case isStimConfigWriting is true this
     * parameter indicates the number of stimulation registers to be written,
     * otherwise this is ignored.
     *
     * \param stimConfigReg In case isStimConfigWriting is true this parameter
     * points to the locatio where the values of the registers are set,
     * otherwise this is ignored.
     *
     * \param accelerometerConfigStartAddress In case
     * isAccelerometerConfigReading or isAccelerometerConfigWriting is true
     * this parameter indicates the first address of the accelerometer
     * configuration register to be read/written, otherwise this is ignored.
     *
     * \param accelerometerConfigNumRegs In case isAccelerometerConfigWriting
     * is true this parameter indicates the number of registers to be written,
     * otherwise this is ignored.
     *
     * \param accelerometerConfigReg In case isAccelerometerConfigWriting is
     * true this parameter points to the location where the values of the
     * registers are set, otherwise this is ignored.
     *
     * \param sdcardConfigStartAddress In case isSDCardConfigWriting is true
     * this parameter indicates the first address of the accelerometer configuration
     * register to be read/written, otherwise this is ignored.
     *
     * \param sdcardConfigNumRegs In case isSDCardConfigWriting
     * is true this parameter indicates the number of registers to be written,
     * otherwise this is ignored.
     *
     * \param sdcardConfigReg In case isSDCardConfigWriting is
     * true this parameter points to the location where the values of the
     * registers are set, otherwise this is ignored.
     *
     * \param onlineStimChannelInfo In case isRealTimeStimulationWriting is
     * true this parameter indicates the channels that whose signal is reported
     * in the request frame, otherwise this is ignored.
     *
     * \param onlineStimNumSamples In case isRealTimeStimulationWriting is true
     * this parameter indicates the number of samples per channel present in
     * the request frame, otherwise this is ignored.
     *
     * \param onlineStimSample In case isRealTimeStimulationWriting is true
     * this parameter points to the location where the values of the samples
     * are set, otherwise this is ignored.
     *
     * \return Size of the request frame that is built in the buffer parameter.
     * A negative number is returned if some of the parameter has an invalid
     * value.
     */
    public static int buildRequestFrame (  Byte[] buffer,
                                  boolean isEEGStarting,
                                  boolean isEEGStopping,
                                  boolean isStimStarting,
                                  boolean isStimStopping,
                                  boolean isImpedanceStarting,
                                  boolean isImpedanceStopping,
                                  boolean isBatteryMeasurement,
                                  boolean isTBD,
                                  boolean isEEGConfigWriting,
                                  boolean isStimConfigWriting,
                                  boolean isEEGConfigReading,
                                  boolean isStimConfigReading,
                                  boolean isFirmWareVersionReading,
                                  boolean isAccelerometerConfigReading,
                                  boolean isAccelerometerConfigWriting,
                                  boolean isRealTimeStimulationWriting,
                                  boolean isSDCardConfigWriting,
                                  Byte eegConfigStartAddress,
                                  Byte eegConfigNumRegs,
                                  Byte[] eegConfigReg,
                                  Byte stimConfigStartAddress,
                                  Byte stimConfigNumRegs,
                                  Byte[] stimConfigReg,
                                  Byte accelerometerConfigStartAddress,
                                  Byte accelerometerConfigNumRegs,
                                  Byte[] accelerometerConfigReg,
                                  Byte sdcardConfigStartAddress,
                                  Byte sdcardConfigNumRegs,
                                  Byte[] sdcardConfigReg,
                                  Byte onlineStimChannelInfo,
                                  Byte onlineStimNumSamples,
                                  Byte[] onlineStimSample){
        buffer[0] = SFD_0;
        buffer[1] = SFD_1;
        buffer[2] = SFD_2;
        buffer[3] = 0; // length
        
        buffer[4] = (byte) (((isEEGStarting?1:0)        << 0) | ( (isEEGStopping?1:0)        << 1) | 
        					( (isStimStarting?1:0)      << 2) | ( (isStimStopping?1:0)      << 3)  | ( (isImpedanceStarting?1:0)  << 4) |
                            ( (isImpedanceStopping?1:0) << 5) | ( (isBatteryMeasurement?1:0) << 6) | ( (isTBD?1:0) << 7) );
        
        buffer[5] = (byte)  (  (isEEGConfigWriting?1:0) | ( (isStimConfigWriting?1:0) << 1) |
                    		( (isEEGConfigReading?1:0) << 2) | ( (isStimConfigReading?1:0) << 3) |
                    		( (isFirmWareVersionReading?1:0) << 4) | ( (isAccelerometerConfigReading?1:0) << 5)|
                    		( (isAccelerometerConfigWriting?1:0) << 6) |
                    		( (isRealTimeStimulationWriting?1:0) << 7) | ( (isSDCardConfigWriting?1:0) << 7) );
                    // Note that MSB is used for both RealTimeStimulationWriting and SDCardConfigWriting
        int pos = 6;
        if (isEEGConfigReading || isEEGConfigWriting)
        {
            if (eegConfigStartAddress >= StarStimData.NumEEGConfigReg)
            {
                return (-1);
            }
            buffer[pos++] = eegConfigStartAddress;
            if ((eegConfigStartAddress + eegConfigNumRegs) > StarStimData.NumEEGConfigReg)
            {
                return (-1);
            }
            buffer[pos++] = eegConfigNumRegs;
            if (isEEGConfigWriting)
            {
                for(int i = 0; i < eegConfigNumRegs; i++)
                {
                    buffer[pos++] = eegConfigReg[i];
                }
            }
        }
        if (isStimConfigReading || isStimConfigWriting)
        {
            if (stimConfigStartAddress >= StarStimData.NumStimConfigReg)
            {
                return (-1);
            }
            buffer[pos++] = stimConfigStartAddress;
            if ((stimConfigStartAddress + stimConfigNumRegs) > StarStimData.NumStimConfigReg)
            {
                return (-1);
            }
            buffer[pos++] = stimConfigNumRegs;
            if (isStimConfigWriting)
            {
                for(int i = 0; i < stimConfigNumRegs; i++)
                {
                    buffer[pos++] = stimConfigReg[i];
                }
            }
        }
        if (isAccelerometerConfigReading || isAccelerometerConfigWriting)
        {
            if (stimConfigStartAddress >= StarStimData.NumAccelerometerConfigReg)
            {
                return (-1);
            }
            buffer[pos++] = accelerometerConfigStartAddress;
            if ((accelerometerConfigStartAddress + accelerometerConfigNumRegs) > StarStimData.NumAccelerometerConfigReg)
            {
                return (-1);
            }
            buffer[pos++] = accelerometerConfigNumRegs;
            if (isAccelerometerConfigWriting)
            {
                for(int i = 0; i < accelerometerConfigNumRegs; i++)
                {
                    buffer[pos++] = accelerometerConfigReg[i];
                }
            }
        }
        if (isRealTimeStimulationWriting)
        {
            int numChannels = 0;
            for (int i = 0; i < 8; i++)
            {
                if ( (onlineStimChannelInfo & (1 << i)) != 0x00)
                {
                    numChannels ++;
                }
            }
            buffer[pos++] = onlineStimChannelInfo;
            // maximum number of samples per request
            int numSamples = onlineStimNumSamples & 0x7F;
            if (numSamples > 15)
            {
                return (-1);
            }
            buffer[pos++] = onlineStimNumSamples;
            int numBytes = numChannels * numSamples * 2;
            for (int i = 0; i < numBytes; i++)
            {
                buffer[pos++] = onlineStimSample[i];
            }
        }

        if( isSDCardConfigWriting ){

            if (sdcardConfigStartAddress >= StarStimData.NumSDCardConfigReg)
            {
                return (-1);
            }
            buffer[pos++] = sdcardConfigStartAddress;
            if ((sdcardConfigStartAddress + sdcardConfigNumRegs) >
                StarStimData.NumSDCardConfigReg)
            {
                return (-1);
            }
            buffer[pos++] = sdcardConfigNumRegs;

            if ( isSDCardConfigWriting )
            {
                for(int i = 0; i < sdcardConfigNumRegs; i++)
                {
                    buffer[pos++] = sdcardConfigReg[i];
                }
            }
    /*
            buffer[pos++] = 'S';  // file name
            buffer[pos++] = 'O';
            buffer[pos++] = 'S';
            buffer[pos++] = '.';
            buffer[pos++] = 'd';
            buffer[pos++] = 'a';
            buffer[pos++] = 't';
    /**/
        }
    /**/


        buffer[pos++] = EFD_0;
        buffer[pos++] = EFD_1;
        buffer[pos++] = EFD_2;
        buffer[3] = (byte) pos; // update length
        return pos;
    }

    /*!
     *It builds an enzo3G/StarStim frame request for starting the EEG
     *streaming.
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildStartEEGFrame (){
    	Logger logger = Logger.getInstance();
    	
        Byte[] frame = new Byte[256];
        Byte[] nullArray = new Byte[1];
        int size = buildRequestFrame(frame, true, false, false, false, false,
                                     false, false, false, false, false, false,
                                     false, false, false, false, false, false,
                                     (byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray, 
                                     (byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray,
                                     (byte) 0, (byte) 0, nullArray);

        if (size < 0)
        {
            logger.info( "Error building start EEG frame", Logger.LOG_FILE_ON );
        }
        else if (size >= 256)
        {
        	logger.info( "Critical: buffer too short. Program may not work properly", Logger.LOG_FILE_ON );
        }
        
        ArrayList<Byte> returnArray = new ArrayList<Byte>(Arrays.asList(frame)); 
        
        return new ArrayList<Byte> (returnArray.subList(0, size)); 
               
    }

    /*!
     *It builds an enzo3G/ StarStim frame request for stopping the EEG
     *streaming.
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildStopEEGFrame (){
    	Logger logger = Logger.getInstance();
    	
        Byte[] frame = new Byte[256];
        Byte[] nullArray = new Byte[1];
        int size = buildRequestFrame(frame, false, true, false, false, false,
                                     false, false, false, false, false, false,
                                     false, false, false, false, false, false,
                                     (byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray, 
                                     (byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray,
                                     (byte) 0, (byte) 0, nullArray);

        if (size < 0)
        {
            logger.info( "Error building start EEG frame", Logger.LOG_FILE_ON );
        }
        else if (size >= 256)
        {
        	logger.info( "Critical: buffer too short. Program may not work properly", Logger.LOG_FILE_ON );
        }
        
        ArrayList<Byte> returnArray = new ArrayList<Byte>(Arrays.asList(frame)); 
        
        return new ArrayList<Byte>( returnArray.subList(0, size));
    }

    /*!
     * It builds a StarStim frame request for starting the stimtulation.
     *
     * \return Byte array that contains the request
     */
    //public static ArrayList<Byte> buildStartStimulationFrame ();

    /*!
     * It builds a StarStim frame request for stopping the stimtulation.
     *
     * \return Byte array that contains the request.
     */
    //public static ArrayList<Byte> buildStopStimulationFrame ();

    /*!
     * It builds a StarStim frame request for starting the impedance
     * measurements.
     *
     * \return Byte array that contains the request.
     */
    //public static ArrayList<Byte> buildStartImpedanceFrame ();

    /*!
     * It builds a StarStim frame request for stopping the impedance
     * measurements.
     *
     * \return Byte array that contains the request.
     */
    //public static ArrayList<Byte> buildStopImpedanceFrame ();

    /*!
     * It builds a StarStim frame request for reading a stimulation
     * configuration register.
     *
     * \param address Address of the stimulation register.
     *
     * \param length Number of consecutive register to be read
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildReadStimRegisterFrame (Byte address, Byte length){
    	Logger logger = Logger.getInstance();
    	
        Byte[] frame = new Byte[256];
        Byte[] nullArray = new Byte[1];
        int size = buildRequestFrame(frame,  false, false, false, false, false,
                					 false, false, false, false, false, false,
                					 true, false, false, false, false, false,
                					 (byte) 0, (byte) 0, nullArray, address, length, nullArray, 
                                     (byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray,
                                     (byte) 0, (byte) 0, nullArray);

        if (size < 0)
        {
            logger.info( "Error building start EEG frame", Logger.LOG_FILE_ON );
        }
        else if (size >= 256)
        {
        	logger.info( "Critical: buffer too short. Program may not work properly", Logger.LOG_FILE_ON );
        }
        
        ArrayList<Byte> returnArray = new ArrayList<Byte>(Arrays.asList(frame)); 
        
        return new ArrayList<Byte>( returnArray.subList(0, size));
    }

    /*!
     * It builds an enzo3G/StarStim frame request for reading a EEG
     * configuration register.
     *
     * \param address Address of the EEG register.
     *
     * \param length Number of consecutive register to be read
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildReadEEGRegisterFrame (Byte address, Byte length){
    	Logger logger = Logger.getInstance();
    	
        Byte[] frame = new Byte[256];
        Byte[] nullArray = new Byte[1];
        int size = buildRequestFrame(frame,  false, false, false, false, false,
                					 false, false, false, false, false, true,
                					 false, false, false, false, false, false,
                                     address, length, nullArray, (byte) 0, (byte) 0, nullArray, 
                                     (byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray,
                                     (byte) 0, (byte) 0, nullArray);

        if (size < 0)
        {
            logger.info( "Error building read EEG register frame", Logger.LOG_FILE_ON );
        }
        else if (size >= 256)
        {
        	logger.info( "Critical: buffer too short. Program may not work properly", Logger.LOG_FILE_ON );
        }
        
        ArrayList<Byte> returnArray = new ArrayList<Byte>(Arrays.asList(frame)); 
        
        return new ArrayList<Byte>( returnArray.subList(0, size));
    }

    /*!
     * It builds an enzo3G/StarStim frame request for reading an
     * accelerometer configuration register.
     *
     * \param address Address of the accelerometer register.
     *
     * \param length Number of consecutive register to be read
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildReadAccRegisterFrame (Byte address, Byte length){
    	Logger logger = Logger.getInstance();
    	
        Byte[] frame = new Byte[256];
        Byte[] nullArray = new Byte[1];
        int size = buildRequestFrame(frame,  false, false, false, false, false,
                					 false, false, false, false, false, false,
                					 false, false, true, false, false, false,
                					 (byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray, 
                                     address, length, nullArray, (byte) 0, (byte) 0, nullArray,
                                     (byte) 0, (byte) 0, nullArray);

        if (size < 0)
        {
            logger.info( "Error building read ACCEL register frame", Logger.LOG_FILE_ON );
        }
        else if (size >= 256)
        {
        	logger.info( "Critical: buffer too short. Program may not work properly", Logger.LOG_FILE_ON );
        }
        
        ArrayList<Byte> returnArray = new ArrayList<Byte>(Arrays.asList(frame)); 
        
        return new ArrayList<Byte>( returnArray.subList(0, size));
    }

    /*!
     * It builds a StarStim frame request for writing a stimulation
     * configuration register.
     *
     * \param address Address of the stimulation register.
     *
     * \param value Pointer to the set of values to be written.
     *
     * \param length Number of register to be updated with the provided values.
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildWriteStimRegisterFrame (Byte address, Byte[] value, Byte length){
    	Logger logger = Logger.getInstance();
    	
        Byte[] frame = new Byte[256];
        Byte[] nullArray = new Byte[1];
        int size = buildRequestFrame(frame, false, false, false, false, false,
                							false, false, false, false, true, false,
                							false, false, false, false, false, false,
                							(byte) 0, (byte) 0, nullArray, address, length, value,  
                							(byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray,
                							(byte) 0, (byte) 0, nullArray);        
        
        if (size < 0)
        {
            logger.info( "Error building start Stim register frame", Logger.LOG_FILE_ON );
        }
        else if (size >= 256)
        {
        	logger.info( "Critical: buffer too short. Program may not work properly", Logger.LOG_FILE_ON );
        }
        
        ArrayList<Byte> returnArray = new ArrayList<Byte>(Arrays.asList(frame)); 
        
        return new ArrayList<Byte>( returnArray.subList(0, size));  
    }

    /*!
     * It builds an enzo3G/StarStim frame request for writing a EEG
     * configuration register.
     *
     * \param address Address of the EEG register.
     *
     * \param value Pointer to the set of values to be written.
     *
     * \param length Number of register to be updated with the provided values.
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildWriteEEGRegisterFrame (Byte address, Byte[] value, Byte length){
    	Logger logger = Logger.getInstance();
    	
        Byte[] frame = new Byte[256];
        Byte[] nullArray = new Byte[1];
        int size = buildRequestFrame(frame, false, false, false, false, false,
                							false, false, false, true, false, false,
                							false, false, false, false, false, false,
                							address, length, value, (byte) 0, (byte) 0, nullArray, 
                							(byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray,
                							(byte) 0, (byte) 0, nullArray);

        if (size < 0)
        {
            logger.info( "Error building write EEG register frame", Logger.LOG_FILE_ON );
        }
        else if (size >= 256)
        {
        	logger.info( "Critical: buffer too short. Program may not work properly", Logger.LOG_FILE_ON );
        }
        
        ArrayList<Byte> returnArray = new ArrayList<Byte>(Arrays.asList(frame)); 
        
        return new ArrayList<Byte>( returnArray.subList(0, size));  
    }

    /*!
     * It builds an enzo3G/StarStim frame request for writing an
     * accelerometer configuration register.
     *
     * \param address Address of the accelerometer register.
     *
     * \param value Pointer to the set of values to be written.
     *
     * \param length Number of register to be updated with the provided values.
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildWriteAccRegisterFrame (Byte address, Byte[] value, Byte length){
    	Logger logger = Logger.getInstance();
    	
        Byte[] frame = new Byte[256];
        Byte[] nullArray = new Byte[1];
        int size = buildRequestFrame(frame, false, false, false, false, false,
                						false, false, false, false, false, false,
                						false, false, false, true, false, false,
                						(byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray, 
                						address, length, value, (byte) 0, (byte) 0, nullArray,
                						(byte) 0, (byte) 0, nullArray);

        if (size < 0)
        {
            logger.info( "Error building start ACCEL write frame", Logger.LOG_FILE_ON );
        }
        else if (size >= 256)
        {
        	logger.info( "Critical: buffer too short. Program may not work properly", Logger.LOG_FILE_ON );
        }
        
        ArrayList<Byte> returnArray = new ArrayList<Byte>(Arrays.asList(frame)); 
        
        return new ArrayList<Byte>( returnArray.subList(0, size));  
    }

    /*!
     * It builds a StarStim frame request for writing the online stimulation
     * signal.
     *
     * \param channelInfo Byte that indicates at bit level (lsb: channel 0 -
     * msb: channel 7) which channels are set.
     *
     * \param numSamples Number of samples per channel that are set in the
     * request frame.
     *
     * \param samples Pointer to the samples that are set in the request frame.
     *
     * \return Byte array that contains the request.
     */
    //public static ArrayList<Byte> buildWriteRealTimeSignalRequest
    //                                            (  char channelInfo,
    //                                             int numSamples,
    //                                               char * samples);

    /*!
     * It builds a StarStim frame request for writing an SDCard Register
     *
     * \param address Address of the accelerometer register.
     *
     * \param value Pointer to the set of values to be written.
     *
     * \param length Number of register to be updated with the provided values.
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildWriteSDCardRegisterFrame (Byte address, Byte[] value, Byte length){
    	Logger logger = Logger.getInstance();
    	
        Byte[] frame = new Byte[256];
        Byte[] nullArray = new Byte[1];
        int size = buildRequestFrame(frame, false, false, false, false, false,
                						false, false, false, false, false, false,
                						false, false, false, false, false, true,
                						(byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray, 
                						(byte) 0, (byte) 0, nullArray, address, length, value, 
                						(byte) 0, (byte) 0, nullArray);

        if (size < 0)
        {
            logger.info( "Error building start ACCEL write frame", Logger.LOG_FILE_ON );
        }
        else if (size >= 256)
        {
        	logger.info( "Critical: buffer too short. Program may not work properly", Logger.LOG_FILE_ON );
        }
        
        ArrayList<Byte> returnArray = new ArrayList<Byte>(Arrays.asList(frame)); 
        
        return new ArrayList<Byte>( returnArray.subList(0, size));  
    }

    /*!
     * It builds an enzo3G/StarStim frame request for the battery
     * measurement.
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildReadBatteryRequest (){
    	Logger logger = Logger.getInstance();
    	
        Byte[] frame = new Byte[256];
        Byte[] nullArray = new Byte[1];
        int size = buildRequestFrame(frame, false, false, false, false, false,
                							false, true, false, false, false, false,
                							false, false, false, false, false, false,
                							(byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray, 
                							(byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray,
                							(byte) 0, (byte) 0, nullArray);

        if (size < 0)
        {
            logger.info( "Error building start Read Battery frame", Logger.LOG_FILE_ON );
        }
        else if (size >= 256)
        {
        	logger.info( "Critical: buffer too short. Program may not work properly", Logger.LOG_FILE_ON );
        }
        
        ArrayList<Byte> returnArray = new ArrayList<Byte>(Arrays.asList(frame)); 
        
        return new ArrayList<Byte>( returnArray.subList(0, size));  
    }

    /*!
     * It builds an enzo3G/StarStim frame request for keeping the
     * communication alive.
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildNullRequest (){
        Byte[] frame = new Byte[9];

        int pos = 0;
        frame[pos++] = SFD_0;
        frame[pos++] = SFD_1;
        frame[pos++] = SFD_2;
        frame[pos++] = 0; // length
        frame[pos++] = (byte) 0x80; // NULL REQUEST COMMAND
        frame[pos++] = (byte) 0x00; // NULL REQUEST COMMAND
        frame[pos++] = EFD_0;
        frame[pos++] = EFD_1;
        frame[pos++] = EFD_2;
        frame[3] = (byte) pos;

        return new ArrayList<Byte>(Arrays.asList(frame)); 
    }

    /*!
     *It builds an enzo3G/StarStim frame request for getting the firmware
     *version of the device and the battery.
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildFirmwareVersionBatteryFrame (){
    	Logger logger = Logger.getInstance();
    	
        Byte[] frame = new Byte[256];
        Byte[] nullArray = new Byte[1];
        int size = buildRequestFrame(frame,  false, false, false, false, false,
                							 false, true, false, false, false, false,
                							 false, true, false, false, false, false,
                                     (byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray, 
                                     (byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray,
                                     (byte) 0, (byte) 0, nullArray);

        if (size < 0)
        {
            logger.info( "Error building start EEG frame", Logger.LOG_FILE_ON );
        }
        else if (size >= 256)
        {
        	logger.info( "Critical: buffer too short. Program may not work properly", Logger.LOG_FILE_ON );
        }
        
        ArrayList<Byte> returnArray = new ArrayList<Byte>(Arrays.asList(frame)); 
        
        return new ArrayList<Byte>( returnArray.subList(0, size));    	
    }

    /*!
     *It builds an enzo3G/StarStim frame request for getting the firmware
     *version of the device.
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildFirmwareVersionFrame (){
    	Logger logger = Logger.getInstance();
    	
        Byte[] frame = new Byte[256];
        Byte[] nullArray = new Byte[1];
        int size = buildRequestFrame(frame, false, false, false, false, false,
                							false, false, false, false, false, false,
                							false, true, false, false, false, false,
                							(byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray, 
                							(byte) 0, (byte) 0, nullArray, (byte) 0, (byte) 0, nullArray,
                							(byte) 0, (byte) 0, nullArray);

        if (size < 0)
        {
            logger.info( "Error building start EEG frame", Logger.LOG_FILE_ON );
        }
        else if (size >= 256)
        {
        	logger.info( "Critical: buffer too short. Program may not work properly", Logger.LOG_FILE_ON );
        }
        
        ArrayList<Byte> returnArray = new ArrayList<Byte>(Arrays.asList(frame)); 
        
        return new ArrayList<Byte>( returnArray.subList(0, size)); 
    }

    /*!
     * It builds an enzo3G/StartStim frame that requests starting the sending
     * of beacons.
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildStartBeaconRequest (){
    	Logger logger = Logger.getInstance();
    	
        Byte[] frame = new Byte[9];

        int pos = 0;
        frame[pos++] = SFD_0;
        frame[pos++] = SFD_1;
        frame[pos++] = SFD_2;
        frame[pos++] = 0; // length
        frame[pos++] = 0x7F; // START BEACON COMMAND
        frame[pos++] = 0x7F; // START BEACON COMMAND
        frame[pos++] = EFD_0;
        frame[pos++] = EFD_1;
        frame[pos++] = EFD_2;
        frame[3] = (byte) pos;
        
        return new ArrayList<Byte>(Arrays.asList(frame)); 
         
    }

    /*!
     * It builds an enzo3G/StartStim frame that requests stopping the sending
     * of beacons.
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildStopBeaconRequest (){
    	Logger logger = Logger.getInstance();
    	
        Byte[] frame = new Byte[9];

        int pos = 0;
        frame[pos++] = SFD_0;
        frame[pos++] = SFD_1;
        frame[pos++] = SFD_2;
        frame[pos++] = 0; // length
        frame[pos++] = 0x00; // STOP BEACON COMMAND
        frame[pos++] = 0x00; // STOP BEACON COMMAND
        frame[pos++] = EFD_0;
        frame[pos++] = EFD_1;
        frame[pos++] = EFD_2;
        frame[3] = (byte) pos;
        
        return new ArrayList<Byte>(Arrays.asList(frame));
    }

    /*!
     * It builds an enzo3G/StartStim frame that requests a rename on the radio
     *
     * \param name Targetted name for the radio
     *
     * \return Byte array that contains the request.
     */
    public static ArrayList<Byte> buildRenameRadioRequest(String name){
    	Logger logger = Logger.getInstance();
    	
    	/*
        Byte[] frame = new Byte[9];

        int pos = 0;
        frame[pos++] = SFD_0;
        frame[pos++] = SFD_1;
        frame[pos++] = SFD_2;
        frame[pos++] = 0; // length
        frame[pos++] = 0x00; // STOP BEACON COMMAND
        frame[pos++] = 0x00; // STOP BEACON COMMAND
        frame[pos++] = EFD_0;
        frame[pos++] = EFD_1;
        frame[pos++] = EFD_2;
        frame[3] = (byte) pos;
        /**/
        
        ArrayList<Byte> frame = new ArrayList<Byte>();
        frame.add((byte) SFD_0);
        frame.add((byte) SFD_1);
        frame.add((byte) SFD_2);

        frame.add( (byte) 0 );  // length
        frame.add( (byte) 0 );  // ST_B0
        frame.add( (byte) 0 );  // ST_B1

        for( int i = 0; i < name.length(); i++){
        	frame.add( (byte) name.charAt(i) ); 
        }
        
        
        frame.add((byte) EFD_0);
        frame.add((byte) EFD_1);
        frame.add((byte) EFD_2);
        
        frame.set(3, (byte) frame.size() );
        
        return frame;    	
    }
    
    
    /*!
     * It sets the firmware version of the enzo3G/StartStim.
     *
     * \param firmwareVersion.
     */
    void setFirmwareVersion(int firmwareVersion){
    	_firmwareVersion = firmwareVersion;
    }

    /*!
     * It indicates whether the device is doing stimulation.
     *
     * \param value.
     */
    public void setIsStimulating(boolean value){
        _isStimulating = value;
    }

    /*!
     * Sets whether StarstimProtocol is working in multisample
     *
     * \param value.
     */
    public void multipleSample( int value ){
        _multipleSample = value;
    }

    /*!
     * Returns the _multiSample parameter
     */
    public int multipleSample(  ){
        return _multipleSample;
    }
    
    /*!
     * Sets whether StarstimProtocol is working with compressed EEG samples
     *
     * \param value.
     */
    public void eegCompressionType( EEGCompressionType value ){
        _eegCompresssionType = value;
    }

    /*!
     * Returns the _eegCompressionType parameter
     */
    public EEGCompressionType eegCompressionType( ){
        return _eegCompresssionType;
    }
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
