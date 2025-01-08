

import Tip
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.ArrayList
import java.util.Objects
import kotlin.jvm.Throws
/**
 * Apstraktna klasa koja služi za generisanje različitih tipova izveštaja.
 */
abstract class Specifikacija {

    abstract var tip : Tip
    abstract var extenzija : Extenzija

    /**
     * Apstraktna metoda koja generiše izveštaj na osnovu prosleđenih parametara.
     *
     * @param izvestaj Objekat klase Izvestaj
     * @param pathToFile Putanja do fajla gde će izveštaj biti sačuvan
     */
    abstract fun genR(izvestaj: Izvestaj, pathToFile: String)

    /**
     * Generiše model iz liste podataka i poziva abstraktnu funkciju genR koja kreira izvestaj.
     *
     * @param podaci Lista u listi koja će biti prosledjeni za kreiranje modela
     * @param pathToFile Putanja do fajla gde će izveštaj biti sačuvan
     */
    fun generateReport(podaci : List<List<Any>>, pathToFile: String){
        var izvestaj = Izvestaj(podaci)
        genR(izvestaj, pathToFile)
    }

    /**
     * Generiše model sa zaglavljem.
     *
     * @param podaci Lista u listi koja će prosledjeni za kreiranje modela
     * @param header Lista naziva header-a
     * @param pathToFile Putanja do fajla gde će izveštaj biti sačuvan
     */
    fun generateReport(podaci : List<List<Any>>, header : List<String>, pathToFile: String){
        var izvestaj = Izvestaj(podaci, null, header)
        genR(izvestaj, pathToFile)
    }

    /**
     * Generiše model sa naslovom i rezimeom.
     *
     * @param podaci Lista u listi koja će biti proslednjena za kreiranje modela
     * @param title Naslov modela
     * @param rezime Mapa koja sadrži podatke za rezime
     * @param pathToFile Putanja do fajla gde će izveštaj biti sačuvan
     */
    fun generateReport(podaci : List<List<Any>>, title : String, rezime : Map<String, Int>, pathToFile: String){
        var izvestaj = Izvestaj(podaci, title)
        izvestaj.izracunajSummary(rezime)
        genR(izvestaj, pathToFile)
    }

    /**
     * Generiše model sa zaglavljem, naslovom i rezimeom.
     *
     * @param podaci Lista u listi koja će biti proslednjena za kreiranje modela
     * @param header Lista naziva header-a
     * @param title Naslov modela
     * @param rezime Mapa koja sadrži podatke za rezime
     * @param pathToFile Putanja do fajla gde će izveštaj biti sačuvan
     */
    fun generateReport(podaci : List<List<Any>>, header : List<String>, title : String, rezime : Map<String, Any>, pathToFile: String){
        var izvestaj = Izvestaj(podaci, title, header)
        izvestaj.izracunajSummary(rezime)
        genR(izvestaj, pathToFile)
    }

    /**
     * Generiše model iz SQL upita.
     *
     * @param conn Konekcija ka bazi podataka i poziva funkciju generateReport(rs, headerUse, pathToFile)
     * @param query SQL upit
     * @param headerUse Označava da li će se koristiti header-i
     * @param pathToFile Putanja do fajla gde će izveštaj biti sačuvan
     */
    @Throws(SQLException::class)
    fun generateReport(conn : Connection, query : String, headerUse: Boolean, pathToFile: String){
        val statement : Statement = conn.createStatement()
        val rs : ResultSet = statement.executeQuery(query)
        generateReport(rs, headerUse, pathToFile)
    }

    /**
     * Generiše izveštaj iz ResultSet-a.
     *
     * @param rs ResultSet koji sadrži podatke i kreira model
     * @param headerUse Označava da li će se koristiti header-i iz ResultSet-a
     * @param pathToFile Putanja do fajla gde će izveštaj biti sačuvan
     */
    fun generateReport(rs : ResultSet, headerUse : Boolean, pathToFile : String){
        val all : MutableList<List<String>> = ArrayList()
        val podaci = rs.metaData
        val numberOfColumns = podaci.columnCount
        while(rs.next()){
            val row : MutableList<String> = ArrayList()
            for(i in 1..numberOfColumns){
                row.add(rs.getString(i))
            }
            all.add(row)
        }
        if(!headerUse) generateReport(all, pathToFile)
        else{
            val header : MutableList<String> = ArrayList()
            for(i in 1..numberOfColumns) {
                header.add(podaci.getColumnLabel(i))
            }
            generateReport(all, header, pathToFile)
        }
    }

