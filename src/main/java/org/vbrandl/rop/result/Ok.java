package org.vbrandl.rop.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Wrapper class for successfull results.
 */
public final class Ok<T, E> extends Result<T, E> {
    /**
     * The wrapped value.
     */
    private final Optional<T> value;

    /**
     * Ctor.
     * @param value The value to be wrapped
     */
    protected Ok(final T value) {
        this.value = Optional.ofNullable(value);
    }

    @Override
    public boolean isOk() {
        return true;
    }

    @Override
    public boolean isErr() {
        return false;
    }

    @Override
    public Optional<T> ok() {
        return this.value;
    }

    @Override
    public Optional<E> err() {
        return Optional.empty();
    }

    @Override
    public boolean hasValue() {
        return this.value.isPresent();
    }

    @Override
    public T getOk() {
        if (value.isPresent()) {
            return value.get();
        }
        throw new EmptyResultException();
    }

    @Override
    public E getErr() {
        throw new OkHasNoErrException();
    }

    @Override
    public <U> Result<U, E> map(final Function<? super T, U> mapFn) {
        return andThen(mapFn.andThen(value -> new Ok<>(value)));
    }

    @Override
    public <U> Result<U, E> andThen(final Function<? super T, Result<U, E>> mapFn) {
        return mapFn.apply(getOk());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Result<T, U> mapErr(final Function<? super E, U> mapFn) {
        return (Result<T, U>)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Result<T, U> orElse(final Function<? super E, Result<T, U>> mapFn) {
        return (Result<T, U>)this;
    }

    @Override
    public void ifOk(final Consumer<? super T> consFn) {
        this.value.ifPresent(consFn);
    }

    @Override
    public void ifErr(final Consumer<? super E> consFn) {}

    @Override
    public T unwrapOr(final T other) {
        return value.orElse(other);
    }

    @Override
    public E unwrapErrOr(final E other) {
        return other;
    }

    @Override
    public T unwrapOrElse(final Supplier<? extends T> other) {
        return value.orElseGet(other);
    }

    @Override
    public E unwrapErrOrElse(final Supplier<? extends E> other) {
        return other.get();
    }

    @Override
    public <X extends Throwable> T unwrapOrThrow(final Supplier<? extends X> exSup) throws X {
        return this.value.orElseThrow(exSup);
    }

    @Override
    public <X extends Throwable> E unwrapErrOrThrow(final Supplier<? extends X> exSup) throws X {
        throw exSup.get();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.value.hashCode() ^ this.value.hashCode());
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
        final Ok<T, E> other = (Ok<T, E>) obj;
        return this.value.equals(other.value);
    }
}
