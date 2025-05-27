import { backendApi } from "#/api";
import type { Channel } from "#/api";
import type { EntityState } from "@reduxjs/toolkit";
import type {
    DefinitionsFromApi,
    OverrideResultType,
    TagTypesFromApi,
} from "@reduxjs/toolkit/query";

type TagTypes = TagTypesFromApi<typeof backendApi>;
type Definitions = DefinitionsFromApi<typeof backendApi>;

type OverrideListChannels = OverrideResultType<
    Definitions["listChannels"],
    EntityState<Channel, string>
>;

type UpdatedDefinitions = Omit<Definitions, "listChannels"> & {
    listChannels: OverrideListChannels;
};

export const enhancedBackendApi = backendApi.enhanceEndpoints<
    TagTypes,
    UpdatedDefinitions
>({});
