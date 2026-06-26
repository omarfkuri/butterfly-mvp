FROM node:22 AS build

WORKDIR /app

COPY package*.json ./

RUN npm ci

COPY . .

RUN npm run build

FROM node:22-slim

WORKDIR /app

COPY package*.json ./

RUN npm ci --omit=dev

COPY --from=build /app/build ./build

EXPOSE 3000

CMD ["node","build"]