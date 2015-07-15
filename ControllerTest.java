package test;

import static org.junit.Assert.*;
import main.Controller;
import main.ControlDeviceType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ControllerTest {

	Controller myApp;
	
	@Before
	public void setUp() throws Exception {
		myApp = new Controller();
	}

	@After
	public void tearDown() throws Exception {
	}

	//	Helping methods and variables
	static final int MAX_DEVICES = 6;
	
	/**
	 * Verifying that added devices are truly in the list of controlled devices 
	 * 
	 * @param deviceId		Unique identifier for the physical device. This is a 4-digit string.
	 */ 
	private void verifyDeviceInTheList(String deviceId) {
		assertTrue(String.format("Device with deviceID $s should be in the list", deviceId), myApp.isDeviceInList(deviceId));
	}
	
	
	/**
	 * Adding and verifying valid devices to the list of controlled devices. Adds a device with default numeric and device type parameters to the list.
	 * 
	 * @param numOfDevices		The number of devices to add to the list of controlled devices. 
	 */ 
	private void verifyAddValidDevices(int numOfDevices) {
		for (int i=1; i<numOfDevices + 1; i++) {
			assertTrue("Add device should succeed", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "000" + i, 0, 80, 10, 60));
		}
	}
	

	/**
	 * Verifying removing devices from the list of controlled devices
	 * 
	 * @param deviceId		Unique identifier for the physical device. This is a 4-digit string.
	 */ 
	private void verifyDeviceRemoval(String deviceId) {
		assertTrue(String.format("Remove a valid device with deviceID $s from the list should succeed", deviceId), myApp.removeDevice(deviceId));
		
	}
	
	//--- This section tests adding different devices to the list of controlled devices ---	

	@Test
	public void testAddValidDeviceToEmptyListSucceeds() {
		assertTrue("Add device should succeed", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "0001", 0, 80, 10, 60));
		verifyDeviceInTheList("0001");
		assertEquals("There should be 1 device in the list of controlled devices", 1, myApp.getNumDevices());
	}

	
	@Test
	public void testAddSixValidDevicesSucceeds() {
		verifyAddValidDevices(MAX_DEVICES);
		
		// Having 6 methods instead of one loop graphically shows exact IDs that are in the list
		verifyDeviceInTheList("0001");
		verifyDeviceInTheList("0002");
		verifyDeviceInTheList("0003");
		verifyDeviceInTheList("0004");
		verifyDeviceInTheList("0005");
		verifyDeviceInTheList("0006");
		assertEquals("There should be " + MAX_DEVICES + " devices in the list of controlled devices", MAX_DEVICES, myApp.getNumDevices());
	}
	 
	
	@Test
	public void testAddSevenDevicesFails() {
		verifyAddValidDevices(MAX_DEVICES);
		assertFalse("Add 7th device with ID 0007 to the list should fail", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "0007", 10, 100, 20, 80));
	}


	@Test 
	public void testAddExistingDeviceException() {
		verifyAddValidDevices(MAX_DEVICES);
		try {
			myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "0001", 0, 80, 10, 60);
				fail("The device already exists in the list");
		} catch (IllegalArgumentException e) {
			// Message expected for all exceptions are taken from the Controller.java 
			assertEquals("Unexpected exception was caught", "Device ID is already in the list", e.getMessage());
		}
	}

	
	@Test
	public void testAddNullDeviceException() {
		try {
			myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, null, 0, 80, 10, 60);
				fail("Invalid device with a null ID");
		} catch (NullPointerException e) {
			// Message expected for all exceptions are taken from the Controller.java 
			assertEquals("Unexpected exception was caught", "Device ID is null", e.getMessage());
		}
	}
	
	
	@Test
	public void testAddDeviceWithInvalidStringIDException() {
		try {
			myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "3abc", 0, 80, 10, 60);
				fail("The device with an invalid string ID");
		} catch (IllegalArgumentException e) {
			// Message expected for all exceptions are taken from the Controller.java 
			assertEquals("Unexpected exception was caught", "Device ID is invalid", e.getMessage());
		}
	}
	
	
	@Test
	public void testAddDeviceWithEmptyStringIDException() {
		try {
			myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "", 0, 80, 10, 60);
				fail("The device with an empty string ID");
		} catch (IllegalArgumentException e) {
			// Message expected for all exceptions are taken from the Controller.java 
			assertEquals("Unexpected exception was caught", "Device ID is invalid", e.getMessage());
		}
	}
	
	@Test
	public void testAddDeviceWithSpaceStringIDException() {
		try {
			myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, " ", 0, 80, 10, 60);
				fail("The device with a space in string ID");
		} catch (IllegalArgumentException e) {
			// Message expected for all exceptions are taken from the Controller.java 
			assertEquals("Unexpected exception was caught", "Device ID is invalid", e.getMessage());
		}
	}
	
	@Test
	public void testAddInvalidDeviceWithShorterIDException() {
		try {
			myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "011", 0, 80, 10, 60);
				fail("The device with invalid ID shorter than 4-digit string");
		} catch (IllegalArgumentException e) {
			// Message expected for all exceptions are taken from the Controller.java 
			assertEquals("Unexpected exception was caught", "Device ID is invalid", e.getMessage());
		}
	}
	
	
	@Test
	public void testAddInvalidDeviceWithLongerIDException() {
		try {
			myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "01221", 0, 80, 10, 60);
				fail("The device with invalid ID longer than 4-digit string");
		} catch (IllegalArgumentException e) {
			// Message expected for all exceptions are taken from the Controller.java 
			assertEquals("Unexpected exception was caught", "Device ID is invalid", e.getMessage());
		}
	}
	
	
	@Test
	public void testAddDeviceWithNegativeOperatingMinException() {
		try {
			myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "0007", -1, 80, 10, 60);
				fail("The device with an invalid negative operatingMin");
		} catch (IllegalArgumentException e) {
			// Message expected for all exceptions are taken from the Controller.java 
			assertEquals("Unexpected exception was caught", "One of the numeric parameters is invalid", e.getMessage());
		}
	}
	
	
	@Test
	public void testAddDeviceWithNegativeOperatingMaxException() {
		try {
			myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "0007", 0, -1, 0, 0);
				fail("The device with an invalid negative operatingMin");
		} catch (IllegalArgumentException e) {
			// Message expected for all exceptions are taken from the Controller.java 
			assertEquals("Unexpected exception was caught", "One of the numeric parameters is invalid", e.getMessage());
		}
	}
	
	
	@Test
	public void testAddDeviceOperatingMinGreaterOperatingMaxException() {
		try {
			myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "0007", 41, 40, 10, 30);
				fail("The invalid device with operatingMin greater than operatingMax");
		} catch (IllegalArgumentException e) {
			// Message expected for all exceptions are taken from the Controller.java 
			assertEquals("Unexpected exception was caught", "One of the numeric parameters is invalid", e.getMessage());
		}
	}
	
	
	@Test
	public void testAddDeviceSafeMinGreaterSafeMaxException() {
		try {
			myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "0007", 1, 100, 41, 40);
				fail("The invalid device with safeMin greater than safeMax");
		} catch (IllegalArgumentException e) {
			// Message expected for all exceptions are taken from the Controller.java 
			assertEquals("Unexpected exception was caught", "One of the numeric parameters is invalid", e.getMessage());
		}
	}
	
	
	@Test
	public void testAddDeviceSafeMinLessOperatingMinException() {
		try {
			myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "0007", 10, 100, 9, 40);
				fail("The invalid device with safeMin less than operatingMin");
		} catch (IllegalArgumentException e) {
			// Message expected for all exceptions are taken from the Controller.java 
			assertEquals("Unexpected exception was caught", "One of the numeric parameters is invalid", e.getMessage());
		}
	}
	
	
	@Test
	public void testAddDeviceSafeMaxGreaterOperatingMaxException() {
		try {
			myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "0007", 20, 50, 10, 51);
				fail("The invalid device with safeMax greater than operatingMax");
		} catch (IllegalArgumentException e) {
			// Message expected for all exceptions are taken from the Controller.java 
			assertEquals("Unexpected exception was caught", "One of the numeric parameters is invalid", e.getMessage());
		}
	}
	
	
	@Test
	public void testAddDeviceSafeMinSafeMaxOutsideOperatingMinOperatingMaxException() {
		try {
			myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "0007", 10, 100, 9, 101);
				fail("An invalid device with safeMin and safeMax are outside operatingMin and operatingMax");
		} catch (IllegalArgumentException e) {
			// Message expected for all exceptions are taken from the Controller.java 
			assertEquals("Unexpected exception was caught", "One of the numeric parameters is invalid", e.getMessage());
		}
	}
	
	
	@Test
	public void testAddValidDeviceWithEqualIntegerParametersSucceeds() {
		assertTrue("Add device should succeed", myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "0006", 4, 4, 4, 4));
		verifyDeviceInTheList("0006");
		assertEquals("There should be 1 device in the list of controlled devices", 1, myApp.getNumDevices());
	}
	

	//--- This section tests removing different devices from the list of controlled devices ---	
	
	@Test
	public void testRemoveValidDeviceFromTheListSucceeds() {
		verifyAddValidDevices(1);
		verifyDeviceInTheList("0001");
		
		verifyDeviceRemoval("0001");
		assertFalse("Device id=0001 shouldn't be in the list", myApp.isDeviceInList("0001"));
		assertEquals("The list of controlled devices should be empty", 0, myApp.getNumDevices());
	}
	
	
	@Test
	public void testRemoveInvalidDeviceFromEmptyListFails() {
		assertFalse("Remove an invalid device with deviceID 0001 from the empty list should fail", myApp.removeDevice("0001"));
	}
	
	
	@Test
	public void testRemoveUnexistingDeviceFromTheListFails() {
		myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "0001", 0, 80, 10, 60);
		assertFalse("Remove an invalid unexisting device with deviceID 0002 from the list should fail", myApp.removeDevice("0002"));
	}

	
	@Test
	public void testRemoveInvalidDeviceWithNullIDFails() {
		try {
			myApp.removeDevice(null);
			fail("Invalid device with a null ID");
		} catch (NullPointerException e) {
			assertEquals("Unexpected exception was caught", "Device ID is null", e.getMessage());
		}
	}
	
	
	@Test
	public void testRemoveSixValidDevicesSucceeds() {
		verifyAddValidDevices(MAX_DEVICES);
		
		// Having 6 methods instead of one loop graphically shows exact device IDs that are being removed
		verifyDeviceRemoval("0001");
		verifyDeviceRemoval("0002");
		verifyDeviceRemoval("0003");
		verifyDeviceRemoval("0004");
		verifyDeviceRemoval("0005");
		verifyDeviceRemoval("0006");
		assertEquals("The list of controlled devices should be empty", 0, myApp.getNumDevices());
	}
	
	
	@Test
	public void testRemoveSevenDevicesFails() {
		verifyAddValidDevices(MAX_DEVICES);
		
		// Having 6 methods instead of one loop graphically shows exact device IDs that are being removed
		verifyDeviceRemoval("0001");
		verifyDeviceRemoval("0002");
		verifyDeviceRemoval("0003");
		verifyDeviceRemoval("0004");
		verifyDeviceRemoval("0005");
		verifyDeviceRemoval("0006");
		assertFalse("Remove an invalid device with deviceID 0007 from the list should fail", myApp.removeDevice("0007"));	
		assertEquals("The list of controlled devices should be empty", 0, myApp.getNumDevices());
	}
	

	@Test
	public void testMovingDevicesInTheList() {
		verifyAddValidDevices(3);
		myApp.removeDevice("0001");
		myApp.removeDevice("0002");
		
		// adding 5 devices with IDs 0004-0008
		for (int i=4; i<9; i++) {
			myApp.addDevice(ControlDeviceType.CONTROLDEVICE_FAN_DRIVER, "000" + i, 0, 80, 10, 60);
		} 
		verifyDeviceRemoval("0008");
		assertEquals("The list of controlled devices should contain 5 devices", 5, myApp.getNumDevices());	
	}
}
	



