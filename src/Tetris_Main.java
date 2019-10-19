public class Tetris_Main {

	public static void main(String[] args) {

		Tetris_Model TModel = new Tetris_Model();
		GameStage GStage = new GameStage(TModel);
		Tetris_View TView = new Tetris_View();
		Tetris_Controller TCon = new Tetris_Controller(TModel, TView);
		int difficulty = 400;		//난이도 조정
		
		
		GStage.addKeyListener(TCon);
		TView.setContentPane(GStage);
		TView.getContentPane().requestFocus();
		
		while(true) {
			TModel.testConsoleView();
			if( !TModel.movePos(0, 1) ) { //블록 내려오다가 막히면 새로운 랜덤블록 생성
				TModel.storeArray();
				TView.repaint();
				if( !TModel.randomBlockGen() ) { //블록 생성도 막히면 게임오버.
					System.out.println("게임 오버");
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
