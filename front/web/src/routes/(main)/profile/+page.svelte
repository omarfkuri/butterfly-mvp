<script lang="ts">
  import PostList from "$lib/comp/PostList.svelte";
  import WritePost from "$lib/comp/WritePost.svelte";
	import type { PageProps } from "./$types";
	
	const { data }: PageProps = $props();
	const { postsPage, user } = $derived(data);

</script>

<svelte:head>
	<title>Butterfly - Profile</title>
</svelte:head>

<WritePost />

<PostList page={postsPage} {user} topic="author:{user.username}"
	path="user/{user.username}"/>

<style lang="less">
	@import (reference) "../../../lib/styles/vars.less";
</style>