package com.movie.script.analysis;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class DialogueLengthMapper extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable dialogueLength = new IntWritable();
    private Text character = new Text();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().trim();
        if (line.contains(":")) {
            String[] parts = line.split(":");
            String characterName = parts[0].trim();
            String dialogue = parts[1].trim();

            character.set(characterName);
            dialogueLength.set(dialogue.length());
            context.write(character, dialogueLength);

            // Increment counter for total characters processed
            context.getCounter("MovieScript", "TotalCharactersProcessed").increment(dialogue.length());
        }
    }
}
