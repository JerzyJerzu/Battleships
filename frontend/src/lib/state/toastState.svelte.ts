/**
 * Global toast state for displaying notifications.
 * Uses Svelte 5 $state rune for reactivity.
 */

export type ToastType = 'error' | 'success' | 'info';

export interface ToastMessage {
	id: number;
	type: ToastType;
	message: string;
}

let nextId = 0;
let toasts: ToastMessage[] = $state([]);

function addToast(type: ToastType, message: string): void {
	const id = nextId++;
	toasts.push({ id, type, message });

	// Auto-dismiss after 5 seconds
	setTimeout(() => {
		removeToast(id);
	}, 5000);
}

function removeToast(id: number): void {
	toasts = toasts.filter((t) => t.id !== id);
}

// Convenience functions for different toast types
export function showError(message: string): void {
	addToast('error', message);
}

export function showSuccess(message: string): void {
	addToast('success', message);
}

export function showInfo(message: string): void {
	addToast('info', message);
}

// Export for Toast component to read
export function getToasts(): ToastMessage[] {
	return toasts;
}

// Export removeToast for dismiss button
export { removeToast };
