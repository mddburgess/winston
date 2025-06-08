import { backendApi } from "#/api";
import type { Channel, Comment, ListCommentsResponse, Video } from "#/api";
import type { EntityState } from "@reduxjs/toolkit";
import type {
    DefinitionsFromApi,
    OverrideResultType,
    TagTypesFromApi,
} from "@reduxjs/toolkit/query";

type TagTypes = TagTypesFromApi<typeof backendApi>;
type Definitions = DefinitionsFromApi<typeof backendApi>;

type ListChannelsDefinition = OverrideResultType<
    Definitions["listChannels"],
    EntityState<Channel, string>
>;

type ListVideosDefinition = OverrideResultType<
    Definitions["listVideos"],
    EntityState<Video, string>
>;

type TopLevelComment = Omit<
    ListCommentsResponse["comments"][number],
    "replies"
> & {
    replies: EntityState<Comment, string>;
};

type ListCommentsDefinition = OverrideResultType<
    Definitions["listComments"],
    EntityState<TopLevelComment, string>
>;

type UpdatedDefinitions = Omit<
    Definitions,
    "listChannels" | "listVideos" | "listComments"
> & {
    listChannels: ListChannelsDefinition;
    listVideos: ListVideosDefinition;
    listComments: ListCommentsDefinition;
};

const enhancedBackendApi = backendApi.enhanceEndpoints<
    TagTypes,
    UpdatedDefinitions
>({});

export { enhancedBackendApi };
export type { TopLevelComment };
