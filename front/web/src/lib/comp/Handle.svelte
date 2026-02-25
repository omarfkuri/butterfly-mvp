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
			margin-top: .20em;
			margin-right: -.06em;

			font-size: .91em;
			line-height: 0;
		}
	}

</style>