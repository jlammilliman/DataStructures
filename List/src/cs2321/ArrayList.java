/**
 Program 1 - Index List, Positional List, and Circular Array Queue 
 In this program, ArrayList.java, we create a bunch of methods that help 
 to define an ArrayList class. These allow us to take any kind of data, 
 store it, and manipulate it 
 =============================================
  @author Justin Milliman
  Data Structures - CS2321 - R01 - Spring 2021
  Date last modified: 01/31/2021
 =============================================
 */
package cs2321;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.datastructures.List;

public class ArrayList<E> implements List<E> {

	// We start first with some lovely variables to make my life easier
	public static final int CAPACITY = 16;
	private E[] data;
	private int size = 0; 				// Always start empty
	private int currentCapacity = 0;	// Since she empty, there is no capacity yet


	public ArrayList() {
		this(CAPACITY);
	}
	public ArrayList(int capacity) {
		data = (E[]) new Object[capacity];
		currentCapacity = capacity;
	}


	// This method ensures that there are no bad index operations
	public void checkIndex(int i, int size) throws IndexOutOfBoundsException{
		if ( i >= currentCapacity) { resize(currentCapacity * 2); }
		if ( i < 0 || i >= size) throw new IndexOutOfBoundsException("Illegal Index: " + i);
	}

	@Override
	public int size() {	return size; }

	@Override
	public boolean isEmpty() { return size == 0; }

	@Override
	// Returns the element in the array if it exists
	public E get(int i) throws IndexOutOfBoundsException {
		checkIndex(i, size); // make sure it exists
		return data[i];
	}

	@Override
	// Replace the element at index i with the fed object e
	public E set(int i, E e) throws IndexOutOfBoundsException {
		checkIndex(i, size);
		E tempData = data[i];	// Make a copy
		data[i] = e;
		return tempData; 		// Return the copy
	}

	@Override
	// Add an object e at the specified index i
	public void add(int i, E e) throws IndexOutOfBoundsException {
		checkIndex(i, size + 1); 				// Be sure there is space
		for (int j = size - 1; j >= i; j--) { 	// Move all elements above i up
			data[j + 1] = data[j];
		}
		data[i] = e;	// Copy e to the appropriate index
		size++;

	}

	@Override
	// Sends the element at index i into the void, never to be seen again
	public E remove(int i) throws IndexOutOfBoundsException {
		checkIndex(i, size);
		E tempdata = data[i];
		for (int j = i; j < size - 1; j++) { // Move all elements above i down
			data[j] = data[j + 1];
		}
		data[size - 1] = null;	// Replace the duplicate element at the end with null
		size--;
		return tempdata; 		// return the deleted element
	}

	
	// Time to Iterate
	
	
	// A quick, cute little nested class
	private class ArrayIterator implements Iterator<E>{
		private int k = 0;
		private boolean removable = false;

		@Override
		public boolean hasNext() { return k < size; }

		@Override
		// If there is a next element, grab it, else throw a fit
		public E next() throws NoSuchElementException {
			if (k == size) throw new NoSuchElementException("No Next Element!!");
			removable = true;
			return data[k++];
		}

		// Piggyback of the arrayList remove method to delete elements
		public void remove() throws IllegalStateException {
			if (!removable) throw new IllegalStateException("Nothing Here To Remove Boo!!");
			ArrayList.this.remove(k - 1);
			k--;
			removable = false;
		}

	}

	@Override
	public Iterator<E> iterator() {
		return new ArrayIterator(); // Make a public instance of nested class
	}




	// Copy the elements from the old array to a new, bigger, better, more powerful(not really) array
	public void resize(int capacity) {
		E[] tempdata = (E[]) new Object[capacity];	// Make a new array with the appropriate amount of space
		for (int i = 0; i < size; i++) {			// Copy data to new tempArray
			tempdata[i] = data[i]; 					
		}
		data = tempdata;			// Make sure we actually save this
		currentCapacity = capacity;	// Update currentCapacity
	}

	public void addFirst(E e)  {
		// For this we will keep it super simple, just call the add method with index of 0
		add(0, e);
	}

	public void addLast(E e)  {
		// Same thing here, call add with index of the last element in the array
		add(size, e);
	}

	public E removeFirst() throws IndexOutOfBoundsException {
		// call remove with the index at 0, return the element 
		return remove(0);
	}

	public E removeLast() throws IndexOutOfBoundsException {
		// call remove at the index of the last element in the array
		return remove(size - 1); // Size is always +1 to element index
	}

	// Return the capacity of array, not the number of elements.
	// Notes: The initial capacity is 16. When the array is full, the array should be doubled. 
	public int capacity() {
		return currentCapacity;
	}

}
