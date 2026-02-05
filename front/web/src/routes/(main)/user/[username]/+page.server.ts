import type { PageServerLoad } from "./$types";
import { apiFetch } from "$lib/server/api";
import type { Post } from "$lib";

export const load: PageServerLoad = async function(event)
{
  const { user } = await event.parent();
  const userPosts = await apiFetch(event, `/posts/user/${event.params.username}`);
  const doesFollow = await apiFetch(event, `/follow/follows/${event.params.username}`);

  return {
    posts: await userPosts.json() as Post[],
    doesFollow: await doesFollow.json() as boolean,
    isSelf: user.username == event.params.username,
  };
};