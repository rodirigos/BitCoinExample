/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal.gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import principal.Wallet;

/**
 *
 * @author Rodrigo
 */
public class TableModelClientes extends AbstractTableModel {
    
    private List<Wallet> linhas;
    private String[] colunas = new String[] {
    "Id", "Moedas", "Preco"};
    
    
    
    public TableModelClientes(ArrayList<Wallet> carteiras){
        this.linhas = new ArrayList<>(carteiras);
     }
    
    
    public void setTable(ArrayList<Wallet> carteiras){
        this.linhas = new ArrayList<>(carteiras);
     }
    
    
    
     @Override
    public int getColumnCount() {
        // EstÃ¡ retornando o tamanho do array "colunas".
        return colunas.length;
    }
    /* Retorna a quantidade de linhas. */
    @Override
    public int getRowCount() {
        // Retorna o tamanho da lista de Cliente.
        return linhas.size();
    }
    @Override
    public String getColumnName(int columnIndex) {
        // Retorna o conteÃºdo do Array que possui o nome das colunas
        return colunas[columnIndex];
    }
    ;
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
    ;
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
            Wallet cliente = linhas.get(rowIndex);
        // Retorna o campo referente a coluna especificada.
        // Aqui é feito um switch para verificar qual é a coluna
        // e retornar o campo adequado. As colunas sãoas mesmas
        // que foram especificadas no array "colunas".
        switch (columnIndex) {
            // Seguindo o exemplo: "Tipo","Data de Cadastro", "Nome", "Idade"};
            case 0:
                return cliente.id;
            case 1:
                return cliente.getMoedas();
            case 2: 
                return cliente.preco;
            default:
                throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }
    }
 @Override
 //modifica na linha e coluna especificada
 public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      Wallet cliente = linhas.get(rowIndex); // Carrega o item da linha que deve ser modificado
     switch (columnIndex) { // Seta o valor do campo respectivo
         case 0:
             cliente.id = Integer.parseInt(aValue.toString());
         case 1:
             cliente.descontaMoedas(Integer.parseInt(aValue.toString()));
         case 2:
             cliente.preco = Integer.parseInt(aValue.toString());
         default:
                         
     }
     fireTableCellUpdated(rowIndex, columnIndex);
     }
    //modifica na linha especificada
    public void setValueAt(Wallet aValue, int rowIndex) {
        Wallet cliente = linhas.get(rowIndex); // Carrega o item da linha que deve ser modificado
        cliente.id = aValue.id;
        cliente.descontaMoedas(aValue.getMoedas());
        cliente.preco = aValue.preco;
        fireTableCellUpdated(rowIndex, 0);
        fireTableCellUpdated(rowIndex, 1);
         fireTableCellUpdated(rowIndex, 2);
    }
    ;
    ;
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    public Wallet getCliente(int indiceLinha) {
        return linhas.get(indiceLinha);
    }
    /* Adiciona um registro. */
    public void addCliente(Wallet m) {
        // Adiciona o registro.
        linhas.add(m);
        int ultimoIndice = getRowCount() - 1;
        fireTableRowsInserted(ultimoIndice, ultimoIndice);
    }
    /* Remove a linha especificada. */
    public void removeCliente(int indiceLinha) {
        linhas.remove(indiceLinha);
        fireTableRowsDeleted(indiceLinha, indiceLinha);
    }
    /* Adiciona uma lista de Cliente ao final dos registros. */
    public void addListaDeCliente(List<Wallet> cliente) {
        // Pega o tamanho antigo da tabela.
        int tamanhoAntigo = getRowCount();
        // Adiciona os registros.
        linhas.addAll(cliente);
        fireTableRowsInserted(tamanhoAntigo, getRowCount() - 1);
    }
    /* Remove todos os registros. */
    public void limpar() {
        linhas.clear();
        fireTableDataChanged();
    }
    /* Verifica se este table model esta vazio. */
    public boolean isEmpty() {
        return linhas.isEmpty();
    }
}
