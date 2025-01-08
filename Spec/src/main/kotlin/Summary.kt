import java.awt.Color

class Summary(private var rezime : MutableMap<LabelaRezime,VrednostRezime> = mutableMapOf()) {
    fun dodaj(key: LabelaRezime, value: VrednostRezime) {
       rezime[key] = value
    }

    fun getRezime(): MutableMap<LabelaRezime, VrednostRezime> {
        return rezime
    }
}