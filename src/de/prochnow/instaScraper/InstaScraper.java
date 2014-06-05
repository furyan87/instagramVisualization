package de.prochnow.instaScraper;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jinstagram.Instagram;
import org.jinstagram.entity.common.Pagination;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prochnow.instaScraper.util.PropertyReader;

public class InstaScraper {

	private final static Logger logger = LoggerFactory
			.getLogger(InstaScraper.class);

	private static Instagram instagram = new Instagram(
			PropertyReader.getProperty("client_id"));

	private static long userID = Long.valueOf(PropertyReader
			.getProperty("user_id"));

	private static double latitude = Double.valueOf(PropertyReader
			.getProperty("latitude"));
	private static double longitude = Double.valueOf(PropertyReader
			.getProperty("longitude"));

	public static List<Media> getRecentMediaFromUser() {
		logger.info("Starting to get media feed from user {}", userID);
		List<Media> media = new LinkedList<Media>();
		MediaFeed feed = null;

		Date date = new Date(0);
		System.out.println(date);
		try {
			// feed = instagram.getRecentMediaFeed(userID, 100, null, null, new
			// Date(), new Date(0));

			feed = instagram.searchMedia(latitude, longitude, new Date(),
					new Date(0), 5000);

		} catch (InstagramException e) {
			logger.error("Failed to load recent media feed for user: {}", e);
		}
		logger.info("Successfully loaded recent media feed");
		for (MediaFeedData data : feed.getData()) {
			media.add(Media.createMediaFromMediaFeedData(userID, data));
		}
		return media;
	}

}
