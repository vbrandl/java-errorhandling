package org.vbrandl.rop.result;

/**
 * Exception to be thrown if {@link Result#getOk} is called on an empty {@link Ok}.
 */
public final class EmptyResultException extends RuntimeException {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -6371715190256049149L;
}
