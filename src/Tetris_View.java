import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Tetris_View extends JFrame {
	
	public Tetris_View() {
		setTitle("테트리스 v0.3");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		setVisible(true);
		
		
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		
	}
}

@SuppressWarnings("serial")
class GameStage extends JPanel {
	private final int BlockSize = 20; //블록한변의 크기 ( 픽셀단위임!! )
	
	private Tetris_Model TModel;
	
	public GameStage(Tetris_Model model) {
		this.TModel = model;
	}
	
	public void paintComponent(Graphics g) {			//repaint 함수가 호출될때마다 실행되는 함수
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLUE);
		g.drawRect(0, 0, this.getWidth() , this.getHeight() );
		
		int[][] testView = new int[Tetris_Model.PanY][Tetris_Model.PanX];		//Pan의 복사본을 하나 만들어서 내려오는 블록을 배열에 찍어 같이 드로잉
		
		for(int x=0; x < testView.length; x++) {				
			testView[x] = TModel.getPan()[x].clone();
		}
		
		int RealSizeX = TModel.getTShape().getRealBlockSize('x', TModel.getCurShape() );
		int RealSizeY = TModel.getTShape().getRealBlockSize('y', TModel.getCurShape() );
		
		for(int y=0; y < (RealSizeY); y++) {
			
			for(int x=0; x < (RealSizeX); x++) {
				if(testView[y + TModel.getyPos()][x + TModel.getxPos()] == 0)
					testView[y + TModel.getyPos()][x + TModel.getxPos()] = TModel.getCurShape()[y][x];
			}
		}
		
		for(int y=0; y < Tetris_Model.PanY; y++) {
			for(int x=0; x < Tetris_Model.PanX; x++) {
				switch(testView[y][x]) {
				case 0:
					g.setColor(Color.BLACK);
					g.drawRect(x * BlockSize, y * BlockSize, BlockSize, BlockSize);		//배열 안에 있는 값마다 색깔지정
					if(TModel.getxPos() <= x && x < TModel.getxPos() + RealSizeX) {
						g.setColor(Color.GREEN);
						g.drawRect(x * BlockSize, y * BlockSize, BlockSize, BlockSize);		//배열 안에 있는 값마다 색깔지정
					}
					break;
				case 1:
					g.setColor(Color.RED);
					g.fill3DRect(x * BlockSize, y * BlockSize, BlockSize, BlockSize, true);
					break;
				case 2:
					g.setColor(Color.ORANGE);
					g.fill3DRect(x * BlockSize, y * BlockSize, BlockSize, BlockSize, true);
					break;
				case 3:
					g.setColor(Color.YELLOW);
					g.fill3DRect(x * BlockSize, y * BlockSize, BlockSize, BlockSize, true);
					break;
				case 4:
					g.setColor(Color.GREEN);
					g.fill3DRect(x * BlockSize, y * BlockSize, BlockSize, BlockSize, true);
					break;
				case 5:
					g.setColor(Color.BLUE);
					g.fill3DRect(x * BlockSize, y * BlockSize, BlockSize, BlockSize, true);
					break;
				case 6:
					g.setColor(Color.MAGENTA);
					g.fill3DRect(x * BlockSize, y * BlockSize, BlockSize, BlockSize, true);
					break;
				case 7:
					g.setColor(Color.PINK);
					g.fill3DRect(x * BlockSize, y * BlockSize, BlockSize, BlockSize, true);
					break;
				}
				
				
			}
		}
		//도움말 출력
		g.setColor(Color.MAGENTA);
		g.setFont(new Font("고딕체", Font.PLAIN, BlockSize));
		g.drawString("다음 블록", (Tetris_Model.PanX + 2) * BlockSize, 1 * BlockSize);
		for(int y=0; y < TModel.getTShape().getShape(TModel.getBlockQueue()[1]).length; y++) {
			for(int x=0; x < TModel.getTShape().getShape(TModel.getBlockQueue()[1])[0].length; x++) {
				if(TModel.getTShape().getShape(TModel.getBlockQueue()[1])[y][x] != 0) {
				
					switch(TModel.getBlockQueue()[1] + 1) {
					case 0:
						g.setColor(Color.BLACK);
						g.drawRect((x + Tetris_Model.PanX + 2) * BlockSize, (y + 2) * BlockSize, BlockSize, BlockSize);		//배열 안에 있는 값마다 색깔지정
						break;
					case 1:
						g.setColor(Color.RED);
						g.fill3DRect((x + Tetris_Model.PanX + 2) * BlockSize, (y + 2) * BlockSize, BlockSize, BlockSize, true);
						break;
					case 2:
						g.setColor(Color.ORANGE);
						g.fill3DRect((x + Tetris_Model.PanX + 2) * BlockSize, (y + 2) * BlockSize, BlockSize, BlockSize, true);
						break;
					case 3:
						g.setColor(Color.YELLOW);
						g.fill3DRect((x + Tetris_Model.PanX + 2) * BlockSize, (y + 2) * BlockSize, BlockSize, BlockSize, true);
						break;
					case 4:
						g.setColor(Color.GREEN);
						g.fill3DRect((x + Tetris_Model.PanX + 2) * BlockSize, (y + 2) * BlockSize, BlockSize, BlockSize, true);
						break;
					case 5:
						g.setColor(Color.BLUE);
						g.fill3DRect((x + Tetris_Model.PanX + 2) * BlockSize, (y + 2) * BlockSize, BlockSize, BlockSize, true);
						break;
					case 6:
						g.setColor(Color.MAGENTA);
						g.fill3DRect((x + Tetris_Model.PanX + 2) * BlockSize, (y + 2) * BlockSize, BlockSize, BlockSize, true);
						break;
					case 7:
						g.setColor(Color.PINK);
						g.fill3DRect((x + Tetris_Model.PanX + 2) * BlockSize, (y + 2) * BlockSize, BlockSize, BlockSize, true);
						break;
					}
				}
				
				
			}
		}
		
		
		
		
		g.setColor(Color.BLACK);
		g.drawString("점수", (Tetris_Model.PanX + 7) * BlockSize, 1 * BlockSize);
		g.setColor(Color.BLUE);
		g.drawString(Integer.toString(TModel.getCurScore()), (Tetris_Model.PanX + 7) * BlockSize, 2 * BlockSize);
		
		
		g.drawString("도움말", (Tetris_Model.PanX + 2) * BlockSize, (Tetris_Model.PanY / 2) * BlockSize);
		g.setColor(Color.BLACK);
		g.drawString("좌 우 움직이기 : LEFT RIGHT", (Tetris_Model.PanX + 2) * BlockSize, (Tetris_Model.PanY / 2 + 2) * BlockSize);
		g.drawString("빨리 내려가기 : DOWN", (Tetris_Model.PanX + 2) * BlockSize, (Tetris_Model.PanY / 2 + 3) * BlockSize);
		g.drawString("한번에 블록 바닥착지 : UP", (Tetris_Model.PanX + 2) * BlockSize, (Tetris_Model.PanY / 2 + 4) * BlockSize);
		g.drawString("좌 우 회전 : D F", (Tetris_Model.PanX + 2) * BlockSize, (Tetris_Model.PanY / 2 + 5) * BlockSize);
		g.drawString("다시시작 : R", (Tetris_Model.PanX + 2) * BlockSize, (Tetris_Model.PanY / 2 + 6) * BlockSize);
		
	}
}