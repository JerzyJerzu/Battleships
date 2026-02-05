<script lang="ts">
	import type { ShipPosition, PlayerState, Coordinate, ShotResult } from '../../types';
	import BoardCell from './BoardCell.svelte';

	interface Props {
		ships: ShipPosition[];
		opponentState: PlayerState; // The opponent's shots on this board
		showShips?: boolean;
		testId?: string;
	}

	let { ships, opponentState, showShips = true, testId = 'board' }: Props = $props();

	const BOARD_SIZE = 10;

	// Compute which cells contain ships
	const shipCells = $derived.by(() => {
		const cells = new Set<string>();
		for (const ship of ships) {
			for (let i = 0; i < ship.size; i++) {
				const x = ship.direction === 'HORIZONTAL' ? ship.x + i : ship.x;
				const y = ship.direction === 'VERTICAL' ? ship.y + i : ship.y;
				cells.add(`${x},${y}`);
			}
		}
		return cells;
	});

	// Create a map of shots for quick lookup
	const shotMap = $derived.by(() => {
		const map = new Map<string, ShotResult>();
		for (const shot of opponentState.shotsFired) {
			map.set(`${shot.coordinate.x},${shot.coordinate.y}`, shot.result);
		}
		return map;
	});

	// Create a set of forbidden cells for quick lookup
	const forbiddenCells = $derived.by(() => {
		const cells = new Set<string>();
		for (const coord of opponentState.forbiddenMoves) {
			cells.add(`${coord.x},${coord.y}`);
		}
		return cells;
	});

	// Helper to check cell state
	function hasShip(x: number, y: number): boolean {
		return showShips && shipCells.has(`${x},${y}`);
	}

	function getShotResult(x: number, y: number): ShotResult | null {
		return shotMap.get(`${x},${y}`) ?? null;
	}

	function isForbidden(x: number, y: number): boolean {
		return forbiddenCells.has(`${x},${y}`);
	}

	// Generate row/column indices
	const rows = Array.from({ length: BOARD_SIZE }, (_, i) => i);
	const cols = Array.from({ length: BOARD_SIZE }, (_, i) => i);
</script>

<div data-testid={testId} class="inline-block">
	<!-- Column headers (A-J) -->
	<div class="flex">
		<div class="h-6 w-8"></div> <!-- Empty corner -->
		{#each cols as col}
			<div class="flex h-6 w-8 items-center justify-center text-xs font-medium text-gray-600">
				{String.fromCharCode(65 + col)}
			</div>
		{/each}
	</div>

	<!-- Rows with row numbers -->
	{#each rows as row}
		<div class="flex">
			<!-- Row number (1-10) -->
			<div class="flex h-8 w-8 items-center justify-center text-xs font-medium text-gray-600">
				{row + 1}
			</div>
			<!-- Cells -->
			{#each cols as col}
				<BoardCell
					x={col}
					y={row}
					hasShip={hasShip(col, row)}
					shotResult={getShotResult(col, row)}
					isForbidden={isForbidden(col, row)}
					testId="{testId}-cell"
				/>
			{/each}
		</div>
	{/each}
</div>
