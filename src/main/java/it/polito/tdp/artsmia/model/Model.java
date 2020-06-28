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

	private List<Artista> bestSoluzione;
	private Integer grado = 0;

	public Model() {
		this.dao = new ArtsmiaDAO();
	}

	public List<String> getRuoli() {
		return this.dao.getRuoli();
	}

	public String creaGrafo(String genere) {
		this.grafo = new SimpleWeightedGraph<Artista, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.dao.getNodi(genere));
		for (Adiacenza a : this.dao.getArchi(genere)) {
			if (this.grafo.containsVertex(a.getA1()) && this.grafo.containsVertex(a.getA2())) {
				Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
		return String.format("Grafo creato: (%s) nodi e (%s) archi", this.grafo.vertexSet().size(),
				this.grafo.edgeSet().size());
	}

	public List<Adiacenza> getConnessi(String genere) {
		List<Adiacenza> vicini = new ArrayList<>(this.dao.getArchi(genere));
		Collections.sort(vicini);
		return vicini;
	}

	public List<Artista> getPercorso(Integer id) {
		this.bestSoluzione = new ArrayList<>();
		this.grado = 0;

		List<Artista> parziale = new ArrayList<>();

		Artista artista = null;
		List<Artista> artisti = new ArrayList<>(this.grafo.vertexSet());
		for (Artista a : artisti) {
			if (a.getId().equals(id)) {
				artista = a;
			}
		}
		parziale.add(artista);
		this.bestSoluzione.add(artista);
		ricorsione(parziale, 0);
		return this.bestSoluzione;

	}

	private void ricorsione(List<Artista> parziale, Integer peso) {
		if (parziale.size() > bestSoluzione.size()) {
			this.bestSoluzione = new ArrayList<>(parziale);
			this.grado=peso;
			return;
		}
		for (Artista a : this.grafo.vertexSet()) {
			if (!parziale.contains(a)) {
				if (this.grafo.containsEdge(parziale.get(parziale.size() - 1), a)) {
					Integer pesoArco = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(parziale.size()-1), a));
					if (parziale.size() == 1 && peso == 0) {
                        parziale.add(a);
                        ricorsione(parziale, pesoArco);
                        parziale.remove(a);
					}
					if(pesoArco==peso) {
						parziale.add(a);
						ricorsione(parziale, pesoArco);
						parziale.remove(a);
					}
				}

			}
		}

	}

	public boolean getValido(Integer id) {
		List<Artista> artisti = new ArrayList<>(this.grafo.vertexSet());
		Boolean valido = false;
		for (Artista a : artisti) {
			if (a.getId().equals(id)) {
				valido = true;
			}
		}
		return valido;
	}

	public Integer getGrado() {
		return grado;
	}

}
