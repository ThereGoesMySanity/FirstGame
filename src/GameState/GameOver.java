package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.Background;

public class GameOver extends GameState {
	private Background bg;
	private int currentChoice = 0;
	private Font font;
	private BufferedImage image;
	
	private String[] options = {
			"Start Over",
			"Menu"
	};
	public GameOver(GameStateManager gsm){
		this.gsm = gsm;
		init();
	}
	public void init() {
		
		try{
			image = ImageIO.read(getClass().getResourceAsStream("/Sprites/ThrowingStar1.png"));
			bg = new Background("/Background/menubgmod.png", 0);
		}catch(Exception e){e.printStackTrace();}
		font = new Font("Fixedsys", Font.TRUETYPE_FONT, 24);
	}

	public void update() {}

	public void draw(Graphics2D g) {
		bg.draw(g);
		g.setColor(Color.MAGENTA);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
        int x = ((320 - fm.stringWidth("Game Over")) / 2);
		g.drawString("Game Over", x, 32);
		for(int i = 0; i<options.length; i++){
			x = ((320 - fm.stringWidth(options[i])) / 2);
			if(i==currentChoice){
				g.setColor(Color.DARK_GRAY);
			}else{
				g.setColor(Color.WHITE);
			}
			
			g.drawString(options[i], x, 100+(32*i));
			g.drawImage(image, 140-(fm.stringWidth(options[currentChoice])/2), 82+currentChoice*32, null);
		}
	}

	public void keyPressed(int k) {
		switch(k){
		case KeyEvent.VK_ENTER:
			select();
			break;
		case KeyEvent.VK_UP:
			currentChoice--;
			if (currentChoice == -1){
				currentChoice = options.length - 1;
			}
			break;
		case KeyEvent.VK_DOWN:
			currentChoice++;
			if (currentChoice == options.length){
				currentChoice = 0;
			}
			break;
		}
	}

	public void keyReleased(int k) {

	}
	public void select(){
		switch(currentChoice){
		case 0:
			gsm.setState(GameStateManager.LEVEL1STATE);
			break;
		case 1:
			gsm.setState(GameStateManager.MENUSTATE);
			break;
		}
	}

}
