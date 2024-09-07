package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This file contains an iterative (Non-Linear) "OpMode".
 * It has been wrote to work with omni wheels, do not use with other wheels..
 *
 * If you are a new programmer but understand java, take a look at the
 * example OpModes in the FtcRobotController > java > org.firstinspires.ftc.robotcontroller
 */
@SuppressWarnings("unused, SpellCheckingInspection")
@TeleOp(name = "Omni Iterative OpMode", group = "Iterative OpMode")
public class DriverControlled extends OpMode {

    private final ElapsedTime runtime = new ElapsedTime();
    public double prevLeftStickX = 0.00;
    public double prevLeftStickY = 0.00;
    public double prevRightStickX = 0.00;
    public double leftStickX = 0.00;
    public double leftStickY = 0.00;
    public double rightStickX = 0.00;
    public int count = 0;
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        /* TODO: Choose ports and stick with them. Probably plug them in based on the existing code.
         * This expects the motors to be plugged in in the following configuration:
         * - left_front_drive:  Port ?
         * - left_back_drive:   Port ?
         * - right_front_drive: Port ?
         * - right_back_drive:  Port ?
         */

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        // TODO: Come up with solution to make this more dynamic.
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");

        // Flip it if the wheel is going an unexpected direction.
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
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

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        // Setup a variable for each drive wheel to save power level for telemetry
        double leftFrontPower;
        double rightFrontPower;
        double leftBackPower;
        double rightBackPower;

        // POV Mode uses left stick to go forward, and right stick to turn.
        // - This uses basic math to combine motions and is easier to drive straight.

        // Sticks work via potentiometers, so they can have a value from -1 to 1.
        leftStickX = -gamepad1.left_stick_x;
        leftStickY = gamepad1.left_stick_y;
        rightStickX = -gamepad1.right_stick_x;

        double axial = leftStickY;
        double lateral = leftStickX;
        double yaw = rightStickX;

        leftFrontPower = axial + lateral + yaw;
        rightFrontPower = axial - lateral - yaw;
        leftBackPower = axial - lateral + yaw;
        rightBackPower = axial + lateral - yaw;

        // Send calculated power to wheels
        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);

        // Good place to handle any other logic for things like claws or arms.

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime);
        telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
        telemetry.update();
    }

}