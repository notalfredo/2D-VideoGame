/*
You may use ANSI escape sequences to add colored text on Windows 10 Command Prompt and macOS Terminal.
For newer versions of macOS, you shouldn't have to configure anything for them to work.
For Windows 10, you may need to enable the ANSI escape sequences since they are disabled by default.
It can be enabled with the following registry modification (run from the command prompt as administrator):
reg add HKEY_CURRENT_USER\Console /v VirtualTerminalLevel /t REG_DWORD /d 0x00000001 /f

*/


import java.io.*;
import javax.sound.sampled.*;
import java.util.*;
public class Game
{
		static String CLS = "\033[2J\033[1;1H";
		static String Red = "\033[31;1m";
		static String Green = "\033[32;1m";
		static String Yellow = "\033[33;1m";
		static String Blue = "\033[34;1m";
		static String Purple = "\033[35;1m";
		static String Cyan = "\033[36;1m";
		static String White = "\033[37;1m";
		static String Normal = "\033[0m";

		public static void main(String[] args) throws IOException
		{
			Scanner in = new Scanner(System.in);
					playSound("DoomMusic.wav");
			String Choice = "";

			//creating the player will initialize the world
			Player P = new Player ("Alfredo" , 'P');
			// Create some enemies here in random locations
			ArrayList<Enemy> Enemies = new ArrayList<Enemy>();
			Enemies.add(new Enemy("Hell Demon"));
			Enemies.add(new Enemy("Hell Demon"));
			Enemies.add(new Enemy("Hell Demon"));
			Enemies.add(new Enemy("Hell Demon"));
			Enemies.add(new Enemy("Hell Demon"));
			Enemies.add(new Enemy("Baron of Hell "));
			Enemies.add(new Enemy("Baron of Hell "));
			Enemies.add(new Enemy("Baron of Hell "));

			IntroScreen I = new IntroScreen();
			System.out.println("Press Enter To Start");
			in.nextLine();

			while (!Choice.equals("q") && P.HP > 0 && Enemies.size()>0)
			{
				System.out.print(CLS);
				P.PrintWorld();
				System.out.println("HP: " +Red+P.HP+Normal+" Attack:"+Red+P.Attack+Normal);
				System.out.println("Enter your command: ");
				Choice = in.nextLine();

				// Player Moves
				if (Choice.equals("a"))
					P.MoveLeft();
				if (Choice.equals("d"))
					P.MoveRight();
				if (Choice.equals("w"))
					P.MoveUp();
				if (Choice.equals("s"))
					P.MoveDown();


				for (int i=0; i<Enemies.size(); i++)
				 {
					if ((Enemies.get(i).Ypos == P.Ypos && (Enemies.get(i).Xpos == P.Xpos+1)) || // player is to the left
					   (Enemies.get(i).Ypos == P.Ypos && (Enemies.get(i).Xpos == P.Xpos-1)) ||  // player is to the right
					   (Enemies.get(i).Xpos == P.Xpos && (Enemies.get(i).Ypos == P.Ypos+1)) ||  // player is above
					   (Enemies.get(i).Xpos == P.Xpos && (Enemies.get(i).Ypos == P.Ypos-1)))    // player is below
					{
					   Enemies.get(i).HP -= P.Attack;    // Player attacks Enemy
					   P.HP -= Enemies.get(i).Attack;    // Enemy attacks Player
					   // Here is a more advanced attack formula that utilizes attack and armor values.
					   // P.HP -= (100 * Enemy.get(i).Attack) / (100 + P.Armor)

					   if (Enemies.get(i).HP <= 0)    // Enemy dies
					   {
						  P.World[Enemies.get(i).Xpos][Enemies.get(i).Ypos] = ' ';
						  Enemies.remove(i);
					   }
					}
				 }


			for (int i=0 ; i<Enemies.size(); i++)
			{
				if ((Math.abs(P.Xpos - Enemies.get(i).Xpos) <= Enemies.get(i).Range) &&
					(Math.abs(P.Ypos - Enemies.get(i).Ypos) <= Enemies.get(i).Range))
				{
					if (Enemies.get(i).Xpos > P.Xpos)
						Enemies.get(i).MoveLeft();
					else
						Enemies.get(i).MoveRight();
					if (Enemies.get(i).Ypos > P.Ypos)
						Enemies.get(i).MoveUp();
					else
						Enemies.get(i).MoveDown();
				}
				else
				{
					int R = (int)(Math.random()*4);
					if (R == 0)
						Enemies.get(i).MoveLeft();
					else if (R == 1)
						Enemies.get(i).MoveRight();
					else if (R == 2)
						Enemies.get(i).MoveUp();
					else
						Enemies.get(i).MoveDown();
				}
			}
		}
		LoserScreen L;
		if(P.HP <=0)
			L = new LoserScreen();

		WinningScreen W;
		for (int i=0; i<Enemies.size(); i++)
		{
			if (Enemies.get(i).HP <= 0)    // Enemy dies
								   {
			W = new WinningScreen();

					   }
		}



	}// end main

