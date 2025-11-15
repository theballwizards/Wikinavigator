package io.github.theballwizards;

public class AppUserInterface {
    private Runnable searchCallback;

    public AppUserInterface() {

    }

    /**
     * Sets the search callback member.
     * Uses the builder pattern.
     * @param callback Function to run when the go button is pressed.
     * @return instance of caller.
     */
    public AppUserInterface setSearchCallback(Runnable callback) {
        searchCallback = callback;
        return this;
    }
}
