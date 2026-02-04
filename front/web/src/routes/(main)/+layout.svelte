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
			<a href="/">SO</a>

			<div class="buttons">
				<button onclick={onLogOut}>Log out</button>
			</div>
		</div>
	</div>

	<div class="feed">
		{@render children()}
	</div>

	<div class="other-wrapper">
		<div class="other">
			<a href="/user/{user.username}">{user.username}</a>
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
			padding-block: .125em;

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

		max-width: @mobileWidth - 100px;
		
		.mobile({
			max-width: unset;
		});

		border-inline: 1px solid @bd1;

		overflow-y: scroll;
	}

	.other-wrapper
	{
		flex-grow: 1;

		.mobile({
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

</style>