<script lang="ts">
  import { browser } from '$app/environment';
	import type { Page, Post, Topic, User } from '$lib';
  import Message from './Message.svelte';
  import PostElem from './Post.svelte';

	interface Props
	{
		page: Page<Post>
		topic: Topic
		user: User | null
		emptyString?: string
		forComments?: boolean
		path: string
	}

	let {
		page, 
		user, 
		topic, 
		emptyString = "No posts yet",
		path,
		forComments = false,

	}: Props = $props();

	let posts = $derived<Post[]>(page.content);
	let lastElement = $state<HTMLElement | null>(null);

	let errorComp = $state<Message>();
	let errorMsg = $state("");

	if (browser)
		$effect(() => {
	// 		const path = `/server/events/posts/${topic}`;

	// 		const source = new EventSource(path, {
	// 			withCredentials: true
	// 		});

	// 		async function getPost(id: string): Promise<Post>
	// 		{
	// 			const res = await fetch(`/server/posts/get/${id}`);
	// 			return res.json();
	// 		}

	// 		source.addEventListener('created', async function (e)
	// 		{
	// 			const post = JSON.parse(e.data) as Post;
	// 			posts = [post, ...posts];
	// 		});

	// 		source.addEventListener('deleted', async function (e)
	// 		{
	// 			const id = e.data;
	// 			posts = posts.filter(post => post.id != id);
	// 		});

	// 		source.addEventListener('updated', async function (e)
	// 		{
	// 			const id = e.data;
	// 			const found = posts.find(post => post.id == id);

	// 			if (!found)
	// 				return;

	// 			const post = await getPost(id);

	// 			const i = posts.findIndex(post => post.id == id);
	// 			posts[i] = post;
	// 		});

			if (lastElement)
			{
				const options = {
					root: null,
					rootMargin: '0px',
					threshold: 0.5
				};

				const observer = new IntersectionObserver(([e]) => {
					if (e?.isIntersecting)
						loadMorePosts();
				}, options);

    		observer.observe(lastElement);
    	}

	// 		return () => source.close();
		});

	async function showMessage(content: string)
	{
		errorMsg = content;
		return errorComp?.show();
	}

	async function loadMorePosts()
	{
		if (page.last)
		{
			await showMessage("No posts to show...");
			return;
		}

		const res = await fetch(`/server/posts/${path}?page=${page.number + 1}&size=10`, {
			credentials: "include"
		});

		const pg = await res.json() as Page<Post>;
		posts.push(...pg.content);

		page = pg;
	}

</script>

<Message bind:this={errorComp}>
	{errorMsg}
</Message>

{#each posts as post, i}
	{#if !page.last && i + 1 == posts.length}
		<div class="post-wrapper" bind:this={lastElement}>
			<PostElem {post} {user} isComment={forComments}/>
		</div>
	{:else}
		<div class="post-wrapper">
			<PostElem {post} {user} isComment={forComments}/>
		</div>
	{/if}
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