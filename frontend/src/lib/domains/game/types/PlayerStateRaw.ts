import type { Coordinate } from './Coordinate';
import type { ShotResult } from './ShotResult';

/**
 * Backend format for player state.
 * shotsFired is a Record where key is "x,y" string and value is the result.
 */
export interface PlayerStateRaw {
	shotsFired: Record<string, ShotResult>;
	forbiddenMoves: Coordinate[];
}
