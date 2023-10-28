package it.unicam.cs.asdl2122.mp2;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe singoletto che implementa l'algoritmo di Prim per trovare un Minimum
 * Spanning Tree di un grafo non orientato, pesato e con pesi non negativi.
 * 
 * L'algoritmo richiede l'uso di una coda di min priorità tra i nodi che può
 * essere realizzata con una semplice ArrayList (non c'è bisogno di ottimizzare
 * le operazioni di inserimento, di estrazione del minimo, o di decremento della
 * priorità).
 * 
 * Si possono usare i colori dei nodi per registrare la scoperta e la visita
 * effettuata dei nodi.
 * 
 * @author Luca Tesei (template) 
 * 		   Julian Marzoli, julian.marzoli@studenti.unicam.it (implementazione)
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class PrimMSP<L> {

	/*
     * Lista sostitutiva alla coda di min priorità.
     */
    public List<GraphNode<L>> minQueue;
    
    /*
     * In particolare: si deve usare una coda con priorità che può semplicemente
     * essere realizzata con una List<GraphNode<L>> e si deve mantenere un
     * insieme dei nodi già visitati
     */

    /**
     * Crea un nuovo algoritmo e inizializza la coda di priorità con una coda
     * vuota.
     */
    public PrimMSP() {
    	this.minQueue = new ArrayList<GraphNode<L>>();
    }

    /**
     * Utilizza l'algoritmo goloso di Prim per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. Dopo l'esecuzione del metodo nei nodi del grafo il campo
     * previous deve contenere un puntatore a un nodo in accordo all'albero di
     * copertura minimo calcolato, la cui radice è il nodo sorgente passato.
     * 
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @param s
     *              il nodo del grafo g sorgente, cioè da cui parte il calcolo
     *              dell'albero di copertura minimo. Tale nodo sarà la radice
     *              dell'albero di copertura trovato
     * 
     * @throw NullPointerException se il grafo g o il nodo sorgente s sono nulli
     * @throw IllegalArgumentException se il nodo sorgente s non esiste in g
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    public void computeMSP(Graph<L> g, GraphNode<L> s) {
    	if(g == null || s == null)
    		throw new NullPointerException(
    				"Tentativo di applicare algoritmo di Prim su grafo o radice nulli");
    	if(g.getNode(s) == null)
    		throw new IllegalArgumentException(
    				"Tentativo di applicare algoritm Prim da radice non presente");
    	if(g.isDirected())
    		throw new IllegalArgumentException(
    				"Tentativo di applicare algoritmo di Prim su un grafo orientato");
    	for(GraphEdge<L> edge : g.getEdges()) {
	    	if(!edge.hasWeight() || edge.getWeight() < 0)  //g non pesato o pesi negativi
	    		throw new IllegalArgumentException(
	    				"Tentativo di applicare algoritmo di Prim su un grafo non pesato o con pesi negativi");
    	}	
    	// inizializzo il nodo sorgente
    	g.getNode(s).setColor(GraphNode.COLOR_GREY);
    	g.getNode(s).setFloatingPointDistance(0);  	
    	// lo aggiungo come primo elemento nella coda
    	// così verrà estratto per primo
    	this.minQueue.add(0, s);    	
    	// inizializzo gli altri nodi del grafo
    	for(GraphNode<L> v : g.getNodes()) {
    		if(!v.equals(s)){
		    	v.setColor(GraphNode.COLOR_WHITE);
		    	v.setFloatingPointDistance(Double.POSITIVE_INFINITY);
		    	v.setPrevious(null);
		    	this.minQueue.add(v);
    		}
    	}
    	// finchè ci sono ancora nodi nella coda
    	while(!minQueue.isEmpty()) {
    		// estraggo un nodo dalla coda
    		GraphNode<L> u = this.minQueue.get(0);
    		this.minQueue.remove(0);
    		// per ogni nodo adiacente al nodo estratto
    		for(GraphNode<L> v : g.getAdjacentNodesOf(u)) { 
    			// se l'arco che lo collega ha peso minore alla sua distanza corrente
    			if(minQueue.contains(v) && g.getEdge(u, v).getWeight() < v.getFloatingPointDistance()) {
    				v.setColor(GraphNode.COLOR_GREY);
    				// aggiorno la distanza 
    				v.setFloatingPointDistance(g.getEdge(u, v).getWeight());
    				// aggiorno il nodo che lo precede
    				v.setPrevious(u);
    			}	
       		}
    		// il nodo estratto diventa ora "completato"
    		u.setColor(GraphNode.COLOR_BLACK);
    	}
    }
}
