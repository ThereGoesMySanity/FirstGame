package GameState;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Audio.AudioPlayer;
import Entity.*;
import Entity.Enemies.*;
import TileMap.*;
public class Level1State extends GameState{
	private TileMap tileMap;
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	private HUD hud;
	public FlyingNinja bob;
	private Background background;
	private AudioPlayer bgMusic;
	private AudioPlayer sanic;
	private boolean s=false;
	private boolean a=false;
	private boolean n=false;
	private boolean i=false;
	private boolean c=false;
	public Level1State(GameStateManager gsm){
		this.gsm = gsm;
		init();
	}
	public void init(){
		gsm.setBTTime(30);
		tileMap = new TileMap(16);
		tileMap.loadTiles("/Tilesets/lvl1tilesout.png");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		background = new Background("/Background/menubgmod.png", .5);
		background.setVector(-5, 0);
		player = new Player(tileMap);
		player.setPosition(100, 10);
		populateEnemies();
		hud = new HUD(player);
		explosions = new ArrayList<Explosion>();
		bgMusic = new AudioPlayer("/Music/song.wav");
		sanic = new AudioPlayer("/Music/Green_Hill_Zone.mp3");
		bgMusic.play();
	}
	private int randEnemy(int xupper, int xlower){
		return (int)(Math.random()*(xupper-xlower))+xlower;
	}
	private void populateEnemies() {
		
		enemies = new ArrayList<Enemy>();
		
		FlyingNinja bob;
		Point[] points = new Point[] {
			new Point(200, randEnemy(120, 190)),
			new Point(860, randEnemy(120, 190)),
			new Point(960, randEnemy(120, 190)),
			new Point(1060, randEnemy(120, 190)),
			new Point(1260, randEnemy(120, 190)),
			new Point(1360, randEnemy(120, 190)),
			new Point(1460, randEnemy(120, 190)),
			new Point(1490, randEnemy(120, 190)),
			new Point(1525, randEnemy(120, 190)),
			new Point(1680, randEnemy(120, 190)),
			new Point(1900, randEnemy(120, 190)),
			new Point(2000, randEnemy(120, 190)),
			new Point(2050, randEnemy(120, 190)),
			new Point(2120, randEnemy(120, 190)),
			new Point(2440, randEnemy(120, 190)),
			new Point(2800, randEnemy(120, 190))
		};
		for(int i = 0; i < points.length; i++) {
			bob = new FlyingNinja(tileMap);
			bob.setPosition(points[i].x, points[i].y);
			enemies.add(bob);
		}
		
	}
	private void gottaGoFast(){
		player.maxSpeed = 10;
		bgMusic.stop();
		sanic.play();
	}
	public void update(){
		if(s&&a&&n&&i&&c){
			gottaGoFast();
			player.setSanic();
		}
		if(!bgMusic.isRunning()){
			bgMusic.play();
		}
		player.update();
		if(player.isDead()){
			bgMusic.stop();
			sanic.stop();
			gsm.setState(GameStateManager.GAMEOVER);
		}
		tileMap.setPosition(
				
				player.getx(),
				player.gety());
		background.update();
		player.checkAttack(enemies);
		for(int i=0; i<enemies.size();i++){
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()){
				enemies.remove(i);
				explosions.add(new Explosion(
						e.getx(), 
						e.gety()
						));
				i--;
			}
		}
		for(int i = 0; i<explosions.size(); i++){
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()){
				explosions.remove(i);
				i--;
			}
		}
		if(player.getStairs()){
			toMenu();
		}
	}
	public void draw(Graphics2D g) {
		background.draw(g);
		tileMap.draw(g);
		player.draw(g);
		for(int i=0; i<enemies.size();i++){
			enemies.get(i).draw(g);
		}
		hud.draw(g);
		for(int i = 0; i < explosions.size(); i++){
			explosions.get(i).setMapPosition(tileMap.getx(), tileMap.gety());
			explosions.get(i).draw(g);
		}
	}
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_UP)player.setUp(true);
		if(k == KeyEvent.VK_DOWN)player.setDown(true);
		if(k == KeyEvent.VK_LEFT)player.setLeft(true);
		if(k == KeyEvent.VK_RIGHT)player.setRight(true);
		if(k == KeyEvent.VK_Z)player.setJumping(true);
		if(k == KeyEvent.VK_X)player.setFiring(true);
		if(k == KeyEvent.VK_SPACE)gsm.setBulletTime(true);
		if(k == KeyEvent.VK_S)s=!s;
		if(k == KeyEvent.VK_A&&s)a=!a;
		if(k == KeyEvent.VK_N&&s&&a)n=!n;
		if(k == KeyEvent.VK_I&&s&&a&&n)i=!i;
		if(k == KeyEvent.VK_C&&s&&a&&n&&i)c=!c;
		if(!(k == KeyEvent.VK_S
				||k == KeyEvent.VK_A
				||k == KeyEvent.VK_N
				||k == KeyEvent.VK_I
				||k == KeyEvent.VK_C))s=a=n=i=c=false;
	}
	public void keyReleased(int k) {

		if(k == KeyEvent.VK_UP)player.setUp(false);
		if(k == KeyEvent.VK_DOWN)player.setDown(false);
		if(k == KeyEvent.VK_LEFT)player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT)player.setRight(false);
		if(k == KeyEvent.VK_Z)player.setJumping(false);
		if(k == KeyEvent.VK_X)player.setFiring(false);
		if(k == KeyEvent.VK_SPACE)gsm.setBulletTime(false);
	}
	public void toMenu(){
		gsm.setState(GameStateManager.MENUSTATE);
	}
}
