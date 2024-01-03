package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
			System.out.println("onMenuItemDepartamentoAction");
			
		}
		
		// Método para dar ação aos clicks no About
		@FXML
		public void onMenuItemAboutAction() {
			System.out.println("onMenuItemAboutAction");
			
		}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {		
		
	}
}
