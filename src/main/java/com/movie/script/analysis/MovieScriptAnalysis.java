package com.movie.script.analysis;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MovieScriptAnalysis {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        // Task 1: Most Frequent Words by Character
        Job job1 = Job.getInstance(conf, "Most Frequent Words by Character");
        job1.setJarByClass(MovieScriptAnalysis.class);
        job1.setMapperClass(CharacterWordMapper.class);
        job1.setReducerClass(CharacterWordReducer.class);
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job1, new Path(args[1]));
        FileOutputFormat.setOutputPath(job1, new Path(args[2] + "/task1"));
        job1.waitForCompletion(true);

        // Task 2: Dialogue Length Analysis
        Job job2 = Job.getInstance(conf, "Dialogue Length Analysis");
        job2.setJarByClass(MovieScriptAnalysis.class);
        job2.setMapperClass(DialogueLengthMapper.class);
        job2.setReducerClass(DialogueLengthReducer.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job2, new Path(args[1]));
        FileOutputFormat.setOutputPath(job2, new Path(args[2] + "/task2"));
        job2.waitForCompletion(true);

        // Task 3: Unique Words by Character
        Job job3 = Job.getInstance(conf, "Unique Words by Character");
        job3.setJarByClass(MovieScriptAnalysis.class);
        job3.setMapperClass(UniqueWordsMapper.class);
        job3.setReducerClass(UniqueWordsReducer.class);
        job3.setOutputKeyClass(Text.class);
        job3.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job3, new Path(args[1]));
        FileOutputFormat.setOutputPath(job3, new Path(args[2] + "/task3"));
        job3.waitForCompletion(true);

        // Task 4: Hadoop Counters Output
        Job job4 = Job.getInstance(conf, "Hadoop Counter Output");
        job4.setJarByClass(MovieScriptAnalysis.class);
        job4.setMapperClass(ScriptCounterMapper.class);
        job4.setNumReduceTasks(0); // No reducer needed
        FileInputFormat.addInputPath(job4, new Path(args[1]));
        FileOutputFormat.setOutputPath(job4, new Path(args[2] + "/task4"));

        boolean job4Completed = job4.waitForCompletion(true);
        if (job4Completed) {
            Counters counters = job4.getCounters();
            System.out.println("\nHadoop Counter Output:");
            System.out.println("Total Lines Processed: " + counters.findCounter(ScriptCounterMapper.ScriptCounters.TOTAL_LINES_PROCESSED).getValue());
            System.out.println("Total Words Processed: " + counters.findCounter(ScriptCounterMapper.ScriptCounters.TOTAL_WORDS_PROCESSED).getValue());
            System.out.println("Total Characters Processed: " + counters.findCounter(ScriptCounterMapper.ScriptCounters.TOTAL_CHARACTERS_PROCESSED).getValue());
            System.out.println("Total Unique Words Identified: " + counters.findCounter(ScriptCounterMapper.ScriptCounters.TOTAL_UNIQUE_WORDS_IDENTIFIED).getValue());
            System.out.println("Number of Characters Speaking: " + counters.findCounter(ScriptCounterMapper.ScriptCounters.NUMBER_OF_CHARACTERS_SPEAKING).getValue());
        }

        System.exit(job4Completed ? 0 : 1);
    }
}
