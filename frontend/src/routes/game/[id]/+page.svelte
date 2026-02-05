<script lang="ts">
	import { page } from '$app/state';
	import { goto } from '$app/navigation';
	import { resolve } from '$app/paths';
	import { showError } from '$lib/state/toastState.svelte';

	// Import current game state and components
	import {
		loadGame,
		reset,
		getCurrentSnapshot,
		getPlayer1Ships,
		getPlayer2Ships,
		isLoading,
		nextTurn,
		previousTurn,
		canGoNext,
		canGoPrevious,
		getTotalSnapshots,
		getCurrentSnapshotIndex
	} from '$lib/domains/game/state/currentGame.svelte';
	import PlayerBoardPanel from '$lib/domains/game/components/game-viewer/PlayerBoardPanel.svelte';
	import GameStatusBar from '$lib/domains/game/components/game-viewer/GameStatusBar.svelte';
	import ReplayControls from '$lib/domains/game/components/game-viewer/ReplayControls.svelte';

	// Get game ID from URL parameter (e.g., /game/abc123 → id = "abc123")
	const gameId = $derived(page.params.id);

	// Load game when component mounts
	$effect(() => {
		if (gameId) {
			loadGame(gameId).catch((e) => {
				showError(e instanceof Error ? e.message : 'Failed to load game');
				goto(resolve('/'));
			});
		}

		// Cleanup when leaving the page
		return () => {
			reset();
		};
	});

	// Get reactive state values
	const snapshot = $derived(getCurrentSnapshot());
	const player1Ships = $derived(getPlayer1Ships());
	const player2Ships = $derived(getPlayer2Ships());
	const loading = $derived(isLoading());

	// Replay controls state
	const totalTurns = $derived(getTotalSnapshots());
	const currentTurnIndex = $derived(getCurrentSnapshotIndex());
	const canNext = $derived(canGoNext());
	const canPrev = $derived(canGoPrevious());
</script>

<div data-testid="game-viewer-page">
	{#if loading}
		<!-- Loading state -->
		<div data-testid="game-loading" class="flex items-center justify-center py-16">
			<p class="text-lg text-gray-600">Loading game...</p>
		</div>
	{:else if snapshot}
		<!-- Game loaded successfully -->
		<div class="flex flex-col items-center gap-6">
			<!-- Status bar at top -->
			<GameStatusBar {snapshot} />

			<!-- Two boards side by side -->
			<div
				data-testid="game-boards"
				class="flex flex-wrap items-start justify-center gap-8"
			>
				<!-- Player 1's board - shows Player 2's shots on it -->
				<PlayerBoardPanel
					playerNumber={1}
					ships={player1Ships}
					opponentState={snapshot.player2State}
				/>

				<!-- Player 2's board - shows Player 1's shots on it -->
				<PlayerBoardPanel
					playerNumber={2}
					ships={player2Ships}
					opponentState={snapshot.player1State}
				/>
			</div>

			<!-- Replay controls -->
			<ReplayControls
				currentTurn={currentTurnIndex}
				{totalTurns}
				canGoNext={canNext}
				canGoPrevious={canPrev}
				onNext={nextTurn}
				onPrevious={previousTurn}
			/>
		</div>
	{:else}
		<!-- No game data (shouldn't happen normally) -->
		<div data-testid="game-error" class="flex items-center justify-center py-16">
			<p class="text-lg text-red-600">Failed to load game</p>
		</div>
	{/if}
</div>
