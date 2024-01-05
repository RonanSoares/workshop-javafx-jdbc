package gui;


import java.awt.TextField;
import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DepartmentFormController implements Initializable{
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label labelErrorName;	
	@FXML
	private Button btSave;	
	@FXML
	private Button btCancel;
	
	//Método para aplicar os eventos nos botões.
	@FXML
	public void onBTSaveAction() {
		System.out.println("onBTSaveAction");
	}
		
	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//initializeNodes();
	}
	// Método para aplicar restrições nos objetos das janelas
	private void initializeNodes() {
		//Constraints.setTextFieldInteger(txtId);
		//Constraints.setTextFieldMaxLength(txtName, 30);
	}

}
