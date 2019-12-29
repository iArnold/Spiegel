/*
 * @(#)Spiegel.java	1.0
 */
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
/**
 * Spiegelpuzzel applicatie gebaseerd op de applet uit 2010.
 *
 * @version 	1.0, 29 Dec 2019
 * @author Arnold van Hofwegen
 * Copyright 2019 Arnold van Hofwegen
 */
public
class Spiegel extends Frame implements MouseListener {

	// Constante waarden
    public static final int SIZE = 30;
	int xoff = SIZE;
	int yoff = SIZE;
	int margeb = 50;
	int margel = 50;
	static Random randGen = new Random();

	// Spelbord en game informatie
	int[][] bord = new int[10][10];
	int[] spiegels = new int[64];
	int[] orientatie = new int[17];
	int[] randwaarden = new int[32];
	// Omdat ik niet altijd linksboven 1, 2, 3 wil zien wordt dit random ingesteld
	int[] rand16 = new int[16];
	// Aantal door de speler geplaatste spiegels
	int geplaatst = 0;

	// Boodschap naar de speler
	public String message;
    private Font font1 = new Font("serif", Font.PLAIN, 12);

    /**
     * Initialiseer het applet.
     */
    public Spiegel() { // dit was de init() is nu constructor.
      // Toegevoegd layout zetten
      //setLayout(new FlowLayout());
      // Toegevoegd setXXX functies
      setTitle("Spiegels plaatsen");
      setSize(400, 400);
      setVisible(true);
      //setLocation(80, 80);
  	  // Initialiseren bord
  		for ( int i = 0; i < 10 ; i++) {
  			for (int j = 0; j < 10 ; j++){
  			    bord[i][j] = 0;
  			}
  		}
  	    // Maken opgave
  		// Doordat de message niet gevuld werd ging het tonen van de waardes
  		// langs de rand ook fout.
  		message = "";
  		maakbord(17, 64);
  	    //
  		addMouseListener(this);
    }

    public void destroy() {
        removeMouseListener(this);
    }

