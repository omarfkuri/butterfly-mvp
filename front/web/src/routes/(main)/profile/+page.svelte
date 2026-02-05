<script lang="ts">
  import PostList from "$lib/comp/PostList.svelte";
  import WritePost from "$lib/comp/WritePost.svelte";
	import type { PageProps } from "./$types";
	
	const { data }: PageProps = $props();
	const { posts, user } = $derived(data);

</script>

<svelte:head>
	<title>SO - Profile</title>
</svelte:head>

<div class="menu">
	<a class="link" href="/" data-sveltekit-preload-data="off">←</a>
	<span class="title">Profile</span>
</div>

<div class="profile">
	<div class="cover">
		<img
			src="/image/cover.png" 
			alt="Cover"
			draggable={false}
		>
	</div>
	<div class="info">
		<div class="profile-picture">
			<img
				src="/image/user.png" 
				alt="User"
				draggable={false}
			>
		</div>
		<div class="title">{user.username}</div>
	</div>
</div>

<WritePost />

<PostList {posts} {user} topic="author:{user.username}"/>

<style lang="less">
	@import (reference) "../../../lib/styles/vars.less";

	.menu
	{
		display: flex;
		align-items: center;
		gap: .5em;

		.card(false);

		font-size: .8em;

		.link
		{
			color: @fg1;
		}
	}
	
	.profile
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