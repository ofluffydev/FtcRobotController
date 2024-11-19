package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@SuppressWarnings("unused")
@TeleOp(name = "Grabber Test", group = "Testing")
public class GrabberTest extends OpMode {
    public boolean toggle = false, prevA = false, prevB = false, initHang = false;
    RobotHardware robot = new RobotHardware(this);


    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        telemetry.addData("Mode", "Grabber Test");
    }

    // Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    @Override
    public void init_loop() {
    }

    // Code to run ONCE when the driver hits PLAY
    @Override
    public void start() {
    }

    @Override
    public void loop() {
        // Gripper
        if (gamepad2.b && !prevB) {
            robot.toggleGrabber();
        }
        prevB = gamepad2.b;

        // Toggle wrist
        toggle = (gamepad2.a && !prevA) != toggle;
        prevA = gamepad2.a;
        robot.setToggleServoPosition(!toggle ? 1 : 0.5);

        // Show the elapsed game time and wheel power.
        telemetry.addData("Wrist position", robot.getWrist().getPosition());
        telemetry.addData("Grabber status", robot.getGrabber().getPosition() == 0 ? "Open" : "Grabbing");
        telemetry.update();
    }

}