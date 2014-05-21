package de.prochnow.instaScraper;

public class Tag extends VisualObject implements Comparable<Tag> {

    public String name = "";

    public Tag(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object obj) {
        return (obj instanceof Tag) && this.name.equals(((Tag)obj).name);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(final Tag o) {
        return this.name.compareTo(o.name);
    }

}
