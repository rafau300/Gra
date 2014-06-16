package pl.kielce.tu.projekt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

/**
 * W tej klasie rysujemy na ekranie i obslugujemy zdarzenia np. klikniecia mysza czy nacisniecia poszczegolnych klawiszy
 * @see javax.swing.JComponent
 * @author Rafal Bebenek
 * @author Adrian Bartosinski
 */

class MyBoard extends JComponent {

	private static final long serialVersionUID = 1L;
	static int pozycjaElementow=-50;

	MyApplet2 aplet = new MyApplet2();
	// wczytywanie obrazow
	Image tlo = Toolkit.getDefaultToolkit().createImage("obrazy/space.jpg");
	Image statek1 = Toolkit.getDefaultToolkit().createImage(
			"obrazy/statek1.png");
	Image statek2 = Toolkit.getDefaultToolkit().createImage(
			"obrazy/statek2.png");
	Image asteroida1 = Toolkit.getDefaultToolkit().createImage("obrazy/asteroida1.png"); 
	Image mars = Toolkit.getDefaultToolkit().createImage("obrazy/mars.png");

	// String host =aplet.host;
	/*---------------------------------Obsluga myszy----------------------------------------*/
	/**
	 * Pole, po ktorym poruszaja sie gracze
	 */
	public MyBoard() {
		addMouseListener(new MouseAdapter() {
			/**
			 * Obsluga zdarzen myszy
			 * 
			 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
			 */
			@SuppressWarnings("static-access")
			public void mousePressed(MouseEvent e) {
				// repaint();
				if (aplet.ktoryGracz == 1) {
					aplet.d1.x = e.getX() - 24;
					aplet.d1.y = e.getY() - 24;
					// aplet.przesylka();
					// repaint();
				}
				if (aplet.ktoryGracz == 2) {
					aplet.d2.x = e.getX() - 24;
					aplet.d2.y = e.getY() - 24;
					// aplet.przesylka();
					// repaint();

				}
			}
		});

		/*------------------------Obsluga klawiatury--------------------------------*/
		JComponent jc = (JComponent) this; // potrzebne do obslugi keyListenera
		jc.setFocusable(true); // bez tego keyListener nie bedzie chodzic
		jc.addKeyListener(new KeyAdapter() {
			/**
			 * Obsluga klawiatury
			 * 
			 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
			 */
			@SuppressWarnings("static-access")
			@Override
			public void keyPressed(KeyEvent e) {
				int klawisz = e.getKeyCode();

				if (klawisz == KeyEvent.VK_LEFT) {
					if (aplet.ktoryGracz == 1 && aplet.d1.x>0) 
						aplet.d1.x -= 5;
					else if (aplet.ktoryGracz == 2 && aplet.d2.x>0)
						aplet.d2.x -= 5;
				} else if (klawisz == KeyEvent.VK_RIGHT) {
					if (aplet.ktoryGracz == 1  && aplet.d1.x<592)
						aplet.d1.x += 5;
					else if (aplet.ktoryGracz == 2 && aplet.d2.x<592) 
						aplet.d2.x += 5;
				} else if (klawisz == KeyEvent.VK_UP) {
					if (aplet.ktoryGracz == 1 && aplet.d1.y>0)
						aplet.d1.y -= 5;
					else if (aplet.ktoryGracz==2 && aplet.d2.y>0)
						aplet.d2.y -= 5;
				} else if (klawisz == KeyEvent.VK_DOWN) {
					if (aplet.ktoryGracz == 1 && aplet.d1.y<460)
						aplet.d1.y += 5;
					else if (aplet.ktoryGracz==2 && aplet.d2.y<460)
						aplet.d2.y += 5;
				}
			}

			@SuppressWarnings("static-access")
			@Override
			public void keyReleased(KeyEvent e) {
				int klawisz = e.getKeyCode();

				if (klawisz == KeyEvent.VK_LEFT) {
					if (aplet.ktoryGracz == 1)
						aplet.d1.x -= 0;
					else
						aplet.d2.x -= 0;
				} else if (klawisz == KeyEvent.VK_RIGHT) {
					if (aplet.ktoryGracz == 1)
						aplet.d1.x += 0;
					else
						aplet.d2.x += 0;
				} else if (klawisz == KeyEvent.VK_UP) {
					if (aplet.ktoryGracz == 1)
						aplet.d1.y -= 0;
					else
						aplet.d2.y -= 0;
				} else if (klawisz == KeyEvent.VK_DOWN) {
					if (aplet.ktoryGracz == 1)
						aplet.d1.y += 0;
					else
						aplet.d2.y += 0;
				}
			}
		});
	}

	/**
	 * Rysowanie na ekranie
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@SuppressWarnings("static-access")
	@Override
	public void paintComponent(Graphics g) {
		/*----------------------------Rysowanie na ekranie----------------------------*/
		//if (aplet.petla%10==0) pozycjaElementow++;
		//if (pozycjaElementow>=600) pozycjaElementow=-100;
		
		g.setColor(Color.black);
		g.fillRect(0, 0, 640, 580);
		g.drawImage(tlo, 0, 0, null);
		//g.drawImage(mars, 100, pozycjaElementow, null);

		// rysowanie asteroid
		for (int i = 0; i < 10; i++) {
			if (aplet.d1.asteroidaX[i] >= -50 && aplet.d1.asteroidaHp[i] > 0) {
				switch (aplet.d1.lvl[i]) {
				case 1: g.drawImage(asteroida1,aplet.d1.asteroidaX[i], aplet.d1.asteroidaY[i], aplet);
					break;
				case 2: g.setColor(Color.YELLOW);
					break;
				case 3: g.setColor(Color.ORANGE);
					break;
				default: g.setColor(Color.RED);
				}
				//g.fillRect(aplet.d1.asteroidaX[i], aplet.d1.asteroidaY[i], 20,20);
			}
		}

		// rysowanie statkow
		g.drawImage(statek1, aplet.d1.x, aplet.d1.y, 48, 48, null);
		g.drawImage(statek2, aplet.d2.x, aplet.d2.y, 48, 48, null);

		g.setColor(Color.GREEN);
		for (int i = 0; i < 10; i++) {
			if (aplet.d1.pociskStrzelono[i] == true) {
				g.setColor(Color.ORANGE);
				g.fillRect(aplet.d1.pociskX[i] + 20, aplet.d1.pociskY[i], 4, 4);
			}
			if (aplet.d2.pociskStrzelono[i] == true) {
				g.setColor(Color.CYAN);
				g.fillRect(aplet.d2.pociskX[i] + 20, aplet.d2.pociskY[i], 4, 4);
			}
		}

		g.setColor(Color.BLACK);
		g.fillRect(0, 480, 640, 100);

		//Wypisywanie informacji tekstowych
		g.setColor(Color.white);
		if (aplet.ktoryGracz == 0)
			g.drawString("Nieudane polaczenie z serwerem", 1, 20);
		else {
			g.drawString("Nr gracza: ", 300, 500);
			g.drawString("" + aplet.ktoryGracz, 320, 520);
			g.drawString("" + aplet.d1.punkty, aplet.d1.x + 40, aplet.d1.y);
			g.drawString("" + aplet.d2.punkty, aplet.d2.x + 40, aplet.d2.y);
		}

		g.setColor(Color.RED);
		if (aplet.nieudanePolaczenie != null)
			g.drawString(aplet.nieudanePolaczenie, 170, 530);

		super.repaint();
	}
}
