import { apiFetch } from '$lib/server/api';
import { type RequestHandler } from '@sveltejs/kit';

export const DELETE: RequestHandler = async (event) =>
{
	return apiFetch(event, `/follow/rem/${event.params.followedID}`, {
		method: "DELETE",
		credentials: 'include',
	});
};
