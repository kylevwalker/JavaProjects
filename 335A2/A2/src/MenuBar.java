import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;


/**
 * This class implements a MenuBar within the GUI.shell instance of LilLexiMain.
 * The shell's MenuBar is not set until setMenu is called.
 * The menu implements drop downs for each SubMenu type.
 * 
 * @author Ryan Pecha
 */
public class MenuBar {
	
	// main menu
	Menu menuBar;
	
	// File
	Menu fileMenu;
	MenuItem fileMenuHeader, fileExitItem;
	
	// Edit
	Menu editMenu;
	MenuItem editMenuHeader, editUndoItem, editRedoItem;
	
	// Format
	Menu formatMenu;
	MenuItem formatMenuHeader, formatColumns1Item, formatColumns2Item;
	
	// Font Size
	Menu fontSizeMenu;
	MenuItem fontSizeMenuHeader;
	ArrayList<MenuItem> fontSizeMenuItems;
	
	// Font Style
	Menu fontStyleMenu;
	MenuItem fontStyleMenuHeader;
	ArrayList<MenuItem> fontStyleMenuItems;
	
	// Insert
	Menu insertMenu;
	MenuItem insertMenuHeader,insertImageItem, insertRectItem;
	
	
	/*
	 * This method initializes all menu components and their listeners
	 * before setting the menu bar of LilLexiMain.gui.shell
	 */
	public void setMenu(Display display, Shell shell) {
		
		// MenuBar for all menus
		menuBar = new Menu(shell, SWT.BAR);
		
		
		
		// File menu
		fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("File");
		fileMenuHeader.setMenu(fileMenu);
		// Exiting program
		fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
		fileExitItem.setText("Exit");
		fileExitItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
				display.dispose();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				shell.close();
				display.dispose();
			}
		});
		
		
		
		// Edit menu
		editMenu = new Menu(shell, SWT.DROP_DOWN);
		editMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		editMenuHeader.setText("Edit");
		editMenuHeader.setMenu(editMenu);
		// Undo
		editUndoItem = new MenuItem(editMenu, SWT.PUSH);
		editUndoItem.setText("Undo");
		editUndoItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				LilLexiMain.gui.undo();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				LilLexiMain.gui.undo();
			}
		});
		// Redo
		editRedoItem = new MenuItem(editMenu, SWT.PUSH);
		editRedoItem.setText("Redo");
		editRedoItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				LilLexiMain.gui.redo();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				LilLexiMain.gui.redo();
			}
		});
		
		
		
		// Insert Menu
		insertMenu = new Menu(shell, SWT.DROP_DOWN);
		insertMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		insertMenuHeader.setText("Insert");
		insertMenuHeader.setMenu(insertMenu);
		
		// Image
		insertImageItem = new MenuItem(insertMenu, SWT.PUSH);
		insertImageItem.setText("Image");
		
		// Rectangle
		insertRectItem = new MenuItem(insertMenu, SWT.PUSH);
		insertRectItem.setText("Rectangle");
		insertRectItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				IBeam caret = LilLexiMain.gui.getCaret();
				caret.addGlyph(new Rect(new Point(40,25)));
				//caret.addGlyph(new Rect(new Rectangle(caret.getLocation().x, caret.getLocation().y, 40, 25)));
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				IBeam caret = LilLexiMain.gui.getCaret();
				caret.addGlyph(new Rect(new Point(40,25)));
				//caret.addGlyph(new Rect(new Rectangle(caret.getLocation().x, caret.getLocation().y, 40, 25)));
			}
		});
		
		
		
		
		class FormatSelectionListener implements SelectionListener {
			private int columnCount;
			public FormatSelectionListener(int columnCount) {
				this.columnCount = columnCount;
			}
			public void widgetSelected(SelectionEvent e) {
				CommandFormat cf = new CommandFormat(columnCount);
				LilLexiMain.gui.executeCommand(cf);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				CommandFormat cf = new CommandFormat(columnCount);
				LilLexiMain.gui.executeCommand(cf);
			}
		}
		// Format Menu
		formatMenu = new Menu(shell, SWT.DROP_DOWN);
		formatMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		formatMenuHeader.setText("Format");
		formatMenuHeader.setMenu(formatMenu);
		
		// 1 columns format
		formatColumns1Item = new MenuItem(formatMenu, SWT.PUSH);
		formatColumns1Item.setText("1 Column");
		FormatSelectionListener cfsl1 = new FormatSelectionListener(1);			
		formatColumns1Item.addSelectionListener(cfsl1);
		
		// 2 columns format
		// broken :(
		/*
		formatColumns2Item = new MenuItem(formatMenu, SWT.PUSH);
		formatColumns2Item.setText("2 Columns");
		FormatSelectionListener cfsl2 = new FormatSelectionListener(2);			
		formatColumns2Item.addSelectionListener(cfsl2);
		*/
		
		
		
		// Event class for setting font size
		class FontSizeSelectionListener implements SelectionListener {
			private int fontSize;
			public FontSizeSelectionListener(int fontSize) {
				this.fontSize = fontSize;
			}
			public void widgetSelected(SelectionEvent e) {
				CommandFontSize cf = new CommandFontSize(fontSize);
				LilLexiMain.gui.executeCommand(cf);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				CommandFontSize cf = new CommandFontSize(fontSize);
				LilLexiMain.gui.executeCommand(cf);
			}
		}
		// FontSize Menu
		fontSizeMenu = new Menu(shell, SWT.DROP_DOWN);
		fontSizeMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fontSizeMenuHeader.setText("Font Size");
		fontSizeMenuHeader.setMenu(fontSizeMenu);
		fontSizeMenuItems = new ArrayList<MenuItem>();
		int fontMax = 20;
		int fontMin = 5;
		
		// Iterating over font sizes and creating MenuItems for each one
		for (int fontSize = fontMin; fontSize <= fontMax; fontSize++) {
			
			// Creating the drop-down item
			MenuItem fontSizeMenuHeaderItem = new MenuItem(fontSizeMenu, SWT.PUSH);
			fontSizeMenuHeaderItem.setText(Integer.toString(fontSize));
			fontSizeMenuItems.add(fontSizeMenuHeader);
			
			// Binding command to menu item
			FontSizeSelectionListener nsl = new FontSizeSelectionListener(fontSize);			
			fontSizeMenuHeaderItem.addSelectionListener( nsl);
		}
		
		

		// Event class for setting font type
		class FontStyleSelectionListener implements SelectionListener {
			private String fontType;
			public FontStyleSelectionListener(String fontType) {
				this.fontType = fontType;
			}
			public void widgetSelected(SelectionEvent e) {
				CommandFontType cf = new CommandFontType(fontType);
				LilLexiMain.gui.executeCommand(cf);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				CommandFontType cf = new CommandFontType(fontType);
				LilLexiMain.gui.executeCommand(cf);
			}
		}
		// FontStyle Menu
		fontStyleMenu = new Menu(shell, SWT.DROP_DOWN);
		fontStyleMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fontStyleMenuHeader.setText("Font Style");
		fontStyleMenuHeader.setMenu(fontStyleMenu);
		fontStyleMenuItems = new ArrayList<MenuItem>();
		List<String> fontStyles = Arrays.asList("Courier", "Arial", "Calibri", "Cambria");

		// Iterating over fonts and creating MenuItems for each one
		for (String fontStyle : fontStyles) {
			
			// Creating the drop-down item
			MenuItem fontStyleMenuHeaderItem = new MenuItem(fontStyleMenu, SWT.PUSH);
			fontStyleMenuHeaderItem.setText(fontStyle);
			fontStyleMenuItems.add(fontSizeMenuHeader);
			
			// Binding command to menu item
			FontStyleSelectionListener nsl = new FontStyleSelectionListener(fontStyle);			
			fontStyleMenuHeaderItem.addSelectionListener( nsl);
		}

		

		// Setting MenuBar inside shell
		Menu systemMenu = Display.getDefault().getSystemMenu();
		if (systemMenu != null) {
			MenuItem[] mi = systemMenu.getItems();
			mi[0].addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					System.out.println("About");
				}
			});
		}
		shell.setMenuBar(menuBar);
		
	}
}
