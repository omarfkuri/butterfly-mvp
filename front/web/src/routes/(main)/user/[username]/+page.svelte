<script lang="ts">
  import Back from "$lib/comp/Back.svelte";
    import Message from "$lib/comp/Message.svelte";
    import NameDisplay from "$lib/comp/NameDisplay.svelte";
  import PostList from "$lib/comp/PostList.svelte";
	import type { PageProps } from "./$types";
	
	const { data, params }: PageProps = $props();
	const {
		author,
		postsPage,
		user,
		doesFollow,
		isSelf,
		followerCount,
		followingCount,
		profile,
	} = $derived(data);
	const { username } = $derived(params);

	// svelte-ignore state_referenced_locally
	let following = $state(doesFollow);

	let errorComp = $state<Message>();
	let errorMsg = $state("");

	async function showMessage(content: string)
	{
		errorMsg = content;
		return errorComp?.show();
	}
	
	async function follow()
	{
		const res = await fetch(`/server/follow/add/${username}`, {
			method: "POST",
		});

		if (!res.ok)
			await showMessage(`Failed to follow ${username}`)
		
		else
			following = true;
	}

	async function unfollow()
	{
		const res = await fetch(`/server/follow/rem/${username}`, {
			method: "DELETE",
		});

		if (!res.ok)
			await showMessage(`Failed to unfollow ${username}`)
		
		else
			following = false;
	}

</script>

<Message bind:this={errorComp}>
	{errorMsg}
</Message>

<svelte:head>
	<title>SO - {username}</title>
</svelte:head>

<Back title={username}/>

<div class="user-profile">
	<div class="cover">
		<img
			src={profile.coverPicture || "/image/cover.png"}
			alt="{user.name} - Cover"
			draggable=false
		>
	</div>
	<NameDisplay user={author} {profile} fontSize="1em"/>
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
		
		padding-top: 4em;

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
	}

	.controls
	{
		display: flex;
		gap: .5em;

		.card();

		// .follow-btn
		// {

		// }

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