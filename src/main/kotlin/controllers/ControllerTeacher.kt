package controllers

import JDBC.Utils
import JDBC.dao.User
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.util.Callback
import poko.*
import java.sql.Connection


class ControllerTeacher {
    @FXML
    private var tab_loader: TabPane? = null

    @FXML
    private var tab_manager: Tab? = null

    @FXML
    private var main_btn_teachers: Button? = null

    @FXML
    private var btn_exit: Button? = null

    @FXML
    private var main_btn_groups: Button? = null

    @FXML
    private var main_btn_marks: Button? = null

    @FXML
    private var tab_alc: Tab? = null

    @FXML
    private var btn_create_alc_task: Button? = null

    @FXML
    private var table_gr_people: TableView<Student>? = null

    @FXML
    private var table_gr_people_first: TableColumn<Student, String>? = null

    @FXML
    private var table_gr_people_second: TableColumn<Student, String>? = null

    @FXML
    private var table_gr_people_middle: TableColumn<Student, String>? = null

    @FXML
    private var table_gr: TableView<Group>? = null

    @FXML
    private var table_gr_name: TableColumn<Group, String>? = null

    @FXML
    private var table_gr_year: TableColumn<Group, Long>? = null

    @FXML
    private var table_gr_course: TableColumn<Group, Long>? = null

    @FXML
    private var table_gr_count_people: TableColumn<Group, Long>? = null

    @FXML
    private var btn_gr_clean_people: Button? = null

    @FXML
    private var feld_show_alc: TextField? = null

    @FXML
    private var reg_name: TextField? = null

    @FXML
    private var reg_second_name: TextField? = null

    @FXML
    private var reg_middle_name: TextField? = null

    @FXML
    private var btn_reg_student: Button? = null

    @FXML
    private var reg_gr_name: ComboBox<String>? = null

    @FXML
    private var reg_gr: TextField? = null

    @FXML
    private var btn_reg_gr: Button? = null

    @FXML
    private var btn_back_gr: Button? = null

    @FXML
    private var tab_task: Tab? = null

    @FXML
    private var btn_create_give: Button? = null

    @FXML
    private var table_marks: TableView<Marks>? = null

    @FXML
    private var mk_st_second: TableColumn<Marks, String>? = null

    @FXML
    private var mk_st_first: TableColumn<Marks, String>? = null

    @FXML
    private var mk_st_middle: TableColumn<Marks, String>? = null

    @FXML
    private var mk_st_group: TableColumn<Marks, String>? = null

    @FXML
    private var mk_teacher_second: TableColumn<Marks, String>? = null

    @FXML
    private var mk_teacher_first: TableColumn<Marks, String>? = null

    @FXML
    private var mk_teacher_middle: TableColumn<Marks, String>? = null

    @FXML
    private var mk_subject: TableColumn<Marks, String>? = null

    @FXML
    private var mk_value: TableColumn<Marks, Long>? = null

    @FXML
    private var btn_show_task: Button? = null

    @FXML
    private var mk_name_search: TextField? = null

    @FXML
    private var mk_second_name_search: TextField? = null

    @FXML
    private var mk_middle_name_search: TextField? = null

    @FXML
    private var btn_mk_search: Button? = null

    @FXML
    private var switch_mk_group: ComboBox<*>? = null

    @FXML
    private var mk_from_search: TextField? = null

    @FXML
    private var mk_to_search: TextField? = null

    @FXML
    private var feld_show_task: TextField? = null

    @FXML
    private var btn_back_task: Button? = null

    @FXML
    private var tab_alc1: Tab? = null

    @FXML
    private var btn_teacher_sb_clean: Button? = null

    @FXML
    private var table_teacher: TableView<Teacher>? = null

    @FXML
    private var table_teacher_second: TableColumn<Teacher, String>? = null

    @FXML
    private var table_teacher_first: TableColumn<Teacher, String>? = null

    @FXML
    private var table_teacher_middle: TableColumn<Teacher, String>? = null

    @FXML
    private var table_teacher_subject: TableColumn<Subject, String>? = null

    @FXML
    private var btn_show_alc1: Button? = null

    @FXML
    private var feld_show_alc1: TextField? = null

