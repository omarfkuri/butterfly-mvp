<script lang="ts">
    import type { ApiErrorRes } from "$lib";
    import Message from "./Message.svelte";

	let content = $state("");

	const disabled = $derived(content.length == 0);

	let errorComp = $state<Message>();
	let errorMsg = $state("");

	async function onPost()
	{
		if (content.length == 0)
			return await showMessage("No content was provided");

		if (content.length < 2 || content.length > 512)
			return await showMessage("Content must be between 4 and 512 letters");

		if (/^[\\p{L}\\p{N} .,'"\-!?()]+$/.test(content))
			return await showMessage("Content did not match expected pattern");

		const res = await fetch("/server/posts/create", {
			method: "POST",
			body: JSON.stringify({
				content,
			})
		});

		if (!res.ok)
		{
			const err = await res.json() as ApiErrorRes;
			for (const error of err.errors)
				await showMessage(`Failed to post: ${error.defaultMessage}`)
		}
		else
		{
			content = "";
		}
	}

	async function showMessage(content: string)
	{
		errorMsg = content;
		return errorComp?.show();
	}

</script>

<Message bind:this={errorComp}>
	{errorMsg}
</Message>

<div class="form">
	<div class="inputs">
		<label>
			<textarea
				required
				name="content" 
				bind:value={content}
				placeholder="What's going on?"
			></textarea>
		</label>
	</div>
	
	<div class="button">
		<button
			{disabled}
			tabindex="0"
			class="dark" 
			onclick={onPost}
		>Post</button>
	</div>
</div>

<style lang="less">
	@import (reference) "../styles/vars.less";

	.form
	{
		width: 100%;
		display: flex;

		.card();

		.inputs
		{
			display: flex;
			flex-direction: column;

			width: 100%;

			label
			{
				display: flex;
				flex-direction: column;

				input, textarea
				{
					width: 100%;
					padding: 0;
					border: none;
					outline: none;
				}

				textarea
				{
					resize: none;
					max-height: 60px;
				}
			}
		}

		.button
		{
			display: flex;
			flex-direction: column;
			justify-content: end;

			button
			{
				width: min-content;
			}
		}
	}
</style>