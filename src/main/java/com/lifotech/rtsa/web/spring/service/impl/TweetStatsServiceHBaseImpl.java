package com.lifotech.rtsa.web.spring.service.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lifotech.rtsa.web.spring.service.TweetStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifotech.rtsa.web.spring.dao.TweetSentimentDAO;
import com.lifotech.rtsa.web.spring.domain.TweetNegativeSentiment;
import com.lifotech.rtsa.web.spring.domain.TweetPositiveSentiment;
import com.lifotech.rtsa.web.spring.domain.TweetSentiment;
import com.lifotech.rtsa.web.spring.domain.TweetSentimentIndexCount;
import com.lifotech.rtsa.web.spring.hbase.dao.TweetSentimentHBaseDAO;

/**
 * This is a service class provides statistical information.
 * 
 * @author SK Singh
 * 
 */
public class TweetStatsServiceHBaseImpl implements TweetStatsService {

	private static final Logger logger = LoggerFactory.getLogger(TweetStatsServiceHBaseImpl.class);

	private TweetSentimentDAO tweetSentimentDAO;

	public TweetStatsServiceHBaseImpl() {

	}

	public TweetStatsServiceHBaseImpl(TweetSentimentDAO tweetSentimentDAO) {
		super();
		this.tweetSentimentDAO = tweetSentimentDAO;
	}

	public TweetSentimentDAO getTweetSentimentDAO() {
		return tweetSentimentDAO;
	}

	public void setTweetSentimentDAO(TweetSentimentDAO tweetSentimentDAO) {
		this.tweetSentimentDAO = tweetSentimentDAO;
	}

	/*
	 * The method gets the list of TweetSentimentIndexCount.
	 * 
	 * @param startDate
	 * 
	 * @param endDate
	 * 
	 * @param tableName
	 * 
	 * @return {@link List} of {@link TweetSentimentIndexCount}
	 * 
	 * (non-Javadoc)
	 * 
	 * @see edu.pennstate.greatvalley.sweng.spring.service.TweetStatsService#
	 * getTweetSentimentIndexCountList(java.util.Date, java.util.Date,
	 * java.lang.String)
	 */
	public List<TweetSentimentIndexCount> getTweetSentimentIndexCountList(Date startDate, Date endDate, String tableName)
			throws Exception {

		TweetSentimentHBaseDAO tweetSentimentHBaseDAO = new TweetSentimentHBaseDAO();

		List<TweetSentiment> list = tweetSentimentHBaseDAO.getTweetSentimentList(startDate, endDate, tableName);

		Map<String, BigInteger> postitiveSentimentIndexCountMap = new HashMap<String, BigInteger>();
		Map<String, BigInteger> negativeSentimentIndexCountMap = new HashMap<String, BigInteger>();
		Map<String, BigInteger> neutralSentimentIndexCountMap = new HashMap<String, BigInteger>();
		Map<String, BigInteger> mixedSentimentIndexCountMap = new HashMap<String, BigInteger>();

		for (TweetSentiment tweetSentiment : list) {

			Date tweetDateTime = tweetSentiment.getTweetDateTime();
			if (tweetDateTime == null)
				continue;

			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			String tweetDate = dateFormat.format(tweetDateTime);

			double sentimentIndex = tweetSentiment.getSentimentIndex();

			if (sentimentIndex > 0.0d) {
				postitiveSentimentIndexCountMap = getPositiveSentimentIndexCountMap(postitiveSentimentIndexCountMap,
						tweetDate);
			}

			if (tweetSentiment.getSentimentIndex() < 0.0d) {
				negativeSentimentIndexCountMap = getNegativeSentimentIndexCountMap(negativeSentimentIndexCountMap,
						tweetDate);
			}

			if (tweetSentiment.getSentimentIndex() == 0.0d) {
				neutralSentimentIndexCountMap = getNeutralSentimentIndexCountMap(neutralSentimentIndexCountMap,
						tweetDate);
			}

			if (tweetSentiment.getSentimentIndex() == 1.0d) {
				mixedSentimentIndexCountMap = getMixedSentimentIndexCountMap(mixedSentimentIndexCountMap, tweetDate);
			}

		}

		List<TweetSentimentIndexCount> tweetSentimentIndexCountList = new ArrayList<TweetSentimentIndexCount>();
		Map<String, TweetSentimentIndexCount> tweetSentimentIndexCountMap = new HashMap<String, TweetSentimentIndexCount>();

		Set<String> positiveKeys = postitiveSentimentIndexCountMap.keySet();

		for (String str : positiveKeys) {
			tweetSentimentIndexCountMap = getTweetSentimenIndexCountMapBySettingPositiveIndex(
					postitiveSentimentIndexCountMap, tweetSentimentIndexCountMap, str);

		}

		Set<String> negativeKeys = negativeSentimentIndexCountMap.keySet();

		for (String str : negativeKeys) {
			tweetSentimentIndexCountMap = getTweetSentimenIndexCountMapBySettingNegativeIndex(
					negativeSentimentIndexCountMap, tweetSentimentIndexCountMap, str);
		}

		Set<String> mixedKeys = mixedSentimentIndexCountMap.keySet();

		for (String str : mixedKeys) {
			tweetSentimentIndexCountMap = getTweetSentimenIndexCountMapBySettingMixedIndex(mixedSentimentIndexCountMap,
					tweetSentimentIndexCountMap, str);
		}

		Set<String> neutralKeys = neutralSentimentIndexCountMap.keySet();

		for (String str : neutralKeys) {
			tweetSentimentIndexCountMap = getTweetSentimenIndexCountMapBySettingNeutralIndex(
					neutralSentimentIndexCountMap, tweetSentimentIndexCountMap, str);
		}

		Set<String> tweetDateSet = tweetSentimentIndexCountMap.keySet();

		for (String tweetDate : tweetDateSet) {

			tweetSentimentIndexCountList.add(tweetSentimentIndexCountMap.get(tweetDate));

		}
		return tweetSentimentIndexCountList;

	}

