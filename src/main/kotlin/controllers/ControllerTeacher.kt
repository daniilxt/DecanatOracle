package controllers

import JDBC.Type
import JDBC.Utils
import JDBC.dao.User
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.util.Callback
import jdk.jshell.execution.Util
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
    private var btn_clear_mk: Button? = null

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
    private var btn_back_mk: Button? = null

    @FXML
    private var mk_name_search: TextField? = null

    @FXML
    private var mk_second_name_search: TextField? = null

    @FXML
    private var mk_middle_name_search: TextField? = null

    @FXML
    private var btn_mk_search: Button? = null

    @FXML
    private var switch_mk_group: ComboBox<String>? = null

    @FXML
    private var switch_mk_subject: ComboBox<String>? = null

    @FXML
    private var mk_from_search: TextField? = null

    @FXML
    private var mk_to_search: TextField? = null

    @FXML
    private var mk_name_edit: TextField? = null

    @FXML
    private var mk_second_name_edit: TextField? = null

    @FXML
    private var mk_middle_name_edit: TextField? = null

    @FXML
    private var mk_value_edit: TextField? = null

    @FXML
    private var switch_mk_edit_teacher: ComboBox<String>? = null

    @FXML
    private var switch_mk_edit_subject: ComboBox<String>? = null

    @FXML
    private var btn_mk_edit_add: Button? = null

    @FXML
    private var btn_mk_edit_clear: Button? = null

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
    private var btn_teacher_refresh: Button? = null

    @FXML
    private var teacher_switch_subject: ComboBox<String>? = null

    @FXML
    private var switch_param_subject: ComboBox<String>? = null

    @FXML
    private var switch_param_group: ComboBox<String>? = null

    @FXML
    private var switch_param_teacher: ComboBox<String>? = null

    @FXML
    private var btn_param_filter: Button? = null

    @FXML
    private var btn_subjects_clean: Button? = null

    @FXML
    private var subject_param_from: TextField? = null

    @FXML
    private var subject_param_to: TextField? = null

    @FXML
    private var table_stat: TableView<Statistics>? = null

    @FXML
    private var table_stat_year: TableColumn<Statistics, String>? = null

    @FXML
    private var table_stat_subject: TableColumn<Statistics, String>? = null

    @FXML
    private var table_stat_avg: TableColumn<Statistics, String>? = null

    @FXML
    private var table_stat_last_name: TableColumn<Statistics, String>? = null

    @FXML
    private var table_stat_group: TableColumn<Statistics, String>? = null

    @FXML
    private var table_subjects: TableView<Subject>? = null

    @FXML
    private var table_subjects_name: TableColumn<Subject, String>? = null

    @FXML
    private var btn_reg_subject: Button? = null

    @FXML
    private var reg_subject_name: TextField? = null

    @FXML
    private fun alert(str: String = "Incorrect input", type: Alert.AlertType = Alert.AlertType.ERROR) {
        val alert = Alert(type)
        alert.title = "Attention"
        alert.contentText = str
        alert.showAndWait()
    }

    private var editedMarkId: Long? = null

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
        updateSubjectsInTeacherSpinner(connection)

        updateTeacherSpinnerInMarksEdit(connection)
        updateSubjectSpinnerInMarksEdit(connection)
        updateSubjectSpinnerInMarksFind(connection)
        updateGroupSpinnerInMarksFind(connection)

        updateGroupSpinnerInStatistics(connection)
        updateTeacherSpinnerInStatistics(connection)
        updateSubjectSpinnerInStatistics(connection)

    }

    private fun updateGroupSpinnerInStatistics(connection: Connection) {
        val groupList = Utils.getGroupList(connection)?.map { "${it.name}" }?.toMutableList()?.apply { add(0, "") }
        if (groupList != null) {
            switch_param_group?.items?.addAll(groupList)
        }
    }

    private fun updateTeacherSpinnerInStatistics(connection: Connection) {
        val teacherList =
            Utils.getTeacherList(connection)?.map { "${it.secondName} ${it.firstName} ${it.middleName}" }
                ?.toMutableList()?.apply { add(0, "") }
        if (teacherList != null) {
            switch_param_teacher?.items?.addAll(teacherList)
        }
    }

    private fun updateSubjectSpinnerInStatistics(connection: Connection) {
        val groupList = Utils.getSubjectList(connection)?.map { "${it.name}" }?.toMutableList()?.apply { add(0, "") }
        if (groupList != null) {
            switch_param_subject?.items?.addAll(groupList)
        }
    }

    private fun updateTeacherSpinnerInMarksEdit(connection: Connection) {
        val teacherList =
            Utils.getTeacherList(connection)?.map { "${it.secondName} ${it.firstName} ${it.middleName}" }?.toList()
        if (teacherList != null) {
            switch_mk_edit_teacher?.items?.addAll(teacherList)
        }
    }

    private fun updateSubjectSpinnerInMarksEdit(connection: Connection) {
        val groupList = Utils.getSubjectList(connection)?.map { "${it.name}" }?.toList()
        if (groupList != null) {
            switch_mk_edit_subject?.items?.addAll(groupList)
        }
    }

    private fun updateSubjectSpinnerInMarksFind(connection: Connection) {
        val subjectList = Utils.getSubjectList(connection)?.map { "${it.name}" }?.toMutableList()?.apply { add(0, "") }
        if (subjectList != null) {
            switch_mk_subject?.items?.addAll(subjectList)
        }
    }

    private fun updateGroupSpinnerInMarksFind(connection: Connection) {
        val subjectList = Utils.getGroupList(connection)?.map { "${it.name}" }?.toMutableList()?.apply { add(0, "") }
        if (subjectList != null) {
            switch_mk_group?.items?.addAll(subjectList)
        }
    }

    private fun updateSubjectsInTeacherSpinner(connection: Connection) {
        val groupList = Utils.getSubjectList(connection)?.map { "${it.name}" }?.toMutableList()?.apply { add(0, "") }
        if (groupList != null) {
            teacher_switch_subject?.items?.addAll(groupList)
        }
    }

    private fun updateGroupSpinner(connection: Connection) {
        val groupList = Utils.getGroupList(connection)?.map { "${it.name}" }?.toList()
        if (groupList != null) {
            reg_gr_name?.items?.addAll(groupList)
        }
    }

    private fun initButtons(connection: Connection) {
        moveButtons(connection)
        btn_teacher_sb_clean?.setOnAction {
            table_teacher_sb?.items?.clear()
        }
        btn_teacher_refresh?.setOnAction {
            tableTeacherFiller(Utils.getTeacherList(connection))
        }

        btn_search_teacher?.setOnAction {
            if (!teacher_search_first?.text.isNullOrBlank() || !teacher_search_second?.text.isNullOrBlank() || !teacher_search_middle?.text.isNullOrBlank() ||
                teacher_switch_subject?.value != null
            ) {
                tableTeacherFiller(
                    Utils.findTeacher(
                        connection,
                        teacher_search_second!!.text,
                        teacher_search_first!!.text,
                        teacher_search_middle!!.text,
                        if (teacher_switch_subject?.value != null) {
                            teacher_switch_subject?.value.toString()
                        } else {
                            ""
                        }
                    )
                )
            } else {
                alert("Empty input")
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
                        alert("SUCCESS", Alert.AlertType.CONFIRMATION)
                        reg_teacher_first?.text = ""
                        reg_teacher_second?.text = ""
                        reg_teacher_middle?.text = ""
                    } else {
                        alert("Can't create teacher")
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

        btn_clear_mk?.setOnAction {
            tableMarksFiller(Utils.getFilteredFullMarksList(connection))
        }
        btn_mk_search?.setOnAction {
            if (!mk_name_search?.text.isNullOrBlank() || !mk_second_name_search?.text.isNullOrBlank() ||
                !mk_middle_name_search?.text.isNullOrBlank() || (switch_mk_group?.value.toString() != "null")
                || (switch_mk_subject?.value.toString() != "null") || !mk_from_search?.text.isNullOrBlank()
                || !mk_to_search?.text.isNullOrBlank()
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
                        if (switch_mk_subject?.value != null) {
                            switch_mk_subject?.value.toString()
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
        btn_mk_edit_add?.setOnAction {
            if (
                !mk_name_edit?.text.isNullOrBlank() &&
                !mk_second_name_edit?.text.isNullOrBlank() &&
                !mk_middle_name_edit?.text.isNullOrBlank() &&
                !mk_value_edit?.text.isNullOrBlank() &&
                switch_mk_edit_teacher?.value != null &&
                switch_mk_edit_subject?.value != null
            ) {
                if (editedMarkId != null) {
                    try {
                        if (mk_value_edit?.text!!.toLong() in 2..5) {
                            Utils.editMark(connection, editedMarkId!!, mk_value_edit!!.text.toLong())
                            editedMarkId = null
                            changeFieldStatusInEditMarks(Type.ENABLED)
                            alert("SUCCESS", Alert.AlertType.CONFIRMATION)
                            tableMarksFiller(Utils.getFilteredFullMarksList(connection))
                        } else {
                            alert("Incorrect mark value")
                        }
                    } catch (e: Exception) {
                        alert("Input number between 2 and 5")
                    }
                } else {
                    val (teacherSecond, teacherFirst, teacherMiddle) = switch_mk_edit_teacher?.value?.split(" ")!!
                        .toList()
                    if (Utils.createMark(
                            connection, Marks(
                                null,
                                mk_second_name_edit!!.text,
                                mk_name_edit!!.text,
                                mk_middle_name_edit!!.text,
                                null,
                                teacherSecond,
                                teacherFirst,
                                teacherMiddle,
                                switch_mk_edit_subject?.value,
                                value = mk_value_edit?.text?.toLong()
                            )
                        )
                    ) {
                        println("OK ${editedMarkId}")
                        changeFieldStatusInEditMarks(Type.ENABLED)
                        alert("SUCCESS", Alert.AlertType.CONFIRMATION)
                        tableMarksFiller(Utils.getFilteredFullMarksList(connection))
                    } else {
                        println("ERROR ${editedMarkId}")
                        alert("ERROR")

                    }
                }
            } else {
                alert("Fill all fields")
            }
        }
        btn_mk_edit_clear?.setOnAction {
            changeFieldStatusInEditMarks(Type.ENABLED)
            editedMarkId = null

        }

        btn_param_filter?.setOnAction {
            if (!subject_param_from?.text.isNullOrBlank() || !subject_param_to?.text.isNullOrBlank() ||
                switch_param_group?.value != null || switch_param_subject?.value != null || switch_param_teacher?.value != null
            ) {
                tableStatisticFiller(
                    Utils.getFilteredStatistics(
                        connection,
                        if (switch_param_group?.value != null) {
                            switch_param_group?.value.toString()
                        } else {
                            ""
                        },
                        if (switch_param_subject?.value != null) {
                            switch_param_subject?.value.toString()
                        } else {
                            ""
                        },
                        if (switch_param_teacher?.value != null) {
                            switch_param_teacher?.value.toString()
                        } else {
                            ""
                        },
                        if (!subject_param_from?.text.isNullOrBlank()) {
                            subject_param_from!!.text
                        } else {
                            "0"
                        },
                        if (!subject_param_to?.text.isNullOrBlank()) {
                            subject_param_to!!.text
                        } else {
                            "3000"
                        }
                    )
                )
            }
        }
        btn_subjects_clean?.setOnAction {
            tableStatisticFiller(Utils.getStatisticsList(connection))
        }

        btn_reg_subject?.setOnAction {
            if (!reg_subject_name?.text.isNullOrBlank()) {
                if (Utils.createSubject(connection, reg_subject_name!!.text)) {
                    alert("SUCCESS", Alert.AlertType.CONFIRMATION)
                    tableSubjectsFiller(Utils.getSubjectList(connection))
                } else {
                    alert("ERROR")
                }
            } else {
                alert("Fill all fields")
            }
        }
    }

    private fun moveButtons(connection: Connection) {
        btn_back_mk?.setOnAction {
            println("BACK FROM MK")
        }

        btn_teacher_back?.setOnAction {
            println("PRESSED BACK")
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

        tableStatistic(connection)
        tableStatisticFiller(Utils.getStatisticsList(connection))

        tableSubjects(connection)
        tableSubjectsFiller(Utils.getSubjectList(connection))

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
                } else {
                    alert("SUCCESS", Alert.AlertType.CONFIRMATION)
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
        table_marks?.columns?.add(addButtonColumn("Action", "edit") {
            editMark(connection, it)
        })
    }

    private fun editMark(connection: Connection, it: Marks) {
        editedMarkId = it.id
        mk_name_edit?.text = it.stFirstName
        mk_second_name_edit?.text = it.stSecondName
        mk_middle_name_edit?.text = it.stMiddleName
        mk_value_edit?.text = it.value.toString()
        switch_mk_edit_teacher?.value = "${it.thSecondName} ${it.thFirstName} ${it.thMiddleName}"
        switch_mk_edit_subject?.value = it.subject
        changeFieldStatusInEditMarks(Type.DISABLED)

    }

    private fun changeFieldStatusInEditMarks(status: Type) {
        when (status) {
            Type.DISABLED -> {
                mk_name_edit?.isEditable = false
                mk_second_name_edit?.isEditable = false
                mk_middle_name_edit?.isEditable = false
                switch_mk_edit_teacher?.isDisable = true
                switch_mk_edit_subject?.isDisable = true
            }
            Type.ENABLED -> {
                mk_name_edit?.isEditable = true
                mk_second_name_edit?.isEditable = true
                mk_middle_name_edit?.isEditable = true
                switch_mk_edit_teacher?.isDisable = false
                switch_mk_edit_subject?.isDisable = false

                mk_name_edit?.text = ""
                mk_second_name_edit?.text = ""
                mk_middle_name_edit?.text = ""
                mk_value_edit?.text = ""
                switch_mk_edit_teacher?.value = ""
                switch_mk_edit_subject?.value = ""
            }
        }
    }

    private fun deleteMark(connection: Connection, it: Marks) {
        println("DELETED MARKS ${it.id}")
        if ((Utils.deleteMark(connection, it.id))) {
            table_marks?.items?.remove(it)
            alert("SUCCESS", Alert.AlertType.CONFIRMATION)
        } else {
            alert("ERROR")
        }
    }

    private fun tableMarksFiller(data: List<Marks>?) {
        table_marks?.items?.clear()
        if (data != null) {
            table_marks?.items?.addAll(data)
            //println("Marks  is ${data}")
        }
    }

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

            if (Utils.deleteStudent(connection, it.idStudent)) {
                table_gr_people?.items?.remove(it)
                print("deleted student ${it.idStudent}")
                tableGroupsFiller(Utils.getGroupList(connection))
            }
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

    /*MARKS LIST--------------------------------------------------------------------*/


    /*STAT LIST--------------------------------------------------------------------*/
    private fun tableStatistic(connection: Connection) {
        table_stat_year?.cellValueFactory = PropertyValueFactory("year")
        table_stat_subject?.cellValueFactory = PropertyValueFactory("subject")
        table_stat_avg?.cellValueFactory = PropertyValueFactory("avg")
        table_stat_last_name?.cellValueFactory = PropertyValueFactory("lastName")
        table_stat_group?.cellValueFactory = PropertyValueFactory("group")
    }

    private fun tableStatisticFiller(data: List<Statistics>?) {
        table_stat?.items?.clear()
        if (data != null) {
            table_stat?.items?.addAll(data)
        }
    }
    /*STAT LIST--------------------------------------------------------------------*/
    /*Subjects LIST--------------------------------------------------------------------*/


    private fun tableSubjects(connection: Connection) {
        table_subjects_name?.cellValueFactory = PropertyValueFactory("name")
        table_subjects?.columns?.add(addButtonColumn("Action", "delete") {
            println("Deleted sb  ${it.idSubject}")
        })
    }

    private fun tableSubjectsFiller(data: List<Subject>?) {
        table_subjects?.items?.clear()
        if (data != null) {
            table_subjects?.items?.addAll(data)

        }
    }
    /*subjects LIST--------------------------------------------------------------------*/

    fun onGroupsAction(actionEvent: ActionEvent) {

    }

    fun onMarksAction(actionEvent: ActionEvent) {

    }

    fun onTeacherAction(actionEvent: ActionEvent) {

    }

    fun onExitAction(actionEvent: ActionEvent) {

    }

    private fun <T> addButtonColumn(
        columnName: String,
        btnName: String,
        func: (it: T) -> Unit
    ): TableColumn<T, Void> {
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