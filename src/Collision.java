
public class Collision {
	Vector vector = new Vector();
	public void reaction(Entity entity1, Entity entity2, double restitution, double zoom){
		if (vector.distance(entity1.getX(), entity1.getY(), entity2.getX(), entity2.getY())*zoom < entity1.getR() + entity2.getR()){
			double dist = ((entity1.getR()+entity2.getR()) - vector.distance(entity1.getX(), entity1.getY(), entity2.getX(), entity2.getY())*zoom)/2;
			entity1.setX(entity1.getX() - (vector.xComponent(vector.angle(entity1.getX(), entity1.getY(), entity2.getX(), entity2.getY()), dist)/zoom));
			entity1.setY(entity1.getY() + (vector.yComponent(vector.angle(entity1.getX(), entity1.getY(), entity2.getX(), entity2.getY()), dist)/zoom));
			entity2.setX(entity2.getX() - (vector.xComponent(vector.angle(entity2.getX(), entity2.getY(), entity1.getX(), entity1.getY()), dist)/zoom));
			entity2.setY(entity2.getY() + (vector.yComponent(vector.angle(entity2.getX(), entity2.getY(), entity1.getX(), entity1.getY()), dist)/zoom));
		}
	}
	public void blackHole(Entity allEntities[], Entity blackHoleEntity, Entity entity, double zoom, int j){
		if (vector.distance(blackHoleEntity.getX(), blackHoleEntity.getY(), entity.getX(), entity.getY())*zoom < blackHoleEntity.getR() + entity.getR()){
			blackHoleEntity.setM(blackHoleEntity.getM()+entity.getM());
			for(int i=j;i<Render.entityNum;i++){
				allEntities[i] = allEntities[i+1];
			}
			Render.entityNum--;
			Render.entityDestroyed = true;
		}
	}
	public void bounce(Entity entity1, Entity entity2, double restitution, double zoom){
		double newAnglei = 0;
		double tangentAngle = 0;
		double tempMagnitude = 0;
		double tempAngle = 0;
		if (vector.distance(entity1.getX(), entity1.getY(), entity2.getX(), entity2.getY())*zoom < entity1.getR() + entity2.getR()){
			//Store entity values
			if(entity1.getX() > entity2.getX()){
				if(entity2.getY() > entity1.getY()){
					tangentAngle = vector.angle(entity2.getX(), entity2.getY(),entity1.getX(),entity1.getY())+90;
				}
				else{
					tangentAngle = vector.angle(entity2.getX(), entity2.getY(),entity1.getX(),entity1.getY())-90;
				}
			}
			else if(entity1.getX() <= entity2.getX()){
				if(entity1.getY() > entity2.getY()){
					tangentAngle = vector.angle(entity1.getX(), entity1.getY(),entity2.getX(),entity2.getY())+90;
				}
				else{
					tangentAngle = vector.angle(entity1.getX(), entity1.getY(),entity2.getX(),entity2.getY())-90;
				}
			}
			
			tempMagnitude = Math.hypot(entity1.getVelX(),entity1.getVelY());
			if(entity1.getVelX() >= 0){
				tempAngle = 90 + ((180/Math.PI)*Math.asin(entity1.getVelY()/Math.hypot(entity1.getVelX(), entity1.getVelY())));
			}
			else{
				tempAngle = 270 - ((180/Math.PI)*Math.asin(entity1.getVelY()/Math.hypot(entity1.getVelX(), entity1.getVelY())));
			}
			//Angle and magnitude
			if(entity1.getX() <= entity2.getX() && entity1.getY() <= entity2.getY()){
				if(tempAngle <= 180){
					if(vector.modulus(tempAngle-tangentAngle) <= 90){
						newAnglei = 2*(180-((180 - tempAngle)+tangentAngle));
						entity1.setVelX(vector.xComponent((tempAngle-newAnglei),tempMagnitude)*restitution);
						entity1.setVelY(-vector.yComponent((tempAngle-newAnglei),tempMagnitude)*restitution);
					}
					if(vector.modulus(tempAngle-tangentAngle) > 90 && vector.modulus(tempAngle-tangentAngle) <= 180){
						newAnglei = 2*(180-(180-((180 - tempAngle)+tangentAngle)));
						entity1.setVelX(vector.xComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
						entity1.setVelY(-vector.yComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
					}
				}
				else if(tempAngle > 180){
					if(vector.modulus(tempAngle-tangentAngle) <= 90){
						newAnglei = 2*(180-(tempAngle + (180-tangentAngle)));
						entity1.setVelX(vector.xComponent((tempAngle-newAnglei),tempMagnitude)*restitution);
						entity1.setVelY(-vector.yComponent((tempAngle-newAnglei),tempMagnitude)*restitution);
					}
					if(vector.modulus(tempAngle-tangentAngle) > 90 && vector.modulus(tempAngle-tangentAngle) <= 180){
						newAnglei = 2*(180-((tempAngle-180)+(180-tangentAngle)));
						entity1.setVelX(vector.xComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
						entity1.setVelY(-vector.yComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
					}
				}
			}
			else if(entity1.getX() > entity2.getX() && entity1.getY() < entity2.getY()){
				if(tempAngle <= 180){
					if(tempAngle-tangentAngle <= 90 && tempAngle-tangentAngle >= 0){
						newAnglei = 2*(180-((180 - tempAngle)+tangentAngle));
						entity1.setVelX(vector.xComponent((tempAngle-newAnglei),tempMagnitude)*restitution);
						entity1.setVelY(-vector.yComponent((tempAngle-newAnglei),tempMagnitude)*restitution);
					}
					if(tempAngle-tangentAngle > 90 && tempAngle-tangentAngle <= 180){
						newAnglei = 2*(180-(180-((180 - tempAngle)+tangentAngle)));
						entity1.setVelX(vector.xComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
						entity1.setVelY(-vector.yComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
					}
				}
				else if(tempAngle > 180){
					if(tempAngle-tangentAngle <= 90 && tempAngle-tangentAngle >= 0){
						newAnglei = 2*(180-(180-((tempAngle - 180)+(180-tangentAngle))));
						entity1.setVelX(vector.xComponent((tempAngle-newAnglei),tempMagnitude)*restitution);
						entity1.setVelY(-vector.yComponent((tempAngle-newAnglei),tempMagnitude)*restitution);
					}
					if(tempAngle-tangentAngle > 90 && tempAngle-tangentAngle <= 180){
						newAnglei = 2*(180-((tempAngle - 180)+(180-tangentAngle)));
						entity1.setVelX(vector.xComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
						entity1.setVelY(-vector.yComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
					}
				}
			}
			else if(entity1.getX() < entity2.getX() && entity1.getY() > entity2.getY()){
				if(tempAngle <= 180){
					if(tempAngle-tangentAngle >= -90 && tempAngle-tangentAngle <= 0){
						newAnglei = 2*(180-((tempAngle)+(180-tangentAngle)));
						entity1.setVelX(vector.xComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
						entity1.setVelY(-vector.yComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
					}
					if(tempAngle-tangentAngle < -90 && tempAngle-tangentAngle >= -180){
						newAnglei = 2*(180-(180-((tempAngle)+(180-tangentAngle))));
						entity1.setVelX(vector.xComponent((tempAngle-newAnglei),tempMagnitude)*restitution);
						entity1.setVelY(-vector.yComponent((tempAngle-newAnglei),tempMagnitude)*restitution);
					}
				}
				else if(tempAngle > 180){
					if(tempAngle-tangentAngle <=270 && tempAngle-tangentAngle >= 180){
						newAnglei = 2*(180-((360-tempAngle)+(tangentAngle)));
						entity1.setVelX(vector.xComponent((tempAngle-newAnglei),tempMagnitude)*restitution);
						entity1.setVelY(-vector.yComponent((tempAngle-newAnglei),tempMagnitude)*restitution);
					}
				}
			}
			else if(entity1.getX() > entity2.getX() && entity1.getY() >= entity2.getY()){
				if(tempAngle <= 180){
					if(tempAngle-tangentAngle >= -90 && tempAngle-tangentAngle <= 0){
						newAnglei = 2*(180-((tempAngle)+(180-tangentAngle)));
						entity1.setVelX(vector.xComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
						entity1.setVelY(-vector.yComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
					}
					if(tempAngle-tangentAngle > 90){
						newAnglei = 2*(180-(180-((tempAngle)+(180-tangentAngle))));
						entity1.setVelX(vector.xComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
						entity1.setVelY(-vector.yComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
					}
				}
				else if(tempAngle > 180){
					if((tempAngle-180)-tangentAngle <=90){
						newAnglei = 2*(180-((360-tempAngle)+(tangentAngle)));
						entity1.setVelX(vector.xComponent((tempAngle-newAnglei),tempMagnitude)*restitution);
						entity1.setVelY(-vector.yComponent((tempAngle-newAnglei),tempMagnitude)*restitution);
					}
					if((tempAngle-180)-tangentAngle > 90){
						newAnglei = 2*(180-(180-((360-tempAngle)+(tangentAngle))));
						entity1.setVelX(vector.xComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
						entity1.setVelY(-vector.yComponent((tempAngle+newAnglei),tempMagnitude)*restitution);
					}
				}
			}
		}
	}
}
