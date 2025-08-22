import { Col, Row, Spinner } from "react-bootstrap";
import { useGetFetchLimitsQuery } from "#/api";

const WARNING_PERCENT = 0.2;
const DANGER_PERCENT = 0.05;

const AvailableQuota = () => {
    const { data } = useGetFetchLimitsQuery();

    const availablePercent = data
        ? data.available_quota / data.daily_quota
        : 100;
    const textClass =
        availablePercent <= DANGER_PERCENT
            ? "text-danger"
            : availablePercent <= WARNING_PERCENT
              ? "text-warning"
              : "text-body-tertiary";

    return (
        <Row className={textClass}>
            <Col xs={12} className={"small"}>
                Quota available
            </Col>
            <Col className={`fw-bold ${textClass}`}>
                {data ? data.available_quota : <Spinner size={"sm"} />}
            </Col>
        </Row>
    );
};

export { AvailableQuota };
