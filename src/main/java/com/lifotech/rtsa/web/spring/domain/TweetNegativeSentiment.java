package com.lifotech.rtsa.web.spring.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * This is domain class, which encapsulates attributes related to negative sentiments.
 * 
 * @author SK Singh
 *
 */
public class TweetNegativeSentiment implements Serializable, Comparable<TweetNegativeSentiment> {

	
	private static final long serialVersionUID = 1L;

	private Date tweetDateTime;

	private String tweetDateStr;

	/**
	 * Score Type: sentiment polarity: "positive", "negative", or "neutral" 1 :
	 * Mixed and 0: Neutral
	 */

	private double sentimentIndex;

	private String tweetText;

	public String getTweetText() {
		return tweetText;
	}

	public void setTweetText(String tweetText) {
		this.tweetText = tweetText;
	}

	public Date getTweetDateTime() {
		return tweetDateTime;
	}

	public void setTweetDateTime(Date tweetDateTime) {
		this.tweetDateTime = tweetDateTime;
	}

	public String getTweetDate() {
		return tweetDateStr;
	}

	public void setTweetDate(String tweetDateStr) {
		this.tweetDateStr = tweetDateStr;
	}

	public double getSentimentIndex() {
		return sentimentIndex;
	}

	public void setSentimentIndex(double sentimentIndex) {
		this.sentimentIndex = sentimentIndex;
	}

	@Override
	public int compareTo(TweetNegativeSentiment other) {

		int result = 0;

		if (this.sentimentIndex > other.sentimentIndex) {
			return 1;
		}
		if (this.sentimentIndex < other.sentimentIndex) {
			return -1;
		}
		if (this.sentimentIndex == other.sentimentIndex) {

			if (this.tweetDateTime.after(tweetDateTime)) {
				result = -1;
			} else if (tweetDateTime.before(tweetDateTime)) {
				result = 1;
			}
		}

		return result;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(sentimentIndex);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((tweetDateTime == null) ? 0 : tweetDateTime.hashCode());
		result = prime * result + ((tweetText == null) ? 0 : tweetText.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TweetNegativeSentiment other = (TweetNegativeSentiment) obj;
		if (Double.doubleToLongBits(sentimentIndex) != Double.doubleToLongBits(other.sentimentIndex))
			return false;
		if (tweetDateTime == null) {
			if (other.tweetDateTime != null)
				return false;
		} else if (!tweetDateTime.equals(other.tweetDateTime))
			return false;
		if (tweetText == null) {
			if (other.tweetText != null)
				return false;
		} else if (!tweetText.equals(other.tweetText))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TweetSentiment [tweetDateTime=" + tweetDateTime + ", sentimentIndex=" + sentimentIndex + ", tweetText="
				+ tweetText + "]";
	}

}
