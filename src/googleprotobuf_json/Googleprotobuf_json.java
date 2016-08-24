/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googleprotobuf_json;

import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import googleprotobuf_json.ResultProto.Result;
import googleprotobuf_json.ResultProto.StudentMsg;
import googleprotobuf_json.ResultProto.CourseMarks;
import java.io.FileOutputStream;

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
            
//            if(nameAndTokens.length > 0) {
                name = nameAndTokens[0];
//            }
//            if(nameAndTokens.length > 1) { 
                String[] rollnoAndTokens = nameAndTokens[1].split(":",2);
//                if(rollnoAndTokens.length > 0)
                    rollno = Integer.parseInt(rollnoAndTokens[0]);
//                System.out.print("rollnoAndTokens: ");
//                System.out.println(Arrays.toString(rollnoAndTokens));
                if(rollnoAndTokens.length > 1) {
                    String[] courseStrings = rollnoAndTokens[1].split(":");
//                    System.out.print("courseStrings: ");
//                    System.out.println(Arrays.toString(courseStrings));
                    for(String courseString: courseStrings) {
//                        System.out.println(courseString);
                        String[] courseStringArray = courseString.split(",");
//                        System.out.println(Arrays.toString(courseStringArray));
                        if(courseStringArray.length == 2) {
                            Course course = new Course(courseStringArray[0], Integer.parseInt(courseStringArray[1]));
                            coursesList.add(course);
                        }
                    }
                }
//            }
            Course[] courses = coursesList.toArray(new Course[0]);
            Student student = new Student(name, rollno, courses);
            studentsList.add(student);
        }
        Student[] students = studentsList.toArray(new Student[0]);
        return students;
    }
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        // TODO Ordered Dictionary
//        try (Scanner scanner = new Scanner(System.in)) {
//            String myString = scanner.next();
//        }
        
