package desarrolloDeInterfaces;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane; // Importante para las ventanas emergentes
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class D_I_DanielSchwab_Ej4 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					D_I_DanielSchwab_Ej4 frame = new D_I_DanielSchwab_Ej4();
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
	public D_I_DanielSchwab_Ej4() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel usuario = new JLabel("Usuario");
		contentPane.add(usuario, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel contraseña = new JLabel("Contraseña");
		contentPane.add(contraseña, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		JTextField t_usuario = new JTextField(15);
		contentPane.add(t_usuario, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		JPasswordField t_contraseña = new JPasswordField(15);
		contentPane.add(t_contraseña, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		// Hacemos que el botón ocupe 2 columnas para que quede centrado bajo los campos
		gbc.gridwidth = 2; 
		JButton verificar = new JButton("Verificar");
		contentPane.add(verificar, gbc);
		
		verificar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				try {
					String true_user = "admin";
					String true_password = "1234";
					// Obtenemos los valores introducidos
					String user = t_usuario.getText();
					String pass = new String(t_contraseña.getPassword());
					
					// Validación de ejemplo
					if (user.equals(true_user) && pass.equals(true_password)) {
						JOptionPane.showMessageDialog(contentPane, "¡Acceso concedido!", "Login", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(contentPane, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
}