		public static void playSound(String songname)
		{
			try
			{
				AudioInputStream audioInputStream =
					AudioSystem.getAudioInputStream(new File(songname).getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
			}
			catch(Exception ex)
			{
				System.out.println("Error with playing sound.");
				ex.printStackTrace();
	    		}
		}


}



class GameObject //Superclass
{
	static String CLS = "\033[2J\033[1;1H";
	static String Red = "\033[31;1m";
	static String Green = "\033[32;1m";
	static String Yellow = "\033[33;1m";
	static String Blue = "\033[34;1m";
	static String Purple = "\033[35;1m";
	static String Cyan = "\033[36;1m";
	static String White = "\033[37;1m";
	static String Normal = "\033[0m"; // default gray color & reset background to black
	static char World[][] = new char[41][21];
	int Xpos , Ypos;
	char Avatar;

	void PrintWorld()
	{
		for (int y=1; y<=20; y++)
		{
			for (int x=1; x<=40; x++)
			{
				if (World[x][y]==' ')
					System.out.print(' ');
				else if (World[x][y] == '#')
					System.out.print(Red + '#' + Normal);
				else if (World[x][y] =='H')
					System.out.print(Yellow+ 'H' + Normal); // Hell Demon
				else if (World[x][y] =='B')
					System.out.print(White + 'B' +Normal); //Baron of Hell
				else if (World[x][y] =='P')
					System.out.print(Purple + 'P' + Normal); //player
				else if (World [x][y] == '+')
					System.out.print(Yellow + '+' + Normal);
				else if (World [x][y] == '@')
					System.out.print(Green + '@' + Normal);
				else if (World [x][y] == '-')
					System.out.print(Purple + '-' + Normal);
				else if (World [x][y] == '|')
					System.out.print(Purple + '|' + Normal);
				else if (World [x][y] == '*')
					System.out.print(Purple + '*' + Normal);
				else if (World [x][y] == '$')
					System.out.print(Blue + '$' + Normal);
				else
					System.out.print(World[x][y]);

				// optionally put a space
				if (x< 40) System.out.print(" ");
			}
			System.out.println();
		}
	}

	void MoveRight()
	{
		if (World[Xpos+1][Ypos] == ' ')
		{
			World[Xpos][Ypos] = ' ';
			Xpos++;
			World[Xpos][Ypos] = Avatar;
			}
		}
	void MoveLeft()
		{
			if (World[Xpos-1][Ypos] == ' ')
			{
				World[Xpos][Ypos] = ' ';
				Xpos--;
				World[Xpos][Ypos] = Avatar;
			}
		}
	void MoveUp()
	{
		if (World[Xpos][Ypos-1] == ' ')
		{
			World[Xpos][Ypos] = ' ';
			Ypos--;
			World[Xpos][Ypos] = Avatar;
		}
	}

	void MoveDown()
	{
		if (World[Xpos][Ypos+1] == ' ')
		{
			World[Xpos][Ypos] = ' ';
			Ypos++;
			World[Xpos][Ypos] = Avatar;
		}
	}
}
	// finish moveleft , move up , move down
class Enemy extends GameObject
{
	String Type;
	int HP , Attack , Armor , Speed , Range;

	Enemy(String theType)
	{
		Type = theType;
		Xpos = (int)(Math.random()*38)+2;
		Ypos = (int)(Math.random()*18)+2;
		if (Type.equals("Hell Demon"))
		{	Avatar='H' ; HP=100; Range=10; Attack=10; Range=12;}
		if (Type.equals("Baron of Hell "))
		{	Avatar='B' ; HP=150; Range=5; Attack=25; Range=6;}
		World[Xpos][Ypos] = Avatar;

	}

}

class Player extends GameObject
{

