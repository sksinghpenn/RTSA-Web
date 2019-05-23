package com.lifotech.rtsa.web.spring.dao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.lifotech.rtsa.web.spring.domain.TweetSentiment;

/**
 * This is an interface  for DAO to access TweetSentiment.
 * 
 * @author SK Singh
 *
 */
public interface TweetSentimentDAO {
	
	
	public List<TweetSentiment> getTweetSentimentList(Date startDate, Date endDate, String tableName) throws IOException;

}
