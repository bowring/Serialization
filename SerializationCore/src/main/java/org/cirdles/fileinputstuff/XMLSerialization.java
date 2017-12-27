/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cirdles.fileinputstuff;

/**
 *
 * @author RyanBarrett
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import org.dom4j.Document;
import org.dom4j.Node;
import java.util.List;
import org.dom4j.io.*;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import java.io.FileOutputStream;

public class XMLSerialization {
    
    public static void main(String[] args) {
        ArrayList<Person> list;
        Scanner scan = new Scanner(System.in);
        try{
            File file = new File("XML");
            list = getList(file);
            System.out.println("Current people on file: ");
            for(int i = 0; i < list.size(); i++)
                System.out.println(list.get(i));
            getPeople("", scan, list);
            makeList(file, list);
        }catch(Exception e){
            System.out.println("Something went wrong " + e.getMessage());
        }
    }

    //returns list of persons
    public static ArrayList<Person> getList(File file) throws Exception {
        
        ArrayList<Person> list = new ArrayList<Person>();
        try{
        SAXReader saxReader = new SAXReader();
        Document doc = saxReader.read(file);
        List<Node> nodes = doc.selectNodes("People");
        for(Node node: nodes)
            list.add(new Person(node.selectSingleNode("first").getText(), node.selectSingleNode("last").getText(), node.selectSingleNode("dob").getText()));
        }catch(Exception e){
            System.out.println("Something went wrong with doc stuff");
        }
        return list;
    }

    //makes list of persons on file
    public static void makeList(File file, ArrayList<Person> list) throws Exception {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("people");
        for(int i = 0; i < list.size(); i++)
        {
            Element sub = root.addElement("person");
            sub.addElement("first").addText(list.get(i).getFirstName());
            sub.addElement("last").addText(list.get(i).getLastName());
            sub.addElement("dob").addText(list.get(i).getDOB());
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
        writer.write(doc);
    }

    //gets more people from user
    public static void getPeople(String entry, Scanner userInput, ArrayList<Person> list){
        do{            
            if(entry.equals("a"))
                for(int i = 0; i < list.size(); i++)
                    System.out.println(list.get(i));
            else if(!entry.equals(""))
                list.add(getPerson(entry));
            System.out.println("To add new people enter people in the form \"first name, last name, DOB\", enter q to quit,");
            System.out.println("or to  see current people on file enter a:" );
            entry = userInput.nextLine();
        }while(!entry.equals("q"));
    }
    
    //creates a person from a line of code
    public static Person getPerson(String entry) {
        String firstName = entry.substring(0, entry.indexOf(",")).trim();
        String lastName = entry.substring(entry.indexOf(",") + 1, entry.lastIndexOf(",")).trim();
        String DOB = entry.substring(entry.lastIndexOf(",") + 1).trim();
        return new Person(firstName, lastName, DOB);
    }
}
