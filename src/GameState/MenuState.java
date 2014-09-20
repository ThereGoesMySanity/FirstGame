package GameState;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;
import TileMap.*;

public class MenuState extends GameState{

	private Background bg;
	private int currentChoice = 0;
	private String[] options = {
		"Start",
		"Options",
		"Help",
		"Quit"
	};
	private Color titleColor;
	private Font titleFont;
	private BufferedImage image;
	private BufferedImage loading;
	
	public MenuState(GameStateManager gsm){
		this.gsm = gsm;
		try{
			loading = ImageIO.read(getClass().getResourceAsStream("/Background/loading.png"));
			image = ImageIO.read(getClass().getResourceAsStream("/Sprites/ThrowingStar1.png"));
			bg = new Background("/Background/menubgmod.png", 1);
			bg.setVector(5, 0);
			titleColor = new Color (128, 0, 0);
			titleFont = new Font("Fixedsys", Font.TRUETYPE_FONT, 28);
		}catch(Exception e){e.printStackTrace();}
		
	}
	public void init(){}
	public void update(){
		bg.update();
	}
	public void draw(Graphics2D g){
		
		bg.draw(g);
		g.setColor(titleColor);
		g.setFont(titleFont);
		FontMetrics fm = g.getFontMetrics();
        int x = ((320 - fm.stringWidth("Ninja Game")) / 2);
        int y = ((240 - fm.getHeight()) / 2) + fm.getAscent();
		g.drawString("Ninja Game", x, y-32);
		
		g.setFont(titleFont);
		for(int i = 0; i < options.length; i++){
			if(i==currentChoice){
				g.setColor(Color.DARK_GRAY);
			}else{
				g.setColor(Color.WHITE);
			}
			g.drawString(options[i], 180, 140 + i*30);
			g.drawImage(image, 160, 122+currentChoice*30, null);
		}
	}
	private void select(){
		switch(currentChoice){
		case 0:
			GamePanel.drawImage(loading);
			gsm.setState(GameStateManager.LEVEL1STATE);
			break;
		case 1:
			gsm.setState(GameStateManager.OPTIONSSTATE);
			break;
		case 2:
			//help
			break;
		case 3:
			System.exit(0);
		}
	}
	
	public void keyPressed(int k){
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
	public void keyReleased(int k){}
}
