package org.example

import java.util.ArrayList

fun main() {
    fun generateReport(podaci : List<List<Any>>, headerUse: Boolean){


            val novaLista : MutableList<List<Any>> = podaci.toMutableList()
            val header : MutableList<String> = ArrayList()
            for(i in 0..<podaci[0].size) {
                header.add(podaci[0][i].toString())
            }
            novaLista.removeAt(0)
            println(novaLista)

    }

    val listaUListi: List<List<Any>> = listOf(
        listOf("Prvi", "Drugi", "Treći"),
        listOf(1, 2, 3),
        listOf(4, 5, 6),
        listOf(7, 8, 9)
    )
    val listaStringova: List<String> = listOf("Prvi", "Drugi", "Treći")

    generateReport(listaUListi,true)



}