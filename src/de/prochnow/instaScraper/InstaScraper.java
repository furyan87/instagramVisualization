package de.prochnow.instaScraper;

import java.util.LinkedList;
import java.util.List;

import org.jinstagram.Instagram;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.prochnow.instaScraper.util.PropertyReader;

public class InstaScraper {

    private final static Logger logger = LoggerFactory.getLogger(InstaScraper.class);

    private static Instagram instagram = new Instagram(PropertyReader.getProperty("client_id"));

    private static long userID = Long.valueOf(PropertyReader.getProperty("user_id"));

    public static List<Media> getRecentMediaFromUser() {
        logger.info("Starting to get media feed from user {}", userID);
        List<Media> media = new LinkedList<Media>();
        MediaFeed feed = null;
        try {
            feed = instagram.getRecentMediaFeed(userID, 100, null, null, null, null);


        }
        catch(InstagramException e) {
            logger.error("Failed to load recent media feed for user: {}", e);
        }
        logger.info("Successfully loaded recent media feed");
        for(MediaFeedData data : feed.getData()) {
            media.add(Media.createMediaFromMediaFeedData(userID, data));
        }
        return media;
    }

    public static void main(final String[] args) {
        InstaScraper.getRecentMediaFromUser();
    }

}
