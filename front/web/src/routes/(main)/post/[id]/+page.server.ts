import type { PageServerLoad } from "./$types";
import { apiFetch } from "$lib/server/api";
import type { Page, Post } from "$lib";

export const load: PageServerLoad = async function(event)
{
  const res1 = await apiFetch(event, `/posts/get/${event.params.id}`);
  const res2 = await apiFetch(event, `/posts/comments/all/${event.params.id}`);

  return {
    post: await res1.json() as Post,
    comments: await res2.json() as Page<Post>,
  };
};