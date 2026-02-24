<script lang="ts">
     import { goto } from '$app/navigation';
	import type { Post, User } from '$lib';
    import Handle from './Handle.svelte';
    import ImageCircle from './ImageCircle.svelte';
    import Message from './Message.svelte';
    import NameDisplay from './NameDisplay.svelte';
	import This from "./Post.svelte"

	interface Props
	{
		post: Post
	}

	let { 
		post, 
	}: Props = $props();

	const {
		id,
		createdAt,
	} = $derived(post);

	let {
		likeCount,
		likedByMe,
	} = $derived(post);

	let errorComp = $state<Message>();
	let errorMsg = $state("");

	async function showMessage(content: string)
	{
		errorMsg = content;
		return errorComp?.show();
	}

	async function toggleLike()
	{
		if (likedByMe)
		{
			const res = await fetch(`/server/like/rem/${id}`, {
				method: "DELETE",
				credentials: "include"
			});

			if (!res.ok)
				return showMessage(await res.text());

			likedByMe = false;
			likeCount -= 1;
		}
		else
		{
			const res = await fetch(`/server/like/add/${id}`, {
				method: "POST",
				credentials: "include"
			});

			if (!res.ok)
				return showMessage(await res.text());

			likedByMe = true;
			likeCount += 1;
		}
	}

</script>

<Message bind:this={errorComp}>
	{errorMsg}
</Message>

<label class="likes">
	<input
		type="checkbox" 
		bind:checked={likedByMe}
		onclick={e =>  {
			e.stopPropagation();
		}}
		oninput={toggleLike}
	>
	<span 
		class="like-icon material-icons"
		role="button"
		tabindex="0"
		onclick={e => e.stopPropagation()}
		onkeydown={(e) => e.key === 'Enter' && goto(`/post/${id}`)}
		>
		favorite
	</span>
	<span 
		class="like-count"
		role="button"
		tabindex="0"
		onclick={e => e.stopPropagation()}
		onkeydown={(e) => e.key === 'Enter' && goto(`/post/${id}`)}
		>
		{likeCount}
	</span>
</label>

<style lang="less">
	@import (reference) "../styles/vars.less";
	
	.likes
	{
		display: flex;
		align-items: center;
		gap: .1em;

		font-size: .8em;
		cursor: pointer;

		.no-select();

		input
		{
			display: none;

			&:checked + .like-icon,
			&:checked + .like-icon + .like-count
			{
				color: @primary;
			}

			&:not(:checked) + .like-icon,
			&:not(:checked) + .like-icon + .like-count
			{
				color: @fg3;
			}
		}

		.like-icon
		{
			font-size: 1em;
			line-height: 1;
		}

		.like-count
		{
			font-size: .8em;
			line-height: 1;
		}
	}
</style>