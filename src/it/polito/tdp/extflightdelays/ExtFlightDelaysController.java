

/**
 * Sample Skeleton for 'ExtFlightDelays.fxml' Controller Class
 */

package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import it.polito.tdp.extflightdelays.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ExtFlightDelaysController {

	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="distanzaMinima"
    private TextField distanzaMinima; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="btnAeroportiConnessi"
    private Button btnAeroportiConnessi; // Value injected by FXMLLoader

    @FXML // fx:id="numeroVoliTxtInput"
    private TextField numeroVoliTxtInput; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaItinerario"
    private Button btnCercaItinerario; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	
    	try {
    		
    		Double distanza = Double.parseDouble(this.distanzaMinima.getText());
    		this.model.creaGrafo(distanza);
    		
    		Set<Airport> aeroporti = this.model.vertexSet();
    		
    		this.cmbBoxAeroportoPartenza.getItems().addAll(aeroporti);
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.clear();
    		this.txtResult.appendText("Attenzione! Devi inserire un numero minimo di distanza");
    	}
    	
    	

    }

    @FXML
    void doCalcolaAeroportiConnessi(ActionEvent event) {
    	
    	Airport aeroporto = this.cmbBoxAeroportoPartenza.getValue();
    	
    	if(aeroporto == null) {
    		this.txtResult.clear();
    		this.txtResult.appendText("Seleziona un aeroporto");
    	}else {
    		this.txtResult.clear();
    		List<Vicino> vicini = this.model.getVicini(aeroporto);
    		this.txtResult.appendText("Vicini dell'aeroporto "+aeroporto+"\n");
    		
    		for(Vicino v : vicini) {
    			this.txtResult.appendText(v.toString()+"\n");
    		}
    	}

    }

    @FXML
    void doCercaItinerario(ActionEvent event) {
    	
    	Airport aeroporto = this.cmbBoxAeroportoPartenza.getValue();
    	
    	if(aeroporto == null) {
    		this.txtResult.clear();
    		this.txtResult.appendText("Seleziona un aeroporto");
    	}else {
    		
    		try {
    			
    			Double miglia = Double.parseDouble(this.numeroVoliTxtInput.getText());
    			List<Airport> raggiunti = this.model.cercaItinerarioDa(aeroporto,miglia);
    			Double migliaUsati = this.model.migliaUsati();
    			
    			this.txtResult.clear();
    			this.txtResult.appendText("Miglia Usati: "+migliaUsati+"\n");
    			this.txtResult.appendText("Aeroporti raggiunti da: "+aeroporto+"\n");
    			
    			for(Airport a : raggiunti) {
    				this.txtResult.appendText(a+"\n");
    			}
    			
    		}catch(NumberFormatException e) {
    			this.txtResult.clear();
        		this.txtResult.appendText("Seleziona i miglia");
    		}
    		
    		
    	}
    	
    	

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert numeroVoliTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnCercaItinerario != null : "fx:id=\"btnCercaItinerario\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }
    
    public void setModel(Model model) {
		this.model = model;
		
	}
}

