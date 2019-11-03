package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@TeleOp(name = "Soccerbot")
class Soccerbot(): OpMode() {
    private lateinit var leftDrive: DcMotor
    private lateinit var rightDrive: DcMotor

    private val TURNS_ENABLED = true
    private val TURN_LENGTH = 2 * 60 * 1000 // 2 minutes

    private var turnStartTime: Long = 0
    private var isTurnActive = false

    val timeFormatter = SimpleDateFormat("mm:ss", Locale.US)

    override fun init() {
        leftDrive = hardwareMap.dcMotor.get("left")!!.also { it.direction = DcMotorSimple.Direction.REVERSE }
        rightDrive = hardwareMap.dcMotor.get("right")!!.also { it.direction = DcMotorSimple.Direction.FORWARD }

        telemetry.addLine("Soccerbot Initialized!")
        telemetry.addLine()
        telemetry.addLine("Press START to Begin")
        telemetry.update()
    }

    override fun loop() {
        if (gamepad2.left_trigger > 0.5 || gamepad2.right_trigger > 0.5) overrideMode()
        else {
            telemetry.addLine("Welcome to Seattle Academy!")
            if (!TURNS_ENABLED || isTurnActive) turnActive()
            else turnInactive()
        }

        telemetry.update()
    }

    private fun overrideMode() {
        telemetry.addLine("""
                ---------------------------
                !!!!!! OVERRIDE MODE !!!!!!
                VISITOR DRIVING IS DISABLED
                ---------------------------
            """.trimIndent())

        leftDrive.power = gamepad2.left_stick_y.toDouble()
        rightDrive.power = gamepad2.right_stick_y.toDouble()
    }

    private fun turnActive() {
        leftDrive.power = gamepad1.left_stick_y.toDouble()
        rightDrive.power = gamepad1.right_stick_y.toDouble()

        telemetry.addLine(gamepad1.left_stick_y.toString())

        if (TURNS_ENABLED) {
            val timeElapsed = System.currentTimeMillis() - turnStartTime
            val timeRemaining = TURN_LENGTH - timeElapsed

            if (timeRemaining <= 0) {
                isTurnActive = false
                turnStartTime = 0
            }
        }
    }

    private fun turnInactive() {
        telemetry.addLine("Press A to start")
        if (gamepad1.a) {
            isTurnActive = true
            turnStartTime = System.currentTimeMillis()
        }
    }
}