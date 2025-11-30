import game.Game
import strategy.InformedRandomPlayerStrategy
import strategy.RandomPlayerStrategy
import strategy.TryHardPlayerStrategy

fun main() {
    println("Initializing Battleships...")
    
    try {
        // Game is now a singleton object
        Game.play(
            player1Strategy = InformedRandomPlayerStrategy(),
            player2Strategy = TryHardPlayerStrategy()
        )
        println("Game completed successfully.")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}