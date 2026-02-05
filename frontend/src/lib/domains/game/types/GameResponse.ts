import type { GameHistory } from './GameHistory';

export interface GameResponse {
	gameId: string;
	history: GameHistory;
}
