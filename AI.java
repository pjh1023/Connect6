import java.awt.Point;
import java.util.ArrayList;

public class AI {
	boolean isBlack = true;
	Tree simulation = new Tree();
	short marks[][] = new short[19][19];
	Point must;
	
	public AI(boolean isBlack)
	{
		this.isBlack = isBlack;
		
		for (int i = 0; i < 19; i ++) {
			for (int j = 0; j < 19; j ++) {
				marks[i][j] = 0;
			}
		}
	}
	
	public Point AI_stone(short[][] map, Point current1, Point current2)
	{
		Point result = new Point(0, 0);
		
		int i = 0;
		int j = 0;
		
		long max_value = 0;
		Point max_point = new Point(0, 0);
		
		long temp = 0;
		
		// initialize marks
		for (i = 0; i < 19; i ++) {
			for (j = 0; j < 19; j ++) {
				marks[i][j] = 0;
			}
		}
		
//		if(Main.frame.board.stone_count%2==0)
//			return simulation.winSpot.get(0);
//		else
//			return simulation.winSpot.get(1);
		
		
		// if it's first white stone
		if (current1 == null)
		{
			if (Main.frame.board.stone_count % 2 == 1)
				return new Point (8, 8);
			
			else
				return new Point (8, 10);
		}
		
		Point attack_critical_point = critical(map, false);
		
		if (attack_critical_point != null) {
			System.out.println("##########By attack critical");
			result = attack_critical_point;
			return result;
		}
		
		Point defense_critical_point = critical(map, true);
		
		if (defense_critical_point != null) {
			System.out.println("##########By defense critical");
			result = defense_critical_point;
			return result;
		}
		
		if(must!=null)
		{
			System.out.println("must");
			int x = must.x;
			int y = must.y;
			must = null;
			return new Point(x,y);
		}
		
		if(Main.frame.board.stone_count%2==1)
		{
			Point attack_danger_point = danger(map, false);
			
			if (attack_danger_point != null) {
				System.out.println("---------By attack danger");
				result = attack_danger_point;
				return result;
			}
		}
//		Point attack_danger_point = danger(map, false);
//		
//		if (attack_danger_point != null) {
//			System.out.println("---------By attack danger");
//			result = attack_danger_point;
//			return result;
//		}
		
		Point attack_caution_point = caution(map, false);
		
		if (attack_caution_point != null) {
			System.out.println("---------By attack caution");
			result = attack_caution_point;
			return result;
		}
		
		Point defense_danger_point = danger(map, true);
		
		if (defense_danger_point != null) {
			System.out.println("---------By defence danger");
			result = defense_danger_point;
			return result;
		}
		
		Point defense_caution_point = caution(map, true);
		
		if (defense_caution_point != null) {
			System.out.println("---------By defence caution");
			result = defense_caution_point;
			return result;
		}

		System.out.println("+++++++++By simulation");
		
//		return new Point(6,8);
		
//		simulation.simulate(map, current1, current2);
//		return simulation.winSpot.get(0);
		
		// °¡ÁßÄ¡
		if (current1 == null || current2 == null) {
			current1 = new Point(9, 9);
			current2 = new Point(9, 9);
		}
		
		int x_min = 18;
		int y_min = 18;
		int x_max = 0;
		int y_max = 0;
		
		for (i = 0; i <= 18; i ++) {							
			for (j = 0; j <= 18; j ++) {	
				if (map[i][j] == -1)
					continue;
				
				else {
					if (i > x_max)
						x_max = i;
					if (i < x_min)
						x_min = i;
					if (j > y_max)
						y_max = j;
					if (j < y_min)
						y_min = j;
				}
			}
		}
		
		
		for (i = 0; i <= 18; i ++) {							
			for (j = 0; j <= 18; j ++) {				
				// check if there's another stone on the spot
				if (map[i][j] != -1)
					continue;
				
				temp = evaluate_score (map, new Point(i, j));
				
				if (temp >= max_value) {
					if (i > x_max + 2 || i < x_min - 2 || j > y_max + 2 || j < y_min - 2)
						continue;
					
					max_value = temp;
					max_point = new Point(i, j);
				}
			}
		}
		
		
		result = max_point;
		
		return result;
	}
	
	public long evaluate_score (short[][] map,  Point point)
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
					
