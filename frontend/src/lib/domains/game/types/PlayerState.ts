import type { Coordinate } from './Coordinate';

/**
 * Player state matching backend format directly.
 * shotsFired keys are "(x,y)" strings, values are "hit"/"miss"/"sunk ship-size=N"
 */
export interface PlayerState {
	shotsFired: Record<string, string>;
	forbiddenMoves: Coordinate[];
}
