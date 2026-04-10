import java.io.*;
import java.util.*;

public class StudentManagementSystem{

    static final String FILE_NAME = "students.dat";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Student Management System ---");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.println("7. Sort Students by Marks");
            System.out.println("8. Show Topper");
            System.out.println("9. Count Students");
            System.out.println("10. Export to Text File");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1: addStudent(sc); break;
                case 2: viewStudents(); break;
                case 3: searchStudent(sc); break;
                case 4: updateStudent(sc); break;
                case 5: deleteStudent(sc); break;
                case 6: return;
                case 7: sortStudents(); break;
                case 8: showTopper(); break;
                case 9: countStudents(); break;
                case 10: exportToText(); break;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    // 🔹 READ ALL STUDENTS INTO LIST
    static List<Student> readAll() {
        List<Student> list = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            while (true) {
                list.add((Student) ois.readObject());
            }
        } catch (EOFException e) {
        } catch (Exception e) {
            System.out.println("No data found");
        }

        return list;
    }

    // 🔹 SORT BY MARKS
    static void sortStudents() {
        List<Student> list = readAll();

        list.sort((a, b) -> Double.compare(b.getMarks(), a.getMarks()));

        System.out.println("\n--- Sorted Students ---");
        for (Student s : list) {
            System.out.println(s);
        }
    }

    // 🔹 TOPPER
    static void showTopper() {
        List<Student> list = readAll();

        if (list.isEmpty()) {
            System.out.println("No data");
            return;
        }

        Student topper = Collections.max(list, Comparator.comparingDouble(Student::getMarks));

        System.out.println("\nTopper:");
        System.out.println(topper);
    }

    // 🔹 COUNT
    static void countStudents() {
        List<Student> list = readAll();
        System.out.println("Total Students: " + list.size());
    }

    // 🔹 EXPORT TO TEXT FILE
    static void exportToText() {
        List<Student> list = readAll();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("students.txt"))) {
            for (Student s : list) {
                bw.write(s.toString());
                bw.newLine();
            }
            System.out.println("Exported to students.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 ADD STUDENT
    static void addStudent(Scanner sc) {
        try {
            System.out.print("Enter ID: ");
            int id = sc.nextInt();

            if (isDuplicate(id)) {
                System.out.println("ID exists!");
                return;
            }

            sc.nextLine();
            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Marks: ");
            double marks = sc.nextDouble();

            if (marks < 0 || marks > 100) {
                System.out.println("Invalid marks");
                return;
            }

            Student s = new Student(id, name, marks);

            FileOutputStream fos = new FileOutputStream(FILE_NAME, true);
            ObjectOutputStream oos;

            if (new File(FILE_NAME).length() == 0)
                oos = new ObjectOutputStream(fos);
            else
                oos = new AppendableObjectOutputStream(fos);

            oos.writeObject(s);
            oos.close();

            System.out.println("Added!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 VIEW
    static void viewStudents() {
        List<Student> list = readAll();
        for (Student s : list) {
            System.out.println(s);
        }
    }

    // 🔹 SEARCH
    static void searchStudent(Scanner sc) {
        System.out.print("Enter ID: ");
        int id = sc.nextInt();

        for (Student s : readAll()) {
            if (s.getId() == id) {
                System.out.println("Found: " + s);
                return;
            }
        }

        System.out.println("Not found");
    }

    // 🔹 UPDATE
    static void updateStudent(Scanner sc) {
        System.out.print("Enter ID: ");
        int id = sc.nextInt();

        List<Student> list = readAll();

        boolean found = false;

        for (Student s : list) {
            if (s.getId() == id) {
                found = true;

                sc.nextLine();
                System.out.print("New Name: ");
                s.setName(sc.nextLine());

                System.out.print("New Marks: ");
                s.setMarks(sc.nextDouble());
            }
        }

        writeAll(list);

        if (found) System.out.println("Updated!");
        else System.out.println("Not found");
    }

    // 🔹 DELETE
    static void deleteStudent(Scanner sc) {
        System.out.print("Enter ID: ");
        int id = sc.nextInt();

        List<Student> list = readAll();

        list.removeIf(s -> s.getId() == id);

        writeAll(list);

        System.out.println("Deleted!");
    }

    // 🔹 WRITE ALL BACK
    static void writeAll(List<Student> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            for (Student s : list) {
                oos.writeObject(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 DUPLICATE CHECK
    static boolean isDuplicate(int id) {
        for (Student s : readAll()) {
            if (s.getId() == id) return true;
        }
        return false;
    }
}