package desarrolloDeInterfaces;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

public class D_I_DanielSchwab_MiniPaint extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel lienzo;

	// Opciones actuales del Paint
	private String forma = "Libre";
	private Color color = Color.BLACK;
	private int grosor = 3;

	// Imagen donde se guarda el dibujo (el lienzo solo la enseña en pantalla)
	private BufferedImage imagen = new BufferedImage(2000, 1500, BufferedImage.TYPE_INT_ARGB);

	// Punto donde se pulsó el ratón (en el dibujo libre, el último punto dibujado)
	private int x1, y1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					D_I_DanielSchwab_MiniPaint frame = new D_I_DanielSchwab_MiniPaint();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public D_I_DanielSchwab_MiniPaint() {
		setTitle("Mini Paint - " + forma);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		// ---------- LIENZO ----------
		// Sobrescribimos paintComponent para que el panel pinte nuestra imagen.
		// No se llama a mano: Swing lo llama cada vez que hacemos lienzo.repaint()
		lienzo = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(imagen, 0, 0, null);
			}
		};
		lienzo.setBackground(Color.WHITE);
		contentPane.add(lienzo, BorderLayout.CENTER);

		// ---------- OYENTES (los usan a la vez el menú y la barra) ----------
		// getActionCommand() devuelve el texto del botón pulsado: "Libre", "Línea", "Círculo" o "Cuadrado"
		ActionListener oyenteForma = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				forma = e.getActionCommand();
				setTitle("Mini Paint - " + forma);
			}
		};

		ActionListener oyenteColor = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color nuevo = JColorChooser.showDialog(contentPane, "Elige el color", color);
				if (nuevo != null) { // null = se ha pulsado Cancelar
					color = nuevo;
				}
			}
		};

		ActionListener oyenteGrosor = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String texto = JOptionPane.showInputDialog(contentPane, "Ancho de la brocha (px):", grosor);
					if (texto != null) { // null = se ha pulsado Cancelar
						grosor = Math.max(1, Integer.parseInt(texto)); // como mínimo 1
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, "Escribe un número", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		};

		// ---------- MENÚ Y BARRA DE HERRAMIENTAS ----------
		JMenuBar jmb = new JMenuBar();
		setJMenuBar(jmb);
		JMenu mForma = new JMenu("Forma");
		JMenu mBrocha = new JMenu("Brocha");
		jmb.add(mForma);
		jmb.add(mBrocha);

		JToolBar barra = new JToolBar();
		contentPane.add(barra, BorderLayout.NORTH);

		// Por cada forma se crea una opción en el menú y un botón en la barra, con el mismo oyente
		String[] formas = { "Libre", "Línea", "Círculo", "Cuadrado" };
		for (String f : formas) {
			JMenuItem item = new JMenuItem(f);
			item.addActionListener(oyenteForma);
			mForma.add(item);

			JButton boton = new JButton(f);
			boton.addActionListener(oyenteForma);
			barra.add(boton);
		}
		barra.addSeparator();

		JMenuItem iColor = new JMenuItem("Color...");
		iColor.addActionListener(oyenteColor);
		mBrocha.add(iColor);
		JButton bColor = new JButton("Color");
		bColor.addActionListener(oyenteColor);
		barra.add(bColor);

		JMenuItem iGrosor = new JMenuItem("Ancho...");
		iGrosor.addActionListener(oyenteGrosor);
		mBrocha.add(iGrosor);
		JButton bGrosor = new JButton("Ancho");
		bGrosor.addActionListener(oyenteGrosor);
		barra.add(bGrosor);
		barra.addSeparator();

		JButton bLimpiar = new JButton("Limpiar");
		bLimpiar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int respuesta = JOptionPane.showConfirmDialog(contentPane, "¿Borrar todo el dibujo?", "Limpiar",
						JOptionPane.YES_NO_OPTION);
				if (respuesta == JOptionPane.YES_OPTION) {
					// Una imagen nueva está vacía, así que el lienzo queda en blanco
					imagen = new BufferedImage(2000, 1500, BufferedImage.TYPE_INT_ARGB);
					lienzo.repaint();
				}
			}
		});
		barra.add(bLimpiar);

		// ---------- RATÓN ----------
		lienzo.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				// Guardamos el punto donde empieza el trazo o la figura
				x1 = e.getX();
				y1 = e.getY();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// Las figuras se dibujan al soltar el ratón, desde el punto inicial hasta el final
				int x2 = e.getX();
				int y2 = e.getY();
				// Esquina superior izquierda y lado (el mismo ancho y alto para que salga un círculo/cuadrado)
				int x = Math.min(x1, x2);
				int y = Math.min(y1, y2);
				int lado = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));

				Graphics2D g2 = pincel();
				if (forma.equals("Línea")) {
					g2.drawLine(x1, y1, x2, y2);
				} else if (forma.equals("Círculo")) {
					g2.drawOval(x, y, lado, lado);
				} else if (forma.equals("Cuadrado")) {
					g2.drawRect(x, y, lado, lado);
				}
				g2.dispose();
				lienzo.repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		});

		lienzo.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// Dibujo libre: unimos el punto anterior con el actual con una línea muy corta
				if (forma.equals("Libre")) {
					Graphics2D g2 = pincel();
					g2.drawLine(x1, y1, e.getX(), e.getY());
					g2.dispose();
					x1 = e.getX();
					y1 = e.getY();
					lienzo.repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {}
		});
	}

	// Prepara un "pincel" para dibujar en la imagen con el color y el grosor elegidos
	private Graphics2D pincel() {
		Graphics2D g2 = imagen.createGraphics();
		g2.setColor(color);
		g2.setStroke(new BasicStroke(grosor, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // bordes suaves
		return g2;
	}
}
