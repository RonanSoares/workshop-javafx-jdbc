package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

	// Declaração da dependência da classe DepartmentService
	private DepartmentService service;

	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	@FXML
	private TableColumn<Department, String> tableColumnName;

	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;

	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;

	@FXML
	private Button btNew;

	// Criada lista para que os objetos do Department sejam inseridos nessa lista.
	private ObservableList<Department> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department();                                  //Instancia um departamento vazio
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
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

		// Para adequar o tamanho da TableView ao tamanho da janela
		Stage stage = (Stage) Main.getMainScene().getWindow();                  //Pega a referencia.
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());  //Macete para o TableView acompanhar o tamanho da janela	
	}

	// Método responsável por acessar os servicos, carregar os departamentos e joga-los no obsList
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll();          // Recupera os departamentos do metodo da classe DepartmentService
		obsList = FXCollections.observableArrayList(list);  // obsList recebe a lista
		tableViewDepartment.setItems(obsList);              // A tableViewDepartment recebe a obsList.
		initEditButtons();                                  //Chama o método para acrescentar um botão em cada linha da tabela
		initRemoveButtons();
	}

	// Função para chamar a janela de Seller para preencher os dados
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			DepartmentFormController controller = loader.getController(); //Pega a referência para o controlador
			controller.setDepartment(obj);                                // Injeta no controlador o departamento
			controller.setDepartmentService(new DepartmentService());     // Injeção de dependência
			controller.subscribeDataChangeListener(this);                 //
			controller.updateFormData();                                   //Carrega os dados do objeto no formulário

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);                     // Janela não pode ser redimensionada
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);     // Enquanto a janela não for fechada, não pode acessar a janela anterior
			dialogStage.showAndWait();                           // Chama a janela
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	//Método especial para criar os botões de edição em cada linha da tabela e já edita-los.
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	//Método para remover departamentos
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Department obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que voçê quer deletar?"); //Alerta para a deleção

		if (result.get() == ButtonType.OK) {  //Se apertar no ok, confirma a deleção
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);    // Remove os dados
				updateTableView();      //Atualizar os dados da tabela
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Erro ao apagar o registro", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

	@Override
	public void onDataChaged() {
		updateTableView();		
	}
}
