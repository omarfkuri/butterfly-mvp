<script lang="ts">
    import { goto } from '$app/navigation';
	import type { Post, User } from '$lib';
    import Handle from './Handle.svelte';
    import ImageCircle from './ImageCircle.svelte';
    import LikeButton from './LikeButton.svelte';
    import Loading from './Loading.svelte';
    import Message from './Message.svelte';
	import This from "./Post.svelte"
    import ShareButton from './ShareButton.svelte';
  	import userImg from "$lib/assets/image/user.png"

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

</script>

<Message bind:this={errorComp}>
	{errorMsg}
</Message>

<article
	class="post"
	class:isChild
>
	<div 
		class="main"
		role="link"
		tabindex="0"
		onclick={() => goto(`/post/${id}`)}
		onkeydown={(e) => e.key === 'Enter' && goto(`/post/${id}`)}
	>
		{#if !isChild}
			<div class="side">
				<ImageCircle
					src={author.profilePictureURL || userImg}
					user={author}
					height=1.5em
				/>
			</div>
		{/if}

		<div class="content">
			<div class="content-top">
				<div class="author">
					{#if isChild}
						<ImageCircle
							src={author.profilePictureURL || "/image/user.png"}
							user={author}
							height=1em
						/>
					{/if}

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

			<div class="parent">
				{#if !isComment && !isChild && parentId != null}
				{#await getParent(`${parentId}`)}
					<Loading text="parent"/>
				{:then post}
					<This {post} {user} isChild={true}/>
				{/await}
			{/if}
			</div>

			<div class="content-bottom">
				<span class="title">{title}</span>
				<span class="post-content">{content}</span>
			</div>
		</div>
	</div>

	{#if !isChild}
		<div class="bottom">
			<LikeButton {post} />

			<div class="icon-button">
				<div class="icon material-icons">
					chat_bubble
				</div>
			</div>

			<a 
				data-sveltekit-preload-data=false
				class="unset icon-button" href="/post/{id}">
				<div class="icon material-icons">
					visibility
				</div>
			</a>

			<ShareButton {post}/>
		</div>
	{/if}
</article>

<style lang="less">
	@import (reference) "../styles/vars.less";

	.post
	{
		.card();
		
		display: flex;
		flex-direction: column;

		gap: 1em;

		&.isChild
		{
			border: 1px solid @bd1;
		}
	}

	.main
	{
		display: flex;
		gap: .75em;

		cursor: pointer;

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

			.parent
			{
				margin-left: -.5em;
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