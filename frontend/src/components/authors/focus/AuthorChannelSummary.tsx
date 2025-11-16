import { DateTime } from "luxon";
import { CardFooter, Col, Row } from "react-bootstrap";
import { ChatFill, ClockHistory, Youtube } from "react-bootstrap-icons";
import { useGetAuthorChannelSummaryQuery } from "#/api";
import { Date } from "#/components/Date";

const AuthorChannelSummary = (props: { channelHandle: string; authorHandle: string }) => {
  const { isSuccess, data } = useGetAuthorChannelSummaryQuery({ handle: props.authorHandle });

  if (!isSuccess) {
    return undefined;
  }
  const authorChannel = data.channels.find((channel) => channel.handle === props.channelHandle);
  if (!authorChannel) {
    return undefined;
  }

  const recentlyCommented = DateTime.fromISO(authorChannel.last_commented_at).diffNow("months").months > -1;
  const footerClass = recentlyCommented ? "bg-warning-subtle" : "bg-info-subtle";

  return (
    <CardFooter className={footerClass}>
      <Row>
        <Col className={"flex-center"} xs={"auto"}>
          <Youtube className={"me-2"} />
          {authorChannel.video_count}
        </Col>
        <Col className={"flex-center"} xs={"auto"}>
          <ChatFill className={"me-2"} />
          {authorChannel.comment_count + authorChannel.reply_count}
        </Col>
        <Col className={"flex-center"} xs={"auto"}>
          <ClockHistory className={"me-2"} />
          <Date date={authorChannel.last_commented_at} />
        </Col>
      </Row>
    </CardFooter>
  );
};

export { AuthorChannelSummary };
