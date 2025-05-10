import { useEffect } from "react";
import { Spinner } from "react-bootstrap";
import { useEventSource, useEventSourceListener } from "react-sse-hooks";
import { api } from "../utils/links";
import type { FetchStatusEvent, SubscriptionEvent } from "../types";

type NotificationsSourceProps<T> = {
    onSubscribed: (subscriptionId: string) => void;
    onDataEvent: (dataEvent: T) => void;
    onStatusEvent: (statusEvent: FetchStatusEvent) => void;
};

export const NotificationsSource = <T,>(props: NotificationsSourceProps<T>) => {
    const eventSource = useEventSource({
        source: api.v1.notifications.get(),
    });

    useEffect(() => {
        eventSource.onerror = (event) => {
            console.debug("Unsubscribed from notifications:", event);
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
                        "Subscribed to notifications:",
                        event.data.subscriptionId,
                    );
                    props.onSubscribed(event.data.subscriptionId);
                },
                options: {
                    once: true,
                },
            },
        },
        [eventSource, props],
    );

    useEventSourceListener<T>(
        {
            source: eventSource,
            startOnInit: true,
            event: {
                name: "fetch-data",
                listener: (event) => {
                    console.debug(
                        `Received event of type 'fetch-data':`,
                        event,
                    );
                    props.onDataEvent(event.data);
                },
            },
        },
        [eventSource, props],
    );

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
                    props.onStatusEvent(event.data);
                },
            },
        },
        [eventSource, props],
    );

    return <Spinner size={"sm"} className={"ms-2"} />;
};
