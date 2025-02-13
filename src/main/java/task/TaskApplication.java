package task;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TaskApplication implements Runnable {

	public static final int DEFAULT_MAX_VALUE = 48;
	public static final int DEFAULT_NUM_VALUES = 6;

	private JPanel mainPanel;
	private JPanel[] numberPanelArray = new JPanel[DEFAULT_MAX_VALUE];
	private JCheckBox[] checkBoxArray = new JCheckBox[DEFAULT_MAX_VALUE];
	private Set<Integer> selectionSet = new TreeSet<>();

	private JButton startButton;
	private JButton closeButton;
	private Task task = new Task(DEFAULT_NUM_VALUES, DEFAULT_MAX_VALUE);

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new TaskApplication());
	}

	@Override
	public void run() {
		JFrame frame = createMainFrame();
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	private JFrame createMainFrame() {
		JFrame frame = new JFrame("Skandináv lottósorsolás");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
		frame.add(createMainPanel(), BorderLayout.CENTER);
		frame.add(createButtonPanel(), BorderLayout.PAGE_END);
		return frame;
	}

	private JPanel createMainPanel() {
		mainPanel = new JPanel(new GridLayout(0, 8, 5, 5));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		for (int i = 0; i < 48; i++) {
			mainPanel.add(createNumberPanel(i, mainPanel));
		}
		return mainPanel;
	}

	private JPanel createNumberPanel(int panelNum, JPanel panel1) {
		int number = panelNum + 1;
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		JLabel label = new JLabel(String.valueOf(number));
		panel.add(label, BorderLayout.CENTER);

		JCheckBox checkBox = new JCheckBox();
		checkBox.addActionListener(e -> {
			JCheckBox cbBox = (JCheckBox) e.getSource();
			processSelection(cbBox, number);
		});
		checkBoxArray[panelNum] = checkBox;
		panel.add(checkBox, BorderLayout.PAGE_END);

		numberPanelArray[panelNum] = panel;
		return panel;
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		startButton = new JButton();
		startButton.setText("Sorsol");
		startButton.setEnabled(false);
		startButton.setPreferredSize(new Dimension(50, 50));
		startButton.addActionListener(e -> processLottery());
		panel.add(startButton);

		closeButton = new JButton();
		closeButton.setText("Bezár");
		closeButton.setPreferredSize(new Dimension(50, 50));
		closeButton.addActionListener(e -> System.exit(0));
		panel.add(closeButton);

		return panel;
	}

	private void processLottery() {
		initLottery();
		Set<Integer> results = task.processLottery();
		saveResults(results);
		showResults(results);
	}

	private void saveResults(Set<Integer> result) {
		Integer[] arr = new Integer[result.size()];
		result.toArray(arr);

		Number szamok = new Number();
		szamok.setSz1(arr[0]);
		szamok.setSz2(arr[1]);
		szamok.setSz3(arr[2]);
		szamok.setSz4(arr[3]);
		szamok.setSz5(arr[4]);
		szamok.setSz6(arr[5]);

		NumberModel model = new NumberModel();
		model.saveNyeroszamok(szamok);
	}

	private void initLottery() {
		for (JPanel panel : numberPanelArray) {
			panel.setBackground(mainPanel.getBackground());
		}
	}

	private void showResults(Set<Integer> result) {
		for (Integer resultNum : result) {
			numberPanelArray[resultNum - 1].setBackground(Color.GREEN);
		}
		JOptionPane.showMessageDialog(null, getDialogMessage(result));
	}

	private String getDialogMessage(Set<Integer> result) {
		StringBuilder sb = new StringBuilder();
		sb.append("A kisorsolt nyerőszámok: ")
			.append(result.stream().map(String::valueOf).collect(Collectors.joining(", ")))
			.append("\n")
			.append("Sikeresen eltalált számok száma: " + getNumOfSuccessTips(result));
		return sb.toString();
	}

	private int getNumOfSuccessTips(Set<Integer> result) {
		return selectionSet.stream()
			.filter(result::contains)
			.collect(Collectors.toSet()).size();
	}

	private void processSelection(JCheckBox cbBox, int number) {
		if (cbBox.isSelected()) {
			if (selectionSet.size() == DEFAULT_NUM_VALUES) {
				cbBox.setSelected(false);
				JOptionPane.showMessageDialog(null, "Összesen " + DEFAULT_NUM_VALUES + " számot lehet kiválasztani!");
			} else {
				selectionSet.add(number);
			}
		} else {
			selectionSet.remove(number);
		}
		startButton.setEnabled(selectionSet.size() == DEFAULT_NUM_VALUES);
	}

}
