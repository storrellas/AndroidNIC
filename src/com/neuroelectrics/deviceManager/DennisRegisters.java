package com.neuroelectrics.deviceManager;

public class DennisRegisters {

	// -----------------------------------------------------
	// EEG Registers
	// -----------------------------------------------------
	
	
	/** EEG_REGS_ID: Indicate the device characteristics */
	public static int EEG_REGS_ID_ADDR =               (0x00);

	/** EEG_REGS_CONFIG_1: EEG Configuration Register 1 
	 *                    Configures sampling rate and resolution mode
	 *
	 */
	public static int EEG_REGS_CONFIG_1_ADDR =         (0x01);

	/** EEG_REGS_CONFIG_2: Configures the rest signal generation
	 *
	 *    Bit [7:6]: Must be set to 00
	 *    Bit     5: WCT Chooping Scheme
	 *    Bit     4: Test Source
	 *    Bit     3: Must allways set to '0'
	 *    Bit     2: TEST_AMP: Test Signal amplitude
	 *    Bit [1:0]: Signal frequency
	 *
	 * */
	public static int EEG_REGS_CONFIG_2_ADDR =        (0x02);

	/**
	 * EEG_REGS_CONFIG_3: Configures multi-reference and RLD operation
	 *
	 * */
	public static int EEG_REGS_CONFIG_3_ADDR =        (0x03);

	/**
	 * EEG_REGS_LOFF: Configures the Lead-Off detection operation
	 * */
	public static int EEG_REGS_LOFF_ADDR =            (0x04);

	/**
	 * EEG_REGS_CH_1_SET: Configure the power mode, PGA gain and multiplexer settings channels. 
	 * */
	public static int EEG_REGS_CH_1_SET_ADDR =        (0x05);

	/**
	 * EEG_REGS_CH_2_SET: Configure the power mode, PGA gain and multiplexer settings channels. 
	 * */
	public static int EEG_REGS_CH_2_SET_ADDR =        (0x06);

	/**
	 * EEG_REGS_CH_3_SET: Configure the power mode, PGA gain and multiplexer settings channels. 
	 * */
	public static int EEG_REGS_CH_3_SET_ADDR =        (0x07);

	/**
	 * EEG_REGS_CH_4_SET: Configure the power mode, PGA gain and multiplexer settings channels. 
	 * */
	public static int EEG_REGS_CH_4_SET_ADDR =        (0x08);

	/**
	 * EEG_REGS_CH_5_SET: Configure the power mode, PGA gain and multiplexer settings channels. 
	 * */
	public static int EEG_REGS_CH_5_SET_ADDR =        (0x09);

	/**
	 * EEG_REGS_CH_6_SET: Configure the power mode, PGA gain and multiplexer settings channels. 
	 * */
	public static int EEG_REGS_CH_6_SET_ADDR =        (0x0A);

	/**
	 * EEG_REGS_CH_7_SET: Configure the power mode, PGA gain and multiplexer settings channels. 
	 * */
	public static int EEG_REGS_CH_7_SET_ADDR =        (0x0B);

	/**
	 * EEG_REGS_CH_8_SET: Configure the power mode, PGA gain and multiplexer settings channels. 
	 * */
	public static int EEG_REGS_CH_8_SET_ADDR =        (0x0C);

	/**
	 * EEG_REGS_RLD_SENS_P: Controls the selection of the positive signals from each channel
	 *                      for right leg drive derivation.
	 *
	 * */
	public static int EEG_REGS_RLD_SENS_P_ADDR =      (0x0D);

	/**
	 * EEG_REGS_RLD_SENS_N: Controls the selection of the necagiteve signals from each channel
	 *                      for right leg drive derivation
	 * */
	public static int EEG_REGS_RLD_SENS_N_ADDR =       (0x0E);

	/** EEG_REGS_LOFF_SENS_P: Selects the positive side from each channel for lead-off detection.
	 *
	 * */
	public static int EEG_REGS_LOFF_SENS_P_ADDR =      (0x0F);

	/** EEG_REGS_LOFF_SENS_N: Selects the negativeside from each channel for lead-off detection.
	 *
	 * */
	public static int EEG_REGS_LOFF_SENS_N_ADDR =     (0x10);

	/** 
	 * EEG_REGS_LOFF_FLIP: Controls the direction of the current used for lead-off
	 * */
	public static int EEG_REGS_LOFF_FLIP_ADDR =       (0x11);

	/**
	 * EEG_REGS_LOFF_STAT_P: Stores the statis whether the positive electrode on each
	 *                       channel is ON or OFF.
	 *
	 * */
	public static int  EEG_REGS_LOFF_STAT_P_ADDR =      (0x12);

	/**
	 * EEG_REGS_LOFF_STAT_N: Stores the statis whether the negative electrode on each
	 *                       channel is ON or OFF.
	 *
	 * */
	public static int  EEG_REGS_LOFF_STAT_N_ADDR =      (0x13);

	/**
	 * EEG_REGS_GPIO: General purpose I/O Register that controls the action of the
	 *                GPIO pins. 
	 *
	 * */
	public static int  EEG_REGS_GPIO_ADDR =             (0x14);

	/**
	 * EEG_REGS_PACE: provides the PACE controls that configure the channel signal
	 *                used to feed the external PACE detect circuitry.
	 * */
	public static int  EEG_REGS_PACE_ADDR =             (0x15);

	/**
	 * EEG_REGS_RESP: provides the controls for the respiration circuitry.
	 *
	 * */
	public static int  EEG_REGS_RESP_ADDR =             (0x16);

	/**
	 * EEG_REGS_CONFIG_4: Configures respiration parameters such as modulation
	 *                    frequency
	 * */
	public static int  EEG_REGS_CONFIG_4_ADDR =         (0x17);

	/** 
	 * EEG_REGS_WCT_1: Configures the device WCT circuit channel selection and
	 *                 the augmented mode
	 * */
	public static int  EEG_REGS_WCT_1_ADDR =            (0x18);

	/** 
	 * EEG_REGS_WCT_2: Configures the device WCT circuit channel selection.
	 * */
	public static int  EEG_REGS_WCT_2_ADDR =            (0x19);


	/** 
	 * EEG_CH_INFO: Configures the EEG_CH_INFO that is required for 20-channel EEG.
	 *              For compatibility the format is the same the CH_INFO managed on
	 *              stim_mgr, that is, '0' means EEG active channel and '1' means
	 *              that EEG channel is off. The LSB is ch_0 while the bit 19 is
	 *              the channel 19.
	 * */
	public static int  EEG_CH_INFO_ADDR =                (0x40);

	/** 
	 * EEG_N_INSTRUMENT_CHANNEL: EEG channels in the instrument
	 */
	public static int  EEG_N_INSTRUMENT_CHANNEL =        (0x44);

	public static int  EEG_DATA_FAIL_ADDR =              (0x48);


	/* Enables the multisample mode 
	 *  '1' : multisample enabled
	 *  '0' : multisample disabled
	 */
	public static int  EEG_MULTISAMPLE_MODE_ADDR =       (0x50);


	/* Configures number of samples per beacon */ 
	public static int  EEG_REG_SAMPLES_PER_BEACON_ADDR = (0x51);

