import type { GameStatus } from './GameStatus';
import type { PlayerStateRaw } from './PlayerStateRaw';

/**
 * Backend format for game state snapshot.
 */
export interface GameStateSnapshotRaw {
	turn: number;
	currentPlayer: number;
	status: GameStatus;
	player1Won: boolean | null;
	player1State: PlayerStateRaw;
	player2State: PlayerStateRaw;
}
