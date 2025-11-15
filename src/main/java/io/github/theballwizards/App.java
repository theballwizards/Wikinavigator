package io.github.theballwizards;

import edu.princeton.cs.algs4.Digraph;

public class App {
    private static Digraph graph;

    public static void main(String[] args) {
        graph = GraphBuilder.build();
    }

    /**
     * Finds a path of urls between two articles through a wiki.
     * Articles must have the same web domain.
     * Warning, this may take a while.
     * @param graph A directional graph containing every url on the site
     * @param startUrl The starting position
     * @param endUrl The target position
     * @return An iterable of urls, starting at the start, and ending at the end.
     */
    private static Iterable<String> findPath(String startUrl, String endUrl) {
        return null; // TODO
    }
}