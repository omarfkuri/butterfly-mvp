<script lang="ts">
    import { goto } from '$app/navigation';
	import type { Post, User } from '$lib';
    import Handle from './Handle.svelte';
    import ImageCircle from './ImageCircle.svelte';
    import LikeButton from './LikeButton.svelte';
    import Message from './Message.svelte';
    import NameDisplay from './NameDisplay.svelte';
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
		author,
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
			<div class="side">
				<ImageCircle
					src={author.profilePictureURL || "/image/user.png"}
					user={author}
					height=1.5em
				/>
			</div>
			<div class="content">
				<div class="content-top">
					<div class="author">
						<a class="unset name"
							data-sveltekit-preload-data=false
							href="/user/{author.username}">
							<b>{author.name}</b>
						</a>
						<Handle username={author.username}/>
					</div>

					<div class="info">
						<div class="date">{date} {time}</div>	
					</div>
				</div>

				<div class="content-bottom">
					<span class="title">{title}</span>
					<span class="content">{content}</span>
				</div>
			</div>
		</div>

		<div class="bottom">
			<LikeButton {post} />

			<div class="icon-button">
				<div class="icon material-icons">
					chat_bubble
				</div>
			</div>

			<div class="icon-button">
				<div class="icon material-icons">
					visibility
				</div>
			</div>

			<div class="icon-button">
				<div class="icon material-icons">
					arrow_outward
				</div>
			</div>
		</div>
	</div>
</article>

<style lang="less">
	@import (reference) "../styles/vars.less";

	.post
	{
		.card();
		cursor: pointer;
	}

	.main
	{
		display: flex;
		flex-direction: column;
		gap: .75em;
	}

	.top
	{
		display: flex;

		> * {
			padding: .25em;
		}

		.content
		{
			display: flex;
			flex-direction: column;
			gap: .5em;

			width: 100%;

			.content-top
			{
				display: flex;
				flex-direction: column;

				.author
				{
					display: flex;
					align-items: center;
					gap: .25em;

					.name
					{

					}
				}

				.info
				{
					font-size: .7em;
					color: @fg3;
				}
			}

			.content-bottom
			{
				display: flex;
				flex-direction: column;

				width: 100%;

				.title
				{
					font-weight: bold;
				}

				.post-content
				{
					font-size: .9em;
				}
			}
		}
	}

	.bottom
	{
		display: flex;
		justify-content: space-around;
		gap: .5em;

		padding-block: .5em;
	}

	.icon-button
	{
		color: @fg3;
		
		font-size: .8em;
		line-height: 0;
		
		cursor: pointer;

		.icon
		{
			font-size: 1em;
		}
	}
</style>