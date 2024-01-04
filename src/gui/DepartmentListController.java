package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable{
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	@FXML
	private TableColumn<Department, String> tableColumnNome;
	@FXML
	private Button btNovo;
	
	@FXML
	public void onBtNovoAction() {
		System.out.println("onBtNovoAction");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	
	}
	
	// Método auxiliar para inicializar alguns nodes. Colunas Id e nome
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
		// Para adequar o tamanho da TableView ao tamanho da janela.
		Stage stage = (Stage) Main.getMainScene().getWindow(); //Pega a referencia.
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());	//Macete para o TableView acompanhar o tamanho da janela	
		
	}

}
