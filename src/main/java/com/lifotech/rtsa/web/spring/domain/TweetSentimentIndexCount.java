package com.lifotech.rtsa.web.spring.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The domain class encapsulates total number of positive, negative, neutral,and mixed sentiment
 * and its date. 
 * 
 * @author SK Singh
 *
 */
public class TweetSentimentIndexCount implements Serializable, Comparable<TweetSentimentIndexCount>{
	
	private static final long serialVersionUID = 1L;
	private String tweetDateStr;
	private BigInteger positiveSentimentIndexCount = new BigInteger("0");
	private BigInteger negativeSentimentIndexCount = new BigInteger("0");
	private BigInteger neutralSentimentIndexCount = new BigInteger("0");
	private BigInteger mixedSentimentIndexCount = new BigInteger("0");

	public String getTweetDate() {
		return tweetDateStr;
	}

	public void setTweetDate(String tweetDate) {
		this.tweetDateStr = tweetDate;
	}

	public BigInteger getPositiveSentimentIndexCount() {
		return positiveSentimentIndexCount;
	}

	public void setPositiveSentimentIndexCount(
			BigInteger positiveSentimentIndexCount) {
		this.positiveSentimentIndexCount = positiveSentimentIndexCount;
	}

	public BigInteger getNegativeSentimentIndexCount() {
		return negativeSentimentIndexCount;
	}

	public void setNegativeSentimentIndexCount(
			BigInteger negativeSentimentIndexCount) {
		this.negativeSentimentIndexCount = negativeSentimentIndexCount;
	}

	public BigInteger getNeutralSentimentIndexCount() {
		return neutralSentimentIndexCount;
	}

	public void setNeutralSentimentIndexCount(
			BigInteger neutralSentimentIndexCount) {
		this.neutralSentimentIndexCount = neutralSentimentIndexCount;
	}

	public BigInteger getMixedSentimentIndexCount() {
		return mixedSentimentIndexCount;
	}

	public void setMixedSentimentIndexCount(BigInteger mixedSentimentIndexCount) {
		this.mixedSentimentIndexCount = mixedSentimentIndexCount;
	}

	@Override
	public int compareTo(TweetSentimentIndexCount other) {
		
		int result  = 0;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			Date tweetDateDtThis = dateFormat.parse(tweetDateStr);
			
			
			Date tweetDateDtOther = dateFormat.parse(other.getTweetDate());
			
			if (tweetDateDtThis.after(tweetDateDtOther)) {
				result = 1;
			} else if (tweetDateDtThis.before(tweetDateDtOther)) {
				result= -1;
			}
			
			
			
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  result;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((tweetDateStr == null) ? 0 : tweetDateStr.hashCode());
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
		TweetSentimentIndexCount other = (TweetSentimentIndexCount) obj;
		if (tweetDateStr == null) {
			if (other.tweetDateStr != null)
				return false;
		} else if (!tweetDateStr.equals(other.tweetDateStr))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TweetSentimentIndexCount [tweetDateStr=" + tweetDateStr
				+ ", positiveSentimentIndexCount="
				+ positiveSentimentIndexCount
				+ ", negativeSentimentIndexCount="
				+ negativeSentimentIndexCount + ", neutralSentimentIndexCount="
				+ neutralSentimentIndexCount + ", mixedSentimentIndexCount="
				+ mixedSentimentIndexCount + "]";
	}
	
	 

}
