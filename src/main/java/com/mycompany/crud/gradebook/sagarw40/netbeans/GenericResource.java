/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.crud.gradebook.sagarw40.netbeans;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import model.Student;
import model.WorkItem;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * REST Web Service
 *
 * @author NAME
 */
@Path("GradeBook")
public class GenericResource {

    @Context
    private UriInfo context;
    private static ArrayList<Student> students = new ArrayList<>();
    private static HashMap studentNames = new HashMap();
    private static HashMap taskNames = new HashMap(); //change
    private static Random randomGenerator=new Random(1234567890);
    private Response response;
//    File file;
    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() throws IOException {
//        response = Response.status(Response.Status.CREATED).build();
//     try{
//        ObjectMapper objectMapper = new ObjectMapper();
//        
//        this.file = new File("/Users/Shubham/NetBeansProjects/CRUD-GradeBook-sagarw40-Netbeans/Data/studentGrades.json");
//        
//        this.students = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Student.class));
//        
//        studentNames = new HashMap();
//        for(Student temp: students) studentNames.put(temp.getName(),temp.getId());
//        
//        taskNames = new HashMap(); //change
//        for(WorkItem wi : students.get(0).getWorkItems()) taskNames.put(wi.getTitle(), null); //change
//     }catch(Exception e){
//         response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Empty Gradebook!").build();
//     }
         
    }

    /**
     * Retrieves representation of an instance of com.mycompany.crud.gradebook.sagarw40.netbeans.GenericResource
     * @return an instance of java.lang.String
     */
    
    
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudents() throws IOException{
        URI uri;
        Response response;
        if(!students.isEmpty()){
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
            //return mapper.writeValueAsString(students);
            uri = URI.create("http://localhost:8080/CRUD-GradeBook-sagarw40-Netbeans/webresources/GradeBook");
            response = Response.status(Response.Status.OK).entity(mapper.writeValueAsString(students)).location(uri).build();
        }
        else{
            uri = URI.create("");
            response = Response.status(Response.Status.NO_CONTENT).entity("no student in the gradebook").location(uri).build();
        }
        return response;
    }
    
    @GET
    @Path("Students")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getStudentNames() throws IOException{
       
        String output="";
//        if(!students.isEmpty()){
            Set names = studentNames.keySet();
            Collection ids = studentNames.values();
            Iterator<Integer> itId = ids.iterator();
            if(names.isEmpty()){
                output = "There is no student in the gradebook yet!";
                response = Response.status(Response.Status.NO_CONTENT).entity(output).build();
            }
            else{
                Iterator<String> it = names.iterator();
                output = output+"NAME     -      ID\n\n";
                while(it.hasNext()){
                    output = output+it.next()+"  -  "+itId.next()+"\n";
                }
                response = Response.status(Response.Status.OK).entity(output).build();
            }
//        }
        return response;
    }
    
    @GET
    @Path("Tasks")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getTaskNames() throws IOException{
        Response response;
        URI uri;
        String output="";
        Set names = taskNames.keySet();
        if(names.isEmpty()){
            uri = URI.create("");
            response = Response.status(Response.Status.OK).entity("There is no task in the gradebook yet!").location(uri).build();
            return response;
        }
        Iterator<String> it = names.iterator();
        while(it.hasNext()){
            output = output+it.next()+"\n";
        }
        uri = URI.create("http://localhost:8080/CRUD-GradeBook-sagarw40-Netbeans/webresources/GradeBook/Tasks");
            response = Response.status(Response.Status.OK).entity(output).location(uri).build();
        return response;
    }
    
    @GET
    @Path("Tasks/{taskName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getStudentsInfoOnTaskName(@PathParam("taskName") String taskName) throws IOException{
        String output = "";
        if(taskNames.containsKey(taskName)){
            for(Student tempStudent: students){
                output = output+"student ID : "+tempStudent.getId()+"\n"+"student Name : "+tempStudent.getName()+"\n";
                for(WorkItem wi: tempStudent.getWorkItems()){
                    if(taskName.equals(wi.getTitle())){
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
                        output = output+mapper.writeValueAsString(wi)+"\n\n" + "---------------------------------" + "\n\n";
                        break;
                    }
                }
            }
            response = Response.status(Response.Status.OK).entity(output).build();
        }
        else response = Response.status(Response.Status.NOT_FOUND).entity("This task doesnt exist in the gradebook").build();
        return response;
    }
    
