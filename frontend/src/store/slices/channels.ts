import { createEntityAdapter } from "@reduxjs/toolkit";
import { enhancedBackendApi } from "#/store/slices/backend";
import type { Channel, ListChannelsResp } from "#/api";

const channelsApi = enhancedBackendApi.enhanceEndpoints({
  endpoints: {
    listChannels: {
      transformResponse: (response: ListChannelsResp) =>
        channelsAdapter.addMany(channelsAdapter.getInitialState(), response.channels),
    },
  },
});

const { useListChannelsQuery, useGetChannelQuery } = channelsApi;

const channelsAdapter = createEntityAdapter<Channel>({
  sortComparer: (a, b) => a.title.localeCompare(b.title),
});

const { selectAll: selectAllChannels } = channelsAdapter.getSelectors();

export { selectAllChannels, useGetChannelQuery, useListChannelsQuery };
