import { Col, ListGroupItem, Row } from "react-bootstrap";
import { XCircle } from "react-bootstrap-icons";
import { usePatchAuthorMutation } from "#/api";
import { IconButton } from "#/components/IconButton";
import type { Author } from "#/api";

const AuthorAliasesListItem = (props: { author: Author; index: number; alias: string }) => {
  const [patchAuthor] = usePatchAuthorMutation();

  const deleteAlias = () => {
    void patchAuthor({
      handle: props.author.handle,
      body: [{ op: "remove", path: `/aliases/${props.index}` }],
    });
  };

  return (
    <ListGroupItem>
      <Row>
        <Col className={"flex-center"}>{props.alias}</Col>
        <Col xs={"auto"} className={"p-0"}>
          <IconButton variant={"link"} icon={XCircle} onClick={deleteAlias} />
        </Col>
      </Row>
    </ListGroupItem>
  );
};

export { AuthorAliasesListItem };
