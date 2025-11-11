import { CardFooter, Col, Row } from "react-bootstrap";
import { useGetAuthorChannelSummaryQuery, useGetSettingsQuery } from "#/api";
import type { ChannelProps } from "#/types";
import { ChatFill, ClockHistory, Youtube } from "react-bootstrap-icons";
import { Date } from "#/components/Date.tsx";
import { DateTime } from "luxon";

const ChannelAuthorFocus = ({ channel }: ChannelProps) => {
  const { isSuccess, data } = useGetSettingsQuery();

  if (!isSuccess || data.author_focus === undefined) {
    return undefined;
  }

  return <AuthorChannelSummary channelHandle={channel.handle} authorHandle={data.author_focus} />;
};

const AuthorChannelSummary = (props: { channelHandle: string; authorHandle: string }) => {
  const { isSuccess, data } = useGetAuthorChannelSummaryQuery({ handle: props.authorHandle });

  if (!isSuccess) {
    return undefined;
  }
  const authorData = data.channels.find((ch) => ch.handle === props.channelHandle);
  if (!authorData) {
    return undefined;
  }

  const recentlyCommented = DateTime.fromISO(authorData.last_commented_at).diffNow("months").months > -1;
  const footerClass = recentlyCommented ? "bg-warning-subtle" : "bg-info-subtle";

  return (
    <CardFooter className={footerClass}>
      <Row>
        <Col className={"flex-center"} xs={"auto"}>
          <Youtube className={"me-2"} />
          {authorData.video_count}
        </Col>
        <Col className={"flex-center"} xs={"auto"}>
          <ChatFill className={"me-2"} />
          {authorData.comment_count + authorData.reply_count}
        </Col>
        <Col className={"flex-center"} xs={"auto"}>
          <ClockHistory className={"me-2"} />
          <Date date={authorData.last_commented_at} />
        </Col>
      </Row>
    </CardFooter>
  );
};

export { ChannelAuthorFocus };
