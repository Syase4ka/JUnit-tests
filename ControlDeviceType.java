package main;

public enum ControlDeviceType {
	
	CONTROLDEVICE_PRESSURE_PUMP ("PressurePump"), 
	CONTROLDEVICE_FAN_DRIVER ("FanDriver"), 
	CONTROLDEVICE_VOLTAGE_REGULATOR ("VoltageRegulator"), 
	CONTROLDEVICE_WATER_VALVE ("WaterValve");
	
	private final String name;
	
	ControlDeviceType (String name) {
		this.name = name;
	}

}

