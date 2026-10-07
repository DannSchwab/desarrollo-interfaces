package desarrolloDeInterfaces;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


public class D_I_DanielSchwab_Ej2 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JMenuBar menu;
	private JMenu m_archivo;
	private JMenu m_editar;
	private JMenu m_formato;
	private JMenuItem i_nuevo; 
	private JMenuItem i_abrir; 
	private JMenuItem i_salir; 
	private JMenuItem i_copiar; 
	private JMenuItem i_pegar; 
	private JMenuItem i_negrita; 
	private JMenuItem i_cursiva; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					D_I_DanielSchwab_Ej2 frame = new D_I_DanielSchwab_Ej2();
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
	public D_I_DanielSchwab_Ej2() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		menu = new JMenuBar();
		// Coloca el JMenuBar en la parte superior del frame
		setJMenuBar(menu);
		
		// Se hace visible el menu
		menu.setVisible(true);
		// Se instancian los componentes del menú con sus nombres
		m_archivo = new JMenu("Archivo");
		m_editar = new JMenu("Editar");
		m_formato = new JMenu("Formato");
		
		// se añaden las pestañas del menú al menú
		menu.add(m_archivo);
		menu.add(m_editar);
		menu.add(m_formato);
		
		//se hacen visibles las pestañas a la vez que se le agregan los items a cada una 
		m_archivo.setVisible(true);
		m_archivo.add(i_nuevo = new JMenuItem("nuevo"));
		m_archivo.addSeparator();
		m_archivo.add(i_abrir = new JMenuItem("abrir"));
		m_archivo.addSeparator();
		m_archivo.add(i_salir = new JMenuItem("salir"));
		
		m_editar.setVisible(true);
		m_editar.add(i_copiar = new JMenuItem("Copiar"));
		m_editar.addSeparator();
		m_editar.add(i_pegar = new JMenuItem("Pegar"));
		
		m_formato.setVisible(true);
		m_formato.add(i_negrita = new JMenuItem("Negrita"));
		m_formato.addSeparator();
		m_formato.add(i_cursiva = new JMenuItem("Cursiva"));
	}

}
