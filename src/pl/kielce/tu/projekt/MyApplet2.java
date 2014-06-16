package pl.kielce.tu.projekt;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Klasa gracza, w niej wykonywane sa przesylki, obliczenia, rowniez kolizje obiektow.
 * @author Rafal Bebenek
 * @author Adrian Bartosinski
 */
public class MyApplet2 extends JApplet implements Zmienne {

	private static final long serialVersionUID = 1L;
	static int ktoryGracz;
	String host;
	static Socket socket = null;
	static int oczekiwanie = 0;
	static Dane d1 = new Dane(250, 300, 1, 0, false);
	static Dane d2 = new Dane(350, 300, 2, 0, false);
	static int petla = 0;
	static String nieudanePolaczenie = new String();//zamiast pokazywac komunikaty ktore troche irytuja to po prostu wyswietlamy znaki # na ekranie

	/*----------------------------Strzelanie--------------------------------*/
	/**
	 * Za pomoca tej funkcji statki wystrzeliwuja pociski
	 * 
	 * @param petla
	 *            - numer wykonywanej petli, zeby mozna bylo strzelac z odpowiednia
	 *            czestotliwoscia
	 */
	public void strzal(int petla) {
		if (petla % 15 == 0) {
			if (ktoryGracz == 1) {
				int j = 0;
				while (d1.pociskStrzelono[j] == true && j < 9)
					j++;
				if (j < 10) {
					d1.pociskX[j] = d1.x;
					d1.pociskY[j] = d1.y;
					d1.pociskStrzelono[j] = true;
				}
			}
			if (ktoryGracz == 2) {
				int j = 0;
				while (d2.pociskStrzelono[j] == true && j < 9)
					j++;
				if (j < 10) {
					d2.pociskX[j] = d2.x;
					d2.pociskY[j] = d2.y;
					d2.pociskStrzelono[j] = true;
				}
			}
		}
	}

	/*------------------------Ruch pociskow--------------------------*/
	/**
	 * "Przesuwanie" pociskow po ekranie
	 */
	public void ruchPociskow() {
		for (int j = 0; j < 10; j++) {
			if (ktoryGracz == 1) {
				if (d1.pociskY[j] >= 0 && d1.pociskStrzelono[j] == true)
					d1.pociskY[j] -= 5;
				else if (d1.pociskY[j] < 0)
					d1.pociskStrzelono[j] = false;
			} else {// gracz nr 2
				if (d2.pociskY[j] >= 0 && d2.pociskStrzelono[j] == true)
					d2.pociskY[j] -= 5;
				else if (d2.pociskY[j] < 0)
					d2.pociskStrzelono[j] = false;
			}
		}
	}

	/*----------------------------Detekcja koizji-----------------------------*/
	/**
	 * Detekcja kolizji na podstawie kolizji prostokatow.
	 * 
	 * @param ktory
	 *            - ktory gracz wywolal funkcje
	 * @return Zwraca 1 jesli nastapila kolizja, a w efekcie - koniec gry dla
	 *         tego gracza
	 */
	public int kolizja(int ktory) {
		if (ktory == 1) {
			for (int i = 0; i < 10; i++) {
				if (d1.asteroidaHp[i] > 0) {
					if (d1.x <= d1.asteroidaX[i] + 20
							&& d1.x >= d1.asteroidaX[i] 
							&& d1.y <= d1.asteroidaY[i] + 20
							&& d1.y >= d1.asteroidaY[i] )
						return 1;
					if (d1.x <= d1.asteroidaX[i] + 20
							&& d1.x >= d1.asteroidaX[i] 
							&& d1.y + 48 >= d1.asteroidaY[i] 
							&& d1.y + 48 <= d1.asteroidaY[i] + 20)
						return 1;
					if (d1.x + 48 >= d1.asteroidaX[i] 
							&& d1.x + 48 <= d1.asteroidaX[i] + 20
							&& d1.y <= d1.asteroidaY[i] + 20
							&& d1.y >= d1.asteroidaY[i] )
						return 1;
					if (d1.x + 48 >= d1.asteroidaX[i] 
							&& d1.x + 48 <= d1.asteroidaX[i] + 20
							&& d1.y + 48 >= d1.asteroidaY[i] 
							&& d1.y + 48 <= d1.asteroidaY[i] + 20)
						return 1;
				}
			}
		}
		if (ktory == 2) {
			for (int i = 0; i < 10; i++) {
				if (d1.asteroidaHp[i] > 0) {
					if (d2.x <= d1.asteroidaX[i] + 20
							&& d2.x >= d1.asteroidaX[i] 
							&& d2.y <= d1.asteroidaY[i] + 20
							&& d2.y >= d1.asteroidaY[i] )
						return 1;
					if (d2.x <= d1.asteroidaX[i] + 20
							&& d2.x >= d1.asteroidaX[i] 
							&& d2.y + 48 >= d1.asteroidaY[i] 
							&& d2.y + 48 <= d1.asteroidaY[i] + 20)
						return 1;
					if (d2.x + 48 >= d1.asteroidaX[i] 
							&& d2.x + 48 <= d1.asteroidaX[i] + 20
							&& d2.y <= d1.asteroidaY[i] + 20
							&& d2.y >= d1.asteroidaY[i] )
						return 1;
					if (d2.x + 48 >= d1.asteroidaX[i] 
							&& d2.x + 48 <= d1.asteroidaX[i] + 20
							&& d2.y + 48 >= d1.asteroidaY[i] 
							&& d2.y + 48 <= d1.asteroidaY[i] + 20)
						return 1;
				}
			}
		}
		return 0;
	}

