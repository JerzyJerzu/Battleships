import type { GameStateSnapshotRaw } from './GameStateSnapshotRaw';
import type { ShipPosition } from './ShipPosition';

/**
 * Backend format for game history.
 */
export interface GameHistoryRaw {
	player1ShipLayout: ShipPosition[];
	player2ShipLayout: ShipPosition[];
	snapshots: GameStateSnapshotRaw[];
}
