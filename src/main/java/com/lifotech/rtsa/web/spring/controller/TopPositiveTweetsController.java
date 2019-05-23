package com.lifotech.rtsa.web.spring.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lifotech.rtsa.web.spring.domain.TweetPositiveSentiment;
import com.lifotech.rtsa.web.spring.service.impl.TweetStatsServiceHBaseImpl;

/**
 * The class is controller for  top positive tweets.
 * 
 * @author SK Singh
 *
 */
@Controller
public class TopPositiveTweetsController {

	private static final Logger logger = LoggerFactory.getLogger(TopNegativeTweetsController.class);
	
	@RequestMapping("/getTopPositiveTweets")
	public @ResponseBody
	List<TweetPositiveSentiment> getTopPositiveTweet(@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam("clientID") String clientID) {

		System.out.println("getDailyChartData");

		System.out.println("startDate " + startDate);
		System.out.println("endDate " + endDate);
		System.out.println("clientDate " + clientID);		

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

		List<TweetPositiveSentiment> list = new ArrayList<TweetPositiveSentiment>();
		try {
			list = tweetStatsServiceHBaseImpl.getTopPostiveTweetSentimentList(tweetStartDate.getTime(),
					tweetEndDate.getTime(), tableName, 50);
			Collections.sort(list);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("list size " + list.size());
		return list;

	}

	

}