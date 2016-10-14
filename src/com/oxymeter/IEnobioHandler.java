package com.icognos;

import com.icognos.deviceManager.ChannelData;

public interface IenzoHandler {

    /*!
     * Signal that is emitted reporting the remaining percentage of the
     * battery.
     *
     * \param level Remaining battery in percentage from 0 to 100%
     */
    void reportBatteryLevel(int level);

    /*!
     * Signal that is emitted reporting the firmware version of the
     * device.
     *
     * \param version Firmware version
     * \param is1000SPS
     */
    void reportFirmwareVersion(int firmwareVersion, int is1000SPS);

    /*!
     * Signal that is emitted reporting the new accelerometer data
     * received.
     *
     * \param data The new received sample data
     *        We use channels 1,2 and 3
     */
    void newAccelerometerData(ChannelData data);


    /*!
     * Signal that is emitted whenever a new stimulation data is received.
     *
     * \param data The new received sample data
     */
    void newStimulationData(ChannelData data);

    /*!
     * Signal that is emitted whenever a new EEG data is received.
     *
     * \param data The new received sample data
     */
    void newEEGData(ChannelData data);

    /*!
     * Signal that is emitted whenever a new Impedance data is received.
     *
     * \param data The new received sample data
     *
     * \param timeStamp The timeStamp of the data
     */
    void newImpedanceData(ChannelData data, long timeStamp);

    /*!
     * Signal that is emitted whenever the enzo3G/StarStim device status
     * changes.
     *
     * \param deviceStatus New enzo3G/Starstim status. The value 0xFF means
     * that the device does not repond. For different values the meaning is at
     * bit level as it follows: Bit 6: Stimulation ON/OFF, Bit 5: EEG Streaming
     * ON/OFF, Impedance measurement ON/OFF. Bit set to one means ON.
     */
    void newDeviceStatus(int deviceStatus);

    /*!
     * Signal that is emitted whenever the enzo3G/StarStim connection is lost
     * so that the led starts blinking before actually closing the connection.
     */
    void startBlinking();

    /*!
     * Signal that is emitted whenever the enzo3G/StarStim connection is
     * recovered so that the led stops blinking.
     */
    void stopBlinking();

    /*!
     * Send information on percentage of packets lost.
     */
    void newPacketLossData(double percentage);
}
