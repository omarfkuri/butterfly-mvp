<script lang="ts">
  import { navigating } from '$app/state';
	import favicon from '$lib/assets/image/brand/Icon.png';

	import "$lib/styles/global.less";

	let { children } = $props();


</script>

<svelte:head>
	<link rel="icon" href={favicon} />
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
</svelte:head>

{#if navigating.to}
<progress></progress>
{/if}

{@render children()}

<style lang="less">
	@import (reference) "../lib/styles/vars.less";
	
	progress
	{
		width: 100%;
		color: @primary;
	}

</style>