<script lang="ts">
	import type { ShipPosition, PlayerState } from '../../types';
	import Board from './Board.svelte';

	interface Props {
		playerNumber: 1 | 2;
		ships: ShipPosition[];
		opponentState: PlayerState; // Opponent's shots on this player's board
		testId?: string;
	}

	let { playerNumber, ships, opponentState, testId = 'player-board' }: Props = $props();
</script>

<div data-testid="{testId}-{playerNumber}" class="flex flex-col items-center gap-2">
	<h2
		data-testid="{testId}-{playerNumber}-title"
		class="text-lg font-semibold text-gray-800"
	>
		Player {playerNumber}
	</h2>
	<Board
		{ships}
		{opponentState}
		showShips={true}
		testId="{testId}-{playerNumber}-board"
	/>
</div>
