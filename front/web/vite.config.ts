import { sveltekit } from '@sveltejs/kit/vite';
import path from 'path';
import { defineConfig } from 'vite';

export default defineConfig({
  plugins: [sveltekit()],
  resolve: {
    alias: {
      '$lib': path.resolve(__dirname, "src/lib")
    }
  },
  server: {
    host: true,
    allowedHosts: [],
    port: parseInt(process.env.PORT || "0"),
  },
});