//        String ss = "[{\"Name\":\"JANICE\",\"CourseMarks\":[{\"CourseScore\":54,\"CourseName\":\"cs125\"},{\"CourseScore\":73,\"CourseName\":\"me420\"},{\"CourseScore\":65,\"CourseName\":\"me119\"},{\"CourseScore\":44,\"CourseName\":\"cs431\"},{\"CourseScore\":67,\"CourseName\":\"cs481\"}],\"RollNo\":202817265},{\"Name\":\"JACK\",\"CourseMarks\":[{\"CourseScore\":42,\"CourseName\":\"ec904\"},{\"CourseScore\":69,\"CourseName\":\"me420\"},{\"CourseScore\":47,\"CourseName\":\"cs481\"},{\"CourseScore\":35,\"CourseName\":\"ec224\"},{\"CourseScore\":82,\"CourseName\":\"cs125\"},{\"CourseScore\":89,\"CourseName\":\"cs431\"},{\"CourseScore\":53,\"CourseName\":\"cs570\"},{\"CourseScore\":90,\"CourseName\":\"me119\"},{\"CourseScore\":40,\"CourseName\":\"cs565\"}],\"RollNo\":202817266},{\"Name\":\"WARREN\",\"CourseMarks\":[{\"CourseScore\":82,\"CourseName\":\"ec904\"}],\"RollNo\":202817267},{\"Name\":\"OSCAR\",\"CourseMarks\":[{\"CourseScore\":55,\"CourseName\":\"cs565\"},{\"CourseScore\":87,\"CourseName\":\"me119\"}],\"RollNo\":202817268},{\"Name\":\"BEATRICE\",\"CourseMarks\":[{\"CourseScore\":65,\"CourseName\":\"cs570\"},{\"CourseScore\":43,\"CourseName\":\"me119\"},{\"CourseScore\":57,\"CourseName\":\"ec224\"},{\"CourseScore\":36,\"CourseName\":\"ci325\"},{\"CourseScore\":33,\"CourseName\":\"cs125\"},{\"CourseScore\":74,\"CourseName\":\"cs431\"},{\"CourseScore\":81,\"CourseName\":\"me420\"},{\"CourseScore\":55,\"CourseName\":\"cs565\"}],\"RollNo\":202817269},{\"Name\":\"JEROME\",\"CourseMarks\":[{\"CourseScore\":32,\"CourseName\":\"ec904\"},{\"CourseScore\":59,\"CourseName\":\"ci325\"},{\"CourseScore\":25,\"CourseName\":\"cs570\"},{\"CourseScore\":58,\"CourseName\":\"cs431\"}],\"RollNo\":202817270},{\"Name\":\"KELLY\",\"CourseMarks\":[{\"CourseScore\":75,\"CourseName\":\"me420\"},{\"CourseScore\":69,\"CourseName\":\"cs431\"},{\"CourseScore\":71,\"CourseName\":\"me119\"},{\"CourseScore\":59,\"CourseName\":\"cs481\"},{\"CourseScore\":84,\"CourseName\":\"ec224\"},{\"CourseScore\":80,\"CourseName\":\"ci325\"},{\"CourseScore\":49,\"CourseName\":\"ec904\"},{\"CourseScore\":47,\"CourseName\":\"cs125\"},{\"CourseScore\":61,\"CourseName\":\"cs570\"}],\"RollNo\":202817271},{\"Name\":\"LOUIS\",\"CourseMarks\":[{\"CourseScore\":25,\"CourseName\":\"cs431\"},{\"CourseScore\":79,\"CourseName\":\"cs565\"},{\"CourseScore\":45,\"CourseName\":\"ec224\"},{\"CourseScore\":28,\"CourseName\":\"cs125\"}],\"RollNo\":202817272},{\"Name\":\"JONATHAN\",\"CourseMarks\":[{\"CourseScore\":33,\"CourseName\":\"me119\"},{\"CourseScore\":29,\"CourseName\":\"ci325\"},{\"CourseScore\":67,\"CourseName\":\"cs481\"},{\"CourseScore\":41,\"CourseName\":\"cs431\"},{\"CourseScore\":66,\"CourseName\":\"ec224\"},{\"CourseScore\":89,\"CourseName\":\"cs125\"},{\"CourseScore\":37,\"CourseName\":\"cs565\"}],\"RollNo\":202817273},{\"Name\":\"FRANCES\",\"CourseMarks\":[{\"CourseScore\":53,\"CourseName\":\"ec224\"},{\"CourseScore\":68,\"CourseName\":\"cs570\"},{\"CourseScore\":76,\"CourseName\":\"cs431\"},{\"CourseScore\":30,\"CourseName\":\"cs125\"},{\"CourseScore\":64,\"CourseName\":\"ci325\"},{\"CourseScore\":70,\"CourseName\":\"cs481\"},{\"CourseScore\":26,\"CourseName\":\"cs565\"}],\"RollNo\":202817274}]";
//        System.out.println(content);
        List<String> lines = Files.readAllLines(Paths.get("test/input_sample"), StandardCharsets.UTF_8);
        Student[] students = parseStringToStudents(lines);
        
        long x = System.nanoTime();
        Gson gson = new GsonBuilder().create();
//        gson.toJson(students, System.out);
        String jsonResult = gson.toJson(students);
//        System.out.println();
        long y = System.nanoTime();
        System.out.println(y-x);
        x = System.nanoTime();
        Result result = getResultProtoFromStudents(students);
//        System.out.println(result.toString());
        byte[] myByteArray = result.toByteArray();
        y = System.nanoTime();
        System.out.println(y-x);
//        FileOutputStream fos = new FileOutputStream("test/outproto2.txt");
//        fos.write(myByteArray);
//        fos.close();
//        gson.toJson("Hello", System.out);
//        gson.toJson(123, System.out);
        //gson.toJson(persons, System.out);
//        Student[] students = gson.fromJson(ss, Student[].class);
//        Person[] ps = gson.fromJson("[{\"name\":\"Chandan\",\"age\":21},{\"name\":\"Karan\",\"age\":20}]", Person[].class);
//        System.out.println(ps[0].name);
//        System.out.println(students[0].CourseMarks[0].CourseName);
//        System.out.println(students.toString());
    }
    
}