    @FXML
    private var reg_teacher_first: TextField? = null

    @FXML
    private var reg_teacher_second: TextField? = null

    @FXML
    private var reg_teacher_middle: TextField? = null

    @FXML
    private var btn_reg_teacher: Button? = null

    @FXML
    private var btn_teacher_back: Button? = null

    @FXML
    private var table_teacher_sb: TableView<TeacherSubjects>? = null

    @FXML
    private var table_teacher_sb_name_teacher: TableColumn<TeacherSubjects, String>? = null

    @FXML
    private var table_teacher_sb_group: TableColumn<Subject, String>? = null

    @FXML
    private var table_teacher_sb_subject: TableColumn<Subject, String>? = null

    @FXML
    private var teacher_search_first: TextField? = null

    @FXML
    private var teacher_search_second: TextField? = null

    @FXML
    private var teacher_search_middle: TextField? = null

    @FXML
    private var btn_search_teacher: Button? = null

    @FXML
    private var teacher_switch_subject: ComboBox<*>? = null

    @FXML
    private fun alert(str: String = "Incorrect input", type: Alert.AlertType = Alert.AlertType.ERROR) {
        val alert = Alert(type)
        alert.title = "Attention"
        alert.contentText = str
        alert.showAndWait()
    }

    @FXML
    fun initialize(user: User) {
        val connection = Utils.getNewConnection()
        // client = connection?.let { Utils.getClientByLogin(it, user.login) }
        if (connection != null) {
            initTables(connection)
            initButtons(connection)
            initSpinners(connection)
        }
    }

    private fun initSpinners(connection: Connection) {
        updateGroupSpinner(connection)
    }

    private fun updateGroupSpinner(connection: Connection) {
        val groupList = Utils.getGroupList(connection)?.map { "${it.name}" }?.toList()
        if (groupList != null) {
            reg_gr_name?.items?.addAll(groupList)
        }
    }

    private fun initButtons(connection: Connection) {
        btn_teacher_sb_clean?.setOnAction {
            table_teacher_sb?.items?.clear()
        }

        btn_search_teacher?.setOnAction {
            if (!teacher_search_first?.text.isNullOrBlank() || !teacher_search_second?.text.isNullOrBlank() || !teacher_search_middle?.text.isNullOrBlank()) {
                tableTeacherFiller(
                    Utils.findTeacher(
                        connection,
                        teacher_search_second!!.text,
                        teacher_search_first!!.text,
                        teacher_search_middle!!.text
                    )
                )
            }
        }
        btn_reg_teacher?.setOnAction {
            if (!reg_teacher_first?.text.isNullOrBlank() && !reg_teacher_second?.text.isNullOrBlank() && !reg_teacher_middle?.text.isNullOrBlank()) {
                createTeacher(
                    connection,
                    Teacher(reg_teacher_second?.text, reg_teacher_first?.text, reg_teacher_middle?.text)
                ) {
                    if (it) {
                        tableTeacherFiller(Utils.getTeacherList(connection))
                    }
                }
            }
        }

        btn_reg_gr?.setOnAction {
            if (!reg_gr?.text.isNullOrBlank()) {
                if (Utils.createGroup(connection, reg_gr?.text)) {
                    tableGroupsFiller(Utils.getGroupList(connection))
                    updateGroupSpinner(connection)
                } else {
                    alert("Can't create group")
                }
            }
        }
        btn_reg_student?.setOnAction {
            if (!reg_name?.text.isNullOrBlank() &&
                !reg_second_name?.text.isNullOrBlank() &&
                !reg_middle_name?.text.isNullOrBlank() && reg_gr_name?.value != null
            ) {
                if (Utils.createStudent(
                        connection,
                        reg_name!!.text,
                        reg_second_name!!.text,
                        reg_middle_name!!.text,
                        reg_gr_name!!.value.toString()
                    )
                ) {
                    alert("SUCCESS", Alert.AlertType.CONFIRMATION)
                    tableGroupsFiller(Utils.getGroupList(connection))
                    reg_name!!.text = ""
                    reg_second_name!!.text = ""
                    reg_middle_name!!.text = ""
                    reg_gr_name!!.value = ""
                } else {
                    alert("Can't create student")

                }
            } else {
                alert("Can't create student. Check all fields")
            }
        }

        btn_create_give?.setOnAction {
            println("WELCOME TO MARKS")
        }
        btn_mk_search?.setOnAction {
            if (!mk_name_search?.text.isNullOrBlank() || !mk_second_name_search?.text.isNullOrBlank() ||
                !mk_middle_name_search?.text.isNullOrBlank() || (switch_mk_group?.value.toString() != "null")
                || !mk_from_search?.text.isNullOrBlank() || !mk_to_search?.text.isNullOrBlank()
            ) {
                println("Clicked search")
                tableMarksFiller(
                    Utils.getFilteredFullMarksList(
                        connection,
                        mk_second_name_search!!.text,
                        mk_name_search!!.text,
                        mk_middle_name_search!!.text,
                        if (switch_mk_group?.value != null) {
                            switch_mk_group?.value.toString()
                        } else {
                            ""
                        },
                        if (!mk_from_search!!.text.isNullOrBlank()) {
                            mk_from_search!!.text.toLong()
                        } else {
                            0
                        },
                        if (!mk_to_search!!.text.isNullOrBlank()) {
                            mk_to_search!!.text.toLong()
                        } else {
                            3000
                        }

                    )
                )
            } else {
                print("error")
            }
        }
    }

