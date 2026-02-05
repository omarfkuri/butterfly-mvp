<script lang="ts">
	
	interface Props
	{
		title: string
	}

	const { title }: Props = $props();

</script>

<div class="menu">
	<button
		class="default"
		onclick={() => history.back()}
	>←</button>
	<span class="title">{title}</span>
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

	button
	{
		padding: 0;
		margin: 0;
		
		font-size: .9em;
		line-height: 1;

		cursor: pointer;

		background: none;
		border: none;
	}

	.title
	{
		font-weight: bold;
		line-height: 1;
	}
</style>