package com.lifotech.rtsa.web.spring.service;

import java.util.Date;
import java.util.List;

import com.lifotech.rtsa.web.spring.domain.TweetSentimentIndexCount;

/**
 * This is a service interface to get the statistics about tweets.
 * 
 * @author SK Singh
 * 
 */
public interface TweetStatsService {

	public List<TweetSentimentIndexCount> getTweetSentimentIndexCountList(Date startDate, Date endDate, String tableName)
			throws Exception;

}
