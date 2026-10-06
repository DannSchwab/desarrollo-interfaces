package desarrolloDeInterfaces;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

/**
 * Mini Paint con Swing (ampliación del ejercicio de clase).
 *
 * Funciones:
 *  - Menú (JMenuBar) y barra de herramientas (JToolBar) con las mismas opciones.
 *  - Formas: dibujo libre, línea recta, círculo y cuadrado (y una goma de borrar).
 *  - Cambiar el color de la línea (JColorChooser + paleta de colores rápidos).
 *  - Cambiar el ancho de la brocha (JSpinner en la barra y anchos fijos en el menú).
 *  - Limpiar el lienzo y barra de estado con la herramienta, el color, el ancho y la posición del ratón.
 *
 * Idea principal: todo lo que se dibuja se guarda en una imagen (BufferedImage).
 * El lienzo (un JPanel) solo se encarga de enseñar esa imagen en pantalla desde su paintComponent().
 */
public class D_I_DanielSchwab_MiniPaint extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel lienzo;

	// ---------- Herramientas disponibles ----------
	private static final String LIBRE = "Dibujo libre";
	private static final String LINEA = "Línea recta";
	private static final String CIRCULO = "Círculo";
	private static final String CUADRADO = "Cuadrado";
	private static final String GOMA = "Goma";
	private static final String[] HERRAMIENTAS = { LIBRE, LINEA, CIRCULO, CUADRADO, GOMA };

	// Colores de la paleta rápida de la barra de herramientas
	private static final Color[] PALETA = { Color.BLACK, Color.GRAY, Color.RED, Color.ORANGE,
			Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA };

	// Anchos de brocha que aparecen en el menú "Brocha"
	private static final int[] ANCHOS = { 1, 3, 5, 10, 20 };

	// ---------- Estado actual del Paint ----------
	private String herramienta = LIBRE;   // herramienta seleccionada
	private Color color = Color.BLACK;    // color de la línea
	private int grosor = 3;               // ancho de la brocha en píxeles

	// Imagen donde se queda guardado todo lo que se dibuja
	private BufferedImage imagen;

	// Coordenadas del ratón
	private int xInicio, yInicio;         // punto donde se pulsó el botón
	private int xActual, yActual;         // punto actual mientras se arrastra
	private boolean arrastrando = false;
	private int ratonX = -1, ratonY = -1; // posición para la barra de estado (-1 = fuera del lienzo)

	// ---------- Componentes que se actualizan desde varios sitios ----------
	private JRadioButtonMenuItem[] itemsHerramienta = new JRadioButtonMenuItem[HERRAMIENTAS.length];
	private JToggleButton[] botonesHerramienta = new JToggleButton[HERRAMIENTAS.length];
	private JPanel muestraColor;          // cuadradito que enseña el color actual
	private JSpinner spinnerGrosor;
	private JLabel barraEstado;

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
		super("Mini Paint");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		crearImagen();
		crearLienzo();            // CENTER
		crearMenu();              // JMenuBar
		crearBarraHerramientas(); // NORTH
		crearBarraEstado();       // SOUTH

		seleccionarHerramienta(0); // empezamos con el dibujo libre
	}

	// =====================================================================
	//  CONSTRUCCIÓN DE LA INTERFAZ
	// =====================================================================

	/** Crea el lienzo (JPanel) y le añade los listeners del ratón. */
	private void crearLienzo() {
		// Para pintar en un JPanel hay que sobrescribir su método paintComponent().
		// Lo hacemos con una clase anónima: es un JPanel normal, pero con nuestro propio paintComponent.
		// OJO: paintComponent nunca se llama a mano; lo llama Swing cuando hacemos lienzo.repaint().
		lienzo = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);          // pinta el fondo del panel
				g.drawImage(imagen, 0, 0, null);  // 1) pinta todo lo que ya está dibujado

				// 2) Mientras se arrastra una figura se dibuja una "vista previa" encima.
				//    Solo se pinta en pantalla (no en la imagen), por eso va cambiando al mover el ratón.
				if (arrastrando && esFigura()) {
					Graphics2D g2 = (Graphics2D) g.create(); // copia del Graphics para no "ensuciar" el original
					prepararPincel(g2, color);
					dibujarFigura(g2, xInicio, yInicio, xActual, yActual);
					g2.dispose();
				}
			}
		};
		lienzo.setBackground(Color.WHITE);
		lienzo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		lienzo.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		contentPane.add(lienzo, BorderLayout.CENTER);

		// MouseMotionListener: detecta el MOVIMIENTO del ratón
		lienzo.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// Moverse sin pulsar no dibuja nada: solo actualizamos la posición en la barra de estado
				ratonX = e.getX();
				ratonY = e.getY();
				actualizarBarraEstado();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (!esFigura()) {
					// Dibujo libre / goma: unimos el punto anterior con el nuevo con una línea muy corta.
					// Como este método se llama muchas veces por segundo, parece un trazo continuo.
					pintarTrazo(xActual, yActual, e.getX(), e.getY());
				}
				// Guardamos la posición actual (para el siguiente tramo o para la vista previa de la figura)
				xActual = e.getX();
				yActual = e.getY();
				ratonX = e.getX();
				ratonY = e.getY();
				lienzo.repaint();
				actualizarBarraEstado();
			}
		});

		// MouseListener: detecta los CLICS (pulsar, soltar, entrar, salir...)
		lienzo.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				// Guardamos el punto donde empieza el trazo o la figura
				xInicio = e.getX();
				yInicio = e.getY();
				xActual = xInicio;
				yActual = yInicio;
				arrastrando = true;

				// En dibujo libre y goma, un simple clic ya pinta un punto
				if (!esFigura()) {
					pintarTrazo(xInicio, yInicio, xInicio, yInicio);
					lienzo.repaint();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// Al soltar el botón, la figura se dibuja de verdad en la imagen y ya se queda fija
				if (arrastrando && esFigura()) {
					xActual = e.getX();
					yActual = e.getY();
					pintarFigura();
				}
				arrastrando = false;
				lienzo.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// Fuera del lienzo no hay coordenadas que mostrar
				ratonX = -1;
				ratonY = -1;
				actualizarBarraEstado();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// No hace falta hacer nada
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// No hace falta: el punto ya se pinta en mousePressed
			}
		});
	}

	/** Crea la barra de menú: Archivo, Herramientas, Color y Brocha. */
	private void crearMenu() {
		JMenuBar jmb = new JMenuBar();
		setJMenuBar(jmb);

		// Nota: "e -> metodo()" es una lambda, la forma corta de escribir
		// new ActionListener() { public void actionPerformed(ActionEvent e) { metodo(); } }

		// --- Menú Archivo ---
		JMenu mArchivo = new JMenu("Archivo");
		JMenuItem iNuevo = new JMenuItem("Nuevo (limpiar lienzo)");
		iNuevo.setAccelerator(KeyStroke.getKeyStroke("control N"));
		iNuevo.addActionListener(e -> limpiarLienzo());
		JMenuItem iSalir = new JMenuItem("Salir");
		iSalir.setAccelerator(KeyStroke.getKeyStroke("control Q"));
		iSalir.addActionListener(e -> System.exit(0));
		mArchivo.add(iNuevo);
		mArchivo.addSeparator();
		mArchivo.add(iSalir);
		jmb.add(mArchivo);

		// --- Menú Herramientas: qué forma se va a dibujar ---
		// JRadioButtonMenuItem + ButtonGroup = solo puede haber una opción marcada a la vez
		JMenu mHerramientas = new JMenu("Herramientas");
		ButtonGroup grupoMenu = new ButtonGroup();
		for (int i = 0; i < HERRAMIENTAS.length; i++) {
			final int indice = i; // dentro de una lambda solo se pueden usar variables que no cambian
			itemsHerramienta[i] = new JRadioButtonMenuItem(HERRAMIENTAS[i]);
			itemsHerramienta[i].setAccelerator(KeyStroke.getKeyStroke("control " + (i + 1))); // Ctrl+1, Ctrl+2...
			itemsHerramienta[i].addActionListener(e -> seleccionarHerramienta(indice));
			grupoMenu.add(itemsHerramienta[i]);
			mHerramientas.add(itemsHerramienta[i]);
		}
		jmb.add(mHerramientas);

		// --- Menú Color ---
		JMenu mColor = new JMenu("Color");
		JMenuItem iElegirColor = new JMenuItem("Elegir color de la línea...");
		iElegirColor.setAccelerator(KeyStroke.getKeyStroke("control K"));
		iElegirColor.addActionListener(e -> elegirColor());
		mColor.add(iElegirColor);
		jmb.add(mColor);

		// --- Menú Brocha: anchos predefinidos ---
		JMenu mBrocha = new JMenu("Brocha");
		for (int ancho : ANCHOS) {
			JMenuItem iAncho = new JMenuItem("Ancho " + ancho + " px");
			// Solo cambiamos el valor del spinner de la barra; su ChangeListener actualiza el grosor.
			// Así el menú y la barra siempre enseñan el mismo valor.
			iAncho.addActionListener(e -> spinnerGrosor.setValue(ancho));
			mBrocha.add(iAncho);
		}
		jmb.add(mBrocha);
	}

	/** Crea la barra de herramientas (JToolBar) con las mismas opciones que el menú. */
	private void crearBarraHerramientas() {
		JToolBar barra = new JToolBar("Herramientas");
		barra.setFloatable(false); // que no se pueda arrastrar fuera de la ventana

		// --- Formas: JToggleButton, son botones que se quedan pulsados ---
		ButtonGroup grupoBotones = new ButtonGroup();
		for (int i = 0; i < HERRAMIENTAS.length; i++) {
			final int indice = i;
			botonesHerramienta[i] = new JToggleButton(HERRAMIENTAS[i]);
			botonesHerramienta[i].setFocusable(false);
			botonesHerramienta[i].addActionListener(e -> seleccionarHerramienta(indice));
			grupoBotones.add(botonesHerramienta[i]);
			barra.add(botonesHerramienta[i]);
		}
		barra.addSeparator();

		// --- Color: botón que abre el selector + muestra del color actual ---
		JButton bColor = new JButton("Color...");
		bColor.setFocusable(false);
		bColor.addActionListener(e -> elegirColor());
		barra.add(bColor);
		barra.add(Box.createHorizontalStrut(4));

		muestraColor = new JPanel();
		muestraColor.setBackground(color);
		muestraColor.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		muestraColor.setPreferredSize(new Dimension(24, 24));
		muestraColor.setMaximumSize(new Dimension(24, 24));
		muestraColor.setToolTipText("Color actual");
		barra.add(muestraColor);
		barra.add(Box.createHorizontalStrut(8));

		// Paleta de colores rápidos: un botón pequeño de cada color
		for (Color c : PALETA) {
			JButton bPaleta = new JButton();
			bPaleta.setBackground(c);
			bPaleta.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
			bPaleta.setPreferredSize(new Dimension(20, 20));
			bPaleta.setMaximumSize(new Dimension(20, 20));
			bPaleta.setFocusable(false);
			bPaleta.addActionListener(e -> cambiarColor(c));
			barra.add(bPaleta);
			barra.add(Box.createHorizontalStrut(2));
		}
		barra.addSeparator();

		// --- Ancho de la brocha ---
		barra.add(new JLabel("Ancho: "));
		spinnerGrosor = new JSpinner(new SpinnerNumberModel(grosor, 1, 50, 1));
		spinnerGrosor.setMaximumSize(spinnerGrosor.getPreferredSize()); // que no se estire por toda la barra
		spinnerGrosor.addChangeListener(e -> {
			grosor = (Integer) spinnerGrosor.getValue();
			actualizarBarraEstado();
		});
		barra.add(spinnerGrosor);
		barra.add(new JLabel(" px"));
		barra.addSeparator();

		// --- Limpiar ---
		JButton bLimpiar = new JButton("Limpiar");
		bLimpiar.setFocusable(false);
		bLimpiar.addActionListener(e -> limpiarLienzo());
		barra.add(bLimpiar);

		contentPane.add(barra, BorderLayout.NORTH);
	}

	/** Etiqueta inferior que informa de la herramienta, el color, el ancho y la posición. */
	private void crearBarraEstado() {
		barraEstado = new JLabel(" ");
		barraEstado.setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 4));
		contentPane.add(barraEstado, BorderLayout.SOUTH);
	}

	// =====================================================================
	//  ACCIONES DEL MENÚ Y DE LA BARRA
	// =====================================================================

	/**
	 * Cambia la herramienta y la marca a la vez en el menú y en la barra, para que estén sincronizados.
	 * setSelected() no lanza el ActionListener, así que no se crea un bucle infinito.
	 */
	private void seleccionarHerramienta(int indice) {
		herramienta = HERRAMIENTAS[indice];
		itemsHerramienta[indice].setSelected(true);
		botonesHerramienta[indice].setSelected(true);
		actualizarBarraEstado();
	}

	/** Abre el selector de color de Swing (JColorChooser). */
	private void elegirColor() {
		// Devuelve el color elegido, o null si el usuario pulsa "Cancelar"
		Color nuevo = JColorChooser.showDialog(this, "Elige el color de la línea", color);
		if (nuevo != null) {
			cambiarColor(nuevo);
		}
	}

	private void cambiarColor(Color nuevo) {
		color = nuevo;
		muestraColor.setBackground(nuevo);
		actualizarBarraEstado();
	}

	/** Borra todo el dibujo (pidiendo confirmación). */
	private void limpiarLienzo() {
		int r = JOptionPane.showConfirmDialog(this, "Se borrará todo el dibujo. ¿Quieres continuar?",
				"Limpiar lienzo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (r == JOptionPane.YES_OPTION) {
			limpiarImagen();
			lienzo.repaint();
		}
	}

	private void actualizarBarraEstado() {
		if (barraEstado == null) return; // todavía no se ha creado

		String posicion = (ratonX >= 0 && ratonY >= 0) ? ratonX + ", " + ratonY : "-";
		barraEstado.setText(String.format(
				"Herramienta: %s   |   Color: RGB(%d, %d, %d)   |   Ancho: %d px   |   Posición: %s",
				herramienta, color.getRed(), color.getGreen(), color.getBlue(), grosor, posicion));
	}

	// =====================================================================
	//  DIBUJO
	// =====================================================================

	/** Crea la imagen en blanco donde se guarda el dibujo. */
	private void crearImagen() {
		// Del tamaño de la pantalla, para que no se quede corta aunque se maximice la ventana
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		imagen = new BufferedImage(pantalla.width, pantalla.height, BufferedImage.TYPE_INT_RGB);
		limpiarImagen();
	}

	/** Rellena toda la imagen de blanco. */
	private void limpiarImagen() {
		Graphics2D g2 = imagen.createGraphics();
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, imagen.getWidth(), imagen.getHeight());
		g2.dispose();
	}

	/** true si la herramienta actual es una figura (línea, círculo o cuadrado). */
	private boolean esFigura() {
		return herramienta.equals(LINEA) || herramienta.equals(CIRCULO) || herramienta.equals(CUADRADO);
	}

	/** Configura el "pincel": color, grosor y suavizado. */
	private void prepararPincel(Graphics2D g2, Color c) {
		g2.setColor(c);
		// BasicStroke define el grosor de la línea; con extremos redondeados el trazo queda más suave
		g2.setStroke(new BasicStroke(grosor, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		// Antialiasing: suaviza los bordes para que no se vean "dientes de sierra"
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	/** Dibuja un tramo del dibujo libre (o de la goma) directamente en la imagen. */
	private void pintarTrazo(int x1, int y1, int x2, int y2) {
		Graphics2D g2 = imagen.createGraphics();
		// La goma no es más que un pincel blanco (el color del fondo)
		prepararPincel(g2, herramienta.equals(GOMA) ? Color.WHITE : color);
		g2.drawLine(x1, y1, x2, y2);
		g2.dispose();
	}

	/** Dibuja la figura definitiva en la imagen (se llama al soltar el ratón). */
	private void pintarFigura() {
		Graphics2D g2 = imagen.createGraphics();
		prepararPincel(g2, color);
		dibujarFigura(g2, xInicio, yInicio, xActual, yActual);
		g2.dispose();
	}

	/**
	 * Dibuja la figura actual entre el punto (x1, y1) y el punto (x2, y2).
	 * Se usa tanto para la vista previa (en pantalla) como para la figura final (en la imagen).
	 */
	private void dibujarFigura(Graphics2D g2, int x1, int y1, int x2, int y2) {
		if (herramienta.equals(LINEA)) {
			g2.drawLine(x1, y1, x2, y2);
			return;
		}

		// Para el círculo y el cuadrado el ancho y el alto tienen que ser iguales:
		// usamos el lado más largo del rectángulo que forma el arrastre del ratón.
		int lado = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
		// drawOval y drawRect necesitan la esquina superior izquierda.
		// Si se arrastra hacia la izquierda o hacia arriba, esa esquina se desplaza.
		int x = (x2 < x1) ? x1 - lado : x1;
		int y = (y2 < y1) ? y1 - lado : y1;

		if (herramienta.equals(CIRCULO)) {
			g2.drawOval(x, y, lado, lado);
		} else if (herramienta.equals(CUADRADO)) {
			g2.drawRect(x, y, lado, lado);
		}
	}
}