		static String Red = "\033[31;1m";

	String Name;
	int HP , Attack , Armor , Gold;

	Player ( String theName , char theAvatar)
	{
		HP = 100 ; Attack=25;
		Name = theName;
		Avatar = theAvatar;
		Xpos=2; Ypos=2;
		// set world
		for(int x=1 ; x<=40; x++)
			for (int y=1; y<=20; y++)
			World[x][y] = ' ';
		World[Xpos][Ypos] = Avatar;
		// line world with trees
		for (int x=1 ; x<=40 ; x++)
		{ World[x][1] = '#'; World[x][20] = '#';}


		for (int y=1 ; y<=20 ; y++)
		{ World[1][y] = '#'; World[40][y] = '#';}
		//add items
		World[(int)(Math.random()*38)+2][(int)(Math.random()*18)+2] = '+';
		World[(int)(Math.random()*38)+2][(int)(Math.random()*18)+2] = '+';
		World[(int)(Math.random()*38)+2][(int)(Math.random()*18)+2] = '+';
		World[(int)(Math.random()*38)+2][(int)(Math.random()*18)+2] = '+';
		World[(int)(Math.random()*38)+2][(int)(Math.random()*18)+2] = '+';
		World[(int)(Math.random()*38)+2][(int)(Math.random()*18)+2] = '@';
		World[(int)(Math.random()*38)+2][(int)(Math.random()*18)+2] = '@';
		World[(int)(Math.random()*38)+2][(int)(Math.random()*18)+2] = '@';
		World[(int)(Math.random()*38)+2][(int)(Math.random()*18)+2] = '@';
		World[(int)(Math.random()*38)+2][(int)(Math.random()*18)+2] = '@';

		World[3][2] = '+';
		World[2][3] = '@';


		int a = 18;
		int b = 8;
		World[a][b] = '-';
		World[a+1][b] = '-';
		World[a+2][b] = '-';
		World[a+3][b] = '-';
		World[a+4][b] = '-';
		World[a][b+1] = '|';
		World[a+1][b+1] = '*';
		World[a+2][b+1] = '*';
		World[a+3][b+1] = '*';
		World[a+4][b+1] = '|';
		World[a][b+3] = '|';
		World[a+1][b+3] = '*';
		World[a+2][b+3] = '*';
		World[a+3][b+3] = '*';
		World[a+4][b+3] = '|';
		World[a][b+4] = '-';
		World[a+1][b+4] = '-';
		World[a+2][b+4] = '-';
		World[a+3][b+4] = '-';
		World[a+4][b+4] = '-';
		World[a+2][b+2] = '$';



		World[8][3] = '|';
		World[8][2] = '|';
		World[8][4] = '|';
		World[8][5] = '|';
		World[8][6] = '|';
		World[8][7] = '|';
		World[8][8] = '|';
		World[8][9] = '|';

		World[2][15] = '-';
		World[3][15] = '-';
		World[4][15] = '-';
		World[5][15] = '-';
		World[6][15] = '-';
		World[7][15] = '-';
		World[8][15] = '-';
		World[9][15] = '-';
		World[10][15] = '-';
		World[11][15] = '-';

		World[30][2] = '|';
		World[30][3] = '|';
		World[30][4] = '|';
		World[30][5] = '|';
		World[30][6] = '|';
		World[30][7] = '|';
		World[30][8] = '|';
		World[30][9] = '|';
		World[30][10] = '|';
		World[30][11] = '|';
		World[30][12] = '|';

		World[39][18] = '-';
		World[38][18] = '-';
		World[37][18] = '-';
		World[36][18] = '-';
		World[35][18] = '-';
		World[34][18] = '-';
		World[33][18] = '-';
		World[32][18] = '-';
		World[31][18] = '-';
		World[30][18] = '-';

		}


