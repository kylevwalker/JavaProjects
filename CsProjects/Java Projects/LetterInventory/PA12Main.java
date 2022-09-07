import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class PA12Main {

	public static void main(String[] args) {
		Scanner file = null;
		try {
			file = new Scanner(new File(args[0]));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// read dictionary into an ArrayList
		List<String> dictionary = new ArrayList<String>();
		while (file.hasNextLine()) {
			dictionary.add(file.nextLine());
		}

		String text = new String(args[1]);
		int max = Integer.valueOf(args[2]);

		AnagramSolver solver = new AnagramSolver(dictionary);
		
		boolean done = false;
        while (!done) {
            if (text.length() == 0) {
                done = true;
            } else {
                solver.print(text, max);
            }
        }
	}
}



