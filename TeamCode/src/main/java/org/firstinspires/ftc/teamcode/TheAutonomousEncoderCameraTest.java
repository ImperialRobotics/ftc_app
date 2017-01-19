package org.firstinspires.ftc.teamcode;

/**
 */

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import for_camera_opmodes.LinearOpModeCamera;

/**
 * Created by rohitshankar on 10/14/16.
 */

@Autonomous(name = "TheAutonomousTEST" , group = "Autonomous")
public class TheAutonomousEncoderCameraTest extends LinearOpModeCamera
{
    HardwareRobot hr = new HardwareRobot();
    private ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 1120 ;//encoder counts for andymark motor
    static final double DRIVE_GEAR_REDUCTION = 0.667 ;//80/120 = 0.66
    static final double WHEEL_DIAMETER_INCHES = 4.0 ;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)/(WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.5;
    static final double TURN_SPEED = 0.3;
    int ds2 = 2;

    public void runOpMode() throws InterruptedException
    {

        String colorString = "NONE";
        hr.init(hardwareMap);


        if(isCameraAvailable()){

            setCameraDownsampling(4);
            // parameter determines how downsampled you want your images
            // 8, 4, 2, or 1.
            // higher number is more downsampled, so less resolution but faster
            // 1 is original resolution, which is detailed but slow
            // must be called before super.init sets up the camera
            telemetry.addLine("Wait for camera to finish initializing!");
            telemetry.update();
            startCamera();  // can take a while.
            // best started before waitForStart
            telemetry.addLine("Camera ready!");
            telemetry.update();

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

            //write instructions to drive robot
            // Step through each leg of the path,
            // Note: Reverse movement is obtained by setting a negative distance (not speed)
            encoderDrive(DRIVE_SPEED,  20,  20, 5.0);  // S1: Forward 20 Inches with 5 Sec timeout
            encoderDrive(TURN_SPEED,   12, -12, 4.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
            telemetry.addData("Path", "Complete");
            telemetry.update();

            try { // try is needed so catch the interrupt when the opmode is ended to stop the camera
                while (opModeIsActive()) {
                    if (imageReady()) { // only do this if an image has been returned from the camera
                        int redValue = 0;
                        int blueValue = 0;
                        int greenValue = 0;

                        // get image, rotated so (0,0) is in the bottom left of the preview window
                        Bitmap rgbImage;
                        rgbImage = convertYuvImageToRgb(yuvImage, width, height, ds2);

                        for (int x = 0; x < rgbImage.getWidth(); x++) {
                            for (int y = 0; y < rgbImage.getHeight(); y++) {
                                int pixel = rgbImage.getPixel(x, y);
                                redValue += red(pixel);
                                blueValue += blue(pixel);
                                greenValue += green(pixel);
                            }
                        }
                        int color = highestColor(redValue, greenValue, blueValue);

                        switch (color) {
                            case 0:
                                colorString = "RED";
                                break;
                            case 1:
                                colorString = "GREEN";
                                break;
                            case 2:
                                colorString = "BLUE";
                        }

                    } else {
                        colorString = "NONE";
                    }

                    telemetry.addData("Color:", "Color detected is: " + colorString);
                    telemetry.update();
                    sleep(10);
                }
            } catch (Exception e) {
                stopCamera();
            }

        }


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
