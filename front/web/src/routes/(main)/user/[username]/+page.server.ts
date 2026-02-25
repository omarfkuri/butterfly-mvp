import type { PageServerLoad } from "./$types";
import { apiFetch } from "$lib/server/api";
import type { Page, Post, User, UserProfile } from "$lib";

export const load: PageServerLoad = async function(event)
{
  const { user } = await event.parent();
  const author = await apiFetch(event, `/user/get/${event.params.username}`);
  const userPosts = await apiFetch(event, `/posts/user/${event.params.username}`);
  const doesFollow = await apiFetch(event, `/follow/follows/${event.params.username}`);
  
  const followerCount = await apiFetch(event, `/follow/follower-count/${event.params.username}`);
  const followingCount = await apiFetch(event, `/follow/following-count/${event.params.username}`);

  return {
    author: await author.json() as User,
    postsPage: await userPosts.json() as Page<Post>,
    doesFollow: await doesFollow.json() as boolean,
    isSelf: user.username == event.params.username,
    followerCount: await followerCount.json() as number,
    followingCount: await followingCount.json() as number,
  };
};