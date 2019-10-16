/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

import frc.robot.settings.Variables;
import frc.robot.settings.ControllerMap;
import frc.robot.commands.CommandBase;

/**
 * An example command.  You can replace me with your own command.
 */
public class Drive extends CommandBase {

  private double turnSpeed, currentLeftSpeed, currentRightSpeed, lastLeftSpeed, lastRightSpeed;

  public Drive() {
    // Use requires() here to declare subsystem dependencies
    requires(drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    turnSpeed = 0.5 * oi.getAxisDeadzonedSquared(ControllerMap.driveController, ControllerMap.driveTurnAxis, 0.05);

    lastLeftSpeed = currentLeftSpeed;
    lastRightSpeed = currentRightSpeed;

    currentLeftSpeed = -oi.getAxisDeadzonedSquared(ControllerMap.driveController, ControllerMap.driveTurnAxis, 0.1) + turnSpeed;
    currentRightSpeed = -oi.getAxisDeadzonedSquared(ControllerMap.driveController, ControllerMap.driveTurnAxis, 0.1) - turnSpeed;

    if (Variable.useLinearAcceleration) {
      double leftAcceleration = (currentLeftSpeed - lastLeftSpeed);
      double signOfLeftAcceleration = leftAcceleration / abs(leftAcceleration);
      if(abs(leftAcceleration) > Variables.accelerationSpeed) {
        if (abs(currentLeftSpeed) - abs(lastLeftSpeed) > 0) {
          currentLeftSpeed = lastLeftSpeed + (Variables.accelerationSpeed * signOfLeftAcceleration);
                    
        }
      }

      double rightAcceleration = (currentRightSpeed - lastRightSpeed);
      double signOfRightAcceleration = rightAcceleration / abs(rightAcceleration);
      if(abs(rightAcceleration) > Variables.accelerationSpeed) {
        if (abs(currentRightSpeed) - abs(lastRightSpeed) > 0) {
          currentRightSpeed = lastRightSpeed + (Variables.accelerationSpeed * signOfRightAcceleration);

        }
      }
    }

    drivetrain.drivePercentOutput(currentLeftSpeed + turnSpeed, currentRightSpeed - turnSpeed);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    drivetrain.stop();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
