import { apiSlice } from "./api";
import { AuthorListResponse } from "../../model/authors/AuthorListResponse";
import { AuthorDetailsResponse } from "../../model/authors/AuthorDetailsResponse";
import { api } from "../../utils/links";

export const authorsApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        listAuthors: builder.query<AuthorListResponse, void>({
            query: api.authors.get,
        }),
        findAuthorDetailsByHandle: builder.query<AuthorDetailsResponse, string>(
            {
                query: api.authors.handle.get,
            },
        ),
    }),
    overrideExisting: "throw",
});

export const { useListAuthorsQuery, useFindAuthorDetailsByHandleQuery } =
    authorsApi;
