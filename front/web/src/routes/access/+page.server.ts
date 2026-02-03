import { fail, type Actions, redirect, type RequestEvent } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";
import { apiFetch } from "$lib/server/api";

export const load: PageServerLoad = async function(event)
{
  const res = await apiFetch(event, '/me');

  if (res.status !== 401)
    throw redirect(302, '/');

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
      if (result.ok) {
        throw redirect(302, '/');
      }
      return result;
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
        return { ok: false, error: null };
      }
      
      const result = await login(username, password, event);
      if (result.ok) {
        throw redirect(302, '/');
      }
      return result;
    }

    catch(error)
    {
      if (error instanceof redirect) throw error;
      return { fail: true, error: String(error) };
    }
  }
} satisfies Actions;

function verifyParameters(username: string, password: string)
{
  if (username.length == 0)
    return fail(400, { username, missing: true })

  if (password.length == 0)
    return fail(400, { password, missing: true })

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
    return { ok: false, error: null };

  return { ok: true };
}