import type { GameHistoryRaw } from './GameHistoryRaw';

/**
 * Backend format for game response.
 */
export interface GameResponseRaw {
	gameId: string;
	history: GameHistoryRaw;
}
