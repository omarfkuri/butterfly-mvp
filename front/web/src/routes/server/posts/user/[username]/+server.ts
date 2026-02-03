import { apiFetch } from '$lib/server/api';
import { type RequestHandler } from '@sveltejs/kit';

export const GET: RequestHandler = async (event) =>
{
	return apiFetch(event, `/posts/user/${event.params.username}`, {
		credentials: 'include',
	});
};
