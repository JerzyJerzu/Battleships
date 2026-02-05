import type { Coordinate } from './Coordinate';
import type { Shot } from './Shot';

export interface PlayerState {
	shotsFired: Shot[];
	forbiddenMoves: Coordinate[];
}
