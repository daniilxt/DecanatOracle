package JDBC

import poko.*
import java.sql.*
import java.util.*


object Utils {
    @Throws(SQLException::class)
    fun getNewConnection(): Connection? {
        var connection: Connection? = null
        try {
            //val dbURL = "jdbc:mariadb://localhost:3306/bdIlasha"
            val dbURL = "jdbc:oracle:thin:@localhost:1521:XE"
            val user = "c##daniilxt"
            val password = "MyPass"
            connection = DriverManager.getConnection(dbURL, user, password)
            if (connection.isValid(1)) {
                println("Connection successful")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return connection
    }

    fun getTeacherList(connection: Connection, sql: String = "select * from teachers_with_marks"): List<Teacher>? {
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Teacher(
                        resultSet.getString("LAST_NAME"), resultSet.getString("FIRST_NAME"),
                        resultSet.getString("PATHER_NAME"), resultSet.getString("NAME"), resultSet.getLong("ID")
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }


    fun getTeacherSubjectAndGroups(connection: Connection, teacherId: Long): List<TeacherSubjects>? {
        val sql = "\n" +
                "select distinct (SELECT p.last_name FROM people p  WHERE P.ID = $teacherId ) AS LAST_NAME,  G.name as GR_NAME  , S.name as SB_NAME," +
                "substr(g.name, -4, 4) as year\n" +
                "from marks\n" +
                "         join subjects s on marks.subject_id = s.id\n" +
                "         join people p on marks.student_id = p.id\n" +
                "         join groups g on g.id = p.group_id\n" +
                "where teacher_id  = $teacherId" +
                "order by year"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    TeacherSubjects(
                        resultSet.getString("LAST_NAME"),
                        groupName = resultSet.getString("GR_NAME"),
                        subject = resultSet.getString("SB_NAME")
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }


    fun findTeacher(
        connection: Connection,
        secondName: String = "",
        firstName: String = "",
        middleName: String = "",
        subject: String = ""
    ): List<Teacher>? {
        val sql2 =
            "select p.last_name as LAST_NAME, p.first_name as FIRST_NAME, p.pather_name as PATHER_NAME," +
                    " p.first_name as  NAME,p.id as ID\n" +
                    "from people p\n" +
                    "inner join subjects sb on p"
        "where lower(p.last_name) like '%${secondName.toLowerCase()}%'\n" +
                "  and lower(p.first_name) like '%${firstName.toLowerCase()}%'\n" +
                "  and lower(p.pather_name) like '%${middleName.toLowerCase()}%'" +
                "  and lower(p.pather_name) like '%${subject.toLowerCase()}%'"

        val sql = "select p.id, p.last_name, p.first_name, p.pather_name, sb.name\n" +
                "from marks mk\n" +
                "         right join people p on p.id = mk.teacher_id\n" +
                "         left join subjects sb on mk.subject_id = sb.id\n" +
                "where p.type = 'T'\n" +
                "and lower(p.last_name) like '%${secondName.toLowerCase()}%'\n" +
                "  and lower(p.first_name) like '%${firstName.toLowerCase()}%'\n" +
                "  and lower(p.pather_name) like '%${middleName.toLowerCase()}%'" +
                "  and lower(sb.name) like '%${subject.toLowerCase()}%'\n" +
                "group by p.last_name, p.first_name, p.pather_name, sb.name, p.id"
        println(sql)
        return getTeacherList(connection, sql)
    }


    fun createTeacher(connection: Connection, teacher: Teacher): Boolean {
        val sql = "insert into people (FIRST_NAME, LAST_NAME, PATHER_NAME, GROUP_ID, TYPE)\n" +
                "values ('${teacher.firstName}','${teacher.secondName}','${teacher.middleName}',null,'T')\n"
        return doSomethingWithResult(connection, sql)
    }

    private fun doSomethingWithResult(connection: Connection, sql: String): Boolean {
        return try {
            connection.createStatement().executeQuery(sql)
            true
        } catch (ex: SQLException) {
            println(ex)
            false
        }
    }

    fun deleteTeacher(connection: Connection, idTeacher: Long, action: (res: Boolean) -> Unit) {
        val sql = "DELETE FROM PEOPLE  P WHERE P.ID = $idTeacher AND P.TYPE = 'T' "
        action(doSomethingWithResult(connection, sql))
    }

    fun getFullMarksList(
        connection: Connection,
        sql: String = "select marks.id as ID_MARK, p.last_name,\n" +
                "       p.first_name,\n" +
                "       p.pather_name,\n" +
                "       g.name        as GR_NAME,\n" +
                "       pt.last_name  as TEACHER_LAST_NAME,\n" +
                "       pt.first_name  as TEACHER_FIRST_NAME,\n" +
                "       pt.pather_name as TEACHER_PATHER_NAME,\n" +
                "       s.name        as SB_NAME,\n" +
                "       marks.value\n" +
                "from marks\n" +
                "         join subjects s on marks.subject_id = s.id\n" +
                "         join people p on marks.student_id = p.id\n" +
                "         join people pt on marks.teacher_id = pt.id\n" +
                "         join groups g on g.id = p.group_id"
    ): List<Marks>? {
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Marks(
                        resultSet.getLong("ID_MARK"),
                        resultSet.getString("LAST_NAME"),
                        resultSet.getString("FIRST_NAME"),
                        resultSet.getString("PATHER_NAME"),
                        resultSet.getString("GR_NAME"),
                        resultSet.getString("TEACHER_LAST_NAME"),
                        resultSet.getString("TEACHER_FIRST_NAME"),
                        resultSet.getString("TEACHER_PATHER_NAME"),
                        resultSet.getString("SB_NAME"),
                        null,
                        null,
                        resultSet.getLong("VALUE")
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getFilteredFullMarksList(
        connection: Connection,
        secondName: String = "",
        firstName: String = "",
        middleName: String = "",
        groupName: String = "",
        subject: String = "",
        year_from: Long? = 0,
        year_to: Long? = 3000
    ): List<Marks>? {
        val sql = "select marks.id as ID_MARK, p.last_name,\n" +
                "       p.first_name,\n" +
                "       p.pather_name,\n" +
                "       g.name        as GR_NAME,\n" +
                "       pt.last_name  as TEACHER_LAST_NAME,\n" +
                "       pt.first_name  as TEACHER_FIRST_NAME,\n" +
                "       pt.pather_name as TEACHER_PATHER_NAME,\n" +
                "       s.name        as SB_NAME,\n" +
                "       marks.value\n" +
                "from marks\n" +
                "         join subjects s on marks.subject_id = s.id\n" +
                "         join people p on marks.student_id = p.id\n" +
                "         join people pt on marks.teacher_id = pt.id\n" +
                "         join groups g on g.id = p.group_id" +
                " where lower(p.last_name) like '%${secondName.toLowerCase()}%'\n" +
                "  and lower(p.first_name) like '%${firstName.toLowerCase()}%'\n" +
                "  and lower(p.pather_name) like '%${middleName.toLowerCase()}%'\n" +
                "  and lower(s.name) like '%${subject.toLowerCase()}%'\n" +
                "  and g.name like '%${groupName.toLowerCase()}%'\n" +
                "  and TO_NUMBER(substr(g.name, -4, 4)) between '$year_from' and '$year_to'" +
                "order by marks.id"
        return getFullMarksList(connection, sql)
    }

    fun deleteMark(connection: Connection, id: Long?): Boolean {
        val sql = "delete from marks mk where mk.id = $id"
        return (doSomethingWithResult(connection, sql))
    }

    fun getGroupList(connection: Connection): List<Group>? {
        val sql = "select gr.id as GR_ID,\n" +
                "       gr.name                           as GR_NAME,\n" +
                "       TO_NUMBER(substr(gr.name, -4, 4)) as YEAR,\n" +
                "       TO_NUMBER(substr(gr.name, 1, 1))  as COURSE,\n" +
                "       count(p.id)                         as PEOPLE_COUNT\n" +
                "from groups gr\n" +
                "          left join people p on gr.id = p.group_id\n" +
                "group by gr.name,gr.id\n" +
                " order by YEAR"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Group(
                        resultSet.getString("GR_NAME"),
                        resultSet.getLong("YEAR"),
                        resultSet.getLong("COURSE"),
                        resultSet.getLong("PEOPLE_COUNT"),
                        resultSet.getLong("GR_ID")
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getStudentsFromGroup(connection: Connection, idGroup: Long): List<Student>? {
        val sql = "select p.id as ID_STUDENT, p.first_name as FIRST_NAME, p.last_name as LAST_NAME," +
                "p.pather_name as PATHER_NAME\n" +
                "from people p\n" +
                "inner join groups gr on p.group_id = gr.id\n" +
                "where gr.id = '$idGroup'"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Student(
                        resultSet.getString("LAST_NAME"),
                        resultSet.getString("FIRST_NAME"),
                        resultSet.getString("PATHER_NAME"),
                        resultSet.getLong("ID_STUDENT")
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun createGroup(connection: Connection, name: String?): Boolean {
        val sql = "insert into GROUPS (NAME)\n" +
                "values ('${name}')"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return resultSet.next()
        } catch (ex: SQLException) {
            println(ex)
        }
        return false
    }

    fun deleteGroup(connection: Connection, groupName: String): Boolean {
        val sql = "begin deleteGroup('${groupName}'); end;"
        try {
            val cs: CallableStatement = connection.prepareCall(sql)
            cs.execute()
            return true
        } catch (ex: SQLException) {
            println(ex)
        }
        return false
    }

    fun createStudent(
        connection: Connection,
        name: String?,
        surname: String?,
        middleName: String?,
        group: String
    ): Boolean {
        val sql = "begin\n" +
                "    INSERTTOGROUP('${group}','${name}','${surname}','${middleName}');\n" +
                "end;"
        try {
            val cs: CallableStatement = connection.prepareCall(sql)
            cs.execute()
            return true
        } catch (ex: SQLException) {
            println(ex)
        }
        return false
    }

    fun deleteStudent(connection: Connection, idStudent: Long?): Boolean {
        val sql = "begin\n" +
                "    deleteStudent($idStudent);\n" +
                "end;"
        try {
            val cs: CallableStatement = connection.prepareCall(sql)
            cs.execute()
            return true
        } catch (ex: SQLException) {
            println(ex)
        }
        return false
    }

    fun getSubjectList(connection: Connection): List<Subject>? {
        val sql = "select sb.id as ID_SUBJECT, sb.name as SB_NAME from subjects sb"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Subject(
                        resultSet.getString("SB_NAME"),
                        resultSet.getLong("ID_SUBJECT")
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun createMark(connection: Connection, it: Marks): Boolean {
        val sql = "begin\n" +
                "    createMark\n" +
                "    ('${it.stSecondName}','${it.stFirstName}','${it.stMiddleName}'," +
                "'${it.thSecondName}','${it.thFirstName}','${it.thMiddleName}','${it.subject?.toLowerCase()}','${it.value}');\n" +
                "    end;"
        println(sql)
        try {
            val cs: CallableStatement = connection.prepareCall(sql)
            cs.execute()
            return true
        } catch (ex: SQLException) {
            println(ex)
        }
        return false
    }

    fun editMark(connection: Connection, idMark: Long, value: Long): Boolean {
        val sql = "update marks mk set mk.value = '$value' where mk.id = ${idMark}\n"
        return (doSomethingWithResult(connection, sql))
    }

    fun getStatisticsList(connection: Connection): List<Statistics>? {
        val sql = "with marks_sb as (\n" +
                "    select  substr(gr.name, -4, 4) as year, s.name as sb_name, mk.value as mk_value\n" +
                "    from groups gr\n" +
                "             inner join people p on gr.id = p.group_id\n" +
                "             inner join marks mk on mk.student_id = p.id\n" +
                "             inner join subjects s on mk.subject_id = s.id\n" +
                "    order by year)\n" +
                "select marks_sb.year, marks_sb.sb_name, avg(marks_sb.mk_value) as avg\n" +
                "from marks_sb\n" +
                "group by marks_sb.year, marks_sb.sb_name\n" +
                "order by year"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Statistics(
                        year = resultSet.getString(1), subject = resultSet.getString(2), avg = resultSet.getString(3)
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getFilteredStatistics(
        connection: Connection,
        gr: String,
        sb: String,
        from: String,
        to: String
    ): List<Statistics>? {
        val sql = "with marks_sb as (\n" +
                "    select substr(gr.name, -4, 4) as year,\n" +
                "           s.name                 as sb_name,\n" +
                "           mk.value               as mk_value,\n" +
                "           gr.name                as gr_name\n" +
                "    from groups gr\n" +
                "             inner join people p on gr.id = p.group_id\n" +
                "             inner join marks mk on mk.student_id = p.id\n" +
                "             inner join subjects s on mk.subject_id = s.id\n" +
                "    order by year)\n" +
                "select marks_sb.year, marks_sb.sb_name, avg(marks_sb.mk_value) as avg, gr_name\n" +
                "from marks_sb\n" +
                "where year between '$from' and '$to'\n" +
                "  and gr_name like '%${gr}%'\n" +
                "    and sb_name like'%${sb}%'\n" +
                "group by marks_sb.year,  marks_sb.sb_name,gr_name\n" +
                "order by year\n"
        // println(sql)
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Statistics(
                        year = resultSet.getString(1),
                        subject = resultSet.getString(2),
                        avg = resultSet.getString(3),
                        group = resultSet.getString(4)
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getFilteredStatisticsByTeacher(
        connection: Connection,
        teacher: String,
        first: String,
        secondName: String,
        middle: String
    ): List<Statistics>? {
        val student = if (secondName.isNotBlank() || first.isNotBlank() || middle.isNotBlank()) {
            "${secondName} ${first} ${middle}".trim()
        } else {
            ""
        }
        println("Student is \n${student}")
        val sql = "\n" +
                "with marks_sb as (\n" +
                "    select substr(gr.name, -4, 4) as year, s.name as sb_name, mk.value as mk_value\n" +
                "    from marks mk\n" +
                "             inner join people p on mk.teacher_id = p.id\n" +
                "             inner join people st on mk.student_id = st.id\n" +
                "             inner join groups gr on st.group_id = gr.id\n" +
                "             inner join subjects s on mk.subject_id = s.id\n" +
                "    where (lower(p.last_name) || ' ' || lower(p.first_name) || ' ' || " +
                "lower(p.pather_name)) like lower('%${teacher}%')\n" +
                "    and (lower(st.last_name) || ' ' || lower(st.first_name) || ' ' || " +
                "lower(st.pather_name)) like lower('%$student%')\n" +
                "    order by year)\n" +
                "\n" +
                "select marks_sb.year, marks_sb.sb_name, avg(marks_sb.mk_value) as avg\n" +
                "from marks_sb\n" +
                "group by marks_sb.year, marks_sb.sb_name\n" +
                "order by year"
        print(sql)
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Statistics(
                        year = resultSet.getString(1),
                        subject = resultSet.getString(2),
                        avg = resultSet.getString(3)
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun createSubject(connection: Connection, name: String): Boolean {
        val sql = "INSERT INTO SUBJECTS (NAME)\n" +
                "VALUES ('${name}')"
        return (doSomethingWithResult(connection, sql))
    }

    @Throws(SQLException::class)
    private fun <T> getFromResultSet(resultSet: ResultSet, action: () -> T): List<T>? {
        val records: MutableList<T> = ArrayList()
        do {
            val tmp: T = action()
            records.add(tmp)
        } while (resultSet.next())
        return records
    }


}