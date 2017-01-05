package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by rohitshankar on 9/11/16.
 * Basic TeleOp :)
 *
 */
@TeleOp(name = "TheTeleOp", group = "TeleOp")
//@Disabled

public class TheTeleOp extends OpMode
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
    public void loop() {

        double left  = -gamepad1.left_stick_y + gamepad1.right_stick_x;
        double right = -gamepad1.left_stick_y - gamepad1.right_stick_x;

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