	void MoveRight()
	{
		if (World[Xpos+1][Ypos] == ' ' || World[Xpos+1][Ypos] == '+' || World[Xpos+1][Ypos] == '@' || World[Xpos+1][Ypos] == '$')
		{
			if (World[Xpos+1][Ypos] == '+')
				HP+=25;
			if (World[Xpos+1][Ypos] == '@')
				Attack+=25;
			if (World[Xpos+1][Ypos] == '$')
				Attack += 50; HP+=50;

		World[Xpos][Ypos] = ' ';
		Xpos++;
		World[Xpos][Ypos] = Avatar;
		}
	}
	void MoveLeft()
	{
		if (World[Xpos-1][Ypos] == ' '|| World[Xpos-1][Ypos] == '+' || World[Xpos-1][Ypos] == '@' || World[Xpos-1][Ypos] == '$')
		{
			if (World[Xpos-1][Ypos] == '+')
				HP+=25;
			if (World[Xpos-1][Ypos] == '@')
				Attack+=25;
			if (World[Xpos-1][Ypos] == '$')
				Attack+=50; HP+=50;

			World[Xpos][Ypos] = ' ';
			Xpos--;
			World[Xpos][Ypos] = Avatar;
		}
	}
	void MoveUp()
	{
		if (World[Xpos][Ypos-1] == ' '|| World[Xpos][Ypos-1] == '+' || World[Xpos][Ypos-1] == '@' || World[Xpos][Ypos-1] == '$')
		{
			if (World[Xpos][Ypos-1] == '+')
				HP+=25;
			if (World[Xpos][Ypos-1] == '@')
				Attack+=25;
			if (World[Xpos][Ypos-1] == '$')
				Attack+=50; HP+=50;

			World[Xpos][Ypos] = ' ';
			Ypos--;
			World[Xpos][Ypos] = Avatar;
		}
	}
	void MoveDown()
	{
		if (World[Xpos][Ypos+1] == ' '|| World[Xpos][Ypos+1] == '+' || World[Xpos][Ypos+1] == '@'|| World[Xpos][Ypos+1] == '$')
		{
			if (World[Xpos][Ypos+1] == '+')
				HP+=25;
			if (World[Xpos][Ypos+1] == '@')
				Attack+=25;
			if (World[Xpos][Ypos+1] == '$')
				Attack+=50; HP+=50;

			World[Xpos][Ypos] = ' ';
			Ypos++;
			World[Xpos][Ypos] = Avatar;
		}
	}
}
class IntroScreen
{

		static String CLS = "\033[2J\033[1;1H";
		static String Red = "\033[31;1m";
		static String Green = "\033[32;1m";
		static String Yellow = "\033[33;1m";
		static String Blue = "\033[34;1m";
		static String Purple = "\033[35;1m";
		static String Cyan = "\033[36;1m";
		static String White = "\033[37;1m";
		static String Normal = "\033[0m";

		IntroScreen()
		{
System.out.println(Red +"=================     ===============     ===============   ========  ========"		+  	"	Objective:");
System.out.println("\\\\ . . . . . . .\\\\   //. . . . . . .\\\\   //. . . . . . .\\\\  \\\\. . .\\\\// . . //"	+Yellow+	"	-Destory All Of Hell"+Red);
System.out.println("||. . ._____. . .|| ||. . ._____. . .|| ||. . ._____. . .|| || . . .\\/ . . .||"			+White+	"	Hell Demon(H): HP:100 , Attack:10"+Red);
System.out.println("|| . .||   ||. . || || . .||   ||. . || || . .||   ||. . || ||. . . . . . . ||"			+White+	"	Baron of Hell (B): HP:150 , Attack:25"+Red);
System.out.println("||. . ||   || . .|| ||. . ||   || . .|| ||. . ||   || . .|| || . | . . . . .||"			+Purple+	"	Press Any Key To Start"+Red);
System.out.println("|| . .||   ||. "+Yellow+"_-|| ||-_"+Red+" .||   ||. . || || . .||   ||. "+Yellow+"_-|| ||-_"+Red+".|\\ . . . . ||"+Red);
System.out.println("||. . ||"+Yellow+"   ||-'  || ||  `-"+Red+"||   || . .|| ||. . ||   ||"+Yellow+"-'  || ||  `|\\_"+Red+" . .|. .||"+Red);
System.out.println("|| . _"+Yellow+"||   ||    || ||    ||"+Red+"   ||_ . || || . "+Yellow+"_||   ||    || ||   |\\ `-"+Red+"_/| . ||"+Red);
System.out.println("||_"+Yellow+"-' ||  .|/    || ||    \\|"+Red+".  || `-_|| ||"+Yellow+"_-' ||  .|/    || ||   | \\  / |-_"+Red+".||"+Red);
System.out.println(Yellow+"||    ||_-'      || ||      `-_||    || ||    ||_-'      || ||   | \\  / |  `||");
System.out.println("||    `'         || ||         `'    || ||    `'         || ||   | \\  / |   ||");
System.out.println("||"+White+"            .===' `===."+Yellow+"         "+White+".==='.`===.         ."+White+"===' /==."+Yellow+" |  \\/  |   ||"+Yellow);
System.out.println("||"+White+"         .=='   \\_|-_ `==="+Yellow+". ."+White+"==='   _|_   `===. ."+White+"===' _-|/   `=="+Yellow+"  \\/  |   ||"+Yellow);
System.out.println("||"+White+"      .=='    _-'    `-_  `='    _-'   `-_    `='  _-'   `-_  /|  \\/"+Yellow+"  |   ||"+Yellow);
System.out.println("||"+White+"   .=='    _-'          `-__\\._-'         `-_./__-'         `' |. /|  |   ||"+Yellow);
System.out.println("||"+White+".=='    _-'                                                     `' |  /==.||"+Yellow);
System.out.println(White+"=='    _-'                                                            \\/   `==");
System.out.println("\\   _-'                                                                `-_   /");
System.out.println(" `''                                                                      ``'");




	}
}
class LoserScreen
{

