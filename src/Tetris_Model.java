
public class Tetris_Model {
	public final static int PanX = 10, PanY = 24;//게임판 배열크기 지정
	public final static int MaxScore = 1000000000;
	
	private int xPos, yPos;
	private int[][] Pan; //테트리스 게임판.
	
	private Tetris_Shape TShape; //모양들 객체
	private int[][] curShape; //현재 블록의 배열
	private int[] BlockGenQueue; //블록생성 미리보기
	
	private int curScore;
	
	public Tetris_Model() {
		TShape = new Tetris_Shape();
		BlockGenQueue = new int[2];
		initialPan();
		
	}
	
	public boolean randomBlockGen() { //랜덤블록 뽑아내고 curShape에 저장 안되면 false 반환
		
		BlockGenQueue[1] = (int) (Math.random()*7);		//미리보기
		curShape = TShape.getShape(BlockGenQueue[0]);
		
		for(int i=0; i < curShape.length; i++) {		//색깔 구분하기 위해서 다른값 넣어놓기
			for(int j=0; j < curShape[0].length; j++) {
				
				if(curShape[i][j] != 0)
					curShape[i][j] = BlockGenQueue[0] + 1;		//도형 인덱스+1 (첫 인덱스는 0이니까 색깔 넣을려고 구분하기 위해서)
			}
		}
		BlockGenQueue[0] = BlockGenQueue[1];
		
		xPos = 	Pan[0].length / 2 - 1;	//블록 생성위치 pos값은 떨어지는 블록 움직일때 씀
		yPos = 	0;
		//- TShape.getRealBlockSize('y', curShape); //도형크기에 -값을 넣어줌으로써 시작위치를 Pan위로 조정
		
		if(isMoveAble(curShape, xPos, yPos))	//판에서 블록 생성위치에 다른 블록있는지 확인 false면 생성위치에 뭔가 있다는것
			return true;
		else
			return false;
	}
	
