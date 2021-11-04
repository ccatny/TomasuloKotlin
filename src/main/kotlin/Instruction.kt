import StationProvider.loadAndSaveStation
import StationProvider.registerResult
import StationProvider.reservationStation

class Instruction(ins: String, var id: Int) {
    lateinit var type:String
    lateinit var dest:String
    lateinit var operator1:String
    lateinit var operator2:String
    var justIssue = true

    companion object {
        val ADD = "ADDD"
        val SAVE = "SD"
        val LOAD = "LD"
        val SUB = "SUBD"
        val MULTD = "MULD"
        val DIVID = "DIVID"

        val addCycle = 2
        val multiCycle = 3//10
        val loadCycle = 3
        val DividCycle = 40
        val subCycle = 2 //3
        val saveCycle = 3


        fun issueLoad(ins: Instruction): Boolean {
            var availableId = loadAndSaveStation.getLoadAvailable()
            if (availableId != LoadAndSaveStation.BUSY) {
                loadAndSaveStation.loadStation[availableId]?.busy = true
                loadAndSaveStation.loadStation[availableId]?.address = ins.operator1
                loadAndSaveStation.loadStation[availableId]?.ins = ins
                registerResult[ins.dest]?.refer = LOAD + " " + availableId
                return true
            } else {
                return false
            }
        }

        fun issueSave(ins: Instruction): Boolean {
            var availableId = loadAndSaveStation.getSaveAvailable()
            if (availableId != LoadAndSaveStation.BUSY) {
                loadAndSaveStation.saveStation[availableId]?.busy = true
                loadAndSaveStation.saveStation[availableId]?.address = ins.operator1
                loadAndSaveStation.saveStation[availableId]?.ins = ins
                registerResult[ins.dest]?.refer = SAVE + " " + availableId
                return true
            } else {
                return false
            }
        }

        fun issueAdd(ins: Instruction): Boolean {
            var availableId = reservationStation.getAddAvailable()
            if (availableId != ReservationStation.BUSY) {
                var station = reservationStation.addStation[availableId]
                processReservation(ins, station, availableId)
                return true
            } else {
                return false
            }
        }

        fun issueMulti(ins: Instruction): Boolean {
            var availableId = reservationStation.getMultiAvailable()
            if (availableId != ReservationStation.BUSY) {
                var station = reservationStation.multiStation[availableId]
                processReservation(ins, station, availableId)
                return true
            } else {
                return false
            }
        }

        fun processReservation(ins: Instruction, station:ReservationStation.actualStation?, availableId:Int) {
            station?.busy = true
            station?.operation = ins.type
            station?.ins = ins
            if (whetherOperatorAvailable(ins.operator1)) {
                station?.vj = registerResult.get(ins.operator1)?.number!!
            } else {
                station?.qj = registerResult.get(ins.operator1)?.refer!!
            }
            if (whetherOperatorAvailable(ins.operator2)) {
                station?.vk = registerResult.get(ins.operator2)?.number!!
            } else {
                station?.qk = registerResult.get(ins.operator2)?.refer!!
            }
            station?.count = when (ins.type) {
                ADD -> addCycle
                DIVID -> DividCycle
                SUB -> subCycle
                MULTD -> multiCycle
                else -> 2
            }
            registerResult.get(ins.dest)!!.refer = ins.type + " " + availableId
        }

        fun whetherOperatorAvailable(ope: String): Boolean {
            return StationProvider.registerResult.get(ope)?.refer == null
        }
    }
    init {
        val split = ins.split(" ")
        type = split[0]
        if (type == LOAD || type == SAVE) {
            dest = split[1].split(",")[0]
            operator1 = split[1].split(",")[1]
        } else {
            dest = split[1].split(",")[0]
            operator1 = split[1].split(",")[1]
            operator2 = split[1].split(",")[2]
        }
    }

}