package io.github.theballwizards;

import edu.princeton.cs.algs4.StdOut;
import org.junit.Test;

import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;

public class WebScraperTest {

    WebScraper webScraper = new WebScraper();

    @Test
    public void webScraper_BadRelativeStartingPage() {
        assertThrows(IllegalArgumentException.class, () -> {
           new WebScraper("/wiki/C_(programming_language)", 999_999, 1);
        });
    }
    @Test
    public void webScraper_BadAbsoluteStartingPage() {
        assertThrows(IllegalArgumentException.class, () -> {
           new WebScraper("https://en.wikipedia.org/wiki/C_(programming_language)", 999_999, 1);
        });
    }
    @Test
    public void webScraper_InvalidMaxLinks() {
        assertThrows(IllegalArgumentException.class, () -> {
           new WebScraper("C_(programming_language)", -1, 1);
        });
    }
    @Test
    public void webScraper_InvalidDepth() {
        assertThrows(IllegalArgumentException.class, () -> {
           new WebScraper("C_(programming_language)", 999_999, 0);
        });
    }

    @Test
    public void webScrapper_GotLinks() {
        String results = webScraper.scrapeEdgeListOfUrls();
        assertEquals(true, results.split("\n").length > 10);
    }

    @Test
    public void webScrapper_WikipediaPageDoesNotExist() {
        WebScraper ws = new WebScraper("COOLEST_GROUP_IN_2420", 999_999, 1);
        assertEquals(0, ws.scrapeEdgeListOfUrls().length());
    }
    @Test
    public void webScrapper_ClampLinksOnSmallPage() {
        WebScraper ws = new WebScraper("Tatce", 5, 1);
        String results = ws.scrapeEdgeListOfUrls();
        assertEquals(true, results.split("\n").length <= 5);
    }
    @Test
    public void webScrapper_ClampLinksOnLargePage() {
        WebScraper ws = new WebScraper("C_(programming_language)", 20, 1);
        String results = ws.scrapeEdgeListOfUrls();
        assertEquals(true, results.split("\n").length <= 20);
    }

    @Test
    public void webScraper_DefaultInitializationOnCorrectPage() throws Exception {
        String results = webScraper.scrapeEdgeListOfUrls();
        // We Can Make Sure It Is Initialize On The C Programming Langauage Wikipedia By
        // Taking A Few Random Links And Making Sure They Are All There
        String[] expectedLinks = {
                "BASIC",
                "ALGOL",
                "Compiler",
                "FFmpeg",
                "Fortran"
        };
        boolean containsAll = ((Callable<Boolean>)() -> {
            for (var link : expectedLinks) {
                if (!results.contains(link)) return false;
            }
            return true;
        }).call();
        assertTrue(containsAll);
    }
}