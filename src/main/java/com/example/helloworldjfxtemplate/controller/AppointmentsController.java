package com.example.helloworldjfxtemplate.controller;

import com.example.helloworldjfxtemplate.DAO.AppointmentDAO;
import com.example.helloworldjfxtemplate.MainApplication;
import com.example.helloworldjfxtemplate.helper.Alerts;
import com.example.helloworldjfxtemplate.model.Appointment;
import com.example.helloworldjfxtemplate.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * AppointmentsController class initializes TableView for all/week/month appointments
 * Provides a way to visually manupulate an appointment add/modify/delete
 * @author Ervin Iliasov C195
 */
public class AppointmentsController implements Initializable {
    @FXML
    private TableView<Appointment> appointTableView;
    @FXML
    private TableColumn<Appointment, Integer> col_appointCont;
    @FXML
    private TableColumn<Appointment, String> col_appointDesc;
    @FXML
    private TableColumn<Appointment, Timestamp> col_appointEndDate;
    @FXML
    private TableColumn<Appointment, Integer> col_appointID;
    @FXML
    private TableColumn<Appointment, String> col_appointLoc;
    @FXML
    private TableColumn<Appointment, Timestamp> col_appointStartDate;
    @FXML
    private TableColumn<Appointment, String> col_appointTitle;
    @FXML
    private TableColumn<Appointment, String> col_appointType;
    @FXML
    private TableColumn<Appointment, Integer> col_custID;
    @FXML
    private TableColumn<Appointment, Integer> col_userID;
    @FXML
    private Button btn_addAppoint;
    @FXML
    private Button btn_delAppoint;
    @FXML
    private Button btn_updtAppoint;
    @FXML
    private Button btn_menu;
    @FXML
    private RadioButton rb_appoint;
    @FXML
    private RadioButton rb_month;
    @FXML
    private RadioButton rb_week;

    ObservableList<Appointment> appointList = FXCollections.observableArrayList();

    /**
     * initialize() method populates appointTableView
     * and shows appointments depending on selected radiobutton
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (rb_appoint.isSelected()) {
            appointTableView.setItems(AppointmentDAO.getAppointmentList());
            col_appointID.setCellValueFactory(new PropertyValueFactory<>("appointID"));
            col_appointTitle.setCellValueFactory(new PropertyValueFactory<>("appointTitle"));
            col_appointDesc.setCellValueFactory(new PropertyValueFactory<>("appointDesc"));
            col_appointLoc.setCellValueFactory(new PropertyValueFactory<>("appointLoc"));
            col_appointType.setCellValueFactory(new PropertyValueFactory<>("appointType"));
            col_appointCont.setCellValueFactory(new PropertyValueFactory<>("appointContID"));
            col_appointStartDate.setCellValueFactory(new PropertyValueFactory<>("appointStart"));
            col_appointEndDate.setCellValueFactory(new PropertyValueFactory<>("appointEnd"));
            col_custID.setCellValueFactory(new PropertyValueFactory<>("appointCustID"));
            col_userID.setCellValueFactory(new PropertyValueFactory<>("appointUserID"));
        } else if (rb_week.isSelected()) {
            appointTableView.setItems(AppointmentDAO.getWeeklyAppointments());
        } else if (rb_month.isSelected()) {
            appointTableView.setItems(AppointmentDAO.getMonthlyAppointments());
        }
    }

    /**
     * setBtn_addAppoint() method transfers user to appointmentsadd screen
     * @param event
     * @throws IOException
     */
    @FXML
    void setBtn_addAppoint(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(MainApplication.class.getResource("appointmentsadd.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * setBtn_updtAppoint method transfers user to appointmentsmodify screen
     * With help of getAppointInfo() method, method takes selected appointment object from tableview
     * and transfers it to appointmentsmodify screen.
     * If nothing was selected, popup will appear.
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void setBtn_updtAppoint(ActionEvent event) throws IOException, SQLException {
        if (appointTableView.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApplication.class.getResource("appointmentsmodify.fxml"));
            loader.load();
            AppointmentModifyController MCController = loader.getController();
            MCController.getAppointInfo(appointTableView.getSelectionModel().getSelectedItem());
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.centerOnScreen();
            stage.show();
        } else {
            Alerts.getError(6);
        }
    }

    /**
     * setBtn_delAppoint() method removes selected appointment.
     * If there are no selection for appointment, warning popup will appear.
     * Before removal, confirmation popup will appear.
     * @param event
     */
    @FXML
    void setBtn_delAppoint(ActionEvent event) {
        Appointment selectAppoint = appointTableView.getSelectionModel().getSelectedItem();
        if (selectAppoint == null) {
            Alerts.getError(6);
        } else {
            Alert confirmDelete = new Alert(Alert.AlertType.WARNING);
            confirmDelete.setTitle("Alert");
            confirmDelete.setContentText("Do you want to delete selected appointment?");
            confirmDelete.getButtonTypes().clear();
            confirmDelete.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
            confirmDelete.showAndWait();
            if (confirmDelete.getResult() == ButtonType.OK) {
                Alert confirmed = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDelete.setTitle("Alert");
                confirmDelete.setContentText("Appointment ID# " + appointTableView.getSelectionModel().getSelectedItem().getAppointID() +
                        " for " + appointTableView.getSelectionModel().getSelectedItem().getAppointType() + " was canceled.");
                confirmDelete.getButtonTypes().clear();
                confirmDelete.getButtonTypes().addAll(ButtonType.OK);
                confirmDelete.showAndWait();

                AppointmentDAO.delAppoint(appointTableView.getSelectionModel().getSelectedItem().getAppointID());
                appointList = AppointmentDAO.getAppointmentList();
                appointTableView.setItems(appointList);
                appointTableView.refresh();
            } else if (confirmDelete.getResult() == ButtonType.CANCEL) {
                confirmDelete.close();
            }
        }
    }

    /**
     * setRb_appoint() method sets tableview with all appointments.
     * @param event
     */
    @FXML
    void setRb_appoint(ActionEvent event) {
        appointTableView.setItems(AppointmentDAO.getAppointmentList());
    }

    /**
     * setRb_month() method sets tableview with monthly appointments.
     * @param event
     */
    @FXML
    void setRb_month(ActionEvent event) {
        appointTableView.setItems(AppointmentDAO.getMonthlyAppointments());
        appointTableView.setPlaceholder(new Label("No appointments within next month"));
    }

    /**
     * setRb_week() method sets tableview with weekly appointments.
     * @param event
     */
    @FXML
    void setRb_week(ActionEvent event) {
        appointTableView.setItems(AppointmentDAO.getWeeklyAppointments());
        appointTableView.setPlaceholder(new Label("No appointments within next week"));
    }

    /**
     * setBtn_menu() method sends user to "Menu" screen.
     * @param event
     * @throws IOException
     */
    @FXML
    void setBtn_menu(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Leave To The Previous Menu?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            Parent parent = FXMLLoader.load(MainApplication.class.getResource("menu.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }

    }
}
