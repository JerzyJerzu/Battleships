<script lang="ts">
	import type { GameStateSnapshot } from '../../types';

	interface Props {
		snapshot: GameStateSnapshot;
		testId?: string;
	}

	let { snapshot, testId = 'status-bar' }: Props = $props();

	const statusText = $derived.by(() => {
		if (snapshot.status === 'FINISHED') {
			const winner = snapshot.player1Won ? 1 : 2;
			return `Game Over - Player ${winner} wins!`;
		}
		return `Turn ${snapshot.turn} - Player ${snapshot.currentPlayer}'s move`;
	});

	const statusClass = $derived(
		snapshot.status === 'FINISHED'
			? 'bg-green-100 text-green-800 border-green-300'
			: 'bg-blue-100 text-blue-800 border-blue-300'
	);
</script>

<div
	data-testid={testId}
	class="rounded-lg border px-6 py-3 text-center font-medium {statusClass}"
>
	<span data-testid="{testId}-text">{statusText}</span>
</div>
