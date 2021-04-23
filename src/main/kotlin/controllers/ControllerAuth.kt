package controllers

import JDBC.Utils
import JDBC.dao.Role
import JDBC.dao.User
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.net.URL
import java.util.*


class ControllerAuth {

    @FXML
    private var resources: ResourceBundle? = null

    @FXML
    private var location: URL? = null

    @FXML
    private var auth_login: TextField? = null

    @FXML
    private var auth_password: PasswordField? = null

    @FXML
    private var auth_btn_reg: Button? = null

    @FXML
    private var auth_btn_sign: Button? = null

    @FXML
    private fun alert() {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = "Attention"
        alert.contentText = "Incorrect input"
        alert.showAndWait()
    }


    @FXML
    fun onSignIn() {
        //nextScreen(Role.ENGINEER) //todo del
        if (!auth_login?.text.isNullOrEmpty() && !auth_password?.text.isNullOrEmpty()) {
            val connection = Utils.getNewConnection()
            // val user = connection?.let { Utils.getUser(auth_login?.text.toString(), it) }
            println(connection)
/*            if (user != null) {
                if (validateUser(user.password, auth_password!!.text.toString())) {
                    println("Password is Valid")
                    nextScreen(user.role)
                    return
                }
            }
            alert()
        } else {
            alert()
        }*/
            nextScreen(Role.TEACHER)

        }
    }

    @FXML
    fun onRegister() {
        moveToScreen("Registration")
    }

    private fun nextScreen(role: Role) {
        try {
            // auth_btn_sign?.scene?.window?.hide()
            val path = when (role) {
                Role.TEACHER -> "Teacher"
                Role.ADMIN -> "Admin"
            }
            moveToScreen(path, role)
        } catch (ex: Exception) {
            ex.printStackTrace()

        }
    }

    private fun moveToScreen(name: String, role: Role = Role.TEACHER) {
        val loader = FXMLLoader()
        loader.location = javaClass.getResource("../${name}.fxml")
        val root = loader.load<Parent>()
        if (name == "Teacher") {
            val contr: ControllerTeacher = loader.getController()
            contr.initialize(User(auth_login!!.text.toString(), auth_password!!.text.toString(), role))
        }
        auth_btn_sign?.scene?.window?.hide()
        val stage = Stage()
        stage.scene = Scene(root)
        stage.show()
    }
}