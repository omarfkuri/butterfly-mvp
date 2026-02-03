import { apiFetch } from '$lib/server/api';
import { type RequestHandler } from '@sveltejs/kit';

export const DELETE: RequestHandler = async (event) =>
{
	return apiFetch(event, `/posts/delete/${event.params.id}`, {
		method: "DELETE",
		credentials: 'include',
	});
};
