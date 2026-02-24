<script lang="ts">
  import type { User, UserProfile } from "$lib";
    import ImageCircle from "./ImageCircle.svelte";
	
	interface Props
	{
		user: User;
		profile: UserProfile;
		fontSize?: string;
	}

	const { user, profile, fontSize = "1em" } : Props = $props();

</script>

<div class="container" style:font-size={fontSize}>
	<ImageCircle
		src={profile.profilePicture || "/image/user.png"}
		{user}
		height="2.3em"
	/>
	<div class="info">
		<div class="name">{user.name}</div>
		<div class="handle">
			<div class="at">{"@"}</div>
			<div class="username">{user.username}</div>
		</div>
	</div>
</div>

<style lang="less">
	@import (reference) "../styles/vars.less";
	
	.container
	{
		display: flex;
		align-items: center;
		gap: .25em;
	}
	
	.info
	{
		display: flex;
		flex-direction: column;
	}

	.name
	{
		font-size: 1.4em;
		font-weight: bold;
		white-space: nowrap;
	}

	.handle
	{
		display: flex;
		align-items: center;

		font-size: .55em;
		color: @fg3;

		.at
		{
			line-height: 0;
			margin-top: .15em;
		}

		.username
		{}
	}

</style>