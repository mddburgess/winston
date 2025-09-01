const baseUrl = "https://en.wikipedia.org/wiki/";

/**
 * Extracts the topic name from a topic category URL.
 *
 * Topic categories are URLs to Wikipedia pages. This method extracts the topic name by removing the Wikipedia base URL
 * from the topic category, and replacing the remaining underscores with spaces.
 *
 * @param topic a topic category URL
 * @returns the topic name
 */
const getTopicFromUrl = (topic: string) => {
  return topic.slice(baseUrl.length).replace(/_/g, " ");
};

export { getTopicFromUrl };
