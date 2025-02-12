package com.movie.script.analysis;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.StringTokenizer;

public class UniqueWordsMapper extends Mapper<Object, Text, Text, Text> {

    private Text character = new Text();
    private Text word = new Text();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().trim();
        if (line.contains(":")) {
            String[] parts = line.split(":");
            String characterName = parts[0].trim();
            String dialogue = parts[1].trim();

            character.set(characterName);
            StringTokenizer tokenizer = new StringTokenizer(dialogue);

            HashSet<String> uniqueWords = new HashSet<>();
            while (tokenizer.hasMoreTokens()) {
                uniqueWords.add(tokenizer.nextToken().toLowerCase());
            }

            // Emit unique words for each character
            for (String uniqueWord : uniqueWords) {
                word.set(uniqueWord);
                context.write(character, word);
            }

            // Increment counter for total unique words identified
            context.getCounter("MovieScript", "TotalUniqueWordsIdentified").increment(uniqueWords.size());
        }
    }
}
