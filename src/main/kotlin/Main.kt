import StationProvider.executeStation
import StationProvider.insList
import StationProvider.updateStation

var clock = 1

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments at Run/Debug configuration
//    println("Program arguments: ${args.joinToString()}")


    while (! executeStation.checkComplete(executeStation)) {
        issueIns(executeStation)
        executeIns()
        updateStation()
        clock++
    }

    println("Hello World!")

    executeStation.issue.forEach {
        println(it)
    }
    println()
    executeStation.result.forEach {
        println(it)
    }

}

fun issueIns(executeStation:ExecuteStation) {
    if (allIssued(executeStation)) {
        return
    }
    var ins = insList[getNextIssue(executeStation)]
    var success = false
    when (ins.type) {
        Instruction.LOAD -> success = Instruction.issueLoad(ins)
        Instruction.SAVE -> success = Instruction.issueSave(ins)
        Instruction.ADD, Instruction.SUB -> success = Instruction.issueAdd(ins)
        Instruction.MULTD, Instruction.DIVID -> success = Instruction.issueMulti(ins)
    }
    if (success) {
        executeStation.issue[ins.id] = clock
    }
}

fun executeIns() {
    StationProvider.loadAndSaveStation.execute()
    StationProvider.reservationStation.execute()
}