    /**
     * Generiše model iz liste podataka sa opcijom za header.
     *
     * @param podaci Lista u listi
     * @param headerUse Označava da li će se prva lista koristiti kao zaglavlje
     * @param pathToFile Putanja do fajla gde će izveštaj biti sačuvan
     */
    fun generateReport(podaci : List<List<Any>>, headerUse: Boolean, pathToFile: String){
        if(!headerUse) generateReport(podaci, pathToFile)
        else{
            val novaLista : MutableList<List<Any>> = podaci.toMutableList()
            val header : MutableList<String> = ArrayList()
            for(i in 0..<podaci[0].size) {
                header.add(podaci[0][i].toString())
            }
            novaLista.removeAt(0)
            generateReport(podaci, header, pathToFile)
        }
    }

    /**
     * Generiše model iz mape podataka.
     *
     * @param podaci Mapa gde su ključevi nazivi kolona, a vrednost lista u listi
     * @param pathToFile Putanja do fajla gde će izveštaj biti sačuvan
     */
    fun generateReport(podaci : Map<String, List<Any>>,pathToFile: String){
        var header : MutableList<String> = ArrayList()
        var all : MutableList<List<Any>> = ArrayList()
        for ((key, value) in podaci) {
            header.add(key)
            all.add(value)
        }
        fun transpose(rows: List<List<Any>>): MutableList<List<Any>> {
            if (rows.isEmpty()) return mutableListOf()
            val numberOfColumns = rows[0].size
            return MutableList(numberOfColumns) { colIndex ->
                rows.map { it[colIndex] }
            }
        }//promena redova u kolone iz mape, da bi moglo da se prosledi kao lista u listi
        val transposed: MutableList<List<Any>> = transpose(all)

        generateReport(transposed, header, pathToFile)

    }

    /**
     * Generiše model iz liste podataka sa header-om, naslovom i rezimeom.
     *
     * @param podaci Lista u listi
     * @param headerUse Označava da li će se prva lista koristiti kao header
     * @param title Naslov model-a
     * @param rezime Mapa koja sadrži podatke za rezime
     * @param pathToFile Putanja do fajla gde će izveštaj biti sačuvan
     */
    fun generateReport(podaci : List<List<Any>>, headerUse: Boolean, title : String, rezime: Map<String, Int>, pathToFile: String){
        if(!headerUse) generateReport(podaci,title, rezime, pathToFile)
        else{
            val novaLista : MutableList<List<Any>> = podaci.toMutableList()
            val header : MutableList<String> = ArrayList()
            for(i in 0..<podaci[0].size) {
                header.add(podaci[0][i].toString())
            }
            novaLista.removeAt(0)
            generateReport(podaci, header, title, rezime, pathToFile)
        }
    }

    /**
     * Generiše model iz mape podataka sa naslovom i rezimeom.
     *
     * @param podaci Mapa gde su ključevi nazivi kolona, a vrednost lista u listi
     * @param title Naslov modela
     * @param rezime Mapa koja sadrži podatke za rezime
     * @param pathToFile Putanja do fajla gde će izveštaj biti sačuvan
     */
    fun generateReport(podaci : Map<String, List<Any>>, title: String, rezime: Map<String, Any>, pathToFile: String){
        var header : MutableList<String> = ArrayList()
        var all : MutableList<List<Any>> = ArrayList()
        for ((key, value) in podaci) {
            header.add(key)
            all.add(value)
        }
        fun transpose(rows: List<List<Any>>): MutableList<List<Any>> {
            if (rows.isEmpty()) return mutableListOf()
            val numberOfColumns = rows[0].size
            return MutableList(numberOfColumns) { colIndex ->
                rows.map { it[colIndex] }
            }
        }//promena redova u kolone iz mape, da bi moglo da se prosledi kao lista u listi
        val transposed: MutableList<List<Any>> = transpose(all)

        generateReport(transposed, header, title, rezime, pathToFile)

    }









}