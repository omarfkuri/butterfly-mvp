<script lang="ts">
  import { page } from "$app/state";
	import type { LayoutProps } from "./$types";
  import { enhance } from "$app/forms";
  import Back from "$lib/comp/Back.svelte";
  import NameDisplay from "$lib/comp/NameDisplay.svelte";
	
	const { children, data }: LayoutProps = $props();
	const { user } = $derived(data);

	const pages = [
		["Profile", "/profile"],
		["Images", "/profile/images"],
	];

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

<div class="menu">
	{#each pages as [title, path]}
		{@const current = path == page.url.pathname}

		<a
			data-sveltekit-preload-data=false
			href={path} class:current class="unset">{title}</a>
	{/each}
</div>

{@render children()}

<style lang="less">
	@import (reference) "../../../lib/styles/vars.less";

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

	.menu
	{
		display: flex;
		align-items: center;
		gap: .5em;

		.card(false);

		font-size: .8em;

		a
		{
			padding-block: .25em;
			padding-inline: .4em;

			cursor: pointer;

			color: @fg2;
			border-bottom: 2px solid transparent;

			&.current
			{
				border-color: @primary;
			}
		}
	}
</style>