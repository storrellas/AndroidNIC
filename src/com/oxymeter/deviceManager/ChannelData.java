package com.neuroelectrics.deviceManager;

public class ChannelData {

	//  -- Attributtes --
	//  -----------------
    /*!
     * \property ChannelData::_channelInfo
     *
     * Variable that holds the channel information value that indicates the
     * channels that are present in the current sample
     */
    private int _channelInfo;

    /*!
     * \property ChannelData::_data
     *
     * Vector that hols the sample value of all the channels
     */
    //int _data[32];
    int[] _data;

    /*!
     * \property ChannelData::_timeStamp
     *
     * Timestamp for the channel data sample
     */
    long _timeStamp;

    /*!
     * \property ChannelData::_isRepeated
     *
     * Boolean to control if current packet is repeated to compensate for packet loss
     */
    boolean _isRepeated;
	
    //  -- METHODS --
    // --------------
	
    /*!
     * Default constructor
     */
    public ChannelData(){
    	_data = new int[32];
    	for(int i = 0; i < 32; i++) _data[i] = 0;
    	_channelInfo = 0;
    	
    	_timeStamp = 0;
    }
    
    /*!
     * It returns an integer that reports which channels are present in the
     * current sample
     *
     * \return The returned value is organized at bit level. The bits set to
     * '1' mean that the channels are being reported. The data for those
     * channels whose channel info bits are set to zero is undefined. The least
     * significant bit corresponds to channel 0
     */
    public int channelInfo () {return _channelInfo;}

    /*!
     * It returns the pointer to the vector that contains the samples of all
     * the channels
     *
     * \return pointer to the numberOfChannels-lenght vector
     */
    public int[] data () {return _data;}

    /*!
     * It sets the channel info value
     *
     * \param channelInfo Value which information at bit level regarding the
     * channels that are reported in the current sample. The least significant
     * bit corresponds to channel 0
     */
    public void setChannelInfo (int channelInfo) {_channelInfo = channelInfo;}

    /*!
     * It sets the value of the channel for a specific channel
     *
     * \param index 0-based channel index
     *
     * \param value value of the sample
     */
    public void setData(int index, int value) {if (index < 32) _data[index] = value;}

    /*!
     * It gets the timestamp of the sample
     */
    public long timestamp () {return _timeStamp;}

    /*!
     * It sets the timestamp of the sample
     */
    public void setTimestamp (long value) { _timeStamp = value;}

    /*!
     * It indicates whether the sample is a repeated one due to packet loss
     */
    public void setRepeated (boolean value) { _isRepeated = value;}

    /*!
     * It returns whether the sample is a repeated one due to packet loss
     */
    boolean isRepeated () { return _isRepeated;}
    
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
