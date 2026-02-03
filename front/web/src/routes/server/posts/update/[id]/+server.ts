import { apiFetch } from '$lib/server/api';
import { type RequestHandler } from '@sveltejs/kit';

export const DELETE: RequestHandler = async (event) =>
{
	return apiFetch(event, `/posts/update/${event.params.id}`, {
		credentials: 'include',
		body: JSON.stringify(await event.request.json()),
		headers: {
			"Content-Type": "application/json"
		}
	});
};
