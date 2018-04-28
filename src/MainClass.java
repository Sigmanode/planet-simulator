import javax.swing.JFrame;

public class MainClass{
	static int maxEntities = 1000;
	static Render panel = new Render();
	
	public static void main(String args[]){
		JFrame frame = new JFrame("Planet Simulator");
		MenuBar menuBar = new MenuBar();
		frame.setSize(1005,627);
		frame.setLayout(null);
		frame.add(panel);
		frame.add(menuBar);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		
	}
}
