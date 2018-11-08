import java.awt.Point;
import java.util.ArrayList;

public class Tree {
	
	ArrayList<Point> winSpot = new ArrayList<Point>(2);
	
	public void simulate(short[][] map, Point current1, Point current2)
	{
		short my_stone = 0;
		short others_stone = 0;
		short[][] pan = new short[19][19];
		Point move1 = new Point(0,0);
		Point move2 = new Point(0,0);
		long maxEval = -99999999;
		
		for(int i=0; i<19; i++)
			for(int j=0; j<19; j++)
				pan[i][j] = map[i][j];
		
		winSpot.clear();
		
		for(int y = 0; y < 19; y++)
			for(int x = 0; x < 19; x++)
			{
				//individual 7*7 search
				outerloop:
				for(int y1 =y; y1 < 19; y1++)
					for(int x1 = x+1; x1 < 19; x1++)
					{
						if(pan[x][y]!=-1)
							break outerloop;
						if(pan[x1][y1]!=-1)
							continue;
						pan[x][y] = my_stone;
						pan[x1][y1] = my_stone;
						//evaluation
						long eval = winning_point(pan);
						pan[x][y] = -1;
						pan[x1][y1] = -1;
						if(eval > maxEval)
						{
							maxEval = eval;
							move1 = new Point(x,y);
							move2 = new Point(x1,y1);
						}
					}
			}
		
		
//		if(start)
//		{
//			//initial 7*7 search
//			for(int y = 6; y <= 12; y++)
//				for(int x = 6; x <= 12; x++)
//					//individual 7*7 search
//					for(int y1 = y; y1 <= 12; y1++)
//						for(int x1 = x+1; x1 <= 12; x1++)
//						{
//							if(pan[x1][y1]!=-1 || pan[x][y]!=-1)
//								continue;
//							pan[x][y] = others_stone;
//							pan[x1][y1] = others_stone;
//							//evaluation
//							long eval = winning_point(pan);
//							pan[x][y] = -1;
//							pan[x1][y1] = -1;
//							if(eval > maxEval)
//							{
//								maxEval = eval;
//								move1 = new Point(x,y);
//								move2 = new Point(x1,y1);
//							}
//						}
//		}
//		else
//		{
//			//initial 7*7 search
//			//for current1
//			for(int y = current1.y - 3; y < current1.y + 3; y++)
//				for(int x = current1.x - 3; x < current1.x + 3; x++)
//				{
//					//individual 7*7 search
//					for(int y1 =y; y1 < current1.y + 3; y1++)
//						for(int x1 = x+1; x1 < current1.x + 3; x1++)
//						{
//							if(y1+3>18 || x1+3>18 || y<0 || x<0)
//								break;
//							if(pan[x][y]!=-1)
//								break;
//							if(pan[x1][y1]!=-1)
//								continue;
//							pan[x][y] = my_stone;
//							pan[x1][y1] = my_stone;
//							//evaluation
//							long eval = winning_point(pan);
//							pan[x][y] = -1;
//							pan[x1][y1] = -1;
//							if(eval > maxEval)
//							{
//								maxEval = eval;
//								move1 = new Point(x,y);
//								move2 = new Point(x1,y1);
//							}
//						}
//				}
//			//for current2
//			for(int y = current2.y - 3; y < current2.y + 3; y++)
//				for(int x = current2.x - 3; x < current2.x + 3; x++)
//				{
//					//individual 7*7 search
//					for(int y1 =y; y1 < current2.y + 3; y1++)
//						for(int x1 = x+1; x1 < current2.x + 3; x1++)
//						{
//							if(y1+3>18 || x1+3>18 || y<0 || x<0)
//								break;
//							if(pan[x][y]!=-1)
//								break;
//							if(pan[x1][y1]!=-1)
//								continue;
//							pan[x][y] = my_stone;
//							pan[x1][y1] = my_stone;
//							//evaluation
//							long eval = winning_point(pan); 
//							pan[x][y] = -1;
//							pan[x1][y1] = -1;
//							if(eval > maxEval)
//							{
//								maxEval = eval;
//								move1 = new Point(x,y);
//								move2 = new Point(x1,y1);
//							}
//						}
//				}
//		}
		System.out.println("move1.x,y: "+move1.x+","+move1.y);
		System.out.println("move2.x,y: "+move2.x+","+move2.y);
		winSpot.add(move1);
		winSpot.add(move2);
	}
	
