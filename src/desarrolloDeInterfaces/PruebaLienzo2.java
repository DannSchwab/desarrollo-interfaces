package desarrolloDeInterfaces;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PruebaLienzo2 extends JFrame {

	private enum Herramienta {
		Libre, Linea, Circulo, Ovalo, Cuadrado, Rectangulo
	}

	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					PruebaLienzo2 frame = new PruebaLienzo2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public class Lienzo extends JPanel {

		private int grosorActual = 1;
		private Color colorActual = Color.BLACK;
		private ArrayList<Figura> figuras = new ArrayList<Figura>();
		private Point puntoInicio;
		private Herramienta herramientaActual = Herramienta.Libre;

		public Lienzo() {
			
			setBackground(Color.WHITE);

			MouseAdapter raton = new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
					puntoInicio = e.getPoint();
				}

				@Override
				public void mouseDragged(MouseEvent e) {
					if (herramientaActual == Herramienta.Libre) {
						Point puntoActual = e.getPoint();
						figuras.add(new Figura(puntoInicio, puntoActual, colorActual, grosorActual, Herramienta.Libre));
						puntoInicio = puntoActual;
						repaint();
					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if (herramientaActual != Herramienta.Libre) {
						figuras.add(new Figura(puntoInicio, e.getPoint(), colorActual, grosorActual, herramientaActual));
						repaint();
					}
				}

			};

			addMouseListener(raton);
			addMouseMotionListener(raton);

		}

		private class Figura {
			private Point inicio;
			private Point fin;
			private Color color;
			private int grosor;
			private Herramienta tipo;

			private Figura(Point inicio, Point fin, Color color, int grosor, Herramienta tipo) {
				this.inicio = inicio;
				this.fin = fin;
				this.color = color;
				this.grosor = grosor;
				this.tipo = tipo;
			}

			private void dibujar(Graphics2D g2) {
				g2.setColor(color);
				g2.setStroke(new BasicStroke(grosor, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

				// valores para ovalo y Rectangulo
				int x = Math.min(inicio.x, fin.x);
				int y = Math.min(inicio.y, fin.y);
				int ancho = Math.abs(fin.x - inicio.x);
				int alto = Math.abs(fin.y - inicio.y);

				// en caso de que sea círculo se igualan los lados al ancho y alto para que sea
				// un círculo perfecto
				if (tipo == Herramienta.Circulo || tipo == Herramienta.Cuadrado) {
					int lado = Math.min(ancho, alto);
					if (fin.x < inicio.x) {
						x = inicio.x - lado;
					} else {
						x = inicio.x;
					}

					if (fin.y < inicio.y) {
						y = inicio.y - alto;
					} else {
						y = inicio.y;
					}
					ancho = lado;
					alto = lado;
				}

				if (tipo == Herramienta.Ovalo || tipo == Herramienta.Circulo) {
					g2.drawOval(x, y, ancho, alto);
				} else if (tipo == Herramienta.Cuadrado || tipo == Herramienta.Rectangulo) {
					g2.drawRect(x, y, ancho, alto);
				} else {
					g2.drawLine(inicio.x, inicio.y, fin.x, fin.y);
				}
			}
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			for (Figura figura : figuras) {
				figura.dibujar(g2);
			}
		}

		public void limpiar() {
			figuras.clear();
			repaint();
		}

		public void cambiarHerramienta(Herramienta nueva) {
			herramientaActual = nueva;
		}

		public void cambiarGrosor(int grosor) {
			this.grosorActual = grosor;
		}

		public void cambiarColor(Color nuevoColor) {
			colorActual = nuevoColor;
		}

	}

	public PruebaLienzo2() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));

		setContentPane(contentPane);

		Lienzo miLienzo = new Lienzo();
		contentPane.add(miLienzo, BorderLayout.CENTER);

		JToolBar barraHerramientas = new JToolBar();
		barraHerramientas.setFloatable(true);
		barraHerramientas.add(crearPanelHerramienta(miLienzo));
		barraHerramientas.addSeparator();
		barraHerramientas.add(crearPanelGrosor(miLienzo));
		barraHerramientas.addSeparator();
		barraHerramientas.add(crearPanelColor(miLienzo));
		contentPane.add(barraHerramientas, BorderLayout.NORTH);

		JMenuBar jmb = new JMenuBar();

		JMenu lienzo = new JMenu("Lienzo");

		JMenuItem nuevo = new JMenuItem("Nuevo");
		nuevo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				miLienzo.limpiar();
			}
		});

		JMenuItem salir = new JMenuItem("Salir");
		salir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});

		setJMenuBar(jmb);

		jmb.add(lienzo);

		lienzo.add(nuevo);
		lienzo.add(salir);
	}

	private JPanel crearPanelHerramienta(Lienzo lienzo) {
		JPanel panelHerramienta = new JPanel();
		panelHerramienta.setBorder(BorderFactory.createTitledBorder("Herramientas"));
		String[] nombres = { "Libre", "Linea", "Ovalo", "Circulo", "Rectangulo", "Cuadrado" };
		Herramienta[] herramientas = { Herramienta.Libre, Herramienta.Linea, Herramienta.Ovalo, Herramienta.Circulo,
				Herramienta.Rectangulo, Herramienta.Cuadrado };
		JComboBox<String> comboHerramientas = new JComboBox<String>(nombres);

		comboHerramientas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				lienzo.cambiarHerramienta(herramientas[comboHerramientas.getSelectedIndex()]);
			}
		});

		panelHerramienta.add(comboHerramientas);
		return panelHerramienta;
	}

	private JPanel crearPanelGrosor(Lienzo lienzo) {
		JPanel panelGrosor = new JPanel();
		panelGrosor.setBorder(BorderFactory.createTitledBorder("Grosor"));
		JSlider sliderGrosor = new JSlider(1, 40, 1);
		JLabel valorGrosor = new JLabel("1 px");

		sliderGrosor.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				int grosor = sliderGrosor.getValue();
				lienzo.cambiarGrosor(grosor);
				valorGrosor.setText(grosor + " px");
			}

		});

		panelGrosor.add(sliderGrosor);
		panelGrosor.add(valorGrosor);
		return panelGrosor;
	}

	private JPanel crearPanelColor(Lienzo lienzo) {
		JPanel panelColor = new JPanel();
		panelColor.setBorder(BorderFactory.createTitledBorder("Color"));

		JPanel muestra = new JPanel();
		muestra.setPreferredSize(new Dimension(30, 20));
		muestra.setBackground(Color.BLACK);

		JSpinner spinnerR = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
		JSpinner spinnerG = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
		JSpinner spinnerB = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));

		ChangeListener cambioRGB = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int r = (int) spinnerR.getValue();
				int g = (int) spinnerG.getValue();
				int b = (int) spinnerB.getValue();
				Color nuevo = new Color(r, g, b);
				lienzo.cambiarColor(nuevo);
				muestra.setBackground(nuevo);
			}
		};

		spinnerR.addChangeListener(cambioRGB);
		spinnerG.addChangeListener(cambioRGB);
		spinnerB.addChangeListener(cambioRGB);

		panelColor.add(spinnerR);
		panelColor.add(new JLabel("R"));

		panelColor.add(spinnerG);
		panelColor.add(new JLabel("G"));

		panelColor.add(spinnerB);
		panelColor.add(new JLabel("B"));

		panelColor.add(muestra);

		return panelColor;
	}

}
