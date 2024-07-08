package com.example.helloworldjfxtemplate.controller;

import com.example.helloworldjfxtemplate.model.Country;
import com.example.helloworldjfxtemplate.model.Customer;
import com.example.helloworldjfxtemplate.model.FirstLVLDivision;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CustomerModifyController {
    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_save;

    @FXML
    private ComboBox<Country> cb_custCountry;

    @FXML
    private ComboBox<FirstLVLDivision> cb_custDivision;

    @FXML
    private TextField tf_custAddr;

    @FXML
    private TextField tf_custID;

    @FXML
    private TextField tf_custName;

    @FXML
    private TextField tf_custPhone;

    @FXML
    private TextField tf_custPost;

    @FXML
    void setBtn_cancel(ActionEvent event) {

    }

    @FXML
    void setBtn_save(ActionEvent event) {

    }

    @FXML
    void tf_cust(ActionEvent event) {

    }

    public void getCustInfo(Customer selectedCust) {
        tf_custID.setText(Integer.toString(selectedCust.getCustID()));
        tf_custName.setText(selectedCust.getCustName());
        tf_custPhone.setText(selectedCust.getCustPhone());
        tf_custAddr.setText(selectedCust.getCustAddr());
        tf_custPost.setText(selectedCust.getCustPost());


       tf_custPhone
       tf_custPost
    }
}
