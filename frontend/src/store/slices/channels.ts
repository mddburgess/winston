import { createEntityAdapter } from "@reduxjs/toolkit";
import { enhancedBackendApi } from "#/store/slices/backend";
import type { Channel, ListChannelsResp } from "#/api";
import type { EntityState } from "@reduxjs/toolkit";

const channelsApi = enhancedBackendApi.enhanceEndpoints({
    endpoints: {
        listChannels: {
            transformResponse: (response: ListChannelsResp) =>
                channelsAdapter.addMany(
                    channelsAdapter.getInitialState(),
                    response.channels,
                ),
        },
    },
});

const { useListChannelsQuery, useGetChannelByHandleQuery } = channelsApi;

const channelsAdapter = createEntityAdapter<Channel>({
    sortComparer: (a, b) => a.title.localeCompare(b.title),
});

const { selectAll: selectAllChannels } = channelsAdapter.getSelectors();

const appendChannels = (channels: Channel[]) =>
    channelsApi.util.updateQueryData(
        "listChannels",
        undefined,
        (draft: EntityState<Channel, string>) => {
            channelsAdapter.setMany(draft, channels);
        },
    );

export {
    appendChannels,
    selectAllChannels,
    useGetChannelByHandleQuery,
    useListChannelsQuery,
};