	/*
	 * The method lists top positive tweets
	 * 
	 * @param startDate
	 * 
	 * @param endDate
	 * 
	 * @param tableName
	 * 
	 * @return {@link List} of {@link TweetPositiveSentiment}
	 */
	public List<TweetPositiveSentiment> getTopPostiveTweetSentimentList(Date startDate, Date endDate, String tableName,
			int numberofTweets) throws Exception {

		TweetSentimentHBaseDAO tweetSentimentHBaseDAO = new TweetSentimentHBaseDAO();

		List<TweetPositiveSentiment> list = tweetSentimentHBaseDAO.getTopPostiveTweetSentimentList(startDate, endDate,
				tableName);

		Collections.sort(list);

		if (list.size() < numberofTweets) {
			return list.subList(0, list.size());
		}
		return list.subList(0, numberofTweets);
	}

	/*
	 * The method lists top negative tweets
	 * 
	 * @param startDate
	 * 
	 * @param endDate
	 * 
	 * @param tableName
	 * 
	 * @return {@link List} of {@link TweetNegativeSentiment}
	 */
	public List<TweetNegativeSentiment> getTopNegativeTweetSentimentList(Date startDate, Date endDate,
			String tableName, int numberofTweets) throws Exception {

		TweetSentimentHBaseDAO tweetSentimentHBaseDAO = new TweetSentimentHBaseDAO();

		List<TweetNegativeSentiment> list = tweetSentimentHBaseDAO.getTopNegativeTweetSentimentList(startDate, endDate,
				tableName);

		Collections.sort(list);

		if (list.size() < numberofTweets) {
			return list.subList(0, list.size());
		}
		return list.subList(0, numberofTweets);
	}

	private Map<String, TweetSentimentIndexCount> getTweetSentimenIndexCountMapBySettingNeutralIndex(
			Map<String, BigInteger> neutralSentimentIndexCountMap,
			Map<String, TweetSentimentIndexCount> tweetSentimentIndexCountMap, String str) {
		TweetSentimentIndexCount tweetSentimentIndexCount = tweetSentimentIndexCountMap.get(str);

		if (tweetSentimentIndexCount == null) {
			tweetSentimentIndexCount = new TweetSentimentIndexCount();
			tweetSentimentIndexCount.setTweetDate(str);
			tweetSentimentIndexCount.setNeutralSentimentIndexCount(neutralSentimentIndexCountMap.get(str));

		} else {
			tweetSentimentIndexCount.setNeutralSentimentIndexCount(neutralSentimentIndexCountMap.get(str));
		}
		tweetSentimentIndexCountMap.put(str, tweetSentimentIndexCount);

		return tweetSentimentIndexCountMap;
	}

	private Map<String, TweetSentimentIndexCount> getTweetSentimenIndexCountMapBySettingMixedIndex(
			Map<String, BigInteger> mixedSentimentIndexCountMap,
			Map<String, TweetSentimentIndexCount> tweetSentimentIndexCountMap, String str) {
		TweetSentimentIndexCount tweetSentimentIndexCount = tweetSentimentIndexCountMap.get(str);

		if (tweetSentimentIndexCount == null) {
			tweetSentimentIndexCount = new TweetSentimentIndexCount();
			tweetSentimentIndexCount.setTweetDate(str);
			tweetSentimentIndexCount.setMixedSentimentIndexCount(mixedSentimentIndexCountMap.get(str));

		} else {
			tweetSentimentIndexCount.setMixedSentimentIndexCount(mixedSentimentIndexCountMap.get(str));
		}
		tweetSentimentIndexCountMap.put(str, tweetSentimentIndexCount);

		return tweetSentimentIndexCountMap;
	}

