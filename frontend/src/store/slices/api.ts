import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { api } from "#/utils/links";
import type { FetchLimits } from "#/types";

const apiSlice = createApi({
    reducerPath: "api",
    baseQuery: fetchBaseQuery({ baseUrl: "/" }),
    tagTypes: ["fetchLimits"],
    endpoints: (builder) => ({
        getFetchLimits: builder.query<FetchLimits, void>({
            query: api.v1.fetch.limits.get,
            providesTags: ["fetchLimits"],
        }),
    }),
});

const { useGetFetchLimitsQuery } = apiSlice;

const invalidateFetchLimits = () =>
    apiSlice.util.invalidateTags(["fetchLimits"]);

export { apiSlice, invalidateFetchLimits, useGetFetchLimitsQuery };
