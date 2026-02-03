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

<div class="form-wrapper">
	<div class="form-container">
		{#if haveAccount}
			<h2>Log in</h2>

			<form method="POST" action="?/login" use:enhance>
				{#if form}
					{#if form.ok}
						Success!
					{:else if form.fail}
						Error: {form.error}
					{:else}
						Failure: {form.error}
					{/if}
				{/if}
				<label>
					Username
					<input name="username" type="text">
				</label>
				<label>
					Password
					<input name="password" type="password">
				</label>
				<button>Log in</button>
			</form>

		{:else}
			<h2>Register</h2>

			<form method="POST" action="?/register" use:enhance>
				{#if form}
					{#if form.ok}
						Success!
					{:else if form.fail}
						Error: {form.error}
					{:else}
						Failure: {form.error}
					{/if}
				{/if}
				<label>
					Username
					<input name="username" type="text">
				</label>
				<label>
					Password
					<input name="password" type="password">
				</label>
				<button>Register</button>
			</form>

		{/if}

		<div class="change">
			{#if haveAccount}
				Don't have an account? <button onclick={onToggle}>Sign up</button>
			{:else}
				Already have an account? <button onclick={onToggle}>Log in</button>
			{/if}
		</div>
	</div>
</div>

<style lang="less">
	.form-wrapper
	{
		display: flex;
		justify-content: center;
		align-items: center;

		width: 100%;
		height: 100%;
	}

	.form-container
	{
		display: flex;
		flex-direction: column;
		gap: 1em;

		padding: 1em;

		border-radius: .5em;

		background: #F8F8F8;
		box-shadow: 0 0 8px -2.5px #0006;
	}

	form
	{
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: .5em;

		label
		{
			display: flex;
			align-items: center;
			justify-content: space-between;
			gap: .5em;

			width: 100%;
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
	}
</style>