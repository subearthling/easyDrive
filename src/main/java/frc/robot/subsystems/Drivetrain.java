/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;

import frc.robot.settings.Variables;
import frc.robot.settings.Constants;
import frc.robot.commands.Drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class DriveTrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  private static Drivetrain instance = null;

  public static Drivetrain getInstance() {
      if (instance == null) {
        instance = new Drivetrain;
      }

      return instance;
  }

  private TalonSRX mLeftMaster, mRightMaster;
  private VictorSPX mLeftSlave, mRightSlave;

  private Drivetrain() {

    mLeftMaster = TalonSRXFactory.createDefaultTalon(kDrive.kLeftDriveMasterId);
    configureMaster(mLeftMaster, false)

    mLeftSlave = new VictorSPX(kDrive.kLeftDriveSlaveId);
    mLeftSlave.configFactoryDefault();
    mLeftSlave.follow(mLeftMaster);
    mLeftSlave.setInverted(InvertType.FollowMaster);

    mRightMaster = TalonSRXFactory.createDefaultTalon(kDrive.kRightDriveMasterId);
    configureMaster(mRightMaster, false)

    mRightSlave = new VictorSPX(kDrive.kRightDriveSlaveId);
    mRightSlave.configFactoryDefault();
    mRightSlave.follow(mRightMaster);
    mRightSlave.setInverted(InvertType.FollowMaster);

    talon.setInverted(left);

    private void configureMaster(TalonSRX talon, boolean left) {
      talon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 5, Constants.kLongCANTimeoutMs);
      talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kTimeoutMs);
      // primary closed-loop, 100 ms timeout
      final ErrorCode sensorPresent = talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kLongCANTimeoutMs);
      if (sensorPresent != ErrorCode.OK) {
        DriverStation.reportError("Could not detect " + (left ? "left" : "right") + " encoder: " + sensorPresent, false);
      }
    }
  }

  public void drivePercentOutput(double speed) {
		drivePercentOutput(speed, speed);
	}

  public void drivePercentOutput(double leftSpeed, double rightSpeed) {
		driveLeftPercentOutput(leftSpeed);
		driveRightPercentOutput(rightSpeed);
	}

  public void driveLeftPercentOutput(double speed) {
		mLeftMaster.set(ControlMode.PercentOutput, speed * Variables.driveAdjustmentCoefficient);
	}

  public void driveRightPercentOutput(double speed) {
		mRightMaster.set(ControlMode.PercentOutput, speed * Variables.driveAdjustmentCoefficient);
	}

  public void stop() {
		drivePercentOutput(0, 0);
	}

  public TalonSRX getLeftTalon() {
		return mLeftMaster;
	}

  public TalonSRX getRightTalon() {
		return mRightMaster;
	}
}
