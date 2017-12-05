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
import static org.junit.Assert.assertNotEquals;

import java.util.Optional;
import org.junit.Test;

public final class OkTest {

    private static final String STATIC_1337 = "1337";

    private static <T> void throwNpeForValue(final T value) {
        throw new NullPointerException(value.toString());
    }

    @Test
    public void okTest() {
        final Ok<Integer, ?> ok = new Ok(3);
        assertEquals(ok.ok(), Optional.of(3));
    }

    @Test
    public void okEmptyTest() {
        final Ok<Integer, ?> ok = new Ok(null);
        assertEquals(ok.ok(), Optional.empty());
    }

    @Test
    public void errTest() {
        final Ok<Integer, ?> ok = new Ok(3);
        assertEquals(ok.err(), Optional.empty());
    }

    @Test
    public void isOkTest() {
        final Ok<Integer, ?> ok = new Ok(3);
        assertEquals(ok.isOk(), true);
    }

    @Test
    public void isErrTest() {
        final Ok<Integer, ?> ok = new Ok(3);
        assertEquals(ok.isErr(), false);
    }

    @Test
    public void hasValueTest() {
        final Ok<Integer, ?> ok = new Ok(3);
        assertEquals(ok.hasValue(), true);
    }

    @Test
    public void hasValueEmptyTest() {
        final Ok<Integer, ?> ok = new Ok(null);
        assertEquals(ok.hasValue(), false);
    }

    @Test
    public void getOkTest() {
        final Ok<Integer, ?> ok = new Ok("3");
        assertEquals(ok.getOk(), "3");
    }

    @Test(expected = EmptyResultException.class)
    public void getOkEmptyTest() {
        final Ok<Integer, ?> ok = new Ok(null);
        ok.getOk();
    }

    @Test(expected = OkHasNoErrException.class)
    public void getErrTest() {
        final Ok<Integer, ?> ok = new Ok(3);
        ok.getErr();
    }

    @Test
    public void mapTest() {
        final Ok<Integer, ?> ok = new Ok(3);
        assertEquals(ok.map(x -> x * x), new Ok(9));
    }

    @Test
    public void mapEmptyTest() {
        final Ok<Integer, ?> ok = new Ok(null);
        assertEquals(ok.map(x -> x * x), new Ok(null));
    }

    @Test
    public void andThenTest() {
        final Ok<Integer, ?> ok = new Ok(3);
        assertEquals(ok.andThen(x -> Result.ok(x)), new Ok(3));
    }

    @Test
    public void andThenEmptyTest() {
        final Ok<Integer, ?> ok = new Ok(null);
        assertEquals(ok.andThen(x -> Result.ok(x)), new Ok(null));
    }

    @Test
    public void mapErrTest() {
        final Ok<Integer, Boolean> ok = new Ok(42);
        assertEquals(ok.mapErr(x -> !x), new Ok(42));
    }

    @Test
    public void orElseTest() {
        final Ok<Integer, Boolean> ok = new Ok(42);
        assertEquals(ok.orElse(x -> Result.err(!x)), new Ok(42));
    }

    @Test(expected = NullPointerException.class)
    public void ifOkExceptionTest() {
        final Ok<Integer, ?> ok = new Ok(42);
        ok.ifOk(OkTest::throwNpeForValue);
    }

    @Test
    public void ifOkTest() {
        final Ok<Integer, ?> ok = new Ok(42);
        ok.ifOk(System.out::println);
    }

    @Test
    public void ifErrTest() {
        final Ok<Integer, ?> ok = new Ok(42);
        ok.ifErr(OkTest::throwNpeForValue);
    }

    @Test
    public void unwrapOrTest() {
        final Ok<String, ?> ok = new Ok("42");
        assertEquals(ok.unwrapOr(STATIC_1337), "42");
    }

    @Test
    public void unwrapOrEmptyTest() {
        final Ok<String, ?> ok = new Ok(null);
        assertEquals(ok.unwrapOr(STATIC_1337), STATIC_1337);
    }

    @Test
    public void unwrapErrOrTest() {
        final Ok<Integer, Boolean> ok = new Ok(42);
        assertEquals(ok.unwrapErrOr(false), false);
    }

    @Test
    public void unwrapOrElseTest() {
        final Ok<String, ?> ok = new Ok("42");
        assertEquals(ok.unwrapOrElse(() -> STATIC_1337), "42");
    }

    @Test
    public void unwrapOrElseEmptyTest() {
        final Ok<String, ?> ok = new Ok(null);
        assertEquals(ok.unwrapOrElse(() -> STATIC_1337), STATIC_1337);
    }

    @Test
    public void unwrapErrOrElseTest() {
        final Ok<String, Boolean> ok = new Ok("42");
        assertEquals(ok.unwrapErrOrElse(() -> true), true);
    }

    @Test
    public void unwrapOrThrowTest() {
        final Ok<String, Boolean> ok = new Ok("42");
        assertEquals(ok.unwrapOrThrow(NullPointerException::new), "42");
    }

    @Test(expected = NullPointerException.class)
    public void unwrapOrThrowEmptyTest() {
        final Ok<String, Boolean> ok = new Ok(null);
        ok.unwrapOrThrow(NullPointerException::new);
    }

    @Test(expected = NullPointerException.class)
    public void unwrapErrOrThrowTest() {
        final Ok<String, Boolean> ok = new Ok("42");
        ok.unwrapErrOrThrow(NullPointerException::new);
    }

    @Test
    public void equalsSameObjectTest() {
        final Ok<String, Boolean> ok = new Ok("42");
        assertEquals(ok, ok);
    }

    @Test
    public void equalsNullTest() {
        final Ok<String, Boolean> ok = new Ok("42");
        assertNotEquals(ok, null);
    }

    @Test
    public void hashCodeTest() {
        final Ok<String, Boolean> fst = new Ok("42");
        final Ok<String, Boolean> snd = new Ok("42");
        assertEquals(fst.hashCode(), snd.hashCode());
    }
}
