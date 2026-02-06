import { apiFetch } from '$lib/server/api';
import { type RequestHandler } from '@sveltejs/kit';

export const POST: RequestHandler = async (event) =>
{
	return apiFetch(event, `/like/add/${event.params.postID}`, {
		method: "POST",
		credentials: 'include',
	});
};
