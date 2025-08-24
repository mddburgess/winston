import { useEffect } from "react";
import { Spinner } from "react-bootstrap";
import { useEventSource, useEventSourceListener } from "react-sse-hooks";
import type { AppEvent, FetchStatusEvent, SubscriptionEvent } from "#/types";

type AppEventsSourceProps = {
    onSubscribed?: (subscriptionId: string) => void;
    onOperationEvent?: (operationEvent: AppEvent) => void;
    onDataEvent?: (dataEvent: AppEvent) => void;
    onStatusEvent?: (statusEvent: FetchStatusEvent) => void;
    hideSpinner?: boolean;
};

export const AppEventsSource = ({
    onSubscribed = () => {},
    onOperationEvent = () => {},
    onDataEvent = () => {},
    onStatusEvent = () => {},
    hideSpinner = false,
}: AppEventsSourceProps) => {
    const eventSource = useEventSource({
        source: `/api/v1/notifications`,
    });

    useEffect(() => {
        eventSource.onerror = (event) => {
            console.debug("Unsubscribed from app events:", event);
            eventSource.close();
        };
    }, [eventSource]);

    useEventSourceListener<SubscriptionEvent>(
        {
            source: eventSource,
            startOnInit: true,
            event: {
                name: "message",
                listener: (event) => {
                    console.debug(
                        "Subscribed to app events:",
                        event.data.subscriptionId,
                    );
                    onSubscribed(event.data.subscriptionId);
                },
                options: {
                    once: true,
                },
            },
        },
        [eventSource, onSubscribed],
    );

    const handleEvent = (name: string, handler: (event: AppEvent) => void) => ({
        source: eventSource,
        startOnInit: true,
        event: {
            name: name,
            listener: (event: { data: AppEvent }) => {
                console.debug(`Received app event of type '${name}':`, event);
                handler(event.data);
            },
        },
    });

    useEventSourceListener<AppEvent>(
        handleEvent("pull-operation", onOperationEvent),
        [eventSource, onOperationEvent],
    );

    useEventSourceListener<AppEvent>(handleEvent("pull-data", onDataEvent), [
        eventSource,
        onDataEvent,
    ]);

    useEventSourceListener<FetchStatusEvent>(
        {
            source: eventSource,
            startOnInit: true,
            event: {
                name: "fetch-status",
                listener: (event) => {
                    console.debug(
                        `Received event of type 'fetch-status':`,
                        event,
                    );
                    onStatusEvent(event.data);
                },
            },
        },
        [eventSource, onStatusEvent],
    );

    return hideSpinner ? <></> : <Spinner size={"sm"} className={"ms-2"} />;
};
