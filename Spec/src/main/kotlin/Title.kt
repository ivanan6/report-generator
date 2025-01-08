import java.awt.Color

class Title(private var naslov:String) {
    private var boldovano : Boolean = false
    private var italic : Boolean = false
    private var underline : Boolean = false
    private var boja:String? = null
    fun formatNaslov(bold: Boolean = false, italic: Boolean = false, underline: Boolean = false, boja:Color? = null) {
        var formattedTitle = naslov

//        // Dodavanje HTML tagova
//        if (bold) formattedTitle = "<b>$formattedTitle</b>"
//        if (italic) formattedTitle = "<i>$formattedTitle</i>"
//        if (underline) formattedTitle = "<u>$formattedTitle</u>"
//        if (color != null) formattedTitle = "<span style='color: $color;'>$formattedTitle</span>"
//
//        return formattedTitle
        this.boldovano = bold
        this.italic = italic
        this.underline = underline
        if (boja != null) {
            this.boja = String.format("#%02x%02x%02x", boja.red, boja.green, boja.blue)
        }
    }

    fun isBoldovano(): Boolean {
        return boldovano
    }

    // Getter za italic
    fun isItalic(): Boolean {
        return italic
    }

    // Getter za underline
    fun isUnderline(): Boolean {
        return underline
    }

    // Getter za boja
    fun getBoja(): String? {
        return boja
    }

    fun getNaslov(): String {
        return naslov
    }



}