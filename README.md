# Railroad-Oriented-Programming

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
