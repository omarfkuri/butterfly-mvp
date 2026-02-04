// place files you want to import through the `$lib` alias in this folder.

export type User = {
	username: string
}

export type Post = {
	title: string
	content: string
	createdAt: string
	parent: string | null
	id: string
	username: string
}