package main;


public class WaterValve extends ControlledDevice {

	public WaterValve(String deviceID, int operatingMin, int operatingMax, int safeMin, int safeMax) {
		super(deviceID, operatingMin, operatingMax, safeMin, safeMax);
	}

	
	//--- Implemented abstract methods ---
	
	protected boolean processInput(int newVal) {
		return true;
	}
	

}

