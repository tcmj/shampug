package com.tcmj.shampug.intern;

/**
 * Root exception of the shampug project.
 */
public class ShamPugException extends RuntimeException {
    public ShamPugException(String message) {
        super(message);
    }

    public ShamPugException(String message, Throwable cause) {
        super(message, cause);
    }
}