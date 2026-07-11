<script lang="ts">
  import { page } from "$app/state";
	import type { LayoutProps } from "./$types";
  import Back from "$lib/comp/Back.svelte";
  import NameDisplay from "$lib/comp/NameDisplay.svelte";
  import UploadImage from "$lib/comp/UploadImage.svelte";
	import cover from "$lib/assets/image/cover.png"
  
	
	const { children, data }: LayoutProps = $props();
	const { user } = $derived(data);

	const pages = [
		["Profile", "/profile"],
		["Images", "/profile/images"],
	];

</script>

<Back title="Profile"/>

<div class="profile">
	<div class="cover">
		<img
			src={user.coverPictureURL || cover}
			alt="{user.name} - Cover"
			draggable=false
		>
	</div>
	<NameDisplay {user} fontSize="1em"/>
</div>

<div class="actions">
	<UploadImage
		openText="Change Profile Picture"
		cancelText="Cancel"
		confirmText="Select Profile Picture"
		action="/profile/?/setProfilePicture"
	/>
	<UploadImage
		openText="Change Cover Picture"
		cancelText="Cancel"
		confirmText="Select Cover Picture"
		action="/profile/?/setCoverPicture"
	/>
</div>

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