	/* Configures whether the system compresses EEG samples 
	 *  '2' : compression enabled @ 12 bit/EEGSample
	 *  '1' : compression enabled @ 16 bit/EEGSample
	 *  '0' : compression disabled
	 */
	public static int  EEG_COMPRESSION_TYPE_ADDR =       (0x52);


	/* Configures the rate for the EEG Streaming
	 *  '8' : EEG rate 75SPS
	 *  '4' : EEG rate 125SPS
	 *  '2' : EEG rate 250SPS
	 *  '1' : EEG rate 500SPS
	 */
	public static int  EEG_STREAMING_RATE_500SPS = 1;
	public static int  EEG_STREAMING_RATE_250SPS = 2;
	public static int  EEG_STREAMING_RATE_125SPS = 4;
	public static int  EEG_STREAMING_RATE_75SPS  = 8;
	public static int  EEG_STREAMING_RATE_ADDR =         (0x53);
	
	
	// -----------------------------------------------------
	// ACCEL Registers
	// -----------------------------------------------------
	
	/** ACCEL_MODE_ADDR: Indicates the mode of operation of the ACCEL manager. 
	 *                   '0' indicates that ACCEL is OFF
	 *                   '1' indicates that ACCEL is being read and reported to the host */
	public static int   ACCEL_MODE_ADDR =               (0x00);


	/** ACCEL_THR_TAP_ADDR */
	public static int   ACCEL_THR_TAP_ADDR =            (0x01);

	/** OFSX */
	public static int   ACCEL_OFSX_ADDR =               (0x02);

	/** OFSY */
	public static int   ACCEL_OFSY_ADDR =               (0x03);

	/** OFSZ */
	public static int   ACCEL_OFSZ_ADDR =               (0x04);

	/** DUR */
	public static int   ACCEL_DUR_ADDR =                (0x05);

	/** LAT */
	public static int   ACCEL_LAT_ADDR =                (0x06);

	/** WIN */
	public static int   ACCEL_WIN_ADDR =                (0x07);

	/** THR_ACT */
	public static int   ACCEL_THR_ACT_ADDR =            (0x08);

	/** THR_INACT */
	public static int   ACCEL_THR_INACT_ADDR =          (0x09);

	/** TIME_INACT */
	public static int   ACCEL_TIME_INACT_ADDR =         (0x0A);

	/** ACT_INACT_CTL */
	public static int   ACCEL_ACT_INACT_CTL_ADDR =      (0x0B);

	/** THR_FF */
	public static int   ACCEL_THR_FF_ADDR =             (0x0C);

	/** TIME_FF */
	public static int   ACCEL_TIME_FF_ADDR =            (0x0D);

	/** TAP_AXES */
	public static int   ACCEL_TAP_AXES_ADDR =           (0x0F);

	/** ACT_TAP_ST */
	public static int   ACCEL_ACT_TAP_ST_ADDR =         (0x10);

	/** BW_RATE */
	public static int   ACCEL_BW_RATE_ADDR =            (0x11);

	/** POWER_CTL */
	public static int   ACCEL_POWER_CTL_ADDR =          (0x12);

	/** INT_ENABLE */
	public static int   ACCEL_INT_ENABLE_ADDR =         (0x13);

	/** INT_MAP */
	public static int   ACCEL_INT_MAP_ADDR =            (0x14);

	/** INT_SRC */
	public static int   ACCEL_INT_SRC_ADDR =            (0x15);

	/** DATA_FORMAT */
	public static int   ACCEL_DATA_FORMAT_ADDR =        (0x16);

	/** DATAX0 */
	public static int   ACCEL_DATAX0_ADDR =             (0x17);

	/** DATAX1 */
	public static int   ACCEL_DATAX1_ADDR =             (0x18);

	/** DATAY0 */
	public static int   ACCEL_DATAY0_ADDR =             (0x19);

	/** DATAY1 */
	public static int   ACCEL_DATAY1_ADDR =             (0x20);

	/** DATAZ0 */
	public static int   ACCEL_DATAZ0_ADDR =             (0x21);

	/** DATAZ1 */
	public static int   ACCEL_DATAZ1_ADDR =             (0x22);

	/** FIFO_CTL */
	public static int   ACCEL_FIFO_CTL_ADDR =           (0x23);

	/** FIFO_ST */
	public static int   ACCEL_FIFO_ST_ADDR =            (0x24);
	
	
	// -----------------------------------------------------
	// SDCARD Registers
	// -----------------------------------------------------
	
	/** SDCARD file_name (128 characters) - 128 byte */
	public static int SDCARD_FILE_NAME =            (0x00);

	/** SDCARD_EXPERIMENT_TIME [s] (4,294,967,296 sec - 1193046.47 hours - 49710.27 days) - 4 byte */
	public static int SDCARD_EXPERIMENT_TIME =      (0x80);

	/* MODE CONFIGURATION :                                                     */
	/* bit 0 : Enable Recording -  1 Recording ON / 0 Recording OFF byte        */
	/* bit 1 : Experiment time enabled - 1 ExpTime Enabled / 0 ExpTime disabled */
	public static int SDCARD_RECORDING_MODE =       (0x84);
	
	
	// -----------------------------------------------------
	// STIMULATION Registers
	// -----------------------------------------------------	
	

	/* DAC REGISTERS                                                              */

	/**
	 * DAC Input shoft register BYTE-0
	 */
	public static int STM_REGS_DAC_B0_ADDR =           (0);

	/**
	 * DAC Input shoft register BYTE-1
	 */
	public static int STM_REGS_DAC_B1_ADDR =           (1);

	/**
	 * DAC Input shoft register BYTE-2
	 */
	public static int STM_REGS_DAC_B2_ADDR =           (2);

	/**
	 * DAC Input shoft register BYTE-3
	 */
	public static int STM_REGS_DAC_B3_ADDR =           (3);
	
	
	/* OPERATION REGISTERS                                                        */
	
	/**
	 * STM_REGS_CH_INFO (1 bytes): Indicates the CHANNELS that must be reported
	 *                             though the communication link.
	 *
	 *   Bit-0: '0' channel 0 is NOT reported. '1' Channel 0 is reported
	 *   Bit-1: '0' channel 1 is NOT reported. '1' Channel 1 is reported
	 *   Bit-2: '0' channel 2 is NOT reported. '1' Channel 2 is reported
	 *   Bit-3: '0' channel 3 is NOT reported. '1' Channel 3 is reported
	 *   Bit-4: '0' channel 4 is NOT reported. '1' Channel 4 is reported
	 *   Bit-5: '0' channel 5 is NOT reported. '1' Channel 5 is reported
	 *   Bit-6: '0' channel 6 is NOT reported. '1' Channel 6 is reported
	 *   Bit-7: '0' channel 7 is NOT reported. '1' Channel 7 is reported
	 * */
	public static int STM_REGS_CH_INFO_ADDR =          (4);

	/** STM_REGS_MODE: Mode of Stimulation 
	 *  0: Stimulation of pre-defined waveforms
	 *  1: User Defined Stimulation
	 *
	 * */

	public static int STM_REGS_MODE_ADDR =             (5);