	public void initialPan() { //판 초기화
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
	
	public void searchAndPushLine() {		//채워진 줄이 있다면 그 위에 블록들 한칸씩 밑으로 당김
		int IsLineFull = 0; //라인이 꽉찼는지 확인하는 변수
		
		for(int y = 0; y < Pan.length; y++) {
			IsLineFull = 0;
			
			for(int x = 0; x < Pan[0].length; x++) {
				
				if(Pan[y][x] != 0)
					IsLineFull++;
			}
			
			if(IsLineFull == Pan[0].length) {
				
				for(int PushY = y; PushY > 0; PushY--) {		//PushY는 꽉찬 한줄 찾은 y의 인덱스 
					for(int x = 0; x < Pan[0].length; x++) {
						Pan[PushY][x] = Pan[PushY - 1][x];
					}
				}
				if(curScore < MaxScore)
					curScore += 50;
			}
		}
	
	}
	
	public void storeArray() {		//현재 내려오는 블록 굳히기
		for(int y = 0; y < TShape.getRealBlockSize('y', curShape); y++) {
			for(int x = 0; x < TShape.getRealBlockSize('x', curShape); x++) {
				if(Pan[y + yPos][x + xPos] == 0)		//0일때만 배열 채우기
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
	
	public boolean movePos(int xPosAdd, int yPosAdd) { //Pos에 더할 움직일 값받기
		if (isMoveAble(curShape, xPos + xPosAdd, yPos + yPosAdd)) {		//움직일수 있는지 확인절차
			xPos += xPosAdd; yPos += yPosAdd;
			return true;
		}
		else
			return false;
	}
	
	public boolean isMoveAble(int [][] target, int xPrePos, int yPrePos) {	//PrePos은 이동예상지점		target은 현재 도형
		int sizeY = TShape.getRealBlockSize('Y',target);					//예상지점에서 target도형이 들어갈 수 없으면 false 있으면 true 반환
		int sizeX = TShape.getRealBlockSize('X',target);
		
		if((yPrePos + sizeY) > Pan.length || (xPrePos + sizeX) > Pan[0].length)		//y나 x포스에서 움직일양 더하고 도형size 더한게
			return false;																				//전체판의 크기보다 작아야 통과!
		
		if(xPrePos < 0)		//움직인 뒤 위치가 양수여야 통과! y구현안하는 이유는 랜덤블록생성 메소드에서 Pan보다 위에서 생성하게 하려고
			return false;	
		
		//판에 이미 있는 블록이랑 	``	겹치지 않아야 통과!
		for(int y = yPrePos; y < (yPrePos + sizeY); y++) {
			
			for(int x = xPrePos; x < (xPrePos + sizeX); x++) {
				if(Pan[y][x] != 0 && target[y - yPrePos][x - xPrePos] != 0)		 {//판과 타겟이 같이 0이 아닐때 통과!
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
	
	//왼쪽 아니면 오른쪽 돌수있는지 확인
	public boolean isRotateAble(int [][] target, char direction) { //direction에서 R이면 오른쪽 L이면 왼쪽
		
		int sizeX =	TShape.getRealBlockSize('x', target);
		int sizeY =	TShape.getRealBlockSize('y', target);
		int bigsize = sizeX > sizeY ?
				sizeX : sizeY; //size는 도형의 실제크기. 실제크기 가로세로중에 큰놈 bigsize에 넣음
					
		for(int y=0; y < sizeX; y++) {		//도형크기만큼만 돌고 크기만큼만 판에서 확인한다.
			for(int x=0; x < sizeY; x++) {			//가로 세로가 바뀌니까 sizeXY를 바꾼다.
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
					switch(direction) { 		//엘과 알이 아니면 죽음을
						case 'r':
						case 'R':
							if(target[bigsize-1-x][y] != 0)	//또한 판과 타겟이 같이 0이 아닌경우에 돌수 없음을 반환
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
	
	
	
	public void positioningCurShape() {		//도형이 바짝 안붙어 있을시 0,0에 바짝 붙인다.
		int CheckZero;
		int LineZeroCount = 0; //라인이 꽉찼는지 확인하는 변수
		
		//가로 빈공간 잡기
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
		//세로 빈공간 잡기
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
	
	
	
	public void testConsoleView() {		//콘솔창에 현재 배열 표기
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
		System.out.println("테스트 뷰 :");
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
		System.out.println("실제 판 :");
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
	테트리스 사용할 도형 클래스
	회전동작규정 메소드 포함
	모양은 번호 순서대로 I, J, L, S, Z, O, T 가 있음
				   0  1  2  3  4  5  6
	
	7개의 4X4 도형 배열화 해서 Shapes라는 배열에 넣어놈


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
	
	public int getRealBlockSize(char xORy, int[][] target) { //블록의 실제 사이즈 구하는 함수
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
		switch(xORy) {	//x일때 xMax y일때 yMax 반환하고 어떤것도 아니면 -1 반환
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
	
	public int[][] rotateRight(int[][] target) { //오른쪽으로 회전
		int sizeX =	getRealBlockSize('x', target);
		int sizeY =	getRealBlockSize('y', target);
		int bigsize = sizeX > sizeY ?
				sizeX : sizeY; //size는 도형의 실제크기. 실제크기 가로세로중에 큰놈 bigsize에 넣음
		int[][] result = new int[target.length][target[0].length];
		
		for(int y=0; y < target.length; y++) {
			for(int x=0; x < target[0].length; x++) {
				if(y >= bigsize || x >= bigsize) {
					result[y][x] = 0;
				}
				else {
					result[y][x] = target[bigsize-1-x][y]; //실제크기만 회전해서 배열에 저장.
				}
			}
		}
		return result;
		
	}
	public int[][] rotateLeft(int[][] target) { //왼쪽으로 회전
		int sizeX =	getRealBlockSize('x', target);
		int sizeY =	getRealBlockSize('y', target);
		int bigsize = sizeX > sizeY ?
				sizeX : sizeY; //size는 도형의 실제크기. 실제크기 가로세로중에 큰놈 bigsize에 넣음
		int[][] result = new int[target.length][target[0].length];
		
		for(int y=0; y < target.length; y++) {
			for(int x=0; x < target[0].length; x++) {
				if(y >= bigsize || x >= bigsize) {	//실제 크기에서 벗어난 곳은 0으로 초기화
					result[y][x] = 0;
				}
				else {
					result[y][x] = target[x][bigsize-1-y]; //실제크기만 회전해서 배열에 저장.
				}
			}
		}
		return result;
		
	}
	public int[][] getShape(int i) { //도형내놔
		return Shapes[i];
	}
}

/*
class PalseHandler extends Thread {
	//편하게 하기위해 자주쓸거 변수에 담아서 작성
	Tetris_Model TModel;
	int[][] TM_curShape;
	int[][] TM_Pan;
	Tetris_Shape TM_TShape;
	
	public void run() {
		while(true) {
			testConsoleView();
			if( !TModel.movePos(0, 1) ) { //블록 내려오다가 막히면 새로운 랜덤블록 생성
				TModel.storeArray();
				if( !TModel.randomBlockGen() ) { //블록 생성도 막히면 게임오버.
					System.out.println("게임 오버");
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
	
	public void testConsoleView() {		//콘솔창에 현재 배열 표기
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
		System.out.println("테스트 뷰 :");
		for(int y=0; y < testView.length; y++) {
			for(int x=0; x < testView[0].length; x++) {
				System.out.print(" " + testView[y][x]);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("실제 판 :");
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