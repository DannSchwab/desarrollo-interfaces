package desarrolloDeInterfaces;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class D_I_DanielSchwab_EJ1 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				/* Se rodea siempre con un try/catch la instanciación de la clase frame para
				 * crear el objeto para que no cuelgue el programa por completo en caso de fallar
				 */
				try {
					D_I_DanielSchwab_EJ1 frame = new D_I_DanielSchwab_EJ1();
					// Se hace visible el frame
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
	public D_I_DanielSchwab_EJ1() {
		// Se define la 'x'como cierre por defecto
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Se definen la posición y extension en píxeles del frame (x,y y width and Height)
		setBounds(100, 200, 400, 400);
	}

}
