import { Col, Row } from "react-bootstrap";
import { ArrowRight, ChatFill, ChatQuoteFill, ClockHistory, PersonBoundingBox } from "react-bootstrap-icons";
import { useGetAuthorVideoSummaryQuery } from "#/api";
import { Date } from "#/components/Date";
import { IconLabel } from "#/components/IconLabel";
import type { Video } from "#/api";

const AuthorVideoDetails = (props: { video: Video; authorHandle: string }) => {
  const { isSuccess, data } = useGetAuthorVideoSummaryQuery({ handle: props.authorHandle });

  if (!isSuccess) {
    return undefined;
  }
  const authorVideo = data.videos.find((video) => video.id === props.video.id);
  if (!authorVideo) {
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
                <IconLabel icon={ChatFill} label={authorVideo.comment_count} />
              </Col>
              <Col xs={"auto"}>
                <IconLabel icon={ChatQuoteFill} label={authorVideo.reply_count} />
              </Col>
            </Row>
          </Col>
          <Col xs={12} lg={"auto"}>
            <Row className={"gx-2"}>
              <Col xs={"auto"} className={"flex-center"}>
                <ClockHistory />
              </Col>
              <Col xs={"auto"} className={"flex-center"}>
                <Date date={authorVideo.first_commented_at} />
              </Col>
              {authorVideo.last_commented_at > authorVideo.first_commented_at && (
                <>
                  <Col xs={"auto"} className={"flex-center"}>
                    <ArrowRight />
                  </Col>
                  <Col xs={"auto"} className={"flex-center"}>
                    <Date date={authorVideo.last_commented_at} />
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

export { AuthorVideoDetails };
