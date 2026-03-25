package chenille.ihm;

import chenille.Anneau;
import chenille.Chenille;
import chenille.Tete;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChenilleGUI extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private Chenille chenille;
    private ChenillePanel panel;
    private Timer timer;
    private JLabel positionLabel; // Label pour afficher la position


    public static void main(String[] args) {
        final int nbAnneaux = 5; // sans la tete
        final int xTete = 10;
        final int yTete = 10;

        SwingUtilities.invokeLater(() -> {
            ChenilleGUI gui = new ChenilleGUI(nbAnneaux, xTete, yTete);
            gui.setVisible(true);
        });
    }

    public ChenilleGUI(int nbAnneaux, int xTete, int yTete) {
        chenille = new Chenille(5, 10, 10);

        setTitle("Chenille Simulation");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel = new ChenillePanel();
        add(panel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start");
        positionLabel = new JLabel("Position: (10, 10)"); // Initialisation du label

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer == null || !timer.isRunning()) {
                    timer = new Timer(100, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            chenille.deplacer(panel.getWidth() / 20, panel.getHeight() / 20);
                            panel.repaint();
                            // Mise à jour de la position de la tête
                            Tete tete = chenille.tete();
                            positionLabel.setText("Position: (" + tete.x() + ", " + tete.y() + ")");
                        }
                    });
                    timer.start();
                    startButton.setText("Stop");
                } else {
                    timer.stop();
                    startButton.setText("Start");
                }
            }
        });

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(startButton, BorderLayout.EAST);
        buttonPanel.add(positionLabel, BorderLayout.WEST); // Ajout du label à gauche
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class ChenillePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int taille = 20;
            int tailleAnneau = taille;           // taille d'un anneau
            int headSize = (int)(taille * 1.3);  // tete 30% plus large

            // on dessine d'abord les anneaux
            g.setColor(Color.GREEN);
            for (Anneau anneau : anneaux()) {
                int x = anneau.x() * taille;
                int y = anneau.y() * taille;
                g.fillOval(x, y, tailleAnneau, tailleAnneau);
                g.setColor(Color.BLACK);
                g.drawOval(x, y, tailleAnneau, tailleAnneau);
                g.setColor(Color.GREEN);  // reset pour le prochain anneau
            }

            // On dessine la tete en dernier et en rouge pour la rendre bien visible
            g.setColor(Color.RED);
            int headX = tete().x() * taille;
            int headY = tete().y() * taille;
            // centrer sur la tete (qui est plus grosse)
            headX -= (headSize - tailleAnneau) / 2;
            headY -= (headSize - tailleAnneau) / 2;
            g.fillOval(headX, headY, headSize, headSize);
            g.setColor(Color.BLACK);
            g.drawOval(headX, headY, headSize, headSize);
        }

        private Tete tete() { return chenille.tete(); }
        private List<Anneau> anneaux() { return chenille.anneaux(); }
    }

}