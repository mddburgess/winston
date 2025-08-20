import { Col, Image, Ratio, Row } from "react-bootstrap";
import { AuthorProfileImage } from "#/components/authors/AuthorProfileImage";
import { AuthorStatistics } from "#/components/authors/AuthorStatistics";
import { VideoChannelTitle } from "#/components/videos/VideoChannelTitle";
import { VideoPublishedAt } from "#/components/videos/VideoPublishedAt";
import type { AuthorProps, VideoProps } from "#/types";

type AuthorInfoJumbotronProps = AuthorProps & Partial<VideoProps>;

const AuthorInfoJumbotron = ({ author, video }: AuthorInfoJumbotronProps) => {
    return (
        <Row className={"bg-body-tertiary border mx-0 my-3 p-3 rounded-3"}>
            <Col>
                <AuthorRow author={author} />
                {video && <VideoRow video={video} />}
            </Col>
        </Row>
    );
};

const AuthorRow = ({ author }: AuthorProps) => (
    <Row>
        <Col xs={"auto"} className={"flex-center px-1"}>
            <AuthorProfileImage author={author} minWidth={48} />
        </Col>
        <Col>
            <h1 className={"mb-0"}>{author.handle}</h1>
        </Col>
        <AuthorStatistics author={author} />
    </Row>
);

const VideoRow = ({ video }: VideoProps) => (
    <Row className={"border-top mt-3 pt-3"}>
        <Col xs={1} className={"ps-1"}>
            <Ratio aspectRatio={"4x3"}>
                <Image rounded src={video.thumbnail_url} />
            </Ratio>
        </Col>
        <Col xs={11} className={"flex-center"}>
            <Row>
                <Col xs={12}>
                    <h3 className={"mb-1"}>{video.title}</h3>
                </Col>
                <Col xs={"auto"}>
                    <VideoChannelTitle video={video} />
                </Col>
                <Col>
                    <VideoPublishedAt video={video} />
                </Col>
            </Row>
        </Col>
    </Row>
);

export { AuthorInfoJumbotron };
