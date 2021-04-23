package controllers

import JDBC.Utils
import JDBC.dao.User
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.util.Callback
import poko.Subject
import poko.Teacher
import poko.TeacherSubjects
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
    private var table_gr_people: TableView<*>? = null

    @FXML
    private var table_gr_people_first: TableColumn<*, *>? = null

    @FXML
    private var table_gr_people_second: TableColumn<*, *>? = null

    @FXML
    private var table_gr_people_middle: TableColumn<*, *>? = null

    @FXML
    private var table_gr: TableView<*>? = null

    @FXML
    private var table_gr_name: TableColumn<*, *>? = null

    @FXML
    private var table_gr_year: TableColumn<*, *>? = null

    @FXML
    private var table_gr_course: TableColumn<*, *>? = null

    @FXML
    private var table_gr_count_people: TableColumn<*, *>? = null

    @FXML
    private var btn_show_alc: Button? = null

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
    private var reg_gr_name: ComboBox<*>? = null

    @FXML
    private var reg_gr: TextField? = null

    @FXML
    private var btn_reg_gr: Button? = null

    @FXML
    private var btn_back_alc: Button? = null

    @FXML
    private var tab_task: Tab? = null

    @FXML
    private var btn_create_give: Button? = null

    @FXML
    private var table_lc_list1: TableView<*>? = null

    @FXML
    private var mk_st_second: TableColumn<*, *>? = null

    @FXML
    private var mk_st_first: TableColumn<*, *>? = null

    @FXML
    private var mk_st_middle: TableColumn<*, *>? = null

    @FXML
    private var mk_st_group: TableColumn<*, *>? = null

    @FXML
    private var mk_teacher_second: TableColumn<*, *>? = null

    @FXML
    private var mk_teacher_first: TableColumn<*, *>? = null

    @FXML
    private var mk_teacher_middle: TableColumn<*, *>? = null

    @FXML
    private var mk_subject: TableColumn<*, *>? = null

    @FXML
    private var mk_value: TableColumn<*, *>? = null

    @FXML
    private var btn_show_task: Button? = null

    @FXML
    private var reg_name11: TextField? = null

    @FXML
    private var reg_second_name11: TextField? = null

    @FXML
    private var reg_middle_name11: TextField? = null

    @FXML
    private var btn_mk_search: Button? = null

    @FXML
    private var switch_mk_group: ComboBox<*>? = null

    @FXML
    private var reg_middle_name111: TextField? = null

    @FXML
    private var reg_middle_name1111: TextField? = null

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
        }
    }


    private fun initTables(connection: Connection) {

        var data = mutableListOf<Teacher>()

        table_teacher?.items?.clear()
        Utils.getTeacherList(connection)?.let { data.addAll(it) }
        table_teacher?.items?.addAll(data)

        // created teacher table
        table_teacher_second?.cellValueFactory = PropertyValueFactory("secondName")
        table_teacher_first?.cellValueFactory = PropertyValueFactory("firstName")
        table_teacher_middle?.cellValueFactory = PropertyValueFactory("middleName")
        table_teacher_subject?.cellValueFactory = PropertyValueFactory("subject")
        table_teacher?.columns?.add(addButtonColumn("Action", "handle") {

            showTeacherSubjects(connection, it)
        })

        table_teacher_sb_name_teacher?.cellValueFactory = PropertyValueFactory("secondName")
        table_teacher_sb_group?.cellValueFactory = PropertyValueFactory("groupName")
        table_teacher_sb_subject?.cellValueFactory = PropertyValueFactory("subject")
    }

    private fun showTeacherSubjects(connection: Connection, id: Teacher) {
        val data = mutableListOf<TeacherSubjects>()
        table_teacher_sb?.items?.clear()
        id.idTeacher?.let { Utils.getTeacherSubjectAndGroups(connection, it)?.let { data.addAll(it) } }
        table_teacher_sb?.items?.addAll(data)
        println("Teachers sb is ${data}")

    }

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