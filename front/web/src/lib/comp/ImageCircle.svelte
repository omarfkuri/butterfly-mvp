<script lang="ts">
  import type { User } from "$lib";
	
	interface Props
	{
		src: string
		height: string
		user: User
	}

	const { user, src, height } : Props = $props();

</script>

<div class="image" style:width={height} style:height={height}>
	<img
		{src}
		alt="{user.name} - Profile Picture"
		draggable={false}
	>
</div>

<style lang="less">
	@import (reference) "../styles/vars.less";

	.image
	{
		aspect-ratio: 1 / 1;

		overflow: hidden;
		border-radius: 50%;

		img
		{
			width: 100%;
			height: 100%;

			object-fit: cover;
		}
	}
	
</style>