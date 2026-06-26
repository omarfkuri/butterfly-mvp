import type { LayoutServerLoad } from "./$types";
import { onlyUserRoute } from "$lib/server/api";

export const load: LayoutServerLoad = async function (event)
{
  console.log("Hi loading but in (main)")
  return {
    user: await onlyUserRoute(event)
  };
};
