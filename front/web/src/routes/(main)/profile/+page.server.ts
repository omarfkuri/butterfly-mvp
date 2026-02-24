import type { PageServerLoad } from "./$types";
import { apiFetch } from "$lib/server/api";
import type { Page, Post, UserProfile } from "$lib";
import { fail, type Actions } from "@sveltejs/kit";

export const load: PageServerLoad = async function(event)
{
  const { user } = await event.parent();
  const res = await apiFetch(event, `/posts/user/${user.username}`);
  const profile = await apiFetch(event, `/profile/get/${user.username}`);

  return {
    postsPage: await res.json() as Page<Post>,
    profile: await profile.json() as UserProfile,
  };
};

export const actions = {

  async setProfilePicture(event)
  {
    console.log("Setting profile pic")
    const formData = await event.request.formData();

    const res = await apiFetch(
      event,
      "/profile/set/profile-picture",
      {
        method: 'POST',
        body: formData
      }
    );

    if (!res.ok) {
      console.log("Did not set profile pic")
      return fail(500, "Failed to upload profile picture");
    }

    console.log("Set profile pic")
    return { success: true };
  },

  async setCoverPicture(event)
  {
    const formData = await event.request.formData();

    const res = await apiFetch(
      event,
      "/profile/set/cover-picture",
      {
        method: 'POST',
        body: formData
      }
    );

    if (!res.ok) {
      return fail(500, "Failed to upload cover picture");
    }

    return { success: true };
  }

} satisfies Actions;