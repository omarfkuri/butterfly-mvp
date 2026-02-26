import type { PageServerLoad } from "./$types";
import { apiFetch } from "$lib/server/api";
import type { Image, Page } from "$lib";

export const load: PageServerLoad = async function(event)
{
  // const { user } = await event.parent();
  const res = await apiFetch(event, `/image/own`);

  return {
    imagesPage: await res.json() as Page<Image>,
  };
};