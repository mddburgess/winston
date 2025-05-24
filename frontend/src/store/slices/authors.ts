import { api } from "#/utils/links";
import { apiSlice } from "./api";
import type { AuthorListResponse, AuthorSummaryResponse } from "#/types";

const authorsApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        listAuthors: builder.query<AuthorListResponse, void>({
            query: api.v1.authors.get,
        }),
        findAuthorSummaryByHandle: builder.query<AuthorSummaryResponse, string>(
            { query: api.v2.authors.handle.get },
        ),
    }),
    overrideExisting: "throw",
});

export const { useListAuthorsQuery, useFindAuthorSummaryByHandleQuery } =
    authorsApi;
