
public class Tetris_Model {
	public final static int PanX = 10, PanY = 24;//������ �迭ũ�� ����
	public final static int MaxScore = 1000000000;
	
	private int xPos, yPos;
	private int[][] Pan; //��Ʈ���� ������.
	
	private Tetris_Shape TShape; //���� ��ü
	private int[][] curShape; //���� ����� �迭
	private int[] BlockGenQueue; //��ϻ��� �̸�����
	
	private int curScore;
	
	public Tetris_Model() {
		TShape = new Tetris_Shape();
		BlockGenQueue = new int[2];
		initialPan();
		
	}
	
	public boolean randomBlockGen() { //������� �̾Ƴ��� curShape�� ���� �ȵǸ� false ��ȯ
		
		BlockGenQueue[1] = (int) (Math.random()*7);		//�̸�����
		curShape = TShape.getShape(BlockGenQueue[0]);
		
		for(int i=0; i < curShape.length; i++) {		//���� �����ϱ� ���ؼ� �ٸ��� �־����
			for(int j=0; j < curShape[0].length; j++) {
				
				if(curShape[i][j] != 0)
					curShape[i][j] = BlockGenQueue[0] + 1;		//���� �ε���+1 (ù �ε����� 0�̴ϱ� ���� �������� �����ϱ� ���ؼ�)
			}
		}
		BlockGenQueue[0] = BlockGenQueue[1];
		
		xPos = 	Pan[0].length / 2 - 1;	//��� ������ġ pos���� �������� ��� �����϶� ��
		yPos = 	0;
		//- TShape.getRealBlockSize('y', curShape); //����ũ�⿡ -���� �־������ν� ������ġ�� Pan���� ����
		
		if(isMoveAble(curShape, xPos, yPos))	//�ǿ��� ��� ������ġ�� �ٸ� ����ִ��� Ȯ�� false�� ������ġ�� ���� �ִٴ°�
			return true;
		else
			return false;
	}
	
	public void initialPan() { //�� �ʱ�ȭ
		BlockGenQueue[0] = (int) (Math.random()*7);
		BlockGenQueue[1] = -1;
		Pan = new int[PanY][PanX];
		for(int y = 0; y < PanY; y++) {
			for(int x = 0; x < PanX; x++) {
				Pan[y][x] = 0;
			}
		}
		curScore = 0;
		randomBlockGen();
		
	}
	
	public void searchAndPushLine() {		//ä���� ���� �ִٸ� �� ���� ��ϵ� ��ĭ�� ������ ���
		int IsLineFull = 0; //������ ��á���� Ȯ���ϴ� ����
		
		for(int y = 0; y < Pan.length; y++) {
			IsLineFull = 0;
			
			for(int x = 0; x < Pan[0].length; x++) {
				
				if(Pan[y][x] != 0)
					IsLineFull++;
			}
			
			if(IsLineFull == Pan[0].length) {
				
				for(int PushY = y; PushY > 0; PushY--) {		//PushY�� ���� ���� ã�� y�� �ε��� 
					for(int x = 0; x < Pan[0].length; x++) {
						Pan[PushY][x] = Pan[PushY - 1][x];
					}
				}
				if(curScore < MaxScore)
					curScore += 50;
			}
		}
	
	}
	
	public void storeArray() {		//���� �������� ��� ������
		for(int y = 0; y < TShape.getRealBlockSize('y', curShape); y++) {
			for(int x = 0; x < TShape.getRealBlockSize('x', curShape); x++) {
				if(Pan[y + yPos][x + xPos] == 0)		//0�϶��� �迭 ä���
					Pan[y + yPos][x + xPos] = curShape[y][x];
			}
		}
	}
	
	
	public void crashLandBlock() {
		/*for(int i = PanY - 1; i >= 0 ; i--) {
			
			if(movePos(0, i - yPos) ) {
				break;
			}
		}*/
		
		for(int i = 0; i < PanY ; i++) {
			
			if(!movePos(0, 1) ) {
				break;
			}
		}
	}
	
	public boolean movePos(int xPosAdd, int yPosAdd) { //Pos�� ���� ������ ���ޱ�
		if (isMoveAble(curShape, xPos + xPosAdd, yPos + yPosAdd)) {		//�����ϼ� �ִ��� Ȯ������
			xPos += xPosAdd; yPos += yPosAdd;
			return true;
		}
		else
			return false;
	}
	
