import type { PageServerLoad } from "./$types";
import { apiFetch } from "$lib/server/api";
import type { Post } from "$lib";

export const load: PageServerLoad = async function(event)
{
  const { user } = await event.parent();
  const res = await apiFetch(event, `/posts/user/${user.username}`);

  return {
    posts: await res.json() as Post[]
  };
};