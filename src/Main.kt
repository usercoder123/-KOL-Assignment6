import models.Animal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

val animals: List<Animal> = listOf(
    Animal("Snake", false, 1, 15, true),
    Animal("Elephant", false, 2, 4, true),
    Animal("Jaguar", false, 3, 2, false),
    Animal("Hyenas", false, 4, 3, false),
    Animal("Whale", false, 5, 7, true),
    Animal("Carp", false, 6, 23, false),
    Animal("Ape", false, 7, 26, true),
    Animal("Tiger", false, 8, 6, true),
    Animal("Lion", false, 9, 12, true),
    Animal("Dinosaur", false, 10, 5, false)
)

fun main() {
    print("Please enter your date of birth (dd/MM/yyyy): ")
    val dob = inputDOB().getDayMonthFormat()
    val categorizedList = getCategorizedAnimalsList(animals, dob)

    println("* Having lunch:")
    val lunchList = categorizedList.getLunchReport()

    println("\n* After lunch:")
    val afterLunchList = getAnimalReportAfterLunch(lunchList)

    println("\n* Remaining animals:")
    val updatedList = animals.updateAnimalList(afterLunchList)
    updatedList.filter { it != Animal(null, null, null, null, null) }
        .forEach { println("- $it") }

    println("\n* Highest dangerous degree: " + updatedList.getHighestDangerDegreeAnimal().name)

}

fun List<Animal>.getHighestDangerDegreeAnimal(): Animal {

    return this.maxBy { it.dangerousDegree ?: 0 }
}

fun getAnimalReportAfterLunch(lunchReport: MutableMap<Animal, Animal>): ArrayList<Animal> {
    val deadAnimals = ArrayList<Animal>()
    lunchReport.forEach {
        val eaten = it.key
        val eater = it.value
        deadAnimals.add(eaten)
        if (eaten.isPoisonous!! && !deadAnimals.contains(eater)) {
            eater.died()
            deadAnimals.add(eater)
        }
    }
    return deadAnimals
}

fun List<Animal>.updateAnimalList(deadAnimals: ArrayList<Animal>): List<Animal> {
    this.map {
        if (deadAnimals.contains(it)) {
            it.dead()
        }
        if (it.dangerousDegree != null && it.dangerousDegree!! > 15) {
            it.evolution()
        }
    }
    return this
}

fun MutableMap<Animal, String>.getLunchReport(): MutableMap<Animal, Animal> {
    val groupA = this.getGroupList("Group A").toSortedMap(compareByDescending { it.dangerousDegree })
    val groupB = this.getGroupList("Group B").toSortedMap(compareBy { it.dangerousDegree }).toMutableMap()
    val result = mutableMapOf<Animal, Animal>()

//    if there are still animals to eat
    groupA.forEach {
        if (groupB.isNotEmpty()) {
//            animal's eating
            val eatenAnimal = groupB.entries.toList()[0].key
            it.key.eat(eatenAnimal)
//            eaten animal dies -> remove from B list
            eatenAnimal.died()
            groupB.remove(eatenAnimal)
//            eater eats more than 1 -> cant be key as key is not duplicate
            result[eatenAnimal] = it.key

//      TODO("Try to simplify this later(turn into function)")
            if (groupB.entries.size >= 1) {
                val eatenAnimal2 = groupB.entries.toList()[0].key
                it.key.eat(eatenAnimal2)
//            eaten animal dies -> remove from B list
                eatenAnimal2.died()
                groupB.remove(eatenAnimal2)
//            eater eats more than 1 -> cant be key as key is not duplicate
                result[eatenAnimal2] = it.key
            }
        }
    }
    return result
}

fun MutableMap<Animal, String>.getGroupList(groupName: String): Map<Animal, String> {
    return this.filter { it.value == groupName }
}

fun getCategorizedAnimalsList(animals: List<Animal>, dob: String): MutableMap<Animal, String> {
    val resultList = mutableMapOf<Animal, String>()
    for (i in animals.indices) {
        if (i.toString().isInIndexesOf(dob)) {
            resultList[animals[i]] = "Group A"
        } else {
            resultList[animals[i]] = "Group B"
        }
    }
    return resultList
}

fun String.isInIndexesOf(dob: String): Boolean {
    val indexes = dob.toCharArray().map { it.toString() }
    indexes.forEach {
        if (this == it) {
            return true
        }
    }
    return false
}

fun String.getDayMonthFormat(): String {
    val dateMonthYear = this.split("/")
    return dateMonthYear[0] + dateMonthYear[1]
}

fun inputDOB(): String {
    val input = readlnOrNull()
    if (input?.let { isDOBValid(it) } == true) {
        return input
    }
    print("Invalid input! Please try again: ")
    return inputDOB()
}

fun isDOBValid(dob: String): Boolean {
    try {
        LocalDate.parse(
            dob, DateTimeFormatter
                .ofPattern(enums.DateFormat.DAYMONTHYEAR.formatter)
        )
    } catch (e: DateTimeParseException) {
        return false
    }
    return true
}