    private fun createTeacher(connection: Connection, teacher: Teacher, action: (res: Boolean) -> Unit) {
        action(Utils.createTeacher(connection, teacher))
    }


    private fun initTables(connection: Connection) {

        tableTeacher(connection)
        tableTeacherFiller(Utils.getTeacherList(connection))

        tableMarks(connection)
        val mk = Utils.getFilteredFullMarksList(connection)
        tableMarksFiller(mk)

        tableGroups(connection)
        tableGroupsFiller(Utils.getGroupList(connection))


        tablePeopleInGroup(connection)

        table_teacher_sb_name_teacher?.cellValueFactory = PropertyValueFactory("secondName")
        table_teacher_sb_group?.cellValueFactory = PropertyValueFactory("groupName")
        table_teacher_sb_subject?.cellValueFactory = PropertyValueFactory("subject")
    }

    /*Teacher--------------------------------------------------------------------*/

    private fun tableTeacher(connection: Connection) {

        table_teacher_second?.cellValueFactory = PropertyValueFactory("secondName")
        table_teacher_first?.cellValueFactory = PropertyValueFactory("firstName")
        table_teacher_middle?.cellValueFactory = PropertyValueFactory("middleName")
        table_teacher_subject?.cellValueFactory = PropertyValueFactory("subject")
        table_teacher?.columns?.add(addButtonColumn("Action", "show") {

            showTeacherSubjects(connection, it)
        })
        table_teacher?.columns?.add(addButtonColumn("Action", "del") {

            deleteTeacher(connection, it)
        })
    }

    private fun deleteTeacher(connection: Connection, teacher: Teacher) {
        teacher.idTeacher?.let { res ->
            Utils.deleteTeacher(connection, res) {
                if (!it) {
                    alert("Teacher has links")
                }
                tableTeacherFiller(Utils.getTeacherList(connection))
            }
        }
    }

    private fun tableTeacherFiller(data: List<Teacher>?) {
        if (data != null) {
            table_teacher?.items?.clear()
            table_teacher?.items?.addAll(data)
            // println("Teachers sb is ${data}")

        }
    }

    private fun showTeacherSubjects(connection: Connection, id: Teacher) {
        val data = mutableListOf<TeacherSubjects>()
        table_teacher_sb?.items?.clear()
        id.idTeacher?.let { Utils.getTeacherSubjectAndGroups(connection, it)?.let { data.addAll(it) } }
        table_teacher_sb?.items?.addAll(data)

    }
    /*Teacher--------------------------------------------------------------------*/

    /*MARKS LIST--------------------------------------------------------------------*/

