package main;

public abstract class ControlledDevice {

	// --- Fields ---
	
	protected ControlDeviceType controlType;	// From enum set e.g. PRESSURE_PUMP
	protected String id; 						// Unique ID for physical device
	protected int operatingMin, operatingMax;	// The range of operation for the device. This will vary by device. 
												//   It must be set when the device object is created i.e. there is no setter method for these values.
	protected int safeMinVal, safeMaxVal;		// 'Safe' operating range for the device. This must lie within the operating range.

	
	// --- Constructors ---

	public ControlledDevice(String deviceID, int operatingMin, int operatingMax, int safeMin, int safeMax) {
		this.id = deviceID;
		this.operatingMin = operatingMin;
		this.operatingMax = operatingMax;
		this.safeMinVal = safeMin;
		this.safeMaxVal = safeMax;
	}	
	
	
	// --- Methods ---

	//     Getters/setters 
	protected String getID() {
		return this.id;
	}
	protected int getOperatingMin() {
		return this.operatingMin;
	}
	protected int getOperatingMax() {
		return this.operatingMax;
	}
	
	protected int getSafeMin() {
		return this.safeMinVal;
	}
	protected int getSafegMax() {
		return this.safeMaxVal;
	}


	protected boolean isWithinOperatingRange(int inputVal) {
		return ((inputVal>=operatingMin) && (inputVal<=operatingMax));
	}
	
	protected boolean isWithinSafeRange(int inputVal) {
		return ((inputVal>=safeMinVal) && (inputVal<=safeMaxVal));
	}
	
	
	// --- Abstract methods to be implemented by subclasses ---
	
	//     Process the input value(s) and return whether the appropriate control action was successfully implemented
	abstract boolean processInput(int newVal);
}

