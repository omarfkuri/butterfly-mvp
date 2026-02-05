<script lang="ts">
  import type { Topic } from "$lib";
	
	interface Props
	{
		current: Topic
		topics: [string, Topic][]
	}

	const { current, topics }: Props = $props();

</script>

<div class="menu">
	{#each topics as [title, topic]}
		{@const isCurrent = topic == current}

		<button class="menu-item default" class:isCurrent>
			{title}
		</button>
	{/each}
</div>

<style lang="less">
	@import (reference) "../styles/vars.less";
	
	.menu
	{
		display: flex;
		align-items: center;
		gap: .5em;

		.card(false);

		font-size: .8em;
	}

	.menu-item
	{
		margin: 0;
		padding: 0;
		border: none;
		background: none;

		font-size: 1em;

		padding-block: .25em;
		padding-inline: .4em;

		cursor: pointer;

		&.isCurrent
		{
			border-bottom: 2px solid @primary;
		}
	}
</style>