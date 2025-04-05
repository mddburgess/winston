import {AuthorDto} from "./AuthorDto";

export type AuthorListResponse = {
    results: number,
    authors: AuthorDto[],
}
