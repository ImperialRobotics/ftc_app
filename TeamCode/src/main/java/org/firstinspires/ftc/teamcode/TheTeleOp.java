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
    ParticleSystem ps = null;
    private boolean[] buttonSavedStates = new boolean[11];
    private boolean shouldShoot = false;

    //when init button is pressed
    public void init()
    {
        hr.init(hardwareMap);
        ps = new ParticleSystem(hr.motorLauncher , hr.motorCarwash ,hr.launcherServo);
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

        double left  = gamepad1.left_stick_y + gamepad1.right_stick_x;
        double right = gamepad1.left_stick_y - gamepad1.right_stick_x;

        // Normalize the values so neither exceed +/- 1.0
        double max = Math.max(Math.abs(left), Math.abs(right));
        if (max > 1.0)
        {
            left /= max;
            right /= max;
        }

        hr.motorLeft.setPower(left);
        hr.motorRight.setPower(right);


        if (toggleAllowed(gamepad1.a, 0)) {
            ps.collect();
        }

        else if(toggleAllowed(gamepad1.b, 1)){
            ps.shoot();
        }

        if(toggleAllowed(gamepad1.x, 2)){
            ps.spinUp();
        }

        if(toggleAllowed(gamepad1.y, 3)){
            ps.trigger();
        }

        if (toggleAllowed(gamepad1.right_bumper, 9)){
            shouldShoot = !shouldShoot;
        }

        if(shouldShoot){
            ps.cycle();
        }
    }
    //end
    public void stop()
    {


    }
    boolean toggleAllowed(boolean button, int buttonIndex)
    {

        /*button indexes:
        0  = a
        1  = b
        2  = x
        3  = y
        4  = dpad_down
        5  = dpad_up
        6  = dpad_left
        7  = dpad_right
        8  = left bumper
        9  = right bumper
        10 = start button
        */

        if (button) {
            if (!buttonSavedStates[buttonIndex])  { //we just pushed the button, and when we last looked at it, it was not pressed
                buttonSavedStates[buttonIndex] = true;
                return true;
            }
            //       else if(buttonCurrentState[buttonIndex] == buttonSavedStates[buttonIndex] && buttonCurrentState[buttonIndex]){
            else { //the button is pressed, but it was last time too - so ignore

                return false;
            }
        }

        buttonSavedStates[buttonIndex] = false; //not pressed, so remember that it is not
        return false; //not pressed

    }


}
