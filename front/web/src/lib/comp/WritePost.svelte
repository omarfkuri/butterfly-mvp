<script lang="ts">
	let title = $state("");
	let content = $state("");

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
		}
	}

</script>

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
				placeholder="What's going on?"
			></textarea>
		</label>
	</div>
	
	<div class="button">
		<button class="dark" onclick={onPost}>Post</button>
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