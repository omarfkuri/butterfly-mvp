import type { ApiError, User } from '$lib';
import { redirect, type RequestEvent, error } from '@sveltejs/kit';

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

export type ApiResponse = {
  ok: true;
  fail: false;
  response: Response;
} | (
  {
    ok: false;
    fail: false;
    response: Response;
  }
  &
  (
    {
      hasError: true;
      error: ApiError;
    }
    |
    {
      hasError: false;
      error: null;
    }
  )
) | {
  ok: false;
  fail: true;
  error: string;
}

export async function apiRequest(
  event: RequestEvent, 
  path: string, 
  init: RequestInit = {}
): Promise<ApiResponse>
{
  try
  {
    const response = await event.fetch(`http://social-api:8080${path}`, {
      ...init,
      headers: {
        ...init.headers,
        cookie: event.request.headers.get('cookie') ?? ''
      }
    });
    
    const setCookie = response.headers.get('set-cookie');
    if (setCookie) {
      event.cookies.set('JSESSIONID', 
        setCookie.split(';')[0].split('=')[1], 
        {
          path: '/',
          httpOnly: true,
          sameSite: 'lax',
          secure: false,
          maxAge: 60 * 60 * 24 * 7,
        }
      );
    }

    if (response.ok)
    {
      return {
        ok: true,
        fail: false,
        response,
      };
    }
    
    const hasError = response.headers.get("Content-Type")
    === "application/json";

    return {
      ok: false,
      fail: false,
      response,
      hasError,
      error:  hasError ? await response.json() : null,
    };
  }

  catch(error)
  {
    return {
      ok: false,
      fail: true,
      error: String(error),
    }
  }
}

export async function onlyUserRoute(event: RequestEvent)
: Promise<User>
{
  const res = await apiRequest(event, '/user/me');

  if (res.fail)
    throw error(500, {
      message: res.error,
    });

  if (!res.ok)
  {
    if (res.response.status === 401)
      throw redirect(302, '/access');

    throw error(res.response.status, {
      message: "Unexpected Error",
    });
  }

  return res.response.json();
}

export async function onlyGuestRoute(event: RequestEvent)
: Promise<void>
{
  const res = await apiRequest(event, '/user/me');

  if (res.fail)
    throw error(500, {
      message: res.error,
    });

  if (res.response.status !== 401)
    throw redirect(302, '/');
}