/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author NAME
 */

public class Student {
    int id;
    String name;
    ArrayList<WorkItem> workItems;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<WorkItem> getWorkItems() {
        return workItems;
    }

    public void setWorkItems(ArrayList<WorkItem> workItems) {
        this.workItems = workItems;
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", name=" + name + ", workItems=" + workItems + '}';
    }
    
}
