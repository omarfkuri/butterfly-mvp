import type { PageServerLoad } from "./$types";
import { apiFetch } from "$lib/server/api";
import type { Page, Post } from "$lib";

export const load: PageServerLoad = async function(event)
{
  const { user } = await event.parent();
  const res = await apiFetch(event, `/posts/user/${user.username}`);

  return {
    postsPage: await res.json() as Page<Post>
  };
};