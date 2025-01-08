package pt
import Extenzija
import Izvestaj
import Specifikacija
import Tip
import jdk.internal.org.jline.utils.AttributedStringBuilder.append
import java.io.File
import java.io.IOException

class PlainText() : Specifikacija(){
    override var tip = Tip.PLAIN_TEXT
    override var extenzija = Extenzija.txt

    override fun genR(izvestaj: Izvestaj, pathToFile: String) {
        for (i in izvestaj.getPodaci()){
            if(i.isBoldovano() || i.isBojaUslov()){
                throw IllegalArgumentException("Plain text ne moze da bude formatiran")
            }
        }
        val maxDuzina = MutableList(izvestaj.getPodaci().get(0).getKolona().size) { 0 }

        for (col in 0 until maxDuzina.size) {
            for (row in izvestaj.getPodaci()) {
                if (col < row.getKolona().size && row.getKolona().get(col).toString().length > maxDuzina[col]) {
                    maxDuzina[col] = row.getKolona().get(col).toString().length
                }
            }
        }
        val result = buildString {
            if(izvestaj.getTitle()?.getNaslov() != null) append(izvestaj.getTitle()?.getNaslov())
            append("\n")
            for (col in 0 until maxDuzina.size){
            for (row in izvestaj.getPodaci()) {

                    if (row.getKolona().get(col).toString().length < maxDuzina[col]) {
                        append(row.getKolona().get(col).toString())
                        append(" ".repeat(maxDuzina[col] - row.getKolona().get(col).toString().length))
                    } else {
                        append(row.getKolona().get(col).toString())
                    }
                    append("\t\t")
                }
                append("\n")
            }
            izvestaj.getRezime()?.getRezime()?.let { rezime ->
                for((key, value) in rezime){
                    append(key, "\t",value)
                    append("\n")
                }
            }

        }
        try{
            val path = pathToFile + "." + extenzija
            File(path).writeText(result)
        }catch (e : IOException){
            println("Greska")
        }

    }

}