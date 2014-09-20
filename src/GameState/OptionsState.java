package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Bullet;
import Entity.Player;
import TileMap.Background;

public class OptionsState extends GameState {
	private Background bg;
	public boolean glitches = false;
	public String[] starsColor = {
			"",
			"grey",
			"pink",
			"cat"
	};
	private String[] starsColorRefined = {
			"Normal",
			"Grey",
			"Pink",
			"Cat"
	};
	private int starsChoice = 1;
	private int currentChoice = 0;
	private String[] options = {
		"Glitches: ",
		"Music: ",
		"Throwing Star Color: ",
		"Quit"
	};
	private Color titleColor;
	private Font titleFont;
	private Font font;
	private BufferedImage image;
	
	public OptionsState(GameStateManager gsm){
		this.gsm = gsm;
		try{
			image = ImageIO.read(getClass().getResourceAsStream("/Sprites/ThrowingStar1.png"));
			bg = new Background("/Background/menubgmod.png", 1);
			bg.setVector(5, 0);
			titleColor = new Color (128, 0, 0);
			titleFont = new Font("Fixedsys", Font.TRUETYPE_FONT, 28);
			font = new Font("Fixedsys", Font.TRUETYPE_FONT, 16);
		}catch(Exception e){e.printStackTrace();}
		
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		bg.update();

	}

	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);
		g.setColor(titleColor);
		g.setFont(titleFont);
		FontMetrics fm = g.getFontMetrics();
        int x = ((320 - fm.stringWidth("Options")) / 2);
        int y = ((240 - fm.getHeight()) / 2) + fm.getAscent();
		g.drawString("Options", x, y-64);
		g.setFont(font);
		options[0] = "Glitches: " + Boolean.toString(glitches);
		options[2] = "Throwing Star Color: " + starsColorRefined[starsChoice];
		for(int i = 0; i < options.length; i++){
			if(i==currentChoice){
				g.setColor(Color.DARK_GRAY);
			}else{
				g.setColor(Color.WHITE);
			}
			g.drawString(options[i], 100, 100 + i*30);
			g.drawImage(image, 80, 82+currentChoice*30, null);
		}

	}
	private void select(){
		switch(currentChoice){
		case 0:
			if(!glitches)glitches = true;
			else glitches = false;
			Player.setGlitch(glitches);
			System.out.println(glitches);
			System.out.println("2: "+ Player.isGlitch());
			break;
		case 1:
			gsm.setState(GameStateManager.OPTIONSSTATE);
			break;
		case 2:
			starsChoice++;
			if(starsChoice==starsColor.length)starsChoice = 0;
			Bullet.setStar(starsColor[starsChoice]);
			break;
		case 3:
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}
	@Override
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

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub

	}

}
