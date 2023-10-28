package it.unicam.cs.asdl2122.mp1;

import java.util.Set;
import java.util.HashSet;

/**
 * La classe viene implementata tramite un Set di liste concatenate.
 * Queste liste concatenate sono rappresentate tramite la classe 
 * {@code LinkedListDisjointSets}.
 * 
 * @author Luca Tesei (template) 
 * 		   Julian Marzoli, julian.marzoli@studenti.unicam.it (implementazione)
 *
 */
public class LinkedListDisjointSets implements DisjointSets {
	
	/*
	 * Il set che rappresenta la collezione di insiemi disgiunti.
	 */
	private Set<LinkedListDisjointSet> collection;
	
	/**
     * Classe per la creazione di una lista concatenata rappresentante 
     * un insieme disgiunto.
     * Ogni oggetto di questa classe ha un riferimento al primo e all'ultimo 
     * elemento della lista, che sono di tipo {@code DisjointSetElement}.
     * Ogni nodo della lista equivale ad un oggetto di tipo 
     * {@code DisjointSetElement}. La concatenazione viene effettuata
     * tramite il parametro {@code Ref1} di ciascun nodo. 
     * Il numero di nodi della lista è indicato nel campo {@code Number}
     * del rappresentante della lista.
     */
    private class LinkedListDisjointSet {
         	
    	/*
    	 * La testa della lista concatenata
    	 */
    	private DisjointSetElement head; 
        
    	/*
    	 * La coda della lista concatenata
    	 */
    	private DisjointSetElement tail; 
	
        /**
         * Crea un insieme singoletto, rappresentato con una lista
         * concatenata avente un solo nodo
         */
        LinkedListDisjointSet(DisjointSetElement e) {
        	this.head = e;
        	this.tail = e;
        	e.setRef1(e);
        	e.setRef2(null);
        	e.setNumber(1);
        }
        
        /**
         * Crea un insieme dati la testa e la coda.
         */
        LinkedListDisjointSet(DisjointSetElement head, DisjointSetElement tail) {
        	this.head = head;
        	this.tail = tail;
        }
         
        /**
         * Restituisce la testa della lista concatenata.
         */
        private DisjointSetElement getHead() {
        	return this.head;
        }
        
        /**
         * Restituisce la coda della lista concatenata.
         */
        private DisjointSetElement getTail() {
        	return this.tail;
        }
        
    }

    /**
     * Crea una collezione vuota di insiemi disgiunti.
     */
    public LinkedListDisjointSets() {
    	this.collection = new HashSet<LinkedListDisjointSet>();
    }

    /*
     * Nella rappresentazione con liste concatenate un elemento è presente in
     * qualche insieme disgiunto se il puntatore al suo elemento rappresentante
     * (ref1) non è null.
     */
    @Override
    public boolean isPresent(DisjointSetElement e) {
    	return e.getRef1() != null; 
    }

    /*
     * Nella rappresentazione con liste concatenate un nuovo insieme disgiunto è
     * rappresentato da una lista concatenata che contiene l'unico elemento. Il
     * rappresentante deve essere l'elemento stesso e la cardinalità deve essere
     * 1.
     */
    @Override
    public void makeSet(DisjointSetElement e) {
    	if (e == null)
    		throw new NullPointerException(
    				"Tentativo di creare un insieme con elemento nullo");
    	if(isPresent(e))
    		throw new IllegalArgumentException(
    				"L'elemento da cui si vuole creare un insieme è gia presente");
    	// creo un insieme singoletto contenente e
    	LinkedListDisjointSet l = new LinkedListDisjointSet(e);
    	// aggiungo l'insieme alla collezione di insiemi disgiunti
    	this.collection.add(l);
    }

    /*
     * Nella rappresentazione con liste concatenate per trovare il
     * rappresentante di un elemento basta far riferimento al suo puntatore
     * ref1.
     */
    @Override
    public DisjointSetElement findSet(DisjointSetElement e) {
    	if (e == null)
    		throw new NullPointerException(
    				"Tentativo di cercare il rappresentante di un insime nullo");
    	if(!isPresent(e))
    		throw new IllegalArgumentException(
    				"L'elemento di cui si vuole recuperare il rappresentante"
    				+ "non è presente in nessun insieme");
        return e.getRef1();
    }

    /*
     * Dopo l'unione di due insiemi effettivamente disgiunti il rappresentante
     * dell'insieme unito è il rappresentate dell'insieme che aveva il numero
     * maggiore di elementi tra l'insieme di cui faceva parte {@code e1} e
     * l'insieme di cui faceva parte {@code e2}. Nel caso in cui entrambi gli
     * insiemi avevano lo stesso numero di elementi il rappresentante
     * dell'insieme unito è il rappresentante del vecchio insieme di cui faceva
     * parte {@code e1}.
     * 
     * Questo comportamento è la risultante naturale di una strategia che
     * minimizza il numero di operazioni da fare per realizzare l'unione nel
     * caso di rappresentazione con liste concatenate.
     * 
     */
    @Override
    public void union(DisjointSetElement e1, DisjointSetElement e2) {
    	if (e1 == null || e2 == null)
    		throw new NullPointerException(
    				"Uno o entrambi gli elementi passati è nullo ");
    	if(!isPresent(e1) || !isPresent(e2))
    		throw new IllegalArgumentException(
    				"Uno o entrambi gli elementi passati non è prensente"
    				+ "in nessun insieme disgiunto");
    	// se i due elementi sono contenuti nello stesso insieme
    	if(getListOf(e1) == getListOf(e2))
    		// non faccio nulla
    		return;
    	// la nuova lista risultante dall'unione
    	LinkedListDisjointSet newList;
    	// se l'insieme in cui è contenuto e2 ha più elementi
    	// della lista in cui è contenuto e1
    	if(e1.getRef1().getNumber() < e2.getRef1().getNumber()) {
    		// sarà la lista di e1 ad essere "inserita" dentro 
    		// alla lista di e2
    		newList = unionLists(getListOf(e1), getListOf(e2));
    	// altrimenti sarà la lista di e2 ad essere "inserita"
    	// dentro la lista di e1
    	} else {
    		newList = unionLists(getListOf(e2), getListOf(e1));
    	}
    	// rimuovo i due insiemi dalla collezione
    	this.collection.remove(getListOf(e1));
		this.collection.remove(getListOf(e2));
		// aggiungo il nuovo insieme 
		this.collection.add(newList);
    }

