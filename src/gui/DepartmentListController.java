package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
	
	// Declaração da dependência da classe DepartmentService
	private DepartmentService service;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	@FXML
	private TableColumn<Department, String> tableColumnName;
	@FXML
	private Button btNew;
	
	// Criada lista para que os objetos do Department sejam inseridos nessa lista.
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		createDialogForm("/gui/DepartmentForm.fxml", parentStage);
	}
	
	// Cria o método get do atributo service para injeção de dependência
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	
	}
	
	// Método auxiliar para inicializar alguns nodes. Colunas Id e nome
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		// Para adequar o tamanho da TableView ao tamanho da janela.
		Stage stage = (Stage) Main.getMainScene().getWindow(); //Pega a referencia.
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());	//Macete para o TableView acompanhar o tamanho da janela	
	}
	
	// Método responsável por acessar os servicos, carregar os departamentos e joga-los no obsList
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("O serviço está nulo!!");
		}
		 List<Department> list = service.findAll();         // Recupera os departamentos do metodo da classe DepartmentService
		 obsList = FXCollections.observableArrayList(list); // obsList recebe a lista
		 tableViewDepartment.setItems(obsList);            // A tableViewDepartment recebe a obsList.
	}
	
	// Função para chamar a janela de Seller para preencher os dados
	private void createDialogForm(String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados do departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);                  // Janela não pode ser redimensionada
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);  // Enquanto a janela não for fechada, não pode acessar a janela anterior
			dialogStage.showAndWait();                        // Chama a janela
		}
		catch(IOException e) {
			Alerts.showAlert("IO Excerption", "Erro ao carregar a página", e.getMessage(), AlertType.ERROR);
		}
	}
}
