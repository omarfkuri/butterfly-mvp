// place files you want to import through the `$lib` alias in this folder.

export type User = {
	username: string
}

export type Post = {
	id: number
	title: string
	content: string
	username: string
	parentId: number
	createdAt: string
	likeCount: number
	likedByMe: boolean
}

export type Topic = (
	|"all"
	|`author:${string}`
);