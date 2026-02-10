// place files you want to import through the `$lib` alias in this folder.

export type User = {
	username: string
}

export type Post = {
	id: string
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

export type Page<Type> = {
	content: Type[],
	empty: boolean,
	first: boolean,
	last: boolean,
	number: number,
	numberOfElements: number,
	pageable: {
		offset: number,
		pageNumber: number,
		pageSize: number,
		paged: boolean,
		sort: {
			empty: boolean,
			sorted: boolean,
			unsorted: boolean
		},
		unpaged: boolean
	},
	size: number,
	sort: {
		empty: boolean,
		sorted: boolean,
		unsorted: boolean
	},
	totalElements: number,
	totalPages: number
}