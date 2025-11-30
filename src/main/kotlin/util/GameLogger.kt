package util

import model.Coordinate
import model.Direction
import model.ShotResult
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object GameLogger {
    private val logFile: File
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
    private val filenameFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")

    init {
        val timestamp = LocalDateTime.now().format(filenameFormatter)
        logFile = File("ships-game-$timestamp.log")
        logFile.createNewFile()
        println("Log file created: ${logFile.absolutePath}")
    }

    private fun log(message: String) {
        val timestamp = LocalDateTime.now().format(timeFormatter)
        logFile.appendText("$timestamp $message\n")
    }

    fun logPlaceShip(size: Int, pos: Coordinate, dir: Direction) {
        log("place-ship: size=$size pos=$pos dir=$dir")
    }

    fun logShot(pos: Coordinate, result: ShotResult) {
        log("shot: pos=$pos result=$result")
    }

    fun logEnemyShot(pos: Coordinate, result: ShotResult) {
        log("enemy-shot: pos=$pos result=$result")
    }

    fun logGameOver(result: String, totalShots: Int, enemyTotalShots: Int) {
        log("game-over: result=$result total-shots=$totalShots enemy-total-shots=$enemyTotalShots")
    }

    fun logEnemyShip(size: Int, pos: Coordinate, dir: Direction) {
        log("enemy-ship: size=$size pos=$pos dir=$dir")
    }
}