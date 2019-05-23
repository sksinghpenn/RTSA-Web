package com.lifotech.rtsa.web.spring.domain;

import java.io.Serializable;
import java.math.BigInteger;


/**
 * The domain class encapsulates cumulative total number of positive, negative, neutral,and mixed sentiment.
 * 
 * 
 * @author SK Singh
 *
 */
public class TweetSentimentIndexCumulativeCount implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private BigInteger positiveSentimentIndexCount = new BigInteger("0");
	private BigInteger negativeSentimentIndexCount = new BigInteger("0");
	private BigInteger neutralSentimentIndexCount = new BigInteger("0");
	private BigInteger mixedSentimentIndexCount = new BigInteger("0");

	public BigInteger getPositiveSentimentIndexCount() {
		return positiveSentimentIndexCount;
	}

	public void setPositiveSentimentIndexCount(BigInteger positiveSentimentIndexCount) {
		this.positiveSentimentIndexCount = positiveSentimentIndexCount;
	}

	public BigInteger getNegativeSentimentIndexCount() {
		return negativeSentimentIndexCount;
	}

	public void setNegativeSentimentIndexCount(BigInteger negativeSentimentIndexCount) {
		this.negativeSentimentIndexCount = negativeSentimentIndexCount;
	}

	public BigInteger getNeutralSentimentIndexCount() {
		return neutralSentimentIndexCount;
	}

	public void setNeutralSentimentIndexCount(BigInteger neutralSentimentIndexCount) {
		this.neutralSentimentIndexCount = neutralSentimentIndexCount;
	}

	public BigInteger getMixedSentimentIndexCount() {
		return mixedSentimentIndexCount;
	}

	public void setMixedSentimentIndexCount(BigInteger mixedSentimentIndexCount) {
		this.mixedSentimentIndexCount = mixedSentimentIndexCount;
	}

	@Override
	public String toString() {
		return "TweetSentimentIndexCumulativeCount [positiveSentimentIndexCount=" + positiveSentimentIndexCount
				+ ", negativeSentimentIndexCount=" + negativeSentimentIndexCount + ", neutralSentimentIndexCount="
				+ neutralSentimentIndexCount + ", mixedSentimentIndexCount=" + mixedSentimentIndexCount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mixedSentimentIndexCount == null) ? 0 : mixedSentimentIndexCount.hashCode());
		result = prime * result + ((negativeSentimentIndexCount == null) ? 0 : negativeSentimentIndexCount.hashCode());
		result = prime * result + ((neutralSentimentIndexCount == null) ? 0 : neutralSentimentIndexCount.hashCode());
		result = prime * result + ((positiveSentimentIndexCount == null) ? 0 : positiveSentimentIndexCount.hashCode());
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
		TweetSentimentIndexCumulativeCount other = (TweetSentimentIndexCumulativeCount) obj;
		if (mixedSentimentIndexCount == null) {
			if (other.mixedSentimentIndexCount != null)
				return false;
		} else if (!mixedSentimentIndexCount.equals(other.mixedSentimentIndexCount))
			return false;
		if (negativeSentimentIndexCount == null) {
			if (other.negativeSentimentIndexCount != null)
				return false;
		} else if (!negativeSentimentIndexCount.equals(other.negativeSentimentIndexCount))
			return false;
		if (neutralSentimentIndexCount == null) {
			if (other.neutralSentimentIndexCount != null)
				return false;
		} else if (!neutralSentimentIndexCount.equals(other.neutralSentimentIndexCount))
			return false;
		if (positiveSentimentIndexCount == null) {
			if (other.positiveSentimentIndexCount != null)
				return false;
		} else if (!positiveSentimentIndexCount.equals(other.positiveSentimentIndexCount))
			return false;
		return true;
	}

}
