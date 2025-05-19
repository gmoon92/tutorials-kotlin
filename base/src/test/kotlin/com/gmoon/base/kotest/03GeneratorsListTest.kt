package com.gmoon.base.kotest

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.RandomSource
import io.kotest.property.arbitrary.*
import io.kotest.property.exhaustive.boolean

/**
 * ## Kotest Property Test Generators 요약
 *
 * - Kotest는 다양한 데이터 타입의 프로퍼티 테스트(랜덤/Exhaustive) 입력값 생성을 위해 여러 Generator(Arb, Exhaustive)를 제공한다.
 * - 주로 사용하는 타입별 Generator 아비스트(Arb.*, Exhaustive.*)들:
 *
 * ### 1. 숫자 관련 (숫자 타입의 난수 또는 Exhaustive)
 * - Arb.int(), Arb.long(), Arb.double(), Arb.float(), Arb.short(), Arb.byte()
 * - Exhaustive.ints(), Exhaustive.longs(), Exhaustive.shorts(), Exhaustive.bytes()
 *
 * ### 2. 문자열 & 문자
 * - Arb.string(), Arb.char() : 임의의 문자열/문자
 * - 다양한 옵션: 길이, 알파벳 집합, 패턴 등 지정 가능
 *
 * ### 3. 불린/비트
 * - Arb.boolean(), Exhaustive.boolean()
 *
 * ### 4. 컬렉션/컨테이너
 * - Arb.list(generator, sizeRange) : 임의 길이와 임의 원소의 리스트
 * - Arb.set(generator, sizeRange)
 * - Arb.map(keyGen, valueGen, minSize, maxSize, maxDups)
 * - Exhaustive.collection(list) : 입력 리스트의 모든 원소
 *
 * ### 5. 날짜/시간
 * - Arb.localDate(), Arb.localDateTime(), Arb.localTime(), Arb.month(), Arb.instant(), ...
 *
 * ### 6. 객체/구조
 * - Arb.enum<T>() : 열거형 타입의 임의 값
 * - Arb.pair(), Arb.triple(), Arb.bind() 등으로 복합 타입 조합 가능
 *
 * ### 7. 그 외 특수 Generator
 * - Arb.nullable(generator, probability): nullable 변수 테스트용
 * - Arb.constant(value): 특정 상수 반복
 * - Arb.choose(vararg generators): 여러 Arb 중 하나 랜덤 선택
 * - Arb.bind(gen1, gen2, ...) { ... } : 여러 generator를 묶어서 복합 객체 생성
 *
 * ### 팁
 * - 사용 사례에 맞춰 Arb (랜덤, 얽힘 있음), Exhaustive (모든 값) 골라 사용
 * - .samples(), .values 등으로 실제 샘플/셋 가져올 수 있음.
 * - .filter(), .map(), .zip()으로 후처리나 조합 가능
 *
 * [Kotest Generators List](https://kotest.io/docs/proptest/property-test-generators-list.html)
 * [KDoc](https://kotlinlang.org/docs/kotlin-doc.html#block-tags)
 */
class `03GeneratorsListTest` : FunSpec({

    context("Generator") {
        test("arb.orNull") {
            val orNull = Arb.int()
                .orNull()
                .sample(RandomSource.default())
                .value

            println("value is $orNull")

            orNull should { value ->
                (value == null || value is Int) shouldBe true
            }
        }

        test(
            "arb.orNull(nullProbability): " +
                    "null 이 나올 확룰(probability)를 지정해서 nullable arbitrary 를 만들 수 있는 함수"
        ) {
            val maybeNull = Arb.int()
                .orNull(nullProbability = 1.0)
                .sample(RandomSource.default())
                .value

            println("value is null: ${maybeNull == null}")

            maybeNull shouldBe null
        }
    }

    context("Booleans") {
        test("Arb.boolean") {
            val booleans = Arb.boolean()
                .take(5)
                .map { it }
                .toList()

            println("value is $booleans")

            booleans.all { value ->
                value is Boolean
            } shouldBe true
        }

        test("Arb.booleanArray(length, content)") {
            val sample = Arb.booleanArray(
                Arb.int(1..100),
                Arb.boolean()
            )
                .sample(RandomSource.default())
                .value

            println("value is ${sample.size}")

            sample.size shouldBeInRange 1..100
        }

        test("Exhaustive.boolean(): Alternatives between true and false.") {
            val values = Exhaustive.boolean()
                .values

            println("values is $values")

            values.size shouldBe 2
        }
    }

    context("Collection Arbitrary") {
        test("Arb.list()/Arb.set()/Arb.map()") {
            val list = Arb.list(Arb.int(1..3), 2..4).sample().value
            val set = Arb.set(Arb.char(), 2..4).sample().value
            Arb.map()
            val map = Arb.map(
                Arb.string(2, 4),
                Arb.int(0..9),
                2..2
            )
                .sample().value
            println("list=$list, set=$set, map=$map")
        }
        test("Exhaustive.collection()") {
            Exhaustive.collection(listOf(1, 2, 3)).values shouldContainAll listOf(1, 2, 3)
        }
    }

    context("날짜/시간 Arbitrary") {
        test("Arb.localDate()/Arb.localDateTime()/Arb.instant()") {
            val d = Arb.localDate().sample().value
            val dt = Arb.localDateTime().sample().value
            val inst = Arb.instant().sample().value
            println("localDate=$d, localDateTime=$dt, instant=$inst")
        }
    }

    context("Enum/복합 Arbitrary") {
        enum class Color { RED, BLUE }
        test("Arb.enum()/Arb.pair()/Arb.triple()") {
            val color = Arb.enum<Color>().sample().value
            val pair = Arb.pair(Arb.string(1, 2), Arb.int(1..10)).sample().value
            val triple = Arb.triple(Arb.int(), Arb.boolean(), Arb.string(2, 3)).sample().value
            println("color=$color, pair=$pair, triple=$triple")
        }
    }

    context("null, Constant, Choose, Bind") {
        test("Arb.orNull()/Arb.constant()/Arb.choose()/Arb.bind()") {
            val nullableInt = Arb.int().orNull(0.5).sample().value
            val constantString = Arb.constant("kotest").sample().value
            val chosen = Arb.choose(Arb.int(), Arb.string(1..2)).sample().value
            val bound = Arb.bind(Arb.string(2, 3), Arb.int(0..5)) { s, i -> "$s-$i" }.sample().value
            println("nullableInt=$nullableInt, constant=$constantString, chosen=$chosen, bound=$bound")
        }
    }

    context("Primitive Array Arbitrary") {
        test("Arb.booleanArray()/Arb.byteArray()") {
            val boolArr = Arb.booleanArray(Arb.int(2..4), Arb.boolean()).sample().value
            val byteArr = Arb.byteArray(Arb.int(2..4), Arb.byte()).sample().value
            println("booleanArray=${boolArr.toList()}, byteArray=${byteArr.toList()}")
        }
    }
})
