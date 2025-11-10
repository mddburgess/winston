import { ListGroup } from "react-bootstrap";
import { AuthorsListItem } from "#/components/authors/AuthorsListItem";
import type { AuthorListProps } from "#/types";

type AuthorsListProps = AuthorListProps & {
  disabled: boolean;
};

const AuthorsList = ({ authors, disabled }: AuthorsListProps) => (
  <ListGroup className={"mb-2"}>
    {authors.map((author) => (
      <AuthorsListItem key={author.id} author={author} disabled={disabled} />
    ))}
  </ListGroup>
);

export { AuthorsList };
