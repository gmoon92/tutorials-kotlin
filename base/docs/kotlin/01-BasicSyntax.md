# [Basic syntax](https://kotlinlang.org/docs/basic-syntax.html)

## 1. Program entry point
- An entry point of a Kotlin application is the `main` function:
    ```kotlin
    fun main() {
        println("Hello world!")
    }
    ```

## 2. [Functions](https://kotlinlang.org/docs/functions.html)
- A function with two Int parameters and Int return type:
    ```kotlin
    fun sum(a: Int, b: Int): Int {
        return a + b
    }
    ```
- A function body can be an expression. Its return type is inferred:
    ```kotlin
    fun sum(a: Int, b: Int) = a + b
    ```
- A function that returns no meaningful value:
    ```kotlin
    fun printSum(a: Int, b: Int): Unit {
        println("sum of $a and $b is ${a + b}")
    }
    ```
- Unit return type can be omitted:
    ```kotlin
    fun printSum(a: Int, b: Int) {
        println("sum of $a and $b is ${a + b}")
    }
    ```

## 3. [Variables](https://kotlinlang.org/docs/properties.html)
- val
    ```kotlin
    val x: Int = 5
    ```
- var
    ```kotlin
    var x: Int = 5
    x += 1
    ```
- data type of omitted:
    ```kotlin
    val x = 5
    ```

## 4. Creating [classes](https://kotlinlang.org/docs/classes.html) and [instances](https://kotlinlang.org/docs/object-declarations.html)
- Properties of a class can be listed in its declaration or body:
    ```kotlin
    class Rectangle(val height: Double, val length: Double) {
        val perimeter = (height + length) * 2
    }
    ```
- The default constructor with parameters listed in the class declaration is available automatically:
    ```kotlin
    class Rectangle(val height: Double, val length: Double) {
        val perimeter = (height + length) * 2 
    }

    fun main() {
        val rectangle = Rectangle(5.0, 2.0)
        println("The perimeter is ${rectangle.perimeter}")
    }
    ```
- Inheritance classes
    ```kotlin
    class Rectangle(val height: Double, val length: Double): Shape() {
        val perimeter = (height + length) * 2
    }
    ```
- Interface
    ```kotlin
    interface MyInterface {
        fun bar()
        fun foo() {
          // optional body
        }
    }

    class Child : MyInterface {
      override fun bar() {
        // body
      }
    }
    ```

## 5. [String templates](https://kotlinlang.org/docs/strings.html#string-templates)
```kotlin
var a = 1
val s1 = "a is $a" 

a = 2
val s2 = "${s1.replace("is", "was")}, but now is $a"
```

## 6. [Conditional expressions](https://kotlinlang.org/docs/control-flow.html#when-expressions-and-statements)
```kotlin
fun maxOf(a: Int, b: Int): Int {
    if (a > b) {
        return a
    } else {
        return b
    }
}
```
```kotlin
fun maxOf(a: Int, b: Int) = if (a > b) a else b
```

## 7. [for loop](https://kotlinlang.org/docs/control-flow.html#for-loops)
```kotlin
val items = listOf("apple", "banana", "kiwifruit")
for (item in items) {
    println(item)
}

for (index in items.indices) {
  println("item at $index is ${items[index]}")
}
```

## 8. [when expression](https://kotlinlang.org/docs/control-flow.html#when-expressions-and-statements)
```kotlin
fun describe(obj: Any): String =
    when (obj) {
        1          -> "One"
        "Hello"    -> "Greeting"
        is Long    -> "Long"
        !is String -> "Not a string"
        else       -> "Unknown"
    }
```

## 9. [Collections](https://kotlinlang.org/docs/collections-overview.html)
```kotlin
when {
    "orange" in items -> println("juicy")
    "apple" in items -> println("apple is fine too")
}
```
- lambda expression
```kotlin
val fruits = listOf("banana", "avocado", "apple", "kiwifruit")

fruits
    .filter { it.startsWith("a") }
    .sortedBy { it }
    .map { it.uppercase() }
    .forEach { println(it) }
```

## 10. [Enums](https://kotlinlang.org/docs/enum-classes.html)
```kotlin
enum class Direction {
    NORTH, SOUTH, WEST, EAST
}
```
```kotlin
enum class Color(val rgb: Int) {
  RED(0xFF0000),
  GREEN(0x00FF00),
  BLUE(0x0000FF)
}
```
