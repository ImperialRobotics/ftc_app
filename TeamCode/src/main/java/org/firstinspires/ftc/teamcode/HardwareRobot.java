package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by rohitshankar on 9/11/16.
 */
public class HardwareRobot
{
    //drive train
    public DcMotor motorRight = null;
    public DcMotor motorLeft = null;
    public DcMotor motorCarwash = null;
    public DcMotor motorLauncher = null;
    public CRServo launcherServo = null;
    private ElapsedTime period  = new ElapsedTime();
    HardwareMap hwMap = null;

    public HardwareRobot()
    {

    }

    public void init(HardwareMap ahwMap)
    {
        //initialize hardware

        hwMap = ahwMap;

        motorRight = hwMap.dcMotor.get("motorRight");
        motorLeft = hwMap.dcMotor.get("motorLeft");
        motorCarwash = hwMap.dcMotor.get("motorCarwash");
        motorLauncher = hwMap.dcMotor.get("motorC");

        launcherServo = hwMap.crservo.get("servoL");

        motorRight.setPower(0);
        motorLeft.setPower(0);
        motorCarwash.setPower(0);
        motorLauncher.setPower(0);
        launcherServo.setPower(0);

        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void waitForTick(long periodMs)  throws InterruptedException
    {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }


}