    /**
     * Paint it.
     */
    public void paint(Graphics g) {
		Dimension d = getSize();

		g.setColor(Color.black);
		for (int l = 0 ; l < 9 ; l++) {
     		// Verticale lijnen
			g.drawLine(l*xoff + margel, margel, l*xoff + margel, 8 * xoff + margel);
			// Horizontale lijnen
	        g.drawLine(margeb, l*yoff + margeb, 8 * yoff + margeb, l*yoff + margeb);
		}
		g.setColor(Color.blue);
        g.setFont(font1);
        g.drawString(message , 20, d.height - 5);
		//
		for (int i = 1 ; i < 9 ; i++){
			for (int j = 1 ; j < 9 ; j++) {
				if (bord[j][i] == 1 << 2){
					g.setColor(Color.blue);
					g.drawLine(margel + (i-1) * SIZE, margeb + (j-1) * SIZE, margel + (i) * SIZE, margeb + (j) * SIZE);
				}
				if (bord[j][i] == 1 << 3){
					g.setColor(Color.blue);
					g.drawLine(margel + (i-1) * SIZE, margeb + (j) * SIZE, margel + (i) * SIZE, margeb + (j-1) * SIZE);
				}
				if (bord[j][i] == ((1 << 2) + (1 << 1))){
					g.setColor(Color.black);
					g.drawLine(margel + (i-1) * SIZE, margeb + (j-1) * SIZE, margel + (i) * SIZE, margeb + (j) * SIZE);
				}
				if (bord[j][i] == (1 << 3) + (1 << 1)){
					g.setColor(Color.black);
					g.drawLine(margel + (i-1) * SIZE, margeb + (j) * SIZE, margel + (i) * SIZE, margeb + (j-1) * SIZE);
				}
			}
		}

		// Zet de cijfers rond het bord
		g.setColor(Color.black);
		for (int i=1; i < 9 ; i++){
			g.drawString("" + bord[i][0] ,2 ,margeb + i * SIZE);
			g.drawString("" + bord[i][9] ,margel + 8 * SIZE + 5, margeb + i * SIZE);
		}
		for (int i=1; i < 9 ; i++){
			g.drawString("" + bord[0][i] , margel + (i-1) * SIZE + 5, margeb - 2);
			g.drawString("" + bord[9][i] , margel + (i-1) * SIZE + 5, margeb + 9 * SIZE);
		}

    }
	/**
	 * Random routine om 17 spiegel op 64 vakken te kiezen
	 */
	public void maakbord (int intKies, int intUit) {
		int waarde, intswap;
		int startr, startc;
		int stepr, stepc;
		// Bepaal random de spiegelwaarden
		for (int i=0; i < intUit; i++)
		{
			 spiegels[i] = i;
		}

		for (int i=0; i < intUit; i++) {
			waarde = i + randGen.nextInt(intUit - i);
			intswap = spiegels[waarde];
			spiegels[waarde] = spiegels[i];
			spiegels[i] = intswap;
		};

		for (int i=0; i<intKies ; i++) {
			orientatie[i] = randGen.nextInt(2);
		}
		// De 10 vaste spiegels
		for (int i=0; i<10 ; i++) {
			int intr = (spiegels[i] % 8) + 1;
			int intc = (int) (spiegels[i] / 8) + 1;
			if (orientatie[i] == 0){
			   bord[intr][intc] = (1 << 2) + (1 << 1);
			} else {
			   bord[intr][intc] = (1 << 3) + (1 << 1);
			}
		}
		// De 7 tijdelijke spiegels
		for (int i=10; i<intKies ; i++) {
			int intr = (spiegels[i] % 8) + 1;
			int intc = (int) (spiegels[i] / 8) + 1;
			if (orientatie[i] == 0){
				bord[intr][intc] = (1 << 2);
			} else {
				bord[intr][intc] = (1 << 3);
			}
		}
		// Bepaal de cijfers langs de rand
		for (int i = 0 ; i < 32 ; i++) {
			randwaarden[i] = 0;
		}

		random16(16, 16);
		int intTel = 0;
		for (int i = 0 ; i < 32 ; i++) {
			if (randwaarden[i] != 0)
				continue;
			// naar boven (noord) is richting 0
			// naar rechts (oost) is richting 1
			// naar onder (zuid) is richting 2
			// naar links (west) is richting 3
		    randwaarden[i] = rand16[intTel] + 1;
			intTel++;
			// Stap het bord op
			int richting;
			if (i < 8) {
				startr = 0;
				startc = i + 1;
				richting = 2; // richting z
			} else if (i < 16) {
				startr = i - 7;
				startc = 9;
				richting = 3; // richting w
			} else if (i < 24) {
				startr = 9;
				startc = 24 - i;
				richting = 0; // richting n
			} else {
				startr = 32 - i;
				startc = 0;
				richting = 1; // richting o
			};
			// Sla de waarde op het bord op
			bord[startr][startc]=randwaarden[i];
			// Stap en blijf stappen volgens de regels tot
			// of de rij == 0 of de rij == 9
			// of de kolom == 0 of de kolom == 9
			stepr = startr;
			stepc = startc;
			do {
				if (richting == 2) {
					stepr+=1;
				} else if (richting == 3) {
					stepc-=1;
				} else if (richting == 0) {
					stepr-=1;
				} else {
					stepc+=1;
				}
				//Test of de richting wijzigt en zo ja waarin
				// Spiegel     slash n --> o, w --> z, z --> w, o --> n
				if (((bord[stepr][stepc]) & (1 << 3)) == 8) {
					// XOR 00 ^ 01 -> 01; 01 ^ 01 -> 00; 10 ^ 01 -> 11; 11 ^ 01 -> 10
					richting = richting ^ 1;
				}
				// Spiegel backslash z --> o, w --> n, n --> w, o --> z
				if ((bord[stepr][stepc] & (1 << 2)) == 4) {
					// XOR 00 ^ 11 -> 11; 01 ^ 11 -> 10; 10 ^ 11 -> 01; 11 ^ 11 -> 00
					richting = richting ^ 3;
				}

			} while ((stepr > 0) && (stepr < 9) && (stepc > 0) && (stepc < 9));

			bord[stepr][stepc] = randwaarden[i];
			vulrandwaarde(stepr, stepc);

		}
		// wis de tijdelijke spiegels
		for (int i=10; i<intKies ; i++) {
			int intr = (spiegels[i] % 8) + 1;
			int intc = (int) (spiegels[i] / 8) + 1;
			bord[intr][intc] = 0;
		}

	} // Einde maakbord

