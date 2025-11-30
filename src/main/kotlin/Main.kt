import game.Game
import strategy.RandomPlayerStrategy

fun main() {
    println("Initializing Battleships...")
    
    try {
        // Game is now a singleton object
        Game.play(
            player1Strategy = RandomPlayerStrategy(),
            player2Strategy = RandomPlayerStrategy()
        )
        println("Game completed successfully.")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}