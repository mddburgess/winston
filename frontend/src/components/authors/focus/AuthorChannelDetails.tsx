import { Col, Row } from "react-bootstrap";
import { ArrowRight, ChatFill, ChatQuoteFill, ClockHistory, PersonBoundingBox, Youtube } from "react-bootstrap-icons";
import { useGetAuthorChannelSummaryQuery } from "#/api";
import { Date } from "#/components/Date";
import { IconLabel } from "#/components/IconLabel";

const AuthorChannelDetails = (props: { channelHandle: string; authorHandle: string }) => {
  const { isSuccess, data } = useGetAuthorChannelSummaryQuery({ handle: props.authorHandle });

  if (!isSuccess) {
    return undefined;
  }
  const authorChannel = data.channels.find((channel) => channel.handle === props.channelHandle);
  if (!authorChannel) {
    return undefined;
  }

  return (
    <Row>
      <Col xs={"auto"}>
        <Row className={"mx-0 mb-2 p-1 border rounded border-info bg-info-subtle"}>
          <Col className={"flex-center"} xs={"12"} lg={"auto"}>
            <IconLabel icon={PersonBoundingBox} label={props.authorHandle} />
          </Col>
          <Col xs={12} lg={"auto"}>
            <Row>
              <Col xs={"auto"}>
                <IconLabel icon={Youtube} label={authorChannel.video_count} />
              </Col>
              <Col xs={"auto"}>
                <IconLabel icon={ChatFill} label={authorChannel.comment_count} />
              </Col>
              <Col xs={"auto"}>
                <IconLabel icon={ChatQuoteFill} label={authorChannel.reply_count} />
              </Col>
            </Row>
          </Col>
          <Col xs={12} lg={"auto"}>
            <Row className={"gx-2"}>
              <Col xs={"auto"} className={"flex-center"}>
                <ClockHistory />
              </Col>
              <Col xs={"auto"} className={"flex-center"}>
                <Date date={authorChannel.first_commented_at} />
              </Col>
              {authorChannel.last_commented_at > authorChannel.first_commented_at && (
                <>
                  <Col xs={"auto"} className={"flex-center"}>
                    <ArrowRight />
                  </Col>
                  <Col xs={"auto"} className={"flex-center"}>
                    <Date date={authorChannel.last_commented_at} />
                  </Col>
                </>
              )}
            </Row>
          </Col>
        </Row>
      </Col>
    </Row>
  );
};

export { AuthorChannelDetails };
