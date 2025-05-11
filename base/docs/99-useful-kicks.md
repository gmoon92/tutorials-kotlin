# Useful Kicks

## 1. [Backing properties](https://kotlinlang.org/docs/properties.html#backing-properties)
- 파라미터는 w로, 코드에서는 width로..
    ```kotlin
    //application/json
    data class RequestVO(
      @JsonProperty("w")
      val width: Int
    )

    //application/x-www-form-urlencoded
    data class RequestVO(
      val w: Int
    ) {
      val width: Int
        get() = w
    }
    ```

## 2. Data Class - copy
> [02-idioms.md](02-idioms.md)
```kotlin
data class User(
  val name: String,
  val age: Int,
) {
  init {
    require(age >= 18)
  }
}
```
```kotlin
class UserTest() {
  @Test
  fun `나이가 18세 미만이면 IllegalArgumentException을 던진다`() {
    val invalidAge = 17
    assertThrows<IllegalArgumentException> {
      val user = defaultUser.copy(age = invalidAge)
    }
  }

  @Test
  fun `나이가 18세 이상이면 예외를 던지지 않는다`() {
    val validAge = 18
    assertDoesNotThrow {
      val user = defaultUser.copy(age = validAge)
    }
  }

  private val defaultUser = User(
    name = "정카펀",
    age = 28
  )
}
```

## 3. Immutable List
```kotlin
val list = listOf(1, 2, 3)
```
- [buildList](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/build-list.html)
  + `build` 내부는 가변(mutable), 응답은 불변(immutable) 객체
    ```kotlin
    val list = buildList {
      add(1)
      add(2)
      add(3)
    }
    ```
