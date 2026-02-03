import type { RequestHandler } from '@sveltejs/kit';

export const GET: RequestHandler = async ({ fetch, request, params }) => {
  const backend = await fetch(`http://social-api:8080/events/posts/${params.topic}`, {
    headers: {
      cookie: request.headers.get('cookie') ?? ''
    }
  });

  if (!backend.body) {
    return new Response('No stream', { status: 500 });
  }

  return new Response(backend.body, {
    status: 200,
    headers: {
      'Content-Type': 'text/event-stream',
      'Cache-Control': 'no-cache',
      'Connection': 'keep-alive'
    }
  });
};
