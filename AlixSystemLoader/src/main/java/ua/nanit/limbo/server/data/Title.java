package ua.nanit.limbo.server.data;

public class Title {

    private String title;
    private String subtitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public Title() {
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public Title setTitle(String title) {
        this.title = title;
        return this;
    }

    public Title setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public Title setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
        return this;
    }

    public Title setStay(int stay) {
        this.stay = stay;
        return this;
    }

    public Title setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
        return this;
    }
}
