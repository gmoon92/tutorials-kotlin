package com.gmoon.base.kotest

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAnyOf
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.RandomSource
import io.kotest.property.arbitrary.*
import io.kotest.property.exhaustive.boolean
import io.kotest.property.exhaustive.collection
import io.kotest.property.exhaustive.ints
import io.kotest.property.forAll
import kotlin.math.absoluteValue

/**
 * Kotest Generator(Gen, Arb, Exhaustive) 테스트 예제
 *
 * ### 기본 개념
 * - `Gen`은 Kotest에서 테스트 입력값을 생성하는 제너레이터의 기반 `sealed class` 다.
 * - 입력 스트림과 비슷하게, 특정 타입의 값을(거의) 무한히 계속 공급할 수 있다.
 * - Kotest에는 두 가지 주요 `Generator`가 있다.
 *
 *   1. **Arb** (Arbitrary)
 *      - 랜덤(임의) 값을 무한하게 생성
 *      - ex: 임의의 정수, 임의의 문자열 등.
 *
 *   2. **Exhaustive**
 *      - 주어진 범위나 집합의 "모든 값"을 빠짐없이 생성(유한 집합).
 *      - ex: 0~10까지 모든 정수, true/false 등.
 *
 * ```kotlin
 * import io.kotest.property.Arb
 * import io.kotest.property.Exhaustive
 * import io.kotest.property.arbitrary.int
 * import io.kotest.property.exhaustive.ints
 * import io.kotest.property.forAll
 *
 * forAll(
 *     Arb.int(1..1000),                // 임의의 양의 정수(Arb)
 *     Exhaustive.ints(0..200 step 2)   // 0~200까지 모든 짝수(Exhaustive)
 * ) { positive, even ->
 *     positive > 0 && even % 2 == 0
 * }
 * ```
 *
 * - 두 가지 타입을 하나의 프로퍼티 테스트에서 조합해 사용할 수도 있다.
 *   예를 들어, 한 인자는 100개의 랜덤 positive integer(Arb),
 *   다른 인자는 0~200까지 모든 짝수(Exhaustive)로 테스트 가능합니다.
 *
 *
 * ### Tips
 * - Arb: 랜덤/경계/다양한 값(샘플)
 * - Exhaustive: 작고 의미 있는 범위 or 모든 경우의 수를 검사
 * - 조합 사용 시 corner case 및 범용성 높은 property test가 가능
 *
 * 일부 제너레이터는 JVM에서만 동작하니 공식문서를 참고
 * [Kotest Generators](https://kotest.io/docs/proptest/property-test-generators.html)
 * [KDoc](https://kotlinlang.org/docs/kotlin-doc.html#block-tags)
 * */
class `02GeneratorsTest` : FunSpec({

    context("Arbitrary 임의 generator") {
        test("Arb.int 기본 edge case 포함") {
            val values = Arb.int()
                .samples(RandomSource.seeded(1L))
                .take(100)
                .map { it.value }

            values.all { it in Int.MIN_VALUE..Int.MAX_VALUE } shouldBe true
        }

        test("Arb.int: 0~100 범위 난수 10개 생성, 크기/범위 검증") {
            val values = Arb.int(0..100)
                .samples(RandomSource.seeded(1L))
                .take(10)
                .toList()
                .map { it.value }

            values.size shouldBe 10
            values.all { it in 0..100 } shouldBe true
        }

        test("Arb.double edge case (NaN, Infinity, MAX, MIN, ...)") {
            val values = Arb.double()
                .samples(RandomSource.seeded(1L))
                .take(200)
                .map { it.value }

            values.all { it in -Double.MAX_VALUE..Double.MAX_VALUE } shouldBe true
        }

        test("Arb.string edge case (empty string, min length 등)") {
            val values = Arb.string()
                .samples(RandomSource.seeded(1L))
                .take(100)
                .map { it.value }

            values.any { it.isEmpty() } shouldBe true
        }

        test("Arb.string & Arb.boolean: 임의 문자열(5~10자)와 랜덤 Bool 조합 생성 및 타입/길이 체크") {
            forAll(
                Arb.string(5..10),
                Arb.boolean()
            ) { str, flag ->
                println("str: $str, flag: $flag")
                str.length in 5..10 && (flag is Boolean)
            }
        }

        test("Arb.list(Int) edge case (empty list, single element, duplicates)") {
            val values = Arb.list(
                Arb.int(1..10),
                0..3
            )
                .samples(RandomSource.seeded(1L))
                .take(100)
                .map { it.value }

            values.any { it.distinct().size < it.size } shouldBe true
        }

        test("Arb.map(Int, String) edge case (empty map)") {
            val values = Arb.map(
                Arb.int(1..3),
                Arb.string(1..3),
                0,
                10,
                10
            ).samples(RandomSource.seeded(1L))
                .take(100)
                .map { it.value }

            values.any { it.isEmpty() } shouldBe true
        }
    }

    context("Exhaustive 유한 집합 generator") {
        test("Exhaustive.ints: 0~5 모든 정수 실제 값 리스트가 정확한지 검증") {
            val values = Exhaustive.ints(0..5)
                .values

            values shouldContainExactly listOf(0, 1, 2, 3, 4, 5)
            values.size shouldBe 6
            values.minOrNull() shouldBe 0
            values.maxOrNull() shouldBe 5
            values.forEach { it shouldBeInRange 0..5 }
        }

        test("IntProgression: 0~10 짝수만 Exhaustive.collection() & 값, 속성, 포함관계 검증") {
            val values = Exhaustive.collection(
                IntProgression.fromClosedRange(0, 10, 2)
                    .toList()
            ).values

            println(values.map { it.absoluteValue })

            values shouldContainAnyOf listOf(0, 2, 10)
            values.shouldContainExactly(0, 2, 4, 6, 8, 10)
            values.forEach { i -> check(i in 0..10) }
            values.distinct().size shouldBe values.size // 중복X
            values.all { it in 0..10 } shouldBe true
            values.first() shouldBe 0
            values.last() shouldBe 10
        }

        test("Exhaustive.boolean: true/false 두 값 모두 포함 및 타입 검증") {
            val boolean = Exhaustive.boolean()
            val values = boolean.values

            values.sorted() shouldBe listOf(false, true)
            values.size shouldBe 2
            values.toSet() shouldBe setOf(true, false)
        }
    }

    context("Arb와 Exhaustive 조합 프로퍼티 테스트 예시") {
        test("Arb(int 1~100) * Exhaustive(0~10) : 조합 case 모두 범위 만족") {
            forAll(
                Arb.int(1..100),
                Exhaustive.ints(0..10)
            ) { rand, exact ->
                rand in 1..100 && exact in 0..10
            }
        }

        test("Arb(문자열) * Exhaustive(true/false) : 조합시 문자열 길이와 bool 타입 검증") {
            forAll(
                Arb.string(3..6),
                Exhaustive.boolean()
            ) { str, b ->
                str.length in 3..6 && (b == true || b == false)
            }
        }
    }
})
