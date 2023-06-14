package pl.jdacewicz.postservice.model;

public record ApiError(int status,
                       String message) {
}
