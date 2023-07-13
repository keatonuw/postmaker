package com.kowalk.postmaker;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.languagetool.JLanguageTool;
import org.languagetool.JLanguageTool.ParagraphHandling;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;

public class PostModel {

    private static final Pattern pattern = Pattern.compile("# ([^\\n]+)\\n## ([^\\n]+)\\n> ([^\\n]+)\\n([\\s\\S]*)");
    private static final Pattern linkPattern = Pattern.compile("\\[([^\\]]+)\\]\\(([^\\)]+)\\)");

    private final JLanguageTool lang;

    private final String title;
    private final String subtitle;
    private final String summary;
    private String contents;
    private final Date posted;

    /**
     * Creates a new PostModel from a String.
     * 
     * @param str to parse.
     * @return a PostModel with the given info.
     */
    public static PostModel parse(String str) {
        Matcher m = pattern.matcher(str);
        if (m.find()) {
            return new PostModel(
                m.group(1), 
                m.group(2), 
                m.group(3), 
                m.group(4), 
                Date.valueOf(LocalDate.now()));
        }
        
        throw new IllegalStateException("Could not parse string");
    }

    protected PostModel(String title, String subtitle, String summary, 
            String contents, Date posted) {
        this.title = title;
        this.subtitle = subtitle;
        this.summary = summary;
        this.contents = contents;
        this.posted = posted;

        try {
            this.lang = new JLanguageTool(new AmericanEnglish());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Couldn't create PostModel");
        }
    }

    /**
     * Spell checks the Post.
     * 
     * @return true iff there are no spelling/grammar errors.
     */
    public boolean spellCheck() {
        try {
            boolean errorFree = true;

            Map<String, List<RuleMatch>> matches = new TreeMap<>();
            matches.put("Title", lang.check(title));
            matches.put("Subtitle", lang.check(subtitle));
            matches.put("Summary", lang.check(summary));
            matches.put("Contents", lang.check(contents, true, ParagraphHandling.NORMAL));
            
            for (String key : matches.keySet()) {
                if (!matches.get(key).isEmpty()) {
                    errorFree = false;
                    
                    for (RuleMatch match : matches.get(key)) {
                        printMatchError(key, match);
                    }
                }
            }

            return errorFree;
        } catch (IOException e) {
            // no idea why a spell check throws an IOException, but, nonetheless:
            e.printStackTrace();
            throw new IllegalStateException("Could not check model");
        }
    }

    public void convertContentLinks() {
        Matcher matcher = linkPattern.matcher(contents);
        this.contents = matcher.replaceAll(m -> String.format("<a href=\"%s\">%s</a>", m.group(2), m.group(1)));
    }

    public String getTitle() {
        return this.title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public String getSummary() {
        return this.summary;
    }

    public String getContents() {
        return this.contents;
    }

    public Date getPosted() {
        return this.posted;
    }

    private void printMatchError(String location, RuleMatch match) {
        System.out.printf("Potential %s Error (%d-%d): %s. Try %s?\n", 
                location, match.getFromPos(), match.getToPos(), match.getMessage(), 
                match.getSuggestedReplacements());
    }
    
    @Override
    public String toString() {
        return new StringBuilder()
            .append(title)
            .append("\n----------------------\n")
            .append(subtitle)
            .append("\n > ")
            .append(summary)
            .append("\n")
            .append(contents)
            .toString();
    }

}
