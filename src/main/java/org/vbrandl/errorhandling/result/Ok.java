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

package org.vbrandl.errorhandling.result;

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
