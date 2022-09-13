/**
 * UI test for Concentration
 * 
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridLayout;
import java.util.List;
import java.util.*;


public class ConcentrationGameUI
{
	//public static Display display;
	public static void main(String args[])
	{
		display = new Display();
		
		// for now just create the card images here in a list
		List<Image> imList = new ArrayList<Image>();
		
		imList.add(new Image(display, "apple.jpg"));
		imList.add(new Image(display, "pear.jpg"));
		imList.add(new Image(display, "peach.jpg"));
		imList.add(new Image(display, "peach.jpg"));
		imList.add(new Image(display, "pineapple.jpg"));
		imList.add(new Image(display, "apple.jpg"));
		imList.add(new Image(display, "pear.jpg"));
		imList.add(new Image(display, "pineapple.jpg"));
		imList.add(new Image(display, "greenapple.jpg"));
		imList.add(new Image(display, "avocado.jpg"));
		imList.add(new Image(display, "greenapple.jpg"));
		imList.add(new Image(display, "avocado.jpg"));
		
		// and a 'blank' image to simulate card flips
		Image blank = new Image(display, "blank.jpg");
		
		Shell shell = new Shell(display);
		shell.setSize(500,600);

		GridLayout gridLayout = new GridLayout();
		shell.setLayout( new GridLayout());

		//---- our Widgets start here
	    Composite upperComp = new Composite(shell, SWT.NO_FOCUS);
	    Composite lowerComp = new Composite(shell, SWT.NO_FOCUS);
	    
		Canvas canvas = new Canvas(upperComp, SWT.NONE);
		canvas.setSize(500,500);
		
		canvas.addPaintListener(new CanvasPaintListener(shell, imList));		
		canvas.addMouseListener(new CanvasMouseListener(shell, imList, blank));

		Button quitButton = new Button(lowerComp, SWT.PUSH);
		quitButton.setText("Quit");
		quitButton.setSize(100, 50);
		quitButton.addSelectionListener(new ButtonSelectionListener());	    	    
		//---- our Widgets end here
		
		// -----------------------Testing
		/* CODE FOR CHECKING MATCH
		Card card1 = new Card();
		card1.front = imList.get(0);
		Card card2 = new Card();
		card2.front = imList.get(1);
		if (card1.front == card2.front) {
			System.out.println("MATCH");
		}
		else {
			System.out.println("NOT MATCHING");
		}
		*/
		
		shell.open();
		while( !shell.isDisposed())
		{
			if(!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	} 
}

/**
 * mouse clicks in the canvas
 * 
 */
class CanvasMouseListener implements MouseListener 
{
    Shell shell;
    List<Image> imList;
    Image blank;
    
    public CanvasMouseListener(Shell sh, List<Image> im, Image blankImage)
    {
    	shell = sh; imList = im; blank = blankImage;
    } 
    
	public void mouseDoubleClick(MouseEvent event){}
	
	public void mouseDown(MouseEvent event)
	{
		Rectangle rect = shell.getClientArea();
		ImageData data = imList.get(0).getImageData();
		
		int col = event.x/data.width;
		int row = event.y/data.height;
		int idx = col + row * rect.width/data.width;
		if (idx < imList.size())
			imList.set(idx, blank);
		System.out.println(idx);
		shell.redraw();
		shell.update();
		
	}
	public void mouseUp(MouseEvent e){}
}	


/**
 * repaints of the canvas
 * 
 */
class CanvasPaintListener implements PaintListener 
{
    Shell shell;
    List<Image> imList;
    
    public CanvasPaintListener(Shell sh, List<Image> im)
    {
    	shell = sh; imList = im;
    }
    
	public void paintControl(PaintEvent event) 
	{
		
		Rectangle rect = shell.getClientArea();
		ImageData data = imList.get(0).getImageData();
		int stride = rect.width/data.width;

        for (int i = 0, j = 0; i < imList.size(); i++)
        	event.gc.drawImage(imList.get(i), (i%stride)*data.width, (i/stride)*data.height);
	}
}  

/**
 * Quit button
 * 
 */

