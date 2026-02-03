<script lang="ts">
  import type { PageProps } from "./$types";
	
	const { data }: PageProps = $props();
	let { posts, user } = $derived(data);

	import { onMount } from 'svelte';

onMount(() => {
  const source = new EventSource(
    '/server/events/posts/all',
    { withCredentials: true }
  );

  source.addEventListener('created', async () => {
    const res = await fetch('/server/posts/all');
    posts = await res.json();
  });

  source.addEventListener('deleted', async () => {
    const res = await fetch('/server/posts/all');
    posts = await res.json();
  });

  source.addEventListener('updated', async () => {
    const res = await fetch('/server/posts/all');
    posts = await res.json();
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
			<div class="username">{username}, {date} {time}</div>

			{#if username == user.username}
				<div class="buttons">
					<button>Update</button>
					<button onclick={()=>deletePost(id)}>Delete</button>
				</div>
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