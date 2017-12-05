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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Optional;
import org.junit.Test;

public final class OkTest {

    /**
     * Test method to create a {@code Result}.
     */
    public Result<Integer, Boolean> createResult(final boolean ok) {
        return ok
            ? Result.ok(3)
            : Result.err(false);
    }

    @Test
    public void mapTest() {
        final Result<Integer, Boolean> mapped = createResult(true)
            .map(x -> x + 5)
            .mapErr(x -> !x); // no-op
        assertEquals(mapped, Result.ok(8));
    }

    @Test
    public void andThenTest() {
        final Result<String, Boolean> mapped = createResult(true)
            .andThen(x -> Result.ok(x.toString()));
        assertEquals(mapped, Result.ok("3"));
    }

    @Test
    public void orElseTest() {
        final Result<Integer, String> mapped = createResult(false)
            .orElse(x -> Result.err(x.toString()));
        assertEquals(mapped, Result.err("false"));
    }

    @Test
    public void mapErrTest() {
        final Result<Integer, Boolean> mappedErr = createResult(false)
            .mapErr(x -> !x)
            .map(x -> x + 5); // no-op
        assertEquals(mappedErr, Result.err(true));
    }

    @Test
    public void getOkTest() {
        final Result<Integer, Boolean> mapped = createResult(true)
            .map(x -> x + 5)
            .mapErr(x -> !x); // no-op
        final int result = mapped.getOk();
        assertEquals(result, 8);
    }

    @Test
    public void getErrTest() {
        final Result<Integer, Boolean> mapped = createResult(false)
            .mapErr(x -> !x);
        final boolean result = mapped.getErr();
        assertEquals(result, true);
    }

    @Test
    public void hasValueEmptyResultTest() {
        final Result<?, ?> res = Result.ok(null);
        assertEquals(res.hasValue(), false);
    }

    @Test
    public void hasValueTest() {
        final Result<?, ?> res = Result.ok(5);
        assertEquals(res.hasValue(), true);
    }

    @Test
    public void hasValueErrTest() {
        final Result<?, ?> res = Result.err(5);
        assertEquals(res.hasValue(), false);
    }

    @Test
    public void isOkOkTest() {
        final Result<?, ?> res = Result.ok(5);
        assertEquals(res.isOk(), true);
    }

    @Test
    public void isOkErrTest() {
        final Result<?, ?> res = Result.err(5);
        assertEquals(res.isOk(), false);
    }

    @Test
    public void isErrOkTest() {
        final Result<?, ?> res = Result.ok(5);
        assertEquals(res.isErr(), false);
    }

    @Test
    public void isErrErrTest() {
        final Result<?, ?> res = Result.err(5);
        assertEquals(res.isErr(), true);
    }

    @Test
    public void unwrapOrOkTest() {
        final Result<String, Boolean> res = createResult(true).map(x -> x.toString());
        final String unwraped = res.unwrapOr("42");
        assertEquals(unwraped, "3");
    }

    @Test
    public void unwrapOrErrTest() {
        final Result<String, Boolean> res = createResult(false).map(x -> x.toString());
        final String unwraped = res.unwrapOr("42");
        assertEquals(unwraped, "42");
    }

    @Test
    public void unwrapErrOrOkTest() {
        final Result<Integer, Boolean> res = createResult(true);
        assertEquals(res.unwrapErrOr(true), true);
    }

    @Test
    public void unwrapErrOrErrTest() {
        final Result<Integer, Boolean> res = createResult(false);
        assertEquals(res.unwrapErrOr(true), false);
    }

    @Test
    public void unwrapOrElseOkTest() {
        final Result<String, Boolean> res = createResult(true).map(x -> x.toString());
        assertEquals(res.unwrapOrElse(() -> "foo"), "3");
    }

    @Test
    public void unwrapOrElseErrTest() {
        final Result<String, Boolean> res = createResult(false).map(x -> x.toString());
        assertEquals(res.unwrapOrElse(() -> "foo"), "foo");
    }

    @Test
    public void unwrapErrOrElseOkTest() {
        final Result<String, Boolean> res = createResult(true).map(x -> x.toString());
        assertEquals(res.unwrapErrOrElse(() -> true), true);
    }

    @Test
    public void unwrapErrOrElseErrTest() {
        final Result<String, Boolean> res = createResult(false).map(x -> x.toString());
        assertEquals(res.unwrapErrOrElse(() -> true), false);
    }

    @Test
    public void okOkTest() {
        final Result<Integer, Boolean> res = createResult(true);
        assertEquals(res.ok(), Optional.of(3));
    }

    @Test
    public void okErrTest() {
        final Result<Integer, Boolean> res = createResult(false);
        assertEquals(res.ok(), Optional.empty());
    }

    @Test
    public void errOkTest() {
        final Result<Integer, Boolean> res = createResult(true);
        assertEquals(res.err(), Optional.empty());
    }

    @Test
    public void errErrTest() {
        final Result<Integer, Boolean> res = createResult(false);
        assertEquals(res.err(), Optional.of(false));
    }

    @Test
    public void unwrapOrThrowOkTest() {
        final Result<String, Boolean> res = createResult(true).map(x -> x.toString());
        assertEquals(res.unwrapOrThrow(NullPointerException::new), "3");
    }

    @Test(expected = NullPointerException.class)
    public void unwrapOrThrowErrTest() {
        final Result<Integer, Boolean> res = createResult(false);
        res.unwrapOrThrow(NullPointerException::new);
    }

    @Test(expected = NullPointerException.class)
    public void unwrapErrOrThrowOkTest() {
        final Result<Integer, Boolean> res = createResult(true);
        res.unwrapErrOrThrow(NullPointerException::new);
    }

    @Test
    public void unwrapErrOrThrowErrTest() {
        final Result<Integer, Boolean> res = createResult(false);
        assertEquals(res.unwrapErrOrThrow(NullPointerException::new), false);
    }

    @Test
    public void equalsOkOkTest() {
        assertEquals(Result.ok(3), Result.ok(3));
        assertNotEquals(Result.ok(42), Result.ok(3));
    }

    @Test
    public void equalsErrErrTest() {
        assertEquals(Result.err(3), Result.err(3));
        assertNotEquals(Result.err(42), Result.err(3));
    }

    @Test
    public void equalsOkErrTest() {
        assertNotEquals(Result.ok(3), Result.err(3));
        assertNotEquals(Result.ok(42), Result.err(3));
    }
}
