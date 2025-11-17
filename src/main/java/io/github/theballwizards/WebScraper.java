package io.github.theballwizards;

import edu.princeton.cs.algs4.SET;
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
    // As The Difference Between A Search Depth Of 1, and A Search Depth Of 2 is 200:100,000 Links
    // FIXME: Currently Unimplemented, Set To Large Value To Avoid Data Loss And Give The Largest Possible Graph
    public static final int MAX_LINKS_PER_PAGE = 999_999;

    // The Depth Is How Many Links Deep To Search
    private static final int MAX_SEARCH_DEPTH = 1;
    // The Starting Page Will Be The Where The Webscraper Will Start
    private static String STARTING_PAGE = "C_(programming_language)";
    // The Parent Domain Must Be A Shared Domain Between All Of The Children's Pages To Work Correctly
    private static String PARENT_DOMAIN = "https://en.wikipedia.org/wiki/";


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

    /**
     * Generates an edge list of urls by crawling a wiki for all its articles.
     * @return An edge list of urls.
     */
    public static String scrapeEdgeListOfUrls() {
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
    private static void scrapeEdgeListOfUrlsFromSite(StringBuilder stringBuilder, String relativeURL, int depth) {
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
            }

            // Go Through All Linked Pages And Recursively Add More Links From The Given Link
            for (String wikiPage : linkedPages) {
                stringBuilder.append(String.format("\"%s\" -> \"%s\"\n",relativeURL, wikiPage));
                scrapeEdgeListOfUrlsFromSite(stringBuilder, wikiPage, depth+1);
            }

        } catch (Exception e) {
            System.err.println("Error Reading Site: " + e.getMessage());
        }
    }

}

