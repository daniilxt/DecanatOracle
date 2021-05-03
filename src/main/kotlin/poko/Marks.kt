package poko

data class Marks(
    var id: Long? = null,
    var stSecondName: String? = null,
    var stFirstName: String? = null,
    var stMiddleName: String? = null,
    var group: String? = null,
    var thSecondName: String? = null,
    var thFirstName: String? = null,
    var thMiddleName: String? = null,
    var subject: String? = null,
    var idStudent: Long? = 0,
    var idTeacher: Long? = 0,
    var value: Long? = 2
)