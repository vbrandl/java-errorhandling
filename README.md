# Java Monadic Errorhandling

[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/vbrandl/countmap/blob/master/LICENSE-MIT)
[![License](https://img.shields.io/badge/license-Apache-green.svg)](https://github.com/vbrandl/countmap/blob/master/LICENSE-APACHE)

This library aims to provide some useful data types to be used in Java.

Currently it only implements a `Result` type to offer an alternative to
exception based error-handling. The API is strongly influenced by Rusts `Result`
type.

## Example

```java
public Result<Integer, String> someMethod() {
    if (conditions) {
        try {
            final Integer result = doSomeCalculation();
            return Result.ok(result);
        } catch (final Exception ex) {
            return Result.err(ex.toString());
        }
    } else {
        return Result.err("Something went wrong");
    }
}

public void handleResult() {
    final Result<String, String> res = someMethod().map(x -> x * 2).map(x -> x.toString());
    final Integer value = res.unwrapOr(5);
}
```

## License

This library is licensed under either of the following, at your option:

 * Apache License, Version 2.0, ([LICENSE-APACHE](LICENSE-APACHE) or
http://www.apache.org/licenses/LICENSE-2.0)
  * MIT License ([LICENSE-MIT](LICENSE-MIT) or
http://opensource.org/licenses/MIT)