	/** STM_REGS_CFG_0: Tobe defined  */
	public static int STM_REGS_CFG_1_ADDR =            (6);

	/**
	 * STM_REGS_CH_FREE (1 byte): This byte indicate which channel is free to
	 *                            Fix the current so that the summatory of all
	 *                            channel is equal to zero.
	 *
	 *   Only and only one bit of this byte can be different than '0'.
	 *   Otherwhise the FREE channel is set to CH-0 by default
	 *
	 *   Bit-0: '1' CH-0 configured as free channel.
	 *   Bit-1: '1' CH-1 configured as free channel.
	 *   Bit-2: '1' CH-2 configured as free channel.
	 *   Bit-3: '1' CH-3 configured as free channel.
	 *   Bit-4: '1' CH-4 configured as free channel.
	 *   Bit-5: '1' CH-5 configured as free channel.
	 *   Bit-6: '1' CH-6 configured as free channel.
	 *   Bit-7: '1' CH-7 configured as free channel.
	 * */
	public static int STM_REGS_CH_FREE_ADDR =          (7);

	/**
	 * STM_REGS_RATE_DEC: Decimation factor applied to the sampling rate to
	 *                    generate the stimation waveform.
	 *
	 *    By default the sampling rate is FRATE = 1000 SPS.
	 *    In order to add flexibility to the waveform generation the sampling rate
	 *    can be reduced. The resulting sampling rate is 
	 *
	 *    1000 / STM_REGS_RATE_DEC SPS
	 *
	 *    RATE_DEC (bits 7 - 0): division factor applied to the sampling rate
	 * */
	public static int STM_REGS_RATE_DEC_ADDR =         (8);


	/**
	 * STM_REGS_RAMP_UP (2 bytes): Duration in secs. of the ramp-up section
	 *                             in the stimulation waveform
	 *
	 *    STM_REGS_RAMP_UP_0: Least significant byte
	 *    STM_REGS_RAMP_UP_1: Most significant byte
	 *
	 * */
	public static int STM_REGS_RAMP_UP_0_ADDR =        (9);
	public static int STM_REGS_RAMP_UP_1_ADDR =        (10);

	/**
	 * STM_REGS_RAMP_DN: Duration in secs. of the ramp-down section
	 *                   in the stimulation waveform
	 *
	 *    STM_REGS_RAMP_DN_0: Least significant byte
	 *    STM_REGS_RAMP_DN_1: Most significant byte
	 *
	 * */
	public static int STM_REGS_RAMP_DN_0_ADDR =        (11);
	public static int STM_REGS_RAMP_DN_1_ADDR =        (12);

	/**
	 * STM_REGS_TIME: Duration in secs. of the stimulation waveform 
	 *                NOT including the ramp-up and ramp-down regions.
	 *
	 *    STM_REGS_TIME_0: Least significant byte
	 *    STM_REGS_TIME_1: Most significant byte
	 *
	 * */
	public static int STM_REGS_TIME_0_ADDR =           (13);
	public static int STM_REGS_TIME_1_ADDR =           (14);

	
	/* GAUSSIAN WAVEFORM GENERATOR                                                */
	
	/**
	 * STM_REGS_CH0_SEED: Define the initial seed of the random number generator 
	 *                    used to generat random waveform in channel 0
	 *
	 *    STM_REGS_CH0_SEED_0: Least significant byte
	 *    STM_REGS_CH0_SEED_1: Most significant byte
	 * */
	public static int STM_REGS_CH0_SEED_0_ADDR =       (15);
	public static int STM_REGS_CH0_SEED_1_ADDR =       (16);

	/**
	 * STM_REGS_CH1_SEED: Define the initial seed of the random number generator 
	 *                    used to generat random waveform in channel 1
	 *
	 *    STM_REGS_CH1_SEED_0: Least significant byte
	 *    STM_REGS_CH1_SEED_1: Most significant byte
	 * */
	public static int STM_REGS_CH1_SEED_0_ADDR =       (17);
	public static int STM_REGS_CH1_SEED_1_ADDR =       (18);

	/**
	 * STM_REGS_CH2_SEED: Define the initial seed of the random number generator 
	 *                    used to generat random waveform in channel 2
	 *
	 *    STM_REGS_CH2_SEED_0: Least significant byte
	 *    STM_REGS_CH2_SEED_1: Most significant byte
	 * */
	public static int STM_REGS_CH2_SEED_0_ADDR =       (19);
	public static int STM_REGS_CH2_SEED_1_ADDR =       (20);

	/**
	 * STM_REGS_CH3_SEED: Define the initial seed of the random number generator 
	 *                    used to generat random waveform in channel 3
	 *
	 *    STM_REGS_CH3_SEED_0: Least significant byte
	 *    STM_REGS_CH3_SEED_1: Most significant byte
	 * */
	public static int STM_REGS_CH3_SEED_0_ADDR =       (21);
	public static int STM_REGS_CH3_SEED_1_ADDR =       (22);

	/**
	 * STM_REGS_CH4_SEED: Define the initial seed of the random number generator 
	 *                    used to generat random waveform in channel 4
	 *
	 *    STM_REGS_CH4_SEED_0: Least significant byte
	 *    STM_REGS_CH4_SEED_1: Most significant byte
	 * */
	public static int STM_REGS_CH4_SEED_0_ADDR =       (23);
	public static int STM_REGS_CH4_SEED_1_ADDR =       (24);

	/**
	 * STM_REGS_CH5_SEED: Define the initial seed of the random number generator 
	 *                    used to generat random waveform in channel 5
	 *
	 *    STM_REGS_CH5_SEED_0: Least significant byte
	 *    STM_REGS_CH5_SEED_1: Most significant byte
	 * */
	public static int STM_REGS_CH5_SEED_0_ADDR =       (25);
	public static int STM_REGS_CH5_SEED_1_ADDR =       (26);

	/**
	 * STM_REGS_CH6_SEED: Define the initial seed of the random number generator 
	 *                    used to generat random waveform in channel 6
	 *
	 *    STM_REGS_CH6_SEED_0: Least significant byte
	 *    STM_REGS_CH6_SEED_1: Most significant byte
	 * */
	public static int STM_REGS_CH6_SEED_0_ADDR =       (27);
	public static int STM_REGS_CH6_SEED_1_ADDR =       (28);

	/**
	 * STM_REGS_CH7_SEED: Define the initial seed of the random number generator 
	 *                    used to generat random waveform in channel 7
	 *
	 *    STM_REGS_CH7_SEED_0: Least significant byte
	 *    STM_REGS_CH7_SEED_1: Most significant byte
	 * */
	public static int STM_REGS_CH7_SEED_0_ADDR =       (29);
	public static int STM_REGS_CH7_SEED_1_ADDR =       (30);


