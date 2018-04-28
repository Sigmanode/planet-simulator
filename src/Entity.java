import java.awt.Color;


public class Entity {
	double xPos, yPos, radius, dispRadius, mass, velX, velY, namePosX, namePosY;
	Color col;
	boolean moving, blackHole;
	String name;
	public Entity(Entity ent, double radius){
		this.xPos = ent.xPos;
		this.yPos = ent.yPos;
		this.radius = radius;
		this.mass = ent.mass;
		this.velX = ent.velX;
		this.velY = ent.velY;
		this.col = ent.col;
		this.moving = ent.moving;
		this.blackHole = ent.blackHole;
		this.name = ent.name;
	}
	public Entity(int x, int y, double rad, double m,double vx,double vy, Color co, boolean mov, double scale, boolean blackHole, String name){
		this.xPos = x;
		this.yPos = y;
		this.radius = rad*scale;
		this.mass = m;
		this.velX = vx;
		this.velY = vy;
		this.col = co;
		this.moving = mov;
		this.blackHole = blackHole;
		this.name = name;
		this.namePosX = this.xPos+this.getDispR()+5;
		this.namePosY = this.yPos;
	}
	
	public double getNamePosX() {
		return namePosX;
	}

	public void setNamePosX(double namePosX) {
		this.namePosX = namePosX;
	}

	public double getNamePosY() {
		return namePosY;
	}

	public void setNamePosY(double namePosY) {
		this.namePosY = namePosY;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getX(){
		return xPos;
	}
	public double getY(){
		return yPos;
	}
	public boolean isBlackHole(){
		return blackHole;
	}
	public void setBlackHole(boolean blackHoleStatus){
		blackHole = blackHoleStatus;
	}
	public void setX(double newPos){
		xPos = newPos;
	}
	public void setY(double newPos){
		yPos = newPos;
	}
	public double getDispR(){
		return dispRadius;
	}
	public void setDispR(double newR){
		dispRadius = newR;
	}
	public void setR(double newR){
		radius = newR;
	}
	public double getR(){
		return radius;
	}
	public double getM(){
		return mass;
	}
	public void setM(double ma){
		mass = ma;
	}
	public double getVelX(){
		return velX;
	}
	public double getVelY(){
		return velY;
	}
	public Color getColor(){
		return col;
	}
	public void setColor(Color setCol){
		col = setCol;
	}
	public boolean getMoving(){
		return moving;
	}
	public void setMoving(boolean movi){
		moving = movi;
	}
	public void setVelX(double newVel){
		velX = newVel;
	}
	public void setVelY(double newVel){
		velY = newVel;
	}
	
}