	/*------------------kolizja Pocisków z asteroidami---------------------*/
	/**
	 * Funkcja sprawdza, czy pocisk uderzyl w przeszkode.
	 * Jesli tak, to pocisk "znika" a od asteroidy odbiera sie punkty zycia.
	 * @return ...
	 */
	
	public int kolizjaPocisku() {
		boolean trafiono = false;
		if (ktoryGracz == 1) {
			for (int i = 0; i < 10; i++) {
				trafiono = false;
				
				if (d2.asteroidaHp[i]<10) d1.asteroidaHp[i]-=5;//zeby wyeliminowac blad przez ktory 2 gracz nie zestrzeliwal asteroid
				
				if (d1.asteroidaHp[i] > 0) {
					for (int j = 0; j < 10; j++) {
						trafiono=false;
						if (d1.pociskX[j] - 2 <= d1.asteroidaX[i] 
								&& d1.pociskX[j] - 2 >= d1.asteroidaX[i] - 20
								&& d1.pociskY[j] - 2 <= d1.asteroidaY[i] + 20
								&& d1.pociskY[j] - 2 >= d1.asteroidaY[i] )
							trafiono = true;
						if (d1.pociskX[j] - 2 <= d1.asteroidaX[i] 
								&& d1.pociskX[j] - 2 >= d1.asteroidaX[i] -20
								&& d1.pociskY[j] + 2 >= d1.asteroidaY[i] 
								&& d1.pociskY[j] + 2 <= d1.asteroidaY[i] +20)
							trafiono = true;
						if (d1.pociskX[j] + 2 >= d1.asteroidaX[i] -20
								&& d1.pociskX[j] + 2 <= d1.asteroidaX[i] 
								&& d1.pociskY[j] - 2 <= d1.asteroidaY[i] +20
								&& d1.pociskY[j] - 2 >= d1.asteroidaY[i] )
							trafiono = true;
						if (d1.pociskX[j] + 2 >= d1.asteroidaX[i] -20
								&& d1.pociskX[j] + 2 <= d1.asteroidaX[i]
								&& d1.pociskY[j] + 2 >= d1.asteroidaY[i] 
								&& d1.pociskY[j] + 2 <= d1.asteroidaY[i] +20)
							trafiono = true;
							
						if (trafiono) {
							d1.asteroidaHp[i] -= 5;
							d1.punkty += 5;
							d1.pociskStrzelono[j] = false;
							d1.pociskX[j]=-50;
						}
					}
				}
			}
		}
		if (ktoryGracz == 2) {
			for (int i = 0; i < 10; i++) {
				
				//d2.asteroidaHp[i]=d1.asteroidaHp[i];//
				//d2.lvl[i]=d1.lvl[i];
				d2.asteroidaHp[i]=10;
				
				trafiono = false;
				if (d1.asteroidaHp[i] > 0) {
					for (int j = 0; j < 10; j++) {
						trafiono=false;
						if (d2.pociskX[j] - 2 <= d1.asteroidaX[i] 
								&& d2.pociskX[j] - 2 >= d1.asteroidaX[i] - 20
								&& d2.pociskY[j] - 2 <= d1.asteroidaY[i] + 20
								&& d2.pociskY[j] - 2 >= d1.asteroidaY[i] )
							trafiono = true;
						if (d2.pociskX[j] - 2 <= d1.asteroidaX[i] 
								&& d2.pociskX[j] - 2 >= d1.asteroidaX[i] -20
								&& d2.pociskY[j] + 2 >= d1.asteroidaY[i] 
								&& d2.pociskY[j] + 2 <= d1.asteroidaY[i] +20)
							trafiono = true;
						if (d2.pociskX[j] + 2 >= d1.asteroidaX[i] -20
								&& d2.pociskX[j] + 2 <= d1.asteroidaX[i] 
								&& d2.pociskY[j] - 2 <= d1.asteroidaY[i] +20
								&& d2.pociskY[j] - 2 >= d1.asteroidaY[i] )
							trafiono = true;
						if (d2.pociskX[j] + 2 >= d1.asteroidaX[i] -20
								&& d2.pociskX[j] + 2 <= d1.asteroidaX[i]
								&& d2.pociskY[j] + 2 >= d1.asteroidaY[i] 
								&& d2.pociskY[j] + 2 <= d1.asteroidaY[i] +20)
							trafiono = true;

						if (trafiono) {
							d2.asteroidaHp[i] -= 5;
							d2.punkty += 5;
							d2.pociskStrzelono[j] = false;
							d2.pociskX[j]=-50;
						}
					}
				}
			}
		}
		return 0;
	}

