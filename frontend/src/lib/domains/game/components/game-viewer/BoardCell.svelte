<script lang="ts">
	import type { ShotResult } from '../../types';

	interface Props {
		x: number;
		y: number;
		hasShip: boolean;
		shotResult: ShotResult | null;
		isForbidden: boolean;
		testId?: string;
	}

	let { x, y, hasShip, shotResult, isForbidden, testId = 'cell' }: Props = $props();

	// Determine cell appearance based on state
	const cellClass = $derived.by(() => {
		// Shot results take priority
		if (shotResult === 'hit' || shotResult === 'sunk') {
			return 'bg-red-500'; // Hit - red
		}
		if (shotResult === 'miss') {
			return 'bg-blue-400'; // Miss - blue
		}
		// No shot yet - show ship or empty
		if (hasShip) {
			return 'bg-gray-500'; // Ship - gray
		}
		return 'bg-gray-200'; // Empty - light gray
	});
</script>

<div
	data-testid="{testId}-{x}-{y}"
	class="relative h-8 w-8 border border-gray-300 {cellClass}"
>
	{#if isForbidden && !shotResult}
		<!-- Forbidden marker (X) - only show if not already shot -->
		<span
			data-testid="{testId}-{x}-{y}-forbidden"
			class="absolute inset-0 flex items-center justify-center text-gray-400 text-xs"
		>
			✕
		</span>
	{/if}
</div>
