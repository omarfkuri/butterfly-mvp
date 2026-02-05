<script lang="ts">
    import { goto } from '$app/navigation';
	import type { Post, User } from '$lib';
	import This from "./Post.svelte"

	interface Props
	{
		post: Post
		user: User | null
		isChild?: boolean
		isComment?: boolean
	}

	let { 
		post, 
		user, 
		isChild = false,
		isComment = false,
	}: Props = $props();

	const {
		title,
		content,
		username,
		id,
		createdAt,
		parent,
	} = $derived(post);

	const date = $derived(new Date(createdAt).toLocaleString(undefined, {
		dateStyle: "long",
	}));
	const time = $derived(new Date(createdAt).toLocaleString(undefined, {
		// timeZone: "America/Mexico_City",
		timeStyle: "short",
		hour12: false
	}));

	async function deletePost(id: string)
	{
		if (!confirm("Are you sure you want to delete this post?"))
			return;

		await fetch(`/server/posts/delete/${id}`, {
			method: "DELETE",
			credentials: "include"
		});
	}

	async function getParent(id: string)
	: Promise<Post>
	{
		const res = await fetch(`/server/posts/get/${id}`, {
			credentials: "include"
		});

		return res.json();
	}

</script>

<article
	class="post"
	class:isChild
>
	{#if !isComment && !isChild && parent != null}
		{#await getParent(parent)}
			Loading parent
		{:then post}
			<This {post} {user} isChild={true}/>
		{/await}
	{/if}

	<div 
		class="main"
		role="link"
		tabindex="0"
		onclick={() => goto(`/post/${id}`)}
		onkeydown={(e) => e.key === 'Enter' && goto(`/post/${id}`)}
	>
		<div class="title">{title}</div>
		<div class="content">{content}</div>
	</div>

	<div class="bottom">
		<div class="info">
			<a href="/user/{username}">{username}</a>
			{date} {time}
		</div>

		<div class="buttons">
			{#if user}
				{#if username == user.username}
					<button>Update</button>
					<button onclick={()=>deletePost(id)}>Delete</button>
				{/if}
			{/if}
		</div>
	</div>
</article>

<style lang="less">
	@import (reference) "../styles/vars.less";

	.post
	{
		display: flex;
		flex-direction: column;
		gap: .75em;

		width: 100%;

		.card();

		&.isChild
		{
			border: none;
			border-left: 10px solid @primary;

			background: @bg3;
		}
	}

	.main
	{
		display: flex;
		flex-direction: column;
		gap: .125em;

		cursor: pointer;

		.title
		{
			font-weight: bold;
			font-size: 1.1em;
		}

		.content
		{
			font: .65em monospace;
		}
	}

	.bottom
	{
		display: flex;
		justify-content: space-between;
		align-items: center;
		
		font-size: .8em;

		.info
		{
			font-size: .8em;
			color: @fg2;
		}

		.buttons
		{
			display: flex;
			align-items: center;
			gap: .5em;
		}
	}
</style>