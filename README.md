# Composing BiFunctions in Java
`BiFunctions` don't compose naturally since the first `BiFunction` returns a single result and the second `BiFunction` requires
two parameters, which raises the question of what to provide for the other parameter to the second `BiFunction`.

One possible solution presented in this example is to use `BiFunctions` where the first parameter of each `BiFunction` is of 
the same type and is used to provide some sort of 'environment' object that all of the combined `BiFunctions` will have 
access to. The second parameter and the `BiFunction` result can then be treated as an ordinary `Function`, but with 
access to the 'environment' passed in as the first parameter.

Combining the `BiFunctions` manually then behaves like this so that both `BiFunctions` receive the same environment:
```java
        ResultType run(Env e, Param p) {
            var tmpResult = bifunc1(e, p);
            var result = bifunc2(e, tmpResult);
            return result;
        }
```
The pattern can be generalised:
```java
        var biFunc3 = biFunc1.biAndThen(biFunc2);

        var result = biFunc3.apply(e, p);
```
Or, using the more mathematical form of function composition:
```java
        var biFunc3 = biFunc2.biCompose(biFunc1);

        var result = biFunc3.apply(e, p);
```
Both forms produce the same result but the parameters are reversed.

The pattern can be used to combine arbitrarily complex sets of `BiFunctions` as long as these conditions are satisfied:
1. The first parameter of the `BiFunctions` all have the same type and it is used to provide an 'environment' or 'context' for the `BiFunctions` to refer to.
2. The result type of one `BiFunction` matches the type of the second parameter of the subsequent `BiFunction`.

I recommend that:
1. The 'environment' object is immutable and only used for reference.
2. The `BiFunctions` should be 'pure', i.e. no side effects.

See the unit test in this repo for an example use case.
