import type { ApiError, User } from '$lib';
import { redirect, type RequestEvent, error } from '@sveltejs/kit';

const BACK = "http://social-api-service:8080"

export async function apiFetch(
  event: RequestEvent, 
  path: string, 
  init: RequestInit = {}
) {
  try
  {
    const response = await event.fetch(`${BACK}${path}`, {
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
  catch(error)
  {
    console.error(new Date, "API Fetch exception", {
      path,
      error: String(error)
    });

    return new Response("Unexpected error", { status: 500 });
  }
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
    const response = await event.fetch(`${BACK}${path}`, {
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

    const error = hasError ? await response.json() : null;

    if (hasError)
      console.error(new Date, "API Request received known error", error);
    else
    {
      const rawBody = await response.text();
      console.error(new Date, "API Request received unknown error", {
        status: response.status,
        statusText: response.statusText,
        contentType: response.headers.get("Content-Type"),
        url: response.url,
        body: rawBody,
      });
    }


    return {
      ok: false,
      fail: false,
      response,
      hasError,
      error,
    };
  }

  catch(error)
  {
    console.error(new Date, "API Request exception", {
      path,
      error: String(error)
    });

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

    console.error(new Date, "Failed to get user route", res.error);

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
  {
    console.error(new Date, "Failed to get guest route", res.error);

    throw error(500, {
      message: res.error,
    });
  }

  if (res.response.status !== 401)
    throw redirect(302, '/');
}