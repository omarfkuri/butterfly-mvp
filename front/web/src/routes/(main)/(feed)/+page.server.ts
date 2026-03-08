import type { PageServerLoad } from "./$types";
import { apiRequest } from "$lib/server/api";
import type { Page, Post } from "$lib";
import { error } from "@sveltejs/kit";

export const load: PageServerLoad = async function(event)
{
  const res = await apiRequest(event, '/posts/all');

  if (res.fail)
    throw error(500, {
      message: `Failure: ${res.error}`,
    });

  if (!res.ok)
  {
    throw error(404, {
      message: res.hasError? res.error.message : "Failed to load posts",
    });
  }

  return {
    postsPage: await res.response.json() as Page<Post>
  };
};