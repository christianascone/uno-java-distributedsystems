package sistemidistribuiti.uno.view.frame;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import sistemidistribuiti.uno.model.card.CARD_COLOR;
import sistemidistribuiti.uno.model.card.SPECIAL_CARD_TYPE;


public class ImageManager {
	
	private HashMap<String, BufferedImage> compMap = new HashMap<String, BufferedImage>();
	private HashMap<String, BufferedImage> cardMap = new HashMap<String, BufferedImage>();

	public ImageManager() {
		loadComponentImages();
	    loadCardImages();
	}
	
	public void clearImage(BufferedImage img){  
		Graphics2D g = img.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		Rectangle2D.Double rect = 
				new Rectangle2D.Double(0,0,img.getWidth(),img.getHeight()); 
		g.fill(rect);
		g.dispose();
	}

	private void loadComponentImages() {
		String[] names = { 
			      "wallpaper.png",
			      "white.png",
			      "shadow.png",
			      "play.png",
			      "playDisabled.png",
			      "draw.png",
			      "drawDisabled.png"
			    };
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>(names.length);
	    for (int i = 0; i < names.length; i++) {
	      BufferedImage current = getResourceImage(names[i], "");
	      images.add(current);
	      compMap.put(names[i], (BufferedImage)images.get(i));
	    }
	}

	public BufferedImage getResourceImage(String fileName, String ext)  {
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

	public void loadCardImages() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice gs = ge.getDefaultScreenDevice();
	    GraphicsConfiguration gc = gs.getDefaultConfiguration();
	    
	    BufferedImage card = null;
	    BufferedImage white = getComp("white.png");
	    BufferedImage shadow = getComp("shadow.png");
	    
	    for(int i=0; i<43; i++){
	    	BufferedImage img = gc.createCompatibleImage(126, 186,BufferedImage.TRANSLUCENT);
	    	RenderingHints hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		    hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		    hints.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
	    	
	        if (i<40){
	        	String colour = null;
	        	int s = (i + 4) % 4;
	            int number = i / 4;
	            if (s == 0) colour = "BLUE";
	            else if (s == 1) colour = "GREEN";
	            else if (s == 2) colour = "RED";
	            else if (s == 3) colour = "YELLOW";
	            card = getResourceImage(number + colour, ".png");
	            Graphics2D g = img.createGraphics();
	            g.drawImage(shadow, 0, 0, 126, 186, null);
	            g.drawImage(white, 3,3, 120, 180, null);
	            g.drawImage(card, 3,3, 120, 180,  null);
	            g.dispose();
	    	    cardMap.put(number+colour, img);
	        } else if (i == 40){
	        	card = getResourceImage("back", ".png");
	        	Graphics2D g = img.createGraphics();
	        	g.drawImage(shadow, 0, 0, 126, 186, null);
	            g.drawImage(white, 3,3, 120, 180, null);
	            g.drawImage(card, 3, 3,120, 180,  null);
	            g.dispose();
	            cardMap.put("back", img);
	        } else if (i== 41){
	        	for(SPECIAL_CARD_TYPE type : SPECIAL_CARD_TYPE.getColorType()){
	        		for(CARD_COLOR color : CARD_COLOR.getValidColor()){
	        			img = gc.createCompatibleImage(126, 186,BufferedImage.TRANSLUCENT);
	        			String fileName = type.name()+"_"+color.name();
		        		card = getResourceImage(fileName, ".png");
			        	Graphics2D g = img.createGraphics();
			        	g.drawImage(shadow, 0, 0, 126, 186, null);
			            g.drawImage(white, 3,3, 120, 180, null);
			            g.drawImage(card, 3, 3,120, 180,  null);
			            g.dispose();
		        		cardMap.put(fileName, img);
	        		}
	        	}
	        } else if (i == 42){
	        	for(SPECIAL_CARD_TYPE type : SPECIAL_CARD_TYPE.getRainbowType()){
	        		img = gc.createCompatibleImage(126, 186,BufferedImage.TRANSLUCENT);
	        		card = getResourceImage(type.name(), ".png");
		        	Graphics2D g = img.createGraphics();
		        	g.drawImage(shadow, 0, 0, 126, 186, null);
		            g.drawImage(white, 3,3, 120, 180, null);
		            g.drawImage(card, 3, 3,120, 180,  null);
		            g.dispose();
	        		cardMap.put(type.name(), img);
	        	}
	        }
	    }
	}

	public BufferedImage getComp(String file) {
		return compMap.get(file);
	}
	
	public BufferedImage getCard(String code) {
		return cardMap.get(code);
	}
	
}