	public boolean isMoveAble(int [][] target, int xPrePos, int yPrePos) {	//PrePos�� �̵���������		target�� ���� ����
		int sizeY = TShape.getRealBlockSize('Y',target);					//������������ target������ �� �� ������ false ������ true ��ȯ
		int sizeX = TShape.getRealBlockSize('X',target);
		
		if((yPrePos + sizeY) > Pan.length || (xPrePos + sizeX) > Pan[0].length)		//y�� x�������� �����Ͼ� ���ϰ� ����size ���Ѱ�
			return false;																				//��ü���� ũ�⺸�� �۾ƾ� ���!
		
		if(xPrePos < 0)		//������ �� ��ġ�� ������� ���! y�������ϴ� ������ ������ϻ��� �޼ҵ忡�� Pan���� ������ �����ϰ� �Ϸ���
			return false;	
		
		//�ǿ� �̹� �ִ� ����̶� 	``	��ġ�� �ʾƾ� ���!
		for(int y = yPrePos; y < (yPrePos + sizeY); y++) {
			
			for(int x = xPrePos; x < (xPrePos + sizeX); x++) {
				if(Pan[y][x] != 0 && target[y - yPrePos][x - xPrePos] != 0)		 {//�ǰ� Ÿ���� ���� 0�� �ƴҶ� ���!
					return false;
				}
			}
		}
		return true;
	}
	
	public void rotateCurShape(char direction) {
		if(isRotateAble(curShape, direction)) {
			switch(direction) {
			case 'r':
			case 'R':
				curShape = TShape.rotateRight(curShape);
				positioningCurShape();
				break;
			case 'l':
			case 'L':
				curShape = TShape.rotateLeft(curShape);
				positioningCurShape();
				break;
			}
		}
	}
	
	//���� �ƴϸ� ������ �����ִ��� Ȯ��
	public boolean isRotateAble(int [][] target, char direction) { //direction���� R�̸� ������ L�̸� ����
		
		int sizeX =	TShape.getRealBlockSize('x', target);
		int sizeY =	TShape.getRealBlockSize('y', target);
		int bigsize = sizeX > sizeY ?
				sizeX : sizeY; //size�� ������ ����ũ��. ����ũ�� ���μ����߿� ū�� bigsize�� ����
					
		for(int y=0; y < sizeX; y++) {		//����ũ�⸸ŭ�� ���� ũ�⸸ŭ�� �ǿ��� Ȯ���Ѵ�.
			for(int x=0; x < sizeY; x++) {			//���� ���ΰ� �ٲ�ϱ� sizeXY�� �ٲ۴�.
				/*
				if(sizeY == bigsize) {
					if(xPos+bigsize-1 >= PanX)
						return false;
				}
				else {
					if(yPos+bigsize-1 >= PanY)
						return false;
				} */
				if(y + yPos >= PanY || x + xPos >= PanX)
					return false;
				if (Pan[y + yPos][x + xPos] != 0 )
					switch(direction) { 		//���� ���� �ƴϸ� ������
						case 'r':
						case 'R':
							if(target[bigsize-1-x][y] != 0)	//���� �ǰ� Ÿ���� ���� 0�� �ƴѰ�쿡 ���� ������ ��ȯ
								return false;
							break;
						case 'l':
						case 'L':
							if(target[x][bigsize-1-y] != 0)
								return false;
							break;
						default:
							return false;
					}
			}
		}
		return true;
	}
	
	
	
