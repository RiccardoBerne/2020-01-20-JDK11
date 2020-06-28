package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	private ArtsmiaDAO dao;
	private Graph<Artista, DefaultWeightedEdge> grafo;
	public Model() {
		this.dao= new ArtsmiaDAO();
	}
	public List<String> getRuoli(){
		return this.dao.getRuoli();
	}
	public String creaGrafo(String genere) {
		this.grafo= new SimpleWeightedGraph<Artista, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.dao.getNodi(genere));
		for(Adiacenza a: this.dao.getArchi(genere)) {
			if(this.grafo.containsVertex(a.getA1())&& this.grafo.containsVertex(a.getA2())) {
				Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
		return String.format("Grafo creato: (%s) nodi e (%s) archi", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	public List<Adiacenza> getConnessi(String genere){
		List<Adiacenza> vicini = new ArrayList<>(this.dao.getArchi(genere));
		Collections.sort(vicini);
		return vicini;
	}

}
