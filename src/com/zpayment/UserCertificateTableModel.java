package com.zpayment;

import database.Certificate;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Julia on 25.05.2015.
 */
public class UserCertificateTableModel implements TableModel {
    private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
    private ArrayList<Certificate> certificates;

    public UserCertificateTableModel(ArrayList <Certificate> certificates) {
        this.certificates = certificates;
    }

    public void addTableModelListener(TableModelListener listener) {
        listeners.add(listener);
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public int getColumnCount() {
        return 4;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Организация";
            case 1:
                return "Дата создания";
            case 2:
                return "Статус";
            case 3:
                return "Файл";
        }
        return "";
    }

    public int getRowCount() {
        return certificates.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Certificate certificate = certificates.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return certificate.getOrganization();
            case 1:
                return certificate.getTimestamp();
            case 2:
                String status = "отозван";
                if (certificate.getStatus()) status = "активный";
                return status;
            case 3:
                return certificate.getFilename();
        }
        return "";
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {

    }
}
