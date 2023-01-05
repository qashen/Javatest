package org.example;
import java.util.ArrayList;
import java.util.LinkedList;

public class LinkedListExample {
    public static void main (String[] args){
        LinkedList<String> namesLinkedList = new LinkedList<>();
        namesLinkedList.add ("John");
        namesLinkedList.add ("Paul");
        namesLinkedList.add ("George");
        namesLinkedList.add ("Ringo");

        for  (int i=0; i< namesLinkedList.size(); i++)
        {
            System.out.print(namesLinkedList.get(i) + " ");

        }
        ArrayList<String> namesArrayList = new ArrayList<>();
        namesArrayList.add ("John");
        namesArrayList.add ("Paul");
        namesArrayList.add ("George");
        namesArrayList.add ("Ringo");
    }
}
