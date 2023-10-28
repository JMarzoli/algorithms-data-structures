/**
 * 
 */
package it.unicam.cs.asdl2122.mp2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * 
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * 
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 * 
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 * 
 * Questa classe supporta i metodi di cancellazione di nodi e archi e
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 * 
 * @author Luca Tesei (template) 
 * 		   Julian Marzoli, julian.marzoli@studenti.unicam.it (implementazione)
 *
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;
    
    /**
     * Numero di archi presenti nel grafo.
     * (La variabile è inizializzata qui perchè, 
     *  so se è concesso modificare il costruttore 
     *  della classe).
     */
    private int edgeCount = 0; 

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public int nodeCount() { 
        return this.matrix.size();
    }

    @Override
    public int edgeCount() { 
    	return this.edgeCount;
    }
    
    @Override
    public void clear() {
    	// rimuovo ogni elemento dalla mappa  
    	this.nodesIndex.clear();
    	// rimuovo ogni elemento dall'insieme di nodi  
    	this.matrix.clear();
    	// aggiorno il numero di archi dle grafo
    	this.edgeCount = 0; 
    }

    @Override
    public boolean isDirected() {
    	// come da API questo grafo non è orientato 
        return false;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
    	if(node == null)
    		throw new NullPointerException(
    				"Tentativo di aggiungere al grafo un nodo nullo");
    	// se non è presente, il nodo viene aggiunto alla mappa   
    	if(this.nodesIndex.putIfAbsent(node, this.nodesIndex.size()) == null) {
    		// aggiungo una riga e una colonna alla matrice di adiacenza
    		this.addColumnToMatrix();
    		this.addRowToMatrix();
    		return true; 
    		}
    	// il node era già presente
    	return false; 
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {
    	if(label == null)
    		throw new NullPointerException(
    				"Tentativo di aggiungere al grafo un nodo con etichetta nulla");
    	GraphNode<L> node = new GraphNode<L>(label);
    	return this.addNode(node); 
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {
    	if(node == null)
    		throw new NullPointerException(
    				"Tentativo di rimuovere dal grafo un nodo nullo");
    	if(!this.nodesIndex.containsKey(node)) 
    		throw new IllegalArgumentException(
    				"Tentativo di rimuovere dal grafo un nodo non presente");
    	// rimuovo la entry del nodo dalla mappa e ne salvo l'indice
    	int index = this.removeNodeFromMap(node);
    	// rimuovo la riga del nodo eliminato dalla matrice di adiacenza 
    	this.matrix.remove(index);
    	// rimuovo la colonna del nodo eliminato dalla matrice di adiacenza
    	this.removeColumnFromMatrix(index);
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
    	if(label == null)
    		throw new NullPointerException(
    				"Tentativo di rimuovere dal grafo un nodo da un'etichetta nulla");
    	GraphNode<L> node = new GraphNode<L>(label);
    	if(!this.nodesIndex.containsKey(node))
    		throw new IllegalArgumentException(
    				"Tentativo di rimuovere dal grafo un nodo da un'etichetta non presente");
    	this.removeNode(node);
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
    	if(i < 0 || i > this.nodesIndex.size() - 1)  
    		throw new IndexOutOfBoundsException(
    				"Tentativo di rimuovere dal grafo un nodo da un indice non prensente");
    	this.removeNode(this.getNode(i));
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
    	if(node == null)
    		throw new NullPointerException(
    				"Tentativo di ricerca nel grafo di un nodo nullo");
    	// per ogni nodo nella mappa  
    	for(GraphNode<L> key : this.nodesIndex.keySet()) {
    		// se coincide con il nodo da ricercare
    		if(node.equals(key)) {
    			// lo ritorno
    			return key; 
    		}
    	}
    	// altrimenti non è presente
    	return null;
    }

    @Override
    public GraphNode<L> getNode(L label) {
    	if(label == null)
    		throw new NullPointerException(
    				"Tentativo di ricerca nel grafo di un nodo a partire da un'etichetta nulla");
    	GraphNode<L> node = new GraphNode<L>(label);
    	return this.getNode(node);
    }

    @Override
    public GraphNode<L> getNode(int i) {
    	if(i < 0 || i > this.nodesIndex.size() - 1)
    		throw new IndexOutOfBoundsException( 
    				"Tentativo di ricerca nel grafo di un nodo a partire da un indice non presente");
        // scorro ogni entry nella mappa  
    	for(Map.Entry<GraphNode<L>, Integer> entry : this.nodesIndex.entrySet()) {
    		// se il valore corrisponde ad i 
        	if(entry.getValue() == i) 
        		// ne ritorno la chiave associata
        		return entry.getKey();        	
        }
    	return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
    	if(node == null)
    		throw new NullPointerException(
    				"Tentativo di ricerca di un indice a partire da un nodo nullo");
    	if(!this.nodesIndex.containsKey(node))
    		throw new IllegalArgumentException(
    				"Tentativo di ricerca di un indice di un nodo non presente nel grafo");
    	// ritorno il valore associato alla chiave (nodo) nella mappa 
        return this.nodesIndex.get(node);
    }

    @Override
    public int getNodeIndexOf(L label) {
    	if(label == null)
    		throw new NullPointerException(
    				"Tentativo di ricerca di un indice di nodo a partire da un'etichetta nulla");
    	GraphNode<L> node = new GraphNode<L>(label); 
    	if(!this.nodesIndex.containsKey(node))
    		throw new IllegalArgumentException(
    				"Tentativo di ricerca di un indice di un nodo non presente nel grafo");
        return this.getNodeIndexOf(node);
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        // ritorno l'insieme delle chiavi nella mappa 
        return this.nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
    	if(edge == null)
    		throw new NullPointerException(
    				"Tentativo di aggiungere al grafo un arco nullo");
    	if(edge.getNode1() == null || edge.getNode2() == null)
    		throw new IllegalArgumentException(
    				"Tentativo di inserire nel grafo un arco con uno o entrambi i nodi non specificati");
    	if(edge.isDirected())
    		throw new IllegalArgumentException(
    				"Tentativo di inserire in un grafo non orientato un arco orientato");
    	// se l'arco non è già presente nella matrice di adiacenza 
    	if(this.getEdge(edge) == null) {
	    	// lo aggiungo alla matrice in posizione i j
	    	this.matrix.get(
	    			this.getNodeIndexOf(edge.getNode1())).set(
	    					this.getNodeIndexOf(edge.getNode2()), edge);			
	    	// lo aggiungo alla matrice in posizione j i 
	    	this.matrix.get(
	    			this.getNodeIndexOf(edge.getNode2())).set(
	    					this.getNodeIndexOf(edge.getNode1()), edge);
	    	this.edgeCount++;
	    	return true; 
    	}
    	// l'arco era già presente 
    	return false; 
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
    	if(node1 == null || node2 == null)
    		throw new NullPointerException(
    				"Tentativo di aggiungere al grafo un arco a partire da uno o entrambi nodi nulli");
    	if(!this.nodesIndex.containsKey(node1) || !this.nodesIndex.containsKey(node2))
    		throw new IllegalArgumentException(
    				"Tentativo di inserire nel grafo un arco con uno o entrambi i nodi non specificati");
    	GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);
        return this.addEdge(edge);
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2,
    		double weight) {
    	if(node1 == null || node2 == null)
    		throw new NullPointerException(
    				"Tentativo di aggiungere al grafo un arco pesato a partire da uno o entrambi nodi nulli");
    	if(!this.nodesIndex.containsKey(node1) || !this.nodesIndex.containsKey(node2))
    		throw new IllegalArgumentException(
    				"Tentativo di inserire nel grafo un arco pesato con uno o entrambi i nodi non specificati");
    	GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false, weight);
        return this.addEdge(edge);
    }

    @Override
    public boolean addEdge(L label1, L label2) {
    	if(label1 == null || label2 == null)
    		throw new NullPointerException(
    				"Tentativo di aggiungere al grafo un arco a partire da una o entrambe le etichette nulle");
    	if(this.getNode(label1) == null || this.getNode(label2) == null)
    		throw new IllegalArgumentException(
    				"Tentativo di inserire nel grafo un arco a partire da delle etichette non presenti in nessun nodo nel grafo");  	
    	return this.addEdge(this.getNode(label1), this.getNode(label2));
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
    	if(label1 == null || label2 == null)
    		throw new NullPointerException(
    				"Tentativo di aggiungere al grafo un arco pesato a partire da una o entrambe le etichette nulle");
    	if(this.getNode(label1) == null || this.getNode(label1) == null)
    		throw new IllegalArgumentException(
    				"Tentativo di inserire nel grafo un arco pesato a partire da delle etichette non presenti in nessun nodo nel grafo");  	
    	return this.addWeightedEdge(this.getNode(label1), this.getNode(label2), weight);
    }

    @Override
    public boolean addEdge(int i, int j) {
	if(i < 0 || i > this.nodesIndex.size() - 1 || j < 0 || j > this.nodesIndex.size() - 1)
    		throw new IndexOutOfBoundsException(
    				"Tentativo di aggiungere al grafo un arco a partire da uno o entrambi gli indici non validi");
    	return this.addEdge(this.getNode(i), this.getNode(j));
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
    	if(i < 0 || i > this.nodesIndex.size() - 1 || j < 0 || j > this.nodesIndex.size() - 1)
    		throw new IndexOutOfBoundsException(
    				"Tentativo di aggiungere al grafo un arco pesato a partire da uno o entrambi gli indici non validi");
    	return this.addWeightedEdge(this.getNode(i), this.getNode(j), weight);
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
    	if(edge == null)
    		throw new NullPointerException(
    				"Tentativo di rimuovere dal grafo un arco nullo");
    	if(this.getEdge(edge) == null)
    		throw new IllegalArgumentException(
    				"Tentativo di rimuovere dal grafo un arco non presente");
    	if(edge.getNode1() == null || edge.getNode2() == null)
    		throw new IllegalArgumentException(
    				"Tentativo di rimuovere dal grafo un arco con uno o entrambi i nodi non specificati");
    	// rimuovo l'arco dalla matrice in posizione i, j
    	this.matrix.get(this.getNodeIndexOf(edge.getNode1())).set(
    			this.getNodeIndexOf(edge.getNode2()), null);
    	// rimuovo l'arco dalla matrice in posizione j, i 
    	this.matrix.get(this.getNodeIndexOf(edge.getNode2())).set(
    			this.getNodeIndexOf(edge.getNode1()), null);
    	// decremento il contatore degli archi
    	this.edgeCount--;
    }
 
    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
    	if(node1 == null || node2 == null)
    		throw new NullPointerException(
    				"Tentativo di rimuovere dal grafo un arco a partire da uno o entrambi nodi nulli");
    	if(this.getEdge(node1, node2) == null)
    		throw new IllegalArgumentException(
    				"Tentativo di rimuovere dal grafo un arco non presente");
    	if(this.getNode(node1) == null || this.getNode(node2) == null)
    		throw new IllegalArgumentException(
    				"Tentativo di rimuovere dal grafo un arco a partire da uno o entrambi i nodi non esistenti");
    	GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);
    	this.removeEdge(edge);
    }

    @Override
    public void removeEdge(L label1, L label2) {
    	if(label1 == null || label2 == null)
    		throw new NullPointerException(
    				"Tentativo di rimuovere dal grafo un arco a partire da una o entrambe etichette nulle");
    	if(this.getNode(label1) == null || this.getNode(label2) == null)
    		throw new IllegalArgumentException(
    				"Tentativo di rimuovere dal grafo un arco a partire da una o entrambe etichette non presenti");
    	if(this.getEdge(label1, label2) == null)
    		throw new IllegalArgumentException(
    				"Tentativo di rimuovere dal grafo un arco non presente a partire da due etichette");
    	this.removeEdge(this.getEdge(label1, label2));
    } 

    @Override
    public void removeEdge(int i, int j) {
    	if(this.getEdge(i, j) == null)
    		throw new IllegalArgumentException(
    				"Tentativo di rimuovere dal grafo un arco non presente a partire da due indici");
    	if(i < 0 || i > this.nodesIndex.size() - 1 || j < 0 || j > this.nodesIndex.size() - 1)
    		throw new IndexOutOfBoundsException(
    				"Tentativo di rimuovere dal grafo un arco a partire da uno o entrambi gli indici non validi");
    	this.removeEdge(this.getEdge(i, j));
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
    	if(edge == null)
    		throw new NullPointerException(
    				"Tentativo di ricerca nel grafo di un arco nullo");
    	if(edge.isDirected())
    		throw new IllegalArgumentException(
    				"Tentativo di ricerca nel grafo di un arco orientato");
    	if(!this.nodesIndex.containsKey(edge.getNode1()) || !this.nodesIndex.containsKey(edge.getNode2()))
    		throw new IllegalArgumentException(
    				"Tentativo di ricerca nel grafo di un arco con uno o entrambi i nodi non presenti");
    	// cerco l'arco in posizione i, j della matrice
    	return this.matrix.get(this.getNodeIndexOf(edge.getNode1())).get(
    			this.getNodeIndexOf(edge.getNode2()));
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
    	if(node1 == null || node2 == null)
    		throw new NullPointerException(
    				"Tentativo di ricerca nel grafo di un arco a partire da uno o entrambi i nodi nulli");
    	if(this.getNode(node1) == null || this.getNode(node2) == null)
    		throw new IllegalArgumentException(
    				"Tentativo di rimuovere dal grafo un arco a partire da uno o entrambi i nodi non presenti");
    	GraphEdge<L> edge = new GraphEdge<L>(node1, node2, false);
    	return this.getEdge(edge);
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
    	if(label1 == null || label2 == null)
    		throw new NullPointerException(
    				"Tentativo di ricerca nel grafo di un arco a partire da una o entrambe le etichette nulle");
    	if(this.getNode(label1) == null || this.getNode(label2) == null)
    		throw new IllegalArgumentException(
    				"Tentativo di rimuovere dal grafo un arco a partire da etichette di uno o entrambi i nodi non presenti"); 
    	return this.getEdge(this.getNode(label1), this.getNode(label2));
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {    	
    	if(i < 0 || i > this.nodesIndex.size() - 1 || j < 0 || j > this.nodesIndex.size() - 1)
    		throw new IndexOutOfBoundsException(
    				"Tentativo di cercare nel grafo un arco a partire da uno o entrambi gli indici dei nodi non validi");
        return this.getEdge(this.getNode(i), this.getNode(j));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
    	if(node == null)
    		throw new NullPointerException(
    				"Tentativo di ricerca dei nodi adiancenti ad un nodo nullo");
    	if(this.getNode(node) == null)
    		throw new IllegalArgumentException(
    				"Tentativo di ricerca dei nodi adiancenti ad un nodo non presente nel grafo");
    	// creo il set in cui aggiungere i nodi adiacenti
    	Set<GraphNode<L>> adj = new HashSet<GraphNode<L>>();
    	// per ogni casella nella riga del nodo
    	for(GraphEdge<L> entry : this.matrix.get(this.getNodeIndexOf(node))) {
    		// se nella casella è presente un arco
    		if(entry != null) {
    			// se il node1 non è esso stesso
    			if(!entry.getNode1().equals(node)) {
    				// lo aggiungo perchè è un nodo adiacente
    				adj.add(entry.getNode1());
    			} else {
    				// altrimenti il nodo adiacente è il node2
    				adj.add(entry.getNode2());
    			}
    		}
    	}
    	return adj; 
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
    	if(label == null)
    		throw new NullPointerException(
    				"Tentativo di ricerca dei nodi adiancenti ad un nodo nullo a partire da un'etichetta nulla");
    	if(this.getNode(label) == null)
    		throw new IllegalArgumentException(
    				"Tentativo di ricerca dei nodi adiancenti ad un nodo non presente nel grafo a partire da un'etichetta");
    	return this.getAdjacentNodesOf(this.getNode(label));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
    	if(!this.nodesIndex.containsValue(i))
    		throw new IndexOutOfBoundsException(
    				"Tentativo di ricerca di nodi adiacenti a partire da un indice di nodo non valido");
    	return this.getAdjacentNodesOf(this.getNode(i));
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
    	if(node == null)
    		throw new NullPointerException(
    				"Tentativo di ricerca degli archi connessi ad un nodo nullo");
    	if(this.getNode(node) == null)
    		throw new IllegalArgumentException(
    				"Tentativo di ricerca degli archi connessi a un nodo non presente nel grafo");
    	// creo il set in cui aggiungere gli archi
    	Set<GraphEdge<L>> edges = new HashSet<GraphEdge<L>>();
    	// scorro la riga con indice del nodo
    	for(GraphEdge<L> entry : this.matrix.get(this.getNodeIndexOf(node))) {
    		// se la casella non è nulla 
    		if(entry != null) {
    			// aggiungo l'arco al set 
    			edges.add(entry);
    		}
    	}
    	return edges;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
    	if(label == null)
    		throw new NullPointerException(
    				"Tentativo di ricerca degli archi connessi ad un nodo a partire da un'etichetta nulla");
    	if(this.getNode(label) == null)
    		throw new IllegalArgumentException(
    				"Tentativo di ricerca degli archi connessi a un nodo non presente nel grafo a partire da un'etichetta");
        return this.getEdgesOf(this.getNode(label));
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
    	if(!this.nodesIndex.containsValue(i))
    		throw new IndexOutOfBoundsException(
    				"Tentativo di ricerca degli archi connessi a partire da un indice di nodo non valido");
    	return this.getEdgesOf(this.getNode(i));
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        // creo l'insieme in cui aggiungere gli archi del grafo
    	Set<GraphEdge<L>> edges = new HashSet<GraphEdge<L>>();
    	// per ogni riga della matrice
    	for(ArrayList<GraphEdge<L>> row : this.matrix) {
    		// per ogni casella della riga
    		for(GraphEdge<L> entry : row) {
    			// se la casella non è vuota
    			if(entry != null) {
    				// aggiungo l'arco al set 
    				edges.add(entry);
    			}
    		}
    	}
        return edges;
    }
    
    /**
     * Aggiunge una colonna vuota alla destra della matrice
     * di adiacenza.
     * 
     */
    private void addColumnToMatrix() {
    	// per ogni riga della matrice
		for(ArrayList<GraphEdge<L>> row : this.matrix) {
			// aggiungo un elemento null in coda 
			row.add(null); 
		}
    }
    
    /**
     * Aggiunge una riga vuota in fondo alla matrice
     * di adiacenza.
     * 
     */
    private void addRowToMatrix() {
    	// creo la lista rappresentate la riga 
		ArrayList<GraphEdge<L>> row = new ArrayList<GraphEdge<L>>();
		// la riempio di elementi null
		for(int i = 0; i < this.nodesIndex.size(); i++) {
			row.add(i, null);
		}
		// aggiungo la riga nella matrice
		this.matrix.add(row);  
    }
    
    /**
     * Rimuove la colonna con l'indice
     * specificato dalla matrice di adiacenza.
     * 
     * @param j
     *                 l'indice della colonna 
     *                 da rimuovere
     */
    private void removeColumnFromMatrix(int j) {
    	if(j < 0 || j > this.nodesIndex.size())
    		throw new IndexOutOfBoundsException( 
    				"Tentativo di rimuovere dalla matrice di adiacenza una colonna con indice non presente");
    	// per ogni riga della matrice
    	for(ArrayList<GraphEdge<L>> row : this.matrix) { 
    		// rimuovo la casella dell'indice della 
    		// colonna da rimuovere
    		row.remove(j);
    	}
    }
    
    /**
     * Dato un nodo del grafo, ne elimina la presenza dalla mappa
     * {@code nodesIndex}. Gli indici (dei nodi) superiori a quello
     * del nodo rimosso, saranno decrementati di una posizione.
     * 
     * @param node
     *              il nodo da rimuovere 
     * @return l'indice del nodo rimosso
     * @throws NullPointerException
     *                                      se il nodo passato è nullo
     * @throws IllegalArgumentException
     *                                      se il nodo passato non è 
     *                                      presente nella mappa                            
     */
    private int removeNodeFromMap(GraphNode<L> node) {
    	if(node == null)
    		throw new NullPointerException(
    				"Tentativo di rimuovere dalla mappa un nodo nullo");
    	if(!this.nodesIndex.containsKey(node))
    		throw new IllegalArgumentException(
    				"Tentativo di rimuovere dalla mappa un nodo non presente");
    	// rimuovo la entry del nodo dalla mappa e ne salvo l'indice
    	int index = this.nodesIndex.remove(node);
    	// per ogni nodo nella mappa
    	for(Map.Entry<GraphNode<L>, Integer> entry : nodesIndex.entrySet()) {
    		// se ha indice superiore all' indice di quello rimosso
    		if(entry.getValue() > index) {
    			// lo decremento di 1
    			entry.setValue(entry.getValue() - 1); 
    		}
    	}
    	return index; 
    }
}