    private fun tableMarks(connection: Connection) {

        mk_st_second?.cellValueFactory = PropertyValueFactory("stSecondName")
        mk_st_first?.cellValueFactory = PropertyValueFactory("stFirstName")
        mk_st_middle?.cellValueFactory = PropertyValueFactory("stMiddleName")
        mk_st_group?.cellValueFactory = PropertyValueFactory("group")
        mk_teacher_second?.cellValueFactory = PropertyValueFactory("thSecondName")
        mk_teacher_first?.cellValueFactory = PropertyValueFactory("thFirstName")
        mk_teacher_middle?.cellValueFactory = PropertyValueFactory("thMiddleName")
        mk_subject?.cellValueFactory = PropertyValueFactory("subject")
        mk_value?.cellValueFactory = PropertyValueFactory("value")
        table_marks?.columns?.add(addButtonColumn("Action", "del") {
            deleteMark(connection, it)
        })
    }

    private fun deleteMark(connection: Connection, it: Marks) {
        println("DELETED MARKS ${it.id}")
        //   Utils.deleteMark(connection,it.id)
    }

    private fun tableMarksFiller(data: List<Marks>?) {
        table_marks?.items?.clear()
        if (data != null) {
            table_marks?.items?.addAll(data)
            //println("Marks  is ${data}")
        }
    }
    /*MARKS LIST--------------------------------------------------------------------*/

    /*GROUPS------------------------------------------------------------------------*/
    fun tableGroups(connection: Connection) {

        table_gr_name?.cellValueFactory = PropertyValueFactory("name")
        table_gr_year?.cellValueFactory = PropertyValueFactory("year")
        table_gr_course?.cellValueFactory = PropertyValueFactory("course")
        table_gr_count_people?.cellValueFactory = PropertyValueFactory("peoples")
        table_gr?.columns?.add(addButtonColumn("Action", "show") {
            tablePeopleInGroupFiller(it.idGroup?.let { it1 -> Utils.getStudentsFromGroup(connection, it1) })
        })
        table_gr?.columns?.add(addButtonColumn("Action", "delete") {
            if (it.name?.let { it1 -> Utils.deleteGroup(connection, it1) } == true) {
                tableGroupsFiller(Utils.getGroupList(connection))
                updateGroupSpinner(connection)
            }
        })
    }

    fun tableGroupsFiller(data: List<Group>?) {
        table_gr?.items?.clear()
        if (data != null) {
            table_gr?.items?.addAll(data)
            //println("Groups  is ${data}")
        }
    }

    /*GROUPS----------------------------------------------------------------------*/

    /*PEOPLE IN GROUP---------------------------------------------------------------*/
    fun tablePeopleInGroup(connection: Connection) {
        table_gr_people_second?.cellValueFactory = PropertyValueFactory("secondName")
        table_gr_people_first?.cellValueFactory = PropertyValueFactory("firstName")
        table_gr_people_middle?.cellValueFactory = PropertyValueFactory("middleName")
        table_gr_people?.columns?.add(addButtonColumn("Action", "del") {
            print("deleted student")
        })
    }

    fun tablePeopleInGroupFiller(data: List<Student>?) {
        table_gr_people?.items?.clear()
        if (data != null) {
            table_gr_people?.items?.addAll(data)
            //println("Peoples in group  is ${data}")
        }
    }

    /*PEOPLE IN GROUP---------------------------------------------------------------*/
    fun onTeacherAction(actionEvent: ActionEvent) {

    }

    fun onExitAction(actionEvent: ActionEvent) {

    }

    fun onGroupsAction(actionEvent: ActionEvent) {

    }

    fun onMarksAction(actionEvent: ActionEvent) {

    }

    private fun <T> addButtonColumn(columnName: String, btnName: String, func: (it: T) -> Unit): TableColumn<T, Void> {
        val colBtn: TableColumn<T, Void> = TableColumn(columnName)
        val cellFactory: Callback<TableColumn<T?, Void?>?, TableCell<T?, Void?>?> =
            Callback {
                object : TableCell<T?, Void?>() {
                    private val btn = Button(btnName)
                    override fun updateItem(item: Void?, empty: Boolean) {
                        super.updateItem(item, empty)
                        btn.setOnAction {
                            val type: T? = tableView.items[index]
                            type?.let { it1 -> func(it1) }
                        }
                        graphic = if (empty) {
                            null
                        } else {
                            btn
                        }
                    }
                }
            }

        colBtn.cellFactory = cellFactory
        return colBtn
    }
}