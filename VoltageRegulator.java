package main;

public class VoltageRegulator extends ControlledDevice {

	public VoltageRegulator(String deviceID, int operatingMin, int operatingMax, int safeMin, int safeMax) {
		super(deviceID, operatingMin, operatingMax, safeMin, safeMax);
	}

	
	//--- Implemented abstract methods ---
	
	protected boolean processInput(int newVal) {
		return true;
	}
	

}

