/**
 Program 1 - Index List, Positional List, and Circular Array Queue 
 In this program, we define a CircularArrayQueue.java class that allows us to create,
 and manipulate the elements. The queue exists using a set capacity, and loops 
 through to add and remove elements. 
 =============================================
  @author Justin Milliman
  Data Structures - CS2321 - R01 - Spring 2021
  Date last modified: 01/31/2021
 =============================================
 */
package cs2321;

import net.datastructures.Queue;


public class CircularArrayQueue<E> implements Queue<E> {
	private int size = 0; 	// Tank starts empty
	private int fi = 0; 	// Start at index 0
	private E[] data;


	public CircularArrayQueue(int queueSize) { data = (E[]) new Object[queueSize]; }

	@Override
	// Return the current Size of the array
	public int size() { return size; }

	@Override
	// Return a true or false if the array is empty or not, true = empty
	public boolean isEmpty() {return size == 0; }

	@Override
	// Adds an element e to the queue at the current focus index
	public void enqueue(E e) throws IllegalStateException{
		if (size ==  data.length) throw new IllegalStateException("Queue full!!");
		int availableSlot = (fi + size) % data.length;
		data[availableSlot] = e;
		size++;
	}

	@Override
	// Returns the first element in the array if it is not empty
	public E first() {
		if (isEmpty()) { return null; }
		return data[fi];
	}

	@Override
	// Remove the current focus index from the queue
	public E dequeue() {
		if (isEmpty()) { return null; }
		E e = data[fi];				 // Copy what's in data
		data[fi] = null;			 // Don't litter
		fi = (fi + 1) % data.length; // Move index
		size--;
		return e;					 // Return the element we popped from data
	}
}











