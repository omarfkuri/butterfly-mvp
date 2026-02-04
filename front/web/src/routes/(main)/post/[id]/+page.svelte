<script lang="ts">
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

<div class="menu">
	<a class="link" href="/" data-sveltekit-preload-data="off">←</a>
	<span class="title">{post.title}</span>
</div>

<Post {post} {user} current/>

<WriteComment id={post.id} />

<PostList posts={comments} {user} topic="all" forComments
	emptyString="No comments"
/>

<style lang="less">
	@import (reference) "../../../../lib/styles/vars.less";
	
</style>