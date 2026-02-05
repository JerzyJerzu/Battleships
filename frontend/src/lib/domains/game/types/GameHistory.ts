import type { GameStateSnapshot } from './GameStateSnapshot';
import type { ShipPosition } from './ShipPosition';

export interface GameHistory {
	player1ShipLayout: ShipPosition[];
	player2ShipLayout: ShipPosition[];
	snapshots: GameStateSnapshot[];
}
