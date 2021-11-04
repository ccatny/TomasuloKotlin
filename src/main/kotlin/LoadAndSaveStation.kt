class LoadAndSaveStation(saveNum:Int, loadNum: Int) {

    lateinit var busy: BooleanArray

    lateinit var result: Array<String?>

    var saveStation: Array<actualStation?>

    var loadStation: Array<actualStation?>
    companion object {
        const val sNum = 2
        const val lNum = 2
        const val saveType = 2
        const val  loadType = 3
        const val BUSY = -1
    }
    init {
        saveStation = arrayOfNulls<actualStation>(saveNum)
        loadStation = arrayOfNulls<actualStation>(loadNum)
        for (i in 0 until saveNum) {
            saveStation[i] = actualStation(saveType, Instruction.saveCycle)
        }
        for (i in 0 until loadNum) {
            loadStation[i] = actualStation(loadType, Instruction.loadCycle)
        }
    }

    fun getLoadAvailable(): Int {
        for (i in loadStation.indices) {
            if (loadStation[i]?.busy == false) {
                return i
            }
        }
        return BUSY
    }

    fun getSaveAvailable(): Int {
        for (i in saveStation.indices) {
            if (saveStation[i]?.busy == false) {
                return i
            }
        }
        return BUSY
    }


    fun execute() {
        executeSave()
        executeLoad()
    }

    fun executeLoad() {
        loadStation.forEachIndexed {index, actualStation ->
            actualStation?.let {
                if (it.busy && !it.ins?.justIssue!!) {
                    if (!it.executing) {
                        StationProvider.updateExecute(it.ins!!.id)
                        it.executing = true
                    }
                    it.count--
                    if (it.count == 0) {
                        StationProvider.updateComplete(it.ins!!.id)
                        it.value = it!!.ins!!.operator1.toDouble()
                    }
                    else if (it.count == -1) {
                        it.busy = false
                        it.address = ""
                        it.executing = false
                        StationProvider.updatefinish(it.ins!!.id)
                        StationProvider.addUpdate(Instruction.LOAD + " " + index, it.value)
                        StationProvider.updateToRegister(it.ins!!, it.value, index)
//                        var target = StationProvider.registerResult[it.ins.dest]
//                        target?.let {
//                            if (target.refer == Instruction.LOAD + " " + index) {
//                                StationProvider.registerResult[actualStation.ins.dest]?.refer = null
//                                StationProvider.registerResult[actualStation.ins.dest]?.number = actualStation.ins.operator1.toDouble()
//                            }
//                        }
                    }
                } else {
                    it.ins?.justIssue = false
                }
            }
        }
    }

    fun executeSave() {
        saveStation.forEachIndexed { index, actualStation ->
            actualStation?.let {
                if (it.busy && it.couldSave) {
                    if (!it.executing) {
                        StationProvider.updateExecute(it.ins!!.id)
                        it.executing = true
                    }
                    it.count--
                    if (it.count == 0) {
                        StationProvider.updateComplete(it.ins!!.id)
                    }
                    if (it.count == -1) {
                        StationProvider.updatefinish(it.ins!!.id)
                        it.couldSave = false
                        it.address = ""
                        it.busy = false
                    }
                }
            }
        }
    }

    class actualStation(val type: Int, var count: Int) {
            var busy = false
            var address = ""
            var value = 0.0
            var executing = false
            var couldSave = false
            var ins: Instruction? = null
    }
}