	private long winning_point (short[][] map)
	 {
		 long result = 0;
		 
		 for (int i = 0; i < 19; i ++) {
			 for (int j = 0; j < 19; j ++) {
				 if (map[i][j] != -1) {
					 // when AI's black
					 result += evaluate_score(map, new Point(i, j), true) / 1000;
					// result -= evaluate_score(map, new Point(i, j), false) / 1000;
				 }
			 }
		 }
		 
		 return result;
	 }

	public long evaluate_score (short[][] map,  Point point, boolean isBlack)
	{
		long result = 0;
		int stage = 0;
		
		int x = point.x;
		int y = point.y;
		
		long horizontal_score = 1;
		long vertical_score = 1;
		long diagonal_score1 = 1;
		long diagonal_score2 = 1;
		
		int i = 0;
		int j = 0;
		
		int a = 0;
		int b = 0;
		
		int c = 0;
		int d = 0;
		
		int my_stone, others_stone;
		
		if (isBlack) {
			my_stone = 1;
			others_stone = 0;
		}
		else {
			my_stone = 0;
			others_stone = 1;
		}
				
		for (stage = 0; stage <= 3; stage ++) {
		// stage 0: check horizontally
		// stage 1: check vertically
		// stage 2: check diagonally (left-up -> right-down)
		// stage 3: check diagonally (right-up -> left-down)
			
			// from the right
			outerloop:
			for (i = 5; i >= 1; i --)
			{
				if (stage == 0) {
					a = i;
					b = 0;
				}
				else if (stage == 1) {
					a = 0;
					b = i;
				}
				else if (stage == 2) {
					a = i;
					b = i;
				}
				else {
					a = i;
					b = -1 * i;
				}
				
				if ((x + a) > 18 || (x + a) < 0 || (y + b) > 18 || (y + b) < 0)
					continue;
				
				// if empty
				if (map[x + a][y + b] == -1) {
					
					// check there's any blocking stone
					for (j = 1; j < i; j ++) {
						
						if (stage == 0) {
							c = j;
							d = 0;
						}
						else if (stage == 1) {
							c = 0;
							d = j;
						}
						else if (stage == 2) {
							c = j;
							d = j;
						}
						else {
							c = j;
							d = -1 * j;
						}
						
						if (map[x + c][y + d] == others_stone)
							continue;
					}
					
					if (stage == 0)
						horizontal_score *= 2;
					else if (stage == 1)
						vertical_score *= 2;
					else if (stage == 2)
						diagonal_score1 *= 2;
					else
						diagonal_score2 *= 2;
				}
					
								
				// if my stone
				else if (map[x + a][y + b] == my_stone) {
						
					// check there's any blocking stone
					for (j = 1; j < i; j ++) {
						
						if (stage == 0) {
							c = j;
							d = 0;
						}
						else if (stage == 1) {
							c = 0;
							d = j;
						}
						else if (stage == 2) {
							c = j;
							d = j;
						}
						else {
							c = j;
							d = -1 * j;
						}
						
						if (map[x + c][y + d] == others_stone)
							break outerloop;
					}
					
					// check special cases (3->5) / if it's second turn
					if (Main.frame.board.stone_count % 2 == 0) {
						
						int countStone = 0;
						int countSpace = 0;
						
						for (int i1 = -5; i1 <= 5; i1 ++) {
							if (stage == 0) {
								c = i1;
								d = 0;
							}
							else if (stage == 1) {
								c = 0;
								d = i1;
							}
							else if (stage == 2) {
								c = i1;
								d = i1;
							}
							else {
								c = i1;
								d = -1 * i1;
							}
							
							if (x + c < 0 || x + c > 18 || y + d < 0 || y + d > 18)
								continue;
							
							int current = map[x + c][y + d];
							
							if (current == -1)
								countSpace ++;
							else if (current == my_stone)
								countStone ++;
						}
						
						//System.out.println("countStone: " + countStone);
						
						if (countStone == 4) {
							System.out.println("check2");
							
							if (stage == 0)
								horizontal_score *= 4;
							else if (stage == 1)
								vertical_score *= 4;
							else if (stage == 2)
								diagonal_score1 *= 4;
							else
								diagonal_score2 *= 4;
							
							break outerloop;
						}
					}
						
					// if there's none
					if (j == i || i == 1) {
						if (stage == 0)
							horizontal_score *= Math.pow(2, (13 - i));
						else if (stage == 1)
							vertical_score *= Math.pow(2, (13 - i));
						else if (stage == 2)
							diagonal_score1 *= Math.pow(2, (13 - i));
						else
							diagonal_score2 *= Math.pow(2, (13 - i));
					}
				}
					
				// if other's stone
				else if (map[x + a][y + b] == others_stone) {
					break;
				}
			}
			
			// from the left
			outerloop:
			for (i = -5; i <= -1; i ++)
			{
				if (stage == 0) {
					a = i;
					b = 0;
				}
				else if (stage == 1) {
					a = 0;
					b = i;
				}
				else if (stage == 2) {
					a = i;
					b = i;
				}
				else {
					a = i;
					b = -1 * i;
				}
				
				if ((x + a) > 18 || (x + a) < 0 || (y + b) > 18 || (y + b) < 0)
					continue;
				
				// if empty
				if (map[x + a][y + b] == -1) {
					
					// check there's any blocking stone
					for (j = 1; j < i; j ++) {
						
						if (stage == 0) {
							c = j;
							d = 0;
						}
						else if (stage == 1) {
							c = 0;
							d = j;
						}
						else if (stage == 2) {
							c = j;
							d = j;
						}
						else {
							c = j;
							d = -1 * j;
						}
						
						if (map[x + c][y + d] == others_stone)
							continue;
					}
					
					if (stage == 0)
						horizontal_score *= 2;
					else if (stage == 1)
						vertical_score *= 2;
					else if (stage == 2)
						diagonal_score1 *= 2;
					else
						diagonal_score2 *= 2;
				}
					
								
				// if my stone
				else if (map[x + a][y + b] == my_stone) {
						
					// check there's any blocking stone
					for (j = -1; j > i; j --) {
						
						if (stage == 0) {
							c = j;
							d = 0;
						}
						else if (stage == 1) {
							c = 0;
							d = j;
						}
						else if (stage == 2) {
							c = j;
							d = j;
						}
						else {
							c = j;
							d = -1 * j;
						}
						
						if (map[x + c][y + d] == others_stone)
							break outerloop;
					}
					
					// check special cases (3->5) / if it's second turn
					if (Main.frame.board.stone_count % 2 == 0) {
						
						int countStone = 0;
						int countSpace = 0;
						
						for (int i1 = -5; i1 <= 5; i1 ++) {
							if (stage == 0) {
								c = i1;
								d = 0;
							}
							else if (stage == 1) {
								c = 0;
								d = i1;
							}
							else if (stage == 2) {
								c = i1;
								d = i1;
							}
							else {
								c = i1;
								d = -1 * i1;
							}
							
							if (x + c < 0 || x + c > 18 || y + d < 0 || y + d > 18)
								continue;
							
							int current = map[x + c][y + d];
							
							if (current == -1)
								countSpace ++;
							else if (current == my_stone)
								countStone ++;
						}
						
						//System.out.println("countStone: " + countStone);
						
						if (countStone == 4) {
							System.out.println("check2");
							
							if (stage == 0)
								horizontal_score *= 4;
							else if (stage == 1)
								vertical_score *= 4;
							else if (stage == 2)
								diagonal_score1 *= 4;
							else
								diagonal_score2 *= 4;
							
							break outerloop;
						}
					}
						
					// if there's none
					if (j == i || i == -1) {
						if (stage == 0)
							horizontal_score *= Math.pow(2, (13 + i));
						else if (stage == 1)
							vertical_score *= Math.pow(2, (13 + i));
						else if (stage == 2)
							diagonal_score1 *= Math.pow(2, (13 + i));
						else
							diagonal_score2 *= Math.pow(2, (13 + i));
					}
				}
					
				// if other's stone
				else if (map[x + a][y + b] == others_stone) {
					break;
				}
			}
		
			//System.out.println("horizontal result: " + horizontal_score);
			//System.out.println("vertical result: " + vertical_score);
			//System.out.println("diagonal1 result: " + diagonal_score1);
			//System.out.println("diagonal2 result: " + diagonal_score2);
		}
		
		result = horizontal_score + vertical_score + diagonal_score1 + diagonal_score2;
		
		//System.out.println("result: " + result);
		
		return result;
	}
	
	int stage = 0;
	public Point simulator(short[][] map, Point current1, Point current2)
	{
		if(++stage==3)
		{
			stage=0;
			return null;
		}
		simulate(map,current1,current2);
		simulator(map, winSpot.get(0), winSpot.get(1));
		
		
		return null;
		
	}

}