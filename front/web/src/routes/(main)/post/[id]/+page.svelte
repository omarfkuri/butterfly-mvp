<script lang="ts">
  import { enhance } from "$app/forms";
  import Back from "$lib/comp/Back.svelte";
  import Post from "$lib/comp/Post.svelte";
  import PostList from "$lib/comp/PostList.svelte";
  import Confirm from "$lib/comp/Confirm.svelte";
  import WriteComment from "$lib/comp/WriteComment.svelte";
	import type { PageProps, SubmitFunction } from "./$types";
	
	const { data }: PageProps = $props();
	const { post, user, comments } = $derived(data);
	
	let confirmComp = $state<Confirm>();

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

</script>

<svelte:head>
	<title>SO - {post.title}, by {post.author.username}</title>
</svelte:head>

<Back title={post.title}/>

<Post {post} {user} />

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

<WriteComment id={post.id} />

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

	.buttons
	{
		display: flex;
		justify-content: flex-end;
		align-items: center;
		gap: .5em;

		.card();
	}
</style>