package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Rohit on 1/31/2017.
 */

public class ParticleSystem {

    DcMotor motorLauncher = null;
    DcMotor carwash = null;
    CRServo launcherServo = null;

    long shootTimer = 0;

    double launcherSpeed = 0.33;
    double carwashSpeed = 1;
    double servoLauncherSpeed = 1;

    public  ParticleSystem(DcMotor motorLauncher , DcMotor carwash , CRServo launcherServo){
        this.motorLauncher = motorLauncher;
        this.carwash = carwash;
        this.launcherServo = launcherServo;
    }

    public void spinUp(){
        if(motorLauncher.getPower() == 0)
            motorLauncher.setPower(launcherSpeed);
        else
            motorLauncher.setPower(0);
    }

    public void trigger(){
        if(launcherServo.getPower() == 0)
            launcherServo.setPower(servoLauncherSpeed);
        else
            launcherServo.setPower(0);
    }

    public void triggerReverse(){
        if(launcherServo.getPower() == 0)
            launcherServo.setPower(-servoLauncherSpeed);
        else
            launcherServo.setPower(0);
    }

    public void stopTrigger(){
        launcherServo.setPower(0);
    }

    public void collect(){
        if(carwash.getPower() == 0)
            carwash.setPower(carwashSpeed);
        else
            carwash.setPower(0);
    }

    public void eject(){
        if(carwash.getPower() == 0)
            carwash.setPower(-carwashSpeed);
        else
            carwash.setPower(0);
    }

    public void stopCollect(){
        carwash.setPower(0);
    }

    public void shoot(){
        if(motorLauncher.getPower() == 0){
            spinUp();
            shootTimer = System.nanoTime() + (long) 1e9;
        }
        if(System.nanoTime() > shootTimer) {
            collect();
            trigger();
        }
    }

    public void stop(){
        launcherServo.setPower(0);
        carwash.setPower(0);
        motorLauncher.setPower(0);
    }
}
