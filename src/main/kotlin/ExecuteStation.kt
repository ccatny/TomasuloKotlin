fun ExecuteStation.checkComplete(station: ExecuteStation): Boolean {
    return !station.result.contains(-1)
}

fun allIssued(station: ExecuteStation):Boolean {
    return !station.issue.contains(-1)
}

fun getNextIssue(station: ExecuteStation): Int {
    for (i in station.issue.indices) {
        if (station.issue[i] == -1) {
            return i
        }
    }
    return -1
}

class ExecuteStation(len: Int) {

    var result: IntArray
    var issue: IntArray
    var comp: IntArray
    var execute: IntArray

    init {
        issue = IntArray(len) { -1 }
        comp = IntArray(len) { -1 }
        execute = IntArray(len) { -1 }
        result = IntArray(len) { -1 }
    }

}
