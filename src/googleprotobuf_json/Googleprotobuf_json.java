/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googleprotobuf_json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import googleprotobuf_json.ResultProto.Result;
import googleprotobuf_json.ResultProto.StudentMsg;
import googleprotobuf_json.ResultProto.CourseMarks;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.NoSuchFileException;

/**
 *
 * @author chandan5
 */

class Course {
    String CourseName;
    int CourseScore;
    Course(String name, int score) {
        CourseName = name;
        CourseScore = score;
    }
}

class Student {
    String Name;
    int RollNo;
    Course[] CourseMarks;
    Student(String name, int rollno) {
        Name = name;
        RollNo = rollno;
    }
    Student(String name, int rollno, Course[] coursemarks) {
        Name = name;
        RollNo = rollno;
        CourseMarks = coursemarks;
    }
}

public class Googleprotobuf_json {

    /**
     * @param args the command line arguments
     */
    private static void writeStudentsToFile(Student[] students, String outputFileName) throws Exception {
        PrintWriter pw = new PrintWriter(new FileWriter(outputFileName));
        
        for(Student student: students) {
            String outputString = "";
            outputString += student.Name;
            outputString += ",";
            outputString += student.RollNo;
            for(Course course: student.CourseMarks) {
                outputString += ":";
                outputString += course.CourseName;
                outputString += ",";
                outputString += course.CourseScore;
            }
            pw.println(outputString);
        }
        pw.close();
    }
    
    private static void printStudents(Student[] students) {
        for(Student student: students) {
            String outputString = "";
            outputString += student.Name;
            outputString += ",";
            outputString += student.RollNo;
            for(Course course: student.CourseMarks) {
                outputString += ":";
                outputString += course.CourseName;
                outputString += ",";
                outputString += course.CourseScore;
            }
            System.out.println(outputString);
        }
    }
    
    private static Result getResultProtoFromStudents(Student[] students){
        Result.Builder result = Result.newBuilder();
        for(Student student: students) {
            StudentMsg.Builder studentMsg = StudentMsg.newBuilder();
            studentMsg.setName(student.Name);
            studentMsg.setRollNum(student.RollNo);
            for(Course course: student.CourseMarks) {
                CourseMarks.Builder courseMarks = CourseMarks.newBuilder();
                courseMarks.setName(course.CourseName);
                courseMarks.setScore(course.CourseScore);
                studentMsg.addMarks(courseMarks);
            }
            result.addStudentmsg(studentMsg);
        }
        return result.build();
    }
    
    private static Student[] parseStringToStudents(List<String> lines) {
        ArrayList<Student> studentsList = new ArrayList<Student>();
        
        for(String line: lines) {
            String[] nameAndTokens = line.split(",",2);
            String name;
            int rollno;
            ArrayList<Course> coursesList = new ArrayList<Course>();
            name = nameAndTokens[0];
            String[] rollnoAndTokens = nameAndTokens[1].split(":",2);
            rollno = Integer.parseInt(rollnoAndTokens[0]);
            
            if(rollnoAndTokens.length > 1) {
                String[] courseStrings = rollnoAndTokens[1].split(":");
                for(String courseString: courseStrings) {
                    String[] courseStringArray = courseString.split(",");
                    if(courseStringArray.length == 2) {
                        Course course = new Course(courseStringArray[0], Integer.parseInt(courseStringArray[1]));
                        coursesList.add(course);
                    }
                }
            }
                
            Course[] courses = coursesList.toArray(new Course[0]);
            Student student = new Student(name, rollno, courses);
            studentsList.add(student);
        }
        
        Student[] students = studentsList.toArray(new Student[0]);
        return students;
    }
    
    private static Student[] getStudentsFromResult(Result result) {
        ArrayList<Student> studentsList = new ArrayList<Student>();
        
        for(StudentMsg studentMsg: result.getStudentmsgList()) {
            String name = studentMsg.getName();
            int rollno = studentMsg.getRollNum();
            ArrayList<Course> coursesList = new ArrayList<Course>();

            for(CourseMarks courseMarks: studentMsg.getMarksList()) {
                Course course = new Course(courseMarks.getName(), courseMarks.getScore());
                coursesList.add(course);
            }
            
            Course[] courses = coursesList.toArray(new Course[0]);
            Student student = new Student(name, rollno, courses);
            studentsList.add(student);
        }
        Student[] students = studentsList.toArray(new Student[0]);
        return students;
    }
    
