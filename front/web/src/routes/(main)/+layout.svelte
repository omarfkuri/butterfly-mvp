<script lang="ts">
  import type { LayoutProps } from "./$types";

	const { data, children }: LayoutProps = $props();
	const { user } = $derived(data);

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
	<div class="side-wrapper">
		<div class="side">
			<a href="/" data-sveltekit-preload-data="off">SO</a>

			<div class="buttons">
				<div class="account">
					<div class="material-icons account-button">
						person
					</div>
					<div class="account-menu" data-sveltekit-preload-data="off">
						<button 
							class="account-menu-item not-dark"
							onclick={onLogOut}
						>
							<div 
								class="icon material-icons"
							>logout</div>
							<span class="account-menu-item-title">Log Out</span>
						</button>
						<a
							class="account-menu-item"
							href="/user/{user.username}"
						>
							<div 
								class="icon material-icons"
							>account_circle</div>
							<span class="account-menu-item-title">Profile</span>
						</a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="feed">
		{@render children()}
	</div>

	<div class="other-wrapper">
		<div class="other">
			
		</div>
	</div>
</div>

<style lang="less">
	@import (reference) "../../lib/styles/vars.less";
	
	.content
	{
		display: flex;

		height: 100%;

		max-width: 700px;
		margin-inline: auto;

		> *
		{
			> *
			{
				padding: .5em;
			}
		}
	}

	.side-wrapper
	{
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
		position: relative;
		width: min-content;

		.account-button
		{
			cursor: pointer;
		}

		.account-menu
		{
			position: absolute;
			z-index: 9999;

			display: flex;
			flex-direction: column;


			background: @bg3;
			box-shadow: 2px 2px 10px #999;

			.account-menu-item
			{
				display: flex;
				align-items: center;
				gap: .25em;

				font-size: .625em;
				height: min-content;
				padding: .5em .5em .5em .5em;

				border: none;
				color: @fg1;
				font-weight: normal;

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

		&:not(:hover) .account-menu
		{
			display: none;
		}
	}

</style>