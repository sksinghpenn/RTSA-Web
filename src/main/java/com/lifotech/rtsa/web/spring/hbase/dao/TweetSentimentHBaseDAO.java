package com.lifotech.rtsa.web.spring.hbase.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifotech.rtsa.web.spring.dao.TweetSentimentDAO;
import com.lifotech.rtsa.web.spring.domain.TweetNegativeSentiment;
import com.lifotech.rtsa.web.spring.domain.TweetPositiveSentiment;
import com.lifotech.rtsa.web.spring.domain.TweetSentiment;

/**
 * The class is HBase implementation of TweetSentimentDAO.
 * 
 * @author SK Singh
 * 
 */
public class TweetSentimentHBaseDAO implements TweetSentimentDAO {

	private static final Logger logger = LoggerFactory.getLogger(TweetSentimentHBaseDAO.class);
	
	private static final String SENTIMENT = "sentiment";
	private static final String SENTIMENT_INDEX = "sentimentIndex";
	private static final String TWEET_TIME = "tweetTime";
	private static final String TWEET_TEXT = "tweetText";
	
	/*
	 * The method returns list of TweetSentiment objects.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param tableName
	 * 
	 * @return {@link List} of {@link TweetSentiment} objects
	 * 
	 * (non-Javadoc)
	 * @see edu.pennstate.greatvalley.sweng.spring.dao.TweetSentimentDAO#getTweetSentimentList(java.util.Date, java.util.Date, java.lang.String)
	 */
	public List<TweetSentiment> getTweetSentimentList(Date startDate, Date endDate, String tableName)
			throws IOException {

		Configuration conf = getHBaseConfiguration();

		HTable table = new HTable(conf, tableName);

		Scan scan = new Scan();
		scan.addFamily(Bytes.toBytes(SENTIMENT));

		ArrayList<TweetSentiment> tweetSentimentList = new ArrayList<TweetSentiment>();


		scan.setTimeRange(startDate.getTime(), endDate.getTime());

		ResultScanner resultScanner = table.getScanner(scan);
		// For each row
		for (Result result : resultScanner) {
			TweetSentiment tweetSentiment = new TweetSentiment();
			for (KeyValue kv : result.raw()) {

				String qualifier = Bytes.toString(kv.getQualifier());
				if (qualifier.equals(SENTIMENT_INDEX)) {
					tweetSentiment.setSentimentIndex(Bytes.toDouble(kv.getValue()));
				}

				if (qualifier.equals(TWEET_TIME)) {
					tweetSentiment.setTweetDateTime(new Date(kv.getTimestamp()));
				}

			}
			tweetSentimentList.add(tweetSentiment);
		}

		return tweetSentimentList;

	}

	/*
	 * The method returns list of TweetPositiveSentiment objects.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param tableName
	 * 
	 * @return {@link List} of {@link TweetPositiveSentiment} objects
	 * 
	 * (non-Javadoc)
	 * @see edu.pennstate.greatvalley.sweng.spring.dao.TweetSentimentDAO#getTweetSentimentList(java.util.Date, java.util.Date, java.lang.String)
	 */
	public List<TweetPositiveSentiment> getTopPostiveTweetSentimentList(Date startDate, Date endDate, String tableName)
			throws IOException {

		Configuration conf = getHBaseConfiguration();

		HTable table = new HTable(conf, tableName);

		Scan scan = new Scan();
		scan.addFamily(Bytes.toBytes(SENTIMENT));

		ArrayList<TweetPositiveSentiment> tweetSentimentList = new ArrayList<TweetPositiveSentiment>();

		scan.setTimeRange(startDate.getTime(), endDate.getTime());

		ResultScanner resultScanner = table.getScanner(scan);

		TweetPositiveSentiment tweetSentiment = null;
		// For each row
		for (Result result : resultScanner) {

			for (KeyValue kv : result.raw()) {

				String qualifier = Bytes.toString(kv.getQualifier());
				if (qualifier.equals(SENTIMENT_INDEX)) {

					double sentimentIndex = Bytes.toDouble(kv.getValue());

					if (sentimentIndex > 0) {
						tweetSentiment = new TweetPositiveSentiment();
						tweetSentiment.setSentimentIndex(Bytes.toDouble(kv.getValue()));
					} else {
						break;
					}

				}
				if (qualifier.equals(TWEET_TIME)) {
					tweetSentiment.setTweetDateTime(new Date(kv.getTimestamp()));
				}

				if (qualifier.equals(TWEET_TEXT)) {
					tweetSentiment.setTweetText(Bytes.toString(kv.getValue()));
				}

			}
			if (tweetSentiment != null)
				tweetSentimentList.add(tweetSentiment);
		}

		return filterDuplicateText(tweetSentimentList);

	}

