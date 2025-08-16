import { Link } from "react-router";
import { routes } from "#/utils/links";
import type { AuthorProps } from "#/types";
import type { ComponentProps } from "react";

type AuthorLinkProps = AuthorProps & Omit<ComponentProps<typeof Link>, "to">;

const AuthorLink = ({ author, ...linkProps }: AuthorLinkProps) => (
    <Link to={routes.authors.details(author.handle)} {...linkProps}>
        {author.handle}
    </Link>
);

export { AuthorLink };
