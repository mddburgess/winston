import {Col, Pagination, Row} from "react-bootstrap";
import {ReactElement} from "react";
import {pluralize} from "../utils";
import {SearchControl} from "./SearchControl";

type PaginationRowProps = {
    name: string,
    total: number,
    pageSize: number,
    page: number,
    setPage: (page: number) => void,
    search?: string,
    setSearch?: (search: string) => void,
}

type MultiPagePaginationRowProps = PaginationRowProps & {
    totalPages: number
}

export const PaginationRow = (props: PaginationRowProps) => {
    const totalPages = Math.ceil(props.total / props.pageSize)
    return (
        <Row className={"mb-2"}>
            {totalPages < 2 && <SinglePagePaginationRow {...props}/>}
            {totalPages >= 2 && <MultiPagePaginationRow {...props} totalPages={totalPages}/>}
        </Row>
    );
};

const SinglePagePaginationRow = (props: PaginationRowProps) => (
    <>
        <Col className={"align-items-center d-flex"}>
            {pluralize(props.total, props.name)}
        </Col>
        {props.setSearch && (
            <Col xs={"auto"}>
                <SearchControl value={props.search} setValue={props.setSearch}/>
            </Col>
        )}
    </>
)

const MultiPagePaginationRow = (props: MultiPagePaginationRowProps) => {

    const firstPageNumber = Math.max(1, Math.min(props.page - 2, props.totalPages - 4));
    const lastPageNumber = Math.min(props.totalPages, Math.max(props.page + 2, 5));

    const paginationItems: ReactElement[] = [];
    for (let page = firstPageNumber; page <= lastPageNumber; ++page) {
        paginationItems.push(
            <Pagination.Item
                key={page}
                active={page === props.page}
                onClick={() => props.setPage(page)}
            >
                {page}
            </Pagination.Item>
        )
    }

    return (
        <>
            <Col className={"align-items-center d-flex"}>
                {first(props)}&ndash;{last(props)} of {pluralize(props.total, props.name)}
            </Col>
            <Col xs={"auto"}>
                <Pagination className={"mb-0"}>
                    <Pagination.First
                        disabled={props.page === 1}
                        onClick={() => props.setPage(1)}
                    />
                    <Pagination.Prev
                        disabled={props.page === 1}
                        onClick={() => props.setPage(props.page - 1)}
                    />
                    {firstPageNumber > 1 && <Pagination.Ellipsis disabled/>}
                    {...paginationItems}
                    {lastPageNumber < props.totalPages && <Pagination.Ellipsis disabled/>}
                    <Pagination.Next
                        disabled={props.page === props.totalPages}
                        onClick={() => props.setPage(props.page + 1)}
                    />
                    <Pagination.Last
                        disabled={props.page === props.totalPages}
                        onClick={() => props.setPage(props.totalPages)}
                    />
                </Pagination>
            </Col>
            {props.setSearch && (
                <Col xs={"auto"}>
                    <SearchControl value={props.search} setValue={props.setSearch}/>
                </Col>
            )}
        </>
    )
}

const first = (props: PaginationRowProps) => {
    return props.pageSize * (props.page - 1) + 1;
}

const last = (props: PaginationRowProps) => {
    return props.pageSize * props.page > props.total ? props.total : props.pageSize * props.page;
}
