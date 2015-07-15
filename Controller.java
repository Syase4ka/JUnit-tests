package main;


/**
 * Enable management of a range of controlled devices. Functionality:
 * 	- Add remove a device to/from the list of devices to be controlled.  
 *  - Send a new input reading to a controlled device.
 * 
 * 	- First change: the bug with deviceID validation is fixed. 
 * Previously the program threw an exception for valid device IDs with mask [0-9]+. Now it's fixed by adding "!" before the condition 
 * (reversing it).
 * 								
 *  - Second change: the bug that prevented adding a new device is fixed by changing findDeviceIDInList() method.
 * The program threw an exception during the checking on the uniqueness of the device because the program checked a device ID against 
 * every of the six possible elements (even null ones) in the list. Now it is changed so it checks only against existing valid device IDs.
 * 
 *  - Third change: the bug in the findDeviceIDInList() method is fixed. 
 *  String values for device IDs were compared on equality using "==" instead of .equals() which led to a wrong comparison on uniqueness of the devices. 
 * 
 *  - Fourth change: the bug that allowed to remove unexisting devices is fixed. 
 * There is a check on a device existing in the device list before removing the device. It didn't return false when the device wasn't 
 * found in the list
 * 
 *  - Fifth change: The bug that allowed to add more than 6(MAX_DEVICES) devices is fixed. 
 * There was no checking for the maximum devices that can be added to the device list.
 * 
 * Also, messages for exceptions were added to distinguish different exceptions
 * 
 *		
 * 
 */

public class Controller {
	
	// --- Constants ---
	static final int MAX_DEVICES = 6;
	
	// --- Fields ---
	// List of devices to be controlled
	private ControlledDevice [] controlledDevices; 
	private int nextIndex;  // equivalent to the number of devices in 'controlledDevics' array

	// --- Constructor ---
	public Controller() {
		controlledDevices = new ControlledDevice[MAX_DEVICES];
	}
	

	// --- Public methods ---
	
	/**
	 * Add a device to the list of devices to be controlled by this controller. 
	 * 
	 * A controller can control up to six physical devices. The method succeeds if the device is successfully 
	 * added to the list of controlled devices. The method fails if the list is full or if the device is not 
	 * correctly specified. 
	 * 
	 * @param deviceType	The type of device to be controlled (enumerated in 'ControlledDeviceTypes') 
	 * @param deviceID		Unique identifier for the physical device. This is a 4-digit string.
	 * @param operatingMin	Minimum value accepted by the device i.e. operating range floor. Non-negative integer value expected.
	 * @param operatingMax	Maximum value accepted by the device i.e. operating range ceiling. Non-negative integer value expected.
	 * @param safeMin		Minimum 'safe' operating value for the device. Expected >= 'operatingMin'.
	 * @param safeMax		Maximum 'safe' operating value accepted by the device. Expected <= operatingMax'.
	 * 
	 * @return 				'true' if the device is successfully added to the list
	 *						'false' if already 6 devices in the array 
	 *			
	 * @throws 				IllegalArgumentException if 'deviceID' is invalid or is already in the list
	 * @throws				NullPointerException if 'deviceID' is null
	 * @throws				IllegalArgumentException if 'operatingMin' or 'operatingMax' is negative
	 * @throws				IllegalArgumentException if 'operatingMin' is greater than 'operatingMax'
	 * @throws				IllegalArgumentException if 'safeMin' is greater than 'safeMax'
	 * @throws				IllegalArgumentException if 'safeMin' or 'safeMax' are outside 'operatingMin' and 'operatingMax'
	 */ 
	public boolean addDevice(ControlDeviceType deviceType, String deviceID, int operatingMin, int operatingMax, int safeMin, int safeMax) 
			throws IllegalArgumentException, NullPointerException {
	
		final String DIGITS_MASK = "[0-9]+"; 

		boolean inputsOk = true;  // assume device will be successfully added
		

		// added messages to exceptions
		
		if (deviceID == null) {
			throw new NullPointerException("Device ID is null");
		}
		
		// 1st change
		// ! is added before deviceID.matches
		if (deviceID.equals("") || deviceID.length()!=4  ||  !deviceID.matches(DIGITS_MASK)) {
			throw new IllegalArgumentException("Device ID is invalid");
		}
		
		if (findDeviceIDInList(deviceID)) {
			throw new IllegalArgumentException("Device ID is already in the list");
		}
		
		// 5th change
		// Limit the number of devices that can be added to MAX_DEVICES
		if (nextIndex >= MAX_DEVICES){
			return false;
		}
		
		if ((operatingMin<0) || (operatingMin>operatingMax) || (safeMin<operatingMin) || (safeMax>operatingMax) || (safeMin>safeMax)) {
			throw new IllegalArgumentException("One of the numeric parameters is invalid");
		}
		
		// Provided data is valid, create a device object and add to the 'controlledDevices' list.
		if (inputsOk) {		
		
			switch (deviceType) {
				case CONTROLDEVICE_FAN_DRIVER: controlledDevices[nextIndex] = new FanDriver(deviceID, operatingMin, operatingMax, safeMin, safeMax); break;
				case CONTROLDEVICE_PRESSURE_PUMP: controlledDevices[nextIndex] = new PressurePump(deviceID, operatingMin, operatingMax, safeMin, safeMax); break;
				case CONTROLDEVICE_VOLTAGE_REGULATOR: controlledDevices[nextIndex] = new VoltageRegulator(deviceID, operatingMin, operatingMax, safeMin, safeMax); break;
				case CONTROLDEVICE_WATER_VALVE: controlledDevices[nextIndex] = new WaterValve(deviceID, operatingMin, operatingMax, safeMin, safeMax); break;
			}
			nextIndex++;
		}
		
		return (inputsOk);
	}