    @GET
    @Path("Students/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getStudent(@PathParam("id") String id) throws IOException{
        URI uri;
        
        if(!studentNames.containsValue(Integer.parseInt(id))){ 
            uri = URI.create("");
            response = Response.status(Response.Status.NOT_FOUND).entity("there is no student with this ID in gradebook").location(uri).build();
        }
        else{
            for(Student tempStudent: students ){
                    //find the student that matches the put id
                    if(tempStudent.getId()==Integer.parseInt(id)){
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
                        uri = URI.create("http://localhost:8080/CRUD-GradeBook-sagarw40-Netbeans/webresources/GradeBook/Students/"+id);
                        response = Response.status(Response.Status.OK).entity(mapper.writeValueAsString(tempStudent)).location(uri).build();
                    }
            }
        }
        return response;
    }
    
    @GET
    @Path("Students/{id}/{workString}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getStudent(@PathParam("id") String id, @PathParam("workString") String workString) throws IOException{
        URI uri;
        if(!studentNames.containsValue(Integer.parseInt(id))){ 
            uri = URI.create("");
            response = Response.status(Response.Status.NOT_FOUND).entity("there is no student with this ID in gradebook").location(uri).build();
        }
        else if(!taskNames.containsKey(workString)){
            uri = URI.create("");
            response = Response.status(Response.Status.NOT_FOUND).entity("there is no such task in gradebook").location(uri).build();
        }
        else{
            for(Student tempStudent: students ){
                    //find the student that matches the put id
                    if(tempStudent.getId()==Integer.parseInt(id)){

                        for(WorkItem wi : tempStudent.getWorkItems()){

                            if(wi.getTitle().equals(workString)){
                                ObjectMapper mapper = new ObjectMapper();
                                mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
                                uri = URI.create("http://localhost:8080/CRUD-GradeBook-sagarw40-Netbeans/webresources/GradeBook/Students/"+id+"/"+workString);
                                response = Response.status(Response.Status.OK).entity(mapper.writeValueAsString(wi)).location(uri).build();
                                
                            }

                        }
                        break;
                        //if(workCount==tempStudent.getWorkItems().size()) return "this work item doesnt exist for the student";
                    }
            }
        }
        return response;
    }
    
    @POST
    @Path("Tasks/{incomingData}")
    public Response addGradeBookTask(@PathParam("incomingData") String incomingData) throws IOException{
        URI uri;
//        if(response.getStatus()!=500){
            try{
                if(taskNames.containsKey(incomingData)){ 
                    uri = URI.create("");
                    response = Response.status(Response.Status.CONFLICT).entity("this task already exists in the gradebook").location(uri).build();
                    return response;
                }
                taskNames.put(incomingData, null);
                for(Student tempStudent: students){
                    WorkItem wi = new WorkItem();
                    wi.setItemId(0);
                    wi.setTitle(incomingData);
                    wi.setGrade(0);
                    wi.setFeedback("");
                    wi.setPercentage(0);
                    tempStudent.getWorkItems().add(wi);
                }
//                ObjectMapper mapper = new ObjectMapper();
//                FileWriter fw = new FileWriter(file.getAbsoluteFile());
//                BufferedWriter bw = new BufferedWriter(fw);
//                bw.write("[]");
//                bw.close();
//
//                PrintWriter writer = new PrintWriter(file);
//                writer.print("");
//                mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
//                mapper.writeValue(file, this.students);
                uri = URI.create("http://localhost:8080/CRUD-GradeBook-sagarw40-Netbeans/webresources/GradeBook/Tasks/"+incomingData);
                response = Response.status(Response.Status.CREATED).entity("Task Created in the Gradebook").location(uri).build();
            }catch (RuntimeException e) {
                   response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Problem with http parameter").build();
             }
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        FileWriter fw = new FileWriter(file.getAbsoluteFile());
//        BufferedWriter bw = new BufferedWriter(fw);
//        bw.write("[]");
//        bw.close();
//
//        PrintWriter writer = new PrintWriter(file);
//        writer.print("");
//        mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
//        mapper.writeValue(file, this.students);
        
        return response;
    }
    
    @POST
    @Path("Students/{incomingData}")
    public Response addStudentData(@PathParam("incomingData") String incomingData) throws IOException {
//        if(response.getStatus()!=500){
            try{
                
                ObjectMapper mapper = new ObjectMapper();
                Student student = new Student();
                student.setName(incomingData);
                int studentId = Math.abs(randomGenerator.nextInt(1000));
                student.setId(studentId);
                student.setWorkItems(new ArrayList<WorkItem>());
                studentNames.put(incomingData, studentId);
                WorkItem wi;
                Set names = taskNames.keySet();
                Iterator<String> it = names.iterator();
                while(it.hasNext()){
                    wi=new WorkItem();
                    wi.setItemId(0);
                    wi.setTitle(it.next());
                    wi.setFeedback("");
                    wi.setGrade(0);
                    wi.setPercentage(0);
                    student.getWorkItems().add(wi);
                }
                students.add(student);

//                FileWriter fw = new FileWriter(file.getAbsoluteFile());
//                BufferedWriter bw = new BufferedWriter(fw);
//                bw.write("[]");
//                bw.close();
//
//                PrintWriter writer = new PrintWriter(file);
//                writer.print("");
//                mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
//                mapper.writeValue(file, this.students);

                URI uri = URI.create("http://localhost:8080/CRUD-GradeBook-sagarw40-Netbeans/webresources/GradeBook/Students/"+studentId);
                response = Response.status(Response.Status.CREATED).entity("New Student entry created in the gradebook").location(uri).build();
            }catch(Exception e){
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server Error").build();
            }
            
//        }
        return response;
        
    }
    
