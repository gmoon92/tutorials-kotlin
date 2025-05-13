# 1. [Function](https://kotlinlang.org/docs/functions.html)
- `fun` 키워드 사용
    ```kotlin
    fun double(x: Int): Int {
        return 2 * x
    }
    val result = double(2)
    ```
## 1.1 Named arguments
```kotlin
fun reformat(
    str: String,
    normalizeCase: Boolean = true,
    upperCaseFirstLetter: Boolean = true,
    divideByCamelHumps: Boolean = false,
    wordSeparator: Char = ' ',
) { /*...*/ }

reformat(
  "String!",
  false,
  upperCaseFirstLetter = false,
  divideByCamelHumps = true,
  '_'
)

reformat("This is a long String!")
```

### Unit
- return 값이 없음
- java의 `void`에 대응되는 wrapper class
    ```kotlin
    public actual object Unit {
        override fun toString(): String = "kotlin.Unit"
    }
    ```
    ```kotlin
    fun printHello(name: String?): Unit {
        if (name != null)
            println("Hello $name")
        else
            println("Hi there!")
        // `return Unit` or `return` is optional
    }

    fun printHello(name: String?) { ... }
    ```

### Any
- java의 `Object`에 대응

> 왜 Kotlin은 `Unit`이나 `Any`를 사용하나?
> - 원시 타입(primitive type)을 사용하지 않음
> - 문자(char)/숫자(int, long, double, float)/부울(boolean) 값을 표현하던 타입들도 전부 클래스 사용
> - 런타임 시에 원시 타입으로 변환됨

## 1.2 Generic functions
```kotlin
fun <T> singletonList(item: T): List<T> { /*...*/ }
```

## 1.3 Higher-order functions and lambdas
- 다른 함수를 파라미터로 받거나 함수를 반환하는 함수
- 람다나 함수 참조를 파라미터로 받거나 람다나 함수 참조를 반환하는 함수
### 1.3.1 함수 타입
```kotlin
public inline fun <T> Iterable<T>.filter(predicate: (T) -> Boolean): List<T> {
  return filterTo(ArrayList<T>(), predicate)
}

list.filter { it > 0}
```
```kotlin
val sum = { x: Int, y: Int -> x + y }
sum(1, 2)
```
### 1.3.2 파라미터로 받은 함수 타입
```kotlin
fun calculate(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
  return operation(a, b)
}

calculate(20, 10) {x, y -> x + y} //30
calculate(20, 10) {x, y -> x - y} //10
```
### 1.3.3 함수를 반환
```kotlin
fun calculate(type: String): (Int, Int) -> Int {
    return when (type) {
        "add" -> { x, y -> x + y }
        "subtract" -> { x, y -> x - y }
        else -> { _, _ -> 0 }
    }
}

val operation = calculate("add")
operation(10, 20) //30
```

# 2. [Scope functions](https://kotlinlang.org/docs/scope-functions.html#scope-functions.md)
## 2.1 let
- 함수를 호출하는 수신 객체 T를 블록의 인자로 넘기고, 블록의 결과값을 반환
  ```kotlin
  public inline fun <T, R> T.let(
    block: (T) -> R
  ): R
  ```
  ```kotlin
  val str: String? = "Hello"   
  val length = str?.let { 
      println("let() called on $it")        
      processNonNullString(it) //'it' is not null inside '?.let { }'
      it.length
  }

  fun processNonNullString(str: String) {}
  ```

## 2.2 also
- let()과 마찬가지로 함수를 호출하는 수신 객체 T를 블록의 인자로 넘기지만 let()과는 다르게 수신 객체T 자체를 반환 
  ```kotlin
  public inline fun <T> T.also(
      block: (T) -> Unit
  ): T
  ```
  ```kotlin
  val numbers = mutableListOf("one", "two", "three")
  numbers
    .also { println("The list elements before adding new one: $it") }
    .add("four")
  ```

## 2.3 apply
- 수신 객체 T를 블록의 인자로 전달하고 객체 자체인 this를 반환
- also() 와는 다르게 수신 객체T 타입의 객체 내에서 작업을 수행할 수 있음
```kotlin
public inline fun <T> T.apply(
    block: T.() -> Unit
): T
```
```kotlin
data class Person(var name: String, var age: Int = 0, var city: String = "")

val adam = Person("Adam").apply {
    age = 32
    city = "London"        
}
```

## 2.4 with
- 인자로 받는 객체를 block의 receiver로 전달하며 결과 반환
```kotlin
public inline fun <T, R> with(
    receiver: T,
    block: T.() -> R
): R
```
```kotlin
val numbers = mutableListOf("one", "two", "three")
val firstAndLast = with(numbers) {
    "The first element is ${first()}," +
    " the last element is ${last()}"
}
```

## 2.5 run
- 인자가 없는 익명 함수처럼 동작하는 형태
  + 어떤 객체를 생성하기 위한 실행문들을 하나로 묶어 리더빌리티를 높이는 역할을 수행
    ```kotlin
    fun <R> run(block: () -> R): R
    ```
    ```kotlin
    val hexNumberRegex = run {
    val digits = "0-9"
    val hexDigits = "A-Fa-f"
    val sign = "+-"

        Regex("[$sign]?[$digits$hexDigits]+")
    }

    for (match in hexNumberRegex.findAll("+123 -FFFF !%*& 88 XYZ")) {
    println(match.value)
    }
    ```
- 객체에서 호출하는 형태 
  + 어떤 값을 계산할 필요가 있거나 지역 변수 범위를 제한
    ```kotlin
    fun <T, R> T.run(block: T.() -> R): R
    ```
    ```kotlin
    class MultiportService(var url: String, var port: Int) {
        fun prepareRequest(): String = "Default request"
        fun query(request: String): String = "Result for query '$request'"
    }
  
    service.run {
      port = 8080
      query(prepareRequest() + " to port $port")
    }
    ```
