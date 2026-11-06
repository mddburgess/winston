import { AllAuthorsList } from "#/components/authors/AllAuthorsList";
import { PageTitle } from "#/components/PageTitle";

const AuthorListRoute = () => {
  return (
    <>
      <PageTitle title={"Authors"} />
      <AllAuthorsList />
    </>
  );
};

export { AuthorListRoute };
