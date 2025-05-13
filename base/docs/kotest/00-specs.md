# Kotest Spec 스타일 한눈에 보기

코테스트(Kotest)는 다양한 "Spec(스타일)"을 제공하며,  
테스트 코드를 어떻게 구조화하고 싶은지에 따라 각기 다른 장점을 가진다.

| 스타일 이름       | 예시 선언                                                       | 특징/특성                        | 중첩/계층 | 적합한 테스트 성격   |
|--------------|-------------------------------------------------------------|------------------------------|-------|--------------|
| StringSpec   | `"test name" { ... }`                                       | 가장 간단함, JUnit 느낌             | X     | 단순 테스트       |
| FunSpec      | `test { ... }`,<br/>`context { ... }`                       | 메서드/함수 스타일, context 그룹화 도입   | O     | 그룹화, 단계적 테스트 |
| DescribeSpec | `describe { ... }`,<br/>`context { ... }`,<br/>`it { ... }` | BDD 스타일, 계층/요구사항 중심          | O     | BDD, 계층적 구조  |
| ShouldSpec   | `should { ... }`,<br/>`context { ... }`                     | 자연어 스타일, 읽기 쉽고 직관적           | O     | 자연어, 명세 위주   |
| FreeSpec     | `"블록명" - { ... }`                                           | 자유로운 계층, 블록 얼마든지 중첩 가능       | 자유로움  | 깊은 계층/중첩구조   |
| BehaviorSpec | `Given { ... }`,<br/>`When { ... }`,<br/>`Then { ... }`     | Given-When-Then 구문 (BDD에 최적) | O     | 시나리오 테스트     |

- 스펙은 프로젝트/취향/설계에 따라 자유롭게 선택
- 하나의 테스트 클래스에는 한 스펙만 사용해야 함
- 실제 기능(DSL, Assertion 등)은 어느 Spec에서도 동일하게 사용 가능

## 1. StringSpec

가장 JUnit스럽고 간단한 방식

```kotlin
import io.kotest.core.spec.style.StringSpec

class MyStringSpec : StringSpec({
    "2와 2를 더하면 4" {
        (2 + 2) shouldBe 4
    }

    "문자열은 비어 있지 않다" {
        "kotlin".isNotEmpty() shouldBe true
    }
})
```

### 2. FunSpec

JUnit의 메서드 스타일과 비슷, context로 중첩 그룹 가능

```kotlin
import io.kotest.core.spec.style.FunSpec

class MyFunSpec : FunSpec({
    context("수학 연산") {
        test("덧셈") { (1 + 2) shouldBe 3 }
        test("뺄셈") { (5 - 2) shouldBe 3 }
    }
    test("단일 테스트는 context 없이도 선언 가능") {
        true shouldBe true
    }
})
```

### 3. DescribeSpec

BDD 스타일, 계층형(기능 요구사항 우선) 테스트에 적합

```kotlin
import io.kotest.core.spec.style.DescribeSpec

class MyDescribeSpec : DescribeSpec({
    describe("Stack") {
        context("비어 있을 때") {
            it("pop은 예외를 던진다") {
                // 코드 예시
            }
        }
    }
})
```

### 4. ShouldSpec

자연어틱한 테스트 설명에 강점

```kotlin
import io.kotest.core.spec.style.ShouldSpec

class MyShouldSpec : ShouldSpec({
    context("List") {
        should("비어 있으면 size는 0이다") {
            emptyList<Int>().size shouldBe 0
        }
    }
})
```

### 5. FreeSpec

자유 계층형, 원하는 만큼 블록 중첩해서 쓸 수 있다

```kotlin
import io.kotest.core.spec.style.FreeSpec

class MyFreeSpec : FreeSpec({
    "outer block" - {
        "inner block" - {
            "세부 테스트" {
                // 테스트
            }
        }
    }
})
```

### 6. BehaviorSpec

Given-When-Then 스타일 (BDD에 최적)

```kotlin
import io.kotest.core.spec.style.BehaviorSpec

class MyBehaviorSpec : BehaviorSpec({
    Given("로또 시스템에서") {
        When("로또 번호 추첨을 할 때") {
            Then("6개의 서로 다른 숫자가 나온다") {
                // 테스트
            }
        }
    }
})
```

### Reference

- [Kotest Testing Styles](https://kotest.io/docs/framework/testing-styles.html)