	/**
	 * STM_REGS_CH0_GAUSS (48-bit): Configures the Gaussian waveofrm generator for channel 0
	 *   
	 *   By default the output of the gaussian generator is mapped to the range from -2mA to 2mA.
	 *   The standard deviation of the default waveform is STD = 640uA, which is 6.25 times the 
	 *   allowable dynamic range. The output waveform can be scaled to generate a random signal
	 *   with lower STD by applying a gain factor of the form K = GAIN_N/GAIN_D where K <= 1.
	 *
	 *   GAIN_N (bits 7 - 0): Numerator of the gain factor K
	 *   GAIN_D (bits 15- 8): Denominator of the gain factor K
	 * */
	public static int STM_REGS_CH0_GAUSS_GAIN_N_ADDR = (31);
	public static int STM_REGS_CH0_GAUSS_GAIN_D_ADDR = (32);

	/**
	 * STM_REGS_CH1_GAUSS (48-bit): Configures the Gaussian waveofrm generator for channel 1
	 *   
	 *   By default the output of the gaussian generator is mapped to the range from -2mA to 2mA.
	 *   The standard deviation of the default waveform is STD = 640uA, which is 6.25 times the 
	 *   allowable dynamic range. The output waveform can be scaled to generate a random signal
	 *   with lower STD by applying a gain factor of the form K = GAIN_N/GAIN_D where K <= 1.
	 *
	 *
	 *   GAIN_N (bits 7 - 0): Numerator of the gain factor K
	 *   GAIN_D (bits 15- 8): Denominator of the gain factor K
	 * */
	public static int STM_REGS_CH1_GAUSS_GAIN_N_ADDR = (33);
	public static int STM_REGS_CH1_GAUSS_GAIN_D_ADDR = (34);

	/**
	 * STM_REGS_CH2_GAUSS (48-bit): Configures the Gaussian waveofrm generator for channel 2
	 *   
	 *   By default the output of the gaussian generator is mapped to the range from -2mA to 2mA.
	 *   The standard deviation of the default waveform is STD = 640uA, which is 6.25 times the 
	 *   allowable dynamic range. The output waveform can be scaled to generate a random signal
	 *   with lower STD by applying a gain factor of the form K = GAIN_N/GAIN_D where K <= 1.
	 *
	 *
	 *   GAIN_N (bits 7 - 0): Numerator of the gain factor K
	 *   GAIN_D (bits 15- 8): Denominator of the gain factor K
	 * */
	public static int STM_REGS_CH2_GAUSS_GAIN_N_ADDR = (35);
	public static int STM_REGS_CH2_GAUSS_GAIN_D_ADDR = (36);

	/**
	 * STM_REGS_CH3_GAUSS (48-bit): Configures the Gaussian waveofrm generator for channel 3
	 *   
	 *   By default the output of the gaussian generator is mapped to the range from -2mA to 2mA.
	 *   The standard deviation of the default waveform is STD = 640uA, which is 6.25 times the 
	 *   allowable dynamic range. The output waveform can be scaled to generate a random signal
	 *   with lower STD by applying a gain factor of the form K = GAIN_N/GAIN_D where K <= 1.
	 *
	 *
	 *   GAIN_N (bits 7 - 0): Numerator of the gain factor K
	 *   GAIN_D (bits 15- 8): Denominator of the gain factor K
	 * */
	public static int STM_REGS_CH3_GAUSS_GAIN_N_ADDR = (37);
	public static int STM_REGS_CH3_GAUSS_GAIN_D_ADDR = (38);

	/**
	 * STM_REGS_CH4_GAUSS (48-bit): Configures the Gaussian waveofrm generator for channel 4
	 *   
	 *   By default the output of the gaussian generator is mapped to the range from -2mA to 2mA.
	 *   The standard deviation of the default waveform is STD = 640uA, which is 6.25 times the 
	 *   allowable dynamic range. The output waveform can be scaled to generate a random signal
	 *   with lower STD by applying a gain factor of the form K = GAIN_N/GAIN_D where K <= 1.
	 *
	 *
	 *   GAIN_N (bits 7 - 0): Numerator of the gain factor K
	 *   GAIN_D (bits 15- 8): Denominator of the gain factor K
	 * */
	public static int STM_REGS_CH4_GAUSS_GAIN_N_ADDR = (39);
	public static int STM_REGS_CH4_GAUSS_GAIN_D_ADDR = (40);

	/**
	 * STM_REGS_CH5_GAUSS (48-bit): Configures the Gaussian waveofrm generator for channel 5
	 *   
	 *   By default the output of the gaussian generator is mapped to the range from -2mA to 2mA.
	 *   The standard deviation of the default waveform is STD = 640uA, which is 6.25 times the 
	 *   allowable dynamic range. The output waveform can be scaled to generate a random signal
	 *   with lower STD by applying a gain factor of the form K = GAIN_N/GAIN_D where K <= 1.
	 *
	 *
	 *   GAIN_N (bits 7 - 0): Numerator of the gain factor K
	 *   GAIN_D (bits 15- 8): Denominator of the gain factor K
	 * */
	public static int STM_REGS_CH5_GAUSS_GAIN_N_ADDR = (41);
	public static int STM_REGS_CH5_GAUSS_GAIN_D_ADDR = (42);

	/**
	 * STM_REGS_CH6_GAUSS (48-bit): Configures the Gaussian waveofrm generator for channel 6
	 *   
	 *   By default the output of the gaussian generator is mapped to the range from -2mA to 2mA.
	 *   The standard deviation of the default waveform is STD = 640uA, which is 6.25 times the 
	 *   allowable dynamic range. The output waveform can be scaled to generate a random signal
	 *   with lower STD by applying a gain factor of the form K = GAIN_N/GAIN_D where K <= 1.
	 *
	 *
	 *   GAIN_N (bits 7 - 0): Numerator of the gain factor K
	 *   GAIN_D (bits 15- 8): Denominator of the gain factor K
	 * */
	public static int STM_REGS_CH6_GAUSS_GAIN_N_ADDR = (43);
	public static int STM_REGS_CH6_GAUSS_GAIN_D_ADDR = (44);

	/**
	 * STM_REGS_CH7_GAUSS (48-bit): Configures the Gaussian waveofrm generator for channel 7
	 *   
	 *   By default the output of the gaussian generator is mapped to the range from -2mA to 2mA.
	 *   The standard deviation of the default waveform is STD = 640uA, which is 6.25 times the 
	 *   allowable dynamic range. The output waveform can be scaled to generate a random signal
	 *   with lower STD by applying a gain factor of the form K = GAIN_N/GAIN_D where K <= 1.
	 *
	 *
	 *   GAIN_N (bits 7 - 0): Numerator of the gain factor K
	 *   GAIN_D (bits 15- 8): Denominator of the gain factor K
	 *   OFFSET (bits 31-16): Offset applied to the output waveform. 
	 * */
	public static int STM_REGS_CH7_GAUSS_GAIN_N_ADDR = (45);
	public static int STM_REGS_CH7_GAUSS_GAIN_D_ADDR = (46);
	
	/* SINUSOIDAL WAVEFORM GENERATOR                                              */

