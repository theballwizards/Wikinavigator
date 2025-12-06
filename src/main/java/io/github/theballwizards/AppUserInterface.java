package io.github.theballwizards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;

public class AppUserInterface extends JFrame {
    private BiFunction<String, String, Iterable<String>> searchCallback;
    private DefaultListModel outputList;

    final JTextField startUrlField;
    final JComboBox<String> endUrlField;

    public AppUserInterface() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800, 800);

        final var main = new JPanel(new GridBagLayout());
        main.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 100, 5, 100);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int gridy = 0;

        final var title = new JLabel("Wikinavigator");
        gbc.gridx = 0; gbc.gridy = gridy; gbc.gridwidth = 2;
        gbc.weightx = 1;
        main.add(title, gbc);
        gbc.gridwidth = 1;

        gridy ++;

        final var desc = new JLabel("Find the shortest path between two wikipedia articles");
        gbc.gridx = 0; gbc.gridy = gridy; gbc.gridwidth = 2;
        gbc.weightx = 1;
        main.add(desc, gbc);
        gbc.gridwidth = 1;

        gridy ++;

        gbc.gridx = 0; gbc.gridy = gridy; gbc.gridwidth = 2;
        gbc.weightx = 1;
        main.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        gridy ++;

        final var startLabel = new JLabel("Start:");
        gbc.gridx = 0; gbc.gridy = gridy; gbc.weightx = 0;
        main.add(startLabel, gbc);

        startUrlField = new JTextField();
        gbc.gridx = 1; gbc.gridy = gridy; gbc.weightx = 1;
        main.add(startUrlField, gbc);

        gridy ++;

        final var endLabel = new JLabel("End:");
        gbc.gridx = 0; gbc.gridy = gridy; gbc.weightx = 0;
        main.add(endLabel, gbc);

        endUrlField = new JComboBox<>();
        endUrlField.setEditable(true);

        gbc.gridx = 1; gbc.gridy = gridy; gbc.weightx = 1;
        main.add(endUrlField, gbc);

        gridy ++;

        final var btn = new JButton("Go");
        gbc.gridx = 0; gbc.gridy = gridy; gbc.gridwidth = 2;
        gbc.weightx = 1;
        main.add(btn, gbc);
        gbc.gridwidth = 1;

        gridy++;

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputList.removeAllElements();
                for (final String s : searchCallback.apply(startUrlField.getText(), "")) {
                    outputList.addElement(s);
                }
            }
        });

        outputList = new DefaultListModel<String>();

        final var list = new JList<String>(outputList);
        final var scrollPane = new JScrollPane(list);

        gbc.gridx = 0; gbc.gridy = gridy;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        main.add(scrollPane, gbc);

        add(main);
        setVisible(true);
    }

    /**
     * Sets the function to run when clicking the go button
     * @param searchCallback must take in String startUrl, String endUrl, and give a Iterable<String>
     * @return this
     */
    public AppUserInterface setSearchCallback(BiFunction<String, String, Iterable<String>> searchCallback) {
        this.searchCallback = searchCallback;
        return this;
    }

    public void setInputOptions(Iterable<String> inputOptions) {
        for (final var s : inputOptions) endUrlField.addItem(s);
    }
}
