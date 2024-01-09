package gui;

import java.net.URL;
import java.util.Date;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	// Declaração da dependência da classe SellerService
	private SellerService service;

	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;

	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;

	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;

	@FXML
	private Button btNew;

	// Criada lista para que os objetos do Seller sejam inseridos nessa lista.
	private ObservableList<Seller> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller();                                  //Instancia um departamento vazio
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
	}

	// Cria o método get do atributo service para injeção de dependência
	public void setSellerService(SellerService service) {
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
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));		
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");                     //Formatar a data
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);                             //Formatar o salario 2 casas dec.
		

		// Para adequar o tamanho da TableView ao tamanho da janela
		Stage stage = (Stage) Main.getMainScene().getWindow();                  //Pega a referencia.
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());  //Macete para o TableView acompanhar o tamanho da janela	
	}

	// Método responsável por acessar os servicos, carregar os departamentos e joga-los no obsList
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Seller> list = service.findAll();          // Recupera os departamentos do metodo da classe SellerService
		obsList = FXCollections.observableArrayList(list);  // obsList recebe a lista
		tableViewSeller.setItems(obsList);              // A tableViewSeller recebe a obsList.
		initEditButtons();                                  //Chama o método para acrescentar um botão em cada linha da tabela
		initRemoveButtons();
	}

	// Função para chamar a janela de Seller para preencher os dados
	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
	/*	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			SellerFormController controller = loader.getController(); //Pega a referência para o controlador
			controller.setSeller(obj);                                // Injeta no controlador o departamento
			controller.setSellerService(new SellerService());     // Injeção de dependência
			controller.subscribeDataChangeListener(this);                 //
			controller.updateFormData();                                   //Carrega os dados do objeto no formulário

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Seller data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);                     // Janela não pode ser redimensionada
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);     // Enquanto a janela não for fechada, não pode acessar a janela anterior
			dialogStage.showAndWait();                           // Chama a janela
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}  */
	}

	//Método especial para criar os botões de edição em cada linha da tabela e já edita-los.
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	//Método para remover departamentos
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
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

	private void removeEntity(Seller obj) {
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
