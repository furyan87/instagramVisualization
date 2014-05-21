package de.prochnow.instaScraper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.TreeSet;

import org.jinstagram.entity.users.feed.MediaFeedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Media {

    private final static Logger logger = LoggerFactory.getLogger(Media.class);

    public static final String DEFAULT_FILE_EXT = ".jpg";

    public static final String DEFAULT_PIC_LOCATION = "pictures";

    public Picture picture;

    public long userID;

    public TreeSet<Tag> tags = new TreeSet<Tag>();

    public Media(final long userID, final Picture pic, final TreeSet<Tag> tags) {
        this.userID = userID;
        this.picture = pic;
        this.tags = tags;

        this.downloadPicture();
    }

    private void downloadPicture() {

        URL url = null;
        try {
            url = new URL(this.picture.url);
        }
        catch(MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String destinationPath = DEFAULT_PIC_LOCATION + File.separator + this.createFileNameFromTags();
        logger.debug("Image path is: {}", destinationPath);

        File pictureFile = new File(destinationPath);
        if(!pictureFile.exists()) {
            try {
                pictureFile.createNewFile();
            }
            catch(IOException e) {
                logger.error("Couldn't create file with path {} : {}", destinationPath, e);
            }
        }
        logger.debug("Created file successfully or already existed");
        logger.debug("Loading image from {}", url);
        try (InputStream is = url.openStream();
                OutputStream os = new FileOutputStream(destinationPath);) {
            byte[] b = new byte[2048];
            int length;

            while((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        }
        catch(IOException e) {
            logger.error("Could'nt load and save image from {} : {}", url, e);
        }

        logger.info("Image {} created from {}", destinationPath, url);
    }

    private String createFileNameFromTags() {
        String filename = String.valueOf(this.userID);

        for(Tag tag : this.tags) {
            filename = filename + "_" + tag.name;
        }

        filename = filename + DEFAULT_FILE_EXT;
        logger.debug("Filename is {}", filename);
        return filename;

    }

    public static Media createMediaFromMediaFeedData(final long userID, final MediaFeedData data) {
        String dataUrl = data.getImages().getStandardResolution().getImageUrl();
        logger.debug("URL of picture is {}", dataUrl);
        Picture pic = null;
        try {
            pic = new Picture(dataUrl);
        }
        catch(MalformedURLException e) {
            logger.error(e.getMessage(), dataUrl);
        }

        List<String> dataTags = data.getTags();
        logger.debug("TAGS for picture are {}", dataTags);
        TreeSet<Tag> tags = new TreeSet<Tag>();
        for(String tag : dataTags) {
            Tag newTag = new Tag(tag);
            logger.debug("Created MEDIA-TAG {}", newTag);
            boolean tagAdded = tags.add(newTag);
            logger.debug("Added newTag to list of MEDIA-TAGS: {}", tagAdded);
        }

        logger.info("Final MEDIA-TAG list is: {}", tags);

        return new Media(userID, pic, tags);
    }

}
