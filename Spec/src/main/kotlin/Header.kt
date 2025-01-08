import java.awt.Color

class Header(private var podaci:List<String>) {
    private var boldovano : Boolean = false
    private var boja_uslov : Boolean = false
    private lateinit var boja:String
    fun bold(bold:Boolean){
        this.boldovano  = true
    }
    fun bc_color(uslov:Boolean, boja: Color){
        this.boja = String.format("#%02x%02x%02x", boja.red, boja.green, boja.blue)
        this.boja_uslov  = uslov

    }
    fun isBoldovano(): Boolean {
        return boldovano
    }


    fun isBojaUslov(): Boolean {
        return boja_uslov
    }


    fun getBoja(): String {
        if (::boja.isInitialized) {
            return boja
        }
        throw UninitializedPropertyAccessException("boja nije inicijalizovana")
    }
    fun getPodaci(): List<String> {
        return podaci
    }

}