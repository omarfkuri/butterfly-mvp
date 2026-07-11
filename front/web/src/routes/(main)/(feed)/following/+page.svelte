<script lang="ts">
  import PostList from "$lib/comp/PostList.svelte";
	import type { PageProps } from "./$types";
	
	const { data }: PageProps = $props();
	const { postsPage, user } = $derived(data);

</script>

<svelte:head>
	<title>Butterfly - Following</title>
</svelte:head>

<PostList page={postsPage} {user} topic="all" path="following" 
	emptyString="No one you follow has posted anything"/>

<style lang="less">
	@import (reference) "../../../../lib/styles/vars.less";

</style>