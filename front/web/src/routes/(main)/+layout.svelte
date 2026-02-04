<script lang="ts">
  import WritePost from "$lib/comp/WritePost.svelte";
  import type { LayoutProps } from "./$types";

	const { data, children }: LayoutProps = $props();
	const { user } = $derived(data);

	let writePost = $state<WritePost>();

	function createPost()
	{
		writePost?.openModal();
	}

	async function onLogOut()
	{
		if (!confirm("Do you want to log out?"))
			return;

		const res = await fetch("/server/logout", {
			method: "POST",
		});

		if (!res.ok)
		{
			alert(`Failed to log out`)
		}
		else
			location.href = ""
	}

</script>
<div class="content">
	<div class="side">
		<a href="/">SO</a>

		<div class="buttons">
			<button onclick={onLogOut}>Log out</button>
			<button onclick={createPost}>New Post</button>
		</div>
	</div>

	<div class="feed">
		{@render children()}
	</div>

	<div class="other">
		<a href="/profile">{user.username}</a>
	</div>
</div>

<WritePost bind:this={writePost} />

<style lang="less">
	
	.content
	{
		display: flex;

		height: 100%;

		max-width: 700px;
		margin-inline: auto;

		* {
			padding-block: .25em;
			padding-inline: .5em;
		}
	}

	.side
	{
		display: flex;
		flex-direction: column;

		.buttons
		{
			display: flex;
			flex-direction: column;
			gap: .25em;
		}
	}

	.feed
	{
		display: flex;
		flex-direction: column;

		width: 100%;
		height: 100%;

		max-width: 450px;

		border-inline: 1px solid #AAA;

		overflow-y: scroll;
	}

	.other
	{
		display: flex;
		flex-direction: column;

		flex-grow: 1;
		overflow-y: scroll;
	}

</style>