<script lang="ts">
	import '../app.css';
	import { resolve } from '$app/paths';
	import Toast from '$lib/components/Toast.svelte';

	let { children } = $props();
</script>

<Toast />

<div data-testid="app-layout" class="min-h-screen bg-gray-100">
	<!-- Header -->
	<header data-testid="app-header" class="bg-blue-600 shadow-md">
		<nav class="mx-auto flex max-w-4xl items-center justify-between px-4 py-4">
			<a
				data-testid="nav-home"
				href={resolve('/')}
				class="text-2xl font-bold text-white hover:text-blue-100"
			>
				Battleship
			</a>
		</nav>
	</header>

	<!-- Main content -->
	<main data-testid="app-main" class="mx-auto max-w-4xl px-4 py-8">
		{@render children()}
	</main>
</div>
