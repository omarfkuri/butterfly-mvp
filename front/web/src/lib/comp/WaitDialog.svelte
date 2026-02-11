<script lang="ts">
	
	interface Props
	{
		children: any
	}

	const { children }: Props = $props();

	let dialog = $state<HTMLDialogElement>();
	let dialogConfirm = $state<HTMLButtonElement>();
	let dialogCancel = $state<HTMLButtonElement>();
	
	export async function waitAction()
	: Promise<boolean>
	{
		dialog?.showModal();

		const confirmed = await new Promise<boolean>(resolve => {
			const confirmHandler = () => {
				dialogConfirm?.removeEventListener('click', confirmHandler);
				resolve(true);
			};
			dialogConfirm?.addEventListener('click', confirmHandler);

			const cancelHandler = () => {
				dialogCancel?.removeEventListener('click', cancelHandler);
				resolve(false);
			};
			dialogCancel?.addEventListener('click', cancelHandler);
		})

		dialog?.close();

		return confirmed;
	}

</script>

<dialog bind:this={dialog} class="wrapper">
	<div class="container">
		{@render children()}

		<div class="buttons">
			<button
				tabindex="0"
				class="lure"
				bind:this={dialogConfirm}
			>Ok</button>
			<button
				tabindex="0"
				bind:this={dialogCancel}
			>Cancel</button>
		</div>
	</div>
</dialog>

<style lang="less">
	
	.wrapper
	{
		width: 100%;
		max-width: 500px;
		margin-inline: auto;

		.container
		{
			display: flex;
			flex-direction: column;
			gap: 2em;

			.buttons
			{
				display: flex;
				flex-direction: row-reverse;
				align-items: center;
				justify-content: center;
				gap: .5em;

				button
				{
					font-size: 1em;
					min-width: 8ch;
				}
			}
		}
	}
</style>