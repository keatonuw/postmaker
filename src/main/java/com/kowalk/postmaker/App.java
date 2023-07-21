package com.kowalk.postmaker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws SQLException, IOException {
        if (args.length < 4) {
            usage();
        }

        Scanner scanner = new Scanner(System.in);

        String filename = args[0];
        String url = args[1];
        String username = args[2];
        String password = args[3];

        System.getenv("test");

        DbService db = new DbService(username, password, url);

        String post = PostFileReader.read("./testfile.md");

        PostModel model = PostModel.parse(post);
        if (model.spellCheck()) {
            System.out.println("Errors Found. Continue? [Y/n] ");
            if (!scanner.next().equalsIgnoreCase("y")) {
                System.exit(0);
            }
        }
        model.convertContentLinks();
        System.out.println(model);
        System.out.println("Upload? [Y/n]");
        if (scanner.next().equalsIgnoreCase("y")) {
            db.insertPost(model);
        }
    }

    private static void usage() {
        System.err.println("Usage: postmaker <file>");
        System.exit(1);
    }
}
