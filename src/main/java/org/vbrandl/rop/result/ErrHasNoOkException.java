package org.vbrandl.rop.result;

/**
 * Exception to be thrown if {@link Result#getOk} is called on an {@link Err}.
 */
public final class ErrHasNoOkException extends RuntimeException {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 5338981359593619861L;
}
