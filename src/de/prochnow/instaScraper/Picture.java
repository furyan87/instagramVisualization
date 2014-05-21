package de.prochnow.instaScraper;

import java.net.MalformedURLException;


public class Picture extends VisualObject {

    public final static int DEFAULT_COLOR_COUNT = 3;

    public String url;

    public int colorCount = 0;

    public int[] colors;

    public String path = "";

    public Picture(final String url) throws MalformedURLException {
        this(url, DEFAULT_COLOR_COUNT);
    }

    public Picture(final String url, final int colorCount) throws MalformedURLException {
        this.url = url;
        this.colorCount = colorCount;
        this.colors = new int[colorCount];
    }

    public void extractColors() throws Exception {
        throw new UnsupportedOperationException("Method not implemented");
    }


}
