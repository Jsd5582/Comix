package com.comix.model;

import com.comix.Commands.ComicMemento;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a comic, holds all the comic attributes
 */
public class Comic {
    private int comicID;
    private String title;
    private String series;
    private String issue;
    private String publisher;
    private String publicationDate;
    private String creators;
    private double value;
    private double baseValue;
    private int grade;
    private boolean isSlabbed;

    public Comic(int comicID, String title, String series, String issue, String publicationDate, String creators,
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
        calculateValue();
    }

    public Comic(int comicID, String title, String series, String issue, String publicationDate, String creators,
            String publisher) {
        this(comicID, title, series, issue, publicationDate, creators, publisher, 0, 0, false);
    }

    public Comic(String title, String series, String issue, String publicationDate, String creators, String publisher,
                 double value) {
        this(-1, title, series, issue, publicationDate, creators, publisher, value, 0, false);
    }

    public Comic(ComicMemento memento) {
        setMemento(memento);
    }

    private void calculateValue() {
        double tempValue = baseValue;
        if (grade == 1) {
            tempValue *= 1.10;
        } else if (grade > 1) {
            tempValue *= Math.log10(grade);
        }
        if (isSlabbed) {
            tempValue *= 2;
        }
        value = Comic.round(tempValue, 2);
    }

    /***
     * Retrieves the ID of the comic
     * @return ComicID
     */
    public int getComicID() {
        return comicID;
    }

    /***
     * Retrieves the title of a comic book
     * 
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /***
     * Retrieves the series of a comic book
     * 
     * @return The series
     */
    public String getSeries() {
        return series;
    }

    /***
     * Retrieves the issue of a comic book
     * 
     * @return The issue
     */
    public String getIssue() {
        return issue;
    }

    /***
     * Retrieves the publication date of a comic book
     * 
     * @return The publication date
     */
    public String getPublicationDate() {
        return publicationDate;
    }

    /**
     * Retrieves the creators of a comic book
     * 
     * @return The creators
     */
    public String getCreators() {
        return creators;
    }

    /***
     * Retrieves the publisher(s) of a comic book
     * 
     * @return The publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /***
     * Retrieves the value of a comic book
     * 
     * @return The value
     */
    public double getValue() {
        return value;
    }

    public double getBaseValue() {
        return baseValue;
    }

    public void setComicID(int comicID) {
        this.comicID = comicID;
    }

    /***
     * Sets/updates the value of a specific comic book
     * 
     * @param newValue the new value
     */
    public void setBaseValue(double newValue) {
        this.baseValue = Comic.round(newValue, 2);
        calculateValue();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setCreators(String creators) {
        this.creators = creators;
    }

    /***
     * Retrieves the slabbed status of a comic book
     * 
     * @return whether a comic is slabbed or not
     */
    public boolean getIsSlabbed() {
        return isSlabbed;
    }

    /**
     * Slabs a specific comic
     */
    public void slabComic() {
        isSlabbed = true;
        calculateValue();
    }

    /**
     * Retrieves the grade of a comic book
     * 
     * @return The grade
     */
    public int getGrade() {
        return grade;
    }

    /***
     * Sets/updates the grade of a specific comic book
     * 
     * @param newGrade The new grade
     */
    public void setGrade(int newGrade) {
        grade = newGrade;
        calculateValue();
    }

    /***
     * Strings the fields of a comic together to show all fields
     * 
     * @return The comic book as a string
     */
    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s",
                title,
                series,
                issue,
                publisher,
                publicationDate,
                creators,
                grade,
                isSlabbed,
                comicID);
    }

    /**
     * Rounds values to the second decimal place.
     * Code is from https://www.baeldung.com/java-round-decimal-number
     * 
     * @param value  the uneditted double value
     * @param places how many decimal places to round to
     * @return the editted double value rounded to two decimal places
     */
    private static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public ComicMemento createMemento() {
        return new ComicMemento(comicID, title, series, issue, publicationDate, creators, publisher, baseValue,
                grade, isSlabbed);
    }

    public void setMemento(ComicMemento memento) {
        comicID = memento.getComicID();
        title = memento.getTitle();
        series = memento.getSeries();
        issue = memento.getIssue();
        publicationDate = memento.getPublicationDate();
        creators = memento.getCreators();
        publisher = memento.getPublisher();
        baseValue = memento.getBaseValue();
        grade = memento.getGrade();
        isSlabbed = memento.isSlabbed();
    }
}
