import java.awt.event.*;

public class Tetris_Controller implements KeyListener{

	private Tetris_Model TModel;
	private Tetris_View TView;
	
	public Tetris_Controller(Tetris_Model model, Tetris_View view) {
		this.TModel = model;
		this.TView = view;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {	//키 바인딩
		case KeyEvent.VK_D:
			TModel.rotateCurShape('L');
			break;
		case KeyEvent.VK_F:
			TModel.rotateCurShape('R');
			break;
		case KeyEvent.VK_R:
			TModel.initialPan();
			break;
		case KeyEvent.VK_LEFT:
			TModel.movePos(-1, 0);
			break;
		case KeyEvent.VK_RIGHT:
			TModel.movePos(+1, 0);
			break;
		case KeyEvent.VK_UP:
			TModel.crashLandBlock();
			TModel.storeArray();
			TModel.searchAndPushLine();	
			TView.repaint();

			if( !TModel.randomBlockGen() ) { //블록 생성도 막히면 게임오버.
				System.out.println("게임 오버");
				TModel.initialPan();
			}
			break;
		case KeyEvent.VK_DOWN:
			TModel.movePos(0, 1);
			break;
		}
			
		TView.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
