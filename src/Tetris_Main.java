public class Tetris_Main {

	public static void main(String[] args) {

		Tetris_Model TModel = new Tetris_Model();
		GameStage GStage = new GameStage(TModel);
		Tetris_View TView = new Tetris_View();
		Tetris_Controller TCon = new Tetris_Controller(TModel, TView);
		int difficulty = 400;		//���̵� ����
		
		
		GStage.addKeyListener(TCon);
		TView.setContentPane(GStage);
		TView.getContentPane().requestFocus();
		
		while(true) {
			TModel.testConsoleView();
			if( !TModel.movePos(0, 1) ) { //��� �������ٰ� ������ ���ο� ������� ����
				TModel.storeArray();
				TView.repaint();
				if( !TModel.randomBlockGen() ) { //��� ������ ������ ���ӿ���.
					System.out.println("���� ����");
					TModel.initialPan();
					continue;
				}
				TModel.searchAndPushLine();	
				TView.repaint();
			}
			TView.repaint();
			
			try {
				Thread.sleep(difficulty);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
