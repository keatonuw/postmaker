package com.kowalk.postmaker;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws SQLException, IOException {
        if (args.length < 2) {
            usage();
        }

        String filename = args[1];

        String url = "jdbc:postgresql://localhost:5432/postgres";

        System.getenv("test");

        DbService db = new DbService("postgres", "", url);
        db.getAll();

        String post = PostFileReader.read("./testfile.md");

        PostModel model = PostModel.parse(post);
        model.spellCheck();
        model.convertContentLinks();
        System.out.println(model);
    }

    private static void usage() {
        System.err.println("Usage: postmaker <file>");
        System.exit(1);
    }
}
