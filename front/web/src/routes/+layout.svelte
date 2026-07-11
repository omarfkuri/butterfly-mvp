<script lang="ts">
	import favicon from '$lib/assets/image/brand/Icon.png';

	import "$lib/styles/global.less";

	let { children } = $props();
</script>

<svelte:head>
	<link rel="icon" href={favicon} />
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
</svelte:head>

{@render children()}