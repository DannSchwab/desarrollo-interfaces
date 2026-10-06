package desarrolloDeInterfaces;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class D_I_DanielSchwab_Ej7 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					D_I_DanielSchwab_Ej7 frame = new D_I_DanielSchwab_Ej7();
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
	public D_I_DanielSchwab_Ej7() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		 
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// Obtener la lista de fuentes disponibles en el sistema
		String[] fuentesDisponibles = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		
		// Crear el JComboBox y llenarlo con el arreglo de fuentes
		JComboBox<String> comboBox = new JComboBox<>(fuentesDisponibles);
		
		// Crear el JTextArea con un texto de prueba y una fuente inicial
		JTextArea tArea = new JTextArea();
		tArea.setText("Prueba escribiendo algo aquí...");
		tArea.setFont(new Font("Arial", Font.PLAIN, 14));
		
		// Añadir un Listener al JComboBox para detectar cuándo cambias de opción
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fuenteSeleccionada = (String) comboBox.getSelectedItem();
				int tamanoActual = tArea.getFont().getSize();
				int estiloActual = tArea.getFont().getStyle();
				
				tArea.setFont(new Font(fuenteSeleccionada, estiloActual, tamanoActual));
			}
		});
		
		contentPane.add(new JScrollPane(tArea), BorderLayout.CENTER);
		contentPane.add(comboBox, BorderLayout.NORTH);
	}
}