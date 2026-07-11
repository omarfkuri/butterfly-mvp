<script lang="ts">
  import { enhance } from "$app/forms";
  import Back from "$lib/comp/Back.svelte";
  import PostView from "$lib/comp/Post.svelte";
  import PostList from "$lib/comp/PostList.svelte";
  import Confirm from "$lib/comp/Confirm.svelte";
  import WriteComment from "$lib/comp/WriteComment.svelte";
	import type { PageProps, SubmitFunction } from "./$types";
  import ImageCircle from "$lib/comp/ImageCircle.svelte";
  import Handle from "$lib/comp/Handle.svelte";
  import type { Post } from "$lib";
  import LikeButton from "$lib/comp/LikeButton.svelte";
  import ShareButton from "$lib/comp/ShareButton.svelte";
  import Loading from "$lib/comp/Loading.svelte";
  import userImg from "$lib/assets/image/user.png"

	
	const { data }: PageProps = $props();
	const { post, user, comments } = $derived(data);
	
	let confirmComp = $state<Confirm>();

	function onReply()
	{
		document
		.querySelector<HTMLInputElement>("#title-input")
		?.focus();
	}

	export const onDelete: SubmitFunction = async function(e)
	{
		const confirmed = await confirmComp?.waitAction();

    if (confirmed)
    {
    	location.href = "/";
      return;
    }
    else
    {
      e.cancel();
    }
	}
	const date = $derived(new Date(post.createdAt).toLocaleString(undefined, {
		dateStyle: "long",
	}));
	const time = $derived(new Date(post.createdAt).toLocaleString(undefined, {
		// timeZone: "America/Mexico_City",
		timeStyle: "short",
		hour12: false
	}));

	async function getParent(id: string)
	: Promise<Post>
	{
		const res = await fetch(`/server/posts/get/${id}`, {
			credentials: "include"
		});

		return res.json();
	}

</script>

<svelte:head>
	<title>Butterfly - {post.title}, by {post.author.username}</title>
</svelte:head>

<Back title={post.title}/>

<article
	class="post"
>
	{#if post.parentId != null}
		{#await getParent(`${post.parentId}`)}
			<Loading text="parent"/>
		{:then parent}
			<PostView post={parent} {user} isChild={true}/>
		{/await}
	{/if}

	<div class="content">
		<div class="content-top">
			<span class="title">{post.title}</span>
			<span class="post-content">{post.content}</span>
		</div>

		<div class="content-bottom">
			<div class="author">
			<ImageCircle
				src={post.author.profilePictureURL || userImg}
				user={post.author}
				height=1em
			/>
				<a class="unset name"
					data-sveltekit-preload-data=false
					href="/user/{post.author.username}">
					<b>{post.author.name}</b>
				</a>
				<Handle username={post.author.username}/>
			</div>

			<div class="info">
				<div class="date">{date} {time}</div>	
			</div>
		</div>
	</div>

	<div class="bottom">
		<LikeButton {post} />		

		<button class="unset icon-button" onclick={onReply}>
			<div class="icon material-icons">
				chat_bubble
			</div>
		</button>

		<ShareButton {post} />
	</div>
</article>

{#if user}
	{#if post.author.username == user.username}
		<div class="buttons">
			<form method="POST" action="?/updatePost" use:enhance>
				<button>Update</button>
			</form>
			<form method="POST" action="?/deletePost" 
				use:enhance={onDelete}
			>
				<button>Delete</button>
			</form>
		</div>
	{/if}
{/if}

<div class="com-title">
	<h4>Replies</h4>
</div>

<WriteComment id={post.id}/>

<PostList page={comments} {user} topic="all" forComments
	emptyString="No replies"
	path="comments/all/{post.id}"
/>

<Confirm bind:this={confirmComp}>
	<div class="delete-msg">
		Delete "<b>{post.title}</b>" forever?
	</div>
</Confirm>

<style lang="less">
	@import (reference) "../../../../lib/styles/vars.less";
	
	.com-title
	{
		.card();
	}

	.delete-msg
	{
		width: 100%;
		word-break: break-all;
	}

	.post
	{
		.card();
		
		display: flex;
		flex-direction: column;
		gap: 1em;

		.content
		{
			display: flex;
			flex-direction: column;
			gap: 2em;

			width: 100%;

			.content-top
			{
				display: flex;
				flex-direction: column;

				width: 100%;
				font-size: 1.6em;

				.title
				{
					font-weight: bold;
				}

				.post-content
				{
					font-size: .8em;
				}
			}

			.content-bottom
			{
				display: flex;
				flex-direction: column;

				.author
				{
					display: flex;
					align-items: center;
					gap: .25em;

					// .name
					// {

					// }
				}

				.info
				{
					font-size: .7em;
					color: @fg3;
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
	}

	.buttons
	{
		display: flex;
		justify-content: flex-end;
		align-items: center;
		gap: .5em;

		.card();
	}
</style>