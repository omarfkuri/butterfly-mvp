<script lang="ts">
	import type { Post, User } from '$lib';

	type Topic = (
		|"all"
		|`author:${string}`
	);

	interface Props
	{
		posts: Post[]
		topic: Topic
		user: User | null
	}

	let { posts, user, topic }: Props = $props();

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

	async function deletePost(id: string)
	{
		const res = await fetch(`/server/posts/delete/${id}`, {
			method: "DELETE",
			credentials: "include"
		});
	}

</script>

{#each posts as { title, content, username, id, createdAt }}
	{@const date = new Date(createdAt).toLocaleString(undefined, {
		dateStyle: "long",
	})}
	{@const time = new Date(createdAt).toLocaleString(undefined, {
		// timeZone: "America/Mexico_City",
		timeStyle: "short",
		hour12: false
	})}
	<article class="post">
		<div class="main">
			<div class="title">{title}</div>
			<div class="content">{content}</div>
		</div>

		<div class="info">
			<div class="username">
				<a href="/user/{username}">{username}</a>
				{date} {time}
			</div>

			{#if user}
				{#if username == user.username}
					<div class="buttons">
						<button>Update</button>
						<button onclick={()=>deletePost(id)}>Delete</button>
					</div>
				{/if}
			{/if}
		</div>
	</article>
{:else}
	No posts yet
{/each}

<style lang="less">
	.post
	{
		display: flex;
		flex-direction: column;
		gap: .25em;

		padding-block: .5em;
		border-bottom: 1px solid #AAA;
	}

	.main
	{
		display: flex;
		flex-direction: column;
		gap: .1em;

		.title
		{
			font-weight: bold;
			font-size: 1.1em;
		}

		.content
		{
			font: 1em monospace;
		}
	}

	.info
	{
		display: flex;
		justify-content: space-between;

		.username
		{
			font-size: .8em;
		}
	}
</style>