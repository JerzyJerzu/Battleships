<script lang="ts">
	import { getToasts, removeToast, type ToastMessage } from '$lib/state/toastState.svelte';

	// Reactive - re-renders when toasts change
	const toasts: ToastMessage[] = $derived(getToasts());

	function getBackgroundColor(type: ToastMessage['type']): string {
		switch (type) {
			case 'error':
				return 'bg-red-600';
			case 'success':
				return 'bg-green-600';
			case 'info':
				return 'bg-blue-600';
		}
	}
</script>

<!-- Fixed position in top-right corner -->
<div data-testid="toast-container" class="fixed right-4 top-4 z-50 flex flex-col gap-2">
	{#each toasts as toast (toast.id)}
		<div
			data-testid="toast-message"
			class="{getBackgroundColor(toast.type)} flex items-center gap-3 rounded-lg px-4 py-3 text-white shadow-lg"
		>
			<span data-testid="toast-text">{toast.message}</span>
			<button
				data-testid="toast-dismiss"
				onclick={() => removeToast(toast.id)}
				class="ml-2 text-white/80 hover:text-white"
			>
				✕
			</button>
		</div>
	{/each}
</div>
