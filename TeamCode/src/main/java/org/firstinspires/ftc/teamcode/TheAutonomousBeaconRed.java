package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
import for_camera_opmodes.LinearOpModeCamera;
/**
 * Created by abhimaster2001 on 1/30/17.
 */
@Autonomous(name = "TheAutonomousBeaconRed" , group = "Autonomous")
public class TheAutonomousBeaconRed extends LinearOpMode{

    HardwareRobot hr = new HardwareRobot();
    private ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 1120 ;//encoder counts for andymark motor
    static final double DRIVE_GEAR_REDUCTION = 0.667 ;//80/120 = 0.66
    static final double WHEEL_DIAMETER_INCHES = 4.0 ;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)/(WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.13;
    static final double TURN_SPEED = 0.1;
    static ColorSensor sensor;

    public void runOpMode() throws InterruptedException
    {

        hr.init(hardwareMap);
        waitForStart();

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        hr.motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hr.motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        //activate encoders
        hr.motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hr.motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d", hr.motorLeft.getCurrentPosition(), hr.motorRight.getCurrentPosition());
        telemetry.update();

        //activate color sensor
        sensor = hardwareMap.colorSensor.get("color");

        waitForStart();
        //write instructions to drive robot
        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)

        hr.motorLauncher.setPower(-0.33);
        encoderDrive(DRIVE_SPEED,  -25.9,  -25.9   , 5.0);  // S1: Forward 20 Inches with 5 Sec timeout
        hr.launcherServo.setPower(1);
        hr.motorCarwash.setPower(-1);
        sleep(4000);
        hr.motorLauncher.setPower(0);
        encoderDrive(DRIVE_SPEED, -15 , -15 , 4.0);
        if (sensor.red()>50){ //assuming that the color sensor is placed on the left side of the bot the number can be changed
            hr.motorLeft.setPower(0.5);
            hr.motorRight.setPower(-0.5);//not completely sure which direction is which
        }else if (sensor.blue()>50){
            hr.motorLeft.setPower(-0.5);
            hr.motorRight.setPower(0.5);//not completely sure which direction is which
        }

        telemetry.addData("Path", "Complete");
        telemetry.update();

    }

    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) throws InterruptedException {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = hr.motorLeft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = hr.motorRight.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            hr.motorLeft.setTargetPosition(newLeftTarget);
            hr.motorRight.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            hr.motorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            hr.motorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            hr.motorLeft.setPower(Math.abs(speed));
            hr.motorRight.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (hr.motorLeft.isBusy() && hr.motorRight.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        hr.motorLeft.getCurrentPosition(),
                        hr.motorRight.getCurrentPosition());
                telemetry.update();

                // Allow time for other processes to run.
                idle();
            }

            // Stop all motion;
            hr.motorLeft.setPower(0);
            hr.motorRight.setPower(0);

            // Turn off RUN_TO_POSITION
            hr.motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            hr.motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move
        }
    }



}
