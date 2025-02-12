# Hadoop MapReduce Movie Script Analysis

## Project Overview

This project implements a Hadoop MapReduce job to analyze a movie script dataset. The dataset consists of dialogues spoken by different characters in the format:

```
Character: Dialogue
```

The MapReduce job processes the dataset to extract insights, including:

- Most frequently spoken words by characters.
- Total dialogue length per character.
- Unique words used by each character.
- Hadoop Counters to track:
  - Total number of lines processed.
  - Total words processed.
  - Total characters processed.
  - Total unique words identified.
  - Number of characters speaking dialogues.

## Approach and Implementation

### Mapper

The Mapper processes each line of input text, extracting the character's name and their dialogue. It performs the following:

1. Splits the line into `Character` and `Dialogue`.
2. Tokenizes the dialogue into words.
3. Emits key-value pairs:
   - (`word`, `1`) for word frequency calculation.
   - (`character`, `dialogue length`) for total dialogue length.
   - (`character`, `set of unique words`) for unique words tracking.
4. Increments Hadoop Counters for total lines, words, characters, and unique words.

### Reducer

The Reducer aggregates data from the Mapper to:

1. Count occurrences of each word.
2. Sum the dialogue lengths per character.
3. Combine unique words per character into a set.
4. Track the number of unique characters speaking.

### Hadoop Counters

Hadoop Counters are used to monitor key statistics:

- `TOTAL_LINES_PROCESSED`: Number of lines processed.
- `TOTAL_WORDS_PROCESSED`: Number of words processed.
- `TOTAL_CHARACTERS_PROCESSED`: Number of characters processed.
- `TOTAL_UNIQUE_WORDS`: Number of unique words identified.
- `TOTAL_CHARACTERS_SPEAKING`: Number of characters speaking dialogues.

## Execution Steps

Follow these steps to build and execute the project:

1. **Setup Hadoop Environment** (Ensure Hadoop is installed and running)

   ```sh
   hdfs dfs -mkdir -p /user/hadoop/input
   hdfs dfs -put movie_script.txt /user/hadoop/input/
   ```

2. **Build Project using Maven**

   ```sh
   mvn clean package
   ```

3. **Run MapReduce Job**

   ```sh
   hadoop jar target/movie-script-analysis.jar MovieScriptAnalysis /user/hadoop/input/movie_script.txt /user/hadoop/output
   ```

4. **View Results**

   ```sh
   hdfs dfs -cat /user/hadoop/output/part-r-00000
   ```

5. **Check Hadoop Counters**

   ```sh
   hadoop job -counter <job-id> TOTAL_LINES_PROCESSED
   ```

   *(Replace **``** with the actual job ID.)*

## Challenges Faced & Solutions

### 1. Handling Large Datasets

**Issue:** Processing large movie scripts caused memory overhead. **Solution:** Optimized Mapper output to minimize data transfer and used combiner to reduce intermediate data.

### 2. Unique Word Tracking

**Issue:** Handling large sets of unique words per character. **Solution:** Used a HashSet in the Mapper and merged results efficiently in the Reducer.

## Sample Input and Output

### Input

```
JACK: The ship is sinking! We have to go now.
ROSE: I won’t leave without you.
JACK: We don’t have time, Rose!
```

### Expected Output

#### 1. Most Frequently Spoken Words by Characters

```
the 3
we 3
have 2
to 2
now 1
without 1
```

#### 2. Total Dialogue Length per Character

```
JACK 54
ROSE 25
```

#### 3. Unique Words Used by Each Character

```
JACK [the, ship, is, sinking, we, have, to, go, now, don’t, time, rose]
ROSE [i, won’t, leave, without, you]
```

#### 4. Hadoop Counter Output

```
Total Lines Processed: 3
Total Words Processed: 18
Total Characters Processed: 79
Total Unique Words Identified: 13
Number of Characters Speaking: 2
```

## Conclusion

This project demonstrates how Hadoop MapReduce can be used to analyze movie scripts efficiently. By implementing a Mapper, Reducer, and Hadoop Counters, we were able to extract meaningful insights while tracking key statistics. The implementation is scalable and can be applied to large script datasets.

