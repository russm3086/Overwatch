package com.russ.test;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * This program demonstrates how to sort rows in a table.
 * 
 * @author www.codejava.net
 *
 */
public class JTableSortingExample extends JFrame {
	private JTable table;

	public JTableSortingExample() {
		super("JTable Sorting Example");

		List<Employee> listEmployees = createListEmployees();
		TableModel tableModel = new EmployeeTableModel(listEmployees);
		table = new JTable(tableModel);
		table.setAutoCreateRowSorter(true);
		
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		 
		int columnIndexToSort = 1;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		 
		sorter.setSortKeys(sortKeys);
		sorter.sort();
		

		// insert code for sorting here...

		add(new JScrollPane(table), BorderLayout.CENTER);

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public List<Employee> createListEmployees() {
		List<Employee> listEmployees = new ArrayList<>();

		listEmployees.add(new Employee("Joe","Plumber", 50));
		listEmployees.add(new Employee("Bubba","Doctor", 70));
		listEmployees.add(new Employee("Mike","NA", 25));
		listEmployees.add(new Employee("Sue","Engineer", 35));
		listEmployees.add(new Employee("Kat","Police", 25));
		listEmployees.add(new Employee("Gunter","Politian", 45));
		
		
		// code to add dummy data here...

		return listEmployees;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new JTableSortingExample().setVisible(true);
			}
		});
	}
}