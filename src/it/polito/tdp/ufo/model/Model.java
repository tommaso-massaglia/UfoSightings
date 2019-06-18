package it.polito.tdp.ufo.model;

import java.time.Year;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {

	private Map<String, State> states;
	private Map<Integer, Sighting> sightingIdMap;
	private Graph<State, DefaultEdge> grafo;

	public Model() {
		SightingsDAO dao = new SightingsDAO();
		this.states = new HashMap<>();
		for (State s : dao.getStatesUS()) {
			this.states.put(s.getState_code(), s);
		}
		this.sightingIdMap = new HashMap<>();
		for (Sighting s : dao.getSightingsUS()) {
			this.sightingIdMap.put(s.getId(), s);
		}
	}

	public Collection<Sighting> getYearSightings(int year) {
		List<Sighting> result = new LinkedList<Sighting>();

		for (Sighting s : this.sightingIdMap.values()) {
			if (s.getDatetime().getYear() == year) {
				this.states.get(s.getState()).getSightings().add(s);
				result.add(s);
			}
		}

		return result;
	}

	public int getNumberSightingsYear(int year) {
		List<Sighting> result = new LinkedList<Sighting>();

		for (Sighting s : this.sightingIdMap.values()) {
			if (s.getDatetime().getYear() == year) {
				result.add(s);
			}
		}

		return result.size();
	}

	public void creaGrafo(int year) {
		this.getYearSightings(year);
		this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
		Graphs.addAllVertices(this.grafo, this.states.values());
		SightingsDAO dao = new SightingsDAO();

		for (State s1 : grafo.vertexSet()) {
			for (State s2 : grafo.vertexSet()) {
				if (!s1.equals(s2)) {
					if (!grafo.containsEdge(s1, s2)
							// && dao.esisteArco(s1.getState_code(), s2.getState_code(), year))
							&& s1.getUltimoAvvistamentoAnno(year).compareTo(s2.getUltimoAvvistamentoAnno(year)) < 0) {
						grafo.addEdge(s1, s2);
					}
				}
			}
		}
	}

	public Collection<State> getBeforeAfter(String state_code) {
		Set<State> beforeafter = new HashSet<State>();

		for (DefaultEdge de : grafo.incomingEdgesOf(this.states.get(state_code))) {
			State s = grafo.getEdgeSource(de);
			beforeafter.add(s);
		}
		for (DefaultEdge de : grafo.outgoingEdgesOf(this.states.get(state_code))) {
			State s = grafo.getEdgeTarget(de);
			beforeafter.add(s);
		}

		return beforeafter;
	}

	public Collection<State> getReachable(String state_code) {
		ConnectivityInspector<State, DefaultEdge> ci = new ConnectivityInspector<>(grafo);
		return ci.connectedSetOf(this.states.get(state_code));
	}

	private List<State> longest_route;

	public String longestPath(String state_code) {
		this.longest_route = new LinkedList<State>();
		Set<State> parziale = new LinkedHashSet<State>();
		parziale.add(this.states.get(state_code));
		this.recursive(parziale, this.states.get(state_code));
		String result = "Lista percorso più lungo: \n";
		int n = 1;
		for (State s : this.longest_route) {
			result += n + ": " + s + "\n";
			n++;
		}
		return result;
	}

	private void recursive(Set<State> parziale, State last_visited) {

		if (parziale.size() > this.longest_route.size()) {
			this.longest_route = new LinkedList<State>(parziale);
		}

		for (DefaultEdge de : grafo.outgoingEdgesOf(last_visited)) {
			State s = grafo.getEdgeTarget(de);
			if (!parziale.contains(s)) {
				parziale.add(s);
				this.recursive(parziale, s);
				parziale.remove(s);
			}
		}

	}

}
