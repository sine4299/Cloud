/**
 *  * Licensed to the Apache Software Foundation (ASF) under one
 *   * or more contributor license agreements.  See the NOTICE file
 *    * distributed with this work for additional information
 *     * regarding copyright ownership.  The ASF licenses this file
 *      * to you under the Apache License, Version 2.0 (the
 *       * "License"); you may not use this file except in compliance
 *        * with the License.  You may obtain a copy of the License at
 *         *
 *          *     http://www.apache.org/licenses/LICENSE-2.0
 *           *
 *            * Unless required by applicable law or agreed to in writing, software
 *             * distributed under the License is distributed on an "AS IS" BASIS,
 *              * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *               * See the License for the specific language governing permissions and
 *                * limitations under the License.
 *                 */
//package org.apache.hadoop.examples;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class WordSearch {

  public static class TokenizerMapper 
       extends Mapper<Object, Text, Text, IntWritable>{
    
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
      
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      int lineNumber = Integer.parseInt(itr.nextToken());
      one.set(lineNumber);
      String filePathString = ((FileSplit) context.getInputSplit()).getPath().toString();
      String fileLength = ":0+" + context.getInputSplit().getLength() + ", ";
      while (itr.hasMoreTokens()) {
    	  word.set(itr.nextToken() + " " + filePathString + fileLength);
    	  context.write(word, one);
      }
    }
  }
  
  public static class IntSumReducer 
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, 
                       Context context
                       ) throws IOException, InterruptedException {
      Configuration conf = context.getConfiguration();
      String wordToSearch = conf.get("word");
      String keyWord = key.toString();
      String[] words = keyWord.split(" ");
      if(wordToSearch.equals(words[0])){  	  
	      for (IntWritable val : values) {
	    	  result.set(val.get());
	    	  context.write(key, result);
	      }
      }    
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length < 3) {
      System.err.println("Usage: wordcount <in> [<in>...] <out>");
      System.exit(2);
    }
    conf.set("word", args[2]);
    Job job = new Job(conf, "word count");
    job.setJarByClass(WordSearch.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    for (int i = 0; i < otherArgs.length - 2; ++i) {
      FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
    }
    FileOutputFormat.setOutputPath(job,
      new Path(otherArgs[otherArgs.length - 2]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