    @POST
    @Path("Students/{id}/{workString}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudentAssignment(@PathParam("id") String id, @PathParam("workString") String workString, String incomingData) throws IOException {
        URI uri;
//        if(response.getStatus()!=500){
            if(!taskNames.containsKey(workString)){
                uri= URI.create("");
                response = Response.status(Response.Status.NOT_FOUND).entity("This task does not exist in the gradebook(creating a task for a student is equivalent to updating!)").location(uri).build();
            }
            else{
                ObjectMapper mapper = new ObjectMapper();
                try{
                    WorkItem workItem = mapper.readValue(incomingData, WorkItem.class);
                    workItem.setTitle(workString);
                    

                    //if id doesnt exist, return error
                    if(!studentNames.containsValue(Integer.parseInt(id))){ 
                        uri = URI.create("");
                        response = Response.status(Response.Status.NOT_FOUND).entity("This student does not exist in the gradebook!").location(uri).build();
                    }
                    else{
                        for(Student tempStudent: students ){
                            //find the student that matches the put id
                            if(tempStudent.getId()==Integer.parseInt(id)){
                                int workCount=0;
                                //if assignment for the student already exists, return error
                                for(WorkItem wi : tempStudent.getWorkItems()){
                                    
                                    if(wi.getTitle().equals(workString)){
                                        
                                        if(workItem.getGrade()!=-1)
                                            wi.setGrade(workItem.getGrade());
                                        else workItem.setGrade(wi.getGrade());
                                        if(!workItem.getFeedback().equals("NA"))
                                            wi.setFeedback(workItem.getFeedback());
                                        else workItem.setFeedback(wi.getFeedback());
                                        if(workItem.getPercentage()!=-1)
                                            wi.setPercentage(workItem.getPercentage());
                                        else workItem.setPercentage(wi.getPercentage());
                                        break;
                                    }
                                    workCount++;
                                }
                                if(workCount==tempStudent.getWorkItems().size()) response = Response.status(Response.Status.NOT_FOUND).entity("This task does not exist in the gradebook(creating a task for a student is equivalent to updating!)").build();


                            }
                        }
//                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
//                    BufferedWriter bw = new BufferedWriter(fw);
//                    bw.write("[]");
//                    bw.close();
//
//                    PrintWriter writer = new PrintWriter(file);
//                    writer.print("");
//                    mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
//                    mapper.writeValue(file, this.students);

                    mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
                    incomingData = mapper.writeValueAsString(workItem);

                    uri = URI.create("http://localhost:8080/CRUD-GradeBook-sagarw40-Netbeans/webresources/GradeBook/Students/"+id+"/"+workString);
                    response = Response.status(Response.Status.CREATED).entity("Student's task created(updated technically) with given values!").location(uri).build();
                    }


                    

                }
                catch (Exception e) { 
                    uri = URI.create("");
                    response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("server error").location(uri).build();
                }

            }
//        }
        return response;
        
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     */
    @PUT
    @Path("Students/{id}/{workString}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response changeStudentData(@PathParam("id") String id, @PathParam("workString") String workString, String incomingData) {
        URI uri;
//        if(response.getStatus()!=500){
            if(!taskNames.containsKey(workString)){
                uri= URI.create("");
                response = Response.status(Response.Status.NOT_FOUND).entity("This task does not exist in the gradebook(creating a task for a student is equivalent to updating!)").location(uri).build();
            }
            else{
                ObjectMapper mapper = new ObjectMapper();
                try{
                    WorkItem workItem = mapper.readValue(incomingData, WorkItem.class);
                    workItem.setTitle(workString);
                    

                    //if id doesnt exist, return error
                    if(!studentNames.containsValue(Integer.parseInt(id))){ 
                        uri = URI.create("");
                        response = Response.status(Response.Status.NOT_FOUND).entity("This student does not exist in the gradebook!").location(uri).build();
                    }
                    else{
                        for(Student tempStudent: students ){
                            //find the student that matches the put id
                            if(tempStudent.getId()==Integer.parseInt(id)){
                                int workCount=0;
                                //if assignment for the student already exists, return error
                                for(WorkItem wi : tempStudent.getWorkItems()){
                                    
                                    if(wi.getTitle().equals(workString)){
                                        
                                        if(workItem.getGrade()!=-1)
                                            wi.setGrade(workItem.getGrade());
                                        else workItem.setGrade(wi.getGrade());
                                        if(!workItem.getFeedback().equals("NA"))
                                            wi.setFeedback(workItem.getFeedback());
                                        else workItem.setFeedback(wi.getFeedback());
                                        if(workItem.getPercentage()!=-1)
                                            wi.setPercentage(workItem.getPercentage());
                                        else workItem.setPercentage(wi.getPercentage());
                                        break;
                                    }
                                    workCount++;
                                }
                                if(workCount==tempStudent.getWorkItems().size()) response = Response.status(Response.Status.NOT_FOUND).entity("This task does not exist in the gradebook(creating a task for a student is equivalent to updating!)").build();


                            }
                        }
//                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
//                        BufferedWriter bw = new BufferedWriter(fw);
//                        bw.write("[]");
//                        bw.close();
//
//                        PrintWriter writer = new PrintWriter(file);
//                        writer.print("");
//                        mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
//                        mapper.writeValue(file, this.students);
//
//                        mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
//                        incomingData = mapper.writeValueAsString(workItem);

                        uri = URI.create("http://localhost:8080/CRUD-GradeBook-sagarw40-Netbeans/webresources/GradeBook/Students/"+id+"/"+workString);
                        response = Response.status(Response.Status.OK).entity("Student's task updated!").location(uri).build();
                    }


                    

                }
                catch (Exception e) { response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("server error").build();}

            }
//        }
        return response;
    }
    
    @DELETE
    @Path("Students/{id}/{workString}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteStudentData(@PathParam("id") String id, @PathParam("workString") String workString) throws IOException{
        URI uri;
//        if(response.getStatus()!=500){
            if(!taskNames.containsKey(workString)){
                uri= URI.create("");
                response = Response.status(Response.Status.NOT_FOUND).entity("This task does not exist in the gradebook(creating a task for a student is equivalent to updating!)").location(uri).build();
            }
            else{
                ObjectMapper mapper = new ObjectMapper();
                try{
                    //if id doesnt exist, return error
                    if(!studentNames.containsValue(Integer.parseInt(id))){ 
                        uri = URI.create("");
                        response = Response.status(Response.Status.NOT_FOUND).entity("This student does not exist in the gradebook!").location(uri).build();
                    }
                    else{
                        for(Student tempStudent: students ){
                            //find the student that matches the put id
                            if(tempStudent.getId()==Integer.parseInt(id)){
                                int workCount=0;
                                //if assignment for the student already exists, return error
                                for(WorkItem wi : tempStudent.getWorkItems()){
                                    
                                    if(wi.getTitle().equals(workString)){
                                        wi.setGrade(0);
                                        wi.setFeedback("");
                                        wi.setPercentage(0);
                                        
                                        break;
                                    }
                                    workCount++;
                                }
                                if(workCount==tempStudent.getWorkItems().size()) response = Response.status(Response.Status.NOT_FOUND).entity("This task does not exist in the gradebook(creating a task for a student is equivalent to updating!)").build();


                            }
                        }
//                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
//                        BufferedWriter bw = new BufferedWriter(fw);
//                        bw.write("[]");
//                        bw.close();
//
//                        PrintWriter writer = new PrintWriter(file);
//                        writer.print("");
//                        mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
//                        mapper.writeValue(file, this.students);

                        

                        uri = URI.create("http://localhost:8080/CRUD-GradeBook-sagarw40-Netbeans/webresources/GradeBook/Students/"+id+"/"+workString);
                        response = Response.status(Response.Status.NO_CONTENT).entity("Student's grades for this assignment have been deleted!").location(uri).build();
                    }


                    

                }
                catch (Exception e) { response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("server error").build();}

            }
//        }
        return response;
        
        
    }
    
    @DELETE
    @Path("Tasks/{taskName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteTask(@PathParam("taskName") String taskName) throws IOException{
//        if(response.getStatus()!=500){
            if(taskNames.containsKey(taskName)){
                taskNames.remove(taskName, null);
                for(Student tempStudent: students){
                    for(WorkItem wi: tempStudent.getWorkItems()){
                        if(wi.getTitle().equals(taskName)){
                            tempStudent.getWorkItems().remove(wi);
                            break;
                        }
                    }
                }


                response = Response.status(Response.Status.NO_CONTENT).entity(taskName+" has been removed from the gradebook").build();

            }
            else{
                response = Response.status(Response.Status.NOT_FOUND).entity("this task doesnt exist in the gradebook").build();

            }
//        }
        return response;
    }
    
    
}
