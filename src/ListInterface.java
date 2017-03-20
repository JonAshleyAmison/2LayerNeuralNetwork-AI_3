//Interface for the ADT list. Entries in the list have positions that begin with 1.
public interface ListInterface {
	public boolean add(Object newEntry);
    //adds a new entry to the end of the list

	public boolean add(int newPosition, Object newEntry);
    //adds a new entry at a specified position within the list

	public Object remove(int givenPosition);
    //removes the entry at a given position from the list

	public void clear();
    //removes all entries from the list

	public boolean replace(int givenPosition, Object newEntry);
    //replaces the entry at a given position in the list

	public Object getEntry(int givenPosition);
    //retrieves the entry at a given position in the list

	public boolean contains(Object anEntry);
    //determines whether the list contains a given entry

	public int getLength();
    //returns the length of the list

	public boolean isEmpty();
    //determines if the list is empty

	public boolean isFull();
    //determine if the list is full

	public void display();
    //display all entries that are in the list
}