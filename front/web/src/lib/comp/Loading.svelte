<script lang="ts">
	interface Props
	{
		text?: string;
	}

	const { text } : Props = $props();

</script>

<div class="container">
	<div class="text">Loading {text}</div>
	<div class="ellipsis"></div>
</div>

<style lang="less">
	@import (reference) "../styles/vars.less";

	.container
	{
		display: flex;
		align-items: center;

		padding: .5em;
		border: 1px solid @primary;
		font-size: .8em;

		color: @fg3;

		// .text
		// {

		// }

		.ellipsis::after
		{
			animation-name: anim;
			animation-duration: 2s;

			@keyframes anim {

				0% {
					content: "";
				}
				
				25% {
					content: ".";
				}
				
				50% {
					content: "..";
				}
				
				75% {
					content: "...";
				}

				100% {
					content: "";
				}
			}
		}
	}

</style>