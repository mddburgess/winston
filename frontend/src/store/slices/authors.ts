import {apiSlice} from "./api";
import {AuthorListResponse} from "../../model/authors/AuthorListResponse";
import {AuthorDetailsResponse} from "../../model/authors/AuthorDetailsResponse";

export const authorsApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        listAuthors: builder.query<AuthorListResponse, void>({
            query: () => "/authors"
        }),
        findAuthorDetailsById: builder.query<AuthorDetailsResponse, string>({
            query: (authorId) => `/authors/${authorId}`
        }),
    }),
    overrideExisting: 'throw'
})

export const {
    useListAuthorsQuery,
    useFindAuthorDetailsByIdQuery,
} = authorsApi;
