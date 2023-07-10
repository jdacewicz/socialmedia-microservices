package pl.jdacewicz.sharingservice.model;

public record ApiError(int status,
                       String message) {
}
