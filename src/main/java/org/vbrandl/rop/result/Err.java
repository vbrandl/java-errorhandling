package org.vbrandl.rop.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Wrapper class for erorr values.
 */
public final class Err<T, E> extends Result<T, E> {
    /**
     * The wrapped error.
     */
    private final E error;

    /**
     * Ctor.
     * @param error The error to be wrapped
     */
    public Err(final E error) {
        this.error = error;
    }

    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public boolean isErr() {
        return true;
    }

    @Override
    public Optional<T> ok() {
        return Optional.empty();
    }

    @Override
    public Optional<E> err() {
        return Optional.of(error);
    }

    @Override
    public boolean hasValue() {
        return false;
    }

    @Override
    public T getOk() {
        throw new ErrHasNoOkException();
    }

    @Override
    public E getErr() {
        return this.error;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Result<U, E> map(final Function<? super T, U> mapFn) {
        return (Result<U, E>)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Result<U, E> andThen(final Function<? super T, Result<U, E>> mapFn) {
        return (Result<U, E>)this;
    }

    @Override
    public <U> Result<T, U> mapErr(final Function<? super E, U> mapFn) {
        return orElse(mapFn.andThen(error -> new Err<>(error)));
    }

    @Override
    public <U> Result<T, U> orElse(final Function<? super E, Result<T, U>> mapFn) {
        return mapFn.apply(getErr());
    }

    @Override
    public void ifOk(final Consumer<? super T> consFn) {}

    @Override
    public void ifErr(final Consumer<? super E> consFn) {
        consFn.accept(error);
    }

    @Override
    public T unwrapOr(final T other) {
        return other;
    }

    @Override
    public E unwrapErrOr(final E other) {
        return this.error;
    }

    @Override
    public T unwrapOrElse(final Supplier<? extends T> other) {
        return other.get();
    }

    @Override
    public E unwrapErrOrElse(final Supplier<? extends E> other) {
        return this.error;
    }

    @Override
    public <X extends Throwable> T unwrapOrThrow(final Supplier<? extends X> exSup) throws X {
        throw exSup.get();
    }

    @Override
    public <X extends Throwable> E unwrapErrOrThrow(final Supplier<? extends X> exSup) throws X {
        return this.error;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.error.hashCode() ^ this.error.hashCode());
        return hash;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Err<T, E> other = (Err<T, E>) obj;
        return this.error.equals(other.error);
    }
}
