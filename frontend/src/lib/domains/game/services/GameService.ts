import type { GameState, GameResponse, PlayerType, ErrorResponse } from '../types';

const API_URL = 'http://localhost:8080/api/games';

/**
 * Parses error response from backend and throws with the actual message.
 * Falls back to statusText if body parsing fails.
 */
async function throwApiError(response: Response, fallbackMessage: string): Promise<never> {
	try {
		const errorBody: ErrorResponse = await response.json();
		throw new Error(errorBody.error);
	} catch (parseError) {
		// If JSON parsing failed, use fallback
		if (parseError instanceof Error && parseError.message !== 'Unexpected end of JSON input') {
			throw parseError; // Re-throw if it's our ErrorResponse error
		}
		throw new Error(`${fallbackMessage}: ${response.statusText}`);
	}
}

export class GameService {
	/**
	 * List all saved games (summary only).
	 */
	static async listGames(): Promise<GameState[]> {
		const response = await fetch(API_URL);
		if (!response.ok) {
			await throwApiError(response, 'Failed to list games');
		}
		return response.json();
	}

	/**
	 * Get full game history by ID.
	 * Returns backend format directly - no transformation.
	 */
	static async getGame(id: string): Promise<GameResponse> {
		const response = await fetch(`${API_URL}/${id}`);
		if (!response.ok) {
			await throwApiError(response, `Failed to get game ${id}`);
		}
		return response.json();
	}

	/**
	 * Create and run AI vs AI game.
	 * Returns full game history including all snapshots.
	 */
	static async createAIvsAIGame(
		player1Strategy: PlayerType = 'TRYHARD',
		player2Strategy: PlayerType = 'TRYHARD'
	): Promise<GameResponse> {
		const response = await fetch(`${API_URL}/ai-vs-ai`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				player1Strategy,
				player2Strategy
			})
		});

		if (!response.ok) {
			await throwApiError(response, 'Failed to create AI vs AI game');
		}

		return response.json();
	}
}
