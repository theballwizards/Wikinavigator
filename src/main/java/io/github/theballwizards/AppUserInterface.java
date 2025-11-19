package io.github.theballwizards;

import javax.swing.*;
import java.awt.*;

public class AppUserInterface extends JFrame {
    private Runnable searchCallback;

    public AppUserInterface() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800, 800);

        final var main = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 100, 5, 100);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        final var startLabel = new JLabel("Start:");
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        main.add(startLabel, gbc);

        final var startUrlField = new JTextField();
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        main.add(startUrlField, gbc);

        final var endLabel = new JLabel("End:");
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        main.add(endLabel, gbc);

        final var endUrlField = new JTextField();
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        main.add(endUrlField, gbc);

        final var btn = new JButton("Go");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.weightx = 1;
        main.add(btn, gbc);
        gbc.gridwidth = 1;

        final var model = new DefaultListModel<String>();
        model.addElement("ITEM 1");
        model.addElement("ITEM 2");
        model.addElement("ITEM 3");

        final var list = new JList<String>(model);
        final var scrollPane = new JScrollPane(list);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        main.add(scrollPane, gbc);

        add(main);
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
