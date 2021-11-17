import StationProvider.executeStation
import StationProvider.insList
import StationProvider.updateStation

var clock = 1
/**
 * Created by Chengkai Zhang on 6, November 2021
 */

fun main(args: Array<String>) {

    // Main body of the cycle, If the calculation is not finished, another cycle is added
    while (! executeStation.checkComplete(executeStation)) {
        issueIns(executeStation)
        executeIns()
        updateStation()
        clock++
    }

    //print the result
    println("issue   start complete   finish complete   write back   ")
    for (i in 0 until executeStation.issue.size) {
        print(executeStation.issue[i].toString() + "       ")
        print(executeStation.execute[i].toString() + "                ")
        print(executeStation.comp[i].toString() + "                 ")
        print(executeStation.result[i].toString() + "     ")
        println()
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

