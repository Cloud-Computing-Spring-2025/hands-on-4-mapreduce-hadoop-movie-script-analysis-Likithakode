package com.movie.script.analysis;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class CharacterWordMapper extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
    private Text characterWord = new Text();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Split line into character and dialogue
        String line = value.toString();
        String[] parts = line.split(":", 2);  // Split only at the first colon

        if (parts.length == 2) {
            String character = parts[0].trim(); // Extract character name
            String dialogue = parts[1].trim(); // Extract dialogue

            StringTokenizer tokenizer = new StringTokenizer(dialogue);
            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken().toLowerCase()); // Normalize words
                characterWord.set(character + "_" + word); // Key format: "Character_Word"
                context.write(characterWord, one);  // Ensure second argument is IntWritable
            }
        }
    }
}
