import kotlin.random.Random
// start values
val food_pairs = 60
val start_Cuckolds = 119
val start_Alphas = 1
val cycles = 30
//
var population = mutableListOf<Creature>()
var field = mutableListOf<Pair<Creature?, Creature?>>()

fun main() {

// filling a pop
    for (i in 1..start_Cuckolds) population.add(Cuckold())
    for (i in 1..start_Alphas) population.add(Alpha())
// life cycle
    for (cycle in 1..cycles) {
    // filling a field
        field = mutableListOf<Pair<Creature?, Creature?>>()
        for (i in 1..food_pairs) field.add(Pair(null, null))
        for (creature in population) {
            while (true) {
                val r_pair = Random.nextInt(field.size)
                if (field[r_pair].first == null) {
                    field[r_pair] = Pair(creature, null)
                    break
                } else if (field[r_pair].second == null) {
                    field[r_pair] = Pair(field[r_pair].first, creature)
                    break
                }
            }
        }
    population = mutableListOf<Creature>()
    // using creature's behavior
        for (pair in field) {
            if (pair.first != null && pair.second != null) {
                val first = pair.first
                val second = pair.second
                first!!.behavior(second!!.type)
                second!!.behavior(first!!.type)
            } else if (pair.first != null) {
                val first = pair.first
                first!!.behavior(null)
            }
        }
        println(population.size)
        //for(pair in field)println(pair)
        var cuck = 0
        var alph = 0
        for (i in population) {
            if (i.type == "Cuckold") cuck ++ else alph ++
        }
        println("Cuck:$cuck, Alph:$alph")
    }
}

open abstract class Creature(var type:String = "Creature", var food:Double = 0.0) {
    open fun behavior(opponent:String?){}
}
class Cuckold:Creature(type = "Cuckold", food = 0.0) {
     override fun behavior(opponent:String?){
         if (opponent == "Cuckold") food += 1.0 else if (opponent == null) food += 2.0 else if (opponent == "Alpha") food += 0.5

         if ((population.size < food_pairs*2 && food == 1.0) || (population.size < food_pairs*2 && food == 0.5 && Random.nextInt(100) <= 49)) {
             population.add(Cuckold())
         } else if (food == 2.0) {
             if (population.size + 1 < food_pairs * 2) {
                population.add(Cuckold())
                population.add(Cuckold())
             } else if (population.size < food_pairs * 2) population.add(Cuckold())
         }
     }
}
class Alpha:Creature(type = "Alpha", food = 0.0) {
    override fun behavior(opponent:String?){
        if (opponent == "Cuckold") food += 1.5 else if (opponent == null) food += 2

        if ((population.size < food_pairs*2 && food == 1.0) || (population.size < food_pairs*2 && food == 1.5 && Random.nextInt(100) <= 49)) {
            population.add(Alpha())
        } else if (food >= 1.5){
            if (population.size + 1 < food_pairs * 2) {
                population.add(Alpha())
                population.add(Alpha())
            } else if (population.size < food_pairs * 2) population.add(Alpha())
        }
    }
}