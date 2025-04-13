import {useEventSource, useEventSourceListener} from "react-sse-hooks";
import {useEffect} from "react";
import {SubscriptionEvent} from "../model/events/SubscriptionEvent";
import {Spinner} from "react-bootstrap";
import {FetchStatusEvent} from "../model/events/FetchEvent";

type NotificationsSourceProps = {
    onSubscribed: (subscriptionId: string) => void,
    onDataEvent: (dataEvent: any) => void,
    onStatusEvent: (statusEvent: FetchStatusEvent) => void,
}

export const NotificationsSource = (props: NotificationsSourceProps) => {

    const eventSource = useEventSource({ source: `/api/notifications` });

    useEffect(() => {
        eventSource.onerror = (event) => {
            console.debug("Unsubscribed from notifications:", event);
            eventSource.close();
        }
    }, [eventSource]);

    useEventSourceListener<SubscriptionEvent>({
        source: eventSource,
        startOnInit: true,
        event: {
            name: "message",
            listener: (event) => {
                console.debug("Subscribed to notifications:", event.data.subscriptionId);
                props.onSubscribed(event.data.subscriptionId);
            },
            options: {
                once: true
            }
        },
    }, [eventSource, props]);

    useEventSourceListener<Parameters<typeof props.onDataEvent>[0]>({
        source: eventSource,
        startOnInit: true,
        event: {
            name: "fetch-data",
            listener: event => {
                console.debug(`Received event of type 'fetch-data':`, event);
                props.onDataEvent(event.data)
            }
        }
    }, [eventSource, props]);

    useEventSourceListener<FetchStatusEvent>({
        source: eventSource,
        startOnInit: true,
        event: {
            name: "fetch-status",
            listener: event => {
                console.debug(`Received event of type 'fetch-status':`, event);
                props.onStatusEvent(event.data)
            }
        }
    }, [eventSource, props]);

    return (
        <Spinner size={"sm"} className={"ms-2"}/>
    );
}
