import { ListGroup } from "react-bootstrap";
import { RefreshChannelsListItem } from "#/components/channels/RefreshChannelsListItem";
import type { Problem } from "#/api";
import type { ChannelListProps, PullOperationStatus } from "#/types";
import type { Dictionary } from "lodash";

type RefreshChannelsListProps = ChannelListProps & {
  responses: Partial<Dictionary<PullOperationStatus>>;
  errors: Partial<Dictionary<Problem>>;
};

const RefreshChannelsList = ({ channels, responses, errors }: RefreshChannelsListProps) => (
  <ListGroup variant={"flush"}>
    {channels.map((channel, index) => (
      <RefreshChannelsListItem
        key={channel.id}
        index={index + 1}
        channel={channel}
        status={responses[channel.handle] ?? "ready"}
        error={errors[channel.handle]}
      />
    ))}
  </ListGroup>
);

export { RefreshChannelsList };
