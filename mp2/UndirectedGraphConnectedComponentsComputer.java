package it.unicam.cs.asdl2122.mp2;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe singoletto che realizza un calcolatore delle componenti connesse di un
 * grafo non orientato utilizzando una struttura dati efficiente (fornita dalla
 * classe {@ForestDisjointSets<GraphNode<L>>}) per gestire insiemi disgiunti di
 * nodi del grafo che sono, alla fine del calcolo, le componenti connesse.
 * 
 * @author Luca Tesei (template) 
 * 		   Julian Marzoli, julian.marzoli@studenti.unicam.it (implementazione)
 *
 * @param <L>
 *                il tipo delle etichette dei nodi del grafo
 */
public class UndirectedGraphConnectedComponentsComputer<L> {

    /*
     * Struttura dati per gli insiemi disgiunti.
     */
    private ForestDisjointSets<GraphNode<L>> f;

    /**
     * Crea un calcolatore di componenti connesse.
     */
    public UndirectedGraphConnectedComponentsComputer() {
        this.f = new ForestDisjointSets<GraphNode<L>>();
    }

    /**
     * Calcola le componenti connesse di un grafo non orientato utilizzando una
     * collezione di insiemi disgiunti.
     * 
     * @param g
     *              un grafo non orientato
     * @return un insieme di componenti connesse, ognuna rappresentata da un
     *         insieme di nodi del grafo
     * @throws NullPointerException
     *                                      se il grafo passato è nullo
     * @throws IllegalArgumentException
     *                                      se il grafo passato è orientato
     */
    public Set<Set<GraphNode<L>>> computeConnectedComponents(Graph<L> g) {
    	if(g == null)
    		throw new NullPointerException(
    				"Tentativo di calcolare componenti connesse di un grafo nullo");
    	if(g.isDirected())
    		throw new NullPointerException(
    				"Tentativo di calcolare componenti connesse di un grafo orientato");
    	// set rappresentante i nodi connessi 
    	Set<Set<GraphNode<L>>> connectedComponents = new HashSet<Set<GraphNode<L>>>();
    	// creo una foresta
    	this.f = new ForestDisjointSets<GraphNode<L>>();
    	// popolo la foresta con un insieme singoletto per ogni nodo nel grafo
    	for(GraphNode<L> node : g.getNodes()) {
    		f.makeSet(node);
    	}
    	// per ogni arco nel grafo 
    	for(GraphEdge<L> edge : g.getEdges()) {
    		// se il rappresentante del nodo 1 è diverso da quello del nodo 2 
    		if(f.findSet(edge.getNode1()) != f.findSet(edge.getNode2())) {
    			// li unisco perchè sono componenti connesse (e ancora non erano stati connessi)
    			f.union(edge.getNode1(), edge.getNode2());
    		}
    	}
    	// per ogni rappresentante dell'insieme disgiunto
    	for(GraphNode<L> repr : f.getCurrentRepresentatives()) {
    		// creo il set rappresentante l'albero 
    		Set<GraphNode<L>> tree = new HashSet<GraphNode<L>>();
    		// recupero gli elementi dell'albero 
    		for(GraphNode<L> node : f.getCurrentElementsOfSetContaining(repr)) {
    			// salvo l'elemento nel set albero
    			tree.add(node);
    		}
    		// aggiungo la componente connessa nell'insieme di componenti connesse
    		connectedComponents.add(tree);
    	}
    	// ritorno l'insieme di componenti connesse 
        return connectedComponents;
    }
}
