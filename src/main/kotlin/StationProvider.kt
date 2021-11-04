object StationProvider {
    const val registerSize = 30

    lateinit var executeStation: ExecuteStation

    lateinit var reservationStation: ReservationStation

    lateinit var loadAndSaveStation: LoadAndSaveStation

    lateinit var waitToExeList: List<Instruction>

    var registerResult: HashMap<String, Register>

    var insList: ArrayList<Instruction>

    var updateMap = HashMap<String, Double>()

    init {
        val insSet = readInstructions(filePath)

        insList = ArrayList()
        insSet.forEachIndexed { index, s ->
            insList.add(Instruction(s, index))
        }
        executeStation = ExecuteStation(insList.size)
        reservationStation = ReservationStation(ReservationStation.ANum, ReservationStation.MNum)
        loadAndSaveStation = LoadAndSaveStation(LoadAndSaveStation.sNum, LoadAndSaveStation.lNum)
        registerResult = HashMap()
        waitToExeList = ArrayList<Instruction>()


        registerResult["F0"] = Register(2.0)
        registerResult.put("F2", Register(3.0))
        registerResult.put("F4", Register(5.0))
        registerResult.put("F6", Register(6.0))
        registerResult.put("F8", Register(9.0))
        registerResult.put("F10", Register(11.0))
        registerResult.put("F12", Register(13.0))
        registerResult.put("F14", Register(15.0))
        registerResult.put("F16", Register(17.0))
        registerResult.put("F18", Register(19.0))
    }

    fun addUpdate(refer: String, num: Double) {
        updateMap.put(refer, num)
    }

    fun updateStation() {
        updateMap.keys.forEach {
            update(it, updateMap.get(it)!!)
        }
        updateMap.clear()
    }

    private fun update(refer:String, num:Double) {
        reservationStation.addStation.forEach {
            it?.let {
                if (it.qj == refer) {
                    it.vj = num
                    it.qj = ""
                }
                if (it.qk == refer) {
                    it.vk = num
                    it.qk = ""
                }
            }
        }
        reservationStation.multiStation.forEach {
            it?.let {
                if (it.qj == refer) {
                    it.vj = num
                    it.qj = ""
                }
                if (it.qk == refer) {
                    it.vk = num
                    it.qk = ""
                }
            }
        }
        loadAndSaveStation.saveStation.forEach {
            it?.let {
                if (it.ins?.dest == refer) {
                    it.value = num
                    it.couldSave = true
                }
            }
        }
    }

    fun updateExecute(id: Int) {
        executeStation.execute[id] = clock
    }

    fun updateIssue(id: Int) {
        executeStation.issue[id] = clock
    }

    fun updateComplete(id: Int) {
        executeStation.comp[id] = clock
    }

    fun updatefinish(id: Int) {
        executeStation.result[id] = clock
    }

    fun updateToRegister(ins: Instruction, value:Double, index: Int) {
        var target = registerResult[ins.dest]
        target?.let {
            if (target.refer == ins.type + " " + index) {
                registerResult[ins.dest]?.refer = null
                registerResult[ins.dest]?.number = value
            }
        }
    }
}