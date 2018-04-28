import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.util.Random;

import javax.swing.*;


public class Render extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{
	private static final long serialVersionUID = 1L;
	
	Timer time = new Timer(10, this);
	boolean entityMoving = false, mouseDown = false, entityDraw = true, blackMode = true, moveMode = false, shiftDown = false, ctrlDown = false, panPosOnce = true, orbit = false, assistedZoom = true, showNames = false, multiSelectBox = false;
	static boolean pause = true, entityDestroyed = false, pauseWasTrue = true;
	boolean antialiasing = true, entitySelected = false, multiEntitySelected = false, distanceToolOn = false, track = false, randomColorMode = false;
	Vector vector = new Vector();
	Entity entity[] = new Entity[MainClass.maxEntities];
	Entity selectedEntities[] = new Entity[MainClass.maxEntities];
	Entity selectedEntity, entityToOrbit, trackedEntity;
	Collision collide = new Collision();
	int selected[] = new int[MainClass.maxEntities];
	double multiMoveX[] = new double[MainClass.maxEntities], multiMoveY[] = new double[MainClass.maxEntities];
	double trackedEntitiesX[] = new double[MainClass.maxEntities], trackedEntitiesY[] = new double[MainClass.maxEntities];
	int currentX, currentY, newX, newY, drawEntity[] = {0,0,-200,0}, selectedEntityNum, movedEntityNum, trackX = 400, trackY = 290, multiSelectCount = 0, pasteEntityNum = 0;
	static int entityNum = 0;
	double distX, distY, movedX, movedY;
	int zoomX, zoomY, rightClickZoomX=0, rightClickZoomY=0;
	double oldZoom, zoomAmount = 1, zoomTime = 15;
	static int zoomCounter = 0;
	static double zoomScale = 1, endZoom = 1, deltaZoom;
	double moveX, moveY, panDistanceX[] = new double[MainClass.maxEntities], panDistanceY[] = new double[MainClass.maxEntities];
	double initialTimeStep = 100000;
	double speedOfLight = 299792458;
	static double timeStep;
	int pauseSize[] = {-175, 19, 20, 20}, drawSize[] = {-95, 19, 20, 20}, blackSize[] = {-135, 19, 20, 20}, moveSize[] = {-55, 19, 20, 20};
	int selectedSize[] = {-130, 330, 115, 15}, trackSize[] = {-130, 355, 115, 15}, colorSize[] = {-130, 380, 115, 15}, assistedSize[] = {-130, 405, 115, 15}, nameSize[] = {-130, 430, 115, 15};
	Random randNum = new Random();
	
