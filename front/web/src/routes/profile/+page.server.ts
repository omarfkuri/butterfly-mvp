import { redirect } from "@sveltejs/kit";
import type { LayoutServerLoad } from "../$types";
import { apiFetch } from "$lib/server/api";
import type { User } from "$lib";

export const load: LayoutServerLoad = async (event) => {
  const res = await apiFetch(event, '/me');
  if (res.status === 401) {
    throw redirect(302, '/access');
  }
  return {
    user: await res.json() as User
  };
};
