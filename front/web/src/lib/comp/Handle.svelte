<script lang="ts">
	interface Props
	{
		username: string;
	}

	const { username } : Props = $props();

</script>

<div class="handle">
	<div class="at">{"@"}</div>
	<div class="username">{username}</div>
</div>

<style lang="less">
	@import (reference) "../styles/vars.less";

	.handle
	{
		display: flex;
		align-items: center;

		color: @fg3;

		.at
		{
			line-height: 0;
			margin-top: .15em;
		}
	}

</style>