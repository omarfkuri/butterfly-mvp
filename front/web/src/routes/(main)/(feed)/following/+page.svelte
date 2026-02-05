<script lang="ts">
  import PostList from "$lib/comp/PostList.svelte";
	import type { PageProps } from "./$types";
	
	const { data }: PageProps = $props();
	const { posts, user } = $derived(data);

</script>

<svelte:head>
	<title>SO - Following</title>
</svelte:head>

<PostList {posts} {user} topic="all"/>

<style lang="less">
	@import (reference) "../../../../lib/styles/vars.less";

</style>