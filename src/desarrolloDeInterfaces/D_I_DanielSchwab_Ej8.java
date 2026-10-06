package desarrolloDeInterfaces;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * Editor de textos básico con Swing.
 *
 * Componentes utilizados:
 *  - JMenuBar / JMenu / JMenuItem / JCheckBoxMenuItem : menús "Archivo" y "Estilo".
 *  - JTextArea dentro de un JScrollPane                : área de edición.
 *  - JComboBox                                         : selección del tipo de letra.
 *  - JSpinner y JSlider (sincronizados)                : tamaño de la fuente.
 *  - JCheckBox (sincronizados con el menú Estilo)      : negrita y cursiva.
 *  - JLabel                                            : barra de estado (caracteres, líneas, formato).
 */
public class D_I_DanielSchwab_Ej8 extends JFrame {

    // ---------- Componentes de la interfaz ----------
    private JTextArea areaTexto;
    private JComboBox<String> comboFuente;
    private JSpinner spinnerTamano;
    private JSlider sliderTamano;
    private JCheckBox chkNegrita, chkCursiva;
    private JCheckBoxMenuItem menuNegrita, menuCursiva;
    private JLabel barraEstado;

    // ---------- Constantes ----------
    private static final int TAM_MIN = 8;
    private static final int TAM_MAX = 72;
    private static final int TAM_INICIAL = 16;
    private static final String[] FUENTES = {
            "Arial", "Times New Roman", "Courier New", "Verdana",
            "Georgia", "Monospaced", "SansSerif", "Serif"
    };

    /** Evita bucles infinitos cuando un componente actualiza a otro que está enlazado. */
    private boolean sincronizando = false;
    private final ButtonGroup buttonGroup = new ButtonGroup();

    public D_I_DanielSchwab_Ej8() {
        super("Editor de textos básico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null); // centrar la ventana

        crearMenu();
        crearInterfaz();
        aplicarFormato();      // formato inicial
        actualizarBarraEstado();
    }

    // =====================================================================
    //  CONSTRUCCIÓN DE LA INTERFAZ
    // =====================================================================

    /** Crea la barra de menú con los menús "Archivo" y "Estilo". */
    private void crearMenu() {
        JMenuBar barraMenu = new JMenuBar();

        // --- Menú Archivo ---
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemNuevo = new JMenuItem("Nuevo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemNuevo.setAccelerator(KeyStroke.getKeyStroke("control N"));
        itemSalir.setAccelerator(KeyStroke.getKeyStroke("control Q"));

        itemNuevo.addActionListener(e -> nuevoDocumento());
        itemSalir.addActionListener(e -> System.exit(0));

        menuArchivo.add(itemNuevo);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        // --- Menú Estilo ---
        JMenu menuEstilo = new JMenu("Estilo");
        menuNegrita = new JCheckBoxMenuItem("Negrita");
        menuCursiva = new JCheckBoxMenuItem("Cursiva");
        menuNegrita.setAccelerator(KeyStroke.getKeyStroke("control B"));
        menuCursiva.setAccelerator(KeyStroke.getKeyStroke("control I"));

        // Al pulsar el menú, se actualiza el JCheckBox equivalente
        menuNegrita.addActionListener(e -> {
            chkNegrita.setSelected(menuNegrita.isSelected());
            aplicarFormato();
        });
        menuCursiva.addActionListener(e -> {
            chkCursiva.setSelected(menuCursiva.isSelected());
            aplicarFormato();
        });

        menuEstilo.add(menuNegrita);
        menuEstilo.add(menuCursiva);

        barraMenu.add(menuArchivo);
        barraMenu.add(menuEstilo);
        setJMenuBar(barraMenu);
    }

    /** Crea el panel de herramientas, el área de texto y la barra de estado. */
    private void crearInterfaz() {
        getContentPane().setLayout(new BorderLayout());

        // ---------- Panel superior con los controles de formato ----------
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelControles.setBorder(BorderFactory.createTitledBorder("Formato"));

        // Tipo de letra
        comboFuente = new JComboBox<>(FUENTES);
        comboFuente.addActionListener(e -> aplicarFormato());
        panelControles.add(new JLabel("Fuente:"));
        panelControles.add(comboFuente);

        // Tamaño: JSpinner
        spinnerTamano = new JSpinner(new SpinnerNumberModel(TAM_INICIAL, TAM_MIN, TAM_MAX, 1));
        spinnerTamano.addChangeListener(e -> {
            if (sincronizando) return;
            sincronizando = true;
            sliderTamano.setValue((Integer) spinnerTamano.getValue()); // spinner -> slider
            sincronizando = false;
            aplicarFormato();
        });
        panelControles.add(new JLabel("Tamaño:"));
        panelControles.add(spinnerTamano);

        // Tamaño: JSlider (conectado con el spinner)
        sliderTamano = new JSlider(TAM_MIN, TAM_MAX, TAM_INICIAL);
        sliderTamano.setMajorTickSpacing(16);
        sliderTamano.setMinorTickSpacing(4);
        sliderTamano.setPaintTicks(true);
        sliderTamano.setPaintLabels(true);
        sliderTamano.addChangeListener(e -> {
            if (sincronizando) return;
            sincronizando = true;
            spinnerTamano.setValue(sliderTamano.getValue()); // slider -> spinner
            sincronizando = false;
            aplicarFormato();
        });
        panelControles.add(sliderTamano);

        // Casillas de verificación de negrita y cursiva (conectadas con el menú)
        chkNegrita = new JCheckBox("Negrita");
        buttonGroup.add(chkNegrita);
        chkCursiva = new JCheckBox("Cursiva");
        buttonGroup.add(chkCursiva);
        chkNegrita.addActionListener(e -> {
            menuNegrita.setSelected(chkNegrita.isSelected());
            aplicarFormato();
        });
        chkCursiva.addActionListener(e -> {
            menuCursiva.setSelected(chkCursiva.isSelected());
            aplicarFormato();
        });
        panelControles.add(chkNegrita);
        panelControles.add(chkCursiva);

        getContentPane().add(panelControles, BorderLayout.NORTH);

        // ---------- Área de texto con scroll ----------
        areaTexto = new JTextArea();
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setMargin(new Insets(8, 8, 8, 8));
        // Cada cambio en el documento actualiza la barra de estado
        areaTexto.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { actualizarBarraEstado(); }
            public void removeUpdate(DocumentEvent e)  { actualizarBarraEstado(); }
            public void changedUpdate(DocumentEvent e) { actualizarBarraEstado(); }
        });
        getContentPane().add(new JScrollPane(areaTexto), BorderLayout.CENTER);

        // ---------- Barra de estado ----------
        barraEstado = new JLabel(" ");
        barraEstado.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        getContentPane().add(barraEstado, BorderLayout.SOUTH);
    }