	/**
	 * STM_REGS_CH0_SIN (32-bit): Configures the Sinusoidal waveform in channel 0
	 *
	 *   FREQ  (bits 15- 0): Frequency of the sinusoidal waveform (FREQ = FSIN(Hz)*65536/1000)
	 *   GAIN_N(bits 23-16): Numerator of Gain factor applied to the sinusoidal waveform from 1 to 255
	 *   GAIN_D(bits 31-24): Denominator of the Gain factor applied to the sinusoidal waveform from 1 to 255
	 *
	 *   BYTE-0: Bits ( 7- 0) of the FERQ field
	 *   BYTE-1: Bits (15- 8) of the FERQ field
	 *   BYTE-2: Bits ( 7- 0) of the GAIN_N field
	 *   BYTE-3: Bits ( 7- 0) of the GAIN_D field
	 * */
	public static int STM_REGS_CH0_SIN_FREQ_0_ADDR =   (47);
	public static int STM_REGS_CH0_SIN_FREQ_1_ADDR =   (48);
	public static int STM_REGS_CH0_SIN_PHASE_0_ADDR =  (49);
	public static int STM_REGS_CH0_SIN_PHASE_1_ADDR =  (50);
	public static int STM_REGS_CH0_SIN_GAIN_N_ADDR =   (51);
	public static int STM_REGS_CH0_SIN_GAIN_D_ADDR =   (52);

	/**
	 * STM_REGS_CH1_SIN (32-bit): Configures the Sinusoidal waveform in channel 1
	 *
	 *   FREQ  (bits 15- 0): Frequency of the sinusoidal waveform (FREQ = FSIN(Hz)*65536/1000)
	 *   GAIN_N(bits 23-16): Numerator of Gain factor applied to the sinusoidal waveform from 1 to 255
	 *   GAIN_D(bits 31-24): Denominator of the Gain factor applied to the sinusoidal waveform from 1 to 255
	 *
	 *   BYTE-0: Bits ( 7- 0) of the FERQ field
	 *   BYTE-1: Bits (15- 8) of the FERQ field
	 *   BYTE-2: Bits ( 7- 0) of the GAIN_N field
	 *   BYTE-3: Bits ( 7- 0) of the GAIN_D field
	 * */
	public static int STM_REGS_CH1_SIN_FREQ_0_ADDR =   (53);
	public static int STM_REGS_CH1_SIN_FREQ_1_ADDR =   (54);
	public static int STM_REGS_CH1_SIN_PHASE_0_ADDR =  (55);
	public static int STM_REGS_CH1_SIN_PHASE_1_ADDR =  (56);
	public static int STM_REGS_CH1_SIN_GAIN_N_ADDR =   (57);
	public static int STM_REGS_CH1_SIN_GAIN_D_ADDR =   (58);

	/**
	 * STM_REGS_CH2_SIN (32-bit): Configures the Sinusoidal waveform in channel 2
	 *
	 *   FREQ  (bits 15- 0): Frequency of the sinusoidal waveform (FREQ = FSIN(Hz)*65536/1000)
	 *   GAIN_N(bits 23-16): Numerator of Gain factor applied to the sinusoidal waveform from 1 to 255
	 *   GAIN_D(bits 31-24): Denominator of the Gain factor applied to the sinusoidal waveform from 1 to 255
	 *
	 *   BYTE-0: Bits ( 7- 0) of the FERQ field
	 *   BYTE-1: Bits (15- 8) of the FERQ field
	 *   BYTE-2: Bits ( 7- 0) of the GAIN_N field
	 *   BYTE-3: Bits ( 7- 0) of the GAIN_D field
	 * */
	public static int STM_REGS_CH2_SIN_FREQ_0_ADDR =   (59);
	public static int STM_REGS_CH2_SIN_FREQ_1_ADDR =   (60);
	public static int STM_REGS_CH2_SIN_PHASE_0_ADDR =  (61);
	public static int STM_REGS_CH2_SIN_PHASE_1_ADDR =  (62);
	public static int STM_REGS_CH2_SIN_GAIN_N_ADDR =   (63);
	public static int STM_REGS_CH2_SIN_GAIN_D_ADDR =   (64);

	/**
	 * STM_REGS_CH3_SIN (32-bit): Configures the Sinusoidal waveform in channel 3
	 *
	 *   FREQ  (bits 15- 0): Frequency of the sinusoidal waveform (FREQ = FSIN(Hz)*65536/1000)
	 *   GAIN_N(bits 23-16): Numerator of Gain factor applied to the sinusoidal waveform from 1 to 255
	 *   GAIN_D(bits 31-24): Denominator of the Gain factor applied to the sinusoidal waveform from 1 to 255
	 *
	 *   BYTE-0: Bits ( 7- 0) of the FERQ field
	 *   BYTE-1: Bits (15- 8) of the FERQ field
	 *   BYTE-2: Bits ( 7- 0) of the GAIN_N field
	 *   BYTE-3: Bits ( 7- 0) of the GAIN_D field
	 * */
	public static int STM_REGS_CH3_SIN_FREQ_0_ADDR =   (65);
	public static int STM_REGS_CH3_SIN_FREQ_1_ADDR =   (66);
	public static int STM_REGS_CH3_SIN_PHASE_0_ADDR =  (67);
	public static int STM_REGS_CH3_SIN_PHASE_1_ADDR =  (68);
	public static int STM_REGS_CH3_SIN_GAIN_N_ADDR =   (69);
	public static int STM_REGS_CH3_SIN_GAIN_D_ADDR =   (70);

	/**
	 * STM_REGS_CH4_SIN (32-bit): Configures the Sinusoidal waveform in channel 4
	 *
	 *   FREQ  (bits 15- 0): Frequency of the sinusoidal waveform (FREQ = FSIN(Hz)*65536/1000)
	 *   GAIN_N(bits 23-16): Numerator of Gain factor applied to the sinusoidal waveform from 1 to 255
	 *   GAIN_D(bits 31-24): Denominator of the Gain factor applied to the sinusoidal waveform from 1 to 255
	 *
	 *   BYTE-0: Bits ( 7- 0) of the FERQ field
	 *   BYTE-1: Bits (15- 8) of the FERQ field
	 *   BYTE-2: Bits ( 7- 0) of the GAIN_N field
	 *   BYTE-3: Bits ( 7- 0) of the GAIN_D field
	 * */
	public static int STM_REGS_CH4_SIN_FREQ_0_ADDR =   (71);
	public static int STM_REGS_CH4_SIN_FREQ_1_ADDR =   (72);
	public static int STM_REGS_CH4_SIN_PHASE_0_ADDR =  (73);
	public static int STM_REGS_CH4_SIN_PHASE_1_ADDR =  (74);
	public static int STM_REGS_CH4_SIN_GAIN_N_ADDR =   (75);
	public static int STM_REGS_CH4_SIN_GAIN_D_ADDR =   (76);

