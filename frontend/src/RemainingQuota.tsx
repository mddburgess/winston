import {useGetFetchLimitsQuery} from "./store/slices/api";
import {Spinner} from "react-bootstrap";

export const RemainingQuota = () => {
    const { data } = useGetFetchLimitsQuery();
    return data ? <>{data.remainingQuota}</> : <Spinner size={"sm"}/>;
}
