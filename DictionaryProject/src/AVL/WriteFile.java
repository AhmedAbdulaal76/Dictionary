package AVL;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteFile {
    private String path; // the file path
    private boolean append_to_file = false; //To determine whether to append or not

    public WriteFile(String file_path) {
        path = file_path;
    } // getting the file path

    public WriteFile(String file_path, boolean append_value) {
        path = file_path;
        append_to_file = append_value;
    }

    public void writeToFile(String textLine) throws IOException { //Writes the string in a single line in the desired file
        FileWriter write = new FileWriter(path, append_to_file);
        PrintWriter print_line = new PrintWriter(write);

        print_line.printf("%s" + "%n", textLine);
        print_line.close();
    }

    public void clearFile() throws IOException { // Deletes all lines in the desired file
        FileWriter write = new FileWriter(path, false);
        PrintWriter print_line = new PrintWriter(write);

        print_line.printf("");
        print_line.close();
    }

}