	public void positioningCurShape() {		//������ ��¦ �Ⱥپ� ������ 0,0�� ��¦ ���δ�.
		int CheckZero;
		int LineZeroCount = 0; //������ ��á���� Ȯ���ϴ� ����
		
		//���� ����� ���
		for(int y = 0; y < curShape.length; y++) {
			CheckZero = 0;
			
			for(int x = 0; x < curShape[0].length; x++) {
				
				if(curShape[y][x] == 0)
					CheckZero++;
			}
			
			if(CheckZero == curShape[0].length) {
				LineZeroCount++;
			}
			else
				break;
		}
		for(int y=0; y <curShape.length; y++) {
			for(int x=0; x < curShape[0].length; x++) {
				if(y + LineZeroCount >= curShape.length)
					break;
				curShape[y][x] = curShape[y + LineZeroCount][x];
			}
		}
		if(LineZeroCount != 0) {
			for(int x=0; x < curShape[0].length; x++) {
				curShape[curShape[0].length - 1][x] = 0;
			}
		}
		//���� ����� ���
		LineZeroCount = 0;
		for(int x=0; x < curShape[0].length; x++) {
			CheckZero = 0;
			
			for(int y=0; y < curShape.length; y++) {
				
				if(curShape[y][x] == 0)
					CheckZero++;
			}
			
			if(CheckZero == curShape.length) {
				LineZeroCount++;
			}
			else
				break;
		}
		for(int x=0; x < curShape[0].length; x++) {
			for(int y=0; y < curShape.length; y++) {
				if(x + LineZeroCount >= curShape[0].length)
					break;
				curShape[y][x] = curShape[y][x + LineZeroCount];
			}
		}
		if(LineZeroCount != 0) {
			for(int y=0; y < curShape.length; y++) {
				curShape[y][curShape.length - 1] = 0;
			}
		}
	}
	
	
	
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	public int getxPos() {
		return xPos;
	}
	public int getyPos() {
		return yPos;
	}
	public void setPan(int[][] Pan) {
		this.Pan = Pan;
	}
	public int[][] getPan() {
		return Pan;
	}
	public void setCurShape(int[][] curShape) {
		this.curShape = curShape;
	}
	public int[][] getCurShape() {
		return curShape;
	}
	public Tetris_Shape getTShape() {
		return TShape;
	}
	public int getCurScore() {
		return curScore;
	}
	public int[] getBlockQueue() {
		return BlockGenQueue;
	}
	
	
	
