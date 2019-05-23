package com.lifotech.rtsa.web.spring.domain;

import java.util.Date;

/**
 * The domain class encapsulates attributes of a tweet including its sentiment score.
 * 
 * @author SK Singh
 *
 */
public class TweetSentiment {

	private Date tweetDateTime;
	
	
	/**
	 * Score Type: sentiment polarity: "positive", "negative", or "neutral"
	 * 1 : Mixed
	 * and 0: Neutral
	 */	
	
	private double sentimentIndex;


	public Date getTweetDateTime() {
		return tweetDateTime;
	}


	public void setTweetDateTime(Date tweetDateTime) {
		this.tweetDateTime = tweetDateTime;
	}


	public double getSentimentIndex() {
		return sentimentIndex;
	}


	public void setSentimentIndex(double sentimentIndex) {
		this.sentimentIndex = sentimentIndex;
	}
	
	
	

}
