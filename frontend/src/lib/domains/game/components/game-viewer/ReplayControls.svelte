<script lang="ts">
	interface Props {
		currentTurn: number;
		totalTurns: number;
		canGoNext: boolean;
		canGoPrevious: boolean;
		onNext: () => void;
		onPrevious: () => void;
		testId?: string;
	}

	let {
		currentTurn,
		totalTurns,
		canGoNext,
		canGoPrevious,
		onNext,
		onPrevious,
		testId = 'replay-controls'
	}: Props = $props();
</script>

<div data-testid={testId} class="flex items-center gap-4">
	<!-- Previous button -->
	<button
		data-testid="{testId}-prev"
		onclick={onPrevious}
		disabled={!canGoPrevious}
		class="
			rounded-lg bg-gray-200 px-4 py-2 font-medium text-gray-700
			transition-colors hover:bg-gray-300
			disabled:cursor-not-allowed disabled:opacity-50
		"
	>
		← Previous
	</button>

	<!-- Turn indicator -->
	<span data-testid="{testId}-indicator" class="text-sm text-gray-600">
		Turn {currentTurn} / {totalTurns - 1}
	</span>

	<!-- Next button -->
	<button
		data-testid="{testId}-next"
		onclick={onNext}
		disabled={!canGoNext}
		class="
			rounded-lg bg-gray-200 px-4 py-2 font-medium text-gray-700
			transition-colors hover:bg-gray-300
			disabled:cursor-not-allowed disabled:opacity-50
		"
	>
		Next →
	</button>
</div>