	public void testConsoleView() {		//�ܼ�â�� ���� �迭 ǥ��
		int[][] testView = new int[Tetris_Model.PanY][Tetris_Model.PanX];
		
		for(int x=0; x < testView.length; x++) {
			testView[x] = Pan[x].clone();
		}
		
		for(int y=0; y < (TShape.getRealBlockSize('y', curShape)); y++) {
			
			for(int x=0; x < (TShape.getRealBlockSize('x', curShape)); x++) {
				if(testView[y + yPos][x + xPos] == 0)
					testView[y + yPos][x + xPos] = curShape[y][x];
			}
		}
		System.out.println("�׽�Ʈ �� :");
		for(int y=0; y < testView.length; y++) {
			for(int x=0; x < testView[0].length; x++) {
				System.out.print(" " + testView[y][x]);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
		/*
		System.out.println("���� �� :");
		for(int y=0; y < Pan.length; y++) {
			for(int x=0; x < Pan[0].length; x++) {
				System.out.print(" " + Pan[y][x]);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
		*/
	}

}









/*
	��Ʈ���� ����� ���� Ŭ����
	ȸ�����۱��� �޼ҵ� ����
	����� ��ȣ ������� I, J, L, S, Z, O, T �� ����
				   0  1  2  3  4  5  6
	
	7���� 4X4 ���� �迭ȭ �ؼ� Shapes��� �迭�� �־��


*/
class Tetris_Shape { 
	public final int[][][] Shapes = {	{	{1, 0, 0, 0},	//I 0
											{1, 0, 0, 0},
											{1, 0, 0, 0},
											{1, 0, 0, 0}	},
													
										{	{0, 1, 0, 0},	//J 1
											{0, 1, 0, 0},
											{1, 1, 0, 0},
											{0, 0, 0, 0}	},
											
										{	{1, 0, 0, 0},	//L 2
											{1, 0, 0, 0},
											{1, 1, 0, 0},
											{0, 0, 0, 0}	},
											
										{	{0, 1, 1, 0},	//S 3
											{1, 1, 0, 0},
											{0, 0, 0, 0},
											{0, 0, 0, 0}	},
									
										{	{1, 1, 0, 0},	//Z 4
											{0, 1, 1, 0},
											{0, 0, 0, 0},
											{0, 0, 0, 0}	},
											
										{	{1, 1, 0, 0},	//O 5
											{1, 1, 0, 0},
											{0, 0, 0, 0},
											{0, 0, 0, 0}	},
											
										{	{1, 1, 1, 0},	//T 6
											{0, 1, 0, 0},
											{0, 0, 0, 0},
											{0, 0, 0, 0}	}	};
	
	public int getRealBlockSize(char xORy, int[][] target) { //����� ���� ������ ���ϴ� �Լ�
		int xMax =0, yMax=0, x, y;
		
		for(y=0; y<target.length; y++) {
			for(x=0; x < target[0].length; x++) {
				
				if(target[y][x] != 0) {
					if(xMax <= x)
						xMax = x + 1;
					if(yMax <= y)
						yMax = y + 1;
				}
			}
		}
		switch(xORy) {	//x�϶� xMax y�϶� yMax ��ȯ�ϰ� ��͵� �ƴϸ� -1 ��ȯ
			case 'x' :
			case 'X' :
				return xMax;
			case 'y' :
			case 'Y' :
				return yMax;
			default:
				return -1;
		}
	}
	
	public int[][] rotateRight(int[][] target) { //���������� ȸ��
		int sizeX =	getRealBlockSize('x', target);
		int sizeY =	getRealBlockSize('y', target);
		int bigsize = sizeX > sizeY ?
				sizeX : sizeY; //size�� ������ ����ũ��. ����ũ�� ���μ����߿� ū�� bigsize�� ����
		int[][] result = new int[target.length][target[0].length];
		
		for(int y=0; y < target.length; y++) {
			for(int x=0; x < target[0].length; x++) {
				if(y >= bigsize || x >= bigsize) {
					result[y][x] = 0;
				}
				else {
					result[y][x] = target[bigsize-1-x][y]; //����ũ�⸸ ȸ���ؼ� �迭�� ����.
				}
			}
		}
		return result;
		
	}
	public int[][] rotateLeft(int[][] target) { //�������� ȸ��
		int sizeX =	getRealBlockSize('x', target);
		int sizeY =	getRealBlockSize('y', target);
		int bigsize = sizeX > sizeY ?
				sizeX : sizeY; //size�� ������ ����ũ��. ����ũ�� ���μ����߿� ū�� bigsize�� ����
		int[][] result = new int[target.length][target[0].length];
		
		for(int y=0; y < target.length; y++) {
			for(int x=0; x < target[0].length; x++) {
				if(y >= bigsize || x >= bigsize) {	//���� ũ�⿡�� ��� ���� 0���� �ʱ�ȭ
					result[y][x] = 0;
				}
				else {
					result[y][x] = target[x][bigsize-1-y]; //����ũ�⸸ ȸ���ؼ� �迭�� ����.
				}
			}
		}
		return result;
		
	}
	public int[][] getShape(int i) { //��������
		return Shapes[i];
	}
}

/*
class PalseHandler extends Thread {
	//���ϰ� �ϱ����� ���־��� ������ ��Ƽ� �ۼ�
	Tetris_Model TModel;
	int[][] TM_curShape;
	int[][] TM_Pan;
	Tetris_Shape TM_TShape;
	
	public void run() {
		while(true) {
			testConsoleView();
			if( !TModel.movePos(0, 1) ) { //��� �������ٰ� ������ ���ο� ������� ����
				TModel.storeArray();
				if( !TModel.randomBlockGen() ) { //��� ������ ������ ���ӿ���.
					System.out.println("���� ����");
					break;
				}
			}
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public PalseHandler(Tetris_Model Model) {
		this.TModel = Model;
		TM_curShape = TModel.getCurShape();
		TM_Pan = TModel.getPan();
		TM_TShape = TModel.getTShape();
		
	}
	
	public void testConsoleView() {		//�ܼ�â�� ���� �迭 ǥ��
		int[][] testView = new int[Tetris_Model.PanY][Tetris_Model.PanX];
		int TM_yPos = TModel.getyPos();
		int TM_xPos = TModel.getxPos();
		
		for(int x=0; x < testView.length; x++) {
			testView[x] = TModel.getPan()[x].clone();
		}
		
		for(int y=TM_yPos; y < (TM_yPos + TM_TShape.getRealBlockSize('y', TM_curShape)); y++) {
			
			for(int x=TM_xPos; x < (TM_xPos + TM_TShape.getRealBlockSize('x', TM_curShape)); x++) {
				if(y >= 0)
					testView[y][x] = TM_curShape[y - TM_yPos][x - TM_xPos];
			}
		}
		System.out.println("�׽�Ʈ �� :");
		for(int y=0; y < testView.length; y++) {
			for(int x=0; x < testView[0].length; x++) {
				System.out.print(" " + testView[y][x]);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("���� �� :");
		for(int y=0; y < TModel.getPan().length; y++) {
			for(int x=0; x < TModel.getPan()[0].length; x++) {
				System.out.print(" " + TModel.getPan()[y][x]);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
	}
}
*/