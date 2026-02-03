import { apiFetch } from '$lib/server/api';
import { type RequestHandler } from '@sveltejs/kit';

export const POST: RequestHandler = async (event) =>
{
	return apiFetch(event, '/posts/create', {
		method: 'POST',
		credentials: 'include',
		body: JSON.stringify(await event.request.json()),
		headers: {
			"Content-Type": "application/json"
		}
	});
};