	/**
	 * STM_REGS_CH5_SIN (32-bit): Configures the Sinusoidal waveform in channel 5
	 *
	 *   FREQ  (bits 15- 0): Frequency of the sinusoidal waveform (FREQ = FSIN(Hz)*65536/1000)
	 *   GAIN_N(bits 23-16): Numerator of Gain factor applied to the sinusoidal waveform from 1 to 255
	 *   GAIN_D(bits 31-24): Denominator of the Gain factor applied to the sinusoidal waveform from 1 to 255
	 *
	 *   BYTE-0: Bits ( 7- 0) of the FERQ field
	 *   BYTE-1: Bits (15- 8) of the FERQ field
	 *   BYTE-2: Bits ( 7- 0) of the GAIN_N field
	 *   BYTE-3: Bits ( 7- 0) of the GAIN_D field
	 * */
	public static int STM_REGS_CH5_SIN_FREQ_0_ADDR =   (77);
	public static int STM_REGS_CH5_SIN_FREQ_1_ADDR =   (78);
	public static int STM_REGS_CH5_SIN_PHASE_0_ADDR =  (79);
	public static int STM_REGS_CH5_SIN_PHASE_1_ADDR =  (80);
	public static int STM_REGS_CH5_SIN_GAIN_N_ADDR =   (81);
	public static int STM_REGS_CH5_SIN_GAIN_D_ADDR =   (82);

	/**
	 * STM_REGS_CH6_SIN (32-bit): Configures the Sinusoidal waveform in channel 6
	 *
	 *   FREQ  (bits 15- 0): Frequency of the sinusoidal waveform (FREQ = FSIN(Hz)*65536/1000)
	 *   GAIN_N(bits 23-16): Numerator of Gain factor applied to the sinusoidal waveform from 1 to 255
	 *   GAIN_D(bits 31-24): Denominator of the Gain factor applied to the sinusoidal waveform from 1 to 255
	 *
	 *   BYTE-0: Bits ( 7- 0) of the FERQ field
	 *   BYTE-1: Bits (15- 8) of the FERQ field
	 *   BYTE-2: Bits ( 7- 0) of the GAIN_N field
	 *   BYTE-3: Bits ( 7- 0) of the GAIN_D field
	 * */
	public static int STM_REGS_CH6_SIN_FREQ_0_ADDR =   (83);
	public static int STM_REGS_CH6_SIN_FREQ_1_ADDR =   (84);
	public static int STM_REGS_CH6_SIN_PHASE_0_ADDR =  (85);
	public static int STM_REGS_CH6_SIN_PHASE_1_ADDR =  (86);
	public static int STM_REGS_CH6_SIN_GAIN_N_ADDR =   (87);
	public static int STM_REGS_CH6_SIN_GAIN_D_ADDR =   (88);

	/**
	 * STM_REGS_CH7_SIN (32-bit): Configures the Sinusoidal waveform in channel 7
	 *
	 *   FREQ  (bits 15- 0): Frequency of the sinusoidal waveform (FREQ = FSIN(Hz)*65536/1000)
	 *   GAIN_N(bits 23-16): Numerator of Gain factor applied to the sinusoidal waveform from 1 to 255
	 *   GAIN_D(bits 31-24): Denominator of the Gain factor applied to the sinusoidal waveform from 1 to 255
	 *
	 *   BYTE-0: Bits ( 7- 0) of the FERQ field
	 *   BYTE-1: Bits (15- 8) of the FERQ field
	 *   BYTE-2: Bits ( 7- 0) of the GAIN_N field
	 *   BYTE-3: Bits ( 7- 0) of the GAIN_D field
	 * */
	public static int STM_REGS_CH7_SIN_FREQ_0_ADDR =   (89);
	public static int STM_REGS_CH7_SIN_FREQ_1_ADDR =   (90);
	public static int STM_REGS_CH7_SIN_PHASE_0_ADDR =  (91);
	public static int STM_REGS_CH7_SIN_PHASE_1_ADDR =  (92);
	public static int STM_REGS_CH7_SIN_GAIN_N_ADDR =   (93);
	public static int STM_REGS_CH7_SIN_GAIN_D_ADDR =   (94);

	/* DC WAVEFORM GENERATOR                                                      */
	/**
	 * STM_REGS_CH0_DC (16-bit): Configure the offset current in DC mode
	 *
	 *   The generated DC offset is OFFSET*4/65536 (mA)
	 *
	 *   OFFSET_0 (bits 7 - 0): The Least significant byte of the  offset
	 *   OFFSET_1 (bits 15- 8): The Most significant byte of the  offset
	 *
	 * */
	public static int STM_REGS_CH0_DC_OFF_0_ADDR =     (95);
	public static int STM_REGS_CH0_DC_OFF_1_ADDR =     (96);

	/**
	 * STM_REGS_CH1_DC (16-bit): Configure the offset current in DC mode
	 *
	 *   The generated DC offset is OFFSET*4/65536 (mA)
	 *
	 *   OFFSET_0 (bits 7 - 0): The Least significant byte of the  offset
	 *   OFFSET_1 (bits 15- 8): The Most significant byte of the  offset
	 *
	 * */
	public static int STM_REGS_CH1_DC_OFF_0_ADDR =     (97);
	public static int STM_REGS_CH1_DC_OFF_1_ADDR =     (98);

	/**
	 * STM_REGS_CH2_DC (16-bit): Configure the offset current in DC mode
	 *
	 *   The generated DC offset is OFFSET*4/65536 (mA)
	 *
	 *   OFFSET_0 (bits 7 - 0): The Least significant byte of the  offset
	 *   OFFSET_1 (bits 15- 8): The Most significant byte of the  offset
	 *
	 * */
	public static int STM_REGS_CH2_DC_OFF_0_ADDR =     (99);
	public static int STM_REGS_CH2_DC_OFF_1_ADDR =     (100);

	/**
	 * STM_REGS_CH3_DC (16-bit): Configure the offset current in DC mode
	 *
	 *   The generated DC offset is OFFSET*4/65536 (mA)
	 *
	 *   OFFSET_0 (bits 7 - 0): The Least significant byte of the  offset
	 *   OFFSET_1 (bits 15- 8): The Most significant byte of the  offset
	 *
	 * */
	public static int STM_REGS_CH3_DC_OFF_0_ADDR =     (101);
	public static int STM_REGS_CH3_DC_OFF_1_ADDR =     (102);

	/**
	 * STM_REGS_CH4_DC (16-bit): Configure the offset current in DC mode
	 *
	 *   The generated DC offset is OFFSET*4/65536 (mA)
	 *
	 *   OFFSET_0 (bits 7 - 0): The Least significant byte of the  offset
	 *   OFFSET_1 (bits 15- 8): The Most significant byte of the  offset
	 *
	 * */
	public static int STM_REGS_CH4_DC_OFF_0_ADDR =     (103);
	public static int STM_REGS_CH4_DC_OFF_1_ADDR =     (104);

	/**
	 * STM_REGS_CH5_DC (16-bit): Configure the offset current in DC mode
	 *
	 *   The generated DC offset is OFFSET*4/65536 (mA)
	 *
	 *   OFFSET_0 (bits 7 - 0): The Least significant byte of the  offset
	 *   OFFSET_1 (bits 15- 8): The Most significant byte of the  offset
	 *
	 * */
	public static int STM_REGS_CH5_DC_OFF_0_ADDR =     (105);
	public static int STM_REGS_CH5_DC_OFF_1_ADDR =     (106);

	/**
	 * STM_REGS_CH6_DC (16-bit): Configure the offset current in DC mode
	 *
	 *   The generated DC offset is OFFSET*4/65536 (mA)
	 *
	 *   OFFSET_0 (bits 7 - 0): The Least significant byte of the  offset
	 *   OFFSET_1 (bits 15- 8): The Most significant byte of the  offset
	 *
	 * */
	public static int STM_REGS_CH6_DC_OFF_0_ADDR =     (107);
	public static int STM_REGS_CH6_DC_OFF_1_ADDR =     (108);

