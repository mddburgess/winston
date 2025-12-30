import { DateTime } from "luxon";
import { CardFooter, Col, Row } from "react-bootstrap";
import { ChatFill, ChatQuoteFill, ClockHistory } from "react-bootstrap-icons";
import { useGetAuthorVideoSummaryQuery } from "#/api";
import { Date } from "#/components/Date";
import { IconLabel } from "#/components/IconLabel";

const AuthorVideoSummary = (props: { videoId: string; authorHandle: string }) => {
  const { isSuccess, data } = useGetAuthorVideoSummaryQuery({ handle: props.authorHandle });

  if (!isSuccess) {
    return undefined;
  }
  const authorVideo = data.videos.find((video) => video.id === props.videoId);
  if (!authorVideo) {
    return undefined;
  }

  const recentlyCommented = DateTime.fromISO(authorVideo.last_commented_at).diffNow("months").months > -1;
  const footerClass = recentlyCommented ? "bg-warning-subtle" : "bg-info-subtle";

  return (
    <CardFooter className={footerClass}>
      <Row>
        <Col xs={"auto"}>
          <IconLabel icon={ChatFill} label={authorVideo.comment_count} />
        </Col>
        <Col xs={"auto"}>
          <IconLabel icon={ChatQuoteFill} label={authorVideo.reply_count} />
        </Col>
        <Col xs={"auto"}>
          <IconLabel icon={ClockHistory}>
            <Date date={authorVideo.last_commented_at} />
          </IconLabel>
        </Col>
      </Row>
    </CardFooter>
  );
};

export { AuthorVideoSummary };
