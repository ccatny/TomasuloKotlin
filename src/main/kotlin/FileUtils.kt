import java.io.File

const val filePath = "instructions.txt"

fun readInstructions(filePath: String): List<String> {
    val file = File(filePath)
    val lines: List<String> = file.readLines()
    return lines
}

fun printWithSpace(num: Int, space: String) {
    if (num == -1) {
        print(" " + space)
    } else {
        print(num.toString() + space)
    }
}



