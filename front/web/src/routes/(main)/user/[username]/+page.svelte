<script lang="ts">
  import PostList from "$lib/comp/PostList.svelte";
	import type { PageProps } from "./$types";
	
	const { data, params }: PageProps = $props();
	const { posts, user } = $derived(data);
	const { username } = $derived(params);

</script>

<div class="user-profile">
	<h2>{username}</h2>
</div>

<PostList {posts} {user} topic="author:{username}"/>

<style lang="less">
	@import (reference) "../../../../lib/styles/vars.less";
	
	.user-profile
	{
		.card();
	}

</style>