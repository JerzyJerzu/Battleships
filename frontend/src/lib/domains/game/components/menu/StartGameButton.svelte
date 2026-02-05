<script lang="ts">
	interface Props {
		onclick: () => void;
		disabled?: boolean;
		loading?: boolean;
		testId?: string;
	}

	let { onclick, disabled = false, loading = false, testId = 'start-game-btn' }: Props = $props();
</script>

<button
	data-testid={testId}
	{onclick}
	disabled={disabled || loading}
	class="
		rounded-lg border border-transparent
		bg-blue-600 px-8 py-4 text-lg font-semibold text-white
		shadow-md transition-all
		hover:bg-blue-700 hover:shadow-lg
		focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2
		disabled:cursor-not-allowed disabled:opacity-50
	"
>
	{#if loading}
		<span data-testid="{testId}-loading">Starting game...</span>
	{:else}
		<span data-testid="{testId}-text">Start AI vs AI Game</span>
	{/if}
</button>
