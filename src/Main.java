import java.awt.*;
import javax.swing.*;

public class Main {
	public static void main(String [] args) {
		//new Form("Entry", EntryForm.class).show();
		JFrame frame = new Form("Entry", EntryForm.class);
		//frame.setVisible(true);
		frame.show();
		System.out.println("Closed.");
	}
}