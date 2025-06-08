import { backendApi } from "#/api";
import type {
    Channel,
    Comment,
    ListCommentsForVideoResponse,
    Video,
} from "#/api";
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

type ListVideosForChannelDefinition = OverrideResultType<
    Definitions["listVideosForChannel"],
    EntityState<Video, string>
>;

type TopLevelComment = Omit<
    ListCommentsForVideoResponse["comments"][number],
    "replies"
> & {
    replies: EntityState<Comment, string>;
};

type ListCommentsForVideoDefinition = OverrideResultType<
    Definitions["listCommentsForVideo"],
    EntityState<TopLevelComment, string>
>;

type UpdatedDefinitions = Omit<
    Definitions,
    "listChannels" | "listVideosForChannel"
> & {
    listChannels: ListChannelsDefinition;
    listVideosForChannel: ListVideosForChannelDefinition;
    listCommentsForVideo: ListCommentsForVideoDefinition;
};

const enhancedBackendApi = backendApi.enhanceEndpoints<
    TagTypes,
    UpdatedDefinitions
>({});

export { enhancedBackendApi };
export type { TopLevelComment };