	/*----------------------------Przesylanie obiektow------------------------*/
	/**
	 * Cyklicznie wywolywana funkcja. Przesyla obiekty do serwera, a potem je
	 * odbiera.
	 * 
	 * @return ...
	 */
	public int przesylka() {
		// String host = this.getDocumentBase().getHost();
		// Socket socket;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		int zwroconyKod = 0;
		try {// Przesy�ka obiekt�w na porcie 54322
				// socket = new Socket(host, 54322);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			if (ktoryGracz == 1) {
				zwroconyKod = kolizja(1);
				if (zwroconyKod != 0)
					d1.koniecGry = true;
				else
					d1.koniecGry = false;

				out.writeObject(d1);
				// out.flush();
			}

			if (ktoryGracz == 2) {
				zwroconyKod = kolizja(2);
				if (zwroconyKod != 0)
					d2.koniecGry = true;
				else
					d2.koniecGry = false;

				out.writeObject(d2);
				// out.flush();
			}

			d1 = (Dane) in.readObject();
			d2 = (Dane) in.readObject();

			// ========================
			// socket.close();

			if (d1.koniecGry || d2.koniecGry) { // gdy ktorys z graczy zakonczy gre
				String info = new String();
				if (ktoryGracz==1) {
					if (d1.punkty > d2.punkty) info = "Wygrales";
					else if (d1.punkty <d2.punkty) info = "Przegrales";
					else info = "Remis";
					
					info += ", zdobyte punkty: " + d1.punkty;
				}
				else {
					if (d2.punkty > d1.punkty) info = "Wygrales";
					else if (d2.punkty <d1.punkty) info = "Przegrales";
					else info = "Remis";
					
					info += ", zdobyte punkty: " + d2.punkty;
				}
				
				JOptionPane.showMessageDialog(this, "Koniec Gry!\n" + info, "APLET", 0);
				socket.close();
				return 0;
			}
			// }
		} catch (Exception e) {
			// JOptionPane.showMessageDialog(this,
			// "Nie udalo sie polaczyc z serwerem", "SERWER", 0);
			// System.out.println("Nieudane polaczenie z serwerem - gracz " +
			// ktoryGracz);
			// e.printStackTrace();
			//if (!nieudanePolaczenie
			//		.equals("##########################################")) {
				nieudanePolaczenie += "#";
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				//}
			}
			if (nieudanePolaczenie.length() >=50) {
				JOptionPane.showMessageDialog(this,"Drugi gracz ma problem z siecia lub polaczenie zostalo zerwane\nGra zostanie zamknieta", "APLET", 0);
				System.exit(1);
			}
		}
		return 0;
	}

	/**
	 * Inicjalizacja i cykliczne wywolywanie innych funkcji, zeby gra odbywala sie z okreslona predkoscia.
	 * 
	 * @see java.applet.Applet#init()
	 */
	@Override
	/*-----------------------------Inicjalizacja------------------------------*/
	public void init() {
		JFrame f = new JFrame();
		f.setTitle("Space Shooter");
		f.setSize(BOARD_WIDTH, BOARD_HEIGTH);
		f.add(new MyBoard());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setLocation(200,200);
		f.setVisible(true);

		// ====
		String host = this.getDocumentBase().getHost();
		this.host = host;
		try {// Wyslanie na port 54321 informacji o przy�aczeniu si� do gry
			socket = new Socket(host, 54321);
			socket.getOutputStream().write(12345);
			ktoryGracz = (socket.getInputStream().read());// Pobranie nr gracza
			// while(oczekiwanie !=1111) {
			oczekiwanie = socket.getInputStream().read();// Pobranie
															// potwierdzenia o
															// przy��czeniu
															// si� innego
															// gracza
			// }
			socket.close();
			repaint();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Nie udalo sie polaczyc z serwerem", "APLET", 0);
			e.printStackTrace();
		}

		try {
			socket = new Socket(host, 54322);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// /////////////////////////////////

		class MyTask extends TimerTask {
			@Override
			public void run() {
				// System.err.print("#");
				petla++;
				strzal(petla);
				ruchPociskow();
				kolizjaPocisku();
				if (!d1.koniecGry && !d2.koniecGry)
					przesylka();
				else {
					System.exit(1); // gdy gra sie zakonczy,
									// zamknij aplikacje
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (nieudanePolaczenie.length()>=100) {
					JOptionPane.showMessageDialog(null,"Inny gracz przestal grac lub polaczenie zostalo zerwane\nGra zostanie zamknieta", "APLET", 0);
					System.exit(1);
				}
			}
		}

		MyTask task = new MyTask();
		Timer timer = new Timer();
		timer.schedule(task, 0, 50);
	}
}