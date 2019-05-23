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

import com.lifotech.rtsa.web.spring.domain.TweetSentimentIndexCount;
import com.lifotech.rtsa.web.spring.service.impl.TweetStatsServiceHBaseImpl;

/**
 * The controller is used to display bar chart.
 *  
 * @author SK Singh
 */
@Controller
public class DailyChartDataController {

	private static final Logger logger = LoggerFactory.getLogger(DailyChartDataController.class);

	@RequestMapping("/getDailyChartData")
	public @ResponseBody
	List<TweetSentimentIndexCount> getDailyChartData(@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam("clientID") String clientID) {

		logger.info("startDate " + startDate);
		logger.info("endDate " + endDate);
		logger.info("clientDate " + clientID);

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
			Collections.sort(list);

		} catch (Exception e) {			
			e.printStackTrace();
		}

		logger.info("list size " + list.size());
		return list;

	}

	
}