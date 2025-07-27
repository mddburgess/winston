import { Spinner } from "react-bootstrap";
import { useGetFetchLimitsQuery } from "#/api";

export const RemainingQuota = () => {
    const { data } = useGetFetchLimitsQuery();
    return data ? <>{data.remainingQuota}</> : <Spinner size={"sm"} />;
};
