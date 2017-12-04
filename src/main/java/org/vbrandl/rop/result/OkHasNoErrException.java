package org.vbrandl.rop.result;

/**
 * Exception to be thrown if {@link Result#getErr} is called on an {@link Ok}.
 */
public final class OkHasNoErrException extends RuntimeException {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4406468355415609205L;
}
