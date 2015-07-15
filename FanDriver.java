package main;

public class FanDriver extends ControlledDevice {

	public FanDriver(String deviceID, int operatingMin, int operatingMax, int safeMin, int safeMax) {
		super(deviceID, operatingMin, operatingMax, safeMin, safeMax);
	}

	
	//--- Implemented abstract methods ---
	
	protected boolean processInput(int newVal) {
		return true;
	}

	
}

