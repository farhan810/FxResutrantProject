package com.tablecheck.windows;

public class BatteryStatus {

	public static void main(String[] args) {
		try {
				Kernel32.SYSTEM_POWER_STATUS batteryPowerStatus = new Kernel32.SYSTEM_POWER_STATUS();
				Kernel32.INSTANCE.GetSystemPowerStatus(batteryPowerStatus);
				System.out.println(batteryPowerStatus.getACLineStatusString());
				System.out.println(batteryPowerStatus.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
