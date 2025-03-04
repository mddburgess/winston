import {Container, ListGroup} from "react-bootstrap";
import {EventSourceProvider, useEventSource, useEventSourceListener} from "react-sse-hooks";
import {useEffect, useState} from "react";


const TimeList = () => {

    const [times, setTimes] = useState<string[]>([])

    const eventSource = useEventSource({
        source: '/api/stream'
    })

    useEventSourceListener<string>({
        source: eventSource,
        startOnInit: true,
        event: {
            name: "message",
            listener: e => {
                setTimes(it => [...it, JSON.stringify(e.data)])
                if (times.length >= 9) {
                    eventSource.close();
                }
            }
        }
    }, [eventSource, times])




    return (
        <ListGroup>
            {times.map((time, index) => (
                <ListGroup.Item key={index}>{time}</ListGroup.Item>
            ))}
        </ListGroup>
    )
}

export const Times = () => {

    return (
        <EventSourceProvider>
            <TimeList/>
        </EventSourceProvider>
    )
    // const renderedResults = allResults.map(item => (
    //     <ListGroup.Item key={item}>{item}</ListGroup.Item>
    // ))
    //
    // return (
    //     <Container>
    //         <ListGroup>
    //             {renderedResults}
    //         </ListGroup>
    //     </Container>
    // )
}
