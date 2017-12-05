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

import static org.junit.Assert.assertEquals;

import java.util.Optional;
import org.junit.Test;

public final class ErrTest {

    private static final String STATIC_1337 = "1337";

    private static <T> void throwNpeForValue(final T value) {
        throw new NullPointerException(value.toString());
    }

    @Test
    public void okTest() {
        final Err<?, Integer> err = new Err(3);
        assertEquals(err.ok(), Optional.empty());
    }

    @Test
    public void errTest() {
        final Err<?, Integer> err = new Err(3);
        assertEquals(err.err(), Optional.of(3));
    }

    @Test
    public void isOkTest() {
        final Err<?, Integer> err = new Err(3);
        assertEquals(err.isOk(), false);
    }

    @Test
    public void isErrTest() {
        final Err<?, Integer> err = new Err(3);
        assertEquals(err.isErr(), true);
    }

    @Test
    public void hasValueTest() {
        final Err<?, Integer> err = new Err(3);
        assertEquals(err.hasValue(), false);
    }

    @Test(expected = ErrHasNoOkException.class)
    public void getOkTest() {
        final Err<?, Integer> err = new Err("3");
        err.getOk();
    }

    @Test
    public void getErrTest() {
        final Err<?, String> err = new Err("3");
        assertEquals(err.getErr(), "3");
    }

    @Test
    public void mapTest() {
        final Err<Integer, Integer> err = new Err(3);
        assertEquals(err.map(x -> x.toString()), new Err(3));
    }

    @Test
    public void andThenTest() {
        final Err<Boolean, Integer> err = new Err(3);
        assertEquals(err.andThen(x -> Result.ok(!x)), new Err(3));
    }

    @Test
    public void mapErrTest() {
        final Err<Boolean, Integer> err = new Err(42);
        assertEquals(err.mapErr(x -> x + 1), new Err(43));
    }

    @Test
    public void orElseTest() {
        final Err<Boolean, Integer> err = new Err(42);
        assertEquals(err.orElse(x -> Result.err(x + 1)), new Err(43));
    }

    @Test
    public void ifOkTest() {
        final Err<?, Integer> err = new Err(42);
        err.ifOk(ErrTest::throwNpeForValue);
    }

    @Test(expected = NullPointerException.class)
    public void ifErrTest() {
        final Err<?, Integer> err = new Err(42);
        err.ifErr(ErrTest::throwNpeForValue);
    }

    @Test
    public void unwrapOrTest() {
        final Err<Boolean, String> err = new Err("42");
        assertEquals(err.unwrapOr(false), false);
    }

    @Test
    public void unwrapErrOrTest() {
        final Err<Boolean, String> err = new Err("42");
        assertEquals(err.unwrapErrOr(STATIC_1337), "42");
    }

    @Test
    public void unwrapOrElseTest() {
        final Err<Boolean, String> err = new Err("42");
        assertEquals(err.unwrapOrElse(() -> false), false);
    }

    @Test
    public void unwrapErrOrElseTest() {
        final Err<String, Boolean> err = new Err(false);
        assertEquals(err.unwrapErrOrElse(() -> true), false);
    }

    @Test(expected = NullPointerException.class)
    public void unwrapOrThrowTest() {
        final Err<String, Boolean> err = new Err(false);
        err.unwrapOrThrow(NullPointerException::new);
    }

    @Test
    public void unwrapErrOrThrowTest() {
        final Err<String, Boolean> err = new Err(false);
        assertEquals(err.unwrapErrOrThrow(NullPointerException::new), false);
    }
}
