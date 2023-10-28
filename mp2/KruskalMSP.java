package it.unicam.cs.asdl2122.mp2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 * 
 * @author Luca Tesei (template) 
 * 		   Julian Marzoli, julian.marzoli@studenti.unicam.it (implementazione)
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class KruskalMSP<L> {

    /*
     * Struttura dati per rappresentare gli insiemi disgiunti utilizzata
     * dall'algoritmo di Kruskal.
     */
    private ForestDisjointSets<GraphNode<L>> disjointSets;
    
    /*
     * Classe privata che fornisce una implementazione dell'algortimo di 
     * ordinamento "QuickSort", che ha come scopo quello di ordinare
     * in modo non-decrescente una lista di {@code GraphEdge<L>}.
     * L'ordimento è basato sul peso di ogni arco. 
     * 
     */
    private class EdgeOrdererQuickSort {
    	
    	/*
         * La lista da ordinare.
         */
    	List<GraphEdge<L>> list = new ArrayList<GraphEdge<L>>();
    	
    	/**
         * Costruisce un risolutore per questo algoritmo.
         * 
         * @param list
         *                 la lista di archi da ordinare
         * @throws NullPointerException
         *                                  se la lista passata è nulla
         */
    	private EdgeOrdererQuickSort(List<GraphEdge<L>> list) {
    		if(this.list == null)
    			throw new NullPointerException(
    					"Tentativo di creare un risolutore da una lista nulla");
    		this.list = list; 
    	}
    	
    	/**
         * Chiamata iniziale sull'intera lista.
         */
    	private List<GraphEdge<L>> sort(){
    		// se la lista ha un solo elemento non và ordinata
    		if(this.list.size() <= 1) 
    			return list; 
    		// chiamo la procedura su l'intera lista
    		quickSort(0, this.list.size() - 1);
    		return this.list; 
    	}
    	
    	private void quickSort(int p, int r) {
    		if(p < r) {
    			// recupero il nuovo pivot
    			int q = partition(p, r);
    			// chiamate ricorsive 
    			quickSort(p, q - 1);
    			quickSort(q + 1, r); 
    		}
    	}
    	
    	private int partition(int p, int r) {
    		// pivot
    		GraphEdge<L> x = this.list.get(r);
    		// inizializzo indice i 
    		int i = p - 1;
    		// per ogni elemento nella partizione
    		for(int j = p; j <= r - 1; j++) {
    			// se è minore del pivot
    			if(this.list.get(j).getWeight() <= x.getWeight()) {
    				i++;
    				// scambio elemento in pos i con quello in pos j 
    				GraphEdge<L> temp = this.list.get(i);
    				this.list.set(i, this.list.get(j));
    				this.list.set(j, temp);
    			}
    		}
    		// scambio l'elemento pivot (in posizione r) con il primo elemento
    		// maggiore del pivot (i + 1)
    		GraphEdge<L> temp = this.list.get(r);
    		this.list.set(r, this.list.get(i + 1));
    		this.list.set(i + 1, temp);
			// ritorno la posizione del nuovo pivot
    		return i + 1; 
    	}
    }

    /**
     * Costruisce un calcolatore di un albero di copertura minimo che usa
     * l'algoritmo di Kruskal su un grafo non orientato e pesato.
     */
    public KruskalMSP() {
        this.disjointSets = new ForestDisjointSets<GraphNode<L>>();
    }

    /**
     * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. L'albero restituito non è radicato, quindi è rappresentato
     * semplicemente con un sottoinsieme degli archi del grafo.
     * 
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @return l'insieme degli archi del grafo g che costituiscono l'albero di
     *         copertura minimo trovato
     * @throw NullPointerException se il grafo g è null
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
    	if(g == null)
    		throw new NullPointerException(
    				"Tentativo di applicare algoritmo di Kruskal su un grafo nullo");
    	if(g.isDirected())
    		throw new IllegalArgumentException(
    				"Tentativo di applicare algoritmo di Kruskal su un grafo orientato");
    	for(GraphEdge<L> edge : g.getEdges()) {
	    	if(!edge.hasWeight() || edge.getWeight() < 0)  //g non pesato o pesi negativi
	    		throw new IllegalArgumentException(
	    				"Tentativo di applicare algoritmo di Kruskal su un grafo non pesato o con pesi negativi");
    	}
    	// pulisco la mappa da eventuali chiamate precedenti 
    	this.disjointSets.clear();
    	// insieme da restituire
    	Set<GraphEdge<L>> mst = new HashSet<GraphEdge<L>>();
    	// creo una lista con gli archi del grafo e la ordino in modo non decrescente 
    	EdgeOrdererQuickSort eoqs = new EdgeOrdererQuickSort(new ArrayList<GraphEdge<L>>(g.getEdges()));
    	List<GraphEdge<L>> edges = eoqs.sort();
    	// creo un insieme singoletto per ogni nodo del grafo
    	for(GraphNode<L> node : g.getNodes()) {
    		// controllo che non sia già presente
    		if(!this.disjointSets.isPresent(node))
    			this.disjointSets.makeSet(node);
    	}
    	// per ogni arco del grafo preso in ordine non decrescente
    	for(GraphEdge<L> edge : edges) {
    		// se non sono già connessi
    		if(this.disjointSets.findSet(edge.getNode1()) != 
    				this.disjointSets.findSet(edge.getNode2())) {
    			// aggiungo l'arco al MST
    			mst.add(edge);
    			// unisco i due insiemi disginti di cui fanno parte 
    			this.disjointSets.union(edge.getNode1(), edge.getNode2());
    		}
    	}
        return mst;
    }
}
