<script lang="ts">
  import { enhance } from "$app/forms";

	interface Props
	{
		openText: string;
		confirmText: string;
		cancelText: string;
		action: string;
	}

	const {
		openText,
		confirmText,
		cancelText,
		action,
	}: Props = $props();

	let dialog = $state<HTMLDialogElement>();
	let form = $state<HTMLFormElement>();
	let input = $state<HTMLInputElement>();
	let file = $state<File | null>(null);

	function onSelect()
	{
		[ file = null ] = input?.files || [];
	}

	function onOpen()
	{
		dialog?.showModal();
	}

	function onConfirm()
	{
		close();
	}

	function onCancel()
	{
		close();
	}

	function close()
	{
		file = null;
		// form?.reset();
		dialog?.close();
	}
</script>

<button
	onclick={onOpen}
>{openText}</button>

<dialog bind:this={dialog}>
	<form
		{action}
		method="POST"
		enctype="multipart/form-data"
		use:enhance
		bind:this={form}
	>
		<div class="top">
			{#if file}
				<div class="preview">
					<img src={URL.createObjectURL(file)} alt="Preview">
				</div>
			{/if}
			<label class="btn">
				<input
					hidden
					name="file"
					type="file"
					onchange={onSelect}
					bind:this={input}
				>
				{#if file}
					Change image
				{:else}
					Choose image
				{/if}
			</label>
		</div>
		<div class="buttons">
			<button
				type="button"
				onclick={onCancel}
			>{cancelText}</button>
			<button
				disabled={file == null}

			>{confirmText}</button>
		</div>
	</form>
</dialog>

<style lang="less">
	@import (reference) "../styles/vars.less";

	.top
	{
		.card();

		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;

		.preview
		{
			img
			{
				width: 100%;
				max-width: 400px;

				border: 1px solid @bd1;
			}
		}
	}

	.buttons
	{
		.card();

		display: flex;
		justify-content: center;
		align-items: center;
	}

	dialog
	{
		padding: 0;
	}
</style>