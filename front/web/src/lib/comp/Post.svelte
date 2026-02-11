<script lang="ts">
    import { goto } from '$app/navigation';
	import type { Post, User } from '$lib';
    import Message from './Message.svelte';
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
		parentId,
	} = $derived(post);

	let {
		likeCount,
		likedByMe,
	} = $derived(post);

	const date = $derived(new Date(createdAt).toLocaleString(undefined, {
		dateStyle: "long",
	}));
	const time = $derived(new Date(createdAt).toLocaleString(undefined, {
		// timeZone: "America/Mexico_City",
		timeStyle: "short",
		hour12: false
	}));

	let errorComp = $state<Message>();
	let errorMsg = $state("");

	async function showMessage(content: string)
	{
		errorMsg = content;
		return errorComp?.show();
	}

	async function getParent(id: string)
	: Promise<Post>
	{
		const res = await fetch(`/server/posts/get/${id}`, {
			credentials: "include"
		});

		return res.json();
	}

	async function toggleLike()
	{
		if (likedByMe)
		{
			const res = await fetch(`/server/like/rem/${id}`, {
				method: "DELETE",
				credentials: "include"
			});

			if (!res.ok)
				return showMessage(await res.text());

			likedByMe = false;
			likeCount -= 1;
		}
		else
		{
			const res = await fetch(`/server/like/add/${id}`, {
				method: "POST",
				credentials: "include"
			});

			if (!res.ok)
				return showMessage(await res.text());

			likedByMe = true;
			likeCount += 1;
		}
	}

</script>

<Message bind:this={errorComp}>
	{errorMsg}
</Message>

<article
	class="post"
	class:isChild
>
	{#if !isComment && !isChild && parentId != null}
		{#await getParent(`${parentId}`)}
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
		<div class="top">
			<span class="title">{title}</span>
			<span class="content">{content}</span>
		</div>

		<div class="bottom">

			<div class="top-bottom">
				<span class="author" data-sveltekit-preload-data="off">
					By
					<a href="/user/{username}">
						{#if user && username == user?.username}
							me
						{:else}
							{username}
						{/if}
					</a>
				</span>
				<label class="likes">
					<input
						type="checkbox" 
						bind:checked={likedByMe}
						onclick={e =>  {
							e.stopPropagation();
						}}
						oninput={toggleLike}
					>
					<span 
						class="like-icon material-icons"
						role="button"
						tabindex="0"
						onclick={e => e.stopPropagation()}
						onkeydown={(e) => e.key === 'Enter' && goto(`/post/${id}`)}
						>
						favorite
					</span>
					<span 
						class="like-count"
						role="button"
						tabindex="0"
						onclick={e => e.stopPropagation()}
						onkeydown={(e) => e.key === 'Enter' && goto(`/post/${id}`)}
						>
						{likeCount}
					</span>
				</label>
			</div>
			<div class="info">
				<div class="date">{date} {time}</div>	
			</div>
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

		cursor: pointer;
	}

	.top
	{
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		gap: .125em;

		.title
		{
			font-weight: bold;
			font-size: 1.1em;

			word-break: break-all;
		}

		.content
		{
			font: .65em monospace;
			padding-bottom: .5em;

			word-break: break-all;
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
			display: flex;
			justify-content: end;
			gap: .5em;

			width: 100%;
			text-align: left;
			font-size: .8em;
			color: @fg2;

			.date
			{
				color: @fg3;
			}
		}
	}

	.top-bottom
	{
		display: flex;
		align-items: center;
		gap: .5em;

		.author
		{
			font-size: .7em;
			white-space: nowrap;
		}

		.likes
		{
			display: flex;
			align-items: center;
			gap: .1em;

			font-size: .8em;
			cursor: pointer;

			.no-select();

			input
			{
				display: none;

				&:checked + .like-icon,
				&:checked + .like-icon + .like-count
				{
					color: @primary;
				}

				&:not(:checked) + .like-icon,
				&:not(:checked) + .like-icon + .like-count
				{
					color: @fg3;
				}
			}

			.like-icon
			{
				font-size: 1em;
				line-height: 1;
			}

			.like-count
			{
				font-size: .8em;
				line-height: 1;
			}
		}
	}
</style>