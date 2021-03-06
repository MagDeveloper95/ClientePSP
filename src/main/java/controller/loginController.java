package controller;

import java.io.IOException;
import java.io.OutputStream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Administrador;
import model.ClientManager;
import model.Paquete;
import model.Usuario;
import utils.AdministradorSingleton;
import utils.UsuarioSingleton;

public class loginController {

	@FXML
	private Button buttAdmin;

	@FXML
	private Button buttCliente;

	@FXML
	private TextField txtContrasena;

	@FXML
	private TextField txtUsuario;

	public OutputStream outputStream;

	@FXML
	void logInAdmin(ActionEvent event) {
		ClientManager cm = new ClientManager("localhost", 9999);
		String contrasena = this.txtContrasena.getText();
		String usuario = this.txtUsuario.getText();

		if (contrasena.isEmpty() & usuario.isEmpty()) {
			utils.Dialog.showError("Error", "Debe ingresar nombre de usuario y contrasena",
					"Debe ingresar usuario y contrasena que se encuentran en la base de datos");
		} else {
			Paquete<Administrador> escribir = new Paquete<>(); 
			escribir.setOpcion(12);
			cm.sendObjectToServer(escribir);
			Object leer = cm.getObjectFromServer();
			Paquete<Administrador> a = (Paquete<Administrador>) leer;
			if (a.getResultado()) {
				AdministradorSingleton administradorSignleton = AdministradorSingleton.getInstance();
				administradorSignleton.setAdmin((Administrador) a.getObjeto());
				try {
					App.setRoot("adminHome");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				utils.Dialog.showError("Error", "No se encontro el usuario",
						"No se encontro el usuario, por favor intente de nuevo con un usuario valido");
			}
		}
	}

	@FXML
	void logInCliente(ActionEvent event) {
		ClientManager cm = new ClientManager("localhost", 9999);
		String contrasena = this.txtContrasena.getText();
		String usuario = this.txtUsuario.getText();

		if (contrasena.isEmpty() & usuario.isEmpty()) {
			utils.Dialog.showError("Error", "Debe ingresar nombre de usuario y contrasena",
					"Debe ingresar usuario y contrasena que se encuentran en la base de datos");
		} else {
			// Creamos un paquete y lo adecuamos para el login y lo enviamos
			Paquete<Usuario> escribir = new Paquete<>();
			escribir.setOpcion(11);
			escribir.setObjeto(new Usuario(usuario, contrasena));
			cm.sendObjectToServer(escribir);
			// Leemos la respuesta del servidor
			Object leer = cm.getObjectFromServer();
			Paquete<Usuario> a = (Paquete<Usuario>) leer;
			//Si el usuario devuelto es correcto, lo seteamos al singleton.
			if (a.getResultado()) {
				UsuarioSingleton usuarioSingleton = UsuarioSingleton.getInstance();
				usuarioSingleton.setUser(a.getObjeto());
				try {
					App.setRoot("IngresarSacarDinero");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				utils.Dialog.showError("Error", "No se encontro el usuario",
						"No se encontro el usuario, por favor intente de nuevo con un usuario valido");
			}
		}
	}

}
