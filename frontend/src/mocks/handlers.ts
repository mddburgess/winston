import {http, HttpResponse} from "msw";

export const handlers = [

    http.get('/api/channels', () => {
        return HttpResponse.json([
            {
                id: "channel.1",
                title: "channel.1.title",
                description: "channel.1.description",
                customUrl: "@channel1url",
                thumbnailUrl: "/api/channels/1/thumbnail",
                topics: [
                    "https://en.wikipedia.org/wiki/Topic",
                ],
                keywords: [
                    "keyword"
                ],
                videoCount: 1,
                publishedAt: "2025-01-01T00:00:00Z",
                lastFetchedAt: "2025-01-02T00:00:00.000000Z"
            }
        ])
    })
]
