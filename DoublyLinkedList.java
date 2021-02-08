/**
 Program 1 - Index List, Positional List, and Circular Array Queue 
 This program, DoublyLinkedList.java, defines a set of methods, and with
 the use of a couple nested classes, allows us to define a doubly-linked-
 list. Each node contains a reference to the node before, and after it. 
 =============================================
  @author Justin Milliman
  Data Structures - CS2321 - R01 - Spring 2021
  Date last modified: 01/31/2021
 =============================================
 */
package cs2321;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.datastructures.Position;
import net.datastructures.PositionalList;


public class DoublyLinkedList<E> implements PositionalList<E> {

	// Keep track of the head, tail, and the size of the list
	private Node<E> head;
	private Node<E> tail;
	private int size = 0; // Always start empty



	// To start things off, I will make my own Node class because nobody told me I could not
	private static class Node<E> implements Position<E>{
		// Each Node should have a reference to itself, and its neighbors
		private Node<E> prev;
		private Node<E> next;
		private E element;

		// Constructor
		public Node(E e, Node<E> p, Node<E> n) {
			element = e;
			prev = p;
			next = n;
		}

		// Some helpful getters and setters
		public E getElement() throws IllegalStateException {
			if (next == null) throw new IllegalStateException("Position not valid");
			return element;
		}
		public Node<E> getPrev() { return prev; }
		public Node<E> getNext() { return next; }
		public void setElement(E e) { element = e; }
		public void setPrev (Node<E> e) { prev = e; }
		public void setNext (Node<E> e) { next = e; }

	}
	// Cool beans





	// The first thing I'm going to do is create a method that doesn't allow me to break this
	private Node<E> validate(Position<E> p) throws IllegalArgumentException {
		
		// If the position given does not point to a node, we don't want it
		if (!(p instanceof Node)) throw new IllegalArgumentException("Invalid p-p-position");
		Node<E> node = (Node<E>) p; // Grab Node
		
		// If the node is pointing to nothing, toss it out the window into the void of nothing
		if (node.getNext() == null) throw new IllegalArgumentException("This position is not on the list");
		return node;
	}

	
	private Position<E> position (Node<E> node) {
		if (node == head || node == tail) { return null; } // If its an edge, return null
		return node;
	}


	// Constructor [FUN FACT : I forgot to actually make this and got really sad when every test failed]
	public DoublyLinkedList() {
		head = new Node<>(null, null, null);
		tail = new Node<>(null, head, null);
		head.setNext(tail);
	}

	@Override
	public int size() { return size; }

	@Override
	public boolean isEmpty() { return size == 0; }

	@Override
	// The head node is always null, so get what comes after
	public Position<E> first() { return position(head.getNext()); }

	@Override
	// The tail is always null, so grab the previous
	public Position<E> last() { return position(tail.getPrev()); }

	@Override
	public Position<E> before(Position<E> p) throws IllegalArgumentException {
		Node<E> node = validate(p);			// Make sure its real
		return position(node.getPrev());
	}

	@Override
	public Position<E> after(Position<E> p) throws IllegalArgumentException {
		Node<E> node = validate(p);	
		return position(node.getNext());
	}

	/**
	 * 
	 * Please notice how it uses the parameters u, p, s and find joy
	 * 
	 * @param u : this is the node you want to stick in somewhere
	 * @param p : this is the first node, or the future previous node of the node you want to insert
	 * @param s : this is the second node, or the successor of the node you want to insert
	 * @return : the newly inserted node
	 */
	private Position<E> addBetween(E u, Node<E> p, Node<E> s) {
		Node<E> newNode = new Node<>(u, p, s);
		p.setNext(newNode);
		s.setPrev(newNode);
		size++;
		return newNode;
	}


	@Override
	// Insert the node after the head node [head is always null]
	public Position<E> addFirst(E e) { return addBetween (e, head, head.getNext()); }

	@Override
	// Place just before the tail node, since the tail is always null
	public Position<E> addLast(E e) { return addBetween (e, tail.getPrev(), tail); }

	@Override
	// Place the element e at the position p, and shift the other guys around to let him in
	public Position<E> addBefore(Position<E> p, E e) throws IllegalArgumentException {
		Node<E> node = validate(p);
		return addBetween(e, node.getPrev(), node);
	}

	@Override
	// Same thing as addBefore, except now you want him to be placed a position ahead
	public Position<E> addAfter(Position<E> p, E e) throws IllegalArgumentException {
		Node<E> node = validate(p);
		return addBetween(e, node, node.getNext());
	}

	@Override
	public E set(Position<E> p, E e) throws IllegalArgumentException {
		Node<E> node = validate(p);
		E n = node.getElement();
		node.setElement(e);
		return n;
	}

	@Override
	public E remove(Position<E> p) throws IllegalArgumentException {

		// Grab the "previous node" and the "next node" around the element we are trying to remove
		Node<E> node = validate(p);
		Node<E> pred = node.getPrev();
		Node<E> succ = node.getNext();

		// Point "previous node" and "next node" at each other
		pred.setNext(succ);
		succ.setPrev(pred);
		size--;

		// Turn the original node into a null node, Justin case somebody still tries to use it
		E n = node.getElement();
		node.setElement(null);
		node.setPrev(null);
		node.setNext(null);
		return n;
	}

	public E removeFirst() throws IllegalArgumentException {
		// Always remove the index at head
		return remove(head.getNext());
	}

	public E removeLast() throws IllegalArgumentException {
		// Always remove the index at tail
		return remove(tail.getPrev());
	}





	// So just to reiterate, we are going to create a nested iterator class, 
	// and a modified iterator of the iterator to use the iterator method. 
	// Glad we're on the same page :D
	
	
	private class PositionIterator implements Iterator<Position<E>> {
		private Position<E> focus = first();
		private Position<E> recent = null; // Nothing before yet, we haven't done anything

		public boolean hasNext() { return focus != null; }

		public Position<E> next() throws NoSuchElementException {
			if (focus == null) throw new NoSuchElementException("Nothing Left here boo");
			recent = focus;			// Save the previous location
			focus = after(focus); 	// Move to the next position
			return recent;			// Return the element we looked at
		}

		public void remove() throws IllegalStateException {
			if (recent == null) throw new IllegalStateException("Nothing To Remove sweetie");
			DoublyLinkedList.this.remove(recent); // Piggyback off existing remove methods
		}
	}

	// Allows the creation of the public instance
	private class PositionIterable implements Iterable<Position<E>> {
		public Iterator<Position<E>> iterator() { return new PositionIterator(); }
	}


	@Override
	// Create an instance of the inner nested class
	public Iterable<Position<E>> positions() { return new PositionIterable(); }


	
	// Now I create a modifier for the iterator class, 
	// this allows us to use Elements E instead of Positions
	private class ElementIterator implements Iterator<E> {
		Iterator<Position<E>> positionIterator = new PositionIterator();
		
		// Pretty much just use the other methods methods, and modify the output to be elements
		public boolean hasNext() { return positionIterator.hasNext(); }
		public E next() { return positionIterator.next().getElement(); }
		public void remove() { positionIterator.remove(); }
	}

	@Override
	// Make an instance of the ElementIterator
	public Iterator<E> iterator() {
		return new ElementIterator();
	}


}

















