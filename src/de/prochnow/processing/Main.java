package de.prochnow.processing;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import de.prochnow.instaScraper.InstaScraper;
import de.prochnow.instaScraper.Media;
import de.prochnow.instaScraper.Tag;

public class Main extends PApplet {
	
	private final static Logger logger = LoggerFactory.getLogger(Main.class);

	int nbr_circles = 100;
	int width = 500;
	int height = 500;
	float[] xPoints;
	float[] yPoints;
	float posX;
	float posY;
	List<Media> feed;
	
	
	PImage img;
	int hue; // actual color
	static final int hueRange = 360;
	float saturation;
	float brightness;

	PFont font;

	public void setup() {
		size(width, height);
		smooth();
		background(232, 232, 232);
		noLoop();
		colorMode(HSB, (hueRange-1));

		feed = InstaScraper.getRecentMediaFromUser();
		nbr_circles = feed.size()*3;
		xPoints = new float[nbr_circles];
		yPoints = new float[nbr_circles];
		logger.debug("Number of media {}", nbr_circles);


	}

	public void draw() {
		float lg_diam = (float) (width * .50); // large circle´ diameter
		float lg_rad = lg_diam / 2; // large circle´ radius
		float lg_circ = PI * lg_diam; // large circumference
		float sm_diam = (lg_circ / nbr_circles); // small circles´ diameter
		float lg_rad_big = (float) ((width * .80) / 2); // outer circle radius
		float cx = (float) (width / 2.0);
		float cy = (float) (height / 2.0);

		
		
		fill(139, 121, 94, 127);
		noStroke();
		
		
		int[] randomPos = createRandomPosition();
		

		for (int index = 0; index < nbr_circles; index = index+3) {
			Media currentMedia = feed.get(index/3);

			// create random color
			int[] pictureColor = extractColorFromImage(currentMedia);
			logger.debug("Color from picture {}", pictureColor);

			for (int i = 0; i < pictureColor.length; i++) {
				float angle = (randomPos[index+i] + 1) * TWO_PI / nbr_circles;
				posX = cx + cos(angle) * lg_rad;
				posY = cy + sin(angle) * lg_rad;
				fill(pictureColor[i], 127);
				
				// outer circle
				ellipse(posX, posY, sm_diam, sm_diam);
				
				fill(pictureColor[i]);
				
				ellipse(posX, posY, sm_diam / 3, sm_diam / 3);
				xPoints[index+i] = posX;
				yPoints[index+i] = posY;
			}

			// inner part for the circle

			// sace points for later
			currentMedia.picture.x = posX;
			currentMedia.picture.y = posY;
			

		}

		// coloring for connection lines
		noFill();
		stroke(140, 140, 140);

		// paint bezier connections
		for (int index = 0; index < nbr_circles; index = index +3) {
			
			bezier(xPoints[index], yPoints[index], cx, cy, cx, cy, xPoints[index+1],
					yPoints[index+1]);
			bezier(xPoints[index+1], yPoints[index+1], cx, cy, cx, cy, xPoints[index+2],
					yPoints[index+2]);
			bezier(xPoints[index+2], yPoints[index+2], cx, cy, cx, cy, xPoints[index],
					yPoints[index]);
		}

		Object[] tags = extractTagsFromMedia();

		// format
		font = createFont("Helvetica", 10);
		textFont(font, 10);

		// space between tags
		float delta = TWO_PI / tags.length;

		// print tags
		for (int index = 0; index < tags.length; index++) {
			posX = width / 2 + lg_rad_big * cos(index * delta);
			posY = height / 2 + lg_rad_big * sin(index * delta);

			pushMatrix();
			translate(posX, posY);
			rotate(delta * index);
			fill(0);
			text(((Tag)tags[index]).name, 0, 0);
			popMatrix();
		}
	}
	
	private int[] createRandomPosition() {
		int size = nbr_circles;
		List<Integer> basic_array = new LinkedList<Integer>();
		int[] result = new int[nbr_circles];
		
		for (int i = 0; i < nbr_circles; i++) {
			basic_array.add(i+1);
		}
		
		for (int i = 0; i < nbr_circles; i++) {
			int number = basic_array.remove((int)(Math.random() * basic_array.size()));
			result[i] = number;
			
			
		}
		
		return result;
		
	}

	private int[] extractColorFromImage(Media media) {
		logger.debug("path to media {}", media.picture.path);
		PImage img = loadImage(media.picture.path);
		logger.debug("img object for media {}", img);
		 // definiert Farbsystem der Ausgabe
		img.loadPixels();
		int numberOfPixels = img.pixels.length;
		int[] hues = new int[hueRange];
		float[] saturations = new float[hueRange]; //ist ein array
		float[] brightnesses = new float[hueRange]; //ist ein array
		
		for (int i = 0; i < numberOfPixels; i++) { // eine Schleife. Geht jeden Pixel durch um die Farbe herraus zu ziehen.
			int pixel = img.pixels[i];
			int hue = Math.round(hue(pixel));
			float saturation = saturation(pixel);
			float brightness = brightness(pixel);
			hues[hue]++;
			saturations[hue] += saturation;
			brightnesses[hue] += brightness;
			
		}
		// Find the most common hue.
		int hueCount[]= new int [3];
		
		int hue[] = new int[3];
		for(int i = 1; i < hues.length; i++) {
			if (hues[i] > hueCount[0]) {
				hueCount[2] = hueCount[1];
				hueCount[1] = hueCount[0];
				hueCount[0] = hues[i];
				hue[2] = hue[1];
				hue[1] = hue[0];
				hue[0] = i;
			}
		}

		int[] result = new int[3];
		// Set the variable for displaying the color
		for (int i = 0; i < hue.length; i++) {
			
			saturation = saturations[hue[i]] / hueCount[i];
			brightness = brightnesses[hue[i]] / hueCount[i];
			result[i] = color(hue[i],saturation,brightness);
		}
		
		
		return result;
		
		}

	private Object[]  extractTagsFromMedia() {
		TreeSet<Tag> tags = new TreeSet<Tag>();

		for (Media media : feed) {
			tags.addAll(media.tags);
		}
		
		return tags.toArray();

	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "de.prochnow.processing.Main" });
	}
}