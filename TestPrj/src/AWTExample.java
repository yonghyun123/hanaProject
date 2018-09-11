import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;

public class AWTExample {

	public static void main(String[] args) {
		Frame frame = new Frame("처음으로 만든 프레임");
		frame.setSize(1000, 800);
		frame.setVisible(true);
		
		Button button = new Button("AWT 버튼");
		Button button2 = new Button("AWT 버튼2");
		frame.setLayout(new FlowLayout());
		
		frame.add(button);
		frame.add(button2);
		
		Label label = new Label("AWT Label");
		frame.add(label);
		
		TextField textField = new TextField("Id", 10);
		frame.add(textField);
		
		TextArea textArea = new TextArea(5, 30);
		frame.add(textArea);
		
		Checkbox checkbox = new Checkbox("man", true);
		frame.add(checkbox);
		
		CheckboxGroup cg = new CheckboxGroup();
		Checkbox cb1 = new Checkbox("male", true, cg);
		Checkbox cb2 = new Checkbox("female",false, cg);
		
		frame.add(cb1);
		frame.add(cb2);
		
		Choice selectBox = new Choice();
		selectBox.add("박찬호");
		selectBox.add("박지성");
		selectBox.add("박찬숙");
		
		frame.add(selectBox);
		
//		frame.setResizable(false);
		
	}
}
