package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@SuppressWarnings("unused")
@Autonomous(name = "Simple drive forward autonomous WITH DELAY", group = "Robot", preselectTeleOp = "Duo Driver Controlled")
public class DriveForwardSimpleAutoDelayed extends LinearOpMode {

    // Calculate the COUNTS_PER_INCH for your specific drive train.
    // Go to your motor vendor website to determine your motor's COUNTS_PER_MOTOR_REV
    // For external drive gearing, set DRIVE_GEAR_REDUCTION as needed.
    // For example, use a value of 2.0 for a 12-tooth spur gear driving a 24-tooth spur gear.
    // This is gearing DOWN for less speed and more torque.
    // For gearing UP, use a gear ratio less than 1.0. Note this will affect the direction of wheel rotation.
    static final double COUNTS_PER_MOTOR_REV = 537.7;
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // No External Gearing.
    static final double WHEEL_DIAMETER_INCHES = 3.78;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.3;
    static final double TURN_SPEED = 0.5;
    static final long SLEEP_TIME_IN_SECONDS = 23;
    private final ElapsedTime runtime = new ElapsedTime();
    RobotHardware robot = null;

    @Override
    public void runOpMode() {

        robot = new RobotHardware(this);

        robot.resetEncoders();

        robot.useEncoders();

        // Wait for the game to start (driver presses START)
        waitForStart();

        // Sleep until the 30 second autonomous period is almost over
        try {
            Thread.sleep(SLEEP_TIME_IN_SECONDS * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        driveForward(60, DRIVE_SPEED);  // S1: Forward 48 Inches with 5 Sec timeout

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);  // pause to display final telemetry message.
    }

    public void driveForward(double inches, double speed) {
        encoderDrive(speed, inches, inches, inches, inches, 5.0);
    }

    public void encoderDrive(double speed, double leftFrontInches, double rightFrontInches, double leftBackInches, double rightBackInches, double timeoutS) {
        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newLeftBackTarget;
        int newRightBackTarget;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftFrontTarget = robot.getLeftFrontDrive().getCurrentPosition() + (int) (leftFrontInches * COUNTS_PER_INCH);
            newRightFrontTarget = robot.getRightFrontDrive().getCurrentPosition() + (int) (rightFrontInches * COUNTS_PER_INCH);
            newLeftBackTarget = robot.getLeftBackDrive().getCurrentPosition() + (int) (leftBackInches * COUNTS_PER_INCH);
            newRightBackTarget = robot.getRightBackDrive().getCurrentPosition() + (int) (rightBackInches * COUNTS_PER_INCH);

            robot.setTargets(newLeftFrontTarget, newRightFrontTarget, newLeftBackTarget, newRightBackTarget);

            // Turn On RUN_TO_POSITION
            robot.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.setPowers(Math.abs(speed));

            while (opModeIsActive() && (runtime.seconds() < timeoutS) && (robot.safeIsBusy())) {
                // Display it for the driver.
                telemetry.addData("running to: ", "%d %d %d %d", newLeftFrontTarget, newRightFrontTarget, newLeftBackTarget, newRightBackTarget);
                telemetry.update();
            }

            // Stop all motion;
            robot.setPowers(0);

            // Turn off RUN_TO_POSITION
            robot.setMode(RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move.
        }
    }
}