	/*
	 * The method returns list of TweetNegativeSentiment objects.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param tableName
	 * 
	 * @return {@link List} of {@link TweetNegativeSentiment} objects
	 * 
	 * (non-Javadoc)
	 * @see edu.pennstate.greatvalley.sweng.spring.dao.TweetSentimentDAO#getTweetSentimentList(java.util.Date, java.util.Date, java.lang.String)
	 */
	public List<TweetNegativeSentiment> getTopNegativeTweetSentimentList(Date startDate, Date endDate, String tableName)
			throws IOException {

		Configuration conf = getHBaseConfiguration();

		HTable table = new HTable(conf, tableName);

		Scan scan = new Scan();
		scan.addFamily(Bytes.toBytes(SENTIMENT));

		ArrayList<TweetNegativeSentiment> tweetSentimentList = new ArrayList<TweetNegativeSentiment>();

		scan.setTimeRange(startDate.getTime(), endDate.getTime());

		ResultScanner resultScanner = table.getScanner(scan);

		TweetNegativeSentiment tweetSentiment = null;
		// For each row
		for (Result result : resultScanner) {

			for (KeyValue kv : result.raw()) {

				String qualifier = Bytes.toString(kv.getQualifier());
				if (qualifier.equals(SENTIMENT_INDEX)) {

					double sentimentIndex = Bytes.toDouble(kv.getValue());

					if (sentimentIndex < 0) {
						tweetSentiment = new TweetNegativeSentiment();
						tweetSentiment.setSentimentIndex(Bytes.toDouble(kv.getValue()));
					} else {
						break;
					}

				}
				if (qualifier.equals(TWEET_TIME)) {
					tweetSentiment.setTweetDateTime(new Date(kv.getTimestamp()));
				}

				if (qualifier.equals(TWEET_TEXT)) {
					tweetSentiment.setTweetText(Bytes.toString(kv.getValue()));
				}

			}
			if (tweetSentiment != null)
				tweetSentimentList.add(tweetSentiment);
		}

		return filterDuplicateTextForNegative(tweetSentimentList);

	}

	private List<TweetPositiveSentiment> filterDuplicateText(List<TweetPositiveSentiment> list) {
		Map<Integer, TweetPositiveSentiment> map = new HashMap<Integer, TweetPositiveSentiment>();

		for (TweetPositiveSentiment tweet : list) {
			map.put(tweet.getTweetText().hashCode(), tweet);
		}

		Set<Integer> keys = map.keySet();

		ArrayList<TweetPositiveSentiment> tweetSentimentList = new ArrayList<TweetPositiveSentiment>();

		for (Integer key : keys) {
			tweetSentimentList.add(map.get(key));
		}

		return tweetSentimentList;
	}

	private List<TweetNegativeSentiment> filterDuplicateTextForNegative(List<TweetNegativeSentiment> list) {
		Map<Integer, TweetNegativeSentiment> map = new HashMap<Integer, TweetNegativeSentiment>();

		for (TweetNegativeSentiment tweet : list) {
			map.put(tweet.getTweetText().hashCode(), tweet);
		}

		Set<Integer> keys = map.keySet();

		ArrayList<TweetNegativeSentiment> tweetSentimentList = new ArrayList<TweetNegativeSentiment>();

		for (Integer key : keys) {
			tweetSentimentList.add(map.get(key));
		}

		return tweetSentimentList;
	}

	private Configuration getHBaseConfiguration() {
		Configuration conf = new Configuration();

		TweetSentimentHBaseDAO tweetSentimentHBaseDAO = new TweetSentimentHBaseDAO();

		InputStream coreSite = tweetSentimentHBaseDAO.getClass().getClassLoader().getResourceAsStream("core-site.xml");

		InputStream hbaseSite = tweetSentimentHBaseDAO.getClass().getClassLoader()
				.getResourceAsStream("hbase-site.xml");

		conf.addResource(coreSite);
		conf.addResource(hbaseSite);
		return conf;
	}

	/**
	 * This is a test method
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();

		TweetSentimentHBaseDAO tweetSentimentHBaseDAO = new TweetSentimentHBaseDAO();

		InputStream coreSite = tweetSentimentHBaseDAO.getClass().getClassLoader().getResourceAsStream("core-site.xml");

		InputStream hbaseSite = tweetSentimentHBaseDAO.getClass().getClassLoader()
				.getResourceAsStream("hbase-site.xml");

		conf.addResource(coreSite);
		conf.addResource(hbaseSite);

		// tweetSentimentHBaseDAO.getTweetSentimentList()

		HTable table = new HTable(conf, "RTSA1002");

		Scan scan = new Scan();
		scan.addFamily(Bytes.toBytes(SENTIMENT));

		byte[] columnFamily = Bytes.toBytes(SENTIMENT);
		byte[] columnQualifier = Bytes.toBytes(TWEET_TIME);

		Calendar tweetStartDate = Calendar.getInstance();

		tweetStartDate.set(2015, 5, 30, 0, 0, 1);
		Date startDate = tweetStartDate.getTime();
		long startDateLong = startDate.getTime();
		System.out.println(new Date(startDateLong));

		Calendar tweetEndDate = Calendar.getInstance();
		tweetEndDate.set(2015, 7, 29, 12, 0, 0);
		Date endDate = tweetEndDate.getTime();
		long endDateLong = endDate.getTime();
		System.out.println(new Date(endDateLong));

		scan.setTimeRange(startDateLong, endDateLong);

		ResultScanner resultScanner = table.getScanner(scan);

		int i = 0;
		// For each row
		for (Result result : resultScanner) {
			i++;
			for (KeyValue kv : result.raw()) {

				String qualifier = Bytes.toString(kv.getQualifier());

				if (qualifier.equals(SENTIMENT_INDEX)) {
					System.out.println("sentiment Index: " + Bytes.toDouble(kv.getValue()));
					// System.out.println(kv.getTimestamp());
					System.out.println(new Date(kv.getTimestamp()));

					// System.out.println(new Date(1435678202561L));

				}
				if (qualifier.equals(TWEET_TIME)) {

					System.out.println("tweetTime : " + Bytes.toString((kv.getValue())));
				}

				if (qualifier.equals(TWEET_TEXT)) {

					System.out.println("tweetText : " + Bytes.toString((kv.getValue())));
				}
				if (qualifier.equals("tweetComment")) {

					System.out.println("tweetComment : " + Bytes.toString((kv.getValue())));
				}
			}
		}

		System.out.println("total " + i);

	}

}
