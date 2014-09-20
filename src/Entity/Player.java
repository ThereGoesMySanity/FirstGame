package Entity;
import Audio.AudioPlayer;
import TileMap.*;

import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
public class Player extends MapObject {
	public static boolean glitch;
	public boolean sanic;
	private boolean sanicd;
	private HashMap<String, AudioPlayer> sfx;
	
	public void setSanic(){
		sanic = true;
		if(!sanicd){
			try{
				BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Sanic.png"));
				sprites = new ArrayList<BufferedImage[]>();
				for(int i=0; i<4; i++){
					BufferedImage[] bi = new BufferedImage[numFrames[i]];
					for(int j = 0; j<numFrames[i]; j++){
						if(i!=3){
							bi[j] = spritesheet.getSubimage(j*width, i*height, width, height);
						}else{
							bi[j] = spritesheet.getSubimage(j*2*width, i*height, width, height);
						}			
					}
					sprites.add(bi);
				}
			}catch(Exception e){e.printStackTrace();}
			sanicd = true;
		}
	}
	public boolean isSanic(){
		return sanic;
	}
	public static void setGlitch(boolean g) {
		glitch = g;
	}
	public static boolean isGlitch(){
		return glitch;
	}
	private boolean prevFiring;
	private int maxHealth;
	private int bullet;
	private int maxBullets;
	private boolean dead;
	private boolean flinching;
	private long flinchTime;
	private ArrayList<Bullet> bullets;
	private boolean firing;
	private int bulletCost;
	private int bulletDamage;
	public boolean played;
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {1, 3, 4, 4};
	private static final int IDLE = 0;
	private static final int RUNNING = 1;
	private static final int JUMPING = 2;
	private static final int SHOOTING = 3;
	public Player(TileMap tm){
		super(tm);
		width = 22;
		height = 32;
		cwidth = 14;
		cheight = 26;
		moveSpeed = 0.6;
		maxSpeed = 2;
		stopSpeed = 1;
		jumpStart = -4.8;
		fallSpeed = 0.15;
		maxFallSpeed = 4;
		stopJumpSpeed = 3;
		health = maxHealth = 25;
		bullet = maxBullets = 1997;
		bulletCost = 100;
		bulletDamage = 1;
		bullets = new ArrayList<Bullet>();
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("sanicHit", new AudioPlayer("/SFX/sonic017.mp3"));
		sfx.put("sanicSlow", new AudioPlayer("/SFX/sonic005.mp3"));
		sfx.put("sanicStep", new AudioPlayer("/SFX/sonic006.mp3"));
		try{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player.png"));
			sprites = new ArrayList<BufferedImage[]>();
			for(int i=0; i<4; i++){
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j<numFrames[i]; j++){
					if(i!=3){
						bi[j] = spritesheet.getSubimage(j*width, i*height, width, height);
					}else{
						bi[j] = spritesheet.getSubimage(j*2*width, i*height, width, height);
					}
					
				}
				sprites.add(bi);
			}
			
		}catch(Exception e){e.printStackTrace();}
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(-1);
	}
	public int getMaxBullets() {
		return maxBullets;
	}
	public int getHealth() {
		return health;
	}
	public int getMaxHealth() {
		return maxHealth;
	}
	public int getBullet() {
		return bullet;
	}
	public void setFiring(boolean b){
		firing = b;
	}
	public void update(){
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		if(currentAction == SHOOTING && animation.hasPlayedOnce())firing = false;
		bullet += 3;
		if(bullet>maxBullets)bullet = maxBullets;
		if(prevFiring != firing){
			if (firing){
				if(bullet>bulletCost){
					bullet -= bulletCost;
					
					Bullet b = new Bullet(tileMap, facingRight);
					b.setPosition(x, y);
					bullets.add(b);
				}	
			}
			prevFiring = firing;
		}else{played = true;}
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).update();
			if(bullets.get(i).shouldRemove()){
				bullets.remove(i);
				i--;
			}
		}
		if((firing&&currentAction != SHOOTING)&&(!played||glitch)){
			currentAction = SHOOTING;
			animation.setFrames(sprites.get(SHOOTING));
			animation.setDelay(20);
			width = 22;
		}else if(dy!=0){
			if(currentAction != JUMPING){
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(60);
				width = 22;
			}
		}else if(left||right){
			if(currentAction != RUNNING){
				if(sanic){
					int sanicRand = (int) (Math.random()*8);
					if(sanicRand == 3)sfx.get("sanicSlow").play();
					if(sanicRand == 7)sfx.get("sanicStep").play();
				}
				currentAction = RUNNING;
				animation.setFrames(sprites.get(RUNNING));
				animation.setDelay(80);
				width = 22;
			}
		}else if(currentAction !=IDLE){
			currentAction = IDLE;
			animation.setFrames(sprites.get(IDLE));
			animation.setDelay(-1);
			width = 22;
		}
		animation.update();

		if(currentAction != SHOOTING){
			if(right)facingRight = true;
			if(left)facingRight = false;
		}
		if(flinching&&flinchTime/9999999>80000000){
			flinching = false;
			System.out.println("done");
		}
	}
	private void getNextPosition() {
		
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		
		// jumping
		if(jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}
		
		// falling
		if(falling) {
			
			dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			
			if(dy > maxFallSpeed) dy = maxFallSpeed;
			
		}
		
	}
	public void draw(Graphics2D g){
		setMapPosition();
		for(int i=0; i<bullets.size(); i++){
			bullets.get(i).draw(g);
		}
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTime) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		super.draw(g);
	}
	public void checkAttack(ArrayList<Enemy> enemies){
		for(int i = 0; i<enemies.size(); i++){
			Enemy e = enemies.get(i);
			for(int j = 0; j<bullets.size(); j++){
				if(bullets.get(j).intersects(e)){
					e.hit(bulletDamage);
					bullets.get(j).setHit();
					System.out.println("hit, " +e.getHealth());
				}
			}
			if(intersects(e)){
				if(!((jumping||falling)&&sanic)){
					hit(e.getDamage());
					if(sanic){
						sfx.get("sanicHit").play();
					}
				}else if(sanic&&(jumping||falling)){
					e.hit(10);
				}
			}
		}
	}
	public void hit(int damage){
		if(flinching)return;
		health-=damage;
		if(health<=0){
			dead = true;
			health = 0;
		}
		flinching = true;
		flinchTime = System.nanoTime();
	}
	public boolean getStairs(){
		return blStair||brStair;
	}
	public boolean isDead(){
		return dead;
	}
}
