package views;

import java.awt.Color;
import models.CarregarTableCarro;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultFormatterFactory;
import models.ClsCarros;
import models.ClsControlaCpNumeric;
import models.ClsLogin;

/**
 * Barema dos Status dos Carros Status 0 Liberado Status 1 Alugado
 *
 * Inativo: 1 para inativado,  0 para ativado
 *
 * @author Tiago Teixeira
 */
public class JfrmVeiculos extends javax.swing.JFrame {

    // variaveis responsaveis por herdar os dados da classe de Login
    private String userLoged;
    private int userIdLoged;
    private String CpfUserLoged;

    // variaveis auxiliares 
    private boolean editando;
    private boolean precionado;
    List<ClsCarros> listaCarros;
    private int linhaIndice;
    
    //variaveis de classes a serem usadas na tela
    controls.CarrosDAO carrosDAO;
    models.ClsCarros clscarros;
    models.ClsMascaraCampos clsMascaracampos;
    SimpleDateFormat formatoBr = new SimpleDateFormat("dd-MM-yyyy");
    Locale locale;
    NumberFormat FormatterMoeda;
    models.ClsValidacoes clsValidacoes;
    CarregarTableCarro modeloTable;

    public JfrmVeiculos() {
        initComponents();
        setIcon();
    }

    public JfrmVeiculos(ClsLogin clslogin) {
        //carregando as variaves de usuario

        userLoged = clslogin.getUserLoged();
        userIdLoged = clslogin.getId();
        CpfUserLoged = clslogin.getCpfUserLoged();

        //carregando os objetos auxiliares
        carrosDAO = new controls.CarrosDAO();
        clscarros = new models.ClsCarros();
        clsMascaracampos = new models.ClsMascaraCampos();
        clsValidacoes = new models.ClsValidacoes();
        clscarros.setId_colaborador(userIdLoged);
        locale = new Locale("pt", "BR");
        FormatterMoeda = NumberFormat.getCurrencyInstance(locale);
        listaCarros = new ArrayList<ClsCarros>(carrosDAO.selectAll());
        modeloTable = new CarregarTableCarro(listaCarros);
        clscarros.setInativo(0);
        clscarros.setStatus(0);

        // executando componentes e metodos da Jframe
        initComponents();
        try {
            addMascara();
        } catch (ParseException e) {
            System.out.println("Erro aqui: "+ e);
        }
        controleDigitacao();
        setIcon();
        disableControl();
        carregarJtable();
        precionado = false;
    }
    
    //verificando status do cadastro
    private boolean verificaStatusInativo(int valor){
        if(clscarros.getInativo() == 0){
        return false;
        }else{
            return true;
        }
    }

    // metodos auxiliares para funcionamento da tela
    private void setCarroTipo() {
        //jCboTipo.removeAllItems();
        jCboTipo.addItem("Selecione");
        jCboTipo.addItem("PASSEIO HATCH");
        jCboTipo.addItem("PASSEIO SEDAN");
        jCboTipo.addItem("UTILITARIO SUV");
        jCboTipo.addItem("CAMINHONETE");
    }

    private void setCarroClasse() {
        //jCboClasse.removeAllItems();
        jCboClasse.addItem("Selecione");
        jCboClasse.addItem("POPULAR");
        jCboClasse.addItem("MEDIO");
        jCboClasse.addItem("LUXO");
    }

    private void setIconBtnNv(boolean funcao) {
        if (funcao == true) {
            JbtnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icone_cancelar.png"))); // NOI18N
            JbtnNovo.setToolTipText("Clique aqui para cancelar a operacao");
        } else {
            JbtnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/add_121935.png"))); // NOI18N
            JbtnNovo.setToolTipText("Clique aqui para novo Veiculo");
        }
    }

    private void disableControl() {
        //desabilitando os controles e campos da tela
        jCboTipo.setEnabled(false);
        jCboClasse.setEnabled(false);
        jCkbInativar.setEnabled(false);
        jTblVeiculos.setEnabled(true);
        jTxtAnoFabricacao.setEnabled(false);
        jTxtAnoModelo.setEnabled(false);
        jTxtChassi.setEnabled(false);
        jTxtCor.setEnabled(false);
        jTxtKm.setEnabled(false);
        jTxtMarca.setEnabled(false);
        jTxtNome.setEnabled(false);
        jTxtNumeroRenavan.setEnabled(false);
        jTxtObservacoes.setEnabled(false);
        jTxtPlaca.setEnabled(false);
        jTxtPlacaBusca.setEnabled(true);
        jTxtValorDiaria.setEnabled(false);
        jTxtValorKmRodado.setEnabled(false);
        jTxtValorMercado.setEnabled(false);
        jTxtValorSeguro.setEnabled(false);
        jTxtVeiculo.setEnabled(false);
        JfTxtData.setEnabled(false);

        //desabilitando os controles botões
        JbtnEditar.setEnabled(false);
        JbtnExcluir.setEnabled(false);
        JbtnSalvar.setEnabled(false);
        JbtnNovo.setEnabled(true);
        jBtnBuscar.setEnabled(true);

    }

    private void enableControl() {
        //desabilitando os controles e campos da tela
        jCboTipo.setEnabled(true);
        jCboClasse.setEnabled(true);
        jCkbInativar.setEnabled(true);
        jTblVeiculos.setEnabled(true);
        jTxtAnoFabricacao.setEnabled(true);
        jTxtAnoModelo.setEnabled(true);
        jTxtChassi.setEnabled(true);
        jTxtCor.setEnabled(true);
        jTxtKm.setEnabled(true);
        jTxtMarca.setEnabled(true);
        jTxtNome.setEnabled(true);
        jTxtNumeroRenavan.setEnabled(true);
        jTxtObservacoes.setEnabled(true);
        jTxtPlaca.setEnabled(true);
        jTxtPlacaBusca.setEnabled(true);
        jTxtValorDiaria.setEnabled(true);
        jTxtValorKmRodado.setEnabled(true);
        jTxtValorMercado.setEnabled(true);
        jTxtValorSeguro.setEnabled(true);
        jTxtVeiculo.setEnabled(true);
        JfTxtData.setEnabled(true);

        //desabilitando os controles botões
        JbtnEditar.setEnabled(false);
        JbtnExcluir.setEnabled(false);
        JbtnSalvar.setEnabled(true);
        JbtnNovo.setEnabled(true);
        jBtnBuscar.setEnabled(true);

    }

