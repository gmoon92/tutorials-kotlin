# 1. [Class](https://kotlinlang.org/docs/classes.html)
## 1.1 Constructor
### 1.1.1 Primary Constructor
- Class 명 옆에 constructor 키워드 사용
- constructor 생략 가능
  ```kotlin
  class Rectangle constructor(val height : Double, val length: Double)

  class Rectangle(val height: Double, val length: Double)
  ```
- Class Properties
  + var/val 키워드 사용하여 클래스의 프라퍼티로 생성 가능
    ```kotlin
    class Rectangle(height: Double, length: Double)
    val rectangle = Rectangle(2.0, 2.0)
    rectangle.height  // error
    ```
    ```kotlin
    class Rectangle(val height: Double, val length: Double)
    val rectangle = Rectangle(2.0, 2.0)
    rectangle.height
    ```
- Default property value
  ```kotlin
  class Rectangle(val height: Double = 1.0, val length: Double = 1.5)
  ```

### 1.1.2 Secondary Constructor
- Class 내부에 constructor 키워드를 사용하여 정의
- constructor 키워드를 생략할 수 없음
  ```kotlin
  class Rectangle {
    constructor(height: Double, length: Double)
  }
  ```
- Class의 주 생성자나 부 생성자를 호출하는 경우 this() 키워드 사용
  ```kotlin
  class Rectangle {
    val height: Double
    val length: Double

    constructor(height: Double, length: Double): this(
      height, length
    )
  }
  ```

## 1.2 Properties
```kotlin
var <propertyName>[: <PropertyType>] [= <property_initializer>]
    [<getter>]
    [<setter>]
```
- [Visibility modifiers](https://kotlinlang.org/docs/visibility-modifiers.html)
  ```kotlin
  class User {
      var name: String = "rsup"
      var password: String = ""
        private set
  }
  ```
  ```kotlin
  data class Group @QueryProjection constructor(
    val id: String,
    val name: String,
    private val count: Long
  ) {
    val existsOfSubGroup: Boolean
      get() = count >= 1L
  }
  ```

## 1.3 Initialization Block
- 한개 이상 초기화 블록을 가질 수 있음
  ```kotlin
  class InitOrderDemo(name: String) {
      val firstProperty = "First property: $name".also(::println)
      init {
          println("First initializer block that prints $name")
      }
      val secondProperty = "Second property: ${name.length}".also(::println)
      init {
          println("Second initializer block that prints ${name.length}")
      }
  }
  ```
  ```kotlin
  class Constructors {
      init {
          println("Init block")
      }
      constructor(i: Int) {
          println("Constructor $i")
      }
  }
  ```
- Class의 property를 초기화/검증
  ```kotlin
  class Rectangle(height: Double, length: Double) {
    val height: Double
    val length: Double

    init {
      this.height = height
      this.length = length
    }
  }
  ```
  ```kotlin
  class Rectangle {
    val height: Double
    val length: Double

    init {
      check(height)
      check(length)
    }

    private fun check(height: Double) {
      if (height <= 0)
        throw Exception("..")
    }
  }
  ```

# 2. [Functional (SAM) interfaces](https://kotlinlang.org/docs/fun-interfaces.html#sam-conversions)
- 하나의 추상 메서드를 가진 인터페이스
- `interface` 앞에 `fun`을 붙임
  ```java
  //java
  public interface OnClickListener {
    void onClick(View view);
  }

  btn.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View view) {
      TODO("Not yet implemented");
    }
  });

  btn.setOnClickListener(view -> {
    TODO("Not yet implemented");
  })
  ```
  ```kotlin
  //kotlin
  fun interface OnClickListener {
    fun onClick(view: view)
  }
  button.setOnClickListener {
    TODO("Not yet implemented")
  }
  ```
  ```kotlin
  fun interface IntPredicate {
     fun accept(i: Int): Boolean
  }

  val isEven = IntPredicate { it % 2 == 0 }
  isEven.accept(7) //false
  ```

# 3. [Object](https://kotlinlang.org/docs/object-declarations.html)
## 3.1 Object declarations
- Create a singleton
  ```kotlin
  object ColumnSize {
    const val UUID = 50
  }
  ```
## 3.2 Companion objects
- 클래스 내부에 선언
- 클래스 당 최대 한개만 존재
- 메서드, 프로퍼티와 초기화블록이 올 수 있으나, 생성자는 만들 수 없음
  ```kotlin 
  @Entity
  @Table(name = "user")
  class User(
    @Id
    @Column(length = ColumnSize.UUID)
    var id: String? = null,
    var name: String
  ) {
    companion object {
      fun of(name: String): User {
        return User(name = name)
      }
    }
  }
  ```
