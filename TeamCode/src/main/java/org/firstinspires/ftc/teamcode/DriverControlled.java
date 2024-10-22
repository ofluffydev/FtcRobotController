package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
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
    public boolean toggle = false;
    public boolean prevA = false;
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor armMotor = null;
    private CRServo spinnyThing = null;
    private Servo toggleServo = null;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        /* This expects the motors to be plugged in in the following configuration:
         * - left_front_drive:  Port 0
         * - left_back_drive:   Port 1
         * - right_front_drive: Port 2
         * - right_back_drive:  Port 3
         */

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        // TODO: Come up with solution to make this more dynamic. (External hardware class)
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        armMotor = hardwareMap.get(DcMotor.class, "arm");
        spinnyThing = hardwareMap.get(CRServo.class, "spinny_thing");
        toggleServo = hardwareMap.get(Servo.class, "toggle_servo");


        // Flip it if the wheel is going an unexpected direction.
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.REVERSE);

        armMotor.setDirection(DcMotor.Direction.FORWARD);

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
        double spinPower;
        double armPower = 0.0;

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

        // Good place to handle any other logic for things like claws or arms.

        System.out.println("Setting spin power");
        // Spin power
        if (gamepad1.right_bumper) spinPower = 1;
        else if (gamepad1.left_bumper) spinPower = -1;
        else spinPower = 0;
        spinnyThing.setPower(spinPower);

        System.out.println("Setting arm power");
        // Dpad up and down for the arm
        if (gamepad1.dpad_up) {
            // Move the arm up
            armPower = 1.0;
        } else if (gamepad1.dpad_down) {
            // Move the arm down
            armPower = -1.0;
        }

        // Toggle rotation
        if (gamepad1.a && !prevA) {
            toggle = !toggle;
        }
        prevA = gamepad1.a;
        if (toggle) toggleServo.setPosition(0.5);
        else toggleServo.setPosition(0);

        // Send calculated power to wheels
        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);

        // Send calculated power to arm
        armMotor.setPower(armPower);

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime);
        telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
        telemetry.update();
    }

}