    public static void main(String[] args) throws IOException {
        // TODO Use options library http://stackoverflow.com/questions/367706/how-to-parse-command-line-arguments-in-java
        if(args.length != 3) {
            System.err.println("Format: -<s/d/t> -<p/j> file_name");
            return;
        }
        System.err.println(Arrays.toString(args));
        int serialise = -1;
        int protobuf = -1;
        String s = "-s";
        String d = "-d";
        String t = "-t";
        String p = "-p";
        String j = "-j";
        if(s.equals(args[0])) {
            serialise = 1;
        }
        else if(d.equals(args[0])) {
            serialise = 0;
        }
        else if(t.equals(args[0])) {
            serialise = 2;
        }
        if(p.equals(args[1])) {
            protobuf = 1;
        }
        else if(j.equals(args[1])) {
            protobuf = 0;
        }
        
        if(serialise == -1 || protobuf == -1) {
            System.err.println("Format: -<s/d/t> -<p/j> file_name");
            return;
        }
        
        try {
            
            String outputFileName = "";
            String inputFileName = args[2];
            if((serialise == 1) && (protobuf == 0)) {
                outputFileName = "result.json";
            }
            else if((serialise == 1) && (protobuf == 1)) {
                outputFileName = "result_protobuf";
            }
            else if((serialise == 0) && (protobuf == 0)) {
                outputFileName = "output_json.txt";
            }
            else if((serialise == 0) && (protobuf == 1)) {
                outputFileName = "output_protobuf.txt";
            }
            
            Gson gson = new GsonBuilder().create();
            if(serialise == 1) {
                List<String> lines = Files.readAllLines(Paths.get(inputFileName), StandardCharsets.UTF_8);
                Student[] students = parseStringToStudents(lines);
                
                if(protobuf == 0)     {
                    String jsonStudents = gson.toJson(students);
                    PrintWriter writer = new PrintWriter(outputFileName, "UTF-8");
                    writer.print(jsonStudents);
                    writer.close();
//                     TODO Check why below code is not working
//                     gson.toJson(students, new FileWriter(outputFileName));
                }
                else {
                    Result result = getResultProtoFromStudents(students);
                    byte[] myByteArray = result.toByteArray();
                    FileOutputStream fos = new FileOutputStream(outputFileName);
                    fos.write(myByteArray);
                    fos.close();
                }
            }
            else if(serialise == 0){
                if(protobuf == 0) {
                    Student[] students = gson.fromJson(new FileReader(inputFileName), Student[].class);
                    writeStudentsToFile(students, outputFileName);
                }
                else {
                    Result result = Result.parseFrom(new FileInputStream(inputFileName));
                    Student[] students = getStudentsFromResult(result);
                    writeStudentsToFile(students, outputFileName);
                }
            }
            else if(serialise == 2) { // time measurements
                File file =new File(inputFileName);
		double inputKBs = 0;
                double marshalledKbs = 0;
                if(file.exists()){
                    inputKBs = file.length()/1024;
                }
                else {
                    System.err.println("Error: No such file exists. Kindly enter correct file path.");
                    return;
                }
                List<String> lines = Files.readAllLines(Paths.get(inputFileName), StandardCharsets.UTF_8);
                Student[] students = parseStringToStudents(lines);
                long startSerialisation, stopSerialisation, startDeserialisation, stopDeserialisation;
                if(protobuf == 0) {
                    startSerialisation = System.nanoTime();
                    String jsonStudents = gson.toJson(students);
                    stopSerialisation = System.nanoTime();
                    byte[] marshalledBytes = jsonStudents.getBytes("UTF-8");
                    marshalledKbs = (marshalledBytes.length)/1024;
                    startDeserialisation = System.nanoTime();
                    gson.fromJson(jsonStudents, Student[].class);
                    stopDeserialisation = System.nanoTime();
                }
                else {
                    startSerialisation = System.nanoTime();
                    Result result = getResultProtoFromStudents(students);
                    byte[] marshalledBytes = result.toByteArray();
                    stopSerialisation = System.nanoTime();
                    marshalledKbs = (marshalledBytes.length)/1024;
                    startDeserialisation = System.nanoTime();
                    result = Result.parseFrom(marshalledBytes);
                    getStudentsFromResult(result);
                    stopDeserialisation = System.nanoTime();
                }
                long marshallingTime = (stopSerialisation-startSerialisation)/1000000;
                long demarshallingTime = (stopDeserialisation-startDeserialisation)/1000000;
                long marshallingRate = (long)(inputKBs*1000)/marshallingTime;
                long demarshallingRate = (long)(marshalledKbs*1000)/demarshallingTime;
                System.out.println("In put File Size: " + inputKBs + " kB");
                System.out.println("Size after marshalling: " + marshalledKbs + " kB");
                System.out.println("Time taken for marshalling: " + marshallingTime + " ms");
                System.out.println("Time taken for demarshalling: " + demarshallingTime + " ms");
                System.out.println("Rate of marshalling: " + marshallingRate + " kb/s");
                System.out.println("Rate of demarshalling: " + demarshallingRate + " kb/s");
                //            System.out.println((y-x)/1000000);
            }

        }
        catch (NoSuchFileException e) {
            System.err.println("Error: No such file exists. Kindly enter correct file path.");
        }
        catch (IOException e) {
            System.err.println("Error : Reading/Writing Error. Please check permissons of file.");
        }
        catch (Exception e) {
            System.err.println("Below mentioned error occured.");
            System.err.println(e);
        }
    }
    
}
