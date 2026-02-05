<script lang="ts">
	import { page } from '$app/state';
	import { goto } from '$app/navigation';
	import { resolve } from '$app/paths';
	import { showError } from '$lib/state/toastState.svelte';

	// Import game viewer state and components
	import {
		loadGame,
		reset,
		getCurrentSnapshot,
		getPlayer1Ships,
		getPlayer2Ships,
		isLoading
	} from '$lib/domains/game/state/gameViewerState.svelte';
	import PlayerBoardPanel from '$lib/domains/game/components/game-viewer/PlayerBoardPanel.svelte';
	import GameStatusBar from '$lib/domains/game/components/game-viewer/GameStatusBar.svelte';

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

			<!-- Turn info -->
			<p data-testid="turn-info" class="text-sm text-gray-500">
				Viewing turn {snapshot.turn} of the game
			</p>
		</div>
	{:else}
		<!-- No game data (shouldn't happen normally) -->
		<div data-testid="game-error" class="flex items-center justify-center py-16">
			<p class="text-lg text-red-600">Failed to load game</p>
		</div>
	{/if}
</div>
