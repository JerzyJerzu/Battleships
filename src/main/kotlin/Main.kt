import game.Game
import strategy.InformedRandomPlayerStrategy
import strategy.PlayerStrategy
import strategy.RandomPlayerStrategy
import strategy.TryHardPlayerStrategy

fun main(args: Array<String>) {
    println("Initializing Battleships...")

    try {
        val player1Strategy: PlayerStrategy
        val player2Strategy: PlayerStrategy

        if (args.size >= 2) {
            player1Strategy = createStrategy(args[0])
            player2Strategy = createStrategy(args[1])
        } else {
            println("No strategies provided or incomplete. Defaulting to InformedRandomPlayerStrategy for both players.")
            player1Strategy = InformedRandomPlayerStrategy()
            player2Strategy = InformedRandomPlayerStrategy()
        }

        // Game is now a singleton object
        Game.play(
            player1Strategy = player1Strategy,
            player2Strategy = player2Strategy
        )
        println("Game completed successfully.")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun createStrategy(strategyName: String): PlayerStrategy {
    return when (strategyName.lowercase()) {
        "random" -> RandomPlayerStrategy()
        "informedrandom" -> InformedRandomPlayerStrategy()
        "tryhard" -> TryHardPlayerStrategy()
        else -> {
            println("Unknown strategy '$strategyName'. Defaulting to InformedRandomPlayerStrategy.")
            InformedRandomPlayerStrategy()
        }
    }
}