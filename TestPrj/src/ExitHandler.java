import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ExitHandler extends WindowAdapter {
	ChatFrame chatFrame;
	
	public ExitHandler(ChatFrame frame){
		this.chatFrame = frame;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		chatFrame.finish();
	}

}
