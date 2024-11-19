package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.time.LocalDateTime;

/*
 * This file contains an iterative (Non-Linear) "OpMode".
 * It has been wrote to work with omni wheels, do not use with other wheels..
 *
 * If you are a new programmer but understand java, take a look at the
 * example OpModes in the FtcRobotController > java > org.firstinspires.ftc.robotcontroller
 */
@SuppressWarnings("unused, SpellCheckingInspection")
@TeleOp(name = "Prototype Robot DO-NOT-USE", group = "Iterative OpMode")
@Disabled
public class PrototypeRobot extends OpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    private DcMotor slides = null;
    private DcMotor turnThingy = null;


    @Override
    public void init() {
        slides = hardwareMap.get(DcMotor.class, "slides");
        turnThingy = hardwareMap.get(DcMotor.class, "turnThingy");

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Mode", "Solo");
    }

    // Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    @Override
    public void init_loop() {
    }

    // Code to run ONCE when the driver hits PLAY
    @Override
    public void start() {
        // Reset the runtime counter when the driver hits PLAY
        runtime.reset();
    }

    @Override
    public void loop() {
        // turnThingy
        slides.setPower(-gamepad1.left_trigger + gamepad1.right_trigger);
        turnThingy.setPower(gamepad1.left_stick_y);

        // Show the elapsed game time and wheel power.

        telemetry.addData("Status", "Run Time: " + runtime);
        telemetry.update();
    }

}