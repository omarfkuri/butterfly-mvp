import type { PageServerLoad } from "./$types";
import { apiFetch } from "$lib/server/api";
import type { Page, Post } from "$lib";
import { fail, type Actions, redirect } from "@sveltejs/kit";

export const load: PageServerLoad = async function(event)
{
  const res1 = await apiFetch(event, `/posts/get/${event.params.id}`);
  const res2 = await apiFetch(event, `/posts/comments/all/${event.params.id}`);

  return {
    post: await res1.json() as Post,
    comments: await res2.json() as Page<Post>,
  };
};

export const actions = {
  async deletePost(event)
  {
    try {
      const res = await apiFetch(event, `/posts/delete/${event.params.id}`, {
        method: 'DELETE',
        credentials: 'include'
      });
      
      if (res.ok)
        throw redirect(302, '/');
      
      return fail(400, { error: "Failed delete post" });
    }

    catch(error)
    {
      if (error instanceof redirect) throw error;
      return fail(400, { error: String(error) });
    }
  }
} satisfies Actions