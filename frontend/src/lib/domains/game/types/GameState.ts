import type { GameStatus } from './GameStatus';

export interface GameState {
	id: string;
	status: GameStatus;
	currentPlayer: number;
	winner: number | null;
	currentTurn: number;
}
