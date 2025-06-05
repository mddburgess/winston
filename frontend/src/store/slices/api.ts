import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

const apiSlice = createApi({
    reducerPath: "api",
    baseQuery: fetchBaseQuery({ baseUrl: "/" }),
    tagTypes: ["Fetch"],
    endpoints: () => ({}),
});

const invalidateFetchLimits = () => apiSlice.util.invalidateTags(["Fetch"]);

export { apiSlice, invalidateFetchLimits };
