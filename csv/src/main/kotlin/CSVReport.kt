package csv

import Extenzija
import Izvestaj
import Specifikacija
import Tip
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter


import java.io.File
import java.io.FileWriter

class CSVReport() : Specifikacija() {
    override var tip = Tip.CSV
    override var extenzija = Extenzija.csv

    override fun genR(izvestaj: Izvestaj, pathToFile: String) {
        if(izvestaj.getTitle()!=null)
            throw IllegalArgumentException("CSV ne moze da ima naslov")
        if(izvestaj.getRezime()!=null)
            throw IllegalArgumentException("CSV ne moze da ima rezime")
        for (i in izvestaj.getPodaci()){
            if(i.isBoldovano() || i.isBojaUslov()){
                throw IllegalArgumentException("CSV ne moze da bude formatiran")
            }
        }
        try {
            val path = pathToFile + "." + extenzija
            val writer = FileWriter(File(path))
            val csvPrinter = CSVPrinter(writer, CSVFormat.DEFAULT)
            if(izvestaj.getHeader()!=null){
                csvPrinter.printRecord(izvestaj.getHeader()!!.getPodaci())
            }
            for( i in 0 until izvestaj.getPodaci().first().getKolona().size){
                val row = mutableListOf<Any>()
                for(col in izvestaj.getPodaci()){
                    row.add(col.getKolona().get(i))
                }


                csvPrinter.printRecord(row)
            }
            writer.close()
            csvPrinter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}