import { enhancedBackendApi } from "#/store/slices/backend";

const invalidateFetchLimits = () =>
    enhancedBackendApi.util.invalidateTags(["Fetch"]);

export { invalidateFetchLimits };
