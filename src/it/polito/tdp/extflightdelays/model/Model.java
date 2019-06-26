package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	Graph<Airport,DefaultWeightedEdge> grafo;
	ExtFlightDelaysDAO dao;
	Map<Integer,Airport> idMapAirport;
	List<Airport> best;
	Double migliaDisponibili;
	Double migliaUsati;
	Double migliaBest;
	
	
	public Model() {
		this.dao = new ExtFlightDelaysDAO();
	}
	
	

	public void creaGrafo(Double distanza) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMapAirport = new HashMap<>();
		
		//load tutti gli aeroporti
		this.dao.loadAllAirports(idMapAirport);
		System.out.println("Tutti aeroporti: "+this.idMapAirport.size());
		
	
		//CARICO ARCHI E VERTICI
		List<Adiacenza> adiacenze = this.dao.listAdiacenze(idMapAirport);
		System.out.println("Adiacenze: "+adiacenze.size());
		
		for(Adiacenza a1: adiacenze) {
			
			boolean trovatoRitorno =false;
			Double peso = a1.getDistance();
			
			for(Adiacenza a2: adiacenze) {
				if(!a1.equals(a2) && a1.getA1().equals(a2.getA2()) && a1.getA2().equals(a2.getA1())) {
					
					trovatoRitorno=true;
					Double peso1 = a1.getDistance();
					Double peso2 = a2.getDistance();
					
					Double tot = (peso1+peso2)/2;
					
					if(tot>=distanza) {
					
					Graphs.addEdgeWithVertices(grafo, a1.getA1(), a1.getA2(), tot);
					
					}
				
				}
			}
			
			if(!trovatoRitorno && peso>distanza) {
				Graphs.addEdgeWithVertices(grafo, a1.getA1(), a1.getA2(), peso);
			}
			
		}
		
		System.out.println("Archi: "+this.grafo.edgeSet().size());
		System.out.println("Vertici: "+this.grafo.vertexSet().size());
	
		
	}
	
	
	
	public Set<Airport> vertexSet(){
		return this.grafo.vertexSet();
	}



	public List<Vicino> getVicini(Airport aeroporto) {
		
		List<Airport> neigh = Graphs.neighborListOf(grafo, aeroporto);
		List<Vicino> vicini = new ArrayList<>();
		for(Airport a : neigh) {
			vicini.add(new Vicino(a, this.grafo.getEdgeWeight(this.grafo.getEdge(aeroporto, a))));
		}
		
		Collections.sort(vicini);
		return vicini;
	}
	
	
	

	public List<Airport> cercaItinerarioDa(Airport aeroporto, Double miglia) {
		
		this.best = new ArrayList<Airport>();
		
		List<Airport> parziale = new ArrayList<>();
		
		//i dati sono gia nel grafo
		this.migliaDisponibili=miglia;
		this.migliaUsati=0.0;
		
		parziale.add(aeroporto);
		
		this.cerca(parziale);
		
		
		return this.best;
		
		
		
	}
	
	public Double migliaUsati() {
		return this.migliaBest;
	}



	private void cerca(List<Airport> parziale) {
		
		
		//se dim lista è maggiore della best e i miglia non finiti = migliore
		if(parziale.size()>this.best.size()) {
			this.best = new ArrayList<>(parziale);
			this.migliaBest=this.migliaUsati;
		}
		
		
		
		
		
		//prendo tutti i vicini del nodo
			//verifico se si possono aggiungere e vado all'altro livello
			//verifico non siano già presenti nella lista per generare loop
		
		List<Airport> vicini = Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1));
		
		for(Airport a : vicini) {
			
			if(!parziale.contains(a) && isValid(parziale,a)) {
				parziale.add(a);
				
				
				Double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(parziale.size()-2), a));
				this.migliaUsati += peso;
				
				this.cerca(parziale);
				parziale.remove(parziale.size()-1);
				this.migliaUsati-=peso;
			}
			
		}

		
	}



	private boolean isValid(List<Airport> parziale, Airport a) {
		
		Airport partenza = parziale.get(parziale.size()-1);
		
		Double distanza = this.grafo.getEdgeWeight(this.grafo.getEdge(partenza, a));
		
		//primo inserimento
		if(parziale.size()==1) {
			
			if(distanza<this.migliaDisponibili) {
				return true;
			}else { 
				return false;
			}
			
			
		}//ce ne sono altri
		else {
			
			if((this.migliaUsati+distanza)<this.migliaDisponibili) {
				return true;
			}else {
				return false;
			}
			
			
		}
	
	}
	
	

	
	
	

}
