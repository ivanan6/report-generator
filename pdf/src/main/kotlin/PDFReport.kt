package pdf

import Extenzija
import Izvestaj
import Specifikacija
import Tip
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.file.Paths


class PDFReport: Specifikacija() {
    override var tip= Tip.PDF
    override var extenzija = Extenzija.pdf


    override fun genR(izvestaj: Izvestaj, pathToFile: String) {
            val stringBuilder = StringBuilder()
            if(izvestaj.getTitle() !=null){
                var title = izvestaj.getTitle()
                var formatiranTitle = izvestaj.getTitle()!!.getNaslov()
                if (title!!.isBoldovano()) formatiranTitle = "<b>$formatiranTitle</b>"
                if (title.isItalic()) formatiranTitle = "<i>$formatiranTitle</i>"
                if (title.isUnderline()) formatiranTitle = "<u>$formatiranTitle</u>"
                if (title.getBoja() != null) formatiranTitle = "<span style='color: ${title.getBoja()};'>$formatiranTitle</span>"
                stringBuilder.append("<html><body><h1>$formatiranTitle</h1><table style='border-collapse: collapse; border: 2px solid black;'>\n") // Start the table
            }else if(izvestaj.getTitle() ==null){
                stringBuilder.append("<html><body><table style='border-collapse: collapse; border: 2px solid black;'>\n") // Start the table

            }
            if(izvestaj.getHeader() !=null){
                stringBuilder.append("  <tr>\n")

                for (h in izvestaj.getHeader()!!.getPodaci()) {
                    // Inicijalizuj formatiranu verziju zaglavlja
                    var formattedHeader = h

                    // Proveri da li je bold
                    if (izvestaj.getHeader()!!.isBoldovano()) {
                        formattedHeader = "<b>$formattedHeader</b>"
                    }

                    // Proveri boju
                    if (izvestaj.getHeader()!!.isBojaUslov()) {
                        val color = izvestaj.getHeader()!!.getBoja()
                        formattedHeader = "<span style='color: $color;'>$formattedHeader</span>"
                    }

                    // Dodaj zaglavlje sa formatiranjem
                    stringBuilder.append("    <th style=' border: 1px solid black;'>$formattedHeader</th>\n")
                }

                stringBuilder.append("  </tr>\n")
            }



            for (rowInd in 0 until izvestaj.getPodaci().get(1).getKolona().size) {
                stringBuilder.append("  <tr>\n")
                for (col in izvestaj.getPodaci()) {
                    val values = col.getKolona()
                    val bold = col.isBoldovano()
                    val colorCondition = col.isBojaUslov()
                    val color = if (colorCondition) col.getBoja() else ""

                    var formattedValue = values[rowInd]
                    if (bold) {
                        formattedValue = "<b>$formattedValue</b>"
                    }
                    if (color.isNotEmpty()) {
                        formattedValue = "<span style='color: $color;'>$formattedValue</span>"
                    }

                    stringBuilder.append("    <td style=' border: 1px solid black;'>$formattedValue</td>\n")

                }
                stringBuilder.append("  </tr>\n")
            }
            stringBuilder.append("</table>\n")


            if(izvestaj.getRezime()!=null){
                stringBuilder.append("<h2>Rezime</h2>\n")
                stringBuilder.append("<table>\n")
                izvestaj.getRezime()!!.getRezime().forEach { (key, value) ->
                    stringBuilder.append("  <tr>\n")
                    var formatiranTitle = key.getKey()
                    if (key.isBold()) formatiranTitle = "<b>$formatiranTitle</b>"
                    if (key.isItalic()) formatiranTitle = "<i>$formatiranTitle</i>"
                    if (key.isUnderline()) formatiranTitle = "<u>$formatiranTitle</u>"
                    if (key.getColor() != null) formatiranTitle = "<span style='color: ${key.getColor()};'>$formatiranTitle</span>"


                    stringBuilder.append("    <td>$formatiranTitle</td>\n")
                    stringBuilder.append("    <td> : </td>\n")

                    var formatiranTitle2 = value.getVrednost()
                    if (value.isBold()) formatiranTitle2 = "<b>$formatiranTitle2</b>"
                    if (value.isItalic()) formatiranTitle2 = "<i>$formatiranTitle2</i>"
                    if (value.isUnderline()) formatiranTitle2 = "<u>$formatiranTitle2</u>"
                    if (value.getColor() != null) formatiranTitle2 = "<span style='color: ${value.getColor()};'>$formatiranTitle2</span>"

                    stringBuilder.append("    <td>$formatiranTitle2</td>\n")
                    stringBuilder.append("  </tr>\n")
                }
                stringBuilder.append("</table>\n")
            }







            stringBuilder.append("</body></html>")
            val html = stringBuilder.toString()
            try {
                val path = pathToFile + "." + extenzija
                val outputStream = FileOutputStream(path)
                val renderer = ITextRenderer()


                renderer.setDocumentFromString(html)
                renderer.layout()


                renderer.createPDF(outputStream)


                outputStream.close()
            }catch (e: Exception) {
                e.printStackTrace()
            }
    }

}