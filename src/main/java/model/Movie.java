package model;

import jakarta.persistence.*;

/** This class is the model that will be used throughout the application.
 *  @Entity represents that this class is a JPA Entity.
 *  @Table represents how the entity will look in the table.
 *  @Id represents that this field is the primary-key, at least on of the fields MUST be annotated with @Id.
 *  @GeneratedValue generates a value depending on the "strategy".
 *  @Column represents mapping to a specific column, in this case it´s for customizing the column name etc. Otherwise, it is optional.
 *  Six attributes that is needed for this specific project, made private so it cant be changed by a user after initiation.
 *  One empty constructor
 *  get and set methods for each attribute.
 */
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    private String title;
    private String genre;
    private int releaseYear;
    private String summary;
    private String director;

    public Movie() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
