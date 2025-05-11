package com.gmoon.base.sample.kotest.property

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.ranges.shouldBeIn
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import io.kotest.property.forAll

/**
 *  Kotest 프로퍼티 기반 테스트 예제
 *
 * - 규칙:
 *   1. "설명" { ... } 블록은 클래스의 람다 최상위에만 선언!
 *   2. 절대 중첩 불가. (중첩시 InvalidDslException 발생)
 *
 * ```kotlin
 * class 실패예제 : StringSpec({
 * // 아래처럼 중첩하면 안됨
 *     "외부 테스트" {
 *         "이너 테스트" { // InvalidDslException 에러 발생! (중첩 불가)
 *             // ...
 *         }
 *     }
 * })
 * ```
 * 위 코드는 "Cannot add a root test after the spec has been instantiated" 등의 에러 발생
 *
 * ```kotlin
 * // 올바른 예시
 * class 정상예제 : StringSpec({
 *     "테스트1" { ... }
 *     "테스트2" { ... }
 * })
 * ```
 *
 * [Kotest property test functions](https://kotest.io/docs/proptest/property-test-functions.html)
 * [KDoc](https://kotlinlang.org/docs/kotlin-doc.html#block-tags)
 * */
class `01PropertyTest` : StringSpec({

    "forAll - 모든 입력 값에 대해 함수가 true 반환하면 테스트 통과" {
        forAll<String, String> { a, b ->
            (a + b).length == a.length + b.length
        }
    }

    "checkAll - haskell 라이브러리에 유래됐으며, 예외가 발생하지 않으면 테스트 통과" {
        checkAll<String, String> { a, b ->
            run {
                val result = a + b

                result shouldHaveLength (a.length + b.length)
                result.length shouldBe a.length + b.length
            }
        }
    }

    "Iterations - 테스트 반복 횟수 지정" {
        checkAll<String, String>(10_000) { a, b ->
            a + b shouldHaveLength (a.length + b.length)
        }
    }

    "Specifying Generators - is allowed to drink in Chicago" {
        forAll(Arb.int(21..150)) { age ->
            print("drink ... $age")
            age in 20..150
        }

        checkAll(Arb.int(21..150)) { age ->
            print("$age drink...")
            age shouldBeIn 20..150
            age shouldBeInRange 20..150
            age shouldBeGreaterThanOrEqual 20
            age shouldBeLessThanOrEqual 150
        }
    }
})
