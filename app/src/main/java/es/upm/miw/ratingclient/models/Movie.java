package es.upm.miw.ratingclient.models;

import java.io.Serializable;

/**
 * Created by franlopez on 14/11/2016.
 */

public class Movie implements Serializable {
    private int movieID;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private String title;
    private String originalTitle;
    private int voteCount;
    private double voteAverage;

    public Movie(int movieID, String posterPath, String overview, String releaseDate, String title,
                 String originalTitle, int voteCount, double voteAverage, Long createdAt) {
        this.setMovieID(movieID);
        this.setPosterPath(posterPath);
        this.setOverview(overview);
        this.setReleaseDate(releaseDate);
        this.setTitle(title);
        this.setOriginalTitle(originalTitle);
        this.setVoteCount(voteCount);
        this.setVoteAverage(voteAverage);
    }

    public int getMovieID() {
        return movieID;
    }

    private void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getPosterPath() {
        return posterPath;
    }

    private void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    private void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    private void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    private void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public int getVoteCount() {
        return voteCount;
    }

    private void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    private void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }
}
