class Archive(private val name: String) {
    val notes: MutableList<Note> = mutableListOf()

    override fun toString(): String {
        return name
    }

}