	/**
	 * STM_REGS_CH7_DC (16-bit): Configure the offset current in DC mode
	 *
	 *   The generated DC offset is OFFSET*4/65536 (mA)
	 *
	 *   OFFSET_0 (bits 7 - 0): The Least significant byte of the  offset
	 *   OFFSET_1 (bits 15- 8): The Most significant byte of the  offset
	 *
	 * */
	public static int STM_REGS_CH7_DC_OFF_0_ADDR =     (109);
	public static int STM_REGS_CH7_DC_OFF_1_ADDR =     (110);
	
	
	/* IMPEDANCE MEASUREMENT                                                      */
	
	/** STM_IMP_FREQ_DEC: Indicates the frequency decimation of the Impedance
	 *                    measurement with respect the Stimulation Sampling rate.
	 *                    In 1 out of STM_IMP_FERQ_DEC the impedance will be reported.
	 *
	 * */
	public static int STM_IMP_FREQ_DEC_ADDR =          (111);

	/** STM_IMP_THR: Configurable threshold to allow automatically stops stimulation
	 *               in cse that measured impedance is higher than this threshold
	 *
	 *               IMP_THR_0: represents the LSB of a 16-bit number
	 *               IMP_THR_1: represents the LSB of a 16-bit number
	 * */
	public static int STM_IMP_THR_0_ADDR =             (112);
	public static int STM_IMP_THR_1_ADDR =             (113);


	/** STM_ERROR: Indicates the reason why the stimulation is stopped
	 *
	 *    0: End of programmed waveform is reached
	 *    1: Impedance check failed
	 *    2: Communication problems
	 *    3: Stop Requested by NIC
	 *
	 * */
	public static int STM_ERROR_ADDR =                 (114);


	/** STM_REPORT_DATA: Used to configure whether the STM data must be reported 
	 *                   to the host or not.
	 *
	 *     0: STM waveform samples will NOT sent to the host
	 *     1: STM waveform samples will be sent to the host
	 *
	 *  Default: 0
	 * */
	public static int STM_REPORT_DATA_ADDR =           (115);

	
	/* ONLINE STIMULATION CHANGE - DC Waveform generator                          */

	/**
	 * STM_OL_CH0_DC_OFF (24-bit): Configures the online stimulation change in DC Waverform generator
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH0_DC_OFF_0_ADDR =       (116);
	public static int STM_OL_CH0_DC_OFF_1_ADDR =       (117);
	public static int STM_OL_CH0_DC_OFF_TRANS0_ADDR =  (118);
	public static int STM_OL_CH0_DC_OFF_TRANS1_ADDR =  (119);

	/**
	 * STM_OL_CH1_DC_OFF (24-bit): Configures the online stimulation change in DC Waverform generator 
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH1_DC_OFF_0_ADDR =       (120);
	public static int STM_OL_CH1_DC_OFF_1_ADDR =       (121);
	public static int STM_OL_CH1_DC_OFF_TRANS0_ADDR =  (122);
	public static int STM_OL_CH1_DC_OFF_TRANS1_ADDR =  (123);

	/**
	 * STM_OL_CH2_DC_OFF (24-bit): Configures the online stimulation change in DC Waverform generator
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH2_DC_OFF_0_ADDR =       (124);
	public static int STM_OL_CH2_DC_OFF_1_ADDR =       (125);
	public static int STM_OL_CH2_DC_OFF_TRANS0_ADDR =  (126);
	public static int STM_OL_CH2_DC_OFF_TRANS1_ADDR =  (127);

	/**
	 * STM_OL_CH3_DC_OFF (24-bit): Configures the online stimulation change in DC Waverform generator
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH3_DC_OFF_0_ADDR =       (128);
	public static int STM_OL_CH3_DC_OFF_1_ADDR =       (129);
	public static int STM_OL_CH3_DC_OFF_TRANS0_ADDR =  (130);
	public static int STM_OL_CH3_DC_OFF_TRANS1_ADDR =  (131);

	/**
	 * STM_OL_CH4_DC_OFF (24-bit): Configures the online stimulation change in DC Waverform generator
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH4_DC_OFF_0_ADDR =       (132);
	public static int STM_OL_CH4_DC_OFF_1_ADDR =       (133);
	public static int STM_OL_CH4_DC_OFF_TRANS0_ADDR =  (134);
	public static int STM_OL_CH4_DC_OFF_TRANS1_ADDR =  (135);

	/**
	 * STM_OL_CH5_DC_OFF (24-bit): Configures the online stimulation change in DC Waverform generator
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH5_DC_OFF_0_ADDR =       (136);
	public static int STM_OL_CH5_DC_OFF_1_ADDR =       (137);
	public static int STM_OL_CH5_DC_OFF_TRANS0_ADDR =  (138);
	public static int STM_OL_CH5_DC_OFF_TRANS1_ADDR =  (139);

	/**
	 * STM_OL_CH6_DC_OFF (24-bit): Configures the online stimulation change in DC Waverform generator
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH6_DC_OFF_0_ADDR =       (140);
	public static int STM_OL_CH6_DC_OFF_1_ADDR =       (141);
	public static int STM_OL_CH6_DC_OFF_TRANS0_ADDR =  (142);
	public static int STM_OL_CH6_DC_OFF_TRANS1_ADDR =  (143);

	/**
	 * STM_OL_CH7_DC_OFF (24-bit): Configures the online stimulation change in DC Waverform generator
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH7_DC_OFF_0_ADDR =       (144);
	public static int STM_OL_CH7_DC_OFF_1_ADDR =       (145);
	public static int STM_OL_CH7_DC_OFF_TRANS0_ADDR =  (146);
	public static int STM_OL_CH7_DC_OFF_TRANS1_ADDR =  (147);


	/* ONLINE STIMULATION CHANGE - Sinusoidal Waveform Generator                  */

