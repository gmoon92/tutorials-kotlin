# [Idioms](https://kotlinlang.org/docs/idioms.html)

## 1. Create DTOs (POJOs/POCOs)
```kotlin
data class Customer(val name: String, val email: String)
```
* provides a Customer class with the following functionality:
  - getters (and setters in case of vars) for all properties
  - equals()
  - hashCode()
  - toString()
  - copy() ...

## 2. Default values for function parameters
```kotlin
fun foo(a: Int = 0, b: String = "") { ... }
```

## 3. Extension functions
```kotlin
fun String.spaceToCamelCase() { ... }

"Convert this to camelcase".spaceToCamelCase()
```

## 4. Create a singleton
```kotlin
object Resource {
    val name = "Name"
}
```

## 5. Instantiate an abstract class
```kotlin
abstract class MyAbstractClass {
    abstract fun doSomething()
    abstract fun sleep()
}

val myObject = object : MyAbstractClass() {
    override fun doSomething() {
        // ...
    }

    override fun sleep() { // ...
    }
}
myObject.doSomething()
```

## 6. If-not-null shorthand
```kotlin
val files = File("Test").listFiles()

println(files?.size)
```

## 7. If-not-null-else shorthand
```kotlin
val files = File("Test").listFiles()
println(files?.size ?: "empty")

val filesSize = files?.size ?: run {
    val someSize = getSomeSize()
    someSize * 2
}
println(filesSize)
```

## 8. Execute a statement if null
```kotlin
val values = ...
val email = values["email"] ?: throw IllegalStateException("Email is missing!")

val emails = ... // might be empty
val mainEmail = emails.firstOrNull() ?: ""
```

## 9. Execute if not null
```kotlin
val value = ...

value?.let {
    ... // execute this block if not null
}
```

## 10. Configure properties of an object (apply)
```kotlin
val myRectangle = Rectangle().apply {
    length = 4
    breadth = 5
}
```

## 11. try-with-resources
```kotlin
val stream = Files.newInputStream(Paths.get("/some/file.txt"))
stream.buffered().reader().use { reader ->
    println(reader.readText())
}
```