					// check there's any blocking stone outside
					for (j = 5; j > i; j --) {
						
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
						
						if ((x + c) > 18 || (x + c) < 0 || (y + d) > 18 || (y + d) < 0)
							continue;
						
						if (map[x + c][y + d] == others_stone)
							continue outerloop;
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
							horizontal_score *= Math.pow(2, (13 - 2 * i));
						else if (stage == 1)
							vertical_score *= Math.pow(2, (13 - 2 * i));
						else if (stage == 2)
							diagonal_score1 *= Math.pow(2, (13 - 2 * i));
						else
							diagonal_score2 *= Math.pow(2, (13 - 2 * i));
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
					
					// check if there's any blocking stone
					for (j = -5; j < i; j ++) {
						
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
						
						if ((x + c) > 18 || (x + c) < 0 || (y + d) > 18 || (y + d) < 0)
							continue;
						
						if (map[x + c][y + d] == others_stone)
							continue outerloop;
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
							horizontal_score *= Math.pow(2, (13 + 2 * i));
						else if (stage == 1)
							vertical_score *= Math.pow(2, (13 + 2 * i));
						else if (stage == 2)
							diagonal_score1 *= Math.pow(2, (13 + 2 * i));
						else
							diagonal_score2 *= Math.pow(2, (13 + 2 * i));
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
		
		if ((marks[x][y] == 1 || marks[x][y] == 2) && Main.frame.board.stone_count % 2 == 0) {
			System.out.println("checkkkkk");
			
			long max = horizontal_score;
			
			if (vertical_score > max)
				max = vertical_score;
			
			if (diagonal_score1 > max)
				max = diagonal_score1;
			
			if (diagonal_score2 > max)
				max = diagonal_score2;
			
			result -= max;
		}
		
		result = horizontal_score + vertical_score + diagonal_score1 + diagonal_score2;
		
		//System.out.println("result: " + result);
		
		return result;
	}
	
	private Point critical (short[][] map, boolean defenseMode)
	{
		int countE = 0;
		int countM = 0;		
		int window = -1;
		int current = 0;
		
		Point result = null;
		
		int my_stone;
		int others_stone;
		
		// if defense mode
		if (defenseMode) {
		     if (isBlack) { 
		    	 	my_stone = 1;
			    others_stone = 0;
			 }
			 else {
			    my_stone = 0;
			    others_stone = 1;
			 }
		}
		
		// if attack mode
		else {
		     if (isBlack) {
		    	 	my_stone = 0;
			    others_stone = 1;
			 }
			 else {
			    my_stone = 1;
			    others_stone = 0;
			 }
		}
	     
	     for (int start_y = 0; start_y < 19; start_y ++) {
	    	 	for (int start_x = 0; start_x < 19; start_x ++) {
	    	 		
	    	 		// down
	    	 		for (int i = 0; i <= 5; i ++)
	    	 		{
	    	 			if (start_y + i > 18) {
	    	 					countM ++;
	    	 					break;
	    	 			}
	    	 		
	    	 			current = map[start_x][start_y + i];
	    	 			
	    	 			if (current == my_stone)
	    	 				countM ++;
	    	 			else if (current == others_stone)
	    	 				countE ++;
	    	 			else
	    	 				window = i;
	    	 		}
	    	 		
	    	 		if (countE >= 4 && countM == 0) {
	    	 			
	    	 			if (!defenseMode && Main.frame.board.stone_count % 2 == 0 && countE == 4) {
	    	 					marks[start_x][start_y + window] = 1;
	    	 				
	    	 				if (start_y + window + 1 <= 18) {
		    	 				if (map[start_x][start_y + window + 1] == -1)
		    	 					marks[start_x][start_y + window + 1] = 2;
	    	 				}
	    	 			}
	    	 			else {
	    	 				System.out.println("check1-1");
	    	 				
	    	 				// evaluate score between two possible spots
	    	 				if (map[start_x][start_y + window - 1] == -1 && map[start_x][start_y + 5] != others_stone
	    	 						&& start_y + 5 <= 18) {
	    	 					System.out.println("check1-2");
	    	 					long score1 = evaluate_score(map, new Point(start_x, start_y + window));
	    	 					long score2 = evaluate_score(map, new Point(start_x, start_y + window - 1));
	    	 					
	    	 					if (score1 >= score2 && countE != 5) {
	    	 						result = new Point(start_x, start_y + window);
	    	 						return result;	 
	    	 					}
	    	 					else {
		    	 						result = new Point(start_x, start_y + window - 1);
		    	 						return result;	 
	    	 						
	    	 					}
	    	 				}
	    	 				
					    	result = new Point(start_x, start_y + window);
					    	return result;	 

	    	 			}
	    	 		}
	    	 		
	    			countE = 0;
	    			countM = 0;		
	    			window = -1;
	    			current = 0;
	    			
	    	 		// right
	    	 		for (int i = 0; i <= 5; i ++)
	    	 		{
	    	 			if (start_x + i > 18) {
	    	 					countM ++;
	    	 					break;

	    	 			}
	    	 				
	    	 			
	    	 			current = map[start_x + i][start_y];
	    	 			
	    	 			if (current == my_stone)
	    	 				countM ++;
	    	 			else if (current == others_stone)
	    	 				countE ++;
	    	 			else
	    	 				window = i;
	    	 		}
	    	 		
	    	 		if (countE >= 4 && countM == 0) {
	    	 			if (!defenseMode && Main.frame.board.stone_count % 2 == 0 && countE == 4) {
	    	 					marks[start_x + window][start_y] = 1;
	    	 				
	    	 				if (start_x + window + 1 <= 18) {
		    	 				if (map[start_x + window + 1][start_y] == -1)
		    	 					marks[start_x + window + 1][start_y] = 2;
	    	 				}
	    	 			}
	    	 			else {
	    	 				System.out.println("check2-1");

	    	 				// evaluate score between two possible spots
	    	 				if (map[start_x + window - 1][start_y] == -1 && map[start_x + 5][start_y] != others_stone
	    	 						&& start_x + 5 <= 18) {
		    	 				System.out.println("check2-2");

	    	 					long score1 = evaluate_score(map, new Point(start_x + window, start_y));
	    	 					long score2 = evaluate_score(map, new Point(start_x + window - 1, start_y));
	    	 					
	    	 					if (score1 >= score2  && countE != 5) {
	    	 						result = new Point(start_x + window, start_y);
	    	 						return result;	 
	    	 					}
	    	 					else {
	    	 						if (map[start_x + window - 1][start_y] == -1) {
		    	 						result = new Point(start_x + window - 1, start_y);
		    	 						return result;	
	    	 						}
	    	 					}
	    	 				}
	    	 			
	    	 					result = new Point(start_x + window, start_y);
	    	 					return result;
	    	 				
	    	 			}
	    	 		}
	    	 		
	    			countE = 0;
	    			countM = 0;		
	    			window = -1;
	    			current = 0;
	    			
	    			// down-right
	    	 		for (int i = 0; i <= 5; i ++)
	    	 		{
	    	 			if (start_x + i > 18 || start_y + i > 18) {
	    	 					countM ++;
	    	 					break;
	    	 	
	    	 			}
	    	 			
	    	 			current = map[start_x + i][start_y + i];
	    	 			
	    	 			if (current == my_stone)
	    	 				countM ++;
	    	 			else if (current == others_stone)
	    	 				countE ++;
	    	 			else
	    	 				window = i;
	    	 		}
	    	 		
	    	 		if (countE >= 4 && countM == 0) {
	    	 			if (!defenseMode && Main.frame.board.stone_count % 2 == 0 && countE == 4) {
	    	 					marks[start_x + window][start_y + window] = 1;
	    	 				
	    	 				if (start_y + window + 1 <= 18 && start_x + window + 1 <= 18) {
		    	 				if (map[start_x + window + 1][start_y + window + 1] == -1)
		    	 					marks[start_x + window + 1][start_y + window + 1] = 2;
	    	 				}
	    	 				
	    	 			}
	    	 			else {
	    	 				System.out.println("check3-1");

	    	 				// evaluate score between two possible spots
	    	 				if (map[start_x + window - 1][start_y + window - 1] == -1 && map[start_x + 5][start_y + 5] != others_stone
	    	 						&& start_x + 5 <= 18 && start_y + 5 <= 18) {
	    	 					long score1 = evaluate_score(map, new Point(start_x + window, start_y + window));
	    	 					long score2 = evaluate_score(map, new Point(start_x + window - 1, start_y + window - 1));
	    	 					
	    	 					if (score1 >= score2  && countE != 5) {
	    	 						result = new Point(start_x + window, start_y + window);
	    	 						return result;	 
	    	 					}
	    	 					else {
	    	 						if (map[start_x + window - 1][start_y + window - 1] == -1) {
		    	 						result = new Point(start_x + window - 1, start_y + window - 1);
		    	 						return result;	 
	    	 						}
	    	 					}
	    	 				}
	    	 			
	    	 					result = new Point(start_x + window, start_y + window);
	    	 					return result;
	    	 				
	    	 			}
	    	 		}
	    	 		
	    			countE = 0;
	    			countM = 0;		
	    			window = -1;
	    			current = 0;
	    			
	    			// down-left
	    	 		for (int i = 0; i <= 5; i ++)
	    	 		{
	    	 			if (start_x - i < 0 || start_y + i > 18) {
	    	 					countM ++;
	    	 					break;
	    	 			
	    	 			}
	    	 			
	    	 			current = map[start_x - i][start_y + i];
	    	 			
	    	 			if (current == my_stone)
	    	 				countM ++;
	    	 			else if (current == others_stone)
	    	 				countE ++;
	    	 			else
	    	 				window = i;
	    	 		}
	    	 		
	    	 		if (countE >= 4 && countM == 0) {
	    	 			if (!defenseMode && Main.frame.board.stone_count % 2 == 0 && countE == 4) {
	    	 					marks[start_x - window][start_y + window] = 1;
	    	 				
	    	 				if (start_y + window + 1 <= 18 && start_x - window - 1 >= 0) {
	    	 					if (map[start_x - window - 1][start_y + window + 1] == -1)
	    	 						marks[start_x - window - 1][start_y + window + 1] = 2;
	    	 				}
	    	 			}
	    	 			else {
	    	 				System.out.println("check");

	    	 				// evaluate score between two possible spots
	    	 				if (map[start_x - window + 1][start_y + window - 1] == -1 && map[start_x - 5][start_y + 5] != others_stone
	    	 						&& start_x - 5 >= 0 && start_y + 5 <= 18) {
	    	 					long score1 = evaluate_score(map, new Point(start_x - window, start_y + window));
	    	 					long score2 = evaluate_score(map, new Point(start_x - window + 1, start_y + window - 1));
	    	 					
	    	 					if (score1 >= score2  && countE != 5) {
	    	 						result = new Point(start_x - window, start_y + window);
	    	 						return result;	 
	    	 					}
	    	 					else {
	    	 						if (map[start_x - window + 1][start_y + window - 1] == -1) {
		    	 						result = new Point(start_x - window + 1, start_y + window - 1);
		    	 						return result;	 
	    	 						}
	    	 					}
	    	 				}
	    	 				
				    	 		result = new Point(start_x - window, start_y + window);
				    	 		return result;
	    	 			
	    	 			}
	    	 		}
	    	 		
	    			countE = 0;
	    			countM = 0;		
	    			window = -1;
	    			current = 0;
	    	 	}
	     }
	     
	     return result;  	 
	}
	
	private Point caution (short[][] map, boolean defenseMode)
	{
		int countE = 0;
		int countM = 0;		
		int window = -1;
		int current = 0;
		short[][] cpymap = new short[19][19];
		for(int i=0;i<19;i++)
			for(int j=0;j<19;j++)
				cpymap[i][j] = map[i][j];
		
		short[][] Dcpymap = new short[19][19];
		for(int i=0;i<19;i++)
			for(int j=0;j<19;j++)
				Dcpymap[i][j] = map[i][j];

		short[][] Rcpymap = new short[19][19];
		for(int i=0;i<19;i++)
			for(int j=0;j<19;j++)
				Rcpymap[i][j] = map[i][j];

		short[][] DRcpymap = new short[19][19];
		for(int i=0;i<19;i++)
			for(int j=0;j<19;j++)
				DRcpymap[i][j] = map[i][j];

		short[][] DLcpymap = new short[19][19];
		for(int i=0;i<19;i++)
			for(int j=0;j<19;j++)
				DLcpymap[i][j] = map[i][j];

		
		Point result = null;

		short my_stone; 
		short others_stone;
		
		// if defense mode
		if (defenseMode) {
		     if (isBlack) {
		    	 	my_stone = 1;
			    others_stone = 0;
			 }
			 else {
			    my_stone = 0;
			    others_stone = 1;
			 }
		}
		
		// if attack mode
		else {
		     if (isBlack) {
		    	 	my_stone = 0;
			    others_stone = 1;
			 }
			 else {
			    my_stone = 1;
			    others_stone = 0;
			 }
		}
		
		for (int start_y = 0; start_y < 18; start_y ++) {
			for (int start_x = 0; start_x < 18; start_x ++) {				
	    	 	// down
				for (int i = 0; i <= 5; i++)
	    	 		{
	    	 			if (start_y + i > 18 || Dcpymap[start_x][start_y + i] == 2)
	    	 				break;
	    	 			
	    	 			current = map[start_x][start_y + i];
	    	 			
	    	 			if (current == my_stone)
	    	 				countM ++;
	    	 			else if (current == others_stone)
	    	 				countE ++;
	    	 			else
	    	 				window = i;
	    	 		}
	    	 		
	    	 	if (countE == 3 && countM == 0) {
	    	 		if(start_y + 5 <= 18)
	    	 		{
	    	 			if(Dcpymap[start_x][start_y+3]==others_stone && Dcpymap[start_x][start_y+4]==others_stone && Dcpymap[start_x][start_y+5]==others_stone)
	    	 			{
	    	 				for(int i = 3; i <= 5; i++)
	    	    	 		{
	    	    	 			if(Dcpymap[start_x][start_y+i]==others_stone)
	    	    	 				Dcpymap[start_x][start_y+i] = 2;
	    	    	 		}
	        	 			cpymap[start_x][start_y + window] = 3;
	    	 			}
	    	 			else if(Dcpymap[start_x][start_y+2]==others_stone && Dcpymap[start_x][start_y+4]==others_stone && Dcpymap[start_x][start_y+5]==others_stone)
	    	 			{
	    	 				for(int i = 2; i <= 5; i++)
	    	    	 		{
	    	    	 			if(Dcpymap[start_x][start_y+i]==others_stone)
	    	    	 				Dcpymap[start_x][start_y+i] = 2;
	    	    	 		}
	        	 			cpymap[start_x][start_y + window] = 3;
	    	 			}
	    	 			else if(Dcpymap[start_x][start_y+2]==others_stone && Dcpymap[start_x][start_y+3]==others_stone && Dcpymap[start_x][start_y+5]==others_stone)
	    	 			{
	    	 				for(int i = 2; i <= 5; i++)
	    	    	 		{
	    	    	 			if(Dcpymap[start_x][start_y+i]==others_stone)
	    	    	 				Dcpymap[start_x][start_y+i] = 2;
	    	    	 		}
	        	 			cpymap[start_x][start_y + window] = 3;
	    	 			}
	    	 			else if(Dcpymap[start_x][start_y+1]==others_stone && Dcpymap[start_x][start_y+3]==others_stone && Dcpymap[start_x][start_y+5]==others_stone)
	    	 			{
	    	 				for(int i = 1; i <= 5; i++)
	    	    	 		{
	    	    	 			if(Dcpymap[start_x][start_y+i]==others_stone)
	    	    	 				Dcpymap[start_x][start_y+i] = 2;
	    	    	 		}
	        	 			cpymap[start_x][start_y + window] = 3;
	    	 			}
	    	 		}
	    	 	}
	    	 	
	    		countE = 0;
	    		countM = 0;		
	    		window = -1;
	    			
	    	 	// right
	    	 	for (int i = 0; i <= 5; i++)
	    	 	{
	    	 		if (start_x + i > 18 || Rcpymap[start_x + i][start_y] == 2)
	    	 			break;
	    	 		
	    	 		current = map[start_x + i][start_y];
	    				
	    	 		if (current == my_stone)
	   	 				countM ++;
	   	 			else if (current == others_stone)
	   	 				countE ++;
	   	 			else
    	 				window = i;
	   	 		}
	    	 		
	   	 		if (countE == 3 && countM == 0) {
		   	 		if(start_y + 5 <= 18 && start_x + 5 <= 18)
	    	 		{
	    	 			if(Rcpymap[start_x+3][start_y]==others_stone && Rcpymap[start_x+4][start_y]==others_stone && Rcpymap[start_x+5][start_y]==others_stone)
	    	 			{
	    	 				for(int i = 3; i <= 5; i++)
	    	    	 		{
	    	    	 			if(Rcpymap[start_x+i][start_y]==others_stone)
	    	    	 				Rcpymap[start_x+i][start_y] = 2;
	    	    	 		}
	        	 			cpymap[start_x + window][start_y] = 3;
	    	 			}
	    	 			else if(Rcpymap[start_x+2][start_y]==others_stone && Rcpymap[start_x+4][start_y]==others_stone && Rcpymap[start_x+5][start_y]==others_stone)
	    	 			{
	    	 				for(int i = 2; i <= 5; i++)
	    	    	 		{
	    	    	 			if(Rcpymap[start_x+i][start_y]==others_stone)
	    	    	 				Rcpymap[start_x+i][start_y] = 2;
	    	    	 		}
	        	 			cpymap[start_x + window][start_y] = 3;
	    	 			}
	    	 			else if(Rcpymap[start_x+2][start_y]==others_stone && Rcpymap[start_x+3][start_y]==others_stone && Rcpymap[start_x+5][start_y]==others_stone)
	    	 			{
	    	 				for(int i = 2; i <= 5; i++)
	    	    	 		{
	    	    	 			if(Rcpymap[start_x+i][start_y]==others_stone)
	    	    	 				Rcpymap[start_x+i][start_y] = 2;
	    	    	 		}
	        	 			cpymap[start_x + window][start_y] = 3;
	    	 			}
	    	 			else if(Rcpymap[start_x+1][start_y]==others_stone && Rcpymap[start_x+3][start_y]==others_stone && Rcpymap[start_x+5][start_y]==others_stone)
	    	 			{
	    	 				for(int i = 1; i <= 5; i++)
	    	    	 		{
	    	    	 			if(Rcpymap[start_x+i][start_y]==others_stone)
	    	    	 				Rcpymap[start_x+i][start_y] = 2;
	    	    	 		}
	        	 			cpymap[start_x + window][start_y] = 3;
	    	 			}
	    	 		}
	   	 		}
    	 		
    			countE = 0;
    			countM = 0;		
    			window = -1;
    			
    			// down-right
    	 		for (int i = 0; i <= 5; i++)
    	 		{
    	 			if (start_x + i > 18 || start_y + i > 18 || DRcpymap[start_x + i][start_y + i] == 2)
    	 				break;
    	 			
    	 			current = map[start_x + i][start_y + i];
    	 			
    	 			if (current == my_stone)
    	 				countM ++;
    	 			else if (current == others_stone)
    	 				countE ++;
    	 			else
    	 				window = i;
    	 		}
    	 		
    	 		if (countE == 3 && countM == 0) {
    	 			if(start_y + 5 <= 18 && start_x + 5 <= 18)
	    	 		{
	    	 			if(DRcpymap[start_x+3][start_y+3]==others_stone && DRcpymap[start_x+4][start_y+4]==others_stone && DRcpymap[start_x+5][start_y+5]==others_stone)
	    	 			{
	    	 				for(int i = 3; i <= 5; i++)
	    	    	 		{
	    	    	 			if(Rcpymap[start_x+i][start_y+i]==others_stone)
	    	    	 				Rcpymap[start_x+i][start_y+i] = 2;
	    	    	 		}
	        	 			cpymap[start_x + window][start_y + window] = 3;
	    	 			}
	    	 			else if(DRcpymap[start_x+2][start_y+2]==others_stone && DRcpymap[start_x+4][start_y+4]==others_stone && DRcpymap[start_x+5][start_y+5]==others_stone)
	    	 			{
	    	 				for(int i = 2; i <= 5; i++)
	    	    	 		{
	    	    	 			if(DRcpymap[start_x+i][start_y+i]==others_stone)
	    	    	 				DRcpymap[start_x+i][start_y+i] = 2;
	    	    	 		}
	        	 			cpymap[start_x + window][start_y + window] = 3;
	    	 			}
	    	 			else if(DRcpymap[start_x+2][start_y+2]==others_stone && DRcpymap[start_x+3][start_y+3]==others_stone && DRcpymap[start_x+5][start_y+5]==others_stone)
	    	 			{
	    	 				for(int i = 2; i <= 5; i++)
	    	    	 		{
	    	    	 			if(DRcpymap[start_x+i][start_y+i]==others_stone)
	    	    	 				DRcpymap[start_x+i][start_y+i] = 2;
	    	    	 		}
	        	 			cpymap[start_x + window][start_y + window] = 3;
	    	 			}
	    	 			else if(DRcpymap[start_x+1][start_y+1]==others_stone && DRcpymap[start_x+3][start_y+3]==others_stone && DRcpymap[start_x+5][start_y+5]==others_stone)
	    	 			{
	    	 				for(int i = 1; i <= 5; i++)
	    	    	 		{
	    	    	 			if(DRcpymap[start_x+i][start_y+i]==others_stone)
	    	    	 				DRcpymap[start_x+i][start_y+i] = 2;
	    	    	 		}
	        	 			cpymap[start_x + window][start_y + window] = 3;
	    	 			}
	    	 		}
    	 		}
    	 		
    			countE = 0;
    			countM = 0;		
    			window = -1;
    			
    			// down-left
    	 		for (int i = 0; i <= 5; i++)
    	 		{
    	 			if (start_x - i < 0 || start_y + i > 18 || DLcpymap[start_x - i][start_y + i] == 2)
    	 				break;
    	 			
    	 			current = map[start_x - i][start_y + i];
    	 			
    	 			if (current == my_stone)
    	 				countM ++;
    	 			else if (current == others_stone)
    	 				countE ++;
    	 			else
    	 				window = i;
    	 		}
    	 		
    	 		if (countE == 3 && countM == 0) {
    	 			if(start_y+5<=18 && start_x-5>=0)
	    	 		{
	    	 			if(DLcpymap[start_x-3][start_y+3]==others_stone && DLcpymap[start_x-4][start_y+4]==others_stone && DLcpymap[start_x-5][start_y+5]==others_stone)
	    	 			{
	    	 				for(int i = 3; i <= 5; i++)
	    	    	 		{
	    	    	 			if(DLcpymap[start_x-i][start_y+i]==others_stone)
	    	    	 				DLcpymap[start_x-i][start_y+i] = 2;
	    	    	 		}
	        	 			cpymap[start_x - window][start_y + window] = 3;
	    	 			}
	    	 			else if(DLcpymap[start_x-2][start_y+2]==others_stone && DLcpymap[start_x-4][start_y+4]==others_stone && DLcpymap[start_x-5][start_y+5]==others_stone)
	    	 			{
	    	 				for(int i = 2; i <= 5; i++)
	    	    	 		{
	    	    	 			if(DLcpymap[start_x-i][start_y+i]==others_stone)
	    	    	 				DLcpymap[start_x-i][start_y+i] = 2;
	    	    	 		}
	        	 			cpymap[start_x - window][start_y + window] = 3;
	    	 			}
	    	 			else if(DLcpymap[start_x-2][start_y+2]==others_stone && DLcpymap[start_x-3][start_y+3]==others_stone && DLcpymap[start_x-5][start_y+5]==others_stone)
	    	 			{
	    	 				for(int i = 2; i <= 5; i++)
	    	    	 		{
	    	    	 			if(DLcpymap[start_x-i][start_y+i]==others_stone)
	    	    	 				DLcpymap[start_x-i][start_y+i] = 2;
	    	    	 		}
	        	 			cpymap[start_x - window][start_y + window] = 3;
	    	 			}
	    	 			else if(DLcpymap[start_x-1][start_y+1]==others_stone && DLcpymap[start_x-3][start_y+3]==others_stone && DLcpymap[start_x-5][start_y+5]==others_stone)
	    	 			{
	    	 				for(int i = 1; i <= 5; i++)
	    	    	 		{
	    	    	 			if(DLcpymap[start_x-i][start_y+i]==others_stone)
	    	    	 				DLcpymap[start_x-i][start_y+i] = 2;
	    	    	 		}
	        	 			cpymap[start_x - window][start_y + window] = 3;
	    	 			}
	    	 		}
    	 		}
    	 	}
		}
		int tcount=0;
		for(int i=0;i<19;i++)
			for(int j=0;j<19;j++)
			{
				if(cpymap[i][j]==3) {
					
					if (Main.frame.board.stone_count % 2 == 0 && !defenseMode) {
						if (marks[i][j] == 1 || marks[i][j] == 2) {
							System.out.println("checkkkk");
							cpymap[i][j] = 0;
						}
						else 
							tcount++;
					}
					
					else
						tcount++;
				}
			}
		System.out.println("three: " + tcount);
				
		if(!defenseMode)
		{
			if(tcount>=2)
				for(int j=0;j<19;j++)
					for(int i=0;i<19;i++)
					{
						if(cpymap[i][j]==3)
							return new Point(i,j);
					}
		}
		else
		{
			if(tcount>=1)
				for(int j=0;j<19;j++)
					for(int i=0;i<19;i++)
					{
						if(cpymap[i][j]==3)
							return new Point(i,j);
					}
		}
		return result;  	 
	}

	private Point danger (short[][] map, boolean defenseMode)
	{
		int countE = 0;
		int countM = 0;
		int countE2 = 0;
		int countM2 = 0;
		int countE3 = 0;
		int window = -1;
		int current = 0;
		
		Point result = null;

		short my_stone;
		short others_stone;
		
		// if defense mode
		if (defenseMode) {
		     if (isBlack) {
		    	 	my_stone = 1;
			    others_stone = 0;
			 }
			 else {
			    my_stone = 0;
			    others_stone = 1;
			 }
		}
		
		// if attack mode
		else {
		     if (isBlack) {
		    	 	my_stone = 0;
			    others_stone = 1;
			 }
			 else {
			    my_stone = 1;
			    others_stone = 0;
			 }
		}
		
		boolean caseEnd = false;
		boolean hasOne = false;
		
		for (int start_y = 0; start_y < 19; start_y ++) {
			for (int start_x = 0; start_x < 19; start_x ++) {				
	    	 	//##### down #####//
				for (int i = 0; i <= 5; i++)
	    	 		{
	    	 			if (start_y + i > 18)
	    	 				break;
	    	 			
	    	 			current = map[start_x][start_y + i];
	    	 			
	    	 			if (current == my_stone)
	    	 			{
	    	 				countM ++;
	    	 				if(countM == 1 && (i == 0 || i == 5))
	    	 					caseEnd = true;
	    	 			}
	    	 			else if (current == others_stone)
	    	 				countE ++;
	    	 			else
	    	 				window = i;
	    	 		}
	    	 		
				if (countE == 3 && (countM == 0 || (countM == 1 && caseEnd))) {
					
		   	 		if(countM==0)
    	 			{
		   	 			//original window
    	 				for(int i=0; i<6; i++)
    	 				{
    	 					if(start_y+i>18)
    	 						break;
    	 					if(map[start_x][start_y + i] == -1)
    	 						//moving comparing window
	    	 					for(int j=3; j>=0; j--)
	    	 					{
	    	 						//comparing window
	    	 						//from left to right
	    	 						for(int k=-1; k<5; k++)
	    	 						{
	    	 							if(start_x-j+k > 18 || start_y+i > 18 || start_x-j+k < 0 || start_y+i < 0)
	    	 								break;
	    	 							
	    	 							current = map[start_x-j+k][start_y+i];
	    	 		    				
	    	 			    	 		if (current == my_stone)
	    	 			    	 		{
	    	 		    	 				countM2 ++;
	    	 		    	 				//comparing window has my_stone on one end
	    	 		    	 				if(k==-1 || k==4)
	    	 		    	 					hasOne = true;
	    	 			    	 		}
	    	 		    	 			else if (current == others_stone)
	    	 		    	 			{
	    	 		    	 				if(k>=0 && k<4)
	    	 		    	 					countE2 ++;
	    	 		    	 			}
	    	 						}
	    	 						if(countM2 == 0 && countE2 == 2)
	    	 						{
	    	 							if(!defenseMode)
	    	 								for(int k=0; k<4; k++)
	    	    	 						{
	    	    	 							if(start_x-j+k > 18 || start_y+i > 18 || start_x-j+k < 0 || start_y+i < 0)
	    	    	 								break;
	    	    	 							
	    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i] == -1)
	    	    	 		    	 				must = new Point(start_x-j+k,start_y+i);
	    	    	 						}
	    	 							System.out.println("down/M:0/EM:0/L-R");
	    	 							return new Point(start_x, start_y + i);
	    	 						}
	    	 						//original is clear, comparing window is blocked on one side
	    	 						else if(countM2 == 1 && hasOne && countE2 ==  2)
	    	 						{
	    	 							if(i==0)
	    	 							{
		    	 							for (int q = 0; q <= 3; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
			    	 							if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	    	 							if(start_x-j+k > 18 || start_y+i > 18 || start_x-j+k < 0 || start_y+i < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/L-R/i=0");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
	    	 							}
	    	 							else if(i==1)
	    	 							{
		    	 							for (int q = 0; q <= 3; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
			    	 							if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	    	 							if(start_x-j+k > 18 || start_y+i > 18 || start_x-j+k < 0 || start_y+i < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/L-R/i=1");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
		    	 							
		    	 							for (int q = 1; q <= 4; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
			    	 							if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	    	 							if(start_x-j+k > 18 || start_y+i > 18 || start_x-j+k < 0 || start_y+i < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/L-R/i=1");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
	    	 							}
	    	 							else if(i==2 || i==3)
	    	 							{
		    	 							for (int q = 0; q <= 3; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
			    	 							if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	    	 							if(start_x-j+k > 18 || start_y+i > 18 || start_x-j+k < 0 || start_y+i < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/L-R/i=2||3");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
		    	 							
		    	 							for (int q = 1; q <= 4; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
			    	 							if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	    	 							if(start_x-j+k > 18 || start_y+i > 18 || start_x-j+k < 0 || start_y+i < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/L-R/i=2||3");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
		    	 							
		    	 							for (int q = 2; q <= 5; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
			    	 							if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	    	 							if(start_x-j+k > 18 || start_y+i > 18 || start_x-j+k < 0 || start_y+i < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/L-R/i=2||3");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
	    	 							}
	    	 							else if(i==4)
	    	 							{
		    	 							for (int q = 1; q <= 4; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
			    	 							if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	    	 							if(start_x-j+k > 18 || start_y+i > 18 || start_x-j+k < 0 || start_y+i < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/L-R/i=4");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
		    	 							
		    	 							for (int q = 2; q <= 5; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
			    	 							if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	    	 							if(start_x-j+k > 18 || start_y+i > 18 || start_x-j+k < 0 || start_y+i < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/L-R/i=4");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
	    	 							}
	    	 							else if(i==5)
	    	 							{
		    	 							for (int q = 2; q <= 5; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
			    	 							if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	    	 							if(start_x-j+k > 18 || start_y+i > 18 || start_x-j+k < 0 || start_y+i < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/L-R/i=5");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
	    	 							}
	    	 						}
	    	 						
	    	 						countM2 = 0;
	    	 						countE2 = 0;
	    	 						hasOne = false;
	    	 						
	    	 						//from up-left to down-right
	    	 						for(int k=-1; k<5; k++)
	    	 						{
	    	 							if(start_x-j+k > 18 || start_y+i-j+k > 18 || start_x-j+k < 0 || start_y+i-j+k < 0)
	    	 								break;
	    	 							
	    	 							current = map[start_x-j+k][start_y+i-j+k];
	    	 		    				
	    	 			    	 		if (current == my_stone)
	    	 			    	 		{
	    	 		    	 				countM2 ++;
	    	 		    	 				//comparing window has my_stone on one end
	    	 		    	 				if(k==-1 || k==4)
	    	 		    	 					hasOne = true;
	    	 			    	 		}
	    	 		    	 			else if (current == others_stone)
	    	 		    	 			{
	    	 		    	 				if(k>=0 && k<4)
	    	 		    	 					countE2 ++;
	    	 		    	 			}
	    	 						}
	    	 						if(countM2 == 0 && countE2 == 2)
	    	 						{
	    	 							if(!defenseMode)
	    	 								for(int k=0; k<4; k++)
	    	    	 						{
	    	 									if(start_x-j+k > 18 || start_y+i-j+k > 18 || start_x-j+k < 0 || start_y+i-j+k < 0)
	    	    	 								break;
	    	    	 							
	    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i-j+k] == -1)
	    	    	 		    	 				must = new Point(start_x-j+k,start_y+i-j+k);
	    	    	 						}
	    	 							System.out.println("down/M:0/EM:0/UL-DR/");
	    	 							return new Point(start_x, start_y + i);
	    	 						}
	    	 						//need to modify original window size to 4
	    	 						else if(countM2 == 1 && hasOne && countE2 ==  2)
	    	 						{
	    	 							if(i==0)
	    	 							{
		    	 							for (int q = 0; q <= 3; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x-j+k > 18 || start_y+i-j+k > 18 || start_x-j+k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i-j+k);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/UL-DR/i=0");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
	    	 							}
	    	 							else if(i==1)
	    	 							{
		    	 							for (int q = 0; q <= 3; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x-j+k > 18 || start_y+i-j+k > 18 || start_x-j+k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i-j+k);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/UL-DR/i=1");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
		    	 							
		    	 							for (int q = 1; q <= 4; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x-j+k > 18 || start_y+i-j+k > 18 || start_x-j+k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i-j+k);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/UL-DR/i=1");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
	    	 							}
	    	 							else if(i==2 || i==3)
	    	 							{
		    	 							for (int q = 0; q <= 3; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x-j+k > 18 || start_y+i-j+k > 18 || start_x-j+k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i-j+k);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/UL-DR/i=2||3");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
		    	 							
		    	 							for (int q = 1; q <= 4; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x-j+k > 18 || start_y+i-j+k > 18 || start_x-j+k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i-j+k);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/UL-DR/i=2||3");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
		    	 							
		    	 							for (int q = 2; q <= 5; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x-j+k > 18 || start_y+i-j+k > 18 || start_x-j+k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i-j+k);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/UL-DR/i=2||3");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
	    	 							}
	    	 							else if(i==4)
	    	 							{
		    	 							for (int q = 1; q <= 4; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x-j+k > 18 || start_y+i-j+k > 18 || start_x-j+k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i-j+k);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/UL-DR/i=4");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
		    	 							
		    	 							for (int q = 2; q <= 5; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x-j+k > 18 || start_y+i-j+k > 18 || start_x-j+k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i-j+k);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/UL-DR/i=4");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
	    	 							}
	    	 							else if(i==5)
	    	 							{
		    	 							for (int q = 2; q <= 5; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x-j+k > 18 || start_y+i-j+k > 18 || start_x-j+k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x-j+k,start_y+i-j+k);
			    	    	 						}
			    	 							System.out.println("down/M:0/EM:1/UL-DR/i=5");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
	    	 							}
	    	 						}
	    	 						
	    	 						countM2 = 0;
	    	 						countE2 = 0;
	    	 						hasOne = false;
	    	 						
	    	 						//from up-right to down-left
	    	 						for(int k=-1; k<5; k++)
	    	 						{
	    	 							if(start_x+j-k > 18 || start_y+i-j+k > 18 || start_x+j-k < 0 || start_y+i-j+k < 0)
	    	 								break;
	    	 							
	    	 							current = map[start_x+j-k][start_y+i-j+k];
	    	 		    				
	    	 			    	 		if (current == my_stone)
	    	 			    	 		{
	    	 		    	 				countM2 ++;
	    	 		    	 				//comparing window has my_stone on one end
	    	 		    	 				if(k==-1 || k==4)
	    	 		    	 					hasOne = true;
	    	 			    	 		}
	    	 		    	 			else if (current == others_stone)
	    	 		    	 			{
	    	 		    	 				if(k>=0 && k<4)
	    	 		    	 					countE2 ++;
	    	 		    	 			}
	    	 						}
	    	 						if(countM2 == 0 && countE2 == 2)
	    	 						{
	    	 							if(!defenseMode)
	    	 								for(int k=0; k<4; k++)
	    	    	 						{
	    	 									if(start_x+j-k > 18 || start_y+i-j+k > 18 || start_x+j-k < 0 || start_y+i-j+k < 0)
	    	    	 								break;
	    	    	 							
	    	    	 			    	 		if (-j+k!=0 && map[start_x+j-k][start_y+i-j+k] == -1)
	    	    	 		    	 				must = new Point(start_x+j-k,start_y+i-j+k);
	    	    	 						}
	    	 							System.out.println("down/M:0/EM:0/UR-DL/");
	    	 							return new Point(start_x, start_y + i);
	    	 						}
	    	 						//need to modify original window size to 4
	    	 						else if(countM2 == 1 && hasOne && countE2 ==  2)
	    	 						{
	    	 							if(i==0)
	    	 							{
		    	 							for (int q = 0; q <= 3; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x+j-k > 18 || start_y+i-j+k > 18 || start_x+j-k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x+j-k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x+j-k,start_y+i-j+k);
			    	    	 						}
		    	 								System.out.println("down/M:0/EM:1/UR-DL/i=0");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
	    	 							}
	    	 							else if(i==1)
	    	 							{
		    	 							for (int q = 0; q <= 3; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x+j-k > 18 || start_y+i-j+k > 18 || start_x+j-k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x+j-k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x+j-k,start_y+i-j+k);
			    	    	 						}
		    	 								System.out.println("down/M:0/EM:1/UR-DL/i=1");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
		    	 							
		    	 							for (int q = 1; q <= 4; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x+j-k > 18 || start_y+i-j+k > 18 || start_x+j-k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x+j-k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x+j-k,start_y+i-j+k);
			    	    	 						}
		    	 								System.out.println("down/M:0/EM:1/UR-DL/i=1");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
	    	 							}
	    	 							else if(i==2 || i==3)
	    	 							{
		    	 							for (int q = 0; q <= 3; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x+j-k > 18 || start_y+i-j+k > 18 || start_x+j-k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x+j-k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x+j-k,start_y+i-j+k);
			    	    	 						}
		    	 								System.out.println("down/M:0/EM:1/UR-DL/i=2||3");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
		    	 							
		    	 							for (int q = 1; q <= 4; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x+j-k > 18 || start_y+i-j+k > 18 || start_x+j-k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x+j-k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x+j-k,start_y+i-j+k);
			    	    	 						}
		    	 								System.out.println("down/M:0/EM:1/UR-DL/i=2||3");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
		    	 							
		    	 							for (int q = 2; q <= 5; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x+j-k > 18 || start_y+i-j+k > 18 || start_x+j-k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x+j-k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x+j-k,start_y+i-j+k);
			    	    	 						}
		    	 								System.out.println("down/M:0/EM:1/UR-DL/i=2||3");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
	    	 							}
	    	 							else if(i==4)
	    	 							{
		    	 							for (int q = 1; q <= 4; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x+j-k > 18 || start_y+i-j+k > 18 || start_x+j-k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x+j-k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x+j-k,start_y+i-j+k);
			    	    	 						}
		    	 								System.out.println("down/M:0/EM:1/UR-DL/i=4");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
		    	 							
		    	 							for (int q = 2; q <= 5; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x+j-k > 18 || start_y+i-j+k > 18 || start_x+j-k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x+j-k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x+j-k,start_y+i-j+k);
			    	    	 						}
		    	 								System.out.println("down/M:0/EM:1/UR-DL/i=4");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
	    	 							}
	    	 							else if(i==5)
	    	 							{
		    	 							for (int q = 2; q <= 5; q++)
		    	 			    	 		{
		    	 			    	 			if (start_y + q > 18)
		    	 			    	 				break;
		    	 			    	 			
		    	 			    	 			current = map[start_x][start_y + q];
		    	 			    	 			
		    	 			    	 			if (current == others_stone)
		    	 			    	 				countE3 ++;
		    	 			    	 		}
		    	 							if(countE3==3)
		    	 							{
		    	 								if(!defenseMode)
			    	 								for(int k=0; k<4; k++)
			    	    	 						{
			    	 									if(start_x+j-k > 18 || start_y+i-j+k > 18 || start_x+j-k < 0 || start_y+i-j+k < 0)
			    	    	 								break;
			    	    	 							
			    	    	 			    	 		if (-j+k!=0 && map[start_x+j-k][start_y+i-j+k] == -1)
			    	    	 		    	 				must = new Point(start_x+j-k,start_y+i-j+k);
			    	    	 						}
		    	 								System.out.println("down/M:0/EM:1/UR-DL/i=5");
			    	 							return new Point(start_x, start_y + i);
		    	 							}
		    	 							countE3 = 0;
	    	 							}
	    	 						}
	    	 						
	    	 						countM2 = 0;
	    	 						countE2 = 0;
	    	 						hasOne = false;
	    	 					}
    	 				}
    	 			}
    	 			else if(caseEnd)
    	 			{
    	 				//original window
    	 				for(int i=0; i<6; i++)
    	 				{
    	 					if(start_y+i>18)
    	 						break;
    	 					if(map[start_x][start_y + i] == -1)
    	 						//moving comparing window
	    	 					for(int j=3; j>=0; j--)
	    	 					{
	    	 						//comparing window
	    	 						//from up to down
	    	 						for(int k=-1; k<5; k++)
	    	 						{
	    	 							if(start_x-j+k > 18 || start_y+i > 18 || start_x-j+k < 0 || start_y+i < 0)
	    	 								break;
	    	 							
	    	 							current = map[start_x-j+k][start_y+i];
	    	 		    				
	    	 			    	 		if (current == my_stone)
	    	 		    	 				countM2 ++;
	    	 		    	 			else if (current == others_stone)
	    	 		    	 			{
	    	 		    	 				if(k>=0 && k<4)
	    	 		    	 					countE2 ++;
	    	 		    	 			}
	    	 						}
	    	 						if(countM2 == 0 && countE2 == 2)
	    	 						{
	    	 							if(!defenseMode)
	    	 								for(int k=0; k<4; k++)
	    	    	 						{
	    	    	 							if(start_x-j+k > 18 || start_y+i > 18 || start_x-j+k < 0 || start_y+i < 0)
	    	    	 								break;
	    	    	 							
	    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i] == -1)
	    	    	 		    	 				must = new Point(start_x-j+k,start_y+i);
	    	    	 						}
	    	 							System.out.println("down/M:1/EM:0/U-P");
	    	 							return new Point(start_x, start_y + i);
	    	 						}
	    	 						
	    	 						countM2 = 0;
	    	 						countE2 = 0;
	    	 						hasOne = false;
	    	 						
	    	 						//from up-left to down-right
	    	 						for(int k=-1; k<5; k++)
	    	 						{
	    	 							if(start_x-j+k > 18 || start_y+i-j+k > 18 || start_x-j+k < 0 || start_y+i-j+k < 0)
	    	 								break;
	    	 							
	    	 							current = map[start_x-j+k][start_y+i-j+k];
	    	 		    				
	    	 			    	 		if (current == my_stone)
	    	 		    	 				countM2 ++;
	    	 		    	 			else if (current == others_stone)
	    	 		    	 			{
	    	 		    	 				if(k>=0 && k<4)
	    	 		    	 					countE2 ++;
	    	 		    	 			}
	    	 						}
	    	 						if(countM2 == 0 && countE2 == 2)
	    	 						{
	    	 							if(!defenseMode)
	    	 								for(int k=0; k<4; k++)
	    	    	 						{
	    	    	 							if(start_x-j+k > 18 || start_y+i > 18 || start_x-j+k < 0 || start_y+i < 0)
	    	    	 								break;
	    	    	 							
	    	    	 			    	 		if (-j+k!=0 && map[start_x-j+k][start_y+i] == -1)
	    	    	 		    	 				must = new Point(start_x-j+k,start_y+i);
	    	    	 						}
	    	 							System.out.println("down/M:1/EM:0/UL-DR");
	    	 							return new Point(start_x, start_y + i);
	    	 						}
	    	 						
	    	 						countM2 = 0;
	    	 						countE2 = 0;
	    	 						hasOne = false;

	    	 						//from up-right to down-left
	    	 						for(int k=-1; k<5; k++)
	    	 						{
	    	 							if(start_x+j-k > 18 || start_y+i-j+k > 18 || start_x+j-k < 0 || start_y+i-j+k < 0)
	    	 								break;
	    	 							
	    	 							current = map[start_x+j-k][start_y+i-j+k];
	    	 		    				
	    	 			    	 		if (current == my_stone)
	    	 		    	 				countM2 ++;
	    	 		    	 				else if (current == others_stone)
	    	 		    	 			{
	    	 		    	 				if(k>=0 && k<4)
	    	 		    	 					countE2 ++;
	    	 		    	 			}
	    	 						}
	    	 						if(countM2 == 0 && countE2 == 2)
	    	 						{
	    	 							if(!defenseMode)
	    	 								for(int k=0; k<4; k++)
	    	    	 						{
	    	 									if(start_x+j-k > 18 || start_y+i-j+k > 18 || start_x+j-k < 0 || start_y+i-j+k < 0)
	    	    	 								break;
	    	    	 							
	    	    	 			    	 		if (-j+k!=0 && map[start_x+j-k][start_y+i-j+k] == -1)
	    	    	 		    	 				must = new Point(start_x+j-k,start_y+i-j+k);
	    	    	 						}
	    	 							System.out.println("down/M:1/EM:0/UR-DL");
	    	 							return new Point(start_x, start_y + i);
	    	 						}
	    	 						
	    	 						countM2 = 0;
	    	 						countE2 = 0;
	    	 						hasOne = false;
	    	 					}
    	 				}
    	 			}
	   	 		}
	    	 	
	    		countE = 0;
	    		countM = 0;		
	    		window = -1;
	    		hasOne = false;
	    		caseEnd = false;
	    			
	    	 	//##### right #####//
	    	 	for (int i = 0; i <= 5; i++)
	    	 	{
	    	 		if (start_x + i > 18)
	    	 			break;
	    	 		
	    	 		current = map[start_x + i][start_y];
	    				
	    	 		if (current == my_stone)
    	 			{
    	 				countM ++;
    	 				if(countM == 1 && (i == 0 || i == 5))
    	 					caseEnd = true;
    	 			}
    	 			else if (current == others_stone)
    	 				countE ++;
    	 			else
    	 				window = i;
    	 		}
    	 		
	    	 	if (countE == 3 && (countM == 0 || (countM == 1 && caseEnd))) {
		   	 		
			   	 		if(countM==0)
	    	 			{
			   	 			//original window
	    	 				for(int i=0; i<6; i++)
	    	 				{
	    	 					if (start_x + i > 18)
	    		    	 			break;
	    	 					if(map[start_x + i][start_y] == -1)
	    	 						//moving comparing window
		    	 					for(int j=3; j>=0; j--)
		    	 					{
		    	 						//comparing window
		    	 						//from up to down
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x+i > 18 || start_x+i < 0 || start_y-j+k > 18 || start_y-j+k < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x+i][start_y-j+k];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 			    	 		{
		    	 		    	 				countM2 ++;
		    	 		    	 				//comparing window has my_stone on one end
		    	 		    	 				if(k==-1 || k==4)
		    	 		    	 					hasOne = true;
		    	 			    	 		}
		    	 		    	 			else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
		    	 							if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x+i > 18 || start_x+i < 0 || start_y-j+k > 18 || start_y-j+k < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y-j+k] == -1)
		    	    	 		    	 				must = new Point(start_x+i,start_y-j+k);
		    	    	 						}
		    	 							System.out.println("right/M:0/EM:0/U-D");
		    	 							return new Point(start_x + i, start_y);
		    	 						}
		    	 						//need to modify original window size to 4
		    	 						else if(countM2 == 1 && hasOne && countE2 ==  2)
		    	 						{
		    	 							if(i==0)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 				    	 		if (start_x + q > 18)
			    	 				    	 			break;
			    	 				    	 		
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/U-D/i=0");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==1)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 				    	 		if (start_x + q > 18)
			    	 				    	 			break;
			    	 				    	 		
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/U-D/i=1");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 				    	 		if (start_x + q > 18)
			    	 				    	 			break;
			    	 				    	 		
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/U-D/i=1");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==2 || i==3)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/U-D/i=2||3");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/U-D/i=2||3");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/U-D/i=2||3");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==4)
		    	 							{
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/U-D/i=4");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/U-D/i=4");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==5)
		    	 							{
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/U-D/i=5");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 						}
		    	 						
		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;

		    	 						//from up-left to down-right
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x+i-j+k][start_y-j+k];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 			    	 		{
		    	 		    	 				countM2 ++;
		    	 		    	 				//comparing window has my_stone on one end
		    	 		    	 				if(k==-1 || k==4)
		    	 		    	 					hasOne = true;
		    	 			    	 		}
		    	 		    	 			else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
		    	 							if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x+i-j+k][start_y-j+k] == -1)
		    	    	 		    	 				must = new Point(start_x+i-j+k,start_y-j+k);
		    	    	 						}
		    	 							System.out.println("right/M:0/EM:0/UL-DR");
		    	 							return new Point(start_x + i, start_y);
		    	 						}
		    	 						//need to modify original window size to 4
		    	 						else if(countM2 == 1 && hasOne && countE2 ==  2)
		    	 						{
		    	 							if(i==0)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i-j+k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i-j+k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UL-DR/i=0");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==1)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i-j+k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i-j+k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UL-DR/i=1");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i-j+k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i-j+k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UL-DR/i=1");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==2 || i==3)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i-j+k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i-j+k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UL-DR/i=2||3");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i-j+k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i-j+k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UL-DR/i=2||3");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i-j+k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i-j+k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UL-DR/i=2||3");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==4)
		    	 							{
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i-j+k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i-j+k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UL-DR/i=4");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i-j+k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i-j+k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UL-DR/i=4");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==5)
		    	 							{
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i-j+k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i-j+k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UL-DR/i=5");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 						}
		    	 						
		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 						
		    	 						//from up-right to down-left
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x+i+j-k][start_y-j+k];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 			    	 		{
		    	 		    	 				countM2 ++;
		    	 		    	 				//comparing window has my_stone on one end
		    	 		    	 				if(k==-1 || k==4)
		    	 		    	 					hasOne = true;
		    	 			    	 		}
		    	 		    	 			else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
		    	 							if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y-j+k] == -1)
		    	    	 		    	 				must = new Point(start_x+i+j-k,start_y-j+k);
		    	    	 						}
		    	 							System.out.println("right/M:0/EM:0/UR-DL");
		    	 							return new Point(start_x + i, start_y);
		    	 						}
		    	 						//need to modify original window size to 4
		    	 						else if(countM2 == 1 && hasOne && countE2 ==  2)
		    	 						{
		    	 							if(i==0)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UR-DL/i=0");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==1)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UR-DL/i=1");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UR-DL/i=1");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==2 || i==3)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UR-DL/i=2||3");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UR-DL/i=2||3");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UR-DL/i=2||3");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==4)
		    	 							{
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UR-DL/i=4");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UR-DL/i=4");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==5)
		    	 							{
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18)
			    	 				    	 			break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y-j+k);
				    	    	 						}
				    	 							System.out.println("right/M:0/EM:1/UR-DL/i=5");
				    	 							return new Point(start_x + i, start_y);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 						}
		    	 						
		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 					}
	    	 				}
	    	 			}
	    	 			else if(caseEnd)
	    	 			{
	    	 				//original window
	    	 				for(int i=0; i<6; i++)
	    	 				{
	    	 					if (start_x + i > 18)
	    		    	 			break;
	    	 					if(map[start_x + i][start_y] == -1)
	    	 						//moving comparing window
		    	 					for(int j=3; j>=0; j--)
		    	 					{
		    	 						//comparing window
		    	 						//from up to down
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x+i > 18 || start_x+i < 0 || start_y-j+k > 18 || start_y-j+k < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x+i][start_y-j+k];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 		    	 				countM2 ++;
		    	 		    	 			else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
		    	 							if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x+i > 18 || start_x+i < 0 || start_y-j+k > 18 || start_y-j+k < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y-j+k] == -1)
		    	    	 		    	 				must = new Point(start_x+i,start_y-j+k);
		    	    	 						}
		    	 							System.out.println("right/M:1/EM:0/U-D");
		    	 							return new Point(start_x + i, start_y);
		    	 						}
		    	 						
		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 						
		    	 						//from up-left to down-right
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x+i-j+k][start_y-j+k];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 		    	 				countM2 ++;
		    	 		    	 			else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
		    	 							if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x+i-j+k][start_y-j+k] == -1)
		    	    	 		    	 				must = new Point(start_x+i-j+k,start_y-j+k);
		    	    	 						}
		    	 							System.out.println("right/M:1/EM:0/UL-DR");
		    	 							return new Point(start_x + i, start_y);
		    	 						}

		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 						
		    	 						//from up-right to down-left
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x+i+j-k][start_y-j+k];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 		    	 				countM2 ++;
		    	 		    	 				else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
		    	 							if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y-j+k > 18 || start_y-j+k < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y-j+k] == -1)
		    	    	 		    	 				must = new Point(start_x+i+j-k,start_y-j+k);
		    	    	 						}
		    	 							System.out.println("right/M:1/EM:0/UR-DL");
		    	 							return new Point(start_x + i, start_y);
		    	 						}

		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 					}
	    	 				}
	    	 			}
	   	 		}
    	 		
    			countE = 0;
    			countM = 0;		
    			window = -1;
	    		hasOne = false;
	    		caseEnd = false;
    			
    			
    			//##### down-right #####//
    	 		for (int i = 0; i <= 5; i++)
    	 		{
    	 			if (start_x + i > 18 || start_y + i > 18)
    	 				break;
    	 			
    	 			current = map[start_x + i][start_y + i];
    	 			
    	 			if (current == my_stone)
    	 			{
    	 				countM ++;
    	 				if(countM == 1 && (i == 0 || i == 5))
    	 					caseEnd = true;
    	 			}
    	 			else if (current == others_stone)
    	 				countE ++;
    	 			else
    	 				window = i;
    	 		}
    	 		
    	 		if (countE == 3 && (countM == 0 || (countM == 1 && caseEnd))) {
		   	 		
			   	 		if(countM==0)
	    	 			{
			   	 			//original window
	    	 				for(int i=0; i<6; i++)
	    	 				{
	    	 					if (start_x + i > 18 || start_y + i > 18)
	    	    	 				break;
	    	 					if(map[start_x + i][start_y + i] == -1)
	    	 						//moving comparing window
		    	 					for(int j=3; j>=0; j--)
		    	 					{
		    	 						//comparing window
		    	 						//from up to down
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x+i][start_y+i-j+k];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 			    	 		{
		    	 		    	 				countM2 ++;
		    	 		    	 				//comparing window has my_stone on one end
		    	 		    	 				if(k==-1 || k==4)
		    	 		    	 					hasOne = true;
		    	 			    	 		}
		    	 		    	 			else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
		    	 							if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
		    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
		    	    	 						}
		    	 							System.out.println("down-right/M:0/EM:0/U-D");
		    	 							return new Point(start_x + i, start_y + i);
		    	 						}
		    	 						//need to modify original window size to 4
		    	 						else if(countM2 == 1 && hasOne && countE2 ==  2)
		    	 						{
		    	 							if(i==0)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
			    	 								System.out.println("down-right/M:0/EM:1/U-D/i=0");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==1)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
			    	 								System.out.println("down-right/M:0/EM:1/U-D/i=1");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
			    	 								System.out.println("down-right/M:0/EM:1/U-D/i=1");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==2 || i==3)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
			    	 								System.out.println("down-right/M:0/EM:1/U-D/i=2||3");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
			    	 								System.out.println("down-right/M:0/EM:1/U-D/i=2||3");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
			    	 								System.out.println("down-right/M:0/EM:1/U-D/i=2||3");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==4)
		    	 							{
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
			    	 								System.out.println("down-right/M:0/EM:1/U-D/i=4");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
			    	 								System.out.println("down-right/M:0/EM:1/U-D/i=4");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==5)
		    	 							{
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
			    	 								System.out.println("down-right/M:0/EM:1/U-D/i=5");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 						}
		    	 						
		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 						
		    	 						//from left to right
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x+i-j+k][start_y+i];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 			    	 		{
		    	 		    	 				countM2 ++;
		    	 		    	 				//comparing window has my_stone on one end
		    	 		    	 				if(k==-1 || k==4)
		    	 		    	 					hasOne = true;
		    	 			    	 		}
		    	 		    	 			else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
		    	 							if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
		    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
		    	    	 						}
		    	 							System.out.println("down-right/M:0/EM:0/L-R");
		    	 							return new Point(start_x + i, start_y + i);
		    	 						}
		    	 						//need to modify original window size to 4
		    	 						else if(countM2 == 1 && hasOne && countE2 ==  2)
		    	 						{
		    	 							if(i==0)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/L-R/i=0");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==1)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/L-R/i=1");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/L-R/i=1");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==2 || i==3)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/L-R/i=2||3");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/L-R/i=2||3");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/L-R/i=2||3");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==4)
		    	 							{
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/L-R/i=4");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/L-R/i=4");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==5)
		    	 							{
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/L-R/i=5");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 						}

		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 						
		    	 						//from up-right to down-left
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x+i+j-k][start_y+i-j+k];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 			    	 		{
		    	 		    	 				countM2 ++;
		    	 		    	 				//comparing window has my_stone on one end
		    	 		    	 				if(k==-1 || k==4)
		    	 		    	 					hasOne = true;
		    	 			    	 		}
		    	 		    	 			else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
		    	 							if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y+i-j+k] == -1)
		    	    	 		    	 				must = new Point(start_x+i+j-k,start_y+i-j+k);
		    	    	 						}
		    	 							System.out.println("down-right/M:0/EM:0/UR-DL");
		    	 							return new Point(start_x + i, start_y + i);
		    	 						}
		    	 						//need to modify original window size to 4
		    	 						else if(countM2 == 1 && hasOne && countE2 ==  2)
		    	 						{
		    	 							if(i==0)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/UR-DL/i=0");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==1)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/UR-DL/i=1");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/UR-DL/i=1");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==2 || i==3)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/UR-DL/i=2||3");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/UR-DL/i=2||3");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/UR-DL/i=2||3");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==4)
		    	 							{
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/UR-DL/i=4");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/UR-DL/i=4");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==5)
		    	 							{
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x + q > 18 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x + q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x+i+j-k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-right/M:0/EM:1/UR-DL/i=5");
				    	 							return new Point(start_x + i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 						}

		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 					}
	    	 				}
	    	 			}
	    	 			else if(caseEnd)
	    	 			{
	    	 				//original window
	    	 				for(int i=0; i<6; i++)
	    	 				{
	    	 					if (start_x + i > 18 || start_y + i > 18)
	    	    	 				break;
	    	 					if(map[start_x + i][start_y + i] == -1)
	    	 						//moving comparing window
		    	 					for(int j=3; j>=0; j--)
		    	 					{
		    	 						//comparing window
		    	 						//from up to down
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x+i][start_y+i-j+k];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 		    	 				countM2 ++;
		    	 		    	 			else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
	    	 								if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x+i > 18 || start_x+i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x+i][start_y+i-j+k] == -1)
		    	    	 		    	 				must = new Point(start_x+i,start_y+i-j+k);
		    	    	 						}
		    	 							System.out.println("down-right/M:1/EM:0/U-D");
		    	 							return new Point(start_x + i, start_y + i);
		    	 						}

		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 						
		    	 						//from up-left to down-right
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x+i-j+k][start_y+i];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 		    	 				countM2 ++;
		    	 		    	 			else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
	    	 								if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x+i-j+k > 18 || start_x+i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x+i-j+k][start_y+i] == -1)
		    	    	 		    	 				must = new Point(start_x+i-j+k,start_y+i);
		    	    	 						}
		    	 							System.out.println("down-right/M:1/EM:0/UL-DR");
		    	 							return new Point(start_x + i, start_y + i);
		    	 						}

		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 						
		    	 						//from up-right to down-left
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x+i+j-k][start_y+i-j+k];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 		    	 				countM2 ++;
		    	 		    	 				else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
		    	 							if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x+i+j-k > 18 || start_x+i+j-k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x+i+j-k][start_y+i-j+k] == -1)
		    	    	 		    	 				must = new Point(start_x+i+j-k,start_y+i-j+k);
		    	    	 						}
		    	 							System.out.println("down-right/M:1/EM:0/UR-DL");
		    	 							return new Point(start_x + i, start_y + i);
		    	 						}

		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 					}
	    	 				}
	    	 			}
	   	 		}
    	 		
    			countE = 0;
    			countM = 0;		
    			window = -1;
	    		hasOne = false;
	    		caseEnd = false;

    			
    			//##### down-left #####//
    	 		for (int i = 0; i <= 5; i++)
    	 		{
    	 			if (start_x - i < 0 || start_y + i > 18)
    	 				break;
    	 			
    	 			current = map[start_x - i][start_y + i];
    	 			
    	 			if (current == my_stone)
    	 			{
    	 				countM ++;
    	 				if(countM == 1 && (i == 0 || i == 5))
    	 					caseEnd = true;
    	 			}
    	 			else if (current == others_stone)
    	 				countE ++;
    	 			else
    	 				window = i;
    	 		}
    	 		
    	 		if (countE == 3 && (countM == 0 || (countM == 1 && caseEnd))) {
		   	 		
			   	 		if(countM==0)
	    	 			{
			   	 			//original window
	    	 				for(int i=0; i<6; i++)
	    	 				{
	    	 					if (start_x - i < 0 || start_y + i > 18)
	    	    	 				break;
	    	    	 			
	    	 					if(map[start_x - i][start_y + i] == -1)
	    	 						//moving comparing window
		    	 					for(int j=3; j>=0; j--)
		    	 					{
		    	 						//comparing window
		    	 						//from up to down
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x-i > 18 || start_x-i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x-i][start_y+i-j+k];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 			    	 		{
		    	 		    	 				countM2 ++;
		    	 		    	 				//comparing window has my_stone on one end
		    	 		    	 				if(k==-1 || k==4)
		    	 		    	 					hasOne = true;
		    	 			    	 		}
		    	 		    	 			else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
	    	 								if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x-i > 18 || start_x-i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x-i][start_y+i-j+k] == -1)
		    	    	 		    	 				must = new Point(start_x-i,start_y+i-j+k);
		    	    	 						}
		    	 							System.out.println("down-left/M:0/EM:0/U-D");
		    	 							return new Point(start_x - i, start_y + i);
		    	 						}
		    	 						//need to modify original window size to 4
		    	 						else if(countM2 == 1 && hasOne && countE2 ==  2)
		    	 						{
		    	 							if(i==0)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i > 18 || start_x-i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/U-D/i=0");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==1)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i > 18 || start_x-i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/U-D/i=1");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i > 18 || start_x-i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/U-D/i=1");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==2 || i==3)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i > 18 || start_x-i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/U-D/i=2||3");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i > 18 || start_x-i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/U-D/i=2||3");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i > 18 || start_x-i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/U-D/i=2||3");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==4)
		    	 							{
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i > 18 || start_x-i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/U-D/i=4");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i > 18 || start_x-i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/U-D/i=4");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==5)
		    	 							{
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i > 18 || start_x-i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/U-D/i=5");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 						}

		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 						
		    	 						//from left to right
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x-i-j+k][start_y+i];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 			    	 		{
		    	 		    	 				countM2 ++;
		    	 		    	 				//comparing window has my_stone on one end
		    	 		    	 				if(k==-1 || k==4)
		    	 		    	 					hasOne = true;
		    	 			    	 		}
		    	 		    	 			else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
	    	 								if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i] == -1)
		    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i);
		    	    	 						}
		    	 							System.out.println("down-left/M:0/EM:0/L-R");
		    	 							return new Point(start_x - i, start_y + i);
		    	 						}
		    	 						//need to modify original window size to 4
		    	 						else if(countM2 == 1 && hasOne && countE2 ==  2)
		    	 						{
		    	 							if(i==0)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/L-R/i=0");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==1)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/L-R/i=1");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/L-R/i=1");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==2 || i==3)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/L-R/i=2||3");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/L-R/i=2||3");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/L-R/i=2||3");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==4)
		    	 							{
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/L-R/i=4");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/L-R/i=4");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==5)
		    	 							{
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/L-R/i=5");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 						}

		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 						
		    	 						//from up-left to down-right
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x-i-j+k][start_y+i-j+k];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 			    	 		{
		    	 		    	 				countM2 ++;
		    	 		    	 				//comparing window has my_stone on one end
		    	 		    	 				if(k==-1 || k==4)
		    	 		    	 					hasOne = true;
		    	 			    	 		}
		    	 		    	 			else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
	    	 								if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i-j+k] == -1)
		    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i-j+k);
		    	    	 						}
		    	 							System.out.println("down-left/M:0/EM:0/UL-DR");
		    	 							return new Point(start_x - i, start_y + i);
		    	 						}
		    	 						//need to modify original window size to 4
		    	 						else if(countM2 == 1 && hasOne && countE2 ==  2)
		    	 						{
		    	 							if(i==0)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/UL-DR/i=0");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==1)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/UL-DR/i=1");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/UL-DR/i=1");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==2 || i==3)
		    	 							{
			    	 							for (int q = 0; q <= 3; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/UL-DR/i=2||3");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/UL-DR/i=2||3");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/UL-DR/i=2||3");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==4)
		    	 							{
			    	 							for (int q = 1; q <= 4; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/UL-DR/i=4");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
			    	 							
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/UL-DR/i=4");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 							else if(i==5)
		    	 							{
			    	 							for (int q = 2; q <= 5; q++)
			    	 			    	 		{
			    	 								if (start_x - q < 0 || start_y + q > 18)
			    	 			    	 				break;
			    	 			    	 			
			    	 			    	 			current = map[start_x - q][start_y + q];
			    	 			    	 			
			    	 			    	 			if (current == others_stone)
			    	 			    	 				countE3 ++;
			    	 			    	 		}
			    	 							if(countE3==3)
			    	 							{
			    	 								if(!defenseMode)
				    	 								for(int k=0; k<4; k++)
				    	    	 						{
				    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
						    	 								break;
				    	    	 							
				    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i-j+k] == -1)
				    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i-j+k);
				    	    	 						}
				    	 							System.out.println("down-left/M:0/EM:1/UL-DR/i=5");
				    	 							return new Point(start_x - i, start_y + i);
			    	 							}
			    	 							countE3 = 0;
		    	 							}
		    	 						}

		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 					}
	    	 				}
	    	 			}
	    	 			else if(caseEnd)
	    	 			{
	    	 				//original window
	    	 				for(int i=0; i<6; i++)
	    	 				{
	    	 					if (start_x - i < 0 || start_y + i > 18)
	    	    	 				break;
	    	    	 			
	    	 					if(map[start_x - i][start_y + i] == -1)
	    	 						//moving comparing window
		    	 					for(int j=3; j>=0; j--)
		    	 					{
		    	 						//comparing window
		    	 						//from up to down
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x-i > 18 || start_x-i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x-i][start_y+i-j+k];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 		    	 				countM2 ++;
		    	 		    	 			else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
	    	 								if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x-i > 18 || start_x-i < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x-i][start_y+i-j+k] == -1)
		    	    	 		    	 				must = new Point(start_x-i,start_y+i-j+k);
		    	    	 						}
		    	 							System.out.println("down-left/M:1/EM:0/U-D");
		    	 							return new Point(start_x - i, start_y + i);
		    	 						}

		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 						
		    	 						//from up-left to down-right
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x-i-j+k][start_y+i];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 		    	 				countM2 ++;
		    	 		    	 			else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
	    	 								if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i > 18 || start_y+i < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i] == -1)
		    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i);
		    	    	 						}
		    	 							System.out.println("down-left/M:1/EM:0/UL-DR");
		    	 							return new Point(start_x - i, start_y + i);
		    	 						}

		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 						
		    	 						//from up-right to down-left
		    	 						for(int k=-1; k<5; k++)
		    	 						{
		    	 							if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
		    	 								break;
		    	 							
		    	 							current = map[start_x-i-j+k][start_y+i-j+k];
		    	 		    				
		    	 			    	 		if (current == my_stone)
		    	 		    	 				countM2 ++;
		    	 		    	 				else if (current == others_stone)
		    	 		    	 			{
		    	 		    	 				if(k>=0 && k<4)
		    	 		    	 					countE2 ++;
		    	 		    	 			}
		    	 						}
		    	 						if(countM2 == 0 && countE2 == 2)
		    	 						{
		    	 							if(!defenseMode)
		    	 								for(int k=0; k<4; k++)
		    	    	 						{
		    	 									if(start_x-i-j+k > 18 || start_x-i-j+k < 0 || start_y+i-j+k > 18 || start_y+i-j+k < 0)
				    	 								break;
		    	    	 							
		    	    	 			    	 		if (-j+k!=0 && map[start_x-i-j+k][start_y+i-j+k] == -1)
		    	    	 		    	 				must = new Point(start_x-i-j+k,start_y+i-j+k);
		    	    	 						}
		    	 							System.out.println("down-left/M:1/EM:0/UR-DL");
		    	 							return new Point(start_x - i, start_y + i);
		    	 						}

		    	 						countM2 = 0;
		    	 						countE2 = 0;
		    	 						hasOne = false;
		    	 					}
	    	 				}
	    	 			}
	   	 		}
    	 		countE = 0;
    			countM = 0;		
    			window = -1;
	    		hasOne = false;
	    		caseEnd = false;
    	 	}
		}
		
		return result;
	}
	
}