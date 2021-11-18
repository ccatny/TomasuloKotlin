import StationProvider.executeStation
import StationProvider.insList
import StationProvider.updateStation

var clock = 1
/**
 * Created by Chengkai Zhang on 6, November 2021
 */

fun main(args: Array<String>) {

    // Main body of the algorithm, If the calculation has not been finished, another cycle is added
    while (! executeStation.checkComplete(executeStation)) {
        issueIns(executeStation)
        executeIns()
        updateStation()
        printStatus()
        clock++
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

fun printStatus() {
    //print the result
    println("current cycle : " + clock)
    println("issue   start complete   finish complete   write back   ")
    for (i in 0 until executeStation.issue.size) {
        printWithSpace(executeStation.issue[i], "       ")
        printWithSpace(executeStation.execute[i], "                ")
        printWithSpace(executeStation.comp[i], "                 ")
        printWithSpace(executeStation.result[i],"     ")
        println()
    }
}

