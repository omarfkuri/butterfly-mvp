<script lang="ts">
	import { fade } from "svelte/transition";

  import type { LayoutProps } from "./$types";
  import { afterNavigate } from "$app/navigation";
  import Message from "$lib/comp/Message.svelte";
  import Confirm from "$lib/comp/Confirm.svelte";
  import ImageCircle from "$lib/comp/ImageCircle.svelte";
	import icon from "$lib/assets/image/brand/Icon.png"
  import userImg from "$lib/assets/image/user.png"

	const { data, children }: LayoutProps = $props();
	const { user } = $derived(data);

	async function onLogOut()
	{
		const confirmed = await confirmComp?.waitAction();

		if (!confirmed)
			return;

		const res = await fetch("/server/logout", {
			method: "POST",
		});

		if (!res.ok)
		{
			await showMessage(`Failed to log out`)
		}
		else
			location.href = ""
	}

	const showOn = 100;
	let hidden = $state(true);
	let container = $state<HTMLDivElement>();

	let confirmComp = $state<Confirm>();

	let errorComp = $state<Message>();
	let errorMsg = $state("");

	async function showMessage(content: string)
	{
		errorMsg = content;
		return errorComp?.show();
	}

	function onScroll()
	{
		if (container)
		{
			hidden = container.scrollTop <= showOn;
		}
	}

	function backToTop()
	{
		container?.scrollTo({
			top: 0,
			behavior: "smooth"
		})
	}

	afterNavigate(() => {
		if (container) {
			container.scrollTo({
				top: 0,
				behavior: "auto"
			});
		}
	});


</script>

<Message bind:this={errorComp}>
	{errorMsg}
</Message>

<Confirm bind:this={confirmComp}>
	Are you sure you want to log out?
</Confirm>

<div class="content">
	<div class="side-wrapper">
		<div class="side">
			<div class="title">
				<a href="/" data-sveltekit-preload-data="off">
					<img 
						src={icon}
						alt="Logo"
						draggable="false"
					/>
				</a>
			</div>

			<div class="buttons">
				<div class="account">
					<div class="material-icons account-button">
						person
					</div>
					<div class="account-menu-wrapper">
						<div class="account-menu">
							<a
							 	data-sveltekit-preload-data=false
								class="unset account-menu-item"
								href="/profile"
							>
								<ImageCircle
									src={user.profilePictureURL || userImg}
									{user} 
									height=".8em" 
								/>
								<span class="account-menu-item-title">{user.name}</span>
							</a>
							<button 
								class="unset account-menu-item not-dark"
								onclick={onLogOut}
							>
								<div 
									class="icon material-icons"
								>logout</div>
								<span class="account-menu-item-title">Log Out</span>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="feed" onscroll={onScroll} bind:this={container}>
		{@render children()}
	</div>

	<div class="other-wrapper">
		<div class="other">
			
		</div>
	</div>

	{#if !hidden}
		<div class="back-to-top" transition:fade={{
			duration: 160
		}}>
			<button class="dark" onclick={backToTop}>↑</button>
		</div>
	{/if}
</div>

<style lang="less">
	@import (reference) "../../lib/styles/vars.less";
	
	.content
	{
		position: relative;

		display: flex;

		height: 100%;
		max-width: 700px;
		margin-inline: auto;
	}

	.side-wrapper
	{
		.side
		{
			display: flex;
			flex-direction: column;
			gap: .5em;

			padding: .5em;

			.title
			{
				width: 1em;
				height: 1em;

				img
				{
					width: 100%;
					height: 100%;

					object-fit: cover;
				}
			}

			.buttons
			{
				display: flex;
				flex-direction: column;
				gap: .25em;

				> * {
					display: flex;
					align-items: center;
					justify-content: center;

					width: 100%;
					
					&:not(:hover)
					{
						color: @fg3;
					}
				}
			}
		}
	}

	.feed
	{
		display: flex;
		flex-direction: column;

		width: 100%;
		height: 100%;

		max-width: @smallWebWidth - 100px;
		
		.smallWeb({
			max-width: unset;
		});

		border-inline: 1px solid @bd1;

		overflow-y: scroll;
	}

	.other-wrapper
	{
		flex-grow: 1;

		.smallWeb({
			display: none;
		});

		.other
		{
			display: flex;
			flex-direction: column;

			overflow-y: scroll;
			width: 100%;
		}
	}

	.account
	{
		display: flex;
		flex-direction: column;
		justify-content: start;

		width: min-content;

		.account-button
		{
			cursor: pointer;
		}

		.account-menu-wrapper
		{
			position: relative;

			width: 100%;

			.account-menu
			{
				position: absolute;
				z-index: 9999;

				display: flex;
				flex-direction: column;

				font-size: .625em;

				background: @bg3;
				box-shadow: 0 4px 8px #999;

				.account-menu-item
				{
					display: flex;
					align-items: center;
					gap: .25em;

					height: min-content;
					padding: .5em;

					border: none;
					color: @fg1;
					font-weight: normal;

					cursor: pointer;

					&:hover
					{
						background: @bg2;
					}

					.icon
					{
						font-size: 1em;
						padding: 0;
					}

					.account-menu-item-title
					{
						padding: 0;
						white-space: nowrap;
					}
				}
			}
		}

		&:not(:hover) .account-menu
		{
			display: none;
		}
	}

	.back-to-top
	{
		position: absolute;
		inset: auto .75em .75em auto;

		font-size: .7em;

		button
		{
			display: flex;
			justify-content: center;
			align-items: center;

			width: 2em;
			height: 2em;
			padding: 0;
			font-size: 1.2em;

			border-radius: 50%;

			box-shadow: 0 0 10px #888;

			border: none;
		}
	}

</style>