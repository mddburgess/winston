import { Col, Row } from "react-bootstrap";
import { ArrowLeft, ArrowRight } from "react-bootstrap-icons";
import { useSearchParams } from "react-router";
import { VideoChannelTitle } from "#/components/videos/VideoChannelTitle";
import type { VideoListProps, VideoProps } from "#/types";

type AuthorVideoNavigationProps = VideoListProps & {
    index: number;
};

const AuthorVideoNavigation = ({
    videos,
    index,
}: AuthorVideoNavigationProps) => {
    const prevVideo = index > 0 ? videos[index - 1] : undefined;
    const nextVideo = videos.at(index + 1);

    return (
        <Row className={"d-print-none mb-2 justify-content-between"}>
            <Col>
                {prevVideo && (
                    <VideoNavigation video={prevVideo} direction={"prev"} />
                )}
            </Col>
            <Col>
                {nextVideo && (
                    <VideoNavigation video={nextVideo} direction={"next"} />
                )}
            </Col>
        </Row>
    );
};

type VideoNavigationProps = VideoProps & {
    direction: "prev" | "next";
};

const VideoNavigation = ({ video, direction }: VideoNavigationProps) => {
    const setSearchParams = useSearchParams()[1];

    const alignment =
        direction === "prev"
            ? "justify-content-start text-start"
            : "justify-content-end text-end";

    const handleClick = () => {
        setSearchParams({ v: video.id });
    };

    return (
        <Row
            className={
                "border cursor-pointer h-100 hover-bg-info-subtle mx-0 p-2 rounded"
            }
            onClick={handleClick}
        >
            {direction === "prev" && (
                <Col xs={"auto"} className={"flex-center fs-5 p-2"}>
                    <ArrowLeft />
                </Col>
            )}
            <Col>
                <Row>
                    <Col xs={12} className={`${alignment} line-clamp-1`}>
                        {video.title}
                    </Col>
                    {direction === "next" && <Col></Col>}
                    <Col xs={"auto"}>
                        <VideoChannelTitle video={video} />
                    </Col>
                </Row>
            </Col>
            {direction === "next" && (
                <Col xs={"auto"} className={"flex-center fs-5 p-2"}>
                    <ArrowRight />
                </Col>
            )}
        </Row>
    );
};

export { AuthorVideoNavigation };
