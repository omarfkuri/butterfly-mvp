<script lang="ts">
    import Message from "./Message.svelte";


	interface Props
	{
		id: string
	}

	const { id }: Props = $props();

	let title = $state("");
	let content = $state("");

	const disabled = $derived(title.length == 0 || content.length == 0);

	let errorComp = $state<Message>();
	let errorMsg = $state("");

	async function onPost()
	{
		if (title.length == 0)
			return await showMessage("No title was provided");

		if (title.length < 2 || title.length > 100)
			return await showMessage("Title must be between 4 and 100 letters");

		if (/^[\\p{L}\\p{N} .,'"\-!?()]+$/.test(title))
			return await showMessage("Title did not match expected pattern");

		if (content.length == 0)
			return await showMessage("No content was provided");

		if (content.length < 2 || content.length > 512)
			return await showMessage("Content must be between 4 and 512 letters");

		if (/^[\\p{L}\\p{N} .,'"\-!?()]+$/.test(content))
			return await showMessage("Content did not match expected pattern");

		const res = await fetch(`/server/posts/comments/create/${id}`, {
			method: "POST",
			body: JSON.stringify({
				title,
				content,
			})
		});

		if (!res.ok)
		{
			await showMessage(`Failed to post`)
		}
		else
		{
			title = content = "";
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
			<input
				required
				name="title" 
				type="text" 
				bind:value={title}
				placeholder="Title"
			/>
		</label>
		<label>
			<textarea
				required
				name="content" 
				bind:value={content}
				placeholder="What's do you think?"
			></textarea>
		</label>
	</div>
	
	<div class="button">
		<button
			{disabled}
			tabindex="0"
			class="dark" 
			onclick={onPost}
		>Reply</button>
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

				input
				{
					font-size: 1.2em;
					font-weight: bold;
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