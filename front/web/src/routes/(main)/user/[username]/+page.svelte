<script lang="ts">
  import PostList from "$lib/comp/PostList.svelte";
	import type { PageProps } from "./$types";
	
	const { data, params }: PageProps = $props();
	const { posts, user } = $derived(data);
	const { username } = $derived(params);

</script>

<h1>Posts by {username}</h1>

<PostList {posts} {user} topic="author:{username}"/>