	static JPopupMenu popupMenu = new JPopupMenu();
	JMenuItem changeMass = new JMenuItem("Set Mass...");
	JMenuItem changeRadius = new JMenuItem("Set Radius...");
	JMenuItem changeColor = new JMenuItem("Change Colour...");
	JMenuItem changeVelocity = new JMenuItem("Set Velocity...");
	JMenuItem noVelocity = new JMenuItem("No Velocity");
	JMenuItem changeName = new JMenuItem("Change Name...");
	JMenuItem zoomVariable = new JMenuItem("Zoom Scale...");
	JMenuItem zoomAmountMenuItem = new JMenuItem("Zoom Amount...");
	JSlider timeSlider = new JSlider();
	Color newColor, tempColor;
	
	
	public Render(){
		time.start();
		setBackground(new Color(255,255,255));
		setBounds(0,20,1000,580);
		addMouseListener(new PopupListener());
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
		setFocusable(true);
		setLayout(null);
		timeSlider.setFocusable(false);
		timeSlider.setBounds(810,65,180,timeSlider.getPreferredSize().height);
		timeSlider.setBackground(new Color(100,100,100));
		changeMass.addActionListener(this);
		changeRadius.addActionListener(this);
		changeColor.addActionListener(this);
		changeVelocity.addActionListener(this);
		noVelocity.addActionListener(this);
		changeName.addActionListener(this);
		zoomVariable.addActionListener(this);
		zoomAmountMenuItem.addActionListener(this);
		popupMenu.add(changeMass);
		popupMenu.add(changeRadius);
		popupMenu.add(changeVelocity);
		popupMenu.add(noVelocity);
		popupMenu.add(changeColor);
		popupMenu.add(changeName);
		popupMenu.addSeparator();
		popupMenu.add(zoomVariable);
		popupMenu.add(zoomAmountMenuItem);
		add(timeSlider);
		requestFocus();
		
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Graphics2D gRotated = (Graphics2D) g;
		AffineTransform orig = gRotated.getTransform();
		if(antialiasing){
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		if(blackMode==true){
			g.setColor(Color.WHITE);
		}
		if(blackMode==false){
			g.setColor(Color.BLACK);
		}
		if(mouseDown == true){
			double radius = vector.distance(currentX, currentY, newX, newY);
			g.fillOval((int)(currentX-radius), (int)(currentY-radius), (int)radius*2, (int)radius*2);
		}
		for(int i=0;i<entityNum;i++){
			if(blackMode == true && entity[i].getColor().getRed() == 0 && entity[i].getColor().getBlue() == 0 && entity[i].getColor().getGreen() == 0){
				entity[i].setColor(new Color(255,255,255));
			}
			if(blackMode == false && entity[i].getColor().getRed() == 255 && entity[i].getColor().getBlue() == 255 && entity[i].getColor().getGreen() == 255){
				entity[i].setColor(new Color(0,0,0));
			}
			g.setColor(entity[i].getColor());
			g.fillOval((int)(entity[i].getX()-entity[i].getDispR()), (int)(entity[i].getY()-entity[i].getDispR()), (int)entity[i].getDispR()*2,(int)entity[i].getDispR()*2);
			
		}
		if(multiEntitySelected){
			for(int i=0;i<multiSelectCount;i++){
				g.setColor(new Color(0,0,255));
				g2.setStroke(new BasicStroke(2));
				g.drawOval((int)(entity[selected[i]].getX()-entity[selected[i]].getDispR()), (int)(entity[selected[i]].getY()-entity[selected[i]].getDispR()), (int)entity[selected[i]].getDispR()*2, (int)entity[selected[i]].getDispR()*2);
				g2.setStroke(new BasicStroke(1));
			}
		}
		if(entitySelected){
			if(!orbit){
				g.setColor(new Color(0,0,255));
			}
			else{
				g.setColor(new Color(255,170,8));
			}
			g2.setStroke(new BasicStroke(2));
			g.drawOval((int)(selectedEntity.getX()-selectedEntity.getDispR()), (int)(selectedEntity.getY()-selectedEntity.getDispR()), (int)selectedEntity.getDispR()*2, (int)selectedEntity.getDispR()*2);
			g2.setStroke(new BasicStroke(1));
		}
		//ENTITY NAMES
		g.setFont(new Font("Arial", Font.BOLD, 10));
		if(showNames){
			if(blackMode){
				g.setColor(new Color(255,255,255));
			}
			else{
				g.setColor(new Color(0,0,0));
			}
			for(int i=0;i<entityNum;i++){
				entity[i].setNamePosY(entity[i].getY());
				for(int j=0;j<i;j++){
					if(i != j){
						if(entity[i].getNamePosY() <= entity[j].getNamePosY()+10 && entity[i].getNamePosY() >= entity[j].getNamePosY()-10 && entity[i].getX()+(int)entity[i].getDispR()+5 >= entity[j].getX()-5 && entity[i].getX()+(int)entity[i].getDispR()+5 <= entity[j].getX()+35){
							entity[i].setNamePosY(entity[j].getNamePosY()+10);
						}
					}
				}
				g.drawString(entity[i].getName(),(int)entity[i].getX()+(int)entity[i].getDispR()+5,(int)entity[i].getNamePosY());
			}
		}
		if(multiSelectBox){
			if(newX-currentX >= 0 && newY-currentY >= 0){
				g.setColor(new Color(0,0,255));
				g.drawRect(currentX, currentY, newX-currentX, newY-currentY);
				g.setColor(new Color(0,0,255,100));
				g.fillRect(currentX, currentY, newX-currentX, newY-currentY);
			}
			else if(newX-currentX <= 0 && newY-currentY >= 0){
				g.setColor(new Color(0,0,255));
				g.drawRect(newX, currentY,currentX-newX, newY-currentY);
				g.setColor(new Color(0,0,255,100));
				g.fillRect(newX, currentY,currentX-newX, newY-currentY);
			}
			else if(newX-currentX <= 0 && newY-currentY <= 0){
				g.setColor(new Color(0,0,255));
				g.drawRect(newX, newY,currentX-newX, currentY-newY);
				g.setColor(new Color(0,0,255,100));
				g.fillRect(newX, newY,currentX-newX, currentY-newY);
			}
			else if(newX-currentX >= 0 && newY-currentY <= 0){
				g.setColor(new Color(0,0,255));
				g.drawRect(currentX, newY,newX-currentX, currentY-newY);
				g.setColor(new Color(0,0,255,100));
				g.fillRect(currentX, newY,newX-currentX, currentY-newY);
			}
		}
		//Menu
		g.setColor(new Color(100,100,100));
		g.fillRect(getWidth()-200,0,200,getHeight());
		//Pause
		if(!pause){
		g.setColor(new Color(0,255,0));
		}
		else{
			g.setColor(new Color(255,0,0));
		}
		g.fillRect(getWidth()+pauseSize[0], pauseSize[1], pauseSize[2], pauseSize[3]);
		g.setColor(new Color(0,0,0));
		g.drawRect(getWidth()+pauseSize[0], pauseSize[1], pauseSize[2], pauseSize[3]);
		//Draw
		if(entityDraw){
			g.setColor(new Color(0,255,0));
		}
		else{
			g.setColor(new Color(255,0,0));
		}
		g.fillRect(getWidth()+drawSize[0], drawSize[1], drawSize[2], drawSize[3]);
		g.setColor(new Color(0,0,0));
		g.drawRect(getWidth()+drawSize[0], drawSize[1], drawSize[2], drawSize[3]);
		//BlackMode
		if(blackMode){
			g.setColor(new Color(0,255,0));
		}
		else{
			g.setColor(new Color(255,0,0));
		}
		g.fillRect(getWidth()+blackSize[0], blackSize[1], blackSize[2], blackSize[3]);
		g.setColor(new Color(0,0,0));
		g.drawRect(getWidth()+blackSize[0], blackSize[1], blackSize[2], blackSize[3]);
		//Move
		if(moveMode){
			g.setColor(new Color(0,255,0));
		}
		else{
			g.setColor(new Color(255,0,0));
		}
		g.fillRect(getWidth()+moveSize[0], moveSize[1], moveSize[2], moveSize[3]);
		g.setColor(new Color(0,0,0));
		g.drawRect(getWidth()+moveSize[0], moveSize[1], moveSize[2], moveSize[3]);
		//Selected
		g.setFont(new Font("Arial", Font.BOLD, 10));
		if(entitySelected){
			g.setColor(new Color(0,255,0));
		}
		else{
			g.setColor(new Color(255,0,0));
		}
		g.fillRect(getWidth()+selectedSize[0], selectedSize[1], selectedSize[2], selectedSize[3]);
		g.setColor(new Color(0,0,0));
		g.drawRect(getWidth()+selectedSize[0], selectedSize[1], selectedSize[2], selectedSize[3]);
		g.setColor(new Color(255,255,255));
		g.drawString("SELECTED:",getWidth()-188,342);
		//Tracked
		if(track){
			g.setColor(new Color(0,255,0));
		}
		else{
			g.setColor(new Color(255,0,0));
		}
		g.fillRect(getWidth()+trackSize[0], trackSize[1], trackSize[2], trackSize[3]);
		g.setColor(new Color(0,0,0));
		g.drawRect(getWidth()+trackSize[0], trackSize[1], trackSize[2], trackSize[3]);
		g.setColor(new Color(255,255,255));
		g.drawString("TRACKING:",getWidth()-188,367);
		//Colour
		if(randomColorMode){
			g.setColor(new Color(0,255,0));
		}
		else{
			g.setColor(new Color(255,0,0));
		}
		g.fillRect(getWidth()+colorSize[0], colorSize[1], colorSize[2], colorSize[3]);
		g.setColor(new Color(0,0,0));
		g.drawRect(getWidth()+colorSize[0], colorSize[1], colorSize[2], colorSize[3]);
		g.setColor(new Color(255,255,255));
		g.drawString("RANDOMC:",getWidth()-188,392);
		//Assisted
		if(assistedZoom){
			g.setColor(new Color(0,255,0));
		}
		else{
			g.setColor(new Color(255,0,0));
		}
		g.fillRect(getWidth()+assistedSize[0], assistedSize[1], assistedSize[2], assistedSize[3]);
		g.setColor(new Color(0,0,0));
		g.drawRect(getWidth()+assistedSize[0], assistedSize[1], assistedSize[2], assistedSize[3]);
		g.setColor(new Color(255,255,255));
		g.drawString("ASSISTED:",getWidth()-188,417);
		//Name
		if(showNames){
			g.setColor(new Color(0,255,0));
		}
		else{
			g.setColor(new Color(255,0,0));
		}
		g.fillRect(getWidth()+nameSize[0], nameSize[1], nameSize[2], nameSize[3]);
		g.setColor(new Color(0,0,0));
		g.drawRect(getWidth()+nameSize[0], nameSize[1], nameSize[2], nameSize[3]);
		g.setColor(new Color(255,255,255));
		g.drawString("NAMES:",getWidth()-188,442);
		
		//TEXT
		g.setColor(new Color(255,255,255));
		g.drawString("PLAY",getWidth()-178,pauseSize[3]-5);
		g.drawString("BLACK",getWidth()-143,pauseSize[3]-5);
		g.drawString("DRAW",getWidth()-100,pauseSize[3]-5);
		g.drawString("MOVE",getWidth()-60,pauseSize[3]-5);
		g.drawLine(getWidth()-195,45,getWidth()-5,45);
		g.drawLine(getWidth()-195,110,getWidth()-5,110);
		g.drawLine(getWidth()-195,150,getWidth()-5,150);
		g.drawLine(getWidth()-195,205,getWidth()-5,205);
		g.drawLine(getWidth()-195,320,getWidth()-5,320);
		g.drawString("TIME:",getWidth()-188,60);
		g.drawString("SCALE: " + twoDecimalExponent(100*endZoom)+"m",getWidth()-140,133);
		g.drawString(Double.toString(timeStep) + "s/s",getWidth()-70,90);
		
		
		
		g.setFont(new Font("Arial", Font.PLAIN, 14));
		if(entitySelected){
			g.drawString("Mass: " + roundTwoDecimals(twoDecimalExponent(selectedEntity.getM())) + "kg", getWidth()-170, 227);
			g.drawString("Radius: " + roundTwoDecimals(twoDecimalExponent(selectedEntity.getR())) + "m", getWidth()-170, 247);
			g.drawString("X velocity: " + roundFourDecimals(twoDecimalExponent(selectedEntity.getVelX()*100)) + "m/s", getWidth()-170, 267);
			g.drawString("Y velocity: " + roundFourDecimals(twoDecimalExponent(-selectedEntity.getVelY()*100)) + "m/s", getWidth()-170, 287);
			g.drawString("Name: " + selectedEntity.getName(), getWidth()-170, 307);
		}
		if(mouseDown){
			g.drawString("Mass: " + roundTwoDecimals(twoDecimalExponent(vector.distance(currentX, currentY, newX, newY)*zoomScale)) + "kg", getWidth()-170, 172);
			g.drawString("Radius: " + roundTwoDecimals(twoDecimalExponent(vector.distance(currentX, currentY, newX, newY)*zoomScale)) + "m", getWidth()-170, 192);
			
		}
		gRotated.rotate(-Math.PI/2);
		g.setFont(new Font("Arial", Font.BOLD, 10));
		g.setColor(new Color(160,160,255));
		g.drawString("CREATE", -198, 815);
		gRotated.drawString("SELECTED", -288, 815);
		gRotated.setTransform(orig);
		
		g.setColor(new Color(0,0,0));
		if(blackMode == true){
			g.setColor(new Color(255,255,255));
		}
		else if(blackMode == false){
			g.setColor(new Color(0,0,0));
		}
		//Scale
		g.drawLine(20, 555, 20, 560);
		g.drawLine(120, 555, 120, 560);
		g.drawLine(20, 560, 120, 560);
		g.setFont(new Font("Arial", Font.BOLD, 10));
		g.drawString(twoDecimalExponent(100*endZoom)+"m", 50, 572);
		if(distanceToolOn){
			double distanceLength = Math.sqrt((Math.pow(distX-movedX,2)+Math.pow(distY-movedY,2)))*zoomScale;
			g.setColor(new Color(255,0,0));
			g.drawLine((int)distX, (int)distY, (int)movedX, (int)movedY);
			g.setColor(new Color(255,255,255));
			g.setFont(new Font("Arial", Font.BOLD, 14));
			g.drawString(Double.toString(distanceLength) + "m", 820, 500);
		}
		
	}
	public void actionPerformed(ActionEvent e){
		if(zoomScale != endZoom){
			zoomScale += deltaZoom/zoomTime;
			double scale = ((double)oldZoom/(double)zoomScale);
			for(int i=0;i<entityNum;i++){
				entity[i].setX(zoomX + ((entity[i].getX()-zoomX)*scale));
				entity[i].setY(zoomY + ((entity[i].getY()-zoomY)*scale));
			}
			oldZoom = zoomScale;
			zoomCounter++;
		}
		if(zoomCounter >= zoomTime){
			zoomScale = endZoom;
		}
		
		for(int i=0;i<entityNum;i++){
			for(int j=0;j<entityNum;j++){
				if(entity[i].getX() == entity[j].getX() && entity[i].getY() == entity[j].getY()){
					entity[j].setX(entity[j].getX()+0.0001);
				}
			}
		}
		timeStep = initialTimeStep*((double)timeSlider.getValue()/(double)100);
		for(int i=0;i<entityNum;i++){
			entity[i].setDispR(entity[i].getR()/zoomScale);
			if(entity[i].getDispR() < 1){
				entity[i].setDispR(1);
			}
		}
		if(!entitySelected){
			changeMass.setEnabled(false);
			changeColor.setEnabled(false);
			changeVelocity.setEnabled(false);
			noVelocity.setEnabled(false);
			changeRadius.setEnabled(false);
		}
		else{
			changeMass.setEnabled(true);
			changeColor.setEnabled(true);
			changeVelocity.setEnabled(true);
			noVelocity.setEnabled(true);
			changeRadius.setEnabled(true);
		}
		if(entitySelected){
			if(e.getSource() == changeMass){
				try{
					selectedEntity.setM(Double.parseDouble(JOptionPane.showInputDialog("Enter new mass:")));
				}
				catch(Exception exc){
					System.out.println("Invalid parameter");
				}
				this.requestFocus();
			}
			if(e.getSource() == changeRadius){
				try{
					selectedEntity.setR(Double.parseDouble(JOptionPane.showInputDialog("Enter new radius:")));
				}
				catch(Exception exc){
					System.out.println("Invalid parameter");
				}
				this.requestFocus();
			}
			if(e.getSource() == changeColor){
				tempColor = selectedEntity.getColor();
				newColor = JColorChooser.showDialog(this, "Pick a Color", new Color(0,0,0));
				if(newColor!=null){
					selectedEntity.setColor(newColor);
				}
				else{
					selectedEntity.setColor(tempColor);
					System.out.println("Invalid parameter");
				}
				this.requestFocus();
			}
			if(e.getSource() == changeVelocity){
				try{
					selectedEntity.setVelX(Double.parseDouble(JOptionPane.showInputDialog("Enter new X velocity:"))/(double)initialTimeStep*1000);
				}
				catch(Exception exc){
					System.out.println("Invalid parameter");
				}
				try{
					selectedEntity.setVelY(-Double.parseDouble(JOptionPane.showInputDialog("Enter new Y velocity:"))/(double)initialTimeStep*1000);
				}
				catch(Exception exc){
					System.out.println("Invalid parameter");
				}
				this.requestFocus();
			}
			if(e.getSource() == changeName){
				MainClass.panel.selectedEntity.setName(JOptionPane.showInputDialog("Enter new name:"));
			}
			if(e.getSource() == noVelocity){
				selectedEntity.setVelX(0);
				selectedEntity.setVelY(0);
				this.requestFocus();
			}
			
		}
		if(e.getSource() == zoomVariable){
			try{
				oldZoom = endZoom;
				endZoom = Double.parseDouble(JOptionPane.showInputDialog("Enter new zoom:"))/100;
				if(endZoom <= 0){
					endZoom = oldZoom;
				}
				zoomX = rightClickZoomX;
				zoomY = rightClickZoomY;
				zoomCounter = 0;
				deltaZoom = endZoom - zoomScale;
			}
			catch(Exception exc){
				System.out.println("Invalid parameter");
			}
			this.requestFocus();
		}
		if(e.getSource() == zoomAmountMenuItem){
			try{
				zoomAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter new zoom:"))/100;
			}
			catch(Exception exc){
				System.out.println("Invalid parameter");
			}
			this.requestFocus();
		}
		if(blackMode == true){
			setBackground(new Color(0,0,0));
		}
		if(blackMode == false){
			setBackground(new Color(255,255,255));
		}
		
		if(pause == false){
			for(int i=0;i<entityNum;i++){
				for(int j=0;j<entityNum;j++){
					if(entity[i].getR() < (2*Vector.gravity*entity[i].getM())/Math.pow(speedOfLight, 2)){
						entity[i].setBlackHole(true);
						entity[i].setR(0);
					}
					if(i!=j && entityNum > 1){
						vector.newVelocities(entity[i], entity[j]);
					}
				}
			}
			for(int i=0;i<entityNum;i++){
				if(entity[i].getMoving() == false){
					entity[i].setX(entity[i].getX()+((timeStep*entity[i].getVelX())/zoomScale));
					entity[i].setY(entity[i].getY()+((timeStep*entity[i].getVelY())/zoomScale));
				}
			}
		}
		collision:	
		for(int i=0;i<entityNum;i++){
			for(int j=0;j<entityNum;j++){
				if(i!=j){
					if(!entity[i].isBlackHole()){
						collide.bounce(entity[i], entity[j], 1, zoomScale);
					}
					else{
						collide.blackHole(entity, entity[i], entity[j], zoomScale, j);
						if(entityDestroyed){
							if(entity[j] == selectedEntity){
								entitySelected = false;
								entityMoving = false;
							}
							entityDestroyed = false;
							break collision;
						}
					}
				}
			}
			for(int j=0;j<entityNum;j++){
				if(i!=j){
					collide.reaction(entity[i], entity[j], 1, zoomScale);
				}
			}
		}
		
		if(track){
			for(int i=0;i<entityNum;i++){
				if(entity[i] != trackedEntity){
					trackedEntitiesX[i] = entity[i].getX()-trackedEntity.getX();
					trackedEntitiesY[i] = trackedEntity.getY()-entity[i].getY();
				}
			}
			trackedEntity.setX(trackX);
			trackedEntity.setY(trackY);
			for(int i=0;i<entityNum;i++){
				if(entity[i] != trackedEntity){
					entity[i].setX(trackedEntity.getX() + trackedEntitiesX[i]);
					entity[i].setY(trackedEntity.getY() - trackedEntitiesY[i]);
				}
			}
		}
        Toolkit.getDefaultToolkit().sync();
		repaint();
	}
	public double twoDecimalExponent(double numberDouble){
		String numberString = Double.toString(numberDouble); 
		boolean exponentStatus = false;
		String exponentSuffix = "";
		for(int i=0;i<numberString.length();i++){
			if(Character.toString(numberString.charAt(i)).equals("E")){
				exponentStatus = true;
			}
			if(exponentStatus){
				exponentSuffix += numberString.charAt(i);
			}
		}
		if(exponentStatus){
			return Double.parseDouble((Character.toString(numberString.charAt(0)) + Character.toString(numberString.charAt(1)) + Character.toString(numberString.charAt(2)) + exponentSuffix));
		}
		else{
			return Double.parseDouble(numberString);
		}
	}
	public double fourDecimalExponent(double numberDouble){
		String numberString = Double.toString(numberDouble); 
		boolean exponentStatus = false;
		String exponentSuffix = "";
		for(int i=0;i<numberString.length();i++){
			if(Character.toString(numberString.charAt(i)).equals("E")){
				exponentStatus = true;
			}
			if(exponentStatus){
				exponentSuffix += numberString.charAt(i);
			}
		}
		if(exponentStatus){
			return Double.parseDouble((Character.toString(numberString.charAt(0)) + Character.toString(numberString.charAt(1)) + Character.toString(numberString.charAt(2)) + Character.toString(numberString.charAt(3))+ Character.toString(numberString.charAt(4)) + exponentSuffix));
		}
		else{
			return Double.parseDouble(numberString);
		}
	}
	public void mousePressed(MouseEvent e) {
		if(ctrlDown){
			for(int i=0;i<entityNum;i++){
				boolean passMultiCtrl = true;
				int deleteMultiJ = 0;
				if(vector.distance(e.getX(), e.getY(), entity[i].getX(), entity[i].getY()) <= entity[i].getDispR()){
					for(int j=0;j<multiSelectCount;j++){
						if(i == selected[j]){
							passMultiCtrl = false;
							deleteMultiJ = j;
						}
					}
					if(passMultiCtrl){
						entitySelected = false;
						multiEntitySelected= true;
						selected[multiSelectCount] = i;
						multiSelectCount++;
					}
					else{
						for(int k=deleteMultiJ;k<multiSelectCount;k++){
							selected[k] = selected[k+1];
						}
						multiSelectCount--;
					}
				}
			}
		}
		if(!ctrlDown){
			if(shiftDown == false && orbit == false){
				if(e.getButton() == 1){
					if(e.getX() >= drawEntity[0] && e.getX() <= getWidth()+drawEntity[2] && e.getY() >= drawEntity[1] && e.getY() <= getHeight() && entityDraw == true){
						multiEntitySelected = false;
						multiSelectCount = 0;
						mouseDown = true;
						currentX = e.getX();
						currentY = e.getY();
						newX = e.getX();
						newY = e.getY();
					}
					if(e.getX() >= drawEntity[0] && e.getX() <= getWidth()+drawEntity[2] && e.getY() >= drawEntity[1] && e.getY() <= getHeight() && entityDraw == false){
						boolean anyEntitiesSelected = false;
						for(int i=0;i<entityNum;i++){
							if(vector.distance(e.getX(), e.getY(), entity[i].getX(), entity[i].getY()) <= entity[i].getDispR() && !multiEntitySelected){
								anyEntitiesSelected = true;
								selectedEntity = entity[i];
								selectedEntityNum = i;
								entitySelected = true;
							}
							if(anyEntitiesSelected == false){
								entitySelected = false;
							}
						}
					}
					if(e.getX() >= drawEntity[0] && e.getX() <= getWidth()+drawEntity[2] && e.getY() >= drawEntity[1] && e.getY() <= getHeight() && moveMode){
						boolean dontEndMulti = false;
						for(int i=0;i<entityNum;i++){
							if(vector.distance(e.getX(), e.getY(), entity[i].getX(), entity[i].getY()) <= entity[i].getDispR()){
								entityMoving = true;
								if(!multiEntitySelected){
									movedEntityNum = i;
									entity[i].setMoving(true);
									moveX = e.getX() - entity[i].getX();
									moveY = e.getY() - entity[i].getY();
								}
								else{
									boolean selectedTest = false;
									for(int j=0;j<multiSelectCount;j++){
										if(i == selected[j]){
											selectedTest = true;
										}
									}
									if(selectedTest){
										dontEndMulti = true;
										for(int j=0;j<multiSelectCount;j++){
											entity[selected[j]].setMoving(true);
											multiMoveX[j] = e.getX() - entity[selected[j]].getX();
											multiMoveY[j] = e.getY() - entity[selected[j]].getY();
										}
									}
									else{
										entityMoving = false;
									}
								}
							}
						}
						if(!dontEndMulti){
							multiEntitySelected = false;
							multiSelectCount = 0;
						}
					}
					if(e.getX() >= drawEntity[0] && e.getX() <= getWidth()+drawEntity[2] && e.getY() >= drawEntity[1] && e.getY() <= getHeight() && !entityDraw){
						currentX = e.getX();
						currentY = e.getY();
						newX = e.getX();
						newY = e.getY();
						multiSelectBox = true;
					}
					if(e.getX() >= getWidth()+pauseSize[0] && e.getX() <= getWidth()+pauseSize[0]+pauseSize[2] && e.getY() >= pauseSize[1] && e.getY() <= pauseSize[1]+pauseSize[3]){
						if(pause == false){
							pause = true;
							pauseWasTrue = true;
						}
						else{
							pause = false;
							pauseWasTrue = false;
						}
					}
					if(e.getX() >= getWidth()+drawSize[0] && e.getX() <= getWidth()+drawSize[0]+drawSize[2] && e.getY() >= drawSize[1] && e.getY() <= drawSize[1]+drawSize[3]){
						if(!orbit){
							if(entityDraw == false){
								entityDraw = true;
								moveMode = false;
							}
							else{
								entityDraw = false;
							}
						}
					}
					if(e.getX() >= getWidth()+moveSize[0] && e.getX() <= getWidth()+moveSize[0]+moveSize[2] && e.getY() >= moveSize[1] && e.getY() <= moveSize[1]+moveSize[3]){
						if(moveMode == false){
							moveMode = true;
							entityDraw = false;
						}
						else{
							moveMode = false;
						}
					}
					if(e.getX() >= getWidth()+blackSize[0] && e.getX() <= getWidth()+blackSize[0]+blackSize[2] && e.getY() >= blackSize[1] && e.getY() <= blackSize[1]+blackSize[3]){
						if(blackMode == false){
							blackMode = true;
						}
						else{
							blackMode = false;
						}
					}
				}
				if(e.getButton() == 3){
					if(e.getX() >= drawEntity[0] && e.getX() <= getWidth()+drawEntity[2] && e.getY() >= drawEntity[1] && e.getY() <= getHeight()){
						boolean anyEntitiesSelected = false;
						for(int i=0;i<entityNum;i++){
							if(vector.distance(e.getX(), e.getY(), entity[i].getX(), entity[i].getY()) <= entity[i].getDispR()){
								anyEntitiesSelected = true;
								selectedEntity = entity[i];
								selectedEntityNum = i;
								entitySelected = true;
							}
							if(anyEntitiesSelected == false){
								entitySelected = false;
							}
						}
					}
				}
			}
			if(orbit == true && shiftDown == false){
				if(e.getX() >= drawEntity[0] && e.getX() <= getWidth()+drawEntity[2] && e.getY() >= drawEntity[1] && e.getY() <= getHeight() && entityDraw == false){
					for(int i=0;i<entityNum;i++){
						if(entity[i] != selectedEntity){
							if(vector.distance(e.getX(), e.getY(), entity[i].getX(), entity[i].getY()) <= entity[i].getDispR()){
								double distanceBetweenEntities = vector.distance(selectedEntity.getX(), selectedEntity.getY(), entity[i].getX(), entity[i].getY())*zoomScale;
								double angleAtOrbitFunction = vector.angle(selectedEntity.getX(), selectedEntity.getY(), entity[i].getX(), entity[i].getY())-90;
								selectedEntity.setVelX(vector.xComponent(angleAtOrbitFunction, Math.sqrt((Vector.gravity*entity[i].getM())/(distanceBetweenEntities))));
								selectedEntity.setVelY(-vector.yComponent(angleAtOrbitFunction, Math.sqrt((Vector.gravity*entity[i].getM())/(distanceBetweenEntities))));
							}
						}
					}
					orbit = false;
				}
			}
			if(shiftDown == true){
				if(e.getButton() == 1){
					pause = true;
					if(panPosOnce == true){
						panPosOnce = false;
						for(int i=0;i<entityNum;i++){
							panDistanceX[i] = (entity[i].getX()-e.getX())*zoomScale;
							panDistanceY[i] = (entity[i].getY()-e.getY())*zoomScale;
						}
					}
				}
			}
			if(e.getButton() == 3){
				rightClickZoomX = e.getX();
				rightClickZoomY = e.getY();
			}
		}
		if(entityMoving){
			multiSelectBox = false;
		}
	}
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == 1){
			if(multiSelectBox){
				multiSelectCount = 0;
				int xMult = 0,yMult = 0,wMult = 0,hMult = 0;
				multiSelectBox = false;
				if(newX-currentX >= 0 && newY-currentY >= 0){
					xMult = currentX;
					yMult = currentY;
					wMult = newX-currentX;
					hMult = newY-currentY;
				}
				else if(newX-currentX <= 0 && newY-currentY >= 0){
					xMult = newX;
					yMult = currentY;
					wMult = currentX-newX;
					hMult = newY-currentY;
				}
				else if(newX-currentX <= 0 && newY-currentY <= 0){
					xMult = newX;
					yMult = newY;
					wMult = currentX-newX;
					hMult = currentY-newY;
				}
				else if(newX-currentX >= 0 && newY-currentY <= 0){
					xMult = currentX;
					yMult = newY;
					wMult = newX-currentX;
					hMult = currentY-newY;
				}
				for(int i=0;i<entityNum;i++){
					if(entity[i].getX() >= xMult && entity[i].getX() <= xMult+wMult && entity[i].getY() >= yMult && entity[i].getY() <= yMult + hMult){
						selectedEntity = entity[i];
						selected[multiSelectCount] = i;
						multiSelectCount++;
						selectedEntityNum = i;
					}
					else if(vector.distance(currentX, currentY, entity[i].getX(), entity[i].getY()) <= entity[i].getDispR()){
						selectedEntity = entity[i];
						selected[multiSelectCount] = i;
						multiSelectCount++;
						selectedEntityNum = i;
					}
				}
				if(multiSelectCount > 1){
					entitySelected = false;
					multiEntitySelected = true;
				}
				else if(multiSelectCount == 1){
					entitySelected = true;
					multiEntitySelected = false;
				}
				else if(multiSelectCount < 1){
					entitySelected = false;
					multiEntitySelected = false;
				}
			}
			if(vector.distance(currentX, currentY, newX, newY)>0 && mouseDown == true){
				Color newCol = null;
				if(!randomColorMode){
					if(blackMode == true){
						newCol = new Color(255,255,255);
					}
					if(blackMode == false){
						newCol = new Color(0,0,0);
					}
				}
				else{
					newCol = new Color((int)(randNum.nextDouble()*255), (int)(randNum.nextDouble()*255), (int)(randNum.nextDouble()*255));
				}
				entity[entityNum] = new Entity(currentX, currentY, vector.distance(currentX, currentY, newX, newY), vector.distance(currentX, currentY, newX, newY)*zoomScale, 0, 0, newCol, false, zoomScale, false, "");
				entityNum++;
			}
			mouseDown = false;
			entityMoving = false;
			multiSelectBox = false;
			for(int i=0;i<entityNum;i++){
				entity[i].setMoving(false);
			}
			panPosOnce = true;
			if(pauseWasTrue == false){
				pause = false;
			}
			else{
				pause = true;
			}
		}
		
	}
	public void mouseClicked(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mouseDragged(MouseEvent e) {
		movedX = e.getX();
		movedY = e.getY();
		if(shiftDown == false){
			newX = e.getX();
			newY = e.getY();
			if (entityMoving && !multiEntitySelected){
				entity[movedEntityNum].setX(newX-moveX);
				entity[movedEntityNum].setY(newY-moveY);
			}
			else if(entityMoving && multiEntitySelected){
				for(int i=0;i<multiSelectCount;i++){
					entity[selected[i]].setX(newX-multiMoveX[i]);
					entity[selected[i]].setY(newY-multiMoveY[i]);
				}
			}
		}
		if(shiftDown == true){
			if(panPosOnce == false){
				for(int i=0;i<entityNum;i++){
					entity[i].setX(e.getX()+(panDistanceX[i]/zoomScale));
					entity[i].setY(e.getY()+(panDistanceY[i]/zoomScale));
				}
			}
		}
		
	}
	public void mouseMoved(MouseEvent e) {
		movedX = e.getX();
		movedY = e.getY();
	}
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_D){
			if(distanceToolOn == false){
				distanceToolOn = true;
				distX = movedX;
				distY = movedY;
				for(int i=0;i<entityNum;i++){
					if(vector.distance(movedX, movedY, entity[i].getX(), entity[i].getY()) <= entity[i].getDispR()){
						distX = entity[i].getX();
						distY = entity[i].getY();
					}
				}
			}
			else{
				distanceToolOn = false;
			}
		}
		if(ctrlDown){
			if(code == KeyEvent.VK_C){
				for(int i=0;i<multiSelectCount;i++){
					selectedEntities[i] = new Entity(entity[selected[i]], entity[selected[i]].getR());
					pasteEntityNum = multiSelectCount;
				}
			}
			if(code == KeyEvent.VK_V){
				for(int i=0;i<pasteEntityNum;i++){
					entity[entityNum] = selectedEntities[i];
					entityNum++;
					selectedEntities[i] = new Entity(selectedEntities[i],  selectedEntities[i].getR());
				}
			}
		}
		if(code == KeyEvent.VK_P){
			if(pause == false){
				pause = true;
				pauseWasTrue = true;
			}
			else{
				pause = false;
				pauseWasTrue = false;
			}
		}
		if(code == KeyEvent.VK_U){
			if(showNames == false){
				showNames = true;
			}
			else{
				showNames = false;
			}
		}
		if(code == KeyEvent.VK_M){
			if(moveMode == false){
				moveMode = true;
				entityDraw = false;
			}
			else{
				moveMode = false;
			}
		}
		if(code == KeyEvent.VK_T){
			boolean turnedThisTime = false;
			if(entitySelected){
				if(!track){
					track = true;
					trackedEntity = selectedEntity;
					turnedThisTime = true;
				}
			}
			if(track && !turnedThisTime){
				track = false;
			}
		}
		if(code == KeyEvent.VK_X){
			if(entitySelected){
				selectedEntity.setBlackHole(true);
				selectedEntity.setR(0);
			}
		}
		if(code == KeyEvent.VK_N){
			if(!orbit){
				if(entityDraw == false){
					entityDraw = true;
					moveMode = false;
				}
				else{
					entityDraw = false;
				}
			}
		}
		if(code == KeyEvent.VK_B){
			if(blackMode == false){
				blackMode = true;
			}
			else{
				blackMode = false;
			}
		}
		if(code == KeyEvent.VK_O){
			if(entitySelected == true){
				orbit = true;
				entityDraw = false;
			}
		}
		if(code == KeyEvent.VK_C && !ctrlDown){
			if(randomColorMode){
				randomColorMode = false;
			}
			else{
				randomColorMode = true;
			}
		}
		if(code == KeyEvent.VK_Z){
			if(assistedZoom){
				assistedZoom = false;
			}
			else{
				assistedZoom = true;
			}
		}
		if(code == KeyEvent.VK_SHIFT){
			shiftDown = true;
		}
		if(code == KeyEvent.VK_CONTROL){
			ctrlDown = true;
		}
		if(code == KeyEvent.VK_I){
			JOptionPane.showMessageDialog(this, "- Click and drag to create an entity\n"
					+ "- Click boxes in menu to toggle\n"
					+ "- Press 'r' to reset\n"
					+ "- Press 'p' to toggle play\n"
					+ "- Press 'n' to toggle draw\n"
					+ "- Press 'm' to toggle moving\n"
					+ "- Press 'b' to toggle black background mode\n"
					+ "- Press 'c' to toggle random colour mode\n"
					+ "- Press 'z' to toggle random assisted zoom\n"
					+ "- Press 'u' to toggle whether names appear next to the entity\n"
					+ "- Press 'l' to enable the multiple selection box, click and drag to use\n"
					+ "- Press 'o' while you have an entity selected then click another entity to create an orbit\n"
					+ "- Press 't' while you have an entity selected to track it\n"
					+ "- Press 'x' while you have an entity selected to turn it into a black hole\n"
					+ "- To select an entity, turn off draw mode and click an entity\n"
					+ "- To move an entity, select turn move on, and click and drag an entity\n"
					+ "- Right click any entity to change its mass, radius, velocity and colour\n"
					+ "- Press 'DEL' with an entity selected to remove it\n"
					+ "- Use mouse wheel to zoom\n"
					+ "- Use slider to change zoom amount and time step\n"
					+ "- Press CTRL+C to copy selected entities\n"
					+ "- Press CTRL+V to paste copied entities\n"
					+ "- Hold shift then click and drag to pan",
					"Instructions",JOptionPane.PLAIN_MESSAGE);
		}
		if(code == KeyEvent.VK_DELETE){
			if(selectedEntity == trackedEntity){
				track = false;
			}
			if(entitySelected == true){
				for(int i=selectedEntityNum;i<entityNum;i++){
					entity[i] = entity[i+1];
				}
				entityNum--;
				entitySelected = false;
			}
			if(multiEntitySelected){
				Entity tempSaveEnt[] = new Entity[1000];
				int deleteCount = 0;
				for(int i=0;i<entityNum;i++){
					boolean doNotDeleteEntity = true;
					for(int j=0;j<multiSelectCount;j++){
						if(i==selected[j]){
							doNotDeleteEntity = false;
						}
					}
					if(doNotDeleteEntity){
						System.out.println(i);
						tempSaveEnt[deleteCount] = entity[i];
						deleteCount++;
					}
				}
				for(int i=0;i<deleteCount;i++){
					entity[i] = tempSaveEnt[i];
				}
				
				entityNum = deleteCount;
				multiSelectCount = 0;
				multiEntitySelected = false;
			}
		}
		if(code == KeyEvent.VK_R){
			track = false;
			reset();
		}
	}
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_SHIFT){
			shiftDown = false;
			panPosOnce = true;
			if(pauseWasTrue == false){
				pause = false;
			}
			else{
				pause = true;
			}
		}
		if(code == KeyEvent.VK_CONTROL){
			ctrlDown = false;
		}
	}
	public void keyTyped(KeyEvent e) {
	}
	public void reset(){
		entityNum = 0;
		multiSelectCount = 0;
		entitySelected = false;
		multiEntitySelected = false;
	}
	public void toggle(boolean subject){
		if(subject == true){
			subject = false;
		}
		else if(subject == false){
			subject = true;
		}
	}
	public double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}
	public double roundFourDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.####");
		return Double.valueOf(twoDForm.format(d));
	}
	public void mouseWheelMoved(MouseWheelEvent e) {
		zoomX = e.getX();
		zoomY = e.getY();
		oldZoom = zoomScale;
		if(e.getWheelRotation() == -1){
			if(!assistedZoom){
				endZoom -= zoomAmount;
			}
			else{
				endZoom -= endZoom/2.0;
			}
			if(endZoom <= 0){
				endZoom = oldZoom;
			}
		}
		if(e.getWheelRotation() == 1){
			if(!assistedZoom){
				endZoom += zoomAmount;
			}
			else{
				endZoom += endZoom;
			}
		}
		zoomCounter = 0;
		deltaZoom = endZoom - zoomScale;
	}
	public void setEntityNum(int newEntityNum){
		entityNum = newEntityNum;
	}
	public void setBlackMode(boolean newBlackMode){
		blackMode = newBlackMode;
	}
	public void setEntity(Entity[] newEntity){
		entity = newEntity;
	}
	public void setEntitySelected(boolean newEntitySelected){
		entitySelected = newEntitySelected;
	}
	public String saveInfo(){
		String saveData = "";
		saveData += Integer.toString(entityNum) + "//" + Double.toString(zoomScale)+ "//" + blackMode + "\n";
		for(int i=0;i<entityNum;i++){
			saveData += Double.toString(entity[i].getX()) + "//";
			saveData += Double.toString(entity[i].getY()) + "//";
			saveData += Double.toString(entity[i].getR()) + "//";
			saveData += Double.toString(entity[i].getM()) + "//";
			saveData += Double.toString(entity[i].getVelX()) + "//";
			saveData += Double.toString(entity[i].getVelY()) + "//";
			saveData += Integer.toString(entity[i].getColor().getRed()) + "-" + Integer.toString(entity[i].getColor().getGreen()) + "-" + Integer.toString(entity[i].getColor().getBlue()) + "//";
			saveData += (entity[i].getMoving())?true:false + "//";
			saveData += (entity[i].isBlackHole())?true:false + "//";
			saveData += (entity[i].getName());
			
			if(i!=entityNum-1){
				saveData += "\n";
			}
		}
		return saveData;
	}
}
