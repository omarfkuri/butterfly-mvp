<script lang="ts">
	import { page } from '$app/state';
</script>

<h1>{page.error?.message}</h1>
<p>Error {page.status}</p>

<a href="/">Go home</a>