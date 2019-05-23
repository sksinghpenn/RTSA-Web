package com.lifotech.rtsa.web.spring.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lifotech.rtsa.web.spring.domain.TweetSentimentIndexCount;
import com.lifotech.rtsa.web.spring.domain.TweetSentimentIndexCumulativeCount;
import com.lifotech.rtsa.web.spring.service.impl.TweetStatsServiceHBaseImpl;

/**
 * The class is controller for pie chart
 * 
 * @author SK Singh
 * 
 */
@Controller
public class PieChartDataController {

	private static final Logger logger = LoggerFactory.getLogger(PieChartDataController.class);

	@RequestMapping("/getDailyPieChartData")
	public @ResponseBody
	TweetSentimentIndexCumulativeCount getDailyPieChartData(@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam("clientID") String clientID) {

		String[] startDateSplit = startDate.split("/");
		Calendar tweetStartDate = Calendar.getInstance();
		tweetStartDate.set(Integer.valueOf(startDateSplit[2]), Integer.valueOf(startDateSplit[0]) - 1,
				Integer.valueOf(startDateSplit[1]), 0, 0, 1);

		String[] endDateSplit = endDate.split("/");
		Calendar tweetEndDate = Calendar.getInstance();
		tweetEndDate.set(Integer.valueOf(endDateSplit[2]), Integer.valueOf(endDateSplit[0]) - 1,
				Integer.valueOf(endDateSplit[1]), 24, 0, 0);

		String tableName = "RTSA" + clientID;

		TweetStatsServiceHBaseImpl tweetStatsServiceHBaseImpl = new TweetStatsServiceHBaseImpl();

		List<TweetSentimentIndexCount> list = new ArrayList<TweetSentimentIndexCount>();
		try {
			list = tweetStatsServiceHBaseImpl.getTweetSentimentIndexCountList(tweetStartDate.getTime(),
					tweetEndDate.getTime(), tableName);

		} catch (Exception e) {			
			e.printStackTrace();
		}

		TweetSentimentIndexCumulativeCount tweetSentimentIndexCumulativeCount = getTweetSentimentIndexCumulativeCount(list);

		System.out.println(tweetSentimentIndexCumulativeCount);
		return tweetSentimentIndexCumulativeCount;

	}

	private TweetSentimentIndexCumulativeCount getTweetSentimentIndexCumulativeCount(List<TweetSentimentIndexCount> list) {

		int positiveSentimentIndexCount = 0;
		int negativeSentimentIndexCount = 0;
		int neutralSentimentIndexCount = 0;
		int mixedSentimentIndexCount = 0;

		for (TweetSentimentIndexCount tweetSentimentIndexCount : list) {
			positiveSentimentIndexCount += tweetSentimentIndexCount.getPositiveSentimentIndexCount().intValue();
			negativeSentimentIndexCount += tweetSentimentIndexCount.getNegativeSentimentIndexCount().intValue();
			neutralSentimentIndexCount += tweetSentimentIndexCount.getNeutralSentimentIndexCount().intValue();
			mixedSentimentIndexCount = +tweetSentimentIndexCount.getMixedSentimentIndexCount().intValue();	
		}

		System.out.println("Sum of counts---");
		System.out.println(positiveSentimentIndexCount);
		System.out.println(negativeSentimentIndexCount);
		System.out.println(neutralSentimentIndexCount);
		System.out.println(mixedSentimentIndexCount);

		TweetSentimentIndexCumulativeCount tweetSentimentIndexCumulativeCount = new TweetSentimentIndexCumulativeCount();

		tweetSentimentIndexCumulativeCount.setPositiveSentimentIndexCount(new BigInteger(String
				.valueOf(positiveSentimentIndexCount)));
		tweetSentimentIndexCumulativeCount.setNegativeSentimentIndexCount(new BigInteger(String
				.valueOf(negativeSentimentIndexCount)));
		tweetSentimentIndexCumulativeCount.setMixedSentimentIndexCount(new BigInteger(String
				.valueOf(mixedSentimentIndexCount)));
		tweetSentimentIndexCumulativeCount.setNeutralSentimentIndexCount(new BigInteger(String
				.valueOf(neutralSentimentIndexCount)));

		return tweetSentimentIndexCumulativeCount;

	}

}