		static String CLS = "\033[2J\033[1;1H";
		static String Red = "\033[31;1m";
		static String Green = "\033[32;1m";
		static String Yellow = "\033[33;1m";
		static String Blue = "\033[34;1m";
		static String Purple = "\033[35;1m";
		static String Cyan = "\033[36;1m";
		static String White = "\033[37;1m";
		static String Normal = "\033[0m";

		LoserScreen()
		{
			Scanner in = new Scanner(System.in);

System.out.println(Red+" __     __                    _____    _              _ ");
System.out.println(" \\ \\   / /                   |  __ \\  (_)            | |");
System.out.println("  \\ \\_/ /    ___    _   _    | |  | |  _    ___    __| |");
System.out.println("   \\   /    / _ \\  | | | |   | |  | | | |  / _ \\  / _` |");
System.out.println("    | |    | (_) | | |_| |   | |__| | | | |  __/ | (_| |");
System.out.println("    |_|     \\___/   \\__,_|   |_____/  |_|  \\___|  \\__,_|"+Normal);


System.out.println(Yellow+"  _______                                               _         		");
System.out.println(" |__   __|                      /\\                     (_)        		");
System.out.println("    | |     _ __   _   _       /  \\      __ _    __ _   _   _ __  		");
System.out.println("    | |    | '__| | | | |     / /\\ \\    / _` |  / _` | | | | '_ \\	");
System.out.println("    | |    | |    | |_| |    / ____ \\  | (_| | | (_| | | | | | | |     	");
System.out.println("    |_|    |_|     \\__, |   /_/    \\_\\  \\__, |  \\__,_| |_| |_| |_|     ");
System.out.println("                    __/ |                __/ |                    		");
System.out.println("                   |___/                |___/                     		"+Normal);


	}
}



class WinningScreen
{

		static String CLS = "\033[2J\033[1;1H";
		static String Red = "\033[31;1m";
		static String Green = "\033[32;1m";
		static String Yellow = "\033[33;1m";
		static String Blue = "\033[34;1m";
		static String Purple = "\033[35;1m";
		static String Cyan = "\033[36;1m";
		static String White = "\033[37;1m";
		static String Normal = "\033[0m";

		WinningScreen()
		{
			Scanner in = new Scanner(System.in);

System.out.println(Cyan+"____    ____  ______    __    __     ____    __    ____  ______   .__   __. 		");
System.out.println("\\   \\  /   / /  __  \\  |  |  |  |    \\   \\  /  \\  /   / /  __  \\  |  \\ |  | 	");
System.out.println(" \\   \\/   / |  |  |  | |  |  |  |     \\   \\/    \\/   / |  |  |  | |   \\|  | 		");
System.out.println("  \\_    _/  |  |  |  | |  |  |  |      \\            /  |  |  |  | |  . `  | 		");
System.out.println("    |  |    |  `--'  | |  `--'  |       \\    /\\    /   |  `--'  | |  |\\   | 		");
System.out.println("    |__|     \\______/   \\______/         \\__/  \\__/     \\______/  |__| \\__| 		");
System.out.println("                                                                            		"+Normal);

	}
}
