import { apiSlice } from "./api";
import type { AuthorDetailsResponse, AuthorListResponse } from "../../types";
import { api } from "../../utils/links";

export const authorsApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        listAuthors: builder.query<AuthorListResponse, void>({
            query: api.v1.authors.get,
        }),
        findAuthorDetailsByHandle: builder.query<AuthorDetailsResponse, string>(
            { query: api.v1.authors.handle.get },
        ),
    }),
    overrideExisting: "throw",
});

export const { useListAuthorsQuery, useFindAuthorDetailsByHandleQuery } =
    authorsApi;
