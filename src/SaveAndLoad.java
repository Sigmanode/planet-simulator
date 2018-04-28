import java.awt.Color;
import java.io.*;
import java.util.*;
public class SaveAndLoad {
	private Formatter save;
	private Scanner load;
	
	public void saveFile(String fileName, String saveData){
		try{
			save = new Formatter(fileName+".sav");
		}
		catch(Exception e){
			System.out.println("Could not ");
		}
		save.format(saveData);
		save.close();
	}
	public void loadFile(File file){
		try{
			load = new Scanner(file);
		}
		catch(Exception e){
			System.out.println("Could not find file.");
		}
		
		String initialLine = load.next();
		int entityNumber = Integer.parseInt(initialLine.split("//")[0]);
		double zoomScaleAmount = Double.parseDouble(initialLine.split("//")[1]);
		
		Entity tempEntity[] = new Entity[MainClass.maxEntities];
		int entityCount = 0;
		while(load.hasNext()){
			String currentLine = load.next();
			tempEntity[entityCount] = new Entity((int)Double.parseDouble(currentLine.split("//")[0]),
					(int)Double.parseDouble(currentLine.split("//")[1]),
					Double.parseDouble(currentLine.split("//")[2])/zoomScaleAmount,
					Double.parseDouble(currentLine.split("//")[3]),
					Double.parseDouble(currentLine.split("//")[4]),
					Double.parseDouble(currentLine.split("//")[5]),
					new Color(Integer.parseInt(currentLine.split("//")[6].split("-")[0]),
					Integer.parseInt(currentLine.split("//")[6].split("-")[1]),
					Integer.parseInt(currentLine.split("//")[6].split("-")[2])),
					(currentLine.split("//")[7].equals("true"))?true:false,
					zoomScaleAmount,
					currentLine.split("//")[8].equals("true")?true:false,
					currentLine.split("//")[9]);
			entityCount++;
		}
		load.close();
		MainClass.panel.setEntityNum(entityNumber);
		Render.zoomScale = zoomScaleAmount;
		MainClass.panel.setBlackMode((initialLine.split("//")[2].equals("true"))?true:false);
		MainClass.panel.setEntity(tempEntity);
		MainClass.panel.setEntitySelected(false);
		Render.pauseWasTrue = true;
	}
}