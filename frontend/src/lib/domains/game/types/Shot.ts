import type { Coordinate } from './Coordinate';
import type { ShotResult } from './ShotResult';

export interface Shot {
	coordinate: Coordinate;
	result: ShotResult;
}
