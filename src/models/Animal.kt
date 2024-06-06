package models

data class Animal(
    var name: String?,
    var evolution: Boolean?,
    var dangerousDegree: Int?,
    var yearOld: Int?,
    var isPoisonous: Boolean?
){
    fun died(){
        println("-> " + this.name + " died")
    }

    fun dead(){
        this.name = null
        this.evolution = null
        this.dangerousDegree = null
        this.yearOld = null
        this.isPoisonous = null
    }

    fun eat(eatedAnimal: Animal){
        println("- " + this.name + " eats " + eatedAnimal.name)
        this.dangerousDegree = this.dangerousDegree!! + eatedAnimal.dangerousDegree!!
    }

    fun evolution(){
        println("(!) " + this.name + " is evoluting")
        this.evolution = true
    }
}