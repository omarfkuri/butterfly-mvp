<script lang="ts">
  import { enhance } from '$app/forms';
  import Confirm from '$lib/comp/Confirm.svelte';
	import type { PageProps, SubmitFunction } from './$types';
	import icon from "$lib/assets/image/brand/Icon.png"

	let { form }: PageProps = $props();

	let haveAccount = $state(false);
	let confirmComp = $state<Confirm>();

	function onToggle()
	{
		haveAccount = !haveAccount;
	}

	export const onRegister: SubmitFunction = async function(e)
	{
		const confirmed = await confirmComp?.waitAction();

    if (confirmed)
    {
    	console.log("Confirmed")
      return ({ update }) => {
      	try {
      		update();
      	}
      	catch(err)
      	{
      		console.log("Err", err)
      	}
      };
    }
    else
    {
      e.cancel();
    }
	}

</script>

<div class="wrapper">
	<div class="content-wrapper">
		<main class="brand-wrapper">
			<div class="brand-name">
				<img 
					src={icon}
					alt="Icon"
					draggable="false"
				/>
				<h1>Butterfly</h1>
			</div>

			<article class="brand-about">
				<h2>Speak your mind</h2>
				<p>
					Follow anyone, from your closest friends
					to the biggest celebrities. Speak your mind
					without limitation and interact with replies.
				</p>
			</article>
		</main>
	</div>
	<div class="form-wrapper">
			
		{#if form?.error}
			<div class="server-error-wrapper">
				<div class="server-error-content">
					{form.error}
				</div>
			</div>
		{/if}

		<div class="form-container">
			{#if haveAccount}
				<h2>Log in</h2>

				<form method="POST" action="?/login" use:enhance>
					<div class="inputs">
						<label>
							<span class="material-icons icon">person</span>
							<input
								name="username"
								type="text"
								placeholder="Username"
							>
						</label>
						<label>
							<span class="material-icons icon">key</span>
							<input
								name="password"
								type="password"
								placeholder="Password"
							>
						</label>
					</div>
					<button class="lure" tabindex="0">Log in</button>
				</form>

			{:else}
				<h2>Register</h2>

				<form method="POST" action="?/register" 
					use:enhance={onRegister}
				>
					<div class="inputs">
						<label>
							<span class="material-icons icon">person</span>
							<input
								name="name"
								type="text"
								placeholder="Name"
							>
						</label>
						<label>
							<span class="material-icons icon">badge</span>
							<input
								name="username"
								type="text"
								placeholder="Username"
							>
						</label>
						<label>
							<span class="material-icons icon">key</span>
							<input
								name="password"
								type="password"
								placeholder="Password"
							>
						</label>
					</div>
					<button class="lure" tabindex="0">Register</button>
				</form>

			{/if}

			<div class="change">
				{#if haveAccount}
					<div class="change-title">Don't have an account?</div> 
					<button tabindex="0" onclick={onToggle}>Sign up</button>
				{:else}
					<div class="change-title">Already have an account?</div> 
					<button tabindex="0" onclick={onToggle}>Log in</button>
				{/if}
			</div>
		</div>
	</div>
</div>

<Confirm bind:this={confirmComp}>
	<div class="warn-container">
		<h2>Before you register</h2>

		<p>
			The purpose of this page is to display
			the capabilities of a social media like
			application. Please do not enter real 
			information. While the back end is secure,
			we make no guarantees to protect data.
		</p>
	</div>
</Confirm>

<style lang="less">
	@import (reference) "../../lib/styles/vars.less";

	.wrapper
	{
		display: flex;
		align-items: center;

		width: 100%;
		height: 100%;

		max-width: 800px;
		margin-inline: auto;

		.smallWeb({
			flex-direction: column;
		});
	}

	.content-wrapper
	{
		width: 100%;
		height: 100%;

		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}
	
	.brand-wrapper
	{
		display: flex;
		flex-direction: column;
		justify-content: center;
		gap: 1.5em;

		width: 100%;
		padding: .5em;


		.brand-name
		{
			display: flex;
			align-items: center;
			gap: .5em;

			width: 80%;

			font-size: 1.5em;

			img
			{
				width: 2em;
				object-fit: contain;
			}
		}

		.brand-about
		{
			display: flex;
			flex-direction: column;
			gap: .25em;

			h2
			{
				font-size: 1em;
			}

			p
			{
				font-size: .75em;
			}
		}
	}

	.form-wrapper
	{
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;

		height: min-content;
		width: 50%;

		font-size: .8em;
		border-left: 1px solid @fg3;

		.smallWeb({
			width: 100%;
			border-top: 1px solid @fg3;
			border-left: none;
		});
	}

	.server-error-wrapper
	{
		padding: 1em;


		.server-error-content
		{
			border: 1px solid @error;
			padding: 1em;
			font-size: .8em;
		}
	}

	.form-container
	{
		display: flex;
		flex-direction: column;
		gap: 1em;

		width: 100%;
		padding: 1em;
	}

	form
	{
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: .5em;

		width: 100%;

		.inputs
		{
			display: flex;
			flex-direction: column;
			gap: .25em;

			width: 100%;

			label
			{
				display: flex;
				align-items: center;
				justify-content: space-between;
				gap: .25em;

				width: 100%;

				input
				{
					width: 100%;
					padding: .25em;
				}

				.icon
				{
					font-size: .9em;
					color: @fg3;
				}
			}
		}

		button
		{
			font-size: 1em;
			width: min-content;
			white-space: nowrap;
		}
	}

	.change
	{
		display: flex;
		align-items: center;
		justify-content: center;
		gap: .5em;

		font-size: .8em;

		padding-top: 1.5em;

		.change-title
		{
			color: @fg3;
		}
	}

	.warn-container
	{
		display: flex;
		flex-direction: column;
		gap: .25em;

		p
		{
			font-size: .8em;
		}
	}

</style>