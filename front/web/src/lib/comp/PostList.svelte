<script lang="ts">
  import { browser } from '$app/environment';
	import type { Post, Topic, User } from '$lib';
  import PostElem from './Post.svelte';

	interface Props
	{
		posts: Post[]
		topic: Topic
		user: User | null
		emptyString?: string
		forComments?: boolean
	}

	let {
		posts, 
		user, 
		topic, 
		emptyString = "No posts yet",
		forComments = false,

	}: Props = $props();

	if (browser)
		$effect(() => {
			const path = `/server/events/posts/${topic}`;

			const source = new EventSource(path, {
				withCredentials: true
			});

			async function getPost(id: string): Promise<Post>
			{
				const res = await fetch(`/server/posts/get/${id}`);
				return res.json();
			}

			source.addEventListener('created', async function (e)
			{
				const post = JSON.parse(e.data) as Post;
				posts = [post, ...posts];
			});

			source.addEventListener('deleted', async function (e)
			{
				const id = e.data;
				posts = posts.filter(post => post.id != id);
			});

			source.addEventListener('updated', async function (e)
			{
				const id = e.data;
				const found = posts.find(post => post.id == id);

				if (!found)
					return;

				const post = await getPost(id);

				const i = posts.findIndex(post => post.id == id);
				posts[i] = post;
			});

			return () => source.close();
		});

</script>

{#each posts as post}
	<PostElem {post} {user} isComment={forComments}/>
{:else}
	<div class="empty">
		{emptyString}
	</div>
{/each}

<style lang="less">
	@import (reference) "../styles/vars.less";

	.empty
	{
		padding: 0.5em;
		color: @fg3;
	}
</style>