/*
 * Copyright (c) 2017 Brandl, Valentin <mail+github@vbrandl.net>
 * Author: Brandl, Valentin <mail+github@vbrandl.net>
 *
 * Licensed unter the Apache License, Version 2.0 or the MIT license, at your
 * option.
 *
 * ********************************************************************************
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * ********************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vbrandl.rop.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A Result data type with the potential to replace exception based error handling with a monadic error type like in
 * Haskell or Rust.
 * @param <T> The type of the Ok value
 * @param <E> The type of the Err value
 */
public abstract class Result<T, E> {

    /**
     * Creates a new {@link org.vbrandl.rop.result.Ok} for the given value. If the value is {@code null}, an empty
     * {@link Ok} will be created.
     * @param value The value to be wrapped
     * @param <T> The type of the Ok value
     * @param <E> The type of the Err value
     * @return A new {@link org.vbrandl.rop.result.Ok} instance
     */
    public static final <T, E> Result<T, E> ok(final T value) {
        return new Ok<>(value);
    }

    /**
     * Converts from {@code Result<T, E>} to {@code Optional<T>}.
     * @return An {@code Optional} containing the wrapped value or an empty value for {@link Err} or an empty
     * {@link Ok}
     */
    abstract Optional<T> ok();

    /**
     * Creates a new {@link org.vbrandl.rop.result.Err} for the given error.
     * @param error The error to be wrapped
     * @param <T> The type of the Ok value
     * @param <E> The type of the Err value
     * @return A new {@link org.vbrandl.rop.result.Err} instance
     */
    public static final <T, E> Result<T, E> err(final E error) {
        return new Err<>(error);
    }

    /**
     * Converts from {@code Result<T, E>} to {@code Optional<E>}.
     * @return An {@code Optional} containing the wrapped error or an empty value for {@link Ok}
     */
    abstract Optional<E> err();

    /**
     * Checks if this instance is an {@link org.vbrandl.rop.result.Ok}.
     * @return {@code true} for {@link org.vbrandl.rop.result.Ok}, else {@code false}
     */
    abstract boolean isOk();

    /**
     * Checks if this instance is an {@link org.vbrandl.rop.result.Err}.
     * @return {@code true} for {@link org.vbrandl.rop.result.Err}, else {@code false}
     */
    abstract boolean isErr();

    /**
     * Checks if this instance has a value.
     * @return {@code true} for an {@link org.vbrandl.rop.result.Ok} with value, {@code false} for an empty
     * {@link org.vbrandl.rop.result.Ok} or {@link org.vbrandl.rop.result.Err}
     */
    abstract boolean hasValue();

    /**
     * Get the wrapped value from an {@link org.vbrandl.rop.result.Ok}
     * @return The wrapped value
     * @throws org.vbrandl.rop.result.EmptyResultException if called on an empty {@link org.vbrandl.rop.result.Ok}
     * @throws org.vbrandl.rop.result.ErrHasNoOkException if called on an {@link org.vbrandl.rop.result.Err}
     */
    abstract T getOk();

    /**
     * Get the wrapped error from an {@link org.vbrandl.rop.result.Err}
     * @return The wrapped error
     * @throws org.vbrandl.rop.result.OkHasNoErrException if called on an {@link org.vbrandl.rop.result.Ok}
     */
    abstract E getErr();

    /**
     * Applies a function {@code T -> U} to the wrapped value and returns a new {@link Result}. If applied on an
     * {@link Err} the wrapper is returned unchanged
     * @param mapFn The function to apply
     * @param <U> The type of the new Ok value
     * @return A new {@code Result<T, E>}
     */
    abstract <U> Result<U, E> map(final Function<? super T, U> mapFn);

    /**
     * Applies a function {@code T -> Result<U, E>} to the wrapped value and returns a new {@link Result}. If
     * applied on an {@link Err} the wrapper is returned unchanged
     * @param mapFn The function to apply
     * @param <U> The type of the new Ok value
     * @return A new {@code Result<T, E>}
     */
    abstract <U> Result<U, E> andThen(final Function<? super T, Result<U, E>> mapFn);

    /**
     * Applies a function {@code E -> U} to the wrapped error and returns a new {@link Result}. If applied on an
     * {@link Ok} the wrapper is returned unchanged
     * @param mapFn The function to apply
     * @param <U> The type of the new Err value
     * @return A new {@code Result<T, U>}
     */
    abstract <U> Result<T, U> mapErr(final Function<? super E, U> mapFn);

    /**
     * Applies a function {@code E -> Result<T, U>} to the wrapped error and returns a new {@link Result}. If
     * applied on an {@link Ok} the wrapper is returned unchanged
     * @param mapFn The function to apply
     * @param <U> The type of the new Err value
     * @return A new {@code Result<T, U>}
     */
    abstract <U> Result<T, U> orElse(final Function<? super E, Result<T, U>> mapFn);

    /**
     * Applies a consumer that takes a {@code T} as parameter to the wrapped value.
     * @param consFn The consumer to apply
     */
    abstract void ifOk(final Consumer<? super T> consFn);

    /**
     * Applies a consumer that takes a {@code T} as parameter to the wrapped error.
     * @param consFn The consumer to apply
     */
    abstract void ifErr(final Consumer<? super E> consFn);

    /**
     * Unwraps the wrapped value or returns the supplied default.
     * @param other Default value
     * @return The wrapped value or {@code default}
     */
    abstract T unwrapOr(final T other);

    /**
     * Unwraps the wrapped error or returns the supplied default.
     * @param other Default error
     * @return The wrapped error or {@code other}
     */
    abstract E unwrapErrOr(final E other);

    /**
     * Unwraps the wrapped value or returns the result of the supplied {@code Supplier}.
     * @param other Supplier to be executed if no value is wrapped
     * @return The wrapped value or the result of {@code other}
     */
    abstract T unwrapOrElse(final Supplier<? extends T> other);

    /**
     * Unwraps the wrapped error or returns the result of the supplied {@code Supplier}.
     * @param other Supplier to be executed if no error is wrapped
     * @return The wrapped error or the result of {@code other}
     */
    abstract E unwrapErrOrElse(final Supplier<? extends E> other);

    /**
     * Unwraps the wrapped value or throws an exception.
     * @param exSup The exception to be thrown
     * @param <X> Exception class
     * @return The wrapped value
     * @throws X if there is no value present
     */
    abstract <X extends Throwable> T unwrapOrThrow(final Supplier<? extends X> exSup) throws X;

    /**
     * Unwraps the wrapped error or throws an exception.
     * @param exSup The exception to be thrown
     * @param <X> Exception class
     * @return The wrapped error
     * @throws X if there is no error present
     */
    abstract <X extends Throwable> E unwrapErrOrThrow(final Supplier<? extends X> exSup) throws X;
}
