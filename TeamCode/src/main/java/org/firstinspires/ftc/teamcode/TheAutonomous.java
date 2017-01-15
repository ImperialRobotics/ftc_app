package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Abhi Bhattaru on 1/15/17.
 * Basic TeleOp :)
 *
 */
@Autonomous(name = "TheAutonomous", group = "Autonomous")
//@Disabled

public class TheAutonomous extends OpMode
{

    HardwareRobot hr = new HardwareRobot();

    //when init button is pressed
    public void init()
    {
        hr.init(hardwareMap);

        telemetry.addData("Say", "Initialize Complete");
        updateTelemetry(telemetry);

    }

    //played between init button and start
    public void init_loop()
    {

    }

    //played when start button is pressed
    public void start()
    {

    }

    //after start and before stop
    //do the loop later
    public void loop() {

        double left  = 0;
        double right = 0;

        // Normalize the values so neither exceed +/- 1.0
        double max = Math.max(Math.abs(left), Math.abs(right));
        if (max > 1.0)
        {
            left /= max;
            right /= max;
        }

        hr.motorLeft.setPower(left);
        hr.motorRight.setPower(right);


        if (gamepad1.right_bumper) {
            hr.motorCarwash.setPower(1);
        }
        else if (gamepad1.left_bumper) {
            hr.motorCarwash.setPower(-1);
        }
        else{
            hr.motorCarwash.setPower(0);
        }
        if (gamepad1.a) {
            hr.motorLauncher.setPower(-1);
        }
        else if(gamepad1.b){
            hr.motorLauncher.setPower(1);
        }
        else {
            hr.motorLauncher.setPower(0);
        }
        if (gamepad1.y){
            hr.launcherServo.setPower(1);
        }
        else if (gamepad1.x){
            hr.launcherServo.setPower(-1);
        }
        else {
            hr.launcherServo.setPower(0);
        }
    }
    //end
    public void stop()
    {


    }

}
