public class Vector {
	static double gravity = 6.67384*Math.pow(10, -11);
	public void newVelocities(Entity ent1, Entity ent2){
		double acc = ((gravity*ent2.getM())/Math.pow((distance(ent1.getX(), ent1.getY(), ent2.getX(), ent2.getY()))*Render.zoomScale,2))*Render.timeStep;
		double newAngle = angle(ent1.getX(),ent1.getY(),ent2.getX(),ent2.getY());
		
		ent1.setVelX(ent1.getVelX() + (Math.sin(Math.toRadians(newAngle))*acc));
		ent1.setVelY(ent1.getVelY() - (Math.cos(Math.toRadians(newAngle))*acc));
	}
	public double distance(double x1, double y1, double x2, double y2){
		return Math.sqrt(Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2));
	}
	public double angle(double x1, double y1, double x2, double y2){
		if(x1 <= x2){
			return 90 + Math.toDegrees(Math.atan((y2-y1)/(x2-x1)));
		}
		else{
			return 270 - Math.toDegrees(Math.atan(-(y2-y1)/(x2-x1)));
		}
	}
	public double xComponent(double angleDeg, double magnitude){
		return (Math.sin(Math.toRadians(angleDeg))*magnitude);
	}
	public double yComponent(double angleDeg, double magnitude){
		return (Math.cos(Math.toRadians(angleDeg))*magnitude);
	}
	public double modulus(double number){
		if(number >= 0){
			return number;
		}
		else{
			return -number;
		}
	}
}
