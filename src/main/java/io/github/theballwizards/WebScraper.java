package io.github.theballwizards;

public class WebScraper {
    public static final int MAX_SEARCH_DEPTH = 12;
    public static final String START_LINK = "TODO";
    public static final String STARTING_PAGE = "TODO";

    /**
     * Generates an edge list of urls by crawling a wiki for all its articles.
     * @param url The url to start crawling from.
     * @return An edge list of urls.
     */
    public static String scrapeEdgeListOfUrls() {
        StringBuilder builder = new StringBuilder();
        scrapeEdgeListOfUrlsFromSite(builder, STARTING_PAGE, MAX_SEARCH_DEPTH);
        return builder.toString();
    }

    /**
     * Generates an edge list of urls by crawling a wiki for all its articles.
     * This method is called recursively.
     * @param builder A string builder that the method will append lines to.
     * @param url The url to scape for other urls.
     * @param depth Base case, when to stop crawling.
     */
    private static void scrapeEdgeListOfUrlsFromSite(StringBuilder builder, String url, int depth) {
        //TODO
    }
}
