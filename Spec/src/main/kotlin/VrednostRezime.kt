import java.awt.Color

data class VrednostRezime(private var vrednost: Any) {
    private var bold: Boolean = false
    private var italic: Boolean = false
    private var underline: Boolean = false
    private var color: String? = null

    fun format(bold: Boolean = false, italic: Boolean = false, underline: Boolean = false, boja: Color?) {
        this.bold = bold
        this.italic = italic
        this.underline = underline
        if (boja != null) {
            this.color = String.format("#%02x%02x%02x", boja.red, boja.green, boja.blue)
        }
    }
//    override fun toString(): String {
//        return vrednost.toString()
//    }

    fun isBold(): Boolean {
        return bold
    }


    fun isItalic(): Boolean {
        return italic
    }


    fun isUnderline(): Boolean {
        return underline
    }


    fun getColor(): String? {
        return color
    }
    fun getVrednost(): Any {
        return vrednost
    }


}