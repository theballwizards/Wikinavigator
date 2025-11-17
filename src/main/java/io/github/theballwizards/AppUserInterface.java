package io.github.theballwizards;

import javax.swing.*;

public class AppUserInterface extends JFrame {
    private Runnable searchCallback;

    private JPanel main;
    private JTextField startUrlField;
    private JTextField endUrlField;
    private JButton Go;
    private JLabel startLabel;
    private JLabel endLabel;
    private JPanel form;
    private JScrollPane result;

    public AppUserInterface() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(main);
        setSize(500, 500);
        setVisible(true);
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
