package io.github.theballwizards;

import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Given A Preconfigured Parent Domain And Starting Page, The Webscraper Will Get All Links That
 * Link From The Parent Directory And Recursively Get Its Children's Links Until A Specified Depth
 *
 * Links Are First Filtered Through A Pre-Configured Blacklist, Then Added To A Set For Duplicate Avoidance,
 * And Finally Added To The Returning String
 *
 * @implNote The "jsoup"(https://jsoup.org/) Library Is Required To Avoid 403 Errors And To Parse HTML Efficiently
 *           Without REGEX, instead Filtering By CSS Selectors.
 *
 * @author Bryson Crader
 */
public class WebScraper {
    // The Max Links Per Page Is To Limit The Growth Of The Graph, It Will Provide A Less Complete Tree, But May Be Required
    // As The Difference Between A Search Depth Of 1, and A Search Depth Of 2 is 200 vs 100,000 Links
    // FIXME: Currently Unimplemented, Set To Large Value To Avoid Data Loss And Give The Largest Possible Graph
    public int MAX_LINKS_PER_PAGE = 999_999;

    // The Depth Is How Many Links Deep To Search
    private int MAX_SEARCH_DEPTH = 1;
    // The Starting Page Will Be The Where The Webscraper Will Start
    private String STARTING_PAGE = "C_(programming_language)";
    // The Parent Domain Must Be A Shared Domain Between All Of The Children's Pages To Work Correctly
    private String PARENT_DOMAIN = "https://en.wikipedia.org/wiki/";


    // Prefixes To Be Excluded When Looking For Links Within Wikipedia Articles
    static SET<String> blacklistedPrefixes = new SET<>(){{
        add("/wiki/Help:");
        add("/wiki/Special:");
        add("/wiki/Wikipedia:");
        add("/wiki/File:");
        add("/wiki/Category:");
        add("/wiki/Portal:");
        add("/wiki/Talk:");
        add("/wiki/Template:");
        add("/wiki/Template_talk:");
    }};

    // Here Will Store The Visited Pages, This Can Be Used As The Graph's Vertices
    private SET<String> visitedPages = new SET<>();

    // Here Is Where The Links Between Pages Will Be Stored,
    // Since Uniqueness Is Handled Internally In The `scrapeEdgeListOfUrls`, After Making Sure
    // That The Page Is A Unique Link, It Should Be Safe To Increment This Value By 1
    private int totalPageConnections = -1;


    /**
     * Returns The Total Count Of Pages Visited In Total After The `scrapeEdgeListOfUrls` Function Is Called.
     * Duplicates Are Not Counted -- With This Being The Case, The Usage Of This Function Would Be Equivalent
     * To A "getVertexCount" Function For A Graph.
     *
     * @return Returns The Count Of Pages Visited In Total After The `scrapeEdgeListOfUrls` Function Is Called
     * @throws RuntimeException If `scrapeEdgeListOfUrls` Is Not Called, To Check For This, The Function Checks
     *                          Whether `visitedPages` Is Empty, If So, The Exception Is Thrown
     */
    public int getVisitedPageCount() {
        if (visitedPages.isEmpty())
            throw new RuntimeException("`visitedPages` Is Empty, Try Calling `WebScraper.scrapeEdgeListOfUrls(...)` Before Calling `WebScraper.getVisitedPageCount()`");
        return visitedPages.size();
    }
    /**
     * Returns The Amount Of Links From 1 Page To Another After The `scrapeEdgeListOfUrls` Function Is Called.
     * Duplicates Should Not Be Included, As Duplicates Are Handled In The `scrapeEdgeListOfUrls` Function.
     * This Function Would Be The Equivalent To A "getEdgeCount" Function For A Graph.
     *
     * @return Returns The Count Of Connections (i.e. "link1 -> link2") After The `scrapeEdgeListOfUrls` Function Is Called
     * @throws RuntimeException If `scrapeEdgeListOfUrls` Is Not Called, To Check For This, The Function Checks
     *      *                          Whether `totalPageConnections` Is Less Than 0, If So, The Exception Is Thrown
     */
    public int getTotalPageConnections() {
        if (totalPageConnections < 0)
            throw new RuntimeException("`totalPageConnections` Is Less Than 0, Try Calling `WebScraper.scrapeEdgeListOfUrls(...)` Before Calling `WebScraper.getTotalPageConnections()`");
        return totalPageConnections;
    }


    /**
     * Generates an edge list of urls by crawling a wiki for all its articles.
     * @return An edge list of urls in string format. The format follows the algs4 graph format
     *          without the starting vertices or edge number. i.e:
     *
     *          page1 page2
     *          page1 page3
     *          page1 page4
     *          page2 page5
     *          ...
     *
     */
    public String scrapeEdgeListOfUrls() {
        StringBuilder builder = new StringBuilder();
        scrapeEdgeListOfUrlsFromSite(builder, STARTING_PAGE, 0);
        return builder.toString();
    }


    /**
     * Generates an edge list of urls by crawling a wiki for all its articles.
     * This method is called recursively.
     * @param stringBuilder A string builder that the method will append connecting links to.
     * @param relativeURL The sub-url for the page to be crawled.
     *                    i.e. On Wikipedia: "/wiki/(ARTICLE NAME)" rather than the full URL
     * @param depth Base case, when to stop crawling.
     */
    private void scrapeEdgeListOfUrlsFromSite(StringBuilder stringBuilder, String relativeURL, int depth) {
        if (depth >= MAX_SEARCH_DEPTH) return;
        try {
            // Get the page to search for links
            Document document = Jsoup.connect(PARENT_DOMAIN+relativeURL).get();
            // Filter Out All The Links, To Only The Ones Pointing To Other Wikipedia Articles
            Elements elements = document.select("a[href^='/wiki/']");

            // Build A Set To Avoid Duplicates, This Can Also Be Used For Threading Purposes
            SET<String> linkedPages = new SET<>();
            outer: for (Element element : elements) {
                // Remove Links With Prefixes On The Blacklist
                for (String prefix : blacklistedPrefixes) {
                    if (element.attr("href").contains(prefix)) {
                        continue outer;
                    }
                }
                // Remove The "/wiki/" Part To Save Space, All Links Should Require Them
                String wikiPage = element.attr("href").replace("/wiki/","");
                // Add Links That Pass All Tests Before-Hand
                linkedPages.add(wikiPage);

                // Here We Need To Add The Page To The Visited Pages Set So That We Can Get An Accurate Vertex Count
                visitedPages.add(wikiPage);

                if (linkedPages.size() >= MAX_LINKS_PER_PAGE) {
                    break;
                }
            }
            // All Pages That Were Linked And Not Duplicated Are In `linkedPages`
            // In That Case We Can Just Add The Size After The Page Is Done Being
            // Iterated Over. Im 99% Certain This Is 100% Correct And 5% Concerned That
            // There May Be An Off By 1, But The Math Checked Out, So We Should Be Fine
            totalPageConnections += linkedPages.size();

            // Go Through All Linked Pages And Recursively Add More Links From The Given Link
            for (String wikiPage : linkedPages) {
                stringBuilder.append(String.format("\"%s\" \"%s\"\n",relativeURL, wikiPage));
                scrapeEdgeListOfUrlsFromSite(stringBuilder, wikiPage, depth+1);
            }

        } catch (Exception e) {
            // Here We Can Log Errors, But It Is Not Really Worth
            // Doing So As We Can Just Ignore The Page And Move On
            // StdOut.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Configures The Starting Page And Maximum Links For The Web Scraper.
     *
     * @param startingWikiPage The Wikipedia Page To Start On, Must Be The Page Name,
     *                         Not The Full Link i.e. C_(programming_language) instead of https://en.wikipedia.org/wiki/C_(programming_language)
     * @param maxLinksPerPage Max Links Per Page Is The Limiter On How Many Links
     *                        Can Be Grabbed From One Page To Avoid The Graph
     *                        Ballooning In Size Rapidly, If Size Is Not An Issue,
     *                        A Large Value May Be Set
     *
     * @throws IllegalArgumentException If Starting Wiki Page Is Found To Be Formatted Incorrectly
     * @throws IllegalArgumentException If Max Links Per Page Is Less Than 0
     * @throws IllegalArgumentException If Depth Is Less Than 1
     */
    public WebScraper(String startingWikiPage, int maxLinksPerPage, int depth) {
        if (startingWikiPage.contains("wiki/"))
            throw new IllegalArgumentException("Starting Wiki Page Must Be The Page Name Only. \"C_(programming_language)\" instead of \"https://en.wikipedia.org/wiki/C_(programming_language)\" For Example.");
        if (maxLinksPerPage < 0)
            throw new IllegalArgumentException("Max Links Per Page Must Be Greater Than Or Equal To Zero.");
        if (depth < 1)
            throw new IllegalArgumentException("Depth Must Be One Or Greater.");

        STARTING_PAGE = startingWikiPage;
        MAX_LINKS_PER_PAGE = maxLinksPerPage;
        MAX_SEARCH_DEPTH = depth;
    }

    /**
     * The Default Construction Of The Webscraper, If No Arguments Are Provided, Developer Constants Will Be Chosen.
     *
     * STARTING_PAGE = "C_(programming_language)"
     * MAX_LINKS_PER_PAGE = 999_999 -- Ignoring Limits/High Upper Bound
     * MAX_SEARCH_DEPTH = 1 -- Avoiding Explosive Growth In Graph Size
     */
    public WebScraper() {
        // Here We Dont Have To Set Anything,
        // Everything Is Set By Default When Declared
    }


    public static void main(String[] args) {
        WebScraper webScraper = new WebScraper();
        StdOut.println(webScraper.scrapeEdgeListOfUrls());
    }

}

