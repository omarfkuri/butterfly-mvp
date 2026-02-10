<script lang="ts">
  import Back from "$lib/comp/Back.svelte";
  import PostList from "$lib/comp/PostList.svelte";
	import type { PageProps } from "./$types";
	
	const { data, params }: PageProps = $props();
	const {
		postsPage,
		user,
		doesFollow,
		isSelf,
		followerCount,
		followingCount,
	} = $derived(data);
	const { username } = $derived(params);

	// svelte-ignore state_referenced_locally
	let following = $state(doesFollow);
	
	async function follow()
	{
		const res = await fetch(`/server/follow/add/${username}`, {
			method: "POST",
		});

		if (!res.ok)
			alert(`Failed to follow ${username}`)
		
		else
			following = true;
	}

	async function unfollow()
	{
		const res = await fetch(`/server/follow/rem/${username}`, {
			method: "DELETE",
		});

		if (!res.ok)
			alert(`Failed to unfollow ${username}`)
		
		else
			following = false;
	}

</script>

<svelte:head>
	<title>SO - {username}</title>
</svelte:head>

<Back title={username}/>

<div class="user-profile">
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
		<div class="bottom">
			<div class="title">{username}</div>
		</div>
	</div>
</div>

<div class="controls">
	<div class="followers">
		<span class="count">{followerCount}</span>
		<span class="title">Followers</span>
	</div>
	<div class="following">
		<span class="count">{followingCount}</span>
		<span class="title">Following</span>
	</div>
	{#if !isSelf}
		<div class="follow-btn">
			{#if following}
				<button onclick={unfollow}>Unfollow</button>
			{:else}
				<button onclick={follow}>Follow</button>
			{/if}
		</div>
	{/if}
</div>

<PostList page={postsPage} {user} topic="author:{username}"
	path="user/{username}"/>

<style lang="less">
	@import (reference) "../../../../lib/styles/vars.less";
	
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
			display: flex;
			flex-direction: column;
			align-items: center;

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

			.bottom
			{
				display: flex;

				.title
				{
					text-align: center;
				}
			}
		}
	}

	.controls
	{
		display: flex;
		gap: .5em;

		.card();

		.follow-btn
		{

		}

		.followers, .following
		{
			display: flex;
			flex-direction: column;
			align-items: center;

			font-size: .8em;

			.count
			{
				font-weight: bold;
			}

			.title
			{
				font-size: .5em;
			}
		}
	}

</style>