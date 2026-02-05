/**
 * State management for the game viewer page.
 * Holds loaded game data and current snapshot index for replay navigation.
 */

import type { GameResponse, GameHistory, GameStateSnapshot, ShipPosition } from '../types';
import { GameService } from '../services/GameService';

// Reactive state - explicit generic types needed for nullable state
let gameId = $state<string | null>(null);
let history = $state<GameHistory | null>(null);
let currentSnapshotIndex = $state(0);
let loading = $state(false);
let error = $state<string | null>(null);

// Derived values - automatically update when state changes
const currentSnapshot: GameStateSnapshot | null = $derived(
	history ? history.snapshots[currentSnapshotIndex] : null
);

const totalSnapshots: number = $derived(history ? history.snapshots.length : 0);

const player1Ships: ShipPosition[] = $derived(history ? history.player1ShipLayout : []);

const player2Ships: ShipPosition[] = $derived(history ? history.player2ShipLayout : []);

const isFirstSnapshot: boolean = $derived(currentSnapshotIndex === 0);

const isLastSnapshot: boolean = $derived(
	totalSnapshots > 0 && currentSnapshotIndex === totalSnapshots - 1
);

// Actions
async function loadGame(id: string): Promise<void> {
	loading = true;
	error = null;

	try {
		const response: GameResponse = await GameService.getGame(id);
		gameId = response.gameId;
		history = response.history;
		// Start at the last snapshot (final game state)
		currentSnapshotIndex = response.history.snapshots.length - 1;
	} catch (e) {
		error = e instanceof Error ? e.message : 'Failed to load game';
		throw e; // Re-throw so component can show toast
	} finally {
		loading = false;
	}
}

function nextTurn(): void {
	if (!isLastSnapshot) {
		currentSnapshotIndex++;
	}
}

function previousTurn(): void {
	if (!isFirstSnapshot) {
		currentSnapshotIndex--;
	}
}

function goToTurn(index: number): void {
	if (index >= 0 && index < totalSnapshots) {
		currentSnapshotIndex = index;
	}
}

function reset(): void {
	gameId = null;
	history = null;
	currentSnapshotIndex = 0;
	loading = false;
	error = null;
}

// Export getters for reactive access in components
export function getGameId(): string | null {
	return gameId;
}

export function getCurrentSnapshot(): GameStateSnapshot | null {
	return currentSnapshot;
}

export function getPlayer1Ships(): ShipPosition[] {
	return player1Ships;
}

export function getPlayer2Ships(): ShipPosition[] {
	return player2Ships;
}

export function isLoading(): boolean {
	return loading;
}

export function getError(): string | null {
	return error;
}

export function getTotalSnapshots(): number {
	return totalSnapshots;
}

export function getCurrentSnapshotIndex(): number {
	return currentSnapshotIndex;
}

export function canGoNext(): boolean {
	return !isLastSnapshot;
}

export function canGoPrevious(): boolean {
	return !isFirstSnapshot;
}

// Export actions
export { loadGame, nextTurn, previousTurn, goToTurn, reset };