	/**
	 * Remove a device from the list of devices to be controlled by this controller. 
	 * 
	 * The method succeeds if the device is successfully removed from the list of controlled devices. The method fails if the list is empty, 
	 * the device is not in the list or "deviceID" is null. 
	 * 
	 * @param deviceID		Unique identifier for the physical device. This is a 4-digit string.
	 * @return 				'true' if the device is successfully removed from the list
	 *						'false' if  the device is not in the list OR the list is empty
	 * @throws 				NullPointerException if "deviceID" is null
	 */ 
	public boolean removeDevice(String deviceID) 
			throws NullPointerException {
	
		boolean inputsOk = true;  // assume device will be successfully added

		if (deviceID == null) {
			throw new NullPointerException("Device ID is null");
		}
		
		// 4th change
		// Returns immediately if there is no device in the list
		if (!findDeviceIDInList(deviceID)) { 
			return false; 		
		}
	
		inputsOk = !(nextIndex == 0);		

		
		// Remove 'deviceID', moving later objects down and decrement the 'next' pointer.
		if (inputsOk) {	
			for (int i=getDeviceIndex(deviceID); i<controlledDevices.length-1; ++i) 
				controlledDevices[i] = controlledDevices[i+1];
			nextIndex--;
		}
		
		return (inputsOk);
	}
	
	/**
	 * Activate control of a device by sending it a new input value.
	 * 
	 * The method succeeds if the device is successfully controlled. The method fails if the device is not 
	 * in the list, "deviceID" is null, the input value is not within the operating range of the device or
	 * the control call fails. 
	 * 
	 * @param deviceID		Unique identifier for the physical device. This is a 4-digit string.
	 * @param inputVal		The value to be passed to the device.
	 * @return 				'true' if the device successfully activates control
	 *						'false' if  'deviceId' is not in the list OR 
	 *									'deviceID' is null OR
	 *									'inputVal' is not inside the operating range for 'deviceID' OR
	 *									 control call fails
	 */ 

	public boolean doControl(String deviceID, int inputVal) {
		
		return true;
	}

	
	
	//--- Helper methods ---
	public boolean isDeviceInList(String deviceID) {
		return findDeviceIDInList(deviceID);
	}
	
	public int getNumDevices() {
		return (nextIndex);
	}	

	
	
	//--- Private methods ---
	private boolean findDeviceIDInList(String id) {
		
		int i=0;
		boolean found = false;
		
		// 2nd change
		// controlledDevices.length replaced with nextIndex
		while  (!found && (i<nextIndex)) {
			// 3d change
			// == replaced with equals()
			if (controlledDevices[i].getID().equals(id)) {
				found = true;
			}
			i++;
		}
		return found;
	}


	private int getDeviceIndex(String id) {
	// Assumes the device with ID is in the list. Invalid return value if it is not.	
		int i = 0;
		for (i=0; (i<nextIndex) && (!controlledDevices[i].getID().equals(id)); ++i) {}
		return i;
		
	}
			
}

