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

<div class="image" style:--size={height}>
	<img
		{src}
		alt="{user.name} - Profile Picture"
		draggable={false}
	>
</div>

<style lang="less">
	@import (reference) "../styles/vars.less";

	@size: var(--size);

	.image
	{
		width: @size;
		height: @size;

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