    private void enableControlBusca() {
        //desabilitando os controles e campos da tela
        jCboTipo.setEnabled(true);
        jCboClasse.setEnabled(true);
        jCkbInativar.setEnabled(true);
        jTblVeiculos.setEnabled(true);
        jTxtAnoFabricacao.setEnabled(true);
        jTxtAnoModelo.setEnabled(true);
        jTxtChassi.setEnabled(true);
        jTxtCor.setEnabled(true);
        jTxtKm.setEnabled(true);
        jTxtMarca.setEnabled(true);
        jTxtNome.setEnabled(true);
        jTxtNumeroRenavan.setEnabled(true);
        jTxtObservacoes.setEnabled(true);
        jTxtPlaca.setEnabled(true);
        jTxtPlacaBusca.setEnabled(true);
        jTxtValorDiaria.setEnabled(true);
        jTxtValorKmRodado.setEnabled(true);
        jTxtValorMercado.setEnabled(true);
        jTxtValorSeguro.setEnabled(true);
        jTxtVeiculo.setEnabled(true);
        JfTxtData.setEnabled(true);

        //desabilitando os controles botões
        JbtnEditar.setEnabled(true);
        JbtnExcluir.setEnabled(true);
        JbtnSalvar.setEnabled(true);
        JbtnNovo.setEnabled(true);
        jBtnBuscar.setEnabled(true);

    }

    private void clearTxt() {

        jTxtAnoFabricacao.setText("");
        jTxtAnoModelo.setText("");
        jTxtChassi.setText("");
        jTxtCor.setText("");
        jTxtKm.setText("");
        jTxtMarca.setText("");
        jTxtNome.setText("");
        jTxtNumeroRenavan.setText("");
        jTxtObservacoes.setText("");
        jTxtPlaca.setText("");
        jTxtPlacaBusca.setText("");
        jTxtValorDiaria.setText("");
        jTxtValorKmRodado.setText("");
        jTxtValorMercado.setText("");
        jTxtValorSeguro.setText("");
        jTxtVeiculo.setText("");
    }

