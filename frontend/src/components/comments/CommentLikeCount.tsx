import { Col, Row } from "react-bootstrap";
import { Heart } from "react-bootstrap-icons";
import type { CommentProps } from "#/types";

const CommentLikeCount = ({ comment }: CommentProps) =>
    comment.like_count > 0 && (
        <Row className={"ms-2 g-1"}>
            <Col xs={"auto"}>
                <Heart />
            </Col>
            <Col xs={"auto"}>{comment.like_count}</Col>
        </Row>
    );

export { CommentLikeCount };
