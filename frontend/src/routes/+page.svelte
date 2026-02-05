<script lang="ts">
	import { goto } from '$app/navigation';
	import { resolve } from '$app/paths';
	import { GameService } from '$lib/domains/game/services/GameService';
	import { showError } from '$lib/state/toastState.svelte';
	import StartGameButton from '$lib/domains/game/components/menu/StartGameButton.svelte';

	let loading = $state(false);

	async function startGame() {
		loading = true;
		try {
			const game = await GameService.createAIvsAIGame();
			goto(resolve(`/game/${game.gameId}`));
		} catch (e) {
			showError(e instanceof Error ? e.message : 'Failed to start game');
		} finally {
			loading = false;
		}
	}
</script>

<div data-testid="menu-page" class="flex flex-col items-center gap-8 py-16">
	<h1 data-testid="menu-title" class="text-4xl font-bold text-gray-800">
		Welcome to Battleship
	</h1>

	<p data-testid="menu-description" class="text-center text-gray-600">
		Watch two AI players battle it out!
	</p>

	<StartGameButton onclick={startGame} {loading} />
</div>
