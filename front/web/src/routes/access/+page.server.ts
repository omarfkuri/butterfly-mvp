import { fail, type Actions, redirect, type RequestEvent } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";
import { apiFetch, onlyGuestRoute } from "$lib/server/api";

export const load: PageServerLoad = async function(event)
{
  await onlyGuestRoute(event);
  return {};
};

export const actions = {
  async login(event)
  {
    try {
      const data = await event.request.formData();
      const username = data.get("username") as string;
      const password = data.get("password") as string;
      const v = verifyParameters(username, password);
      if (v != null)
        return v;
      
      const result = await login(username, password, event);
      if (result.ok)
        throw redirect(302, '/');

      return fail(400, { error: "Failed to login" });
    }

    catch(error)
    {
      if (error instanceof redirect) throw error;
      return { fail: true, error: String(error) };
    }
  },
  async register(event) {
    try {
      const data = await event.request.formData();
      const username = data.get("username") as string;
      const password = data.get("password") as string;
      const v = verifyParameters(username, password);
      if (v != null)
        return v;
      
      const res = await apiFetch(event, '/auth/register', {
        method: 'POST',
        body: new URLSearchParams({
          username,
          password
        }),
        credentials: 'include'
      });
      
      if (!res.ok) {
        return fail(400, { error: "Failed to register" });
      }
      
      const result = await login(username, password, event);
      if (result.ok)
        throw redirect(302, '/');
      
      return fail(400, { error: "Failed to login" });
    }

    catch(error)
    {
      if (error instanceof redirect) throw error;
      return fail(400, { error: String(error) });
    }
  }
} satisfies Actions;

function verifyParameters(username: string, password: string)
{
  if (username.length == 0)
    return fail(400, { error: "No username provided" })

  if (username.length < 4 || username.length > 24)
    return fail(400, { error: "Username must be between 4 and 24 characters" })

  if (!/^[a-z0-9._]+$/.test(username))
    return fail(400, { 
      error: "Username must only contain numbers, letters, dot and underscore" 
    })


  if (password.length == 0)
    return fail(400, { error: "No password provided" })

  if (password.length < 4 || password.length > 24)
    return fail(400, { error: "Password must be between 4 and 24 characters" })

  if (!/^[a-zA-Z0-9#]+$/.test(username))
    return fail(400, { 
      error: "Password must only contain numbers, letters and #" 
    })

  return null;
}

async function login(username: string, password: string, event: RequestEvent)
{
  const res = await apiFetch(event, '/auth/login', {
    method: 'POST',
    body: new URLSearchParams({
      username,
      password
    }),
    credentials: 'include'
  });
  
  if (!res.ok)
    return { ok: false, message: await res.text() };

  return { ok: true };
}