
public class XTankMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//MainMenu mainMenu = new MainMenu();
		//XTankGUI gui = new XTankGUI();
		//gui.run();
	}

}

enum Direction{
	Negative(-1),
	Positive(1);

    public final int val;
    private Direction(int val) {
        this.val = val;
    }
}