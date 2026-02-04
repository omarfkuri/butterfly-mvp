<script lang="ts">
  import PostList from "$lib/comp/PostList.svelte";
	import type { PageProps } from "./$types";
	
	const { data, params }: PageProps = $props();
	const { posts, user } = $derived(data);
	const { username } = $derived(params);

</script>

<div class="menu">
	<a class="link" href="/" data-sveltekit-preload-data="off">←</a>
	<span class="title">{username}</span>
</div>

<div class="user-profile">
	<div class="cover">
		<img src="/image/cover.png" alt="Cover">
	</div>
	<div class="info">
		<div class="profile-picture">
			<img src="/image/user.png" alt="User">
		</div>
		<div class="title">{username}</div>
	</div>
</div>

<PostList {posts} {user} topic="author:{username}"/>

<style lang="less">
	@import (reference) "../../../../lib/styles/vars.less";

	.menu
	{
		display: flex;
		align-items: center;
		gap: .5em;

		padding: .25em;
		font-size: .8em;

		.link
		{
			color: @fg1;
		}
	}
	
	.user-profile
	{
		.card(false);

		position: relative;

		background: linear-gradient(10deg, 
			@bg1, 
			transparent 50%
		);

		.cover
		{
			position: absolute;
			inset: 0;
			z-index: -1;

			width: 100%;
			height: 100%;

			overflow: hidden;

			img
			{
				width: 100%;
				height: 100%;

				object-fit: cover;
			}
		}

		.info
		{
			margin-top: 3em;
			width: min-content;

			.profile-picture
			{
				width: 50px;
				height: 50px;

				overflow: hidden;
				border-radius: 50%;

				img
				{
					width: 100%;
					height: 100%;

					object-fit: cover;
				}
			}

			.title
			{
				text-align: center;
			}
		}
	}

</style>