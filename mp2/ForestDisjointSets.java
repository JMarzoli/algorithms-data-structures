package it.unicam.cs.asdl2122.mp2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implementazione dell'interfaccia <code>DisjointSets<E></code> tramite una
 * foresta di alberi ognuno dei quali rappresenta un insieme disgiunto. Si
 * vedano le istruzioni o il libro di testo Cormen et al. (terza edizione)
 * Capitolo 21 Sezione 3.
 * 
 * @author Luca Tesei (template) 
 * 		   Julian Marzoli, julian.marzoli@studenti.unicam.it (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi degli insiemi disgiunti
 */
public class ForestDisjointSets<E> implements DisjointSets<E> {

    /*
     * Mappa che associa ad ogni elemento inserito il corrispondente nodo di un
     * albero della foresta. La variabile è protected unicamente per permettere
     * i test JUnit.
     */
    protected Map<E, Node<E>> currentElements;
    
    /*
     * Classe interna statica che rappresenta i nodi degli alberi della foresta.
     * Gli specificatori sono tutti protected unicamente per permettere i test
     * JUnit.
     */
    protected static class Node<E> {
        /*
         * L'elemento associato a questo nodo
         */
        protected E item;

        /*
         * Il parent di questo nodo nell'albero corrispondente. Nel caso in cui
         * il nodo sia la radice allora questo puntatore punta al nodo stesso.
         */
        protected Node<E> parent;

        /*
         * Il rango del nodo definito come limite superiore all'altezza del
         * (sotto)albero di cui questo nodo è radice.
         */
        protected int rank;

        /**
         * Costruisce un nodo radice con parent che punta a se stesso e rango
         * zero.
         * 
         * @param item
         *                 l'elemento conservato in questo nodo
         * 
         */
        public Node(E item) {
            this.item = item;
            this.parent = this;
            this.rank = 0;
        }
        
        /**
         * Setta il puntatore al nodo parent di questo nodo.
         * 
         * @param parent
         *                 il nodo che precede questo nodo
         * 
         */
        public void setParent(Node<E> parent) {
        	this.parent = parent;
        }
        
        /**
         * Setta il valore rank di questo nodo.
         * 
         * @param rank
         *                 il valore intero del nuovo rank
         * 
         */
        public void setRank(int rank) {
        	this.rank = rank; 
        }
        	 
    }

    /**
     * Costruisce una foresta vuota di insiemi disgiunti rappresentati da
     * alberi.
     */
    public ForestDisjointSets() {
    	this.currentElements = new HashMap<E, Node<E>>(); 
    }

    @Override
    public boolean isPresent(E e) {
    	if(e == null)
    		throw new NullPointerException(
    				"Tentativo di verificare la presenza di un elemento nullo");
    	// verifico se nella mappa è presente una chiave associata all'elemento e
        return this.currentElements.containsKey(e);
    }

    /*
     * Crea un albero della foresta consistente di un solo nodo di rango zero il
     * cui parent è se stesso.
     */
    @Override
    public void makeSet(E e) {
    	if(e == null)
    		throw new NullPointerException(
    				"Tentativo di creare un insieme disgiunto da un elemento nullo");
    	if(this.isPresent(e))
    		throw new IllegalArgumentException(
    				"Tentativo di creare un insieme disgiunto da un elemento già presente");
    	this.currentElements.put(e, new Node<E>(e));
    }

    /*
     * L'implementazione del find-set deve realizzare l'euristica
     * "compressione del cammino". Si vedano le istruzioni o il libro di testo
     * Cormen et al. (terza edizione) Capitolo 21 Sezione 3.
     */
    @Override
    public E findSet(E e) {
    	if(e == null)
    		throw new NullPointerException(
    				"Tentativo di ricerca del rappresentante di un nodo nullo");
    	// se l'elemento non è presente in nessun insieme torno null
    	if(!this.isPresent(e))
    		return null;
    	// recupero il nodo associato all'elemento
    	Node<E> node = this.currentElements.get(e);
    	// finchè node non è la radice
    	if(node != node.parent)
    		// setto il parent del nodo chiamando ricorsivamente 
    		// questo metodo sul suo parent
    		node.setParent(this.currentElements.get(findSet(node.parent.item)));
    	// chiamate ricorsive terminate, node è il rappresentante (radice)
        return node.parent.item;
    }

    /*
     * L'implementazione dell'unione deve realizzare l'euristica
     * "unione per rango". Si vedano le istruzioni o il libro di testo Cormen et
     * al. (terza edizione) Capitolo 21 Sezione 3. In particolare, il
     * rappresentante dell'unione dovrà essere il rappresentante dell'insieme il
     * cui corrispondente albero ha radice con rango più alto. Nel caso in cui
     * il rango della radice dell'albero di cui fa parte e1 sia uguale al rango
     * della radice dell'albero di cui fa parte e2 il rappresentante dell'unione
     * sarà il rappresentante dell'insieme di cui fa parte e2.
     */
    @Override
    public void union(E e1, E e2) {
    	if(e1 == null || e2 == null)
    		throw new NullPointerException(
    				"Tentativo di unire due insiemi disgiunti a partire da elementi nulli");
    	if(!this.isPresent(e1) || !this.isPresent(e2))
    		throw new IllegalArgumentException(
    				"Tentativo di unire due insiemi disgiunti a partire da elementi non presenti");
    	// chiamo il metodo link, sui rappresentati dei due elementi
    	this.link(this.findSet(e1), this.findSet(e2));
    }

    @Override
    public Set<E> getCurrentRepresentatives() {
    	// creo l'insieme dove aggiungere i rappresentanti
    	Set<E> repr = new HashSet<E>();
    	// per ogni elemento nella mappa
    	for(E element : this.currentElements.keySet()) {
    		// ne trovo il rappresentante e lo aggiungo al set
    		repr.add(this.findSet(element));
    	}
        return repr;
    }

    @Override
    public Set<E> getCurrentElementsOfSetContaining(E e) {
    	if(e == null)
    		throw new NullPointerException(
    				"Tentativo di recuperare gli elementi di un insieme di un elemento nullo");
    	if(!this.isPresent(e))
    		throw new IllegalArgumentException(
    				"Tentativo di recuperare gli elementi di un insieme di un elemento non presente");
    	// creo l'insieme da restituire
    	Set<E> elements = new HashSet<E>();
    	// per ogni elemento nella mappa
    	for(E el : this.currentElements.keySet()) {
    		// se ha stesso rappresentante di e
    		if(this.findSet(el).equals(this.findSet(e)))
    			// fanno parte dello stesso insieme e lo aggiungo
    			elements.add(el);
    	}
        return elements;
    }

    @Override
    public void clear() {
    	// pulisco la mappa 
    	this.currentElements.clear();
    }
    
    /**
     * Sotto-metodo utile all'unione di due insiemi disgiunti
     * a partire dai rappresentanti di due insiemi.
     * Se i rappresentanti dei due insiemi hanno stesso rango,
     * e2 diventerà il rappresentante del nuovo insieme e il suo
     * rango verrà aumentato di uno.
     * Altrimenti il rappresentante del nuovo insieme sarà il nodo
     * con rango maggiore, in questo caso il rango dei due nodi
     * rimane invariato.
     * 
     * @param e1
     *              rappresentante del primo insieme da unire
     * @param e2
     *              rappresentante del secondo insieme da unire
     */
    private void link(E e1, E e2) {
    	// recupero il nodo associato all'elemento di entrambi gli elementi
    	Node<E> node1 = this.currentElements.get(e1);
    	Node<E> node2 = this.currentElements.get(e2);
    	// se il nodo 1 ha rank più alto
    	if(node1.rank > node2.rank) {
    		// diventerà il parent del secondo nodo
    		node2.setParent(node1);
    	} else {
    		// altrimenti sarà l'insieme del secondo nodo
    		// ad essere unito a quello del primo nodo
    		node1.setParent(node2);
    		// se i due nodi hanno stesso rank và aumentato
    		if(node1.rank == node2.rank)
    			node2.setRank(node2.rank + 1);
    	}
    }

}
