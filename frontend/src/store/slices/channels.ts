import { createEntityAdapter } from "@reduxjs/toolkit";
import { enhancedBackendApi } from "#/store/slices/backend";
import type { Channel, ListChannelsResp } from "#/api";

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

const addChannels = (channels: Channel[]) => {
    return channelsApi.util.updateQueryData(
        "listChannels",
        undefined,
        (draft) => {
            channelsAdapter.setMany(draft, channels);
        },
    );
};

export {
    addChannels,
    selectAllChannels,
    useGetChannelByHandleQuery,
    useListChannelsQuery,
};
