package GameState;
import java.awt.Graphics2D;
public class GameStateManager {
	private GameState[] gameStates;
	private int currentState;
	public boolean bulletTime;
	public long btTime;
	public static long btTimer;
	public static boolean btCooldown;
	public static final int NUMGAMESTATES = 4;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int OPTIONSSTATE = 2;
	public static final int GAMEOVER = 3;
	
	public GameStateManager(){
		gameStates = new GameState[NUMGAMESTATES];
		currentState = MENUSTATE;
		loadState(currentState);
		
	}
	private void loadState(int state){
		if(state == MENUSTATE){
			gameStates[state]= new MenuState(this);
		}
		if(state == OPTIONSSTATE){
			gameStates[state] = new OptionsState(this);
		}
		if(state == LEVEL1STATE){
			gameStates[state] = new Level1State(this);
		}
		if(state == GAMEOVER){
			gameStates[state] = new GameOver(this);
		}
	}
	public static long getBTTimer(){
		return btTimer;
	}
	private void unloadState(int state){
		gameStates[state] = null;
	}
	public void setBulletTime(boolean b){
		bulletTime = b;
	}
	public void setState(int state){
		unloadState(currentState);
		currentState = state;
		loadState(state);
	}
	public void update(){
		if(bulletTime&&btTimer > 0&&!btCooldown){
			try {
				btTimer = btTimer - 8;
				System.out.println("BULLET TIME");
				Thread.sleep(btTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else if(btTimer < 320){
			btTimer++;
		}else if(btTimer == 320){
			btCooldown = false;
		}
		if(gameStates[currentState] != null){
			gameStates[currentState].update();
		}
	}
	public void draw(Graphics2D g){
		if(bulletTime&&btTimer > 0&&!btCooldown){
			try {
				System.out.println("BULLET TIME");
				Thread.sleep(btTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else if(btTimer == 0){
			btCooldown = true;
		}
		if(gameStates[currentState] != null){
			gameStates[currentState].draw(g);
		}
	}
	public void keyPressed(int k){
		gameStates[currentState].keyPressed(k);
	}
	public void keyReleased(int k){
		gameStates[currentState].keyReleased(k);
	}
	public void setBTTime(long bt) {
		btTime = bt;
	}
	public static boolean getCooldown(){
		return btCooldown;
	}
}
