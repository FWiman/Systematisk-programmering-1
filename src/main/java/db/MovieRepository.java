package db;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import model.Movie;

import java.util.List;

/** This Class is the class that handles the database. With the CRUD operations the repo takes care of anything db related.
 * @ApplicationScoped This annotation specifies that the MovieRepository lifecycle is active as long as the application runs.
 * @Transactional This annotation in this case means that all methods in this class are transactional,
 * which means that the methods will be executed within a database transaction.
 * U could put it on specific methods if u don't want all methods transactional.
 * @PersistenceContext The injection of the EntityManager that handles the transactions.
 * This class has a single attribute which is the EntityManager, It means that each repo has a EntityManager.
 */
@ApplicationScoped
@Transactional
public class MovieRepository {
    @PersistenceContext
    private EntityManager entityManager;


    /** This method finds all movies in the table movies.
     * I am creating a String called sql and using the query language to select all movies in the table movies.
     * Then I send it in with the entityManager method .createNativeQuery(),
     *  after that I cast the ResultList as a List of movies and return it. Could probably be done better!
     * @return List<Movie> A list of the movies in the table movies.
     */
    public List<Movie> findMovies(){
        String sql = "SELECT * FROM movies";
        Query query = entityManager.createNativeQuery(sql);

        return (List<Movie>) query.getResultList();
    }


    /** This method finds a movie based on title, I was thinking like a "search method" as a user.
     * @param partialTitle The param that is used in the sql query to find all movies with that partialTitle.
     * In this method I create a String named "sql" which is the query that is sent in with the .createQuery() method.
     * The query is selecting m(the movies) where m.title is matching all movies that is LIKE(contains) that partial title.
     * If the resultList is empty I throw a new WebApplicationException with the Response NOT_FOUND(404).
     * Otherwise, I create a List "resultList" and sets the "resultList" with the actual result from the query.
     * @return List<Movie> A list of Movies.
     */
    public List<Movie> findMovieByTitle(String partialTitle) {
        String sql = "SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :partialTitle, '%'))";
        try {

            Query query = (Query) entityManager.createQuery(sql).setParameter("partialTitle", partialTitle).getResultList();

            if(query.getResultList().isEmpty()){
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            List<Movie> resultList = query.getResultList();
            return resultList;

        } catch (NoResultException e) {
           throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    /** This method finds a movie based on ID
     * @param id is is used in the query for finding the movie with the correct ID.
     * Here I create a String named "sql" with the query that is to be used with the entityManager.createQuery(),
     * I am selecting m(the movie) from the table movie where m(the movie).id is equal to the ID in the param.
     * If it exists I getSingleResult() of the movie that I am looking for, otherwise I throw a new WebApplicationException
     * with the status code NOT_FOUND(404)
     * @return Movie that is found by with that ID.
     */
    public Movie findMovieById(long id) {
        String sql = "SELECT m FROM Movie m WHERE m.id = :id";
        try {
            return entityManager.createQuery(sql,Movie.class)
                    .setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }


    /** This method creates a movie.
     * @param movie that we send in to create.
     * In this method I only call the entityManager.persist() method and sending in that movie so it is created.
     */
    public void createMovie(Movie movie){
        entityManager.persist(movie);
    }

    /** This method updates a movie with new values.
     * @param movie A is sent in to update.
     * Here I just call the EM .merge() method to update the movie that is sent in.
     * @return The movie that is updated.
     */
    public Movie updateMovie(Movie movie){
        return entityManager.merge(movie);
    }

    /** This method deletes a movie.
     * @param id The ID is used to find the movie to delete.
     * In this method I call the EM method .find() first to find the movie with that ID,
     * after that I check if it is not NULL and if so I call the EM method .remove() to delete it.
     */
    public void deleteMovie(long id){
        Movie movieToDelete = entityManager.find(Movie.class, id);
        if(movieToDelete != null) {
            entityManager.remove(movieToDelete);
        }
    }

}
