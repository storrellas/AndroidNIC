package com.icognos.deviceManager;

public class StarStimRegister {

	//  -- Attributtes --
	//  -----------------
	
    /*!
     * \property StarStimRegister::_updated
     *
     * Boolean that holds whether the register's value has been updated.
     */
    private boolean _updated;

    /*!
     * \property StarStimRegister::_value
     *
     * The value of the register.
     */
    private int _value;
	
	
    //  -- METHODS --
    // --------------
	
    /*!
     * Defaut constructor.
     */
    public StarStimRegister(){
    	_updated = false;
    	_value = 0;
    }
    
    /*!
     * It returns the value of the register.
     *
     * \return Register's value.
     */
    public int value(){
    	_updated = false;
    	return _value;
    }

    /*!
     * It sets the value of the register.
     */
    public void setValue (int value){
        _value = value;
        _updated = true;
    }

    /*!
     * It returns whether the value of the register was updated since the last
     * time it was accessed.
     *
     * \return True if the register was updated since the las access, false
     * otherwise.
     */
    public boolean updated (){
        return _updated;
    }
    
}
