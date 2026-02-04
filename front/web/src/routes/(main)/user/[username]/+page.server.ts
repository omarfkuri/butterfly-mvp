import type { PageServerLoad } from "./$types";
import { apiFetch } from "$lib/server/api";
import type { Post } from "$lib";

export const load: PageServerLoad = async function(event)
{
  const res = await apiFetch(event, `/posts/user/${event.params.username}`);

  return {
    posts: await res.json() as Post[]
  };
};