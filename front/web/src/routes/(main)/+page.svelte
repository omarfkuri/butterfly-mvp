<script lang="ts">
  import type { Topic } from "$lib";
  import PostList from "$lib/comp/PostList.svelte";
  import TopicMenu from "$lib/comp/TopicMenu.svelte";
  import WritePost from "$lib/comp/WritePost.svelte";
	import type { PageProps } from "./$types";
	
	const { data }: PageProps = $props();
	const { posts, user } = $derived(data);

	let topic = $state<Topic>("all");
	const topics = $state<[string, Topic][]>([
		["Feed", "all"],
	]);

</script>

<svelte:head>
	<title>SO - Home</title>
</svelte:head>

<TopicMenu current={topic} {topics}/>

<WritePost />

<PostList {posts} {user} {topic}/>

<style lang="less">
	@import (reference) "../../lib/styles/vars.less";

</style>