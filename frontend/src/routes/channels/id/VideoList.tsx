import {VideoDto} from "../../../model/VideoDto";
import {ListGroup} from "react-bootstrap";
import {VideoListItem} from "./VideoListItem";

type VideoListProps = {
    videos?: VideoDto[]
}

export const VideoList = ({videos}: VideoListProps) => (
    <ListGroup>
        {videos?.map((video) => (<VideoListItem video={video}/>))}
    </ListGroup>
)
