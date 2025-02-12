package com.movie.script.analysis;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;
import java.util.HashSet;
import java.util.StringTokenizer;

public class ScriptCounterMapper extends Mapper<Object, Text, Text, Text> {

    public enum ScriptCounters {
        TOTAL_LINES_PROCESSED,
        TOTAL_WORDS_PROCESSED,
        TOTAL_CHARACTERS_PROCESSED,
        TOTAL_UNIQUE_WORDS_IDENTIFIED,
        NUMBER_OF_CHARACTERS_SPEAKING
    }

    private Text character = new Text();
    private HashSet<String> uniqueWords = new HashSet<>();
    private HashSet<String> uniqueCharacters = new HashSet<>();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().trim();
        
        if (line.isEmpty()) return; // Skip empty lines
        
        context.getCounter(ScriptCounters.TOTAL_LINES_PROCESSED).increment(1);

        // Extract character and dialogue
        String[] parts = line.split(":", 2);
        if (parts.length < 2) return;

        String characterName = parts[0].trim();
        String dialogue = parts[1].trim();

        uniqueCharacters.add(characterName);
        character.set(characterName);

        context.getCounter(ScriptCounters.NUMBER_OF_CHARACTERS_SPEAKING).setValue(uniqueCharacters.size());

        StringTokenizer tokenizer = new StringTokenizer(dialogue);
        int wordCount = 0;
        int charCount = dialogue.length();

        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken().toLowerCase().replaceAll("[^a-zA-Z]", "");
            if (!word.isEmpty()) {
                wordCount++;
                uniqueWords.add(word);
            }
        }

        context.getCounter(ScriptCounters.TOTAL_WORDS_PROCESSED).increment(wordCount);
        context.getCounter(ScriptCounters.TOTAL_CHARACTERS_PROCESSED).increment(charCount);
        context.getCounter(ScriptCounters.TOTAL_UNIQUE_WORDS_IDENTIFIED).setValue(uniqueWords.size());
    }
}
