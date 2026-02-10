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

export type Page<Type> = {
	content: Type[],
	empty: false,
	first: true,
	last: false,
	number: number,
	numberOfElements: number,
	pageable: {
		offset: number,
		pageNumber: number,
		pageSize: number,
		paged: true,
		sort: {
			empty: false,
			sorted: true,
			unsorted: false
		},
		unpaged: false
	},
	size: number,
	sort: {
		empty: false,
		sorted: true,
		unsorted: false
	},
	totalElements: number,
	totalPages: number
}