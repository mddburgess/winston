import {useEventSource, useEventSourceListener} from "react-sse-hooks";
import {useEffect} from "react";
import {SubscriptionEvent} from "../model/events/SubscriptionEvent";
import {Spinner} from "react-bootstrap";

type NotificationsSourceProps = {
    onSubscribed: (subscriptionId: string) => void,
    eventName: string,
    onEvent: (event: any) => void,
}

export const NotificationsSource = (props: NotificationsSourceProps) => {

    const eventSource = useEventSource({ source: `/api/notifications` });

    useEffect(() => {
        eventSource.onerror = (event) => {
            eventSource.close();
        }
    }, [eventSource]);

    useEventSourceListener<SubscriptionEvent>({
        source: eventSource,
        startOnInit: true,
        event: {
            name: "message",
            listener: (event) => {
                props.onSubscribed(event.data.subscriptionId);
            },
            options: {
                once: true
            }
        },
    }, [eventSource, props]);

    useEventSourceListener<Parameters<typeof props.onEvent>[0]>({
        source: eventSource,
        startOnInit: true,
        event: {
            name: props.eventName,
            listener: event => {
                props.onEvent(event.data)
            }
        }
    }, [eventSource, props]);

    return (
        <Spinner size={"sm"} className={"ms-2"}/>
    );
}
