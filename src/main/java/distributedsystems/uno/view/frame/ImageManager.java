package distributedsystems.uno.view.frame;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import distributedsystems.uno.model.card.CARD_COLOR;
import distributedsystems.uno.model.card.SPECIAL_CARD_TYPE;

/**
 * Class responsible for Images and font related to cards, background and game
 * field
 * 
 * @author Cecilia Falchi
 *
 */
public class ImageManager {

	public static Font font;
	private HashMap<String, BufferedImage> compMap = new HashMap<String, BufferedImage>();
	private HashMap<String, BufferedImage> cardMap = new HashMap<String, BufferedImage>();

	public ImageManager() {
		loadComponentImages();
		loadCardImages();
		try {
			installFont();
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clear a given BufferedImage
	 * 
	 * @param img
	 *            BufferedImage to clear
	 */
	public void clearImage(BufferedImage img) {
		Graphics2D g = img.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, img.getWidth(),
				img.getHeight());
		g.fill(rect);
		g.dispose();
	}

	/**
	 * Load all needed images and add them in Components map
	 */
	private void loadComponentImages() {
		String[] names = { "wallpaper.png", "white.png", "shadow.png",
				"play.png", "playDisabled.png", "playFocus.png", "draw.png",
				"drawDisabled.png", "drawFocus.png", "WILDico.png", "uno.png",
				"unoFocus.png" };
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>(
				names.length);
		for (int i = 0; i < names.length; i++) {
			BufferedImage current = getResourceImage(names[i], "");
			images.add(current);
			compMap.put(names[i], (BufferedImage) images.get(i));
		}
	}

	/**
	 * Gets a {@link BufferedImage} using given file name and file extension
	 * 
	 * @param fileName
	 *            File name string
	 * @param ext
	 *            Extension string
	 * @return A {@link BufferedImage} if found, null otherwise
	 */
	public BufferedImage getResourceImage(String fileName, String ext) {
		String imageDirectory = "/images/";
		URL imgURL = getClass().getResource(imageDirectory + fileName + ext);
		BufferedImage image = null;
		try {
			image = ImageIO.read(imgURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * Install Arista font reading from file
	 * 
	 * @see <a href="https://fonts2u.com/arista.font">Arista Font</a>
	 * 
	 * @throws FontFormatException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private void installFont() throws FontFormatException, IOException,
			URISyntaxException {
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		InputStream is = ImageManager.class.getClassLoader()
				.getResourceAsStream("Arista.ttf");
		File tempFile = File.createTempFile("temp", "font");
		tempFile.deleteOnExit();
		try (FileOutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(is, out);
		}
		font = Font.createFont(Font.TRUETYPE_FONT, tempFile);
		ge.registerFont(font);
	}

	/**
	 * Load images for every Uno Card
	 */
	private void loadCardImages() {
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();

		BufferedImage card = null;
		BufferedImage white = getComp("white.png");
		BufferedImage shadow = getComp("shadow.png");

		final int CARD_COUNT = 43;
		final int NUMBER_CARD_COUNT = CARD_COUNT - 3;
		final int RAINBOW_CARD_INDEX = CARD_COUNT - 1;
		final int SPECIAL_COLOR_CARD_INDEX = RAINBOW_CARD_INDEX - 1;
		final int BACK_CARD_INDEX = SPECIAL_COLOR_CARD_INDEX - 1;

		for (int i = 0; i < CARD_COUNT; i++) {
			BufferedImage img = gc.createCompatibleImage(126, 186,
					BufferedImage.TRANSLUCENT);
			RenderingHints hints = new RenderingHints(
					RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY));
			hints.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON));

			if (i < NUMBER_CARD_COUNT) {
				loadNumberCardImages(white, shadow, i, img);
			} else if (i == BACK_CARD_INDEX) {
				loadBackCardImages(white, shadow, img);
			} else if (i == SPECIAL_COLOR_CARD_INDEX) {
				loadSpecialColorCardImages(gc, white, shadow);
			} else if (i == RAINBOW_CARD_INDEX) {
				loadSpecialRainbowCardImages(gc, white, shadow);
			}
		}
	}

	/**
	 * Load images for special rainbow-colored cards
	 * 
	 * @param gc
	 * @param white
	 * @param shadow
	 */
	private void loadSpecialRainbowCardImages(GraphicsConfiguration gc,
			BufferedImage white, BufferedImage shadow) {
		BufferedImage card;
		BufferedImage img;
		for (SPECIAL_CARD_TYPE type : SPECIAL_CARD_TYPE.getRainbowType()) {
			img = gc.createCompatibleImage(126, 186, BufferedImage.TRANSLUCENT);
			card = getResourceImage(type.name(), ".png");
			Graphics2D g = img.createGraphics();
			g.drawImage(shadow, 0, 0, 126, 186, null);
			g.drawImage(white, 3, 3, 120, 180, null);
			g.drawImage(card, 3, 3, 120, 180, null);
			g.dispose();
			cardMap.put(type.name(), img);
		}
	}

	/**
	 * Load images for special colored cards
	 * 
	 * @param gc
	 * @param white
	 * @param shadow
	 */
	private void loadSpecialColorCardImages(GraphicsConfiguration gc,
			BufferedImage white, BufferedImage shadow) {
		BufferedImage card;
		BufferedImage img;
		for (SPECIAL_CARD_TYPE type : SPECIAL_CARD_TYPE.getColorType()) {
			for (CARD_COLOR color : CARD_COLOR.getValidColor()) {
				img = gc.createCompatibleImage(126, 186,
						BufferedImage.TRANSLUCENT);
				String fileName = type.name() + "_" + color.name();
				card = getResourceImage(fileName, ".png");
				Graphics2D g = img.createGraphics();
				g.drawImage(shadow, 0, 0, 126, 186, null);
				g.drawImage(white, 3, 3, 120, 180, null);
				g.drawImage(card, 3, 3, 120, 180, null);
				g.dispose();
				cardMap.put(fileName, img);
			}
		}
	}

	/**
	 * Load images for cards' back
	 * 
	 * @param white
	 * @param shadow
	 * @param img
	 */
	private void loadBackCardImages(BufferedImage white, BufferedImage shadow,
			BufferedImage img) {
		BufferedImage card;
		card = getResourceImage("back", ".png");
		Graphics2D g = img.createGraphics();
		g.drawImage(shadow, 0, 0, 126, 186, null);
		g.drawImage(white, 3, 3, 120, 180, null);
		g.drawImage(card, 3, 3, 120, 180, null);
		g.dispose();
		cardMap.put("back", img);
	}

	/**
	 * Load images for Number cards
	 * 
	 * @param white
	 * @param shadow
	 * @param i
	 * @param img
	 */
	private void loadNumberCardImages(BufferedImage white,
			BufferedImage shadow, int i, BufferedImage img) {
		BufferedImage card;
		String colour = null;
		final int COLOR_COUNT = 4;
		int s = (i + COLOR_COUNT) % COLOR_COUNT;
		int number = i / COLOR_COUNT;
		if (s == 0)
			colour = "BLUE";
		else if (s == 1)
			colour = "GREEN";
		else if (s == 2)
			colour = "RED";
		else if (s == 3)
			colour = "YELLOW";
		card = getResourceImage(number + colour, ".png");
		Graphics2D g = img.createGraphics();
		g.drawImage(shadow, 0, 0, 126, 186, null);
		g.drawImage(white, 3, 3, 120, 180, null);
		g.drawImage(card, 3, 3, 120, 180, null);
		g.dispose();
		cardMap.put(number + colour, img);
	}

	public BufferedImage getComp(String file) {
		return compMap.get(file);
	}

	public BufferedImage getCard(String code) {
		return cardMap.get(code);
	}

}