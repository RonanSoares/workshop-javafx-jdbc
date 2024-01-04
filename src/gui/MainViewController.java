package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemVendedor;
	@FXML
	private MenuItem menuItemDepartamento;
	@FXML
	private MenuItem menuItemAbout;
	
	// Método para dar ação aos clicks no Vendedor
	@FXML
	public void onMenuItemVendedorAction() {
		System.out.println("onMenuItemVendedorAction");
		
	}
	
	// Método para dar ação aos clicks no Departamento
		@FXML
		public void onMenuItemDepartamentoAction() {
			loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
			});			
		}
		
		// Método para dar ação aos clicks no About
		@FXML
		public void onMenuItemAboutAction() {
			loadView("/gui/About.fxml", x -> {});
			
		}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {		
		
	}
	
	//Função para abrir a tela About. synchronized, para garantir que os cmdos não serão interrompidos
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
		VBox newVBox = loader.load();
		
		// Carrega a View dentro da janela principal
		Scene mainScene = Main.getMainScene();  // Recebe o método get da Main.
		VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();  // Pega o primeiro elemento da view "ScroolPane"
		
		// Guarda uma referencia para o menu. Recebe o primeiro filho do VBox que é o mainMenu
		Node mainMenu = mainVBox.getChildren().get(0);
		mainVBox.getChildren().clear();                        // Limpa todos os filhos do mainBox
		mainVBox.getChildren().add(mainMenu);                  // Adiciona o menu
		mainVBox.getChildren().addAll(newVBox.getChildren());  //Adiciona a coleção  
		
		T controller = loader.getController();
		initializingAction.accept(controller);
		
		}
		catch(IOException e) {
			Alerts.showAlert("IO Excessão", "Erro ao carregar a página", e.getMessage(), AlertType.ERROR);
		}		
	}	
	
}
