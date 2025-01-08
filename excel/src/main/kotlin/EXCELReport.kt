package excel

import Extenzija
import Izvestaj
import Specifikacija
import Tip
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFFont
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

class EXCELReport:Specifikacija() {
    override var tip = Tip.EXCEL
    override var extenzija = Extenzija.xlsx


    override fun genR(izvestaj: Izvestaj, pathToFile: String) {
        val workBook = XSSFWorkbook()

        val sheet = workBook.createSheet("Izvestaj")
        val titleRow = sheet.createRow(0)

        if(izvestaj.getTitle() !=null){
            val titleCell = titleRow.createCell(0)
            val title = izvestaj.getTitle()
            if (title != null) {
                titleCell.setCellValue("${title.getNaslov()}")
            }
            val font: XSSFFont = workBook.createFont()

            if (title!!.isBoldovano()) {
                font.bold = true
            }
            if (title.isItalic()) {
                font.italic = true
            }
            if (title.isUnderline()) {
                font.underline = XSSFFont.U_SINGLE
            }
            if (title.getBoja() != null) {
                val colorHex = title.getBoja() //
                val colorRgb = java.awt.Color.decode(colorHex) // Konvertujemo hex u RGB
                val xssfColor = XSSFColor(colorRgb, null) // Kreiramo XSSFColor sa RGB vrednostima
                font.setColor(xssfColor)
            }
            val cellStyle: XSSFCellStyle = workBook.createCellStyle()
            cellStyle.setFont(font)
            titleCell.cellStyle = cellStyle
        }

        if(izvestaj.getHeader() !=null){
            val headerRow = sheet.createRow(1)
            val header = izvestaj.getHeader()!!
            for ((i, h) in header.getPodaci().withIndex()) {
                val headerCell = headerRow.createCell(i)
                headerCell.setCellValue(h)
                val font: XSSFFont = workBook.createFont()

                if (header.isBoldovano()) {
                    font.bold = true
                }

                if (header.isBojaUslov()) {

                    val colorHex = header.getBoja()
                    val colorRgb = java.awt.Color.decode(colorHex)
                    val xssfColor = XSSFColor(colorRgb, null) // Kreiramo XSSFColor sa RGB vrednostima
                    font.setColor(xssfColor)
                }

                // Kreiramo stil ćelije i dodeljujemo font
                val cellStyle: XSSFCellStyle = workBook.createCellStyle()
                cellStyle.setFont(font)

                headerCell.cellStyle = cellStyle
            }


        }



        for (rowInd in 0 until izvestaj.getPodaci()[0].getKolona().size) {
            val row = sheet.createRow(rowInd + 2)


            for ((colInd, col) in izvestaj.getPodaci().withIndex()) {
                val cell = row.createCell(colInd)
                val values = col.getKolona()
                val bold = col.isBoldovano()
                val colorCondition = col.isBojaUslov()



                val value = values[rowInd]
                when (value) {
                    is String -> cell.setCellValue(value)
                    is Double -> cell.setCellValue(value)
                    is Int -> cell.setCellValue(value.toDouble()) // int se mora konvertovati u double za setCellValue
                    is Boolean -> cell.setCellValue(value)
                    else -> cell.setCellValue(value.toString()) // Ako nije prepoznat tip, koristi toString
                }


                val font: XSSFFont = workBook.createFont()
                if (bold) {
                    font.bold = true
                }
                if (colorCondition) {
                    val colorHex = col.getBoja()
                    val colorRgb = java.awt.Color.decode(colorHex)
                    val xssfColor = XSSFColor(colorRgb, null)
                    font.setColor(xssfColor)
                }
                val cellStyle: XSSFCellStyle = workBook.createCellStyle()
                cellStyle.setFont(font)
            }
        }
        var lastRowNum = sheet.lastRowNum

        if(izvestaj.getRezime()!=null){
            val summaryRowTitle = sheet.createRow(lastRowNum + 1)
            val summaryCellTitle = summaryRowTitle.createCell(0)
            summaryCellTitle.setCellValue("Rezime")
            val titleStyle = workBook.createCellStyle()
            val titleFont = workBook.createFont()
            titleFont.bold = true
            titleStyle.setFont(titleFont)
            summaryCellTitle.cellStyle = titleStyle


            lastRowNum++
            for ((key, value) in izvestaj.getRezime()!!.getRezime()) {
                val row = sheet.createRow(lastRowNum++)

                var formatiranLabel = key.getKey()
                val keyStyle = workBook.createCellStyle()
                val keyFont = workBook.createFont()

                if (key.isBold()) keyFont.bold = true
                if (key.isItalic()) keyFont.italic = true
                if (key.isUnderline()) keyFont.underline = XSSFFont.U_SINGLE
                if (key.getColor() != null) {
                    try {
                        val colorRgb = java.awt.Color.decode(key.getColor())
                        val xssfColor = XSSFColor(colorRgb, null)
                        keyFont.color = xssfColor.index
                    } catch (e: Exception) {
                        keyFont.color = IndexedColors.BLACK.index
                    }
                }
                keyStyle.setFont(keyFont)

                var cell = row.createCell(0)
                cell.setCellValue(formatiranLabel)
                cell.cellStyle = keyStyle


                // Formatiraj vrednost
                var formatiranValue = value.getVrednost()
                val valueStyle = workBook.createCellStyle()
                val valueFont = workBook.createFont()

                // Stilovi za vrednost
                if (value.isBold()) valueFont.bold = true
                if (value.isItalic()) valueFont.italic = true
                if (value.isUnderline()) valueFont.underline = XSSFFont.U_SINGLE
                if (value.getColor() != null) {
                    try {
                        val colorRgb = java.awt.Color.decode(value.getColor())
                        val xssfColor = org.apache.poi.xssf.usermodel.XSSFColor(colorRgb, null)
                        valueFont.color = xssfColor.index
                    } catch (e: Exception) {
                        valueFont.color = IndexedColors.BLACK.index
                    }
                }
                valueStyle.setFont(valueFont)


                cell = row.createCell(1)
                when (formatiranValue) {
                    is String -> cell.setCellValue(formatiranValue)
                    is Double -> cell.setCellValue(formatiranValue)
                    is Int -> cell.setCellValue(formatiranValue.toDouble()) // int se mora konvertovati u double za setCellValue
                    is Boolean -> cell.setCellValue(formatiranValue)
                    else -> cell.setCellValue(formatiranValue.toString()) // Ako nije prepoznat tip, koristi toString
                }
                cell.cellStyle = valueStyle
            }
        }





        val path = pathToFile + "." + extenzija
        FileOutputStream(path).use{ outputStream ->
            workBook.write(outputStream)
        }
        workBook.close()

    }
}