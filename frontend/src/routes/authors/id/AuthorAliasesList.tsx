import { ListGroup } from "react-bootstrap";
import { AuthorAliasesListItem } from "#/routes/authors/id/AuthorAliasesListItem";
import type { AuthorProps } from "#/types";

const AuthorAliasesList = (props: AuthorProps) => (
  <ListGroup>
    {props.author.aliases?.map((alias, index) => (
      <AuthorAliasesListItem key={index} author={props.author} index={index} alias={alias} />
    ))}
  </ListGroup>
);

export { AuthorAliasesList };
