import type { LayoutServerLoad } from "./$types";
import { onlyUserRoute } from "$lib/server/api";

export const load: LayoutServerLoad = async function (event)
{
  return {
    user: await onlyUserRoute(event)
  };
};
