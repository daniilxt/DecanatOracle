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


class MarksController {

    @FXML
    private var btn_create_give: Button? = null

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
            println("WELCOME TO MARKS")
            initButtons()
        }

    }

    private fun initButtons() {
        btn_create_give?.setOnAction {
            println("WELCOME TO MARKS")
        }
    }
}