import Screen.AllArchives.printEntries
import java.util.Scanner
import kotlin.system.exitProcess

object Navigator {

    val input = Scanner(System.`in`)

    const val NUMBER_OF_SYSTEM_ENTRIES = 2

    private val archives: MutableList<Archive> = mutableListOf()

    fun demonstrateScreen(screen: Screen) {
        when (screen) {
            is Screen.AllArchives -> {
                screen.printNavigation()
                printEntries(archives)
                when(val i = getUserInput()) {
                    0 -> exitProcess(0)
                    1 -> addNewArchive()
                    else -> openArchive(i)
                }
            }
            is Screen.ChosenArchive -> {
                screen.printNavigation()
                printEntries(screen.archive.notes)
                when(val i = getUserInput()) {
                    0 -> demonstrateScreen(Screen.AllArchives)
                    1 -> addNewNote(screen.archive)
                    else -> openNote(i, screen.archive)
                }
            }
            is Screen.ChosenNote -> {
                if(screen.note.content == "") {
                    addNoteContent(screen.note)
                }
                else {
                    println(screen.note.content)
                    println("Введите '1', если хотите изменить текст заметки, введите любое другое число, чтобы вернуться в главное меню")
                    when(getUserInput()) {
                        1 -> {
                            addNoteContent(screen.note)
                        }
                        else -> demonstrateScreen(Screen.AllArchives)
                    }
                }
            }
        }
    }

    private fun addNewArchive() {
        println("Введите название архива")
        var name = input.nextLine()
        while (name == "") {
            println("Введите непустую строку")
            name = input.nextLine()
        }
        archives.add(Archive(name))
        demonstrateScreen(Screen.AllArchives)
    }

    private fun openArchive(userInput: Int) {
        var index = userInput - NUMBER_OF_SYSTEM_ENTRIES
        while (index >= archives.size) {
            println("Некорректный ввод. Введите число, соответствущее одному из пунктов меню.")
            demonstrateScreen(Screen.AllArchives)
            index = getUserInput()
        }
        demonstrateScreen(Screen.ChosenArchive(archives[index]))
    }

    private fun addNewNote(archive: Archive) {
        println("Введите название заметки")
        var name = input.nextLine()
        while (name == "") {
            println("Введите непустое название")
            name = input.nextLine()
        }
        archive.notes.add(Note(name))
        demonstrateScreen(Screen.ChosenArchive(archive))
    }

    private fun openNote(userInput: Int, archive: Archive) {
        var index = userInput - NUMBER_OF_SYSTEM_ENTRIES
        while (index >= archive.notes.size) {
            println("Некорректный ввод. Введите число, соответствущее одному из пунктов меню.")
            demonstrateScreen(Screen.ChosenArchive(archive))
            index = getUserInput()
        }
        demonstrateScreen(Screen.ChosenNote(archive.notes[index]))
    }

    private fun getUserInput(): Int {
        var i = input.nextLine()
        while(true) {
            try {
                i.toInt()
                break
            } catch (e: NumberFormatException) {
                println("Некорректный ввод, ожидается число")
                i = input.next()
                continue
            }
        }

        return i.toInt()
    }

    private fun addNoteContent(note: Note) {
        println("Введите текст заметки")
        note.content = input.nextLine()
        demonstrateScreen(Screen.AllArchives)
    }
}