class Izvestaj(private var podaci: List<List<Any>> = mutableListOf(),
               private var title1:String? = null,

               private var header1 : List<String>? = null) {


    private var kolone:MutableList<Column> = mutableListOf()
    private var title:Title? = null
    private var rezime : Summary? = null
    private var header : Header? = null

    init {
        for (i in podaci){
            kolone.add(Column(i))
        }

        title = title1?.let { Title(it) }
        header = header1?.let { Header(it) }




        if (!allColumnsSameSize()) {
            throw IllegalArgumentException("Kolone nemaju isti broj elemenata.")
        }
        if(header!=null){
            if(header!!.getPodaci().size != podaci.size){
                throw IllegalArgumentException("Header nema isti broj elemenata kao tabela.")
            }

        }

    }


    private fun allColumnsSameSize(): Boolean {
        if (kolone.isEmpty()) return true
        val firstColumnSize = kolone.first().getKolona().size
        return kolone.all { it.getKolona().size == firstColumnSize }
    }


    fun getPodaci(): List<Column> {
        return kolone
    }

    // Getter za title
    fun getTitle(): Title? {
        return title
    }

    // Getter za rezime
    fun getRezime(): Summary? {
        return rezime
    }

    // Getter za header
    fun getHeader(): Header? {
        return header
    }
    /**
     * Izračunavanje rezimea na osnovu prosleđene mape operacija i kolona.
     *
     * @param map Mapa gde je ključ naziv operacije koja se izvršava, a vrednost može biti:
     *            - String: vrednost koja ce se upisati u labelu
     *            - List<Int>: broj kolona u listi nad kojima se vrši operacija
     *
     * Podržane operacije su:
     * - "sum": suma vrednosti u kolonama
     * - "prosek": prosečna vrednost u kolonama
     * - "count": broj vrednosti u kolonama
     * - "count if" : broj vrednosti u kolonama koje poseduju zadatu vrednost
     *
     * Primeri upotrebe:
     * ```
     * // Za jednu kolonu
     * izracunajSummary(mapOf("sum" to 50))
     *
     * // Za više kolona
     * izracunajSummary(mapOf("avg" to listOf(0, 1)))
     *
     *
     * @throws IllegalArgumentException ako je prosleđena nepoznata operacija
     */
    fun izracunajSummary(map: Map<String,Any>){//promenio iz MutableMap u obican Map zbog specifikacije
        var mapa :MutableMap<LabelaRezime,VrednostRezime> = mutableMapOf()
        for ((key,value) in map){
            if(value is List<*>){
                if(value.all {it is Int} && (key == "sum" || key == "prosek")){
                    var suma  = 0
                    if(key == "sum"){
                        for(svakaVrednost in value){
                            val kolona = this.getPodaci().get(svakaVrednost as Int).getKolona()
                            for( v in kolona){
                                suma+=v as Int
                            }
                        }
                    }
                    else{
                        var l = 0
                        for(svakaVrednost in value){
                            val kolona = this.getPodaci().get(svakaVrednost as Int).getKolona()
                            for( v in kolona){
                                l++
                                suma+=v as Int
                            }
                        }
                        suma /= l
                    }
                    var label = LabelaRezime(key)
                    var vrednost = VrednostRezime(suma)
                    mapa.set(label, vrednost)
                }
                else if(key == "count"){
                    var suma = 0
                    for(svakaVrednost in value){
                        val kolona = this.getPodaci().get(svakaVrednost as Int).getKolona()
                        for( v in kolona){
                            suma++
                        }
                    }
                    var label = LabelaRezime(key)
                    var vrednost = VrednostRezime(suma)
                    mapa.set(label, vrednost)
                }
                else if("count if" in key){
                    val parts = key.split(" ")
                    val zadataVrednost = parts[2]
                    var suma = 0
                    for(svakaVrednost in value){
                        val kolona = this.getPodaci().get(svakaVrednost as Int).getKolona()
                        for( v in kolona){
                            if(v.equals(zadataVrednost))
                                suma++
                        }
                    }
                    var label = LabelaRezime(key)
                    var vrednost = VrednostRezime(suma)
                    mapa.set(label, vrednost)
                }
                else{
                    throw IllegalArgumentException("Uneli ste pogresnu kalkulaciju")
                }
            }
            else{
                var label = LabelaRezime(key)
                var vrednost = VrednostRezime(value)
                mapa.set(label,vrednost)
            }
        }
        rezime = Summary(mapa)

    }
    fun addColumn(operacija:String, kolona:List<Any>){
        if((operacija == "+" || operacija == "*") && kolona.size >= 2){
            if(operacija == "+"){

                var novaLista : MutableList<Int> = MutableList(this.getPodaci().get(kolona.get(0) as Int).getKolona().size){0}

                for(i in 0 until kolona.size){
                    for(j in 0..this.getPodaci().get(kolona.get(i) as Int).getKolona().size-1){
                        var vrednost : Int = novaLista.get(j)

                        novaLista.set(j, vrednost + this.getPodaci().get(kolona.get(i) as Int).getKolona().get(j) as Int)
                    }
                }
                this.kolone.add(Column(novaLista))

            }
            else if(operacija == "*"){
                var novaLista : MutableList<Int> = MutableList(this.getPodaci().get(kolona.get(0) as Int).getKolona().size){1}

                for(i in 0..kolona.size - 1){
                    for(j in 0..this.getPodaci().get(kolona.get(i) as Int).getKolona().size -1){
                        var vrednost : Int = novaLista.get(j)

                        novaLista.set(j, vrednost * this.getPodaci().get(kolona.get(i) as Int).getKolona().get(j) as Int)
                    }
                }
                this.kolone.add(Column(novaLista))
            }

        }
        else if((operacija == "-" || operacija == "/") && kolona.size == 2){
            if(operacija == "-"){
                val minusList = mutableListOf<Int>()
                for (i in this.getPodaci().get(kolona.get(0) as Int).getKolona().indices) {
                    val minus = this.getPodaci().get(kolona.get(0) as Int).getKolona().get(i) as Int - this.getPodaci().get(kolona.get(1) as Int).getKolona().get(i) as Int
                    minusList.add(minus)
                }
                this.kolone.add(Column(minusList))
            }
            else{
                val divideList = mutableListOf<Int>()
                for (i in this.getPodaci().get(kolona.get(0) as Int).getKolona().indices) {
                    val divide = this.getPodaci().get(kolona.get(0) as Int).getKolona().get(i) as Int / this.getPodaci().get(kolona.get(1) as Int).getKolona().get(i) as Int
                    divideList.add(divide)
                }
                this.kolone.add(Column(divideList))
            }

        }
        else    throw IllegalArgumentException("Uneli ste pogresnu operaciju ili nedovoljan broj elemenata u listu")
    }





}