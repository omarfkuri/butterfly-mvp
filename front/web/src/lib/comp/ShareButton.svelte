<script lang="ts">
	import type { Post } from '$lib';
    	import Message from './Message.svelte';

	interface Props
	{
		post: Post
	}

	const { post }: Props = $props();

	let errorComp = $state<Message>();
	let errorMsg = $state("");

	async function showMessage(content: string)
	{
		errorMsg = content;
		return errorComp?.show();
	}

	async function copyLink() {
		try {
			await navigator.clipboard.writeText(location.href);
			showMessage("Copied to clipboard");
		} catch (err) {
			showMessage("Failed to copy");
		}
	}

</script>

<Message bind:this={errorComp}>
	{errorMsg}
</Message>

<div class="likes">
	<div class="dropup">
		<div class="dropup-title">
			Share
		</div>
		<div class="dropup-menu">
			<button class="unset dropup-menu-item" onclick={copyLink}>
				<span class="icon material-icons">content_copy</span>
				<span class="dropup-menu-item-title">Copy Link</span>
			</button>
		</div>
	</div>
	<div class="like-icon material-icons">
		arrow_outward
	</div>
</div>

<style lang="less">
	@import (reference) "../styles/vars.less";
	
	.likes
	{
		position: relative;

		display: flex;
		align-items: center;
		justify-content: center;

		gap: .1em;

		font-size: .8em;

		.no-select();

		.like-icon
		{
			font-size: 1em;
			line-height: 1;

			cursor: pointer;
		}

		.dropup
		{
			position: absolute;
			bottom: 1em;

			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			gap: .5em;

			padding: .25em .75em;

			background-color: @bg3;
			border: 1px solid @bd1;

			.dropup-title
			{
				font-weight: bold;
			}

			.dropup-menu
			{
				display: flex;
				flex-direction: column;

				.dropup-menu-item
				{
					display: flex;
					align-items: center;
					justify-content: center;
					gap: .25em;

					.icon
					{
						font-size: .8em;
						line-height: 0;
					}

					.dropup-menu-item-title
					{

					}
				}

				button
				{
					padding: .25em;

					font-size: .8em;

					white-space: nowrap;
					cursor: pointer;
				}
			}
		}

		&:not(:hover)
		{
			.dropup
			{
				display: none;
			}
		}

		&:hover
		{
			input
			{
				&:checked + .like-icon
				{
					color: @primary;
				}

				&:not(:checked) + .like-icon
				{
					color: @fg3;
				}
			}
		}
	}


</style>