	private Map<String, TweetSentimentIndexCount> getTweetSentimenIndexCountMapBySettingNegativeIndex(
			Map<String, BigInteger> negativeSentimentIndexCountMap,
			Map<String, TweetSentimentIndexCount> tweetSentimentIndexCountMap, String str) {
		TweetSentimentIndexCount tweetSentimentIndexCount = tweetSentimentIndexCountMap.get(str);

		if (tweetSentimentIndexCount == null) {
			tweetSentimentIndexCount = new TweetSentimentIndexCount();
			tweetSentimentIndexCount.setTweetDate(str);
			tweetSentimentIndexCount.setNegativeSentimentIndexCount(negativeSentimentIndexCountMap.get(str));

		} else {
			tweetSentimentIndexCount.setNegativeSentimentIndexCount(negativeSentimentIndexCountMap.get(str));
		}
		tweetSentimentIndexCountMap.put(str, tweetSentimentIndexCount);

		return tweetSentimentIndexCountMap;
	}

	private Map<String, TweetSentimentIndexCount> getTweetSentimenIndexCountMapBySettingPositiveIndex(
			Map<String, BigInteger> postitiveSentimentIndexCountMap,
			Map<String, TweetSentimentIndexCount> tweetSentimentIndexCountMap, String str) {
		TweetSentimentIndexCount tweetSentimentIndexCount = tweetSentimentIndexCountMap.get(str);

		if (tweetSentimentIndexCount == null) {
			tweetSentimentIndexCount = new TweetSentimentIndexCount();
			tweetSentimentIndexCount.setTweetDate(str);
			tweetSentimentIndexCount.setPositiveSentimentIndexCount(postitiveSentimentIndexCountMap.get(str));

		} else {
			tweetSentimentIndexCount.setPositiveSentimentIndexCount(postitiveSentimentIndexCountMap.get(str));
		}
		tweetSentimentIndexCountMap.put(str, tweetSentimentIndexCount);
		return tweetSentimentIndexCountMap;
	}

	private Map<String, BigInteger> getMixedSentimentIndexCountMap(Map<String, BigInteger> mixedSentimentIndexCountMap,
			String tweetDate) {
		BigInteger mixedSentimentIndexCount = mixedSentimentIndexCountMap.get(tweetDate);
		if (mixedSentimentIndexCount == null) {
			mixedSentimentIndexCountMap.put(tweetDate, new BigInteger("1"));
		} else {
			mixedSentimentIndexCountMap.put(tweetDate, mixedSentimentIndexCount.add(new BigInteger("1")));
		}

		return mixedSentimentIndexCountMap;
	}

	private Map<String, BigInteger> getNeutralSentimentIndexCountMap(
			Map<String, BigInteger> neutralSentimentIndexCountMap, String tweetDate) {
		BigInteger neutralSentimentIndexCount = neutralSentimentIndexCountMap.get(tweetDate);
		if (neutralSentimentIndexCount == null) {
			neutralSentimentIndexCountMap.put(tweetDate, new BigInteger("1"));
		} else {
			neutralSentimentIndexCountMap.put(tweetDate, neutralSentimentIndexCount.add(new BigInteger("1")));
		}

		return neutralSentimentIndexCountMap;
	}

	private Map<String, BigInteger> getNegativeSentimentIndexCountMap(
			Map<String, BigInteger> negativeSentimentIndexCountMap, String tweetDate) {
		BigInteger negativeSentimentIndexCount = negativeSentimentIndexCountMap.get(tweetDate);
		if (negativeSentimentIndexCount == null) {
			negativeSentimentIndexCountMap.put(tweetDate, new BigInteger("1"));
		} else {
			negativeSentimentIndexCountMap.put(tweetDate, negativeSentimentIndexCount.add(new BigInteger("1")));
		}

		return negativeSentimentIndexCountMap;
	}

	private Map<String, BigInteger> getPositiveSentimentIndexCountMap(
			Map<String, BigInteger> postitiveSentimentIndexCountMap, String tweetDate) {
		BigInteger postitiveSentimentIndexCount = postitiveSentimentIndexCountMap.get(tweetDate);
		if (postitiveSentimentIndexCount == null) {
			postitiveSentimentIndexCountMap.put(tweetDate, new BigInteger("1"));
		} else {
			postitiveSentimentIndexCountMap.put(tweetDate, postitiveSentimentIndexCount.add(new BigInteger("1")));
		}

		return postitiveSentimentIndexCountMap;
	}

}
