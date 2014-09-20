package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject{
	protected int health;
	protected boolean dead;
	public boolean isDead() {return dead;}
	protected int damage;
	public int getDamage() {return damage;}

	public Enemy(TileMap tm) {
		super(tm);
		
	}
	public void hit(int damage){
		if(dead)return;
		health -=damage;
		if(health<=0)health=0;dead=true;
	}
	public void update(){
		
	}
}