    // =====================================================================
    //  LÓGICA
    // =====================================================================

    /** Construye la fuente a partir de los controles y la aplica al área de texto. */
    private void aplicarFormato() {
        int estilo = Font.PLAIN;
        if (chkNegrita.isSelected()) estilo |= Font.BOLD;
        if (chkCursiva.isSelected()) estilo |= Font.ITALIC;

        String nombre = (String) comboFuente.getSelectedItem();
        int tamano = (Integer) spinnerTamano.getValue();

        areaTexto.setFont(new Font(nombre, estilo, tamano));
        actualizarBarraEstado();
    }

    /** Muestra caracteres, líneas y formato actual en la barra de estado. */
    private void actualizarBarraEstado() {
        if (barraEstado == null || areaTexto == null) return;

        String estilo = "Normal";
        if (chkNegrita.isSelected() && chkCursiva.isSelected()) estilo = "Negrita + Cursiva";
        else if (chkNegrita.isSelected()) estilo = "Negrita";
        else if (chkCursiva.isSelected()) estilo = "Cursiva";

        barraEstado.setText(String.format(
                "Caracteres: %d   |   Líneas: %d   |   Fuente: %s, %s, %d pt",
                areaTexto.getText().length(),
                areaTexto.getLineCount(),
                comboFuente.getSelectedItem(),
                estilo,
                (Integer) spinnerTamano.getValue()));
    }

    /** Borra el contenido (pidiendo confirmación si hay texto escrito). */
    private void nuevoDocumento() {
        if (!areaTexto.getText().isEmpty()) {
            int r = JOptionPane.showConfirmDialog(this,
                    "Se perderá el texto actual. ¿Crear un documento nuevo?",
                    "Nuevo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (r != JOptionPane.YES_OPTION) return;
        }
        areaTexto.setText("");
        areaTexto.requestFocus();
    }

    // =====================================================================
    //  PUNTO DE ENTRADA
    // =====================================================================
    public static void main(String[] args) {
        // Las interfaces Swing deben crearse en el hilo de eventos (EDT)
        SwingUtilities.invokeLater(() -> new D_I_DanielSchwab_Ej8().setVisible(true));
    }
}