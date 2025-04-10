import {AuthorWithStatisticsDto} from "./AuthorDto";

export type AuthorListResponse = {
    results: number,
    authors: AuthorWithStatisticsDto[],
}
