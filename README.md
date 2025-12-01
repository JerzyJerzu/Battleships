# Battleships Game

## 🎯 Overview

This project is an interview task and serves as a technical demonstration of implementing a classic **Battleship** game. The task was to implement two algorithmic players to compete against each other. For that I implemented 3 player strategies:
*   #### **Random player:**
    The simplest strategy, randomly shoots at undiscovered fields
*   #### **Informed Random player:**
    Similar to the Random player, but when it hits a ship, it tries to find the rest of the ship segments in subsequent moves
*   #### **Try Hard player:**
    Upgraded version of the Informed Random player. When it sinks all the ships smaller than a certain size, it iterates the board and marks all fields where the remaining ships cannot fit, to avoid shooting at them later.

## 🚀 How to Run

### Prerequisites
*   **Java Development Kit (JDK)**: Version 17 or higher.

### Instructions

1.  **Open the project** in your terminal or IDE (IntelliJ IDEA recommended).

2.  **Run the game** using the Gradle Wrapper:

    *   **Default Strategies (InformedRandom vs InformedRandom):**
        *   **On Linux / macOS:**
            ```bash
            ./gradlew run
            ```

        *   **On Windows:**
            ```cmd
            gradlew.bat run
            ```

    *   **With Custom Strategies:**
        You can specify the strategies for Player 1 and Player 2 as command-line arguments.
        Available strategy names: `random`, `informedrandom`, `tryhard`.

        *   **Example (InformedRandom vs TryHard):**
            *   **On Linux / macOS:**
                ```bash
                ./gradlew run --args="informedrandom tryhard"
                ```
            *   **On Windows:**
                ```cmd
                gradlew.bat run --args="informedrandom tryhard"
                ```

        *   **Example (Random vs Random):**
            *   **On Linux / macOS:**
                ```bash
                ./gradlew run --args="random random"
                ```
            *   **On Windows:**
                ```cmd
                gradlew.bat run --args="random random"
                ```

3.  **View the Results**:
    *   The game simulation will run in the console.
    *   A detailed log file will be generated in the project root directory with the format: `ships-game-yyyyMMdd-HHmmss.log`.
    *   Open this log file to inspect every move, ship placement, and the final game statistics.

## 📋 Rules

### Gameplay
*   Players take turns firing shots.
*   A shot can result in a `hit`, `miss`, or `sunk` (the entire ship is sunk).
*   The game ends when all of one player's ships are sunk.
*   You cannot shoot at the same field twice.
*   You cannot shoot at a field that is already "discovered" (meaning it is known to be empty).
*   Ships cannot touch each other by sides (only diagonally).
*   Ships cannot occupy the same fields or extend beyond the board boundaries.

### Opponent
*   The algorithm can be simple (e.g., random shots at undiscovered fields).
*   It **MUST** adhere to the rules.

### Program Output

Each run of the game simulates **a single match** and generates a log file.

**File Name:** `ships-game-[timestamp].log`
**Timestamp Format:** `yyyyMMdd-HHmmss` (e.g., `ships-game-20241024-143052.log`)

**Log Format** (from Player 1's perspective):
```
HH:mm:ss.SSS place-ship: size=4 pos=(0,0) dir=horizontal
HH:mm:ss.SSS place-ship: size=2 pos=(4,2) dir=vertical
HH:mm:ss.SSS shot: pos=(4,2) result=miss
HH:mm:ss.SSS enemy-shot: pos=(3,1) result=hit
HH:mm:ss.SSS shot: pos=(5,2) result=hit
HH:mm:ss.SSS shot: pos=(5,3) result=sunk ship-size=2
HH:mm:ss.SSS enemy-shot: pos=(0,0) result=hit
HH:mm:ss.SSS game-over: result=win total-shots=47 enemy-total-shots=52
HH:mm:ss.SSS enemy-ship: size=4 pos=(2,3) dir=vertical
HH:mm:ss.SSS enemy-ship: size=2 pos=(5,2) dir=horizontal
```
**Event Types:**
*   `place-ship`: Ship placement (at the beginning of the game)
*   `shot`: Player 1's shot
*   `enemy-shot`: Opponent's shot (Player 2)
*   `game-over`: End of the game with the result
*   `enemy-ship`: Player 2's ship placement (summary after the game ends)

**Shot Results:**
*   `miss` - Missed shot
*   `hit` - Hit a ship
*   `sunk ship-size=X` - Ship sunk (specifies the ship size)

---