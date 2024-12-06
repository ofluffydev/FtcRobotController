package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

//@SuppressWarnings("unused")
@TeleOp(name = "Duo Driver Controlled", group = "Iterative OpMode")
public class DriverControlledDuo extends OpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    public double leftStickX = 0.00, leftStickY = 0.00, rightStickX = 0.00;
    public boolean toggle = false, prevA = false, prevB = false;
    RobotHardware robot = new RobotHardware(this);


    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        telemetry.addData("Mode", "Duo");
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
        // Sticks use potentiometers, so they can have a value from -1 to 1 in both x and y direction
        leftStickX = -gamepad1.left_stick_x;
        leftStickY = gamepad1.left_stick_y;
        rightStickX = -gamepad1.right_stick_x;

        DrivePowers powers = calculatePowers(leftStickY, leftStickX, rightStickX);

        // Gripper
        robot.toggleGrabber(gamepad2.b, prevB);
        prevB = gamepad2.b;

        // Toggle wrist
        toggle ^= (gamepad2.a && !prevA); // Debouncing using XOR
        prevA = gamepad2.a;
        robot.setToggleServoPosition(toggle ? 0.5 : 1);

        robot.setDrivePower(powers.leftFrontPower, powers.rightFrontPower, powers.leftBackPower, powers.rightBackPower);
        robot.setSlidesPower(gamepad2.right_trigger - gamepad2.left_trigger);
        robot.setArmMotorPower(gamepad2.left_stick_y);

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime);
        telemetry.addData("Toggle position", robot.getWrist().getPosition());
        telemetry.update();
    }

    public DrivePowers calculatePowers(double leftStickY, double leftStickX, double rightStickX) {
        // Calculate power values
        double leftFrontPower = (leftStickY + leftStickX + rightStickX);
        double rightFrontPower = (leftStickY - leftStickX - rightStickX);
        double leftBackPower = (leftStickY - leftStickX + rightStickX);
        double rightBackPower = (leftStickY + leftStickX - rightStickX);
        // Return a DrivePowers record
        return new DrivePowers(leftFrontPower, rightFrontPower, leftBackPower, rightBackPower);
    }

}