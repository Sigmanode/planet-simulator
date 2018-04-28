import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MenuBar extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	Timer time = new Timer(10,this);
	private File lastPath;
	static int maxEntities = 1000;
	static JMenuBar menuBar = new JMenuBar();
	static JMenu file = new JMenu("File");
	static JMenu setting = new JMenu("Settings");
	static JMenu zoomMenu = new JMenu("Zoom");
	static JMenu load = new JMenu("Load (Local)");
	static JMenu entityMenu = new JMenu("Entity");
	static JMenuItem fileType[] = new JMenuItem[10];
	static JMenuItem settingType[] = new JMenuItem[10];
	static JMenuItem entityType[] = new JMenuItem[10];
	static JMenuItem zoomType[] = new JMenuItem[10];
	static JMenuItem loadType[] = new JMenuItem[10];
	static boolean overAmount = false;
	static int numberOfSavesAvailable = 10, saveNum = 0;
	static double savedEntityX[][] = new double[numberOfSavesAvailable][maxEntities],
			savedEntityY[][] = new double[numberOfSavesAvailable][maxEntities],
			savedEntityRadius[][] = new double[numberOfSavesAvailable][maxEntities],
			savedEntityMass[][] = new double[numberOfSavesAvailable][maxEntities],
			savedEntityVelX[][] = new double[numberOfSavesAvailable][maxEntities],
			savedEntityVelY[][] = new double[numberOfSavesAvailable][maxEntities],
			savedZoomScale[] = new double[numberOfSavesAvailable];
	static int savedEntityNum[] = new int[numberOfSavesAvailable];
	static Color savedEntityColor[][] = new Color[numberOfSavesAvailable][maxEntities];
	public MenuBar(){
		time.start();
		setLayout(null);
		setBounds(0,0,MainClass.panel.getWidth(),MainClass.panel.getHeight());
		fileType[0] = new JMenuItem("New");
		file.add(fileType[0]);
		fileType[0].addActionListener(this);
		file.addSeparator();
		fileType[1] = new JMenuItem("Save (Local)");
		file.add(fileType[1]);
		fileType[1].addActionListener(this);
		fileType[2] = new JMenuItem("Save As (Local)...");
		file.add(fileType[2]);
		fileType[2].addActionListener(this);
		file.add(load);
		file.addSeparator();
		fileType[3] = new JMenuItem("Save As...");
		file.add(fileType[3]);
		fileType[3].addActionListener(this);
		fileType[4] = new JMenuItem("Load...");
		file.add(fileType[4]);
		fileType[4].addActionListener(this);
		
		settingType[0] =
				new JMenuItem("Toggle Anti Aliasing");	
		entityType[0] = new JMenuItem("Set Mass...");
		entityType[1] = new JMenuItem("Set Radius...");
		entityType[2] = new JMenuItem("Set Velocity...");
		entityType[3] = new JMenuItem("No Velocity");
		entityType[4] = new JMenuItem("Change Colour...");
		entityType[5] = new JMenuItem("Change Name...");
		zoomType[0] = new JMenuItem("Zoom Scale...");
		zoomType[1] = new JMenuItem("Zoom Amount...");
		
		menuBar.setBounds(0,0,MainClass.panel.getWidth(),20);
		
		
		
		for(int i=0;i<10;i++){
			if(settingType[i]!=null){
				setting.add(settingType[i]);
				settingType[i].addActionListener(this);
			}
			if(entityType[i]!=null){
				entityMenu.add(entityType[i]);
				entityType[i].addActionListener(this);
			}
			if(zoomType[i]!=null){
				zoomMenu.add(zoomType[i]);
				zoomType[i].addActionListener(this);
			}
		}
		
		menuBar.add(file);
		menuBar.add(entityMenu);
		menuBar.add(zoomMenu);
		menuBar.add(setting);
		add(menuBar);
		
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == fileType[0]){
			MainClass.panel.reset();
		}
		if(e.getSource() == settingType[0]){
			settingType[0].setText("Toggle Anti Aliasing");
			if(MainClass.panel.antialiasing == true){
				MainClass.panel.antialiasing = false;
			}
			else if(MainClass.panel.antialiasing == false){
				MainClass.panel.antialiasing = true;
			}
		}
		if(e.getSource() == entityMenu){
			
		}
		if(MainClass.panel.entitySelected){
			if(e.getSource() == entityType[0]){
				try{
					MainClass.panel.selectedEntity.setM(Double.parseDouble(JOptionPane.showInputDialog("Enter new mass:")));
				}
				catch(Exception exc){
					System.out.println("Invalid parameter");
				}
				MainClass.panel.requestFocus();
			}
			if(e.getSource() == entityType[1]){
				try{
					MainClass.panel.selectedEntity.setR(Double.parseDouble(JOptionPane.showInputDialog("Enter new radius:")));
				}
				catch(Exception exc){
					System.out.println("Invalid parameter");
				}
				MainClass.panel.requestFocus();
			}
			if(e.getSource() == entityType[4]){
				MainClass.panel.tempColor = MainClass.panel.selectedEntity.getColor();
				MainClass.panel.newColor = JColorChooser.showDialog(MainClass.panel, "Pick a Color", new Color(0,0,0));
				if(MainClass.panel.newColor!=null){
					MainClass.panel.selectedEntity.setColor(MainClass.panel.newColor);
				}
				else{
					MainClass.panel.selectedEntity.setColor(MainClass.panel.tempColor);
					System.out.println("Invalid parameter");
				}
				MainClass.panel.requestFocus();
			}
			if(e.getSource() == entityType[2]){
				try{
					MainClass.panel.selectedEntity.setVelX(Double.parseDouble(JOptionPane.showInputDialog("Enter new X velocity:"))/(double)MainClass.panel.initialTimeStep*1000);
				}
				catch(Exception exc){
					System.out.println("Invalid parameter");
				}
				try{
					MainClass.panel.selectedEntity.setVelY(-Double.parseDouble(JOptionPane.showInputDialog("Enter new Y velocity:"))/(double)MainClass.panel.initialTimeStep*1000);
				}
				catch(Exception exc){
					System.out.println("Invalid parameter");
				}
				MainClass.panel.requestFocus();
			}
			if(e.getSource() == entityType[3]){
				MainClass.panel.selectedEntity.setVelX(0);
				MainClass.panel.selectedEntity.setVelY(0);
				MainClass.panel.requestFocus();
			}
			if(e.getSource() == entityType[5]){
				MainClass.panel.selectedEntity.setName(JOptionPane.showInputDialog("Enter new name:"));
			}
		}
		if(e.getSource() == zoomType[0]){
			try{
				MainClass.panel.oldZoom = Render.endZoom;
				Render.endZoom = Double.parseDouble(JOptionPane.showInputDialog("Enter new zoom:"))/100;
				if(Render.endZoom <= 0){
					Render.endZoom = MainClass.panel.oldZoom;
				}
				Render.zoomCounter = 0;
				Render.deltaZoom = Render.endZoom - Render.zoomScale;
			}
			catch(Exception exc){
				System.out.println("Invalid parameter");
			}
			MainClass.panel.requestFocus();
		}
		if(e.getSource() == zoomType[1]){
			try{
				MainClass.panel.zoomAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter new zoom:"))/(double)100;
			}
			catch(Exception exc){
				System.out.println("Invalid parameter");
			}
			MainClass.panel.requestFocus();
		}
		if(e.getSource() == fileType[1]){
			if(saveNum >= 10){
				overAmount = true;
				saveNum = 0;
			}
			if(overAmount){
				load.remove(saveNum);
			}
			
			savedZoomScale[saveNum] = Render.zoomScale;
			savedEntityNum[saveNum] = Render.entityNum;
			
			for(int i=0;i<Render.entityNum;i++){
				savedEntityX[saveNum][i] = MainClass.panel.entity[i].getX();
				savedEntityY[saveNum][i] = MainClass.panel.entity[i].getY();
				savedEntityRadius[saveNum][i] = MainClass.panel.entity[i].getR();
				savedEntityMass[saveNum][i] = MainClass.panel.entity[i].getM();
				savedEntityVelX[saveNum][i] = MainClass.panel.entity[i].getVelX();
				savedEntityVelY[saveNum][i] = MainClass.panel.entity[i].getVelY();
				savedEntityColor[saveNum][i] = MainClass.panel.entity[i].getColor();
			}
			loadType[saveNum] = new JMenuItem("Save " + Integer.toString(saveNum+1));
			load.insert(loadType[saveNum], saveNum);
			loadType[saveNum].addActionListener(this);
			saveNum++;
		}
		if(e.getSource() == fileType[2]){
			if(saveNum >= 10){
				overAmount = true;
				saveNum = 0;
			}
			if(overAmount){
				load.remove(saveNum);
			}
			savedZoomScale[saveNum] = Render.zoomScale;
			savedEntityNum[saveNum] = Render.entityNum;
			
			for(int i=0;i<Render.entityNum;i++){
				savedEntityX[saveNum][i] = MainClass.panel.entity[i].getX();
				savedEntityY[saveNum][i] = MainClass.panel.entity[i].getY();
				savedEntityRadius[saveNum][i] = MainClass.panel.entity[i].getR();
				savedEntityMass[saveNum][i] = MainClass.panel.entity[i].getM();
				savedEntityVelX[saveNum][i] = MainClass.panel.entity[i].getVelX();
				savedEntityVelY[saveNum][i] = MainClass.panel.entity[i].getVelY();
				savedEntityColor[saveNum][i] = MainClass.panel.entity[i].getColor();
			}
			loadType[saveNum] = new JMenuItem(JOptionPane.showInputDialog("Name:"));
			load.insert(loadType[saveNum], saveNum);
			loadType[saveNum].addActionListener(this);
			saveNum++;
		}
		if(e.getSource() == fileType[3]){
			Render.pause = true;
			SaveAndLoad save = new SaveAndLoad();
			String fileNameTemp = JOptionPane.showInputDialog("Save as:");
			save.saveFile(fileNameTemp, MainClass.panel.saveInfo());
		}
		if(e.getSource() == fileType[4]){
			Render.pause = true;
			File file;
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(lastPath);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Save Files", "sav");
			fileChooser.setFileFilter(filter);
			int result  = fileChooser.showOpenDialog(this);
			if(result == JFileChooser.APPROVE_OPTION){
				lastPath = fileChooser.getCurrentDirectory();
				file = fileChooser.getSelectedFile();
				SaveAndLoad load = new SaveAndLoad();
				load.loadFile(file);
			}
		}
		for(int j=0;j<10;j++){
			if(e.getSource() == loadType[j]){
				Render.zoomScale = savedZoomScale[j];
				Render.entityNum = savedEntityNum[j];
				for(int i=0;i<Render.entityNum;i++){
					MainClass.panel.entity[i].setX(savedEntityX[j][i]);
					MainClass.panel.entity[i].setY(savedEntityY[j][i]);
					MainClass.panel.entity[i].setR(savedEntityRadius[j][i]);
					MainClass.panel.entity[i].setM(savedEntityMass[j][i]);
					MainClass.panel.entity[i].setVelX(savedEntityVelX[j][i]);
					MainClass.panel.entity[i].setVelY(savedEntityVelY[j][i]);
					MainClass.panel.entity[i].setColor(savedEntityColor[j][i]);
				}
				Render.pause = true;
			}
			
		}
	}
}
