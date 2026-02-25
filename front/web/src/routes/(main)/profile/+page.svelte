<script lang="ts">
  import { enhance } from "$app/forms";
  import Back from "$lib/comp/Back.svelte";
  import NameDisplay from "$lib/comp/NameDisplay.svelte";
  import PostList from "$lib/comp/PostList.svelte";
  import WritePost from "$lib/comp/WritePost.svelte";
	import type { PageProps } from "./$types";
	
	const { data, form }: PageProps = $props();
	const { postsPage, user } = $derived(data);

	let profileImgInput = $state<HTMLInputElement>()
	let coverImgInput = $state<HTMLInputElement>()

	let profileImgDialog = $state<HTMLDialogElement>()
	let coverImgDialog = $state<HTMLDialogElement>()

	function onProfileChoose()
	{
		profileImgDialog?.showModal();
	}

	function onCoverChoose()
	{
		coverImgDialog?.showModal();
	}

	function onProfileSelect()
	{
		profileImgInput?.click();
	}

	function onCoverSelect()
	{
		coverImgInput?.click();
	}

</script>

<svelte:head>
	<title>SO - Profile</title>
</svelte:head>

<Back title="Profile"/>

<div class="profile">
	<div class="cover">
		<img
			src={user.coverPictureURL || "/image/cover.png"}
			alt="{user.name} - Cover"
			draggable=false
		>
	</div>
	<NameDisplay {user} fontSize="1em"/>
</div>

<div class="actions">
		<button onclick={onProfileChoose}>Change Profile Picture</button>
		<button onclick={onCoverChoose}>Change Cover Picture</button>
</div>

<dialog bind:this={profileImgDialog}>

	{#if form}
		<div class="error">
			{form}
		</div>
	{/if}
	
	<form
		method="POST"
		action="?/setProfilePicture"
		enctype="multipart/form-data"
		use:enhance
	>
		<button onclick={onProfileSelect}>Upload File</button>
		<input
			hidden
			required
			type="file"
			name="file"
			bind:this={profileImgInput}
		/>
		<button>Set Profile Picture</button>
	</form>
</dialog>

<dialog bind:this={coverImgDialog}>

	{#if form}
		<div class="error">
			{form}
		</div>
	{/if}

	<form
		method="POST"
		action="?/setCoverPicture"
		enctype="multipart/form-data"
		use:enhance
	>
		<button onclick={onCoverSelect}>Upload File</button>
		<input
			hidden
			required
			type="file"
			name="file"
			bind:this={coverImgInput}
		/>
		<button>Set Cover Picture</button>
	</form>
</dialog>

<WritePost />

<PostList page={postsPage} {user} topic="author:{user.username}"
	path="user/{user.username}"/>

<style lang="less">
	@import (reference) "../../../lib/styles/vars.less";
	
	.hide
	{
		display: none;
	}

	.profile
	{
		.card(false);

		position: relative;

		padding-top: 4em;

		background: linear-gradient(10deg, 
			@bg1, 
			transparent 50%
		);

		.cover
		{
			position: absolute;
			inset: 0;
			z-index: -1;

			width: 100%;
			height: 100%;

			overflow: hidden;

			img
			{
				width: 100%;
				height: 100%;

				object-fit: cover;
			}
		}
	}

	.actions
	{
		.card();
	}

</style>