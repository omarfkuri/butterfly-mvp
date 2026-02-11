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

export type ApiArg = {
	arguments: unknown,
	code: string,
	codes: string[],
	defaultMessage: string
} | number

export type ApiError = {
	objectName: string,
	field: string,
	rejectedValue: string,
	codes: string[],
	arguments: ApiArg[],
	bindingFailure: boolean,
	code: string,
	defaultMessage: string
}

export type ApiErrorRes = {
  timestamp: string
  status: number,
  error: string
  trace: string
  message: string
  errors: ApiError[],
  path: string
}