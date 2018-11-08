import java.awt.Point;

public class MinMaxTree {
	static boolean isBlack;
	static short[][] map;
	static int max_depth = 2;

	public static void optimize_move (short[][] in_map, boolean in_isBlack)
	{
		map = in_map;
		isBlack = in_isBlack;
		
		int current_depth = 1;
		
		System.out.println(find_node_score (map, 0));
	}
	
	// 한 노드에 대하여 자신의 스코어를 찾은 후, 리턴 
	private static long find_node_score (short[][] map, int depth)
	{
		
		// 가장 말단 depth이면, 자기 자신의 점수가 곧 자신의 스코어
		// presumption: max_depth is odd number
		if (depth == max_depth) {
			if (depth % 2 == 1)
				return winning_point(map, isBlack);
			else
				return winning_point(map, !isBlack);
		}
			
		// 아니라면, 자기 자식 점수들 중 가장 큰 점수가 곧 자신의 스코어
		short temp_map[][] = new short[19][19];
		long max = Long.MIN_VALUE;
		short stone;
		
		if (isBlack) {
			if (depth % 2 == 1)
				stone = 1;
			else
				stone = 0;
		}
		else {
			if (depth % 2 == 1)
				stone = 0;
			else
				stone = 1;
		}
		
		for (int x1 = 6; x1 <= 12; x1 ++) {
			for (int y1 = 6; y1 <= 12; y1 ++) {
				
				if (x1 < 0 || x1 > 18 || y1 < 0 || y1 > 18)
					continue;
				
				if(map[x1][y1] != -1)
					continue;
				
				for (int x2 = 6; x2 <= 12; x2 ++) {
					for (int y2 = 6; y2 <= 12; y2 ++) {
						
						if (x2 < 0 || x2 > 18 || y2 < 0 || y2 > 18)
							continue;
						
						if(map[x2][y2] != -1)
							continue;
							
						// deep copy the map
						for (int i = 0; i <= 18; i ++) {
							for (int j = 0; j <= 18; j ++) {
								temp_map[i][j] = map[i][j];
							}
						}
							
						temp_map[x1][y1] = stone;
						temp_map[x2][y2] = stone;
							
						if (find_node_score (temp_map, depth + 1) > max) {
							max = find_node_score (temp_map, depth + 1);
						}
					}
				}
			}
		}
		
		return max;
		
	}

	 	 
	private static long winning_point (short[][] map, boolean isBlack)
	 {
		 long result = 0;
		 
		 for (int i = 0; i < 19; i ++) {
			 for (int j = 0; j < 19; j ++) {
				 if (map[i][j] != -1) {
					 // when AI's black
					 result += evaluate_score(map, new Point(i, j), isBlack) / 1000;
					// result -= evaluate_score(map, new Point(i, j), false) / 1000;
				 }
			 }
		 }
		 
		 return result;
	 }

	public static long evaluate_score (short[][] map, Point point, boolean isBlack)
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
							
					// if there's none
					if (j == i || i == 1) {
						if (stage == 0)
							horizontal_score *= Math.pow(2, (10 - i));
						else if (stage == 1)
							vertical_score *= Math.pow(2, (10 - i));
						else if (stage == 2)
							diagonal_score1 *= Math.pow(2, (10 - i));
						else
							diagonal_score2 *= Math.pow(2, (10 - i));
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
							
					// if there's none
					if (j == i || i == -1) {
						if (stage == 0)
							horizontal_score *= Math.pow(2, (10 + i));
						else if (stage == 1)
							vertical_score *= Math.pow(2, (10 + i));
						else if (stage == 2)
							diagonal_score1 *= Math.pow(2, (10 + i));
						else
							diagonal_score2 *= Math.pow(2, (10 + i));
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
}