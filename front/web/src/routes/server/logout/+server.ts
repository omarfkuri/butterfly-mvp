import { apiFetch } from '$lib/server/api';
import { redirect, type RequestHandler } from '@sveltejs/kit';

export const POST: RequestHandler = async (event) =>
{
	const res = await apiFetch(event, '/auth/logout', {
		method: 'POST',
		credentials: 'include'
	});

	if (!res.ok)
		return res
	
	event.cookies.delete('JSESSIONID', { path: '/' });
	
	throw redirect(302, '/access');
};
