import { apiFetch } from '$lib/server/api';
import { type RequestHandler } from '@sveltejs/kit';

export const GET: RequestHandler = async (event) =>
{
	return apiFetch(event, '/posts/all', {
		method: 'GET',
		credentials: 'include',
	});
};
