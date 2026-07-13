import { fail, type Actions, redirect, type RequestEvent } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";
import { apiFetch, onlyGuestRoute } from "$lib/server/api";

export const load: PageServerLoad = async function(event)
{
  await onlyGuestRoute(event);
  return {};
};

async function getApiError(res: Response)
{
  if (res.headers.get("Content-Type")?.startsWith("application/json"))
  {
    const { message } = await res.json();
    return message;
  }

  return null;
}

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
      
      if (!result.ok)
      {
        return fail(400, { error: result.message });
      }
    }

    catch(error)
    {
      return { fail: true, error: String(error) };
    }

    throw redirect(302, '/');
  },
  async register(event) {
    try {
      const data = await event.request.formData();
      const name = data.get("name") as string;
      const username = data.get("username") as string;
      const password = data.get("password") as string;
      const v1 = verifyName(name);
      if (v1 != null)
        return v1;

      const v2 = verifyParameters(username, password);
      if (v2 != null)
        return v2;
      
      const res = await apiFetch(event, '/auth/register', {
        method: 'POST',
        body: new URLSearchParams({
          name,
          username,
          password
        }),
        credentials: 'include'
      });
      
      if (!res.ok)
      {
        const msg = await getApiError(res);

        if (msg == null)
          return fail(500, { error: "Something went wrong." });

        return fail(400, { error: msg });
      }
      
      const result = await login(username, password, event);
      
      if (!result.ok)
      {
        return fail(400, { error: result.message });
      }
    }

    catch(error)
    {
      return fail(400, { error: JSON.stringify(error) });
    }
    
    throw redirect(302, '/');
  }
} satisfies Actions;

function verifyName(name: string)
{
  if (name.length == 0)
    return fail(400, { error: "No name provided" })

  if (name.length < 4 || name.length > 24)
    return fail(400, { error: "Name must be between 4 and 24 characters" })

  if (!/^[a-zA-Z0-9 ]+$/.test(name))
    return fail(400, { 
      error: "Name must only contain numbers, letters, dot and underscore" 
    })

  return null;
}


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

  if (!/^[a-zA-Z0-9#]+$/.test(password))
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
  {
    const msg = await getApiError(res);

    if (!msg)
    {
      return { ok: false, message: "Something went wrong" };
    }

    return { ok: false, message: msg };
  }

  return { ok: true };
}