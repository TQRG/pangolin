package pt.up.fe.pangolin.eclipse.runtime;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import pt.up.fe.pangolin.core.runtime.Collector;

public class PangolinFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	JLabel informationLabel;
	JButton startTransactionButton;
	JButton endAssociatedButton;
	JButton endDissociatedButton;
	JButton stopAnalysisButton;

	private int transactions = 0;
	private int associatedTransactions = 0;
	private int dissociatedTransactions = 0;
	private boolean inTransaction = false;

	/**
	 * Launch the application.
	 */
	public static void startFrame() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		} 

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PangolinFrame frame = new PangolinFrame();
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
	public PangolinFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 588, 95);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{122, 122, 122, 122, 0};
		gbl_contentPane.rowHeights = new int[]{0, 43, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);



		informationLabel = new JLabel("New label");
		informationLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_informationLabel = new GridBagConstraints();
		gbc_informationLabel.anchor = GridBagConstraints.WEST;
		gbc_informationLabel.gridwidth = 4;
		gbc_informationLabel.insets = new Insets(0, 5, 5, 5);
		gbc_informationLabel.gridx = 0;
		gbc_informationLabel.gridy = 0;
		contentPane.add(informationLabel, gbc_informationLabel);

		startTransactionButton = new JButton("Start transaction");
		startTransactionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startedTransaction();
			}
		});
		GridBagConstraints gbc_startTransactionButton = new GridBagConstraints();
		gbc_startTransactionButton.fill = GridBagConstraints.BOTH;
		gbc_startTransactionButton.insets = new Insets(0, 0, 0, 5);
		gbc_startTransactionButton.gridx = 0;
		gbc_startTransactionButton.gridy = 1;
		contentPane.add(startTransactionButton, gbc_startTransactionButton);

		endAssociatedButton = new JButton("End Associated");
		GridBagConstraints gbc_endAssociatedButton = new GridBagConstraints();
		gbc_endAssociatedButton.fill = GridBagConstraints.BOTH;
		gbc_endAssociatedButton.insets = new Insets(0, 0, 0, 5);
		gbc_endAssociatedButton.gridx = 1;
		gbc_endAssociatedButton.gridy = 1;
		contentPane.add(endAssociatedButton, gbc_endAssociatedButton);
		endAssociatedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endedAssociatedTransaction();
			}
		});

		endDissociatedButton = new JButton("End Dissociated");
		GridBagConstraints gbc_endDissociatedButton = new GridBagConstraints();
		gbc_endDissociatedButton.fill = GridBagConstraints.BOTH;
		gbc_endDissociatedButton.insets = new Insets(0, 0, 0, 5);
		gbc_endDissociatedButton.gridx = 2;
		gbc_endDissociatedButton.gridy = 1;
		contentPane.add(endDissociatedButton, gbc_endDissociatedButton);
		endDissociatedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endedDissociatedTransaction();
			}
		});

		stopAnalysisButton = new JButton("Stop Analysis");
		GridBagConstraints gbc_stopAnalysisButton = new GridBagConstraints();
		gbc_stopAnalysisButton.fill = GridBagConstraints.BOTH;
		gbc_stopAnalysisButton.gridx = 3;
		gbc_stopAnalysisButton.gridy = 1;
		contentPane.add(stopAnalysisButton, gbc_stopAnalysisButton);
		stopAnalysisButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				stopAnalisys();
			}
		});

		updateInformation();
	}

	protected void stopAnalisys() {
		System.exit(0);
	}

	protected void endedDissociatedTransaction() {
		this.dissociatedTransactions++;
		inTransaction = false;
		
		Collector.instance().endTransaction("Dissociated Transaction #" + Integer.toString(this.dissociatedTransactions), false);
		updateInformation();
	}

	protected void endedAssociatedTransaction() {
		this.associatedTransactions++;
		inTransaction = false;

		Collector.instance().endTransaction("Associated Transaction #" + Integer.toString(this.associatedTransactions), true);
		updateInformation();
	}

	protected void startedTransaction() {
		this.transactions++;
		inTransaction = true;

		Collector.instance().startTransaction();
		updateInformation();
	}

	protected void updateInformation() {
		this.startTransactionButton.setEnabled(!inTransaction);
		this.stopAnalysisButton.setEnabled(!inTransaction);
		this.endAssociatedButton.setEnabled(inTransaction);
		this.endDissociatedButton.setEnabled(inTransaction);

		this.informationLabel.setText("Current transaction: " + this.transactions + 
				", associated: " + this.associatedTransactions + 
				", dissociated: " + this.dissociatedTransactions);
	}

}