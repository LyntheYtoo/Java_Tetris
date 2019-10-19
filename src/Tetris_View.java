import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Tetris_View extends JFrame {
	
	public Tetris_View() {
		setTitle("��Ʈ���� v0.3");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		setVisible(true);
		
		
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		
	}
}

@SuppressWarnings("serial")
class GameStage extends JPanel {
	private final int BlockSize = 20; //����Ѻ��� ũ�� ( �ȼ�������!! )
	
	private Tetris_Model TModel;
	
	public GameStage(Tetris_Model model) {
		this.TModel = model;
	}
	
	public void paintComponent(Graphics g) {			//repaint �Լ��� ȣ��ɶ����� ����Ǵ� �Լ�
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLUE);
		g.drawRect(0, 0, this.getWidth() , this.getHeight() );
		
		int[][] testView = new int[Tetris_Model.PanY][Tetris_Model.PanX];		//Pan�� ���纻�� �ϳ� ���� �������� ����� �迭�� ��� ���� �����
		
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
					g.drawRect(x * BlockSize, y * BlockSize, BlockSize, BlockSize);		//�迭 �ȿ� �ִ� ������ ��������
					if(TModel.getxPos() <= x && x < TModel.getxPos() + RealSizeX) {
						g.setColor(Color.GREEN);
						g.drawRect(x * BlockSize, y * BlockSize, BlockSize, BlockSize);		//�迭 �ȿ� �ִ� ������ ��������
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
		//���� ���
		g.setColor(Color.MAGENTA);
		g.setFont(new Font("���ü", Font.PLAIN, BlockSize));
		g.drawString("���� ���", (Tetris_Model.PanX + 2) * BlockSize, 1 * BlockSize);
		for(int y=0; y < TModel.getTShape().getShape(TModel.getBlockQueue()[1]).length; y++) {
			for(int x=0; x < TModel.getTShape().getShape(TModel.getBlockQueue()[1])[0].length; x++) {
				if(TModel.getTShape().getShape(TModel.getBlockQueue()[1])[y][x] != 0) {
				
					switch(TModel.getBlockQueue()[1] + 1) {
					case 0:
						g.setColor(Color.BLACK);
						g.drawRect((x + Tetris_Model.PanX + 2) * BlockSize, (y + 2) * BlockSize, BlockSize, BlockSize);		//�迭 �ȿ� �ִ� ������ ��������
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
		g.drawString("����", (Tetris_Model.PanX + 7) * BlockSize, 1 * BlockSize);
		g.setColor(Color.BLUE);
		g.drawString(Integer.toString(TModel.getCurScore()), (Tetris_Model.PanX + 7) * BlockSize, 2 * BlockSize);
		
		
		g.drawString("����", (Tetris_Model.PanX + 2) * BlockSize, (Tetris_Model.PanY / 2) * BlockSize);
		g.setColor(Color.BLACK);
		g.drawString("�� �� �����̱� : LEFT RIGHT", (Tetris_Model.PanX + 2) * BlockSize, (Tetris_Model.PanY / 2 + 2) * BlockSize);
		g.drawString("���� �������� : DOWN", (Tetris_Model.PanX + 2) * BlockSize, (Tetris_Model.PanY / 2 + 3) * BlockSize);
		g.drawString("�ѹ��� ��� �ٴ����� : UP", (Tetris_Model.PanX + 2) * BlockSize, (Tetris_Model.PanY / 2 + 4) * BlockSize);
		g.drawString("�� �� ȸ�� : D F", (Tetris_Model.PanX + 2) * BlockSize, (Tetris_Model.PanY / 2 + 5) * BlockSize);
		g.drawString("�ٽý��� : R", (Tetris_Model.PanX + 2) * BlockSize, (Tetris_Model.PanY / 2 + 6) * BlockSize);
		
	}
}