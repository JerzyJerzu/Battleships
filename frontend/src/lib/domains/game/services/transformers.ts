import type {
	PlayerState,
	PlayerStateRaw,
	GameStateSnapshot,
	GameStateSnapshotRaw,
	GameHistory,
	GameHistoryRaw,
	GameResponse,
	GameResponseRaw,
	Shot,
	ShotResult
} from '../types';
//TODO This amount of code may not be necessary at all
/**
 * Transforms backend PlayerStateRaw to frontend PlayerState.
 * Converts { "0,0": "hit", "1,2": "miss" } to Shot[] array.
 */
export function transformPlayerState(raw: PlayerStateRaw): PlayerState {
	const shotsFired: Shot[] = Object.entries(raw.shotsFired).map(([key, result]) => {
		const [x, y] = key.split(',').map(Number);
		return {
			coordinate: { x, y },
			result: normalizeResult(result)
		};
	});

	return {
		shotsFired,
		forbiddenMoves: raw.forbiddenMoves
	};
}

/**
 * Normalizes backend result string to frontend ShotResult.
 * Backend may return "sunk ship-size=X" which we normalize to "sunk".
 */
function normalizeResult(result: string): ShotResult {
	if (result === 'miss') return 'miss';
	if (result === 'hit') return 'hit';
	if (result.startsWith('sunk')) return 'sunk';
	return 'miss'; // fallback
}

/**
 * Transforms backend GameStateSnapshotRaw to frontend GameStateSnapshot.
 */
export function transformSnapshot(raw: GameStateSnapshotRaw): GameStateSnapshot {
	return {
		turn: raw.turn,
		currentPlayer: raw.currentPlayer,
		status: raw.status,
		player1Won: raw.player1Won,
		player1State: transformPlayerState(raw.player1State),
		player2State: transformPlayerState(raw.player2State)
	};
}

/**
 * Transforms backend GameHistoryRaw to frontend GameHistory.
 */
export function transformGameHistory(raw: GameHistoryRaw): GameHistory {
	return {
		player1ShipLayout: raw.player1ShipLayout,
		player2ShipLayout: raw.player2ShipLayout,
		snapshots: raw.snapshots.map(transformSnapshot)
	};
}

/**
 * Transforms backend GameResponseRaw to frontend GameResponse.
 */
export function transformGameResponse(raw: GameResponseRaw): GameResponse {
	return {
		gameId: raw.gameId,
		history: transformGameHistory(raw.history)
	};
}
