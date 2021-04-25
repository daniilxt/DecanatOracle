package JDBC

import poko.Teacher
import poko.TeacherSubjects
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
        middleName: String = ""
    ): List<Teacher>? {
        val sql =
            "select p.last_name as LAST_NAME, p.first_name as FIRST_NAME, p.pather_name as PATHER_NAME," +
                    " p.first_name as  NAME,p.id as ID\n" +
                    "from people p\n" +
                    "where lower(p.last_name) like '%${secondName.toLowerCase()}%'\n" +
                    "  and lower(p.first_name) like '%${firstName.toLowerCase()}%'\n" +
                    "  and lower(p.pather_name) like '%${middleName.toLowerCase()}%'"
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
/*

    fun getUser(login: String, connection: Connection): User? {

        val sql = "SELECT * FROM User WHERE Login = '$login'"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            if (resultSet.next()) {
                return User(
                        resultSet.getString("Login"),
                        resultSet.getString("Password"),
                        Role.valueOf(resultSet.getString("Role").toUpperCase())
                )
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getTasks(idEngineer: Long, connection: Connection): List<Tasks>? {
        val sql = "SELECT t.IdTask, BeerStorage.Name NameBeer, t.IdTechnologicalEngineer, t.Amount,t.Date,t.Status\n" +
                "from Task t " +
                " inner join BeerStorage on BeerStorage.IdBeerKind = t.IdBeerKind\n" +
                "where t.IdTechnologicalEngineer = $idEngineer"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet<Tasks>(resultSet) {
                    Tasks(
                            resultSet.getLong("IdTask"), resultSet.getLong("IdTechnologicalEngineer"),
                            resultSet.getString("NameBeer"), resultSet.getDate("Date"),
                            resultSet.getLong("Amount"), resultSet.getString("Status")
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getTask(idTask: Long, connection: Connection): Task? {
        val sql = "select t.IdTask,BeerStorage.IdBeerKind, BeerStorage.Name NameBeer, t.Amount,t.Date\n" +
                "from Task t\n" +
                "         inner join BeerStorage on BeerStorage.IdBeerKind = t.IdBeerKind\n" +
                "where t.IdTask = $idTask"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Task(
                            resultSet.getLong("IdTask"), resultSet.getLong("IdBeerKind"),
                            resultSet.getString("NameBeer"), resultSet.getLong("Amount"),
                            resultSet.getDate("Date")
                    )
                }?.get(0)
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getRecipe(idBeerKind: Long, connection: Connection): List<Recipe>? {
        val sql = "select ResourceStorage.Name as ResName,\n" +
                "       RecipeList.Amount,\n" +
                "       ResourceStorage.Amount  ResAmount, ResourceStorage.Unit, ResourceStorage.Price ResPrice\n" +
                "from RecipeList\n" +
                "         inner join ResourceStorage on RecipeList.IdResource = ResourceStorage.IdResource\n" +
                "         inner join BeerStorage on RecipeList.IdBeerKind = BeerStorage.IdBeerKind\n" +
                "where BeerStorage.IdBeerKind = ${idBeerKind}\n" +
                "  and BeerStorage.Type != 'Import' "
        try {
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Recipe(
                            resultSet.getString("ResName"), resultSet.getLong("Amount"),
                            resultSet.getLong("ResAmount"), resultSet.getString("Unit"),
                            resultSet.getLong("ResPrice")
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun countFreeCCT(connection: Connection?): Pair<Long, Long> {
        val sql1 = "select count(c.StatusCCT) from CylindricallyConicalTank c\n" +
                "where c.StatusCCT = 'Free' "
        val sql2 = "select count(c.StatusCCT) from CylindricallyConicalTank c\n"
        try {
            var resultSet = connection!!.createStatement()?.executeQuery(sql1)
            val first = if (resultSet!!.next()) {
                resultSet.getLong(1)
            } else 0
            resultSet = connection.createStatement()?.executeQuery(sql2)
            val second = if (resultSet!!.next()) {
                resultSet.getLong(1)
            } else 0

            return Pair(first, second)
        } catch (ex: SQLException) {
            println(ex)
        }
        return Pair(0, 0)
    }

    fun showCCT(connection: Connection): List<CylindricallyTank>? {
        val sql = "select * from CylindricallyConicalTank"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    CylindricallyTank(
                            resultSet.getLong(1), resultSet.getLong(2), resultSet.getDate(3),
                            resultSet.getDate(4), resultSet.getString(5)
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getWorkerByLogin(login: String, connection: Connection): Worker? {
        val sql = "select * from Workers WK where WK.Login = '${login}'"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Worker(
                            resultSet.getLong("ID_WORKER"), resultSet.getString("Name"),
                            resultSet.getString("SecondName"), resultSet.getString("MiddleName"),
                            resultSet.getString("Phone"), resultSet.getDate("DateJoin"),
                            resultSet.getDate("DateDismiss"), resultSet.getString("Login"),
                            resultSet.getLong("WorksDays")
                    )
                }?.get(0)
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun checkRes(resName: String, amount: Long, connection: Connection): Long {
        val sql = "set @num =0;"
        val sql2 = "call validateResources(${amount},'${resName}',@num);"
        val sql3 = "select @num;"
        try {
            connection.createStatement().executeQuery(sql)
            connection.createStatement().executeQuery(sql2)
            val resultSet = connection.createStatement().executeQuery(sql3)
            return if (resultSet!!.next()) {
                resultSet.getLong(1)
            } else 0
        } catch (ex: SQLException) {
            println(ex)
        }
        return 0
    }

    fun checkBeer(beerName: String, amount: Long, connection: Connection): Long {
        val sql = "set @num =0;"
        val sql2 = "call validateBeer(${amount},'${beerName}',@num);"
        val sql3 = "select @num;"
        try {
            connection.createStatement().executeQuery(sql)
            connection.createStatement().executeQuery(sql2)
            val resultSet = connection.createStatement().executeQuery(sql3)
            return if (resultSet!!.next()) {
                resultSet.getLong(1)
            } else 0
        } catch (ex: SQLException) {
            println(ex)
        }
        return 0
    }

    fun getBeerMenu(connection: Connection): List<BeerMenu>? {
        val sql = "select * from BeerMenu"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    BeerMenu(
                            resultSet.getString("Name"), resultSet.getString("Type"),
                            resultSet.getLong("Amount"), resultSet.getLong("Price")
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getBarmanAlcOrders(connection: Connection, idManager: Long): List<Orders>? {
        val sql = "select * from barmanalcorders\n" +
                "where barmanalcorders.IdManager = ${idManager} and barmanalcorders.Status != 'done' "
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Orders(
                            resultSet.getLong(1), resultSet.getLong(2),
                            resultSet.getDate(4), resultSet.getString(5)
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getEngineerResOrders(connection: Connection, idManager: Long): List<Orders>? {
        val sql = "select * from TechnologistEngineerResOrders TET\n" +
                "where TET.IdManager = ${idManager} and TET.Status != 'done' "
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Orders(
                            resultSet.getLong(1), resultSet.getLong(2),
                            resultSet.getDate(4), resultSet.getString(5)
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getBarmanAlcOrdersPosition(connection: Connection, idTask: Long): List<OrderPosition>? {
        val sql = "select b.Name, BOP.Number, b.Type, b.Price * BOP.Number as Price\n" +
                "from barmanalcorderposition BOP\n" +
                "         inner join beerstorage b on BOP.IdBeerKind = b.IdBeerKind\n" +
                "where IdBarmanAlcOrder = ${idTask}"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    OrderPosition(
                            resultSet.getString(1), resultSet.getString(3),
                            resultSet.getLong(2), resultSet.getLong(4)
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getEngineerResOrdersPosition(connection: Connection, idTask: Long): List<OrderPosition>? {
        val sql = "select r.Name, TEOP.Number, r.Unit, r.Price * TEOP.Number as Price\n" +
                "from technologistengineerresorderposition TEOP\n" +
                "         inner join resourcestorage r on TEOP.IdResource = r.IdResource\n" +
                "where IdTechnologistEngineerResOrder = ${idTask}"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    OrderPosition(
                            resultSet.getString(1), resultSet.getString(3),
                            resultSet.getLong(2), resultSet.getLong(4)
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getLoaderTasks(connection: Connection, idLoader: Long): List<LoaderTask>? {
        val sql = "select * from loadertask LT\n" +
                "where LT.IdLoaderMan = ${idLoader} and LT.Status != 'done' "
        try {
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    LoaderTask(
                            resultSet.getLong("IdLoaderTask"), resultSet.getLong("IdResBuy"),
                            resultSet.getLong("IdImportAlc"), resultSet.getDate("Date"),
                            resultSet.getString("Status")
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getAndCheckTask(connection: Connection, idResBuy: Long, idImportAlc: Long): Pair<Long?, Long?>? {
        var sql = "select * from ResBuy RB\n" +
                "where RB.Status != 'system' and RB.IdResBuy = $idResBuy"
        try {
            var pair1: Long? = null
            var resultSet = connection.createStatement().executeQuery(sql)
            if (resultSet.next()) {
                pair1 = resultSet.getLong("IdResBuy")
            }
            sql = "select * from ImportAlcBuy IAB\n" +
                    "where IAB.Status != 'system' and IAB.IdImportAlcBuy = ${idImportAlc}"
            var pair2: Long? = null
            resultSet = connection.createStatement().executeQuery(sql)
            if (resultSet.next()) {
                pair2 = resultSet.getLong("IdImportAlcBuy")
            }
            return Pair(pair1, pair2)

        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getTaskResources(connection: Connection, first: Long): List<TaskResource>? {
        val sql = "select RB.IdResBuy, Name, RBP.Number, Date\n" +
                "from ResBuyPosition RBP\n" +
                "         inner join ResBuy RB on RBP.IdResBuy = RB.IdResBuy\n" +
                "         inner join ResourceStorage RS on RBP.IdResource = RS.IdResource\n" +
                "where RB.IdResBuy = ${first}\n"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    TaskResource(
                            resultSet.getLong("IdResBuy"), resultSet.getString("Name"),
                            resultSet.getLong("Number"), resultSet.getDate("Date")

                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getTaskResourcesAlc(connection: Connection, second: Long): List<TaskResource>? {
        val sql = "select I.IdImportAlcBuy, Name, IAB.Number, Date\n" +
                "from ImportAlcBuyPosition IAB\n" +
                "inner join ImportAlcBuy I on IAB.IdImportAlcBuy = I.IdImportAlcBuy\n" +
                "inner join beerstorage b on IAB.IdBeerKind = b.IdBeerKind\n" +
                "where I.IdImportAlcBuy = ${second}"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    TaskResource(
                            resultSet.getLong("IdImportAlcBuy"), resultSet.getString("Name"),
                            resultSet.getLong("Number"), resultSet.getDate("Date")

                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun updateResourceStoreList(connection: Connection, resName: String, amount: Long): List<ResStorage>? {
        var sql = "call changeResCount(${amount},'${resName}')"
        try {
            connection.createStatement().executeQuery(sql)
            sql = "select * from ResourceStorage"
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    ResStorage(
                            resultSet.getString("Name"), resultSet.getLong("Amount"),
                            resultSet.getString("Unit"), resultSet.getLong("Price")
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun updateBeerStoreList(connection: Connection, name: String, amount: Long): List<AlcStorage>? {
        var sql = "call changeBeerCount(${amount},'${name}')"
        try {
            connection.createStatement().executeQuery(sql)
            sql = "select * from BeerStorage"
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    AlcStorage(
                            resultSet.getString("Name"), resultSet.getLong("Amount"),
                            resultSet.getString("Type"), resultSet.getLong("Price")
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun updateStatusResTask(connection: Connection, status: String, idTask: Long) {
        val sql = "call changeResTaskStatus('${status}',${idTask})"
        try {
            connection.createStatement().executeQuery(sql)
        } catch (ex: SQLException) {
            println(ex)
        }
    }

    fun updateStatusResTaskTechnologistEngineer(connection: Connection, status: String, idTask: Long) {
        val sql = "call changeResTaskTechnologistEngineerStatus('${status}',${idTask})"
        try {
            connection.createStatement().executeQuery(sql)
        } catch (ex: SQLException) {
            println(ex)
        }
    }

    fun updateStatusAlcBarman(connection: Connection, status: String, idTask: Long) {
        val sql = "call changeAlcBarmanStatusStatus('${status}',${idTask})"
        try {
            connection.createStatement().executeQuery(sql)
        } catch (ex: SQLException) {
            println(ex)
        }
    }

    fun updateStatusAlcTask(connection: Connection, status: String, idTask: Long) {
        val sql = "call changeAlcTaskStatus('${status}',${idTask})"
        try {
            connection.createStatement().executeQuery(sql)
        } catch (ex: SQLException) {
            println(ex)
        }
    }

    fun checkLogin(connection: Connection, login: String): Boolean {
        val sql = "select * from User US where us.Login = '${login}'"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return resultSet.next()
        } catch (ex: SQLException) {
            println(ex)
        }
        return false
    }

    fun createAccount(connection: Connection, login: String, password: String, role: Role): Pair<Boolean, Long> {
        var sql = "INSERT INTO User (Login, Password, Role)" +
                " VALUES ('${login}','${password}','${role.toString().toLowerCase()}')\n"
        try {
            connection.createStatement().executeQuery(sql)
            sql = "select IdUser from User where Login = '${login}'"
            val resultSet = connection.createStatement().executeQuery(sql)
            if (resultSet.next()) {
                return Pair(true, resultSet.getLong(1))
            }
        } catch (ex: SQLException) {
            println(ex)
            return Pair(false, 0)
        }
        return Pair(false, 0)
    }

    fun createHuman(connection: Connection, client: Client, role: Role): Boolean {

        val sql = when (role) {
            Role.CLIENT -> "INSERT INTO ClientList (NameClient, SecondNameClient, MiddleNameClient, PhoneClient, Age, DateJoin, IdUser)\n" +
                    "VALUES ( '${client.nameClient}', '${client.secondNameClient}', '${client.middleNameClient}'," +
                    " '${client.phoneClient}', '${client.age}', '${client.dateJoin}', '${client.idUser}');"
            Role.BARMAN -> "INSERT INTO BarMan (Name, SecondName, MiddleName, Phone, DateJoin, Salary, IdUser)\n" +
                    "VALUES ( '${client.nameClient}', '${client.secondNameClient}', '${client.middleNameClient}'," +
                    " '${client.phoneClient}', '${client.age}', '${22000}', '${client.idUser}');"
            Role.MANAGER -> "INSERT INTO Manager (Name, SecondName, MiddleName, Phone, DateJoin, Salary, IdUser)\n" +
                    "VALUES ( '${client.nameClient}', '${client.secondNameClient}', '${client.middleNameClient}'," +
                    " '${client.phoneClient}', '${client.age}', '${22000}', '${client.idUser}');"
            Role.STAFF_MANAGER -> "INSERT INTO StaffManager (Name, SecondName, MiddleName, Phone, DateJoin, Salary, IdUser)\n" +
                    "VALUES ( '${client.nameClient}', '${client.secondNameClient}', '${client.middleNameClient}'," +
                    " '${client.phoneClient}', '${client.age}', '${22000}', '${client.idUser}');"
            Role.LOADER -> "INSERT INTO LoaderMan (Name, SecondName, MiddleName, Phone, DateJoin, Salary, IdUser)\n" +
                    "VALUES ( '${client.nameClient}', '${client.secondNameClient}', '${client.middleNameClient}'," +
                    " '${client.phoneClient}', '${client.age}', '${22000}', '${client.idUser}');"
            Role.ENGINEER -> "INSERT INTO TechnologistEngineer (Name, SecondName, MiddleName, Phone, DateJoin, Salary, IdUser)\n" +
                    "VALUES ( '${client.nameClient}', '${client.secondNameClient}', '${client.middleNameClient}'," +
                    " '${client.phoneClient}', '${client.age}', '${22000}', '${client.idUser}');"
            else -> ""
        }
        return try {
            val resultSet = connection.createStatement().executeQuery(sql)
            resultSet.next()
        } catch (ex: SQLException) {
            println(ex)
            true
        }
    }


    fun deleteAccount(connection: Connection, login: String) {
        val sql = "call deleteUserByLogin('${login}')"
        try {
            connection.createStatement().executeQuery(sql)
        } catch (ex: SQLException) {
            println(ex)
        }
    }

    fun getClientByLogin(connection: Connection, login: String): Client? {
        val sql = "select NameClient, SecondNameClient, MiddleNameClient, PhoneClient, Age, IdClient, CL.IdUser\n" +
                "from ClientList CL\n" +
                "         inner join User US on CL.IdUser = US.IdUser\n" +
                "where US.Login = '${login}'"
        try {
            connection.createStatement().executeQuery(sql)
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Client(
                            resultSet.getString(1), resultSet.getString(2),
                            resultSet.getString(3), resultSet.getString(4),
                            resultSet.getDate(5), idClient = resultSet.getLong(6),
                            idUser = resultSet.getLong("IdUser")
                    )
                }?.get(0)
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun createOrder(connection: Connection, items: List<BeerMenu>, manager: Long, date: java.sql.Date, idClient: Long) {
        var sql = "INSERT INTO Orders (IdClient, IdManager, Date, Status)\n" +
                "VALUES (${idClient}, ${manager}, '${date}', 'process');"

        try {
            connection.createStatement().executeQuery(sql)
            sql = "select IdOrder from Orders where IdClient = ${idClient}"
            var resultSet = connection.createStatement().executeQuery(sql)
            val records: MutableList<Long> = ArrayList()
            if (resultSet.next()) {
                do {
                    val tmp: Long = resultSet.getLong(1)
                    records.add(tmp)
                } while (resultSet.next())
            }

            items.forEach {
                sql = "set @id = 0;"
                connection.createStatement().executeQuery(sql)

                sql = "SET @id = (select IdBeerKind from BeerStorage where Name = '${it.name}');"
                connection.createStatement().executeQuery(sql)

                sql = "select @id;"
                resultSet = connection.createStatement().executeQuery(sql)
                val idBeerKind = if (resultSet!!.next()) {
                    resultSet.getLong(1)
                } else 0

                sql = "INSERT INTO  OrderPosition (Number, IdOrder, IdBeerKind)\n" +
                        "VALUES (${it.amount}, ${records.last()}, ${idBeerKind});"
                connection.createStatement().executeQuery(sql)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getCountManagers(connection: Connection): Long {
        val sql = "select count(IdManager) from manager"
        return try {
            val resultSet = connection.createStatement().executeQuery(sql)
            if (resultSet.next()) {
                resultSet.getLong(1)
            } else {
                0
            }
        } catch (ex: SQLException) {
            println(ex)
            0
        }
    }

    fun getOrdersByClient(connection: Connection, idClient: Long): List<Orders>? {
        val sql = "select * from Orders\n" +
                "where IdClient = ${idClient}"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Orders(
                            resultSet.getLong(1), resultSet.getLong(3),
                            resultSet.getDate(4), resultSet.getString(5)
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getOrderPositionByOrderId(connection: Connection, idClient: Long, idOrder: Long): List<OrderPosition>? {
        val sql = "select BS.Name, BS.Type, OP.Number, sum(BS.Price * OP.Number) from OrderPosition OP\n" +
                "inner join Orders O on O.IdOrder = OP.IdOrder\n" +
                "inner join BeerStorage BS on OP.IdBeerKind = BS.IdBeerKind\n" +
                "where IdClient = ${idClient} and O.IdOrder = ${idOrder}\n" +
                "group by BS.Name"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    OrderPosition(
                            resultSet.getString(1), resultSet.getString(2),
                            resultSet.getLong(3), resultSet.getLong(4)
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getClientsList(connection: Connection): List<Client>? {
        val sql = "select * from ClientList"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Client(
                            resultSet.getString(2), resultSet.getString(3),
                            resultSet.getString(4), resultSet.getString(5),
                            resultSet.getDate(6), resultSet.getDate(7),
                            resultSet.getDate(9), resultSet.getLong(8),
                            resultSet.getLong(1)
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun deleteClient(connection: Connection, idClient: Long, idUser: Long, date: Date) {
        try {
            val sql = "select Login from  User\n" +
                    "inner join clientlist c on user.IdUser = c.IdUser\n" +
                    "where IdClient = ${idClient}"
            val resultSet = connection.createStatement().executeQuery(sql)
            if (resultSet.next()) {
                val login = resultSet.getString(1)
                println(login)
                deleteAccount(connection, login)
            }

        } catch (ex: SQLException) {
            println(ex)
        }
    }

    fun getWorkers(connection: Connection): List<Worker>? {
        val sql = "select * from Workers"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Worker(
                            resultSet.getLong("ID_WORKER"), resultSet.getString("Name"),
                            resultSet.getString("SecondName"), resultSet.getString("MiddleName"),
                            resultSet.getString("Phone"), resultSet.getDate("DateJoin"),
                            resultSet.getDate("DateDismiss"), resultSet.getString("Login"),
                            resultSet.getLong("WorksDays")
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun createBarmanOrder(connection: Connection, items: List<BeerMenu>, idBarman: Long, nowDate: Date) {
        var sql = "INSERT INTO BarmanOrders (IdBarman, Date, Status)\n" +
                "VALUES (${idBarman}, '${nowDate}', 'done');"

        try {
            connection.createStatement().executeQuery(sql)
            sql = "select IdBarmanOrder from BarmanOrders where IdBarman = ${idBarman}"
            var resultSet = connection.createStatement().executeQuery(sql)
            val records: MutableList<Long> = ArrayList()
            if (resultSet.next()) {
                do {
                    val tmp: Long = resultSet.getLong(1)
                    records.add(tmp)
                } while (resultSet.next())
            }

            items.forEach {
                sql = "set @id = 0;"
                connection.createStatement().executeQuery(sql)

                sql = "SET @id = (select IdBeerKind from BeerStorage where Name = '${it.name}');"
                connection.createStatement().executeQuery(sql)

                sql = "select @id;"
                resultSet = connection.createStatement().executeQuery(sql)
                val idBeerKind = if (resultSet!!.next()) {
                    resultSet.getLong(1)
                } else 0

                sql = "INSERT INTO  BarmanOrderPosition (Number, IdBarmanOrder, IdBeerKind)\n" +
                        "VALUES (${it.amount}, ${records.last()}, ${idBeerKind});"
                connection.createStatement().executeQuery(sql)
                sql = "call changeBeerCount('-${it.amount}','${it.name}')"
                connection.createStatement().executeQuery(sql)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getManagers(connection: Connection): List<Worker>? {
        val sql = "select * from manager\n" +
                "inner join user  u on manager.IdUser = u.IdUser"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Worker(
                            resultSet.getLong(1), resultSet.getString(2),
                            resultSet.getString(3), resultSet.getString(4),
                            resultSet.getString(5), dateJoin = null, dateDismiss = null, login = null, worksDays = 0
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getEngineers(connection: Connection): List<Worker>? {
        val sql = "select * from technologistengineer t\n" +
                "inner join user  u on t.IdUser = u.IdUser"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Worker(
                            resultSet.getLong(1), resultSet.getString(2),
                            resultSet.getString(3), resultSet.getString(4),
                            resultSet.getString(5), dateJoin = null, dateDismiss = null, login = null, worksDays = 0
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun createAlcoOrder(connection: Connection, items: List<BeerMenu>, idBarman: Long, idManager: Long, nowDate: Date) {
        var sql = "INSERT INTO BarmanAlcOrders (IdBarman, IdManager,Date, Status)\n" +
                "VALUES (${idBarman},${idManager}, '${nowDate}', 'process');"

        try {
            connection.createStatement().executeQuery(sql)
            sql = "select IdBarmanAlcOrder from BarmanAlcOrders where IdBarman = ${idBarman}"
            var resultSet = connection.createStatement().executeQuery(sql)
            val records: MutableList<Long> = ArrayList()
            if (resultSet.next()) {
                do {
                    val tmp: Long = resultSet.getLong(1)
                    records.add(tmp)
                } while (resultSet.next())
            }

            items.forEach {
                sql = "set @id = 0;"
                connection.createStatement().executeQuery(sql)

                sql = "SET @id = (select IdBeerKind from BeerStorage where Name = '${it.name}');"
                connection.createStatement().executeQuery(sql)

                sql = "select @id;"
                resultSet = connection.createStatement().executeQuery(sql)
                val idBeerKind = if (resultSet!!.next()) {
                    resultSet.getLong(1)
                } else 0

                sql = "INSERT INTO  BarmanAlcOrderPosition (Number, IdBarmanAlcOrder, IdBeerKind)\n" +
                        "VALUES (${it.amount}, ${records.last()}, ${idBeerKind});"
                connection.createStatement().executeQuery(sql)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun createResOrder(connection: Connection, items: List<Recipe>, idTechnologistEngineer: Long, idManager: Long, nowDate: Date) {
        var sql = "INSERT INTO TechnologistEngineerResOrders (IdTechnologistEngineer, IdManager,Date, Status)\n" +
                "VALUES (${idTechnologistEngineer},${idManager}, '${nowDate}', 'process');"

        try {
            connection.createStatement().executeQuery(sql)
            sql = "select IdTechnologistEngineerResOrder from TechnologistEngineerResOrders where IdTechnologistEngineer = ${idTechnologistEngineer}"
            var resultSet = connection.createStatement().executeQuery(sql)
            val records: MutableList<Long> = ArrayList()
            if (resultSet.next()) {
                do {
                    val tmp: Long = resultSet.getLong(1)
                    records.add(tmp)
                } while (resultSet.next())
            }

            items.forEach {
                sql = "set @id = 0;"
                connection.createStatement().executeQuery(sql)

                sql = "SET @id = (select IdResource from ResourceStorage where Name = '${it.resName}');"
                connection.createStatement().executeQuery(sql)

                sql = "select @id;"
                resultSet = connection.createStatement().executeQuery(sql)
                val idResource = if (resultSet!!.next()) {
                    resultSet.getLong(1)
                } else 0

                sql = "INSERT INTO  TechnologistEngineerResOrderPosition (Number, IdTechnologistEngineerResOrder, IdResource)\n" +
                        "VALUES (${it.amount}, ${records.last()}, ${idResource});"
                connection.createStatement().executeQuery(sql)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun createLoaderAlcoTask(connection: Connection, idManager: Long, idBarman: Long, items: List<OrderPosition>, loader: Int, nowDate: Date, idTask: Long) {
        var sql = "INSERT INTO ImportAlcBuy (IdBarman, IdManager,Date, Status)\n" +
                "VALUES (${idBarman},${idManager}, '${nowDate}', 'done');"

        try {
            connection.createStatement().executeQuery(sql)

            sql = "select IdImportAlcBuy from ImportAlcBuy where IdBarman = ${idBarman}"
            var resultSet = connection.createStatement().executeQuery(sql)
            val records: MutableList<Long> = ArrayList()
            if (resultSet.next()) {
                do {
                    val tmp: Long = resultSet.getLong(1)
                    records.add(tmp)
                } while (resultSet.next())
            }
            items.forEach {
                sql = "set @id = 0;"
                connection.createStatement().executeQuery(sql)

                sql = "SET @id = (select IdBeerKind from BeerStorage where Name = '${it.beerName}');" // beername = name!!
                connection.createStatement().executeQuery(sql)

                sql = "select @id;"
                resultSet = connection.createStatement().executeQuery(sql)
                val idBeerKind = if (resultSet!!.next()) {
                    resultSet.getLong(1)
                } else 0

                sql = "INSERT INTO ImportAlcBuyPosition (Number, IdImportAlcBuy, IdBeerKind)\n" +
                        "VALUES (${it.amount}, ${records.last()}, ${idBeerKind});"
                connection.createStatement().executeQuery(sql)
            }
            sql = "INSERT INTO LoaderTask (IdLoaderMan, IdResBuy,IdImportAlc,Date, Status, IdManager)\n" +
                    "VALUES (${loader},5,${records.last()},'${nowDate}', 'process','${idManager}');"
            connection.createStatement().executeQuery(sql)
            updateStatusAlcBarman(connection, "done", idTask)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun createLoaderResTask(connection: Connection, idManager: Long, idEngineer: Long, items: List<OrderPosition>, loader: Int, nowDate: Date, idTask: Long) {
        var sql = "INSERT INTO ResBuy (IdEngineer, IdManager,Date, Status)\n" +
                "VALUES (${idEngineer},${idManager}, '${nowDate}', 'done');"
        try {
            connection.createStatement().executeQuery(sql)

            sql = "select IdResBuy from ResBuy where IdEngineer = ${idEngineer}"
            var resultSet = connection.createStatement().executeQuery(sql)
            val records: MutableList<Long> = ArrayList()
            if (resultSet.next()) {
                do {
                    val tmp: Long = resultSet.getLong(1)
                    records.add(tmp)
                } while (resultSet.next())
            }
            items.forEach {
                sql = "set @id = 0;"
                connection.createStatement().executeQuery(sql)

                sql = "SET @id = (select IdResource from ResourceStorage where Name = '${it.beerName}');" // beername = name!!
                connection.createStatement().executeQuery(sql)

                sql = "select @id;"
                resultSet = connection.createStatement().executeQuery(sql)
                val idBeerKind = if (resultSet!!.next()) {
                    resultSet.getLong(1)
                } else 0

                sql = "INSERT INTO ResBuyPosition (Number, IdResBuy, IdResource)\n" +
                        "VALUES (${it.amount}, ${records.last()}, ${idBeerKind});" //idresource this
                connection.createStatement().executeQuery(sql)
            }
            sql = "INSERT INTO LoaderTask (IdLoaderMan, IdResBuy,IdImportAlc,Date, Status, IdManager)\n" +
                    "VALUES (${loader},${records.last()},1, '${nowDate}', 'process','${idManager}');"

            "VALUES (${loader},5,${records.last()},'${nowDate}', 'process','${idManager}');"
            connection.createStatement().executeQuery(sql)
            updateStatusResTaskTechnologistEngineer(connection, "done", idTask)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getClientOrders(connection: Connection, idManager: Long): List<Orders>? {
        val sql = "select * from orders\n" +
                "where IdManager = ${idManager} and Status != 'done' "
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Orders(
                            resultSet.getLong(1), resultSet.getLong(2),
                            resultSet.getDate(4), resultSet.getString(5)
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getLoginById(connection: Connection, idUser: Long): Pair<String, String>? {
        val sql = " select Login, Password from User U where U.IdUser = ${idUser}"
        try {
            connection.createStatement().executeQuery(sql)
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    Pair(resultSet.getString("Login"), resultSet.getString("Password"))
                }?.get(0)
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun updatePasswordByLogin(connection: Connection, login: String, password: String): Boolean {
        val sql = "call changePasswordByLogin('${login}','${password}')"
        try {
            connection.createStatement().executeQuery(sql)
            val resultSet = connection.createStatement().executeQuery(sql)
            return !resultSet.next()
        } catch (ex: SQLException) {
            println(ex)
        }
        return false
    }

    fun checkUserExists(connection: Connection, phone: String): Boolean {
        val sql = "SELECT EXISTS(SELECT Phone FROM workers WHERE Phone = '${phone}') or EXISTS(SELECT PhoneClient FROM clientlist WHERE PhoneClient = '${phone}')\n"
        try {
            connection.createStatement().executeQuery(sql)
            val resultSet = connection.createStatement().executeQuery(sql)
            if (resultSet.next()) {
                return resultSet.getLong(0) == 0L
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return true
    }

    fun getLoaderTasksViaManager(connection: Connection, idWorker: Long): List<LoaderTask>? {
        val sql = "select * from loadertask LT\n" +
                "where LT.IdManager = ${idWorker} and LT.Status != 'done' "
        try {
            val resultSet = connection.createStatement().executeQuery(sql)

            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    LoaderTask(
                            resultSet.getLong("IdLoaderTask"), resultSet.getLong("IdResBuy"),
                            resultSet.getLong("IdImportAlc"), resultSet.getDate("Date"),
                            resultSet.getString("Status")
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun getClientOrdersPosition(connection: Connection, idOrder: Long): List<OrderPosition>? {
        val sql = "select b.Name, OP.Number, b.Type, b.Price * OP.Number as Price\n" +
                "from orderposition OP\n" +
                "         inner join beerstorage b on OP.IdBeerKind = b.IdBeerKind\n" +
                "where IdOrder = ${idOrder}"
        try {
            val resultSet = connection.createStatement().executeQuery(sql)
            return if (!resultSet.next()) {
                null
            } else {
                getFromResultSet(resultSet) {
                    OrderPosition(
                            resultSet.getString(1), resultSet.getString(3),
                            resultSet.getLong(2), resultSet.getLong(4)
                    )
                }
            }
        } catch (ex: SQLException) {
            println(ex)
        }
        return null
    }

    fun createEngineerTask(connection: Connection, engineerId: String, arr: List<OrderPosition>, dateNow: Date) {

        try {
            arr.forEach {
                var beerKindId = 0L
                val sqlName = "select IdBeerKind from  beerstorage where Name = '${it.beerName}'"
                var resultSet = connection.createStatement().executeQuery(sqlName)
                if (resultSet.next()) {
                    beerKindId = resultSet.getLong(1)
                }
                val sql = "INSERT INTO Task (IdTechnologicalEngineer, IdBeerKind, Date, Status, Amount)\n" +
                        "VALUES ('${engineerId}',' ${beerKindId}', '${dateNow}', 'Active', '${it.amount}');"
                connection.createStatement().executeQuery(sql)

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun completeOrder(connection: Connection, currentClientTask: Orders, items: List<OrderPosition>): Boolean {
        try {
            items.forEach {
                val sql = "call changeBeerCount(${-it.amount},'${it.beerName}')"
                connection.createStatement().executeQuery(sql)
            }
            val sql = " ${currentClientTask.idOrder}"
            connection.createStatement().executeQuery(sql)
            return true
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return false
    }

    fun setToCCT(connection: Connection, freeIndex: Long, idTask: Long, nowDate: Date, dateEnd: Date?, status: String, amountTask:Long) {
        try {
            var sql = "update cylindricallyconicaltank c" +
                    " set c.StatusCCT = '${status}', c.DateStart='${nowDate}',c.DateEnd = '${dateEnd}', c.IdTask = ${idTask} " +
                    "where IdCCT = ${freeIndex}"
            connection.createStatement().executeQuery(sql)

            sql = "select * from recipelist\n" +
                    "inner join beerstorage b on recipelist.IdBeerKind = b.IdBeerKind\n" +
                    "inner join task t on b.IdBeerKind = t.IdBeerKind\n" +
                    "inner join resourcestorage r on recipelist.IdResource = r.IdResource\n" +
                    "where  IdTask = ${idTask}"
            val resultSet = connection.createStatement().executeQuery(sql)
            var arr: List<Pair<String, Long>>? = mutableListOf<Pair<String, Long>>()
            if (resultSet.next()) {
                arr = getFromResultSet(resultSet) {
                    Pair(
                            resultSet.getString("r.Name"), resultSet.getLong("recipeList.Amount")
                    )
                }
            }
            if (arr != null) {
                println("all good")
                arr.forEach {
                     sql = "call changeResCount(${-it.second*amountTask},'${it.first}')"
                    connection.createStatement().executeQuery(sql)

                }
            } else {
                println("all good")

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun handleTaskFromCCTById(connection: Connection, idCCT: Long): Boolean {
        try {
            var sql = "select IdTask from cylindricallyconicaltank where IdCCT = ${idCCT}"
            var resultSet = connection.createStatement().executeQuery(sql)
            var idTask = 0L
            if (resultSet.next()) {
                idTask = resultSet.getLong(1)
            } else {
                return false
            }
            sql = "select t.Amount, b.Name\n" +
                    "from task t\n" +
                    "inner join beerstorage b on t.IdBeerKind = b.IdBeerKind\n" +
                    "where IdTask = ${idTask}"
            resultSet = connection.createStatement().executeQuery(sql)
            var it: Pair<Long, String>
            if (resultSet.next()) {
                it = Pair(resultSet.getLong(1), resultSet.getString(2))
            } else {
                return false
            }
            sql = "call changeBeerCount(${it.first},'${it.second}')"
            connection.createStatement().executeQuery(sql)

            sql = "update task t  set t.Status = 'Done' where IdTask = ${idTask}"
            connection.createStatement().executeQuery(sql)

            sql = "update cylindricallyconicaltank c" +
                    " set c.StatusCCT = 'FREE', c.DateStart='${Date(Calendar.getInstance().time.time)}',c.DateEnd = NULL, c.IdTask = ${idTask} " +
                    "where IdCCT = ${idCCT}"
            connection.createStatement().executeQuery(sql)

            return true
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return false
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
}*/
