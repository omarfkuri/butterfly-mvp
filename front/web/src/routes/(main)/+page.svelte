<script lang="ts">
  import PostList from "$lib/comp/PostList.svelte";
	import type { PageProps } from "./$types";
	
	const { data }: PageProps = $props();
	const { posts, user } = $derived(data);

</script>

<h1>Feed</h1>

<PostList {posts} {user} topic="all"/>