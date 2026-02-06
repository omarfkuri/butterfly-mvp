import { apiFetch } from '$lib/server/api';
import { type RequestHandler } from '@sveltejs/kit';

export const DELETE: RequestHandler = async (event) =>
{
	return apiFetch(event, `/like/rem/${event.params.postID}`, {
		method: "DELETE",
		credentials: 'include',
	});
};
