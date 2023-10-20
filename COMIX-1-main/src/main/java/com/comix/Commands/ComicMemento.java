package com.comix.Commands;

public class ComicMemento {
    private int comicID;
    private String title;
    private String series;
    private String issue;
    private String publisher;
    private String publicationDate;
    private String creators;
    private double baseValue;
    private int grade;
    private boolean isSlabbed;

    public ComicMemento(int comicID, String title, String series, String issue, String publicationDate, String creators,
                        String publisher, double value, int grade, boolean slabbed) {
        this.comicID = comicID;
        this.title = title;
        this.series = series;
        this.issue = issue;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.creators = creators;
        this.grade = grade;
        this.baseValue = value;
        this.isSlabbed = slabbed;
    }

    public int getComicID() {
        return comicID;
    }

    public String getTitle() {
        return title;
    }

    public String getSeries() {
        return series;
    }

    public String getIssue() {
        return issue;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getCreators() {
        return creators;
    }

    public double getBaseValue() {
        return baseValue;
    }

    public int getGrade() {
        return grade;
    }

    public boolean isSlabbed() {
        return isSlabbed;
    }
}
