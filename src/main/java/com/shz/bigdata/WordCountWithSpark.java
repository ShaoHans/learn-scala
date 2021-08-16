package com.shz.bigdata;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class WordCountWithSpark {
    public static void main(String[] args) throws IOException {
        SparkConf conf = new SparkConf();
        conf.setAppName("java-spark-wordcount");
        conf.setMaster("local");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        // 1.读取数据文件，得到一行一行的字符串记录
        JavaRDD<String> dataRDD = jsc.textFile("data/test.txt");

        // 2.对每一行字符串记录按照空格进行split，得到一个集合，然后返回这个集合的迭代器
        JavaRDD<String> wordsRDD = dataRDD.flatMap(new FlatMapFunction<String, String>() {
            public Iterator<String> call(String line) throws Exception {
                return Arrays.asList(line.split(",")).iterator();
            }
        });

        // 3.遍历集合，对每个元素生成一个元祖对象，元祖的key为word，value为1
        JavaPairRDD<String, Integer> pairRDD = wordsRDD.mapToPair(new PairFunction<String, String, Integer>() {
            public Tuple2<String, Integer> call(String word) throws Exception {
                return new Tuple2<String, Integer>(word, 1);
            }
        });

        // 4.根据元祖集合的key进行分组计算
        JavaPairRDD<String, Integer> resRDD = pairRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer oldV, Integer newV) throws Exception {
                return oldV + newV;
            }
        });

        resRDD.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            public void call(Tuple2<String, Integer> t) throws Exception {
                System.out.println("(" + t._1 + "," + t._2 + ")");
            }
        });

        System.in.read();
    }
}
