import { Image, Ratio } from "react-bootstrap";
import type { AuthorProps } from "#/types";

type AuthorProfileImageProps = {
    minWidth?: string | number;
} & AuthorProps;

const AuthorProfileImage = ({ author, minWidth }: AuthorProfileImageProps) => (
    <Ratio aspectRatio={"1x1"} style={{ minWidth }}>
        <Image
            className={"border"}
            roundedCircle={true}
            src={author.profile_image_url}
        />
    </Ratio>
);

export { AuthorProfileImage };