	/**
	 * STM_OL_CH0_SIN (24-bit): Configures the online stimulation change in Sinusoidal Waverform generator
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH0_SIN_GAIN_N_ADDR =  (148);
	public static int STM_OL_CH0_SIN_GAIN_D_ADDR =  (149);
	public static int STM_OL_CH0_SIN_TRANS0_ADDR =  (150);
	public static int STM_OL_CH0_SIN_TRANS1_ADDR =  (151);

	/**
	 * STM_OL_CH1_SIN (24-bit): Configures the online stimulation change in Sinusoidal Waverform generator
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH1_SIN_GAIN_N_ADDR =  (152);
	public static int STM_OL_CH1_SIN_GAIN_D_ADDR =  (153);
	public static int STM_OL_CH1_SIN_TRANS0_ADDR =  (154);
	public static int STM_OL_CH1_SIN_TRANS1_ADDR =  (155);

	/**
	 * STM_OL_CH2_SIN (24-bit): Configures the online stimulation change in Sinusoidal Waverform generator
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH2_SIN_GAIN_N_ADDR =  (156);
	public static int STM_OL_CH2_SIN_GAIN_D_ADDR =  (157);
	public static int STM_OL_CH2_SIN_TRANS0_ADDR =  (158);
	public static int STM_OL_CH2_SIN_TRANS1_ADDR =  (159);

	/**
	 * STM_OL_CH3_SIN (24-bit): Configures the online stimulation change in Sinusoidal Waverform generator
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH3_SIN_GAIN_N_ADDR =  (160);
	public static int STM_OL_CH3_SIN_GAIN_D_ADDR =  (161);
	public static int STM_OL_CH3_SIN_TRANS0_ADDR =  (162);
	public static int STM_OL_CH3_SIN_TRANS1_ADDR =  (163);

	/**
	 * STM_OL_CH4_SIN (24-bit): Configures the online stimulation change in Sinusoidal Waverform generator
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH4_SIN_GAIN_N_ADDR =  (164);
	public static int STM_OL_CH4_SIN_GAIN_D_ADDR =  (165);
	public static int STM_OL_CH4_SIN_TRANS0_ADDR =  (166);
	public static int STM_OL_CH4_SIN_TRANS1_ADDR =  (167);

	/**
	 * STM_OL_CH5_SIN (24-bit): Configures the online stimulation change in Sinusoidal Waverform generator
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH5_SIN_GAIN_N_ADDR =  (168);
	public static int STM_OL_CH5_SIN_GAIN_D_ADDR =  (169);
	public static int STM_OL_CH5_SIN_TRANS0_ADDR =  (170);
	public static int STM_OL_CH5_SIN_TRANS1_ADDR =  (171);

	/**
	 * STM_OL_CH5_SIN (24-bit): Configures the online stimulation change in Sinusoidal Waverform generator
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH6_SIN_GAIN_N_ADDR =  (172);
	public static int STM_OL_CH6_SIN_GAIN_D_ADDR =  (173);
	public static int STM_OL_CH6_SIN_TRANS0_ADDR =  (174);
	public static int STM_OL_CH6_SIN_TRANS1_ADDR =  (175);

	/**
	 * STM_OL_CH5_SIN (24-bit): Configures the online stimulation change in Sinusoidal Waverform generator
	 *
	 *   BYTE-0: Bits ( 7- 0) of the targeted value change
	 *   BYTE-1: Bits (15- 8) of the targeted value change
	 *   BYTE-2: Bits ( 7- 0) milliseconds of the transition
	 *   BYTE-3: Bits (15- 8) milliseconds of the transition
	 * */
	public static int STM_OL_CH7_SIN_GAIN_N_ADDR =  (176);
	public static int STM_OL_CH7_SIN_GAIN_D_ADDR =  (177);
	public static int STM_OL_CH7_SIN_TRANS0_ADDR =  (178);
	public static int STM_OL_CH7_SIN_TRANS1_ADDR =  (179);



	/* ========================================================================== */

	/** STM_FLT_COEF_0_ADDR: Address of the first coefficient of the FIR filter 
	 *                       applied to the ouput signal.
	 *                       The filter is composed of 50 Coefficients which are 
	 */
	public static int STM_FLT_COEF_0_ADDR =            (200);
	public static int STM_FLT_COEF_1_ADDR =            (201);
	public static int STM_FLT_COEF_2_ADDR =            (202);
	public static int STM_FLT_COEF_3_ADDR =            (203);
	public static int STM_FLT_COEF_4_ADDR =            (204);
	public static int STM_FLT_COEF_5_ADDR =            (205);
	public static int STM_FLT_COEF_6_ADDR =            (206);
	public static int STM_FLT_COEF_7_ADDR =            (207);
	public static int STM_FLT_COEF_8_ADDR =            (208);
	public static int STM_FLT_COEF_9_ADDR =            (209);
	public static int STM_FLT_COEF_10_ADDR =           (210);
	public static int STM_FLT_COEF_11_ADDR =           (211);
	public static int STM_FLT_COEF_12_ADDR =           (212);
	public static int STM_FLT_COEF_13_ADDR =           (213);
	public static int STM_FLT_COEF_14_ADDR =           (214);
	public static int STM_FLT_COEF_15_ADDR =           (215);
	public static int STM_FLT_COEF_16_ADDR =           (216);
	public static int STM_FLT_COEF_17_ADDR =           (217);
	public static int STM_FLT_COEF_18_ADDR =           (218);
	public static int STM_FLT_COEF_19_ADDR =           (219);
	public static int STM_FLT_COEF_20_ADDR =           (220);
	public static int STM_FLT_COEF_21_ADDR =           (221);
	public static int STM_FLT_COEF_22_ADDR =           (222);
	public static int STM_FLT_COEF_23_ADDR =           (223);
	public static int STM_FLT_COEF_24_ADDR =           (224);
	public static int STM_FLT_COEF_25_ADDR =           (225);
	public static int STM_FLT_COEF_26_ADDR =           (226);
	public static int STM_FLT_COEF_27_ADDR =           (227);
	public static int STM_FLT_COEF_28_ADDR =           (228);
	public static int STM_FLT_COEF_29_ADDR =           (229);
	public static int STM_FLT_COEF_30_ADDR =           (230);
	public static int STM_FLT_COEF_31_ADDR =           (231);
	public static int STM_FLT_COEF_32_ADDR =           (232);
	public static int STM_FLT_COEF_33_ADDR =           (233);
	public static int STM_FLT_COEF_34_ADDR =           (234);
	public static int STM_FLT_COEF_35_ADDR =           (235);
	public static int STM_FLT_COEF_36_ADDR =           (236);
	public static int STM_FLT_COEF_37_ADDR =           (237);
	public static int STM_FLT_COEF_38_ADDR =           (238);
	public static int STM_FLT_COEF_39_ADDR =           (239);
	public static int STM_FLT_COEF_40_ADDR =           (240);
	public static int STM_FLT_COEF_41_ADDR =           (241);
	public static int STM_FLT_COEF_42_ADDR =           (242);
	public static int STM_FLT_COEF_43_ADDR =           (243);
	public static int STM_FLT_COEF_44_ADDR =           (244);
	public static int STM_FLT_COEF_45_ADDR =           (245);
	public static int STM_FLT_COEF_46_ADDR =           (246);
	public static int STM_FLT_COEF_47_ADDR =           (247);
	public static int STM_FLT_COEF_48_ADDR =           (248);
	public static int STM_FLT_COEF_49_ADDR =           (249);
	public static int STM_FLT_COEF_50_ADDR =           (250);


	/** STM_FLT_CH_INFO_ADDR: FLT_CH_INFO Select the channel in which the Filter
	 *                        will be applied
	 */
	public static int STM_FLT_LEN =                    (50);
	public static int STM_FLT_CH_INFO_ADDR =           (STM_FLT_COEF_0_ADDR+STM_FLT_LEN+1);

	/** STM_FLT_DIV_ADDR: Division factor to apply to the output of the FIR filter
	 *
	 *
	 * */
	public static int STM_FLT_DIV_0_ADDR =             (STM_FLT_CH_INFO_ADDR+2);
	public static int STM_FLT_DIV_1_ADDR =             (STM_FLT_CH_INFO_ADDR+3);
	
}
