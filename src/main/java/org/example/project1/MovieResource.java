package org.example.project1;

import db.MovieRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import model.Movie;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/** This class is the Resource class. It is responsible for the HTTP requests and handles the communication between client and the backend.
 *  In this class I have methods with GET,PUT,POST and DELETE annotations that decides what kind of request it is.
 *  I also have other annotations that will be explained in each method comment.
 * @Path is the default path for all the resources in MovieResource.
 * @Inject makes the creation automatic for the initiation of movieRepository.
 */
@Path("/movies")
public class MovieResource {
    @Inject
    private MovieRepository movieRepository;


    /** This method gets all movies by communicating with movieRepository and calling a method from the repo.
     * @GET is used in this method because I want to fetch data and display it.
     * @Produces is annotated because I am fetching and showing data and not sending data in.
     * @return List<Movie> A list of movies.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getMovies(){
        return movieRepository.findMovies();
    }

    /** This method gets all movies based on if the partialTitle that is sent in matches a part of their title.
     * @GET is used in this method because I want to fetch data and display it.
     * @Path is used because we need to use a specific path in this case. I am using the partialTitle as a param for
     * the path so when the "user" is sending in their title that they are looking for it is used as a param.
     * @param partialTitle is the path param that is used.
     * I am creating a List called listOfMovie that is communicating with the repo and using the method .findMovieByTitle().
     * I am also using the method .build() which finalizes and creates the Response object that is being returned.
     * @return a Response with status code ok(200).
     */
    @GET
    @Path("/{partialTitle}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieByPartialTitle(@PathParam("partialTitle") String partialTitle){
        List<Movie> listOfMovie = movieRepository.findMovieByTitle(partialTitle);
        return Response.ok(listOfMovie).build();
    }


    /** This method posts a movie.
     * @POST is used because the request is posting something to the server.
     * @Consumes is used because I am sending data to the server.
     * @Produces is used to show the movie that is created.
     * @param movie is sent in into the method so when I am communicating with the repo we create a Movie.
     * @return is a Response with the status code created(201) I am sending a message with .entity() and
     * finalizes and creating the Response object to be returned.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addMovie(Movie movie){
        movieRepository.createMovie(movie);
        return Response.status(Response.Status.CREATED)
                .entity("Movie created")
                .build();
    }


    /** This method updates a movie with new data and saves it.
     * @PUT means we are sending data and replacing it with data that is already there.
     * @Consumes is used because I am sending data to the server.
     * @Produces is used to show the movie that is created.
     * @Path is used because we need a specific path with the id as param.
     * @param id is the param that is used to define which movie that is to be updated.
     * @param updatedMovie is also sent in with the values that will be used in the updated object.
     * @return a Response with either ok(200) or NOT_FOUND(404).
     * First I am finding the movie to be updated with the repo method .findMovieById().
     * If there is no movie with that specific ID I tell the user that it cant be found, otherwise I check each attribute
     * of the updatedMovie to see if it is NULL in which case I keep the existing attribute. That makes it easier for
     * the user to only send in the chosen amount of attributes that should be changed.
     * If I did not have these if-statements the "unfilled" attributes would be set to NULL.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response updateMovie(@PathParam("id") long id, Movie updatedMovie){
        Movie existingMovie = movieRepository.findMovieById(id);

        if(existingMovie == null ) {
            return Response.status(Response.Status.NOT_FOUND).entity("Movie to update not found").build();
        }

        if(updatedMovie.getTitle() != null) {
            existingMovie.setTitle(updatedMovie.getTitle());
        }
        if(updatedMovie.getDirector() != null) {
            existingMovie.setDirector(updatedMovie.getDirector());
        }
        if(updatedMovie.getGenre() != null) {
            existingMovie.setGenre(updatedMovie.getGenre());
        }
        if(updatedMovie.getReleaseYear() != -1) {
            existingMovie.setReleaseYear(updatedMovie.getReleaseYear());
        }
        if(updatedMovie.getSummary() != null) {
            existingMovie.setSummary(updatedMovie.getSummary());
        }

        Movie savedMovie = movieRepository.updateMovie(existingMovie);
        return Response.ok(savedMovie).build();
    }

    /** This method deletes a movie based on ID
     * @DELETE is used because the request is deleting a movie.
     * @Consumes is used because I am sending data to the server.
     * @Produces is used to show the movie that is created.
     * @Path is used because we need a specific path with the id as param.
     * @param id is used to specify what movie that is to be deleted.
     * @return a Response with either no content(204) or NOT_FOUND(404).
     * I create a movie of Movie that is using the ID to find the specific movie with that id,
     * then I check if it is NULL. If so we send the 404 Response, otherwise we call the repo method .deleteMovie().
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response deleteMovie(@PathParam("id") long id){
        Movie movie = movieRepository.findMovieById(id);
        if(movie == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Movie to delete not found").build();
        }
        movieRepository.deleteMovie(id);
        return Response.noContent().build();

    }
}
