/**
 * The class of reservation station, This is actually the reservation station
 * for Add, Sub, Multi and Divide
 * Operations related to load and save are in LoadAndSaveStation.kt
 */
class ReservationStation(addNum: Int, mulNum: Int) {

    var addStation: Array<actualStation?>
    var multiStation: Array<actualStation?>
    companion object {
        val ANum = 2
        val MNum = 3

        val AddType = 0
        val MultiType = 1
        val SubType = 2
        val DivideType = 3

        const val BUSY = -1

    }

    // Initialize the add station and multi station
    init {
        addStation = arrayOfNulls<actualStation>(addNum)
        multiStation = arrayOfNulls<actualStation>(mulNum)

        for (i in 0 until addNum) {
            addStation[i] = actualStation(AddType)
        }
        for (i in 0 until mulNum) {
            multiStation[i] = actualStation(MultiType)
        }
    }

    fun getAddAvailable(): Int {
        for (i in addStation.indices) {
            if (addStation[i]?.busy == false) {
                return i
            }
        }
        return BUSY
    }

    fun getMultiAvailable(): Int {
        for (i in multiStation.indices) {
            if (multiStation[i]?.busy == false) {
                return i
            }
        }
        return BUSY
    }

    fun execute() {
        executeAdd()
        executeMulti()
    }

    fun executeAdd() {
        addStation.forEachIndexed { index, actualStation ->
            actualStation?.let {
                if (it.qk == "" && it.qj == "" && it.busy && !it.ins?.justIssue!!) {
                    dealWithStart(it)
                    it.count--
                    if (it.count == 0) {
                        StationProvider.updateComplete(it.ins!!.id)
                        it.value = when (it.ins?.type) {
                            Instruction.ADD -> it.vj + it.vk
                            Instruction.SUB -> it.vj - it.vk
                            else -> it.vj + it.vk
                        }
                    }
                    else if (it.count == -1) {
                        finishAndWrite(it, index)
                    }
                } else {
                    it.ins?.justIssue = false
                }
            }
        }
    }

    fun executeMulti() {
        multiStation.forEachIndexed { index, actualStation ->
            actualStation?.let {
                if (it.qk == "" && it.qj == "" && it.busy) {
                    dealWithStart(it)
                    it.count--
                    if (it.count == 0) {
                        StationProvider.updateComplete(it.ins!!.id)
                        it.value = when (it.ins!!.type) {
                            Instruction.MULTD -> it.vj * it.vk
                            Instruction.DIVID -> it.vj / it.vk
                            else -> it.vj + it.vk
                        }
                    }
                    else if (it.count == -1) {
                        finishAndWrite(it, index)
                    }
                }
            }
        }
    }

    // record an instruction will be executed
    fun dealWithStart(actualStation: actualStation) {
        actualStation.let {
            if (!it.executing) {
                it.executing = true
                StationProvider.updateExecute(it.ins!!.id)
            }
        }
    }

    fun finishAndWrite(actualStation: actualStation, index: Int) {
        actualStation.let {
            it.busy = false
            it.executing = false
            StationProvider.updatefinish(it.ins!!.id)
            StationProvider.addUpdate(it.ins!!.type + " " + index, it.value)
            StationProvider.updateToRegister(it.ins!!,it.value, index)
        }
    }


    class actualStation(type: Int) {
            val type = type
            var busy = false
            var operation = ""
            var vj = 0.0
            var vk = 0.0
            var qj = ""
            var qk = ""
            var value = 0.0
            var executing = false
            var count = 0
            var ins:Instruction? = null
    }
}