	/**
	 * Random routine toewijzen 16 randwaarden
	 */
	public void random16 (int intKies, int intUit) {
		int waarde, intswap;
		// Bepaal random de spiegelwaarden
		for (int i=0; i < intUit; i++)
		{
			rand16[i] = i;
		}
		for (int i=0; i < intUit; i++) {
			waarde = i + randGen.nextInt(intUit - i);
			intswap = rand16[waarde];
			rand16[waarde] = rand16[i];
			rand16[i] = intswap;
		};
	}
	// Einde random routines

	public void vulrandwaarde(int rij, int kolom) {
		int rand;
		if (rij == 0) {
			rand = kolom - 1;
		} else if (rij == 9) {
			rand = 16 + (8 - kolom);
		} else if (kolom == 0) {
			rand = 24 + (8 - rij);
		} else { // kolom == 9
			rand = 7 + rij;
		}
		randwaarden[rand] = bord[rij][kolom];
	}
//
	public boolean testconnect () {
		boolean ok = true;
		int startr, startc, stepr, stepc;
		for (int i = 0 ; i < 32 ; i++) {
			// Doe dit tot blijkt dat dit geen oplossing is
			if (!ok) continue;
			// Stap het bord op
			int richting;
			if (i < 8) {
				startr = 0;
				startc = i + 1;
				richting = 2; // richting z
			} else if (i < 16) {
				startr = i - 7;
				startc = 9;
				richting = 3; // richting w
			} else if (i < 24) {
				startr = 9;
				startc = 24 - i;
				richting = 0; // richting n
			} else {
				startr = 32 - i;
				startc = 0;
				richting = 1; // richting o
			};
			// Ga stappen en stap door tot het einde van het pad
			stepr = startr;
			stepc = startc;
			do {
				if (richting == 2) {
					stepr+=1;
				} else if (richting == 3) {
					stepc-=1;
				} else if (richting == 0) {
					stepr-=1;
				} else {
					stepc+=1;
				}
				if (((bord[stepr][stepc]) & (1 << 3)) == 8) {
					richting = richting ^ 1;
				}
				if ((bord[stepr][stepc] & (1 << 2)) == 4) {
					richting = richting ^ 3;
				}
			} while ((stepr > 0) && (stepr < 9) && (stepc > 0) && (stepc < 9));
			// Vergelijk start en eindpunt
			if (bord[startr][startc] != bord[stepr][stepc])
				ok = false;
		}
		return ok;
	}
    /**
     * De speler heeft in de applet geklikt. Uitzoeken waar dit is gedaan
     * en als dit een geldige zet oplevert voer dan de vereiste vervolgstappen
     * uit.
     */
    public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		// Bepaal de rij/kolom
		int c;
		int r;
		//message = "x-co :" + x + " y-co :" + y;
		if (x >= margel && x <= margel + 8 * SIZE && y >= margeb && y <= margeb + 8 * SIZE ) {
			//message += " dat is in het geldige bereik!";
			r = ((y - margeb) / SIZE) + 1;
			c = ((x - margel) / SIZE) + 1;
			//message += " rij " + r + " kolom " + c;
			if ((bord[r][c] & (1 << 1)) == 2) {
				message = "Deze spiegel is vast";
			} else {
			//maak een schuine streep in het gekozen vak
				if (bord[r][c] == 1 << 3){
					bord[r][c] = 1 << 2;
					message = "";
				} else if (bord[r][c] == 1 << 2 ){
					bord[r][c] = 0;
					geplaatst -= 1;
					message = "";
				} else {
					if (geplaatst < 7) {
						geplaatst += 1;
						bord[r][c] = 1 << 3;
					} else {
						message = "Er zijn al 7 spiegels bijgeplaatst.";
					}
				}
			}
		}
		// check of de oplossing is gevonden
		if ( geplaatst == 7 && testconnect()) {
			message = "Oplossing gevonden! Gefeliciteerd!";
		}
		//
		repaint();

    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    // Toegevoegd main method
    public static void main(String args[])
    {
      new Spiegel();
    }
}
