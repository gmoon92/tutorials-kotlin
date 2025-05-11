# [Collections](https://kotlinlang.org/docs/collections-overview.html)

## 1. Collection types
- Below is a diagram of the Kotlin collection interfaces:
![collection interfaces](images/collections-diagram.png)

## 2. List
```kotlin
val numbers = listOf("one", "two", "three", "four")
println("Number of elements: ${numbers.size}")
println("Third element: ${numbers.get(2)}")
println("Fourth element: ${numbers[3]}")
println("Index of element \"two\" ${numbers.indexOf("two")}")
```

## 3. Set
```kotlin
val numbers = setOf(1, 2, 3, 4)
println("Number of elements: ${numbers.size}")
if (numbers.contains(1)) println("1 is in the set")

val numbersBackwards = setOf(4, 3, 2, 1)
println("The sets are equal: ${numbers == numbersBackwards}")
```

## 4. Operations
### 4.1 Map
```kotlin
val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key4" to 1)

println("All keys: ${numbersMap.keys}")
println("All values: ${numbersMap.values}")
if ("key2" in numbersMap) println("Value by key 'key2': ${numbersMap["key2"]}")    
if (1 in numbersMap.values) println("The value 1 is in the map")
if (numbersMap.containsValue(1)) println("The value 1 is in the map")
```

### 4.2 Transformation
```kotlin
val numbers = setOf(1, 2, 3)
println(numbers.map { it * 3 }) //[3, 6, 9]
println(numbers.mapIndexed { idx, value -> value * idx }) //[0, 2, 6]
```

### 4.3 Filter
```kotlin
val numbers = listOf("one", "two", "three", "four")  
val longerThan3 = numbers.filter { it.length > 3 }
println(longerThan3) //[three, four]

val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key11" to 11)
val filteredMap = numbersMap.filter { (key, value) -> key.endsWith("1") && value > 10}
println(filteredMap) // {key11=11}
```

### 4.4 Grouping
```kotlin
val numbers = listOf("one", "two", "three", "four", "five")

val groupedByFirstLetter = numbers.groupBy { it.first().uppercase() }
println(groupedByFirstLetter) // {O=[one], T=[two, three], F=[four, five]}
```
