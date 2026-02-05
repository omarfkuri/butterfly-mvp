<script lang="ts">
  import Back from "$lib/comp/Back.svelte";
  import Post from "$lib/comp/Post.svelte";
  import PostList from "$lib/comp/PostList.svelte";
  import WriteComment from "$lib/comp/WriteComment.svelte";
	import type { PageProps } from "./$types";
	
	const { data }: PageProps = $props();
	const { post, user, comments } = $derived(data);

	async function deletePost()
	{
		if (!confirm("Are you sure you want to delete this post?"))
			return;

		await fetch(`/server/posts/delete/${post.id}`, {
			method: "DELETE",
			credentials: "include"
		});
	}

</script>

<svelte:head>
	<title>SO - Post</title>
</svelte:head>

<Back title={post.title}/>

<Post {post} {user} />


<div class="buttons">
	{#if user}
		{#if post.username == user.username}
			<button>Update</button>
			<button onclick={()=>deletePost()}>Delete</button>
		{/if}
	{/if}
</div>

<div class="com-title">
	<h4>Replies</h4>
</div>

<WriteComment id={post.id} />

<PostList posts={comments} {user} topic="all" forComments
	emptyString="No replies"
/>

<style lang="less">
	@import (reference) "../../../../lib/styles/vars.less";
	
	.com-title
	{
		.card();
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