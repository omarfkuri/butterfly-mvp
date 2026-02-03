<script lang="ts">
	let title = $state("");
	let content = $state("");
	
	let dialog = $state<HTMLDialogElement>();

	export function openModal()
	{
		dialog?.showModal();
	}

	function onCancel()
	{
		dialog?.close();
	}

	async function onPost()
	{
		if (title.length == 0)
			return alert("No title was provided");

		if (content.length == 0)
			return alert("No content was provided");

		const res = await fetch("/server/posts/create", {
			method: "POST",
			body: JSON.stringify({
				title,
				content,
			})
		});

		if (!res.ok)
		{
			alert(`Failed to post`)
		}
		else
		{
			title = content = "";

			dialog?.close();
		}
	}

</script>

<dialog bind:this={dialog}>
	<div class="control">
		<button onclick={onCancel}>×</button>
	</div>

	<h3>Create Post</h3>

	<div class="form">
		<label>
			<span class="input-title">Title</span>
			<input name="title" type="text" bind:value={title}/>
		</label>
		<label>
			<span class="input-title">Content</span>
			<textarea name="content" bind:value={content}></textarea>
		</label>
		
		<div class="button">
			<button onclick={onPost}>Post</button>
		</div>
	</div>
</dialog>

<style lang="less">
	dialog
	{
		flex-direction: column;
		align-items: center;

		gap: 1em;

		border: none;
		border-radius: .5em;

		&:open
		{
			display: flex;
		}
	}

	.control
	{
		width: 100%;
		display: flex;
		justify-content: end;

		button
		{
			border: none;
			background: none;

			font-size: 1em;
			padding: 0;
			margin: 0;
		}
	}

	.form
	{
		display: flex;
		flex-direction: column;
		gap: .75em;

		label
		{
			display: flex;
			flex-direction: column;

			.input-title
			{
				font-size: .8em;
			}

			input, textarea
			{
				width: 100%;
			}

			textarea
			{
				resize: vertical;
				max-height: 60px;
			}
		}

		.button
		{
			display: flex;
			justify-content: center;

			width: 100%;

			button
			{
				width: min-content;
			}
		}
	}
</style>