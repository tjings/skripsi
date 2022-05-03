package umn.ac.id.skripsijosh.pojo

class ChallengeDetails (
    var challengeId: Int = 0,
    var userCompleted: MutableList<String> = arrayListOf(),
    var desc: String = "",
    var name: String = "",
    var streakNeeded: Int = 0,
    var waterNeeded: Int = 0,
    var medalGot: Int = 0,
    var pointGot: Int = 0
)
