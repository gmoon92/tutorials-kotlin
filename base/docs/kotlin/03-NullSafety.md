# [Null safety](https://kotlinlang.org/docs/null-safety.html)

## 1. Nullable types and non-nullable types
```kotlin
var a: String = "abc"
//Null can not be a value of a non-null type String
a = null
print(a)
```
```kotlin
var b: String? = "abc"
b = null
print(b)
```

## 2. Safe call operator (?.)
```kotlin
val a: String? = "Kotlin"
val b: String? = null

println(a?.length) //6
println(b?.length) //null
```

## 3. Elvis operator (?:)
```kotlin
val b: String? = null
val l: Int = if (b != null) b.length else 0
println(l) //0
```
```kotlin
val b: String? = null
val l = b?.length ?: 0
println(l)
```

## 4. Not-null assertion operator (!!)
```kotlin
val b: String? = "Kotlin"
val l = b!!.length
println(l) //6
```
```kotlin
val b: String? = null
val l = b!!.length //NPE
println(l) 
```

## 5. Let function
```kotlin
val listWithNulls: List<String?> = listOf("Kotlin", null)

for (item in listWithNulls) {
    item?.let { println(it) } //Kotlin 
}
```

## 6. Safe casts
```kotlin
val a: Any = "Hello, Kotlin!"

val aInt: Int? = a as? Int
println(aInt) // null

val aString: String? = a as? String
println(aString) // Hello, Kotlin!
```
