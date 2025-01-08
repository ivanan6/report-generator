

import Izvestaj
import Specifikacija
import Tip
import java.awt.Color
import java.io.File
import java.util.Scanner
import java.util.ServiceLoader
import kotlin.math.exp


fun main() {
    val serviceLoader = ServiceLoader.load(Specifikacija::class.java)

    val exportedServices = mutableMapOf<Tip, Specifikacija>()
    serviceLoader.forEach { service ->
        exportedServices.put(service.tip, service)
    }

    println("Unesite path fajla")
    //C:\Users\hp\Desktop\Spisak.txt
    val scanner = Scanner(System.`in`)

    val filePath: String = scanner.nextLine().toString()

    val file = File(filePath)
    println("Ucitavanje...")
//    val listOfLists: List<List<Any>> = file.readLines().map { line ->
//        line.split(",").map { it.trim().removeSurrounding("\"") }
//    }
    val listOfLists: List<List<Any>> = file.readLines().map { line ->
        line.split(",").map { value ->
            // Trim whitespaces and remove surrounding quotes
            val cleanedValue = value.trim().removeSurrounding("\"")

            // Attempt to convert to Int first
            val numberValue = cleanedValue.toIntOrNull()

            // If the conversion to Int fails, attempt to convert to Double
            if (numberValue != null) {
                numberValue  // It was an integer, so keep it as an Int
            } else {
                val doubleValue = cleanedValue.toDoubleOrNull()
                if (doubleValue != null) {
                    doubleValue  // It was a double, so keep it as a Double
                } else {
                    cleanedValue  // If it's neither an Int nor a Double, keep it as a String
                }
            }
        }
    }
    //print(listOfLists)
    println("Zavrseno ucitavanje")

    println("1.Dodati podatke bez kreiranja modela\n2.Dodati podatke i kreirati model")

    var odgovor = Scanner(System.`in`)
    if (odgovor.nextInt() == 1) {
        var listaFunkcija: MutableList<String> = mutableListOf(
            "generateReport(podaci : List<List<Any>>, pathToFile: String)",
            "\ngenerateReport(podaci : List<List<Any>>, header : List<String>, pathToFile: String)",
            "\ngenerateReport(podaci : List<List<Any>>, title : String, rezime : Map<String, Int>, pathToFile: String)",
            "\ngenerateReport(podaci : List<List<Any>>, header : List<String>, title : String, rezime : Map<String, Int>, pathToFile: String)",
            "\ngenerateReport(conn : Connection, query : String, headerUse: Boolean, pathToFile: String)",
            "\ngenerateReport(rs : ResultSet, headerUse : Boolean, pathToFile : String)",
            "\ngenerateReport(podaci : List<List<Any>>, headerUse: Boolean, pathToFile: String)",//
            "\ngenerateReport(podaci : Map<String, List<Any>>,pathToFile: String)",
            "\ngenerateReport(podaci : List<List<Any>>, headerUse: Boolean, title : String, rezime: Map<String, Int>, pathToFile: String)",//
            "\ngenerateReport(podaci : Map<String, List<Any>>, title: String, rezime: Map<String, Int>, pathToFile: String)"
        )
        println("Opcije koje mozete da izaberete:\n" + listaFunkcija)
        odgovor = Scanner(System.`in`)
        var odg = odgovor.nextLine()
        println("Izaberite tip izvestaja koji zelite da izaberete:\n" + exportedServices.keys)
        var izvestaj = Scanner(System.`in`)
        var iz = izvestaj.nextLine()
        println("Unesite gde zelite da kreiramo vas izvestaj:")
        var path = Scanner(System.`in`)
        var pt = path.nextLine()
        var fund: String = listaFunkcija.get(odg.toInt())
        var funckija = fund.trimStart()//ucitavam iz liste
        val headeri = listOf("Ime", "Prezime", "Brojgodina", "Plata", "Grad")//headeri
        val summaryMapa: Map<String, Int> = mapOf("sum" to 50)
        if (iz.equals("PDF")) {
            if (funckija.equals("generateReport(podaci : List<List<Any>>, pathToFile: String)")) {
                exportedServices.get(Tip.PDF)!!.generateReport(listOfLists, pt)

            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, header : List<String>, pathToFile: String)")) {
                exportedServices.get(Tip.PDF)!!.generateReport(listOfLists, headeri, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, title : String, rezime : Map<String, Int>, pathToFile: String)")) {
                exportedServices.get(Tip.PDF)!!.generateReport(listOfLists, "Podaci", summaryMapa, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, header : List<String>, title : String, rezime : Map<String, Int>, pathToFile: String)")) {
                exportedServices.get(Tip.PDF)!!.generateReport(listOfLists, headeri, "Podaci", summaryMapa, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, headerUse: Boolean, pathToFile: String)")) {
                exportedServices.get(Tip.PDF)!!.generateReport(listOfLists, false, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, headerUse: Boolean, title : String, rezime: Map<String, Int>, pathToFile: String)")) {
                exportedServices.get(Tip.PDF)!!.generateReport(listOfLists, false, "Podaci", summaryMapa, pt)
            }
        } else if (iz.equals("CSV")) {
            if (funckija.equals("generateReport(podaci : List<List<Any>>, pathToFile: String)")) {
                exportedServices.get(Tip.CSV)!!.generateReport(listOfLists, pt)

            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, header : List<String>, pathToFile: String)")) {
                exportedServices.get(Tip.CSV)!!.generateReport(listOfLists, headeri, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, title : String, rezime : Map<String, Int>, pathToFile: String)")) {
                exportedServices.get(Tip.CSV)!!.generateReport(listOfLists, "Podaci", summaryMapa, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, header : List<String>, title : String, rezime : Map<String, Int>, pathToFile: String)")) {
                exportedServices.get(Tip.CSV)!!.generateReport(listOfLists, headeri, "Podaci", summaryMapa, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, headerUse: Boolean, pathToFile: String)")) {
                exportedServices.get(Tip.CSV)!!.generateReport(listOfLists, false, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, headerUse: Boolean, title : String, rezime: Map<String, Int>, pathToFile: String)")) {
                exportedServices.get(Tip.CSV)!!.generateReport(listOfLists, false, "Podaci", summaryMapa, pt)
            }
        } else if (iz.equals("PLAIN_TEXT")) {
            if (funckija.equals("generateReport(podaci : List<List<Any>>, pathToFile: String)")) {
                println("USAO")
                exportedServices.get(Tip.PLAIN_TEXT)!!.generateReport(listOfLists, pt)

            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, header : List<String>, pathToFile: String)")) {
                println("EEE")
                exportedServices.get(Tip.PLAIN_TEXT)!!.generateReport(listOfLists, headeri, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, title : String, rezime : Map<String, Int>, pathToFile: String)")) {
                exportedServices.get(Tip.PLAIN_TEXT)!!.generateReport(listOfLists, "Podaci", summaryMapa, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, header : List<String>, title : String, rezime : Map<String, Int>, pathToFile: String)")) {
                exportedServices.get(Tip.PLAIN_TEXT)!!.generateReport(listOfLists, headeri, "Podaci", summaryMapa, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, headerUse: Boolean, pathToFile: String)")) {
                exportedServices.get(Tip.PLAIN_TEXT)!!.generateReport(listOfLists, false, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, headerUse: Boolean, title : String, rezime: Map<String, Int>, pathToFile: String)")) {
                exportedServices.get(Tip.PLAIN_TEXT)!!.generateReport(listOfLists, false, "Podaci", summaryMapa, pt)
            }
        } else if (iz.equals("EXCEL")) {
            if (funckija.equals("generateReport(podaci : List<List<Any>>, pathToFile: String)")) {
                exportedServices.get(Tip.EXCEL)!!.generateReport(listOfLists, pt)

            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, header : List<String>, pathToFile: String)")) {
                exportedServices.get(Tip.EXCEL)!!.generateReport(listOfLists, headeri, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, title : String, rezime : Map<String, Int>, pathToFile: String)")) {
                exportedServices.get(Tip.EXCEL)!!.generateReport(listOfLists, "Podaci", summaryMapa, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, header : List<String>, title : String, rezime : Map<String, Int>, pathToFile: String)")) {
                exportedServices.get(Tip.EXCEL)!!.generateReport(listOfLists, headeri, "Podaci", summaryMapa, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, headerUse: Boolean, pathToFile: String)")) {
                exportedServices.get(Tip.EXCEL)!!.generateReport(listOfLists, false, pt)
            } else if (funckija.equals("generateReport(podaci : List<List<Any>>, headerUse: Boolean, title : String, rezime: Map<String, Int>, pathToFile: String)")) {
                exportedServices.get(Tip.EXCEL)!!.generateReport(listOfLists, false, "Podaci", summaryMapa, pt)
            }
        }

        odgovor.close()
        izvestaj.close()
        path.close()
        scanner.close()
    } else {
        val headeri = listOf("Ime", "Prezime", "Brojgodina", "Plata", "Grad")//headeri
        println("Da li hocete uz model da kreirate header?")
        var head = Scanner(System.`in`)
        val h = head.nextLine()
        println("Da li hocete uz model da kreirate title?")
        var title = Scanner(System.`in`)
        var tit = title.nextLine()
        var izvestaj: Izvestaj
        if (h.toString().equals("da") && tit.toString().equals("da")) {
            izvestaj = Izvestaj(listOfLists, "Podaci", headeri)
        } else if (h.toString().equals("da") && tit.toString().equals("ne")) {
            izvestaj = Izvestaj(listOfLists, null, headeri)
        } else if (h.toString().equals("ne") && tit.toString().equals("da")) {
            izvestaj = Izvestaj(listOfLists, "Podaci", null)
        } else {
            izvestaj = Izvestaj(listOfLists)
        }
        println("Da li zelite da boldujete kolone")
        var s = Scanner(System.`in`)
        if (s.nextLine().equals("da")) {
            println("Koje kolone zelite da boldujete")
            val scanner = Scanner(System.`in`)

            val input = scanner.nextLine()

            val numbers = input.split(" ").map { it.toInt() }
            for (number in numbers) {
                izvestaj.getPodaci().get(number).bold(true)
            }
        }
        println("Da li zelite da promenite boju kolonama")
        s = Scanner(System.`in`)
        if (s.nextLine().equals("da")) {
            println("Koje kolone zelite da obojite")
            val scanner = Scanner(System.`in`)

            val input = scanner.nextLine()

            val numbers = input.split(" ").map { it.toInt() }
            for (number in numbers) {
                izvestaj.getPodaci().get(number).bc_color(true, Color.red)
            }
        }
        println("Da li zelite da promenite boju headera")
        s = Scanner(System.`in`)
        if (s.nextLine().equals("da")) {
            izvestaj.getHeader()!!.bc_color(true, Color.red)
        }
        println("Da li zelite da boldujete headera")
        s = Scanner(System.`in`)
        if (s.nextLine().equals("da")) {
            izvestaj.getHeader()!!.bold(true)
        }
        println("Da li zelite da formatirate title")
        s = Scanner(System.`in`)
        if (s.nextLine().equals("da")) {
            println("Unesite brojeve za formatiranje:\n" +
                    "1.Bold\n" +
                    "2.Italic\n" +
                    "3.Underline\n" +
                    "4.Color")
            val scanner = Scanner(System.`in`)

            val input = scanner.nextLine()

            val numbers = input.split(" ").map { it.toInt() }
            val boolList: MutableList<Boolean> = mutableListOf(false, false, false, false)
            for (number in numbers) {
                boolList[number - 1] = true
            }
            if(boolList[3]) {
                izvestaj?.getTitle()!!.formatNaslov(boolList.get(0), boolList.get(1),boolList.get(2), Color.red)
            }
            else
                izvestaj?.getTitle()!!.formatNaslov(boolList.get(0), boolList.get(1),boolList.get(2))


            }
        println("Da li zelite da kreirate summary")
        var i = Scanner(System.`in`)
        if(i.nextLine().equals("da")){
            println("Unesite operaciju:\n" +
                    "1.sum\n" +
                    "2.prosek\n" +
                    "3.count\n" +
                    "4.count if 'nesto'")
            i = Scanner(System.`in`)
            val ii = i.nextLine()
            println("Unesite kolone nad kojima zelite da se to izvrsi")
            val scanner = Scanner(System.`in`)

            var input = scanner.nextLine()

            var numbers = input.split(" ").map { it.toInt() }
            var lista : MutableList<Int> = mutableListOf()
            for (number in numbers) {
               lista.add(number)
            }

            var mapa = mutableMapOf(ii to lista )
            izvestaj.izracunajSummary(mapa)
        }
        println("Da li zelite da dodate kolone")
        i = Scanner(System.`in`)
        if(i.nextLine().equals("da")){
            println("Unesite operaciju:\n" +
                    "1. +\n" +
                    "2. *\n" +
                    "3. -\n" +
                    "4. /")
            var operacija = Scanner(System.`in`)
            var opera = operacija.nextLine()
            println("Posaljite listu kolona nad kojima zelite da radite operaciju")
            var scanner = Scanner(System.`in`)

            var input = scanner.nextLine()

            val numbers = input.split(" ").map { it.toInt() }
            var lista : MutableList<Int> = mutableListOf()
            for (number in numbers) {
                lista.add(number)
            }
            izvestaj.addColumn(opera, lista)
        }
        println("Unesite path fajla: ")
        var pathh = Scanner(System.`in`)
        var p = pathh.nextLine()
        println("Izaberite tip izvestaja: \n" + exportedServices.keys)
        var tipIzvestaja = Scanner(System.`in`)
        var tipIz = tipIzvestaja.nextLine()
        if(tipIz.equals("PDF"))
            exportedServices.get(Tip.PDF)!!.genR(izvestaj, p)
        else if(tipIz.equals("PLAIN_TEXT"))
            exportedServices.get(Tip.PLAIN_TEXT)!!.genR(izvestaj, p)
        else if(tipIz.equals("CSV"))
            exportedServices.get(Tip.CSV)!!.genR(izvestaj, p)
        else if(tipIz.equals("EXCEL"))
            exportedServices.get(Tip.EXCEL)!!.genR(izvestaj, p)
        }


    }