    private void msgObgCampo(String dado) {
        JOptionPane.showMessageDialog(this, "Olá " + userLoged + " esse dado: " + dado + " é obrigatório", "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    private void msgAdvCampo(String dado) {
        JOptionPane.showMessageDialog(this, "Olá " + userLoged + " esse dado: " + dado + " está maior ou menor do que o permitido!", "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addMascara() throws ParseException {
        JfTxtData.setFormatterFactory(new DefaultFormatterFactory(clsMascaracampos.mascaraData(JfTxtData)));
    }

    private void controleDigitacao() {
        //todos os Jtext que estiver aqui só vão aceitar numeros e pontos
        jTxtKm.setDocument(new ClsControlaCpNumeric());
        jTxtAnoFabricacao.setDocument(new ClsControlaCpNumeric());
        jTxtAnoModelo.setDocument(new ClsControlaCpNumeric());
        jTxtValorDiaria.setDocument(new ClsControlaCpNumeric());
        jTxtValorKmRodado.setDocument(new ClsControlaCpNumeric());
        jTxtValorMercado.setDocument(new ClsControlaCpNumeric());
        jTxtValorSeguro.setDocument(new ClsControlaCpNumeric());
        jTxtNumeroRenavan.setDocument(new ClsControlaCpNumeric());

    }

    private boolean validaCampos() {
        //valida campo Nome
        if (jTxtNome.getText().length() < 1) {
            msgObgCampo("Nome");
            jTxtNome.requestFocus();
            return false;
        }else
        if (jTxtNome.getText().length() > 49) {
            msgAdvCampo("Nome");
            jTxtNome.requestFocus();
            return false;
        }else
        //valida campo modelo
        if (jTxtVeiculo.getText().length() < 1) {
            msgObgCampo("Modelo");
            jTxtVeiculo.requestFocus();
            return false;
        }else
        if (jTxtVeiculo.getText().length() > 49) {
            msgAdvCampo("Modelo");
            jTxtVeiculo.requestFocus();
            return false;
        }else
        //valida campo marca
        if (jTxtMarca.getText().length() < 1) {
            msgObgCampo("Marca");
            jTxtMarca.requestFocus();
            return false;
        }else
        if (jTxtMarca.getText().length() > 49) {
            msgAdvCampo("Marca");
            jTxtMarca.requestFocus();
            return false;
        }else
        //valida campo cor
        if (jTxtCor.getText().length() < 1) {
            msgObgCampo("Cor");
            jTxtCor.requestFocus();
            return false;
        }else
        if (jTxtCor.getText().length() > 49) {
            msgAdvCampo("Cor");
            jTxtCor.requestFocus();
            return false;
        }else
        //valida campo marca
        if (jTxtPlaca.getText().length() < 1) {
            msgObgCampo("Placa");
            jTxtPlaca.requestFocus();
            return false;
        }else
        if (jTxtPlaca.getText().length() > 7) {
            msgAdvCampo("Placa");
            jTxtPlaca.requestFocus();
            return false;
        }else
        if (jTxtPlaca.getText().length() < 7) {
            msgAdvCampo("Placa");
            jTxtPlaca.requestFocus();
            return false;
        }else
        //valida campo chassi
        
        if (jTxtChassi.getText().length() < 1) {
            msgObgCampo("Chassi");
            jTxtChassi.requestFocus();
            return false;
        }else
        if (jTxtChassi.getText().length() > 49) {
            msgAdvCampo("Chassi");
            jTxtChassi.requestFocus();
            return false;
        }else
        //valida campo km rodado
        if (jTxtKm.getText().length() < 1) {
            msgObgCampo("KmRodados");
            jTxtKm.requestFocus();
            return false;
        }else
        if (jTxtKm.getText().length() > 9) {
            msgAdvCampo("KmRodados");
            jTxtKm.requestFocus();
            return false;
        }else
        //valida campo AnoModelo
        if (jTxtAnoModelo.getText().length() < 1) {
            msgObgCampo("AnoModelo");
            jTxtAnoModelo.requestFocus();
            return false;
        }else
        if (jTxtAnoModelo.getText().length() > 4) {
            msgAdvCampo("AnoModelo");
            jTxtAnoModelo.requestFocus();
            return false;
        }else
        if (jTxtAnoModelo.getText().length() < 4) {
            msgAdvCampo("AnoModelo");
            jTxtAnoModelo.requestFocus();
            return false;
        }else
        //valida campo AnoFabricacao
        if (jTxtAnoFabricacao.getText().length() < 1) {
            msgObgCampo("AnoFabricacao");
            jTxtAnoFabricacao.requestFocus();
            return false;
        }else
        if (jTxtAnoFabricacao.getText().length() > 4) {
            msgAdvCampo("AnoFabricacao");
            jTxtAnoFabricacao.requestFocus();
            return false;
        }else
        if (jTxtAnoFabricacao.getText().length() < 4) {
            msgAdvCampo("AnoFabricacao");
            jTxtAnoFabricacao.requestFocus();
            return false;
        }else
        //Valida campo ValorMercado
        if (jTxtValorMercado.getText().length() < 1) {
            msgObgCampo("ValorMercado");
            jTxtValorMercado.requestFocus();
            return false;
        }else
        if (jTxtValorMercado.getText().length() > 20) {
            msgAdvCampo("ValorMercado");
            jTxtValorMercado.requestFocus();
            return false;
        }else
        // valida campo ValorSeguro
        if (jTxtValorSeguro.getText().length() < 1) {
            msgObgCampo("ValorSeguro");
            jTxtValorSeguro.requestFocus();
            return false;
        }else
        if (jTxtValorSeguro.getText().length() > 20) {
            msgAdvCampo("ValorSeguro");
            jTxtValorSeguro.requestFocus();
            return false;
        }else
        //valida campo ValorKmRodado
        if (jTxtValorKmRodado.getText().length() < 1) {
            msgObgCampo("ValorKmRodado");
            jTxtValorKmRodado.requestFocus();
            return false;
        }else
        if (jTxtValorKmRodado.getText().length() > 20) {
            msgAdvCampo("ValorKmRodado");
            jTxtValorKmRodado.requestFocus();
            return false;
        }else
        //valida campo ValorDiaria
        if (jTxtValorDiaria.getText().length() < 1) {
            msgObgCampo("ValorDiaria");
            jTxtValorDiaria.requestFocus();
            return false;
        }else
        if (jTxtValorDiaria.getText().length() > 20) {
            msgAdvCampo("ValorDiaria");
            jTxtValorDiaria.requestFocus();
            return false;
        }else 
        //valida campo Numero Renavan
        if (jTxtNumeroRenavan.getText().length() < 1) {
            msgObgCampo("NumeroRenavan");
            jTxtNumeroRenavan.requestFocus();
            return false;
        }else
        if (jTxtNumeroRenavan.getText().length() > 30) {
            msgAdvCampo("NumeroRenavan");
            jTxtNumeroRenavan.requestFocus();
            return false;
        }else
        if (jTxtNumeroRenavan.getText().length() < 9) {
            msgAdvCampo("NumeroRenavan");
            jTxtNumeroRenavan.requestFocus();
            return false;
        }else
        return true;

    }

    private void carregarJtable() {

        jTblVeiculos.setModel(modeloTable);
    }
    
    private void removeLinhaJtable(int indice) {
        
        modeloTable.deleteRow(indice);
    }

    private void atualizarJtable() {

        modeloTable.addRow(clscarros);
    }
    
    private void atualizaListaCarros(int indice){
        listaCarros.set(indice, clscarros);
        modeloTable.updatedRow(indice, indice);
    }
    
    private void limpaJtable() {
        jTblVeiculos.setModel(modeloTable);
    }
    
    private void carregarFrame() {

        jLabelCodigo.setText("Codigo: " + clscarros.getId());
        jTxtAnoFabricacao.setText("" + clscarros.getAnoFabricacao());
        jTxtAnoModelo.setText("" + clscarros.getAnoModelo());
        jTxtChassi.setText(clscarros.getChassi());
        jTxtCor.setText(clscarros.getCor());
        jTxtKm.setText("" + clscarros.getKmRodados());
        jTxtMarca.setText(clscarros.getMarca());
        jTxtNome.setText(clscarros.getNome());
        jTxtNumeroRenavan.setText("" + clscarros.getRenavam());
        jTxtObservacoes.setText(clscarros.getObsEstado());
        jTxtPlaca.setText(clscarros.getPlaca());
        jTxtValorDiaria.setText(FormatterMoeda.format(clscarros.getValorDiariaLoc()));
        jTxtValorKmRodado.setText(FormatterMoeda.format(clscarros.getValorKmRd()));
        jTxtValorMercado.setText(FormatterMoeda.format(clscarros.getValorMercado()));
        jTxtValorSeguro.setText(FormatterMoeda.format(clscarros.getValorSeguro()));
        jTxtVeiculo.setText(clscarros.getModelo());
        JfTxtData.setText(clsValidacoes.dataFormatoBR(clsValidacoes.dataFormatoUS(clscarros.getDataCompra())));
        setCarroClasse();
        setCarroTipo();
        jCboTipo.setSelectedItem(clscarros.getTipoVeiculo());
        jCboClasse.setSelectedItem(clscarros.getClasse());
    }
    
    private void carregarFrameLista(int indice) {
        //o indice é o objeto selecionado dentro da lista
        jLabelCodigo.setText("Codigo: " + listaCarros.get(indice).getId());
        jTxtAnoFabricacao.setText("" + listaCarros.get(indice).getAnoFabricacao());
        jTxtAnoModelo.setText("" + listaCarros.get(indice).getAnoModelo());
        jTxtChassi.setText(listaCarros.get(indice).getChassi());
        jTxtCor.setText(listaCarros.get(indice).getCor());
        jTxtKm.setText("" + listaCarros.get(indice).getKmRodados());
        jTxtMarca.setText(listaCarros.get(indice).getMarca());
        jTxtNome.setText(listaCarros.get(indice).getNome());
        jTxtNumeroRenavan.setText("" + listaCarros.get(indice).getRenavam());
        jTxtObservacoes.setText(listaCarros.get(indice).getObsEstado());
        jTxtPlaca.setText(listaCarros.get(indice).getPlaca());
        jTxtValorDiaria.setText(FormatterMoeda.format(listaCarros.get(indice).getValorDiariaLoc()));
        jTxtValorKmRodado.setText(FormatterMoeda.format(listaCarros.get(indice).getValorKmRd()));
        jTxtValorMercado.setText(FormatterMoeda.format(listaCarros.get(indice).getValorMercado()));
        jTxtValorSeguro.setText(FormatterMoeda.format(listaCarros.get(indice).getValorSeguro()));
        jTxtVeiculo.setText(listaCarros.get(indice).getModelo());
        JfTxtData.setText(clsValidacoes.dataFormatoBR(clsValidacoes.dataFormatoUS(listaCarros.get(indice).getDataCompra())));
        setCarroClasse();
        setCarroTipo();
        jCboTipo.setSelectedItem(listaCarros.get(indice).getTipoVeiculo());
        jCboClasse.setSelectedItem(listaCarros.get(indice).getClasse());
        if(listaCarros.get(indice).getInativo()==1){
            jCkbInativar.setSelected(true);
             jLabelStatus.setText("Status: Veiculo Inativado");
            jLabelStatus.setForeground(Color.RED);
           
        }else if (listaCarros.get(indice).getInativo()==0){
            jCkbInativar.setSelected(false);
        }
        if(listaCarros.get(indice).getStatus() == 0 && listaCarros.get(indice).getInativo()==0){
            jLabelStatus.setText("Status: Liberado");
            jLabelStatus.setForeground(Color.GREEN);
  
        }else if (listaCarros.get(indice).getStatus() == 1){
            jLabelStatus.setText("Status: Alugado");
            jLabelStatus.setForeground(Color.BLUE);
        }
    }

    private void buscarPelaPlaca(String placa) {
        boolean encontrado = false;
        for (int i = 1; i > jTblVeiculos.getRowCount(); i++) {
            if (jTblVeiculos.getValueAt(i, 2).toString().equals(placa)) {
                clscarros = carrosDAO.select(jTblVeiculos.getValueAt(i, 2).toString());
                carregarFrame();
                enableControlBusca();
                encontrado = true;

            } else {
                if (i == jTblVeiculos.getRowCount() && encontrado == false) {
                    JOptionPane.showMessageDialog(this, "Veiculo não encontrado", "ERRO", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JbtnNovo = new javax.swing.JButton();
        JbtnEditar = new javax.swing.JButton();
        JbtnSalvar = new javax.swing.JButton();
        JbtnExcluir = new javax.swing.JButton();
        jPaneDadosGerais = new javax.swing.JPanel();
        jTxtNome = new javax.swing.JTextField();
        jTxtVeiculo = new javax.swing.JTextField();
        jTxtMarca = new javax.swing.JTextField();
        jTxtCor = new javax.swing.JTextField();
        jTxtPlaca = new javax.swing.JTextField();
        jTxtChassi = new javax.swing.JTextField();
        jTxtKm = new javax.swing.JTextField();
        jTxtAnoModelo = new javax.swing.JTextField();
        jTxtAnoFabricacao = new javax.swing.JTextField();
        jCboClasse = new javax.swing.JComboBox<>();
        jCboTipo = new javax.swing.JComboBox<>();
        jTxtNumeroRenavan = new javax.swing.JTextField();
        jTxtObservacoes = new javax.swing.JTextField();
        JfTxtData = new javax.swing.JFormattedTextField();
        jLabelCodigo = new javax.swing.JLabel();
        jCkbInativar = new javax.swing.JCheckBox();
        jPanelDadosValores = new javax.swing.JPanel();
        jTxtValorSeguro = new javax.swing.JTextField();
        jTxtValorKmRodado = new javax.swing.JTextField();
        jTxtValorDiaria = new javax.swing.JTextField();
        jPanelBusca = new javax.swing.JPanel();
        jTxtPlacaBusca = new javax.swing.JTextField();
        jBtnBuscar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTxtValorMercado = new javax.swing.JFormattedTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTblVeiculos = new javax.swing.JTable();
        jLabelStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro e Movimentação de Veiculos/Patrimonios");
        setResizable(false);
        setSize(new java.awt.Dimension(1055, 630));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        JbtnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/add_121935.png"))); // NOI18N
        JbtnNovo.setToolTipText("Clique aqui para novo veiculo");
        JbtnNovo.setBorder(null);
        JbtnNovo.setFocusPainted(false);
        JbtnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JbtnNovoActionPerformed(evt);
            }
        });

        JbtnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/new_121792.png"))); // NOI18N
        JbtnEditar.setToolTipText("Clique aqui para editar veiculo");
        JbtnEditar.setBorder(null);
        JbtnEditar.setFocusPainted(false);
        JbtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JbtnEditarActionPerformed(evt);
            }
        });

        JbtnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/save_121760.png"))); // NOI18N
        JbtnSalvar.setToolTipText("Clique aqui para salvar veiculo");
        JbtnSalvar.setBorder(null);
        JbtnSalvar.setFocusPainted(false);
        JbtnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JbtnSalvarActionPerformed(evt);
            }
        });

        JbtnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/bin_121907.png"))); // NOI18N
        JbtnExcluir.setToolTipText("Clique aqui para excluir veiculo");
        JbtnExcluir.setBorder(null);
        JbtnExcluir.setFocusPainted(false);
        JbtnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JbtnExcluirActionPerformed(evt);
            }
        });

        jPaneDadosGerais.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados Gerais"));

        jTxtNome.setBackground(new java.awt.Color(240, 240, 240));
        jTxtNome.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtNome.setToolTipText("Digite o nome do veiculo");
        jTxtNome.setBorder(javax.swing.BorderFactory.createTitledBorder("Nome"));
        jTxtNome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtNomeFocusLost(evt);
            }
        });

        jTxtVeiculo.setBackground(new java.awt.Color(240, 240, 240));
        jTxtVeiculo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtVeiculo.setToolTipText("Digite o modelo do veiculo");
        jTxtVeiculo.setBorder(javax.swing.BorderFactory.createTitledBorder("Modelo"));
        jTxtVeiculo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtVeiculoFocusLost(evt);
            }
        });

        jTxtMarca.setBackground(new java.awt.Color(240, 240, 240));
        jTxtMarca.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtMarca.setToolTipText("Digite a marca do veiculo");
        jTxtMarca.setBorder(javax.swing.BorderFactory.createTitledBorder("Marca"));
        jTxtMarca.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtMarcaFocusLost(evt);
            }
        });

        jTxtCor.setBackground(new java.awt.Color(240, 240, 240));
        jTxtCor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtCor.setToolTipText("Digite a cor do veiculo");
        jTxtCor.setBorder(javax.swing.BorderFactory.createTitledBorder("Cor"));
        jTxtCor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtCorFocusLost(evt);
            }
        });

        jTxtPlaca.setBackground(new java.awt.Color(240, 240, 240));
        jTxtPlaca.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtPlaca.setToolTipText("Digite a placa do veiculo");
        jTxtPlaca.setBorder(javax.swing.BorderFactory.createTitledBorder("Placa"));
        jTxtPlaca.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtPlacaFocusLost(evt);
            }
        });

        jTxtChassi.setBackground(new java.awt.Color(240, 240, 240));
        jTxtChassi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtChassi.setToolTipText("Digite o numero do chassi do veiculo");
        jTxtChassi.setBorder(javax.swing.BorderFactory.createTitledBorder("Chassi"));
        jTxtChassi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtChassiFocusLost(evt);
            }
        });

        jTxtKm.setBackground(new java.awt.Color(240, 240, 240));
        jTxtKm.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtKm.setToolTipText("Digite a KM rodada do veiculo");
        jTxtKm.setBorder(javax.swing.BorderFactory.createTitledBorder("Km Rodado"));
        jTxtKm.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtKmFocusLost(evt);
            }
        });

        jTxtAnoModelo.setBackground(new java.awt.Color(240, 240, 240));
        jTxtAnoModelo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtAnoModelo.setToolTipText("Digite ano modelo do veiculo");
        jTxtAnoModelo.setBorder(javax.swing.BorderFactory.createTitledBorder("Ano Modelo"));
        jTxtAnoModelo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtAnoModeloFocusLost(evt);
            }
        });

        jTxtAnoFabricacao.setBackground(new java.awt.Color(240, 240, 240));
        jTxtAnoFabricacao.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtAnoFabricacao.setToolTipText("Digite ano fabricação do veiculo");
        jTxtAnoFabricacao.setBorder(javax.swing.BorderFactory.createTitledBorder("Ano Fabricacao"));
        jTxtAnoFabricacao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtAnoFabricacaoFocusLost(evt);
            }
        });

        jCboClasse.setBackground(new java.awt.Color(240, 240, 240));
        jCboClasse.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCboClasse.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        jCboClasse.setToolTipText("Selecione a classe do veiculo");
        jCboClasse.setBorder(javax.swing.BorderFactory.createTitledBorder("Classe"));
        jCboClasse.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCboClasseItemStateChanged(evt);
            }
        });

        jCboTipo.setBackground(new java.awt.Color(240, 240, 240));
        jCboTipo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCboTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        jCboTipo.setToolTipText("Selecione o Tipo de veiculo");
        jCboTipo.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo"));
        jCboTipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCboTipoItemStateChanged(evt);
            }
        });

        jTxtNumeroRenavan.setBackground(new java.awt.Color(240, 240, 240));
        jTxtNumeroRenavan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtNumeroRenavan.setToolTipText("Digite o numero RENAVAN do veiculo");
        jTxtNumeroRenavan.setBorder(javax.swing.BorderFactory.createTitledBorder("RENAVAN"));
        jTxtNumeroRenavan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtNumeroRenavanFocusLost(evt);
            }
        });

        jTxtObservacoes.setBackground(new java.awt.Color(240, 240, 240));
        jTxtObservacoes.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtObservacoes.setToolTipText("Digite breve observação sobre o veiculo");
        jTxtObservacoes.setBorder(javax.swing.BorderFactory.createTitledBorder("Observações/Estado"));
        jTxtObservacoes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtObservacoesFocusLost(evt);
            }
        });

        JfTxtData.setBackground(new java.awt.Color(240, 240, 240));
        JfTxtData.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Compra"));
        JfTxtData.setToolTipText("Inserir data de aquisição do Veiculo!");
        JfTxtData.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        JfTxtData.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                JfTxtDataFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPaneDadosGeraisLayout = new javax.swing.GroupLayout(jPaneDadosGerais);
        jPaneDadosGerais.setLayout(jPaneDadosGeraisLayout);
        jPaneDadosGeraisLayout.setHorizontalGroup(
            jPaneDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPaneDadosGeraisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPaneDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPaneDadosGeraisLayout.createSequentialGroup()
                        .addComponent(jTxtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtVeiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtCor, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addComponent(jCboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCboClasse, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPaneDadosGeraisLayout.createSequentialGroup()
                        .addComponent(jTxtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtChassi, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtKm, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jTxtAnoModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jTxtAnoFabricacao, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JfTxtData, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPaneDadosGeraisLayout.createSequentialGroup()
                        .addComponent(jTxtNumeroRenavan, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtObservacoes)))
                .addContainerGap())
        );
        jPaneDadosGeraisLayout.setVerticalGroup(
            jPaneDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPaneDadosGeraisLayout.createSequentialGroup()
                .addGroup(jPaneDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTxtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtVeiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtCor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCboClasse, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPaneDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTxtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtChassi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtKm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtAnoModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtAnoFabricacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JfTxtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPaneDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTxtNumeroRenavan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtObservacoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabelCodigo.setText("Codigo:");
        jLabelCodigo.setToolTipText("");

        jCkbInativar.setMnemonic('0');
        jCkbInativar.setText("Inativar");
        jCkbInativar.setToolTipText("Clique aqui para inativar o veiculo");
        jCkbInativar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCkbInativarItemStateChanged(evt);
            }
        });

        jPanelDadosValores.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados Valores"));

        jTxtValorSeguro.setBackground(new java.awt.Color(240, 240, 240));
        jTxtValorSeguro.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtValorSeguro.setToolTipText("Digite o valor do seguro anual do veiculo");
        jTxtValorSeguro.setBorder(javax.swing.BorderFactory.createTitledBorder("Valor Seguro"));
        jTxtValorSeguro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtValorSeguroFocusLost(evt);
            }
        });
        jTxtValorSeguro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTxtValorSeguroMouseClicked(evt);
            }
        });

        jTxtValorKmRodado.setBackground(new java.awt.Color(240, 240, 240));
        jTxtValorKmRodado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtValorKmRodado.setToolTipText("Digite o valor de cada KM rodado do veiculo");
        jTxtValorKmRodado.setBorder(javax.swing.BorderFactory.createTitledBorder("Valor Km Rodado"));
        jTxtValorKmRodado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtValorKmRodadoFocusLost(evt);
            }
        });
        jTxtValorKmRodado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTxtValorKmRodadoMouseClicked(evt);
            }
        });

        jTxtValorDiaria.setBackground(new java.awt.Color(240, 240, 240));
        jTxtValorDiaria.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtValorDiaria.setToolTipText("Digite o valor da diária do veiculo");
        jTxtValorDiaria.setBorder(javax.swing.BorderFactory.createTitledBorder("Valor Diaria"));
        jTxtValorDiaria.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtValorDiariaFocusLost(evt);
            }
        });
        jTxtValorDiaria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTxtValorDiariaMouseClicked(evt);
            }
        });

        jPanelBusca.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar Placa"));

        jTxtPlacaBusca.setBackground(new java.awt.Color(240, 240, 240));
        jTxtPlacaBusca.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtPlacaBusca.setToolTipText("Digite a placa para realizar a busca");
        jTxtPlacaBusca.setBorder(javax.swing.BorderFactory.createTitledBorder("Placa "));

        jBtnBuscar.setText("Localizar");
        jBtnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBtnBuscarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelBuscaLayout = new javax.swing.GroupLayout(jPanelBusca);
        jPanelBusca.setLayout(jPanelBuscaLayout);
        jPanelBuscaLayout.setHorizontalGroup(
            jPanelBuscaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBuscaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTxtPlacaBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );
        jPanelBuscaLayout.setVerticalGroup(
            jPanelBuscaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBuscaLayout.createSequentialGroup()
                .addGroup(jPanelBuscaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jBtnBuscar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTxtPlacaBusca, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel1.setText("Patrimonios/Veiculos Cadastrados");

        jTxtValorMercado.setBackground(new java.awt.Color(240, 240, 240));
        jTxtValorMercado.setBorder(javax.swing.BorderFactory.createTitledBorder("Valor Mercado/Bem"));
        jTxtValorMercado.setToolTipText("Digite valor de mercado do veiculo");
        jTxtValorMercado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtValorMercado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtValorMercadoFocusLost(evt);
            }
        });
        jTxtValorMercado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTxtValorMercadoMouseClicked(evt);
            }
        });

        jTblVeiculos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTblVeiculosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTblVeiculos);

        javax.swing.GroupLayout jPanelDadosValoresLayout = new javax.swing.GroupLayout(jPanelDadosValores);
        jPanelDadosValores.setLayout(jPanelDadosValoresLayout);
        jPanelDadosValoresLayout.setHorizontalGroup(
            jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDadosValoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDadosValoresLayout.createSequentialGroup()
                        .addGroup(jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanelDadosValoresLayout.createSequentialGroup()
                                .addComponent(jTxtValorMercado, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTxtValorSeguro, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTxtValorKmRodado, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTxtValorDiaria, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(36, 36, 36)
                        .addComponent(jPanelBusca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanelDadosValoresLayout.setVerticalGroup(
            jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDadosValoresLayout.createSequentialGroup()
                .addGroup(jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelDadosValoresLayout.createSequentialGroup()
                        .addComponent(jPanelBusca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(jPanelDadosValoresLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtValorSeguro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTxtValorKmRodado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTxtValorDiaria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTxtValorMercado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(7, 7, 7)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabelStatus.setText("Status:");
        jLabelStatus.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jCkbInativar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(JbtnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(JbtnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JbtnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JbtnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelCodigo)
                            .addComponent(jLabelStatus))
                        .addGap(47, 47, 47))
                    .addComponent(jPaneDadosGerais, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelDadosValores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JbtnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JbtnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JbtnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JbtnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelCodigo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelStatus)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCkbInativar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPaneDadosGerais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDadosValores, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // eventos dos componentes da tela
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        models.ClsLogin clslogin = new ClsLogin();
        clslogin.setUserLoged(userLoged);
        clslogin.setId(userIdLoged);
        clslogin.setCpfUserLoged(CpfUserLoged);
        views.JfrmPrincipal telaprincipal = new views.JfrmPrincipal(clslogin);
        telaprincipal.setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void JbtnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JbtnNovoActionPerformed
        if (precionado == false) {
            setIconBtnNv(true);
            enableControl();
            clearTxt();
            limpaJtable();
            precionado = true;
            editando = false;
            jTxtNome.requestFocus();
            setCarroClasse();
            setCarroTipo();
        } else {
            setIconBtnNv(false);
            disableControl();
            clearTxt();
            precionado = false;
            editando = false;
        }

    }//GEN-LAST:event_JbtnNovoActionPerformed

    private void jTxtNomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtNomeFocusLost
        if ("".equals(jTxtNome.getText())) {
        } else {
            clscarros.setNome(jTxtNome.getText());
        }
    }//GEN-LAST:event_jTxtNomeFocusLost

    private void jTxtVeiculoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtVeiculoFocusLost
        if ("".equals(jTxtVeiculo.getText())) {
        } else {
            clscarros.setModelo(jTxtVeiculo.getText());
        }

    }//GEN-LAST:event_jTxtVeiculoFocusLost

    private void jTxtMarcaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtMarcaFocusLost
        if ("".equals(jTxtMarca.getText())) {
        } else {
            clscarros.setMarca(jTxtMarca.getText());
        }

    }//GEN-LAST:event_jTxtMarcaFocusLost

    private void jTxtCorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtCorFocusLost
        if ("".equals(jTxtCor.getText())) {
        } else {
            clscarros.setCor(jTxtCor.getText());
        }

    }//GEN-LAST:event_jTxtCorFocusLost

    private void jTxtPlacaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtPlacaFocusLost
        if ("".equals(jTxtPlaca.getText())) {
        } else {
            clscarros.setPlaca(jTxtPlaca.getText());
        }

    }//GEN-LAST:event_jTxtPlacaFocusLost

    private void jCboTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCboTipoItemStateChanged
        if (jCboTipo.getSelectedIndex() > -1) {
            clscarros.setTipoVeiculo(jCboTipo.getSelectedItem().toString());
        }
    }//GEN-LAST:event_jCboTipoItemStateChanged

    private void jCboClasseItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCboClasseItemStateChanged
        if (jCboClasse.getSelectedIndex() > -1) {
            clscarros.setClasse(jCboClasse.getSelectedItem().toString());
        }
    }//GEN-LAST:event_jCboClasseItemStateChanged

    private void jTxtChassiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtChassiFocusLost
        if ("".equals(jTxtChassi.getText())) {
        } else {
            clscarros.setChassi(jTxtChassi.getText());
        }


    }//GEN-LAST:event_jTxtChassiFocusLost

    private void jTxtKmFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtKmFocusLost
        if ("".equals(Integer.parseInt(jTxtKm.getText()))) {
        } else {
            clscarros.setKmRodados(Integer.parseInt(jTxtKm.getText()));
        }


    }//GEN-LAST:event_jTxtKmFocusLost

    private void jTxtAnoModeloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtAnoModeloFocusLost
        if ("".equals(Integer.parseInt(jTxtAnoModelo.getText()))) {
        } else {
            clscarros.setAnoModelo(Integer.parseInt(jTxtAnoModelo.getText()));
        }
    }//GEN-LAST:event_jTxtAnoModeloFocusLost

    private void jTxtAnoFabricacaoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtAnoFabricacaoFocusLost
        if ("".equals(Integer.parseInt(jTxtAnoFabricacao.getText()))) {
        } else {
            clscarros.setAnoFabricacao(Integer.parseInt(jTxtAnoFabricacao.getText()));
        }
    }//GEN-LAST:event_jTxtAnoFabricacaoFocusLost

    private void JbtnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JbtnSalvarActionPerformed
        boolean validado = validaCampos();
        if (editando == false && validado == true) {
            carrosDAO.save(clscarros);
            JOptionPane.showMessageDialog(this, carrosDAO.getRetorno(), "Informação", JOptionPane.INFORMATION_MESSAGE);
            setIconBtnNv(false);
            precionado = false;
            editando = false;
            disableControl();
            atualizarJtable();
        } else {
            if (editando == true && validado == true) {
                carrosDAO.update(clscarros);
                atualizaListaCarros(jTblVeiculos.getSelectedRow());
                carregarFrameLista(linhaIndice);
                JOptionPane.showMessageDialog(this, carrosDAO.getRetorno(), "Informação", JOptionPane.INFORMATION_MESSAGE);
                setIconBtnNv(false);
                precionado = false;
                editando = false;
                disableControl();
            }
        }
    }//GEN-LAST:event_JbtnSalvarActionPerformed

    private void JfTxtDataFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_JfTxtDataFocusLost

        if (JfTxtData.getText().length() < 1) {
            msgObgCampo("Data Compra");
        } else {
            clscarros.setDataCompra(clsValidacoes.dataFormatoUS(JfTxtData.getText()));
        }
    }//GEN-LAST:event_JfTxtDataFocusLost

    private void jTxtValorSeguroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtValorSeguroFocusLost
        if ("".equals(jTxtValorSeguro.getText())) {
        } else {
            try {
                clscarros.setValorSeguro(clsValidacoes.formataMoeda(jTxtValorSeguro.getText()));
                jTxtValorSeguro.setText(FormatterMoeda.format(clscarros.getValorSeguro()));
            } catch (ParseException ex) {

            }
        }
    }//GEN-LAST:event_jTxtValorSeguroFocusLost

    private void jTxtValorKmRodadoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtValorKmRodadoFocusLost
        if ("".equals(jTxtValorKmRodado.getText())) {
        } else {
            try {
                clscarros.setValorKmRd(clsValidacoes.formataMoeda(jTxtValorKmRodado.getText()));
                jTxtValorKmRodado.setText(FormatterMoeda.format(clscarros.getValorKmRd()));
            } catch (ParseException ex) {

            }
        }
    }//GEN-LAST:event_jTxtValorKmRodadoFocusLost

    private void jTxtValorDiariaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtValorDiariaFocusLost
        if ("".equals(jTxtValorDiaria.getText())) {
        } else {
            try {
                clscarros.setValorDiariaLoc(clsValidacoes.formataMoeda(jTxtValorDiaria.getText()));
                jTxtValorDiaria.setText(FormatterMoeda.format(clscarros.getValorDiariaLoc()));
            } catch (ParseException ex) {

            }
        }
    }//GEN-LAST:event_jTxtValorDiariaFocusLost

    private void jTxtValorMercadoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtValorMercadoFocusLost

        if ("".equals(jTxtValorMercado.getText())) {
        } else {
            try {
                clscarros.setValorMercado(clsValidacoes.formataMoeda(jTxtValorMercado.getText()));
                jTxtValorMercado.setText(FormatterMoeda.format(clscarros.getValorMercado()));
            } catch (ParseException ex) {

            }
        }
    }//GEN-LAST:event_jTxtValorMercadoFocusLost

    private void jTxtValorMercadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTxtValorMercadoMouseClicked
        jTxtValorMercado.setText("");
    }//GEN-LAST:event_jTxtValorMercadoMouseClicked

    private void jTxtValorSeguroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTxtValorSeguroMouseClicked
        jTxtValorSeguro.setText("");
    }//GEN-LAST:event_jTxtValorSeguroMouseClicked

    private void jTxtValorKmRodadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTxtValorKmRodadoMouseClicked
        jTxtValorKmRodado.setText("");
    }//GEN-LAST:event_jTxtValorKmRodadoMouseClicked

    private void jTxtValorDiariaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTxtValorDiariaMouseClicked
        jTxtValorDiaria.setText("");
    }//GEN-LAST:event_jTxtValorDiariaMouseClicked

    private void jBtnBuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBtnBuscarMouseClicked
        if (jTxtPlacaBusca.getText().equals("") || jTxtPlacaBusca.getText().length() < 7) {
            JOptionPane.showMessageDialog(this, "Favor digitar ou conferir a placa digitada", "ADVERTÊNCIA", JOptionPane.INFORMATION_MESSAGE);
        } else {
            jTxtPlacaBusca.setText(jTxtPlacaBusca.getText().toUpperCase());
            jTblVeiculos.clearSelection();
            //jTblVeiculos.removeRowSelectionInterval(indiceSelJtble, indiceSelJtble);

            for (int i = 0; i < jTblVeiculos.getRowCount(); i++) {

                if (jTblVeiculos.getValueAt(i, 2).toString().equals(jTxtPlacaBusca.getText().toUpperCase())) {
                    jTblVeiculos.addRowSelectionInterval(i, i);
                    break;
                }
            }
        }
    }//GEN-LAST:event_jBtnBuscarMouseClicked

    private void JbtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JbtnEditarActionPerformed
        clscarros = listaCarros.get(linhaIndice);
        precionado = true;
        editando = true;
        setIconBtnNv(true);
        enableControl();
    }//GEN-LAST:event_JbtnEditarActionPerformed

    private void jTblVeiculosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblVeiculosMouseClicked
        precionado = false;
        setIconBtnNv(false);
        linhaIndice = jTblVeiculos.getSelectedRow();
        //clscarros = carrosDAO.select(jTblVeiculos.getValueAt(linhaIndice, 2).toString());
        //System.out.println(jTblVeiculos.getValueAt(linha, 0).toString() + "Linha" + linha);
        carregarFrameLista(linhaIndice);
        clscarros = listaCarros.get(linhaIndice);
        JbtnEditar.setEnabled(true);
        JbtnExcluir.setEnabled(true);
        JbtnNovo.setEnabled(true);
        
    }//GEN-LAST:event_jTblVeiculosMouseClicked

    private void jTxtNumeroRenavanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtNumeroRenavanFocusLost
               if ("".equals(Integer.parseInt(jTxtNumeroRenavan.getText()))) {
        } else {
            clscarros.setRenavam(Integer.parseInt(jTxtNumeroRenavan.getText()));
        }
    }//GEN-LAST:event_jTxtNumeroRenavanFocusLost

    private void jTxtObservacoesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtObservacoesFocusLost
        if (jTxtObservacoes.getText().equals("")) {
            clscarros.setObsEstado(" ");
        }else{
        clscarros.setObsEstado(jTxtObservacoes.getText());
        }
    }//GEN-LAST:event_jTxtObservacoesFocusLost

    private void jCkbInativarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCkbInativarItemStateChanged
        if (jCkbInativar.isSelected() == true) {
            clscarros.setInativo(1);
        } else if (jCkbInativar.isSelected() == false) {
            clscarros.setInativo(0);
        }
    }//GEN-LAST:event_jCkbInativarItemStateChanged

    private void JbtnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JbtnExcluirActionPerformed
        int deletar = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir?", "Atenção", JOptionPane.YES_NO_OPTION);
        if(deletar == 0){
            carrosDAO.delete(clscarros.getId());
            if(carrosDAO.isSucesso() == true){
                clearTxt();
                disableControl();
                removeLinhaJtable(jTblVeiculos.getSelectedRow());
                setIconBtnNv(false);
                precionado = false;
            }else{
                JOptionPane.showMessageDialog(this, carrosDAO.getRetorno() + "O usuario "+userLoged+""
                                              + " não tem permissao para deletar!", "Erro", JOptionPane.ERROR_MESSAGE);
                setIconBtnNv(true);
                precionado = true;
            }
            
        }
    }//GEN-LAST:event_JbtnExcluirActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JbtnEditar;
    private javax.swing.JButton JbtnExcluir;
    private javax.swing.JButton JbtnNovo;
    private javax.swing.JButton JbtnSalvar;
    private javax.swing.JFormattedTextField JfTxtData;
    private javax.swing.JButton jBtnBuscar;
    private javax.swing.JComboBox<String> jCboClasse;
    private javax.swing.JComboBox<String> jCboTipo;
    private javax.swing.JCheckBox jCkbInativar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelCodigo;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JPanel jPaneDadosGerais;
    private javax.swing.JPanel jPanelBusca;
    private javax.swing.JPanel jPanelDadosValores;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTblVeiculos;
    private javax.swing.JTextField jTxtAnoFabricacao;
    private javax.swing.JTextField jTxtAnoModelo;
    private javax.swing.JTextField jTxtChassi;
    private javax.swing.JTextField jTxtCor;
    private javax.swing.JTextField jTxtKm;
    private javax.swing.JTextField jTxtMarca;
    private javax.swing.JTextField jTxtNome;
    private javax.swing.JTextField jTxtNumeroRenavan;
    private javax.swing.JTextField jTxtObservacoes;
    private javax.swing.JTextField jTxtPlaca;
    private javax.swing.JTextField jTxtPlacaBusca;
    private javax.swing.JTextField jTxtValorDiaria;
    private javax.swing.JTextField jTxtValorKmRodado;
    private javax.swing.JFormattedTextField jTxtValorMercado;
    private javax.swing.JTextField jTxtValorSeguro;
    private javax.swing.JTextField jTxtVeiculo;
    // End of variables declaration//GEN-END:variables

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/imagens/icone_veiculo.png")));
    }
}