<script lang="ts">
  import { enhance } from '$app/forms';
	import type { PageProps } from './$types';

	let { form }: PageProps = $props();

	let haveAccount = $state(false);

	function onToggle()
	{
		haveAccount = !haveAccount;
	}
</script>

<div class="wrapper">
	<div class="content-wrapper">
		<main class="brand-wrapper">
			<div class="brand-name">
				<img src="/image/brand/Icon.png" alt="Icon">
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
			
		{#if form}
			{#if form.ok}
				Success!
			{:else}
				<div class="form-error">
					Error: {JSON.stringify(form)}
				</div>
			{/if}
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

				<form method="POST" action="?/register" use:enhance>
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
					<button class="lure" tabindex="0">Register</button>
				</form>

			{/if}

			<div class="change">
				{#if haveAccount}
					Don't have an account? 
					<button tabindex="0" onclick={onToggle}>Sign up</button>
				{:else}
					Already have an account? 
					<button tabindex="0" onclick={onToggle}>Log in</button>
				{/if}
			</div>
		</div>
	</div>
</div>

<style lang="less">
	@import (reference) "../../lib/styles/vars.less";

	.wrapper
	{
		display: flex;

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
		justify-content: center;
		align-items: center;

		height: 100%;
		width: 50%;

		font-size: .8em;

		.smallWeb({
			width: 100%;
			border-top: 1px solid @fg3;
		});
	}

	.form-container
	{
		display: flex;
		flex-direction: column;
		gap: 1em;

		width: 100%;
		padding: 1em;

		border-left: 1px solid @fg3;

		.smallWeb({
			border-left: none;
		});
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
					font-size: 1em;
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
	}
</style>