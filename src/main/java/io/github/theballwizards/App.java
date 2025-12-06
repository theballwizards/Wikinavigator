package io.github.theballwizards;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.util.function.BiFunction;

public class App {
    private static Digraph graph;
    static AppUserInterface ui;

    public static void main(String[] args) {
        ui = new AppUserInterface()
                .setSearchCallback(App::findPath)
                .setGetRandomArticle(() -> {return "Dogs";});

        ui.setInputOptions(getInputOptions());
    }

    /**
     * Finds a path of urls between two articles through a wiki.
     * Articles must have the same web domain.
     * Warning, this may take a while.
     * @param startUrl The starting position
     * @param endUrl The target position
     * @return An iterable of urls, starting at the start, and ending at the end.
     */
    private static Iterable<String> findPath(String startUrl, String endUrl) {
        final var q = new Queue<String>();
        q.enqueue(startUrl);
        q.enqueue("ff");
        q.enqueue(endUrl);
        return q;
    }

    private static Iterable<String> getInputOptions() {
        final var q = new Queue<String>();
        q.enqueue("fewfwe");
        q.enqueue("ff");
        q.enqueue("fewiowfe");
        return q;
    }
}