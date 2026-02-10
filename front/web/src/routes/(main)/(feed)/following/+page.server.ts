import type { PageServerLoad } from "./$types";
import { apiFetch } from "$lib/server/api";
import type { Page, Post } from "$lib";

export const load: PageServerLoad = async function(event)
{
  const res = await apiFetch(event, '/posts/following');

  return {
    postsPage: await res.json() as Page<Post>
  };
};