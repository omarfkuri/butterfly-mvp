import { apiFetch } from '$lib/server/api';
import { type RequestHandler } from '@sveltejs/kit';

export const POST: RequestHandler = async (event) =>
{
	return apiFetch(event, `/follow/add/${event.params.followedID}`, {
		method: "POST",
		credentials: 'include',
	});
};
