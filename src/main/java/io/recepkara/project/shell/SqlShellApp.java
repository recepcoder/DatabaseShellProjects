package io.recepkara.project.shell;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SqlShellApp {
    private static ShellSqlDatabaseEngine sqlDatabaseEngine=new ShellSqlDatabaseEngine();
    public static void main(String[] args) {
        System.out.println("[---------- Recep Kara SQL Shell ----------]");
        System.out.println("Enter : quit to exit this shell");
        System.out.println();
        System.out.print("> ");

        Scanner scanner=new Scanner(System.in);
        List<String> inputs=new ArrayList<>();

        while (true)
        {
            String input=scanner.nextLine();
            if (input.trim().equalsIgnoreCase(":quit")) {
                System.out.println("Thank you for using Recep Kara SQL Shell");
                System.exit(0);
            } else if (input.endsWith(";")) {
                if (input.length() > 1) {
                    input = input.substring(0, input.length() - 1);
                } else {
                    input="";
                }
                inputs.add(input);
                String sqlString=String.join(" ",inputs);
                sqlDatabaseEngine.parseSql(sqlString);
                inputs.clear();
                System.out.println();
                System.out.print(">");

            } else {
                inputs.add(input);
            }
        }

    }
}
