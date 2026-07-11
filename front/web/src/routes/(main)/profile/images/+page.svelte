<script lang="ts">
	import type { PageProps } from "./$types";
	
	const { data }: PageProps = $props();
	const { imagesPage } = $derived(data);

</script>

<svelte:head>
	<title>Butterfly - Profile</title>
</svelte:head>

<div class="list">
	{#each imagesPage.content as image}
		<div class="item">
			<img
				src={image.url}
				alt="User"
				draggable="false"
			>
		</div>
	{:else}
		No images available
	{/each}
</div>

<style lang="less">
	@import (reference) "../../../../lib/styles/vars.less";

	.list
	{
		display: flex;
		flex-direction: column;
	}

	.item
	{
		.card();

		img
		{
			width: 100%;

			border: 1px solid @bd1;
		}
	}
</style>