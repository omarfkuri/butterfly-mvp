<script lang="ts">
  import Back from "$lib/comp/Back.svelte";
  import Post from "$lib/comp/Post.svelte";
  import PostList from "$lib/comp/PostList.svelte";
  import WriteComment from "$lib/comp/WriteComment.svelte";
	import type { PageProps } from "./$types";
	
	const { data }: PageProps = $props();
	const { post, user, comments } = $derived(data);

</script>

<svelte:head>
	<title>SO - Post</title>
</svelte:head>

<Back title={post.title}/>

<Post {post} {user} current/>

<div class="com-title">
	<h4>Comments</h4>
</div>

<WriteComment id={post.id} />

<PostList posts={comments} {user} topic="all" forComments
	emptyString="No comments"
/>

<style lang="less">
	@import (reference) "../../../../lib/styles/vars.less";
	
	.com-title
	{
		.card();
	}
</style>