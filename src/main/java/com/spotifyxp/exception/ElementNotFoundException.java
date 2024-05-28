package com.spotifyxp.exception;

public class ElementNotFoundException extends Exception {
    public ElementNotFoundException(Object element) {
        super("Element not found: " + element.getClass().getName());
    }
}
