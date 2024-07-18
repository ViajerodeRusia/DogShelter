package com.assistance.DogShelter.exceptions;

public class VolunteerNotFoundException extends RuntimeException {
    public VolunteerNotFoundException() {
    }

    public VolunteerNotFoundException(String message) {
        super(message);
    }
}
