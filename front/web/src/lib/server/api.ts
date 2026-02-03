import type { User } from '$lib';
import { redirect, type RequestEvent } from '@sveltejs/kit';

export async function apiFetch(
  event: RequestEvent, 
  path: string, 
  init: RequestInit = {}
) {
  const response = await event.fetch(`http://social-api:8080${path}`, {
    ...init,
    headers: {
      ...init.headers,
      cookie: event.request.headers.get('cookie') ?? ''
    }
  });
  
  // Forward Set-Cookie headers from backend to client
  const setCookie = response.headers.get('set-cookie');
  if (setCookie) {
    event.cookies.set('JSESSIONID', 
      setCookie.split(';')[0].split('=')[1], 
      {
        path: '/',
        httpOnly: true,
        sameSite: 'lax',
        secure: false, // Set to true in production
        maxAge: 60 * 60 * 24 * 7 // 1 week
      }
    );
  }
  
  return response;
}

export async function onlyUserRoute(event: RequestEvent)
: Promise<User>
{
  const res = await apiFetch(event, '/me');
  
  if (res.status === 401)
    throw redirect(302, '/access');

  return res.json();
}

export async function onlyGuestRoute(event: RequestEvent)
: Promise<void>
{
  const res = await apiFetch(event, '/me');

  if (res.status !== 401)
    throw redirect(302, '/');
}