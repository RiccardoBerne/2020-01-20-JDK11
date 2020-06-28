package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Artista;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	String ruolo;
    	try {
    		ruolo = this.boxRuolo.getValue();
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Ruolo non nel formato richiesto");
    		return;
    	}
    	for(Adiacenza a: this.model.getConnessi(ruolo)) {
    		this.txtResult.appendText(a.getA1().toString()+"-"+a.getA2().toString()+"-"+a.getPeso().toString()+"\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	Integer id;
    	try {
    		id=Integer.parseInt(this.txtArtista.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Artista non espresso nel formato corretto");
    		return;
    	}
    	if(this.model.getValido(id)==true) {
    		this.model.getPercorso(id);
    	}else {
    		this.txtResult.appendText("Artista non presente");
    	}
    	List<Artista> attori = new ArrayList<>(this.model.getPercorso(id));
    	Integer grado= this.model.getGrado();
    	if(grado!=0) {
    		this.txtResult.appendText("Numero Esposizioni comuni: "+grado+"\n");
    	}else {
    		this.txtResult.appendText("Ricorsione non giunta a buon fine");
    		return;
    	}
    	if(attori.size()==1) {
    		this.txtResult.appendText("Ricorsione senza collegamenti");
    		return;
    	}else {
    		for(Artista a: attori) {
        		this.txtResult.appendText(a.getNome()+"("+a.getId()+")\n");
        	}
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String ruolo;
    	try {
    		ruolo = this.boxRuolo.getValue();
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Ruolo non nel formato richiesto");
    		return;
    	}
    	String msg=this.model.creaGrafo(ruolo);
    	this.txtResult.appendText(msg);
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().addAll(this.model.getRuoli());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
