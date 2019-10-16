package frc.robot.settings;

public class Constants() {

    public static final int kTimeoutMs = 10;
    public static final int kLongCANTimeoutMs = 100;
    public static final double kDriveVoltageRampRate = .5;
	public static final int kVelocitySlotId = 0;

    public static final class kDrive {
        public static final int kRightDriveMasterId = 10;
        public static final int kRightDriveSlaveId = 11;
        public static final int kLeftDriveMasterId = 1;
        public static final int kLeftDriveSlaveId = 0;
    }
}