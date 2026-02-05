<script lang="ts">
  import { page } from "$app/state";
	import type { LayoutProps } from "./$types";
	
	const { children }: LayoutProps = $props();

	const pages = [
		["Home", "/"],
		["Following", "/following"],
	];

</script>

<div class="menu">
	{#each pages as [title, path]}
		{@const current = path == page.url.pathname}

		<a href={path} class:current>{title}</a>
	{/each}
</div>

{@render children()}

<style lang="less">
	@import (reference) "../../../lib/styles/vars.less";

	.menu
	{
		display: flex;
		align-items: center;
		gap: .5em;

		.card(false);

		font-size: .8em;
	}

	a
	{
		font-size: 1em;

		padding-block: .25em;
		padding-inline: .4em;

		cursor: pointer;

		color: @fg2;
		border-bottom: 2px solid transparent;

		&.current
		{
			border-color: @primary;
		}
	}
</style>