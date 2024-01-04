package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	private TableColumn<Department, String> tableColumnNome;
	@FXML
	private Button btNovo;
	
	// Criada lista para que os objetos do Department sejam inseridos nessa lista.
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNovoAction() {
		System.out.println("onBtNovoAction");
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
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
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
}
