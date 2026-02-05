import type { GameStatus } from './GameStatus';
import type { PlayerState } from './PlayerState';

export interface GameStateSnapshot {
	turn: number;
	currentPlayer: number;
	status: GameStatus;
	player1Won: boolean | null;
	player1State: PlayerState;
	player2State: PlayerState;
}