    @Override
    public Set<DisjointSetElement> getCurrentRepresentatives() {
    	// creo un Set in cui inserire i rappresentanti
    	Set<DisjointSetElement> repr = new HashSet<DisjointSetElement>();
    	// per ogni lista nella collection
    	for(LinkedListDisjointSet l : this.collection) {
    		// ne aggiungo il rappresentante nel Set
    		repr.add(findSet(l.getHead()));
    	}
        //ritorno il Set
    	return repr;
    }

    @Override
    public Set<DisjointSetElement> getCurrentElementsOfSetContaining(
            DisjointSetElement e) {
    	if (e == null)
    		throw new NullPointerException(
    				"L'elemento passato è nullo");
    	if(!isPresent(e))
    		throw new IllegalArgumentException(
    				"L'elemento passato non è prensente "
    				+ "in nessun insieme disgiunto");
    	// creo un Set in cui inserire gli elementi
    	Set<DisjointSetElement> elmts = new HashSet<DisjointSetElement>();
    	// parto dall testa della lista
    	DisjointSetElement curr = getListOf(e).head;
    	int count = findSet(e).getNumber();
    	// finche ci sono elementi
    	while(count > 0) {
    		// aggiungo l'elemento corrente nella lista
    		elmts.add(curr);
    		// passo al prossimo elemento
    		curr = curr.getRef2();
    		count--;
    	}
        // ritorno il nuovo Set
    	return elmts;
    }

    @Override
    public int getCardinalityOfSetContaining(DisjointSetElement e) {
    	if(e == null)
    		throw new NullPointerException(
    				"Tentativo di trovare la cardinalità di una lista "
    				+ "da un elemento nullo ");
    	if(!isPresent(e))
    		throw new IllegalArgumentException(
    				"L'elemento non è prensente in nessuna lista");
    	// recupero il campo {@code Number} dal rappresentante di e
    	return findSet(e).getNumber();
    }

    /**
     * Restituisce l'insieme disgiunto di cui fa parte un
     * certo elemento.
     * 
     * @param e
     *              l'elemento di cui si vuole ottenere l'insieme
     * @return la lista concatenata in cui è presente l'insieme
     * @throws NullPointerException
     *                                      se l'elemento passato è null
     * @throws IllegalArgumentException
     *                                      se l'elemento passato non è
     *                                      contenuto in nessun insieme
     *                                      disgiunto
     */
    private LinkedListDisjointSet getListOf(DisjointSetElement e) {
    	if(e == null)
    		throw new NullPointerException(
    				"Tentativo di trovare la lista di un elemento nullo ");
    	if(!isPresent(e))
    		throw new IllegalArgumentException(
    				"L'elemento non è prensente in nessuna lista");
    	// per ogni insieme nella collezione
    	for(LinkedListDisjointSet l : this.collection) {
    		// se la testa di questo isieme corrisponde
    		// al rappresentante dell'elemento
    		if(l.head.equals(findSet(e)))
    			// la lista è questa
    			return l;
    	}
    	return null;
    }
    
    /**
     * Date due liste le concatena per formare una solo lista. 
     * Più precisamente la prima lista viene insierita tra il 
     * primo e il secondo nodo della seconda lista.
     * 
     * @param l1
     *              la lista da inserire 
     * @param l2
     *              la lista in cui viene inserita 
     * @return la lista concatenata risultante dall'unione
     * @throws NullPointerException
     *                                      se una o entrambe le liste 
     *                                      passate sono nulle
     */
    private LinkedListDisjointSet unionLists(LinkedListDisjointSet l1, LinkedListDisjointSet l2) {
    	if(l1 == null || l2 == null)
    		throw new NullPointerException(
    				"Tentativo di unire due liste nulle");
    	// per ogni elemento nella prima lista
    	DisjointSetElement e = l1.getHead();
    	int count = l1.head.getNumber();
    	while(count > 0) { 
    		count--;
    		// cambio il ref1 con il ref1 della seconda lista
    		e.setRef1(l2.getHead().getRef1());
    		e = e.getRef2();
    	}
    	// il ref2 della coda della prima lista diventa il ref2 della testa della seconda lista
    	l1.getTail().setRef2(l2.getHead().getRef2());
    	// il ref2 della testa della seconda lista diventa la testa della prima lista 
    	l2.getHead().setRef2(l1.getHead());
    	// aggiorno il campo number nel nuovo rappresentante
    	l2.getHead().setNumber(l1.getHead().getNumber() + l2.getHead().getNumber());
    	// creo una nuova lista avente testa e coda quelli della seconda lista 
    	LinkedListDisjointSet unitedSet = new LinkedListDisjointSet(l2.getHead(), l2.getTail());
    	// ritorno la nuova lista 
    	return unitedSet;
    }
    
}
