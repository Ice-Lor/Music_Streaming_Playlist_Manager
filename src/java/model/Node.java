package model;

public class Node {
    public Song data;
    public Node next;
    public Node prev;

    public Node(Song song) {
        this.data = song;
        this.next = null;
        this.prev = null;
    }
}
