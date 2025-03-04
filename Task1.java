import java.util.ArrayList;
import java.util.Scanner;
public class Task1 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ArrayList<Integer> grades = new ArrayList<>();
        
        System.out.println("Enter student grades (type -1 to stop):");
        while (true) {
            int grade = scan.nextInt();
            if (grade == -1) break;
            grades.add(grade);//to add a grade
        }
        
        if (grades.isEmpty()) {
            System.out.println("No grades entered.");
        } else {
            System.out.println("Average Grade: " + calculateAverage(grades));
            System.out.println("Highest Grade: " + findHighest(grades));
            System.out.println("Lowest Grade: " + findLowest(grades));
        }
    }
    public static double calculateAverage(ArrayList<Integer> grades) {
        int sum = 0;
        for (int grade : grades) {
            sum += grade;//sum of the grades
        }
        return (double) sum / grades.size();//to print average grade of a student
    }
    public static int findHighest(ArrayList<Integer> grades) {
        int highest = grades.get(0);
        for (int grade : grades) {
            if (grade > highest) {
                highest = grade;//to find highest grade student
            }
        }
        return highest;
    }
    
    public static int findLowest(ArrayList<Integer> grades) {
        int lowest = grades.get(0);
        for (int grade : grades) {
            if (grade < lowest) {
                lowest = grade;//to find lowest grade of student
            }
        }
        return lowest;
    }
}
