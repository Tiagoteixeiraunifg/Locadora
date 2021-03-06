
package views;


import controls.CarrosDAO;
import controls.ClientesDAO;
import controls.ConexaoDAO;
import controls.ContratosDAO;
import controls.EnderecosDAO;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultFormatterFactory;
import models.ClsCarros;
import models.ClsCidades;
import models.ClsClientes;
import models.ClsContratos;
import models.ClsControlaCpNumeric;
import models.ClsEnderecos;
import models.ClsImpressao;
import models.ClsLogin;
import models.ClsMascaraCampos;
import models.ClsValidacoes;

/**
 *  
 * @author Tiago Teixeira
 */
public class JfrmContratos extends javax.swing.JFrame {

    private String userLoged;
    private int userIdLoged;
    private String CpfUserLoged;
    private String userNivel;
    private String userNv;
    
    
    
    //INDICADOR DE TIPO DE CONTRATO
    private int tipoContrato; // 0 KM, 1 DIARIA
    //GLOBAIS INDICES DAS LISTAS
    private int indiceCliente;
    private int indiceCarro;
    private boolean precionado;
    private boolean editando;
    SimpleDateFormat formatoBr = new SimpleDateFormat("dd-MM-yyyy");
    Locale locale;
    NumberFormat FormatterMoeda;
    ClsValidacoes clsValidacoes;
    
    //GLOBAIS OBJETOS DE CONEXAO
    ConexaoDAO conexaoDAO;
    CarrosDAO carrosDAO;
    ClientesDAO clientesDAO;
    EnderecosDAO enderecosDAO;
    ContratosDAO contratosDAO;
    
    
    //GLOBAIS OBJETOS LISTA 
    List<ClsCarros> listaCarros;
    List<ClsCarros> listaCarrosFull;
    List<ClsClientes> listaClientes;
    
    //GLOBAIS OBJETOS USO COMUM
    ClsEnderecos clsEnderecos;
    ClsCidades clsCidades;
    ClsContratos clsContratos;
    ClsCarros clsCarros;
    ClsClientes clsClientes;
    ClsMascaraCampos clsMascaracampos;
    
    
  
    
    public JfrmContratos() {
        initComponents();
        setIcon();
    }
    
    public JfrmContratos(ClsLogin clslogin) {
        initComponents();
        setIcon();

        clsEnderecos = new ClsEnderecos();
        clsCidades = new ClsCidades();
        clsContratos = new ClsContratos();
        clsCarros = new ClsCarros();
        clsClientes = new ClsClientes();
        clsMascaracampos = new ClsMascaraCampos();
        locale = new Locale("pt", "BR");
        FormatterMoeda = NumberFormat.getCurrencyInstance(locale);
        clsValidacoes = new ClsValidacoes();

        carrosDAO = new CarrosDAO();
        clientesDAO = new ClientesDAO();
        enderecosDAO = new EnderecosDAO();
        contratosDAO = new ContratosDAO();


        userLoged = clslogin.getUserLoged();
        userIdLoged = clslogin.getId();
        CpfUserLoged = clslogin.getCpfUserLoged();
        userNivel = clslogin.getNivel();
        userNv = clslogin.getNivel();
        
        listaCarros = carrosDAO.selectAllStatus();
        listaCarrosFull = carrosDAO.selectAll();
        listaClientes = clientesDAO.selectAll();
        
        
        precionado = false;
        editando = false;
        
        controleDigitacao();
        loadCombCarro();
        loadCombTipoStatus();
        loadCombCliente();
        disableControl();
        try {
            addMascara();
        } catch (ParseException ex) {
            System.out.println("Erro aqui: "+ ex);
        }
                
    }

    public void JfrmContratosLiberacao(String userNivel) {
       this.userNv = userNivel;
    }
    
    private boolean nivelUserLoged() {
        boolean verificacao = false;
        if (userNivel.equals("GERENTE")) {
            verificacao = true;
        } else if (userNivel.equals("OPERADOR")) {
            verificacao = false;
        }
        return verificacao;
    }
    /**
     * Responsavel por trocar a fun????o e icone do bot??o "jBtnNovo"
     * Passando false o bot??o assume a posi????o de novo
     * Passando true o bot??o assume a posi????o de cancelar 
     * @param funcao 
     */        
    private void setIconBtnNv(boolean funcao) {
        if (funcao == true) {
            jBtnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icone_cancelar.png"))); // NOI18N
            jBtnNovo.setToolTipText("Clique aqui para cancelar a operacao");
        } else {
            jBtnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/add_121935.png"))); // NOI18N
            jBtnNovo.setToolTipText("Clique aqui para novo Veiculo");
        }
    }
    /**
     * Respons??vel por aplicar as mascaras nos campos formatados, e aplica a limita????o da digita????o tamb??m
     * @throws ParseException 
     */
    private void addMascara() throws ParseException {
        JfTxtDataChegada.setFormatterFactory(new DefaultFormatterFactory(clsMascaracampos.mascaraData(JfTxtDataChegada)));
        JfTxtDataSaida.setFormatterFactory(new DefaultFormatterFactory(clsMascaracampos.mascaraData(JfTxtDataSaida)));
        jFtxtCep.setFormatterFactory(new DefaultFormatterFactory(clsMascaracampos.mascaraCep(jFtxtCep)));
        jFtxtFone.setFormatterFactory(new DefaultFormatterFactory(clsMascaracampos.mascaraTelefone(jFtxtFone)));
        jFtxtCelular.setFormatterFactory(new DefaultFormatterFactory(clsMascaracampos.mascaraCelular(jFtxtCelular)));
    }
    /**
     * Passa um documento para cada campo de texto limitando o input de caracteres
     */
    private void controleDigitacao() {
        //todos os Jtext que estiver aqui s?? v??o aceitar numeros e pontos
        jTxtValorDiaria.setDocument(new ClsControlaCpNumeric());
        jTxtValorKmRodado.setDocument(new ClsControlaCpNumeric());
        jTxtQtdDias.setDocument(new ClsControlaCpNumeric());
        jTxtValorKmRodado.setDocument(new ClsControlaCpNumeric());
        jTxtValorExtra.setDocument(new ClsControlaCpNumeric());

    }
    /**
     * Passando o valor true ele vai formatar um CPF
     * Passando o valor false ele vai formatar um CNPJ
     * @param tipo
     * @throws ParseException 
     */
    private void addMascaraCpfCnpj(boolean tipo) throws ParseException {
        //se for true aplica a mascara CPF // se for false aplica a mascara cnpj
        if (tipo == true) {
                jFTxtCpfCnpj.setFormatterFactory(new DefaultFormatterFactory(clsMascaracampos.mascaraCpf(jFTxtCpfCnpj))); 
        } else if (tipo == false) {
                jFTxtCpfCnpj.setFormatterFactory(new DefaultFormatterFactory(clsMascaracampos.mascaraCnpj(jFTxtCpfCnpj)));
        }
    }
    
    /**
     * Responsavel por desabilitar os controles que n??o s??o editaveis nessa tela
     * Usado ao carregar a tela
     */
    private void disableControl(){
        //desabilitando os comboBox
        jCboNome.setEnabled(false);
        jCboPlaca.setEnabled(false);
        jCboTipoContrato.setEnabled(false);
        jCboStatusContrato.setEnabled(false);
        //desabilitando os botoes
        jBtnEditar.setEnabled(false);
        jBtnExcluir.setEnabled(false);
        jBtnImprimir.setEnabled(false);
        jBtnSalvar.setEnabled(false);
        jBtnBuscar.setEnabled(true);
        //desabilitando campos de texto
        jTxtKmUtil.setEnabled(false);
        jTxtObservacoes.setEnabled(true);
        jTxtBairro.setEnabled(false);
        jTxtEmail.setEnabled(false);
        jTxtEstado.setEnabled(false);
        jTxtNumero.setEnabled(false);
        jTxtReferencia.setEnabled(false);
        jTxtRua.setEnabled(false);
        jFTxtCpfCnpj.setEnabled(false);
        jFtxtCelular.setEnabled(false);
        jFtxtCep.setEnabled(false);
        JfTxtDataChegada.setEnabled(false);
        JfTxtDataSaida.setEnabled(false);
        jFtxtFone.setEnabled(false);
        jFtxtRgIe.setEnabled(false);
        jFtxtCnh.setEnabled(false);
        jTxtClasse.setEnabled(false);
        jTxtTipo.setEnabled(false);
        jTxtCidade.setEnabled(false);
        jTxtTipoEnd.setEnabled(false);
        jTxtValorDiaria.setEnabled(false);
        jTxtValorExtra.setEnabled(false);
        jTxtValorTotal.setEnabled(false);
        jTxtValorKmFinal.setEnabled(false);
        jTxtQtdDias.setEnabled(false);
        jTxtValorKmRodado.setEnabled(false);
        
        jTxtAnoFabricacao.setEnabled(false);
        jTxtAnoModelo.setEnabled(false);
        jTxtChassi.setEnabled(false);
        jTxtCor.setEnabled(false);
        jTxtKm.setEnabled(false);
        jTxtMarca.setEnabled(false);
        jTxtNome.setEnabled(false);
        jTxtNumeroRenavan.setEnabled(false);
        jTxtVeiculo.setEnabled(false);
    
    }
    
    /**
     * Responsavel por habilitar a tela 
     * para novo cadastro ou edi????o de um exitente
     */
    private void enableControl() {
        jCboNome.setEnabled(true);
        jCboPlaca.setEnabled(true);
        jCboTipoContrato.setEnabled(true);
        jCboStatusContrato.setEnabled(true);
        //desabilitando os botoes
        jBtnEditar.setEnabled(false);
        jBtnExcluir.setEnabled(true);
        jBtnImprimir.setEnabled(true);
        jBtnSalvar.setEnabled(true);
        jBtnBuscar.setEnabled(false);
        
        JfTxtDataChegada.setEnabled(false);
        JfTxtDataSaida.setEnabled(false);
        jTxtObservacoes.setEnabled(true);
        
    }
    /**
     * Responsavel por habilitar o uso dos bot??es 
     * de controle da tela de acordo com a regra de neg??cio
     */
    private void enableControlBusca() {
        jBtnEditar.setEnabled(true);
        jBtnExcluir.setEnabled(true);
        jBtnImprimir.setEnabled(true);
        jBtnSalvar.setEnabled(false);
        jBtnBuscar.setEnabled(true);
    }
    /**
     * Respons??vel para limpar a tela quando cancela a opera????o ou deleta um contrato
     */
    private void clearTxt() {
    
        jTxtObservacoes.setText("");
        jTxtBairro.setText("");
        jTxtEmail.setText("");
        jTxtEstado.setText("");
        jTxtNumero.setText("");
        jTxtReferencia.setText("");
        jTxtRua.setText("");
        jFTxtCpfCnpj.setText("");
        jFtxtCelular.setText("");
        jFtxtCep.setText("");
        JfTxtDataChegada.setText("");
        JfTxtDataSaida.setText("");
        jFtxtFone.setText("");
        jFtxtRgIe.setText("");
        jFtxtCnh.setText("");
        jTxtClasse.setText("");
        jTxtTipo.setText("");
        jTxtKmUtil.setText("");
        jTxtValorDiaria.setText("");
        jTxtValorExtra.setText("");
        jTxtValorTotal.setText("");
        jTxtValorKmFinal.setText("");
        jTxtQtdDias.setText("");
        jTxtValorKmRodado.setText("");
        
        jTxtAnoFabricacao.setText("");
        jTxtAnoModelo.setText("");
        jTxtChassi.setText("");
        jTxtCor.setText("");
        jTxtKm.setText("");
        jTxtMarca.setText("");
        jTxtNome.setText("");
        jTxtNumeroRenavan.setText("");
        jTxtVeiculo.setText("");
    }
    
    /**
     * Passando true ele habilita os controles Jtext para Tipo de Contrato por KM
     * Passando false ele habilita os controles Jtext para o Tipo de contrato por Dia Locado
     * @param Tipo 
     */
    private void enableTipoKM(boolean tipo) {
        if (tipo) {
            
            jTxtValorExtra.setEnabled(true);
            jTxtValorTotal.setEnabled(false);
            jTxtValorKmFinal.setEnabled(true);           
            jTxtQtdDias.setEnabled(false);
            jTxtValorKmFinal.requestFocus();

        } else {

            jTxtValorExtra.setEnabled(true);
            jTxtValorTotal.setEnabled(false);
            jTxtValorKmFinal.setEnabled(false);
            jTxtQtdDias.setEnabled(true);
            jTxtQtdDias.requestFocus();

        }
    }
    /**
     * Carrega o jCboNome com os dados retornados do Banco de Dados
     *
     */
    private void loadCombCliente() {
        if (listaClientes.size() > 0) {
            for (models.ClsClientes clC : listaClientes) {
                jCboNome.addItem(clC.getNome());
            }
        }
    }
    /**
     * Carrega o jCboPlaca com os dados retornados do Banco de Dados
     * 
     */
    private void loadCombCarro() {
        if (listaCarros.size() > 0) {
        for (models.ClsCarros clCar: listaCarros) {
            jCboPlaca.addItem(clCar.getPlaca());
        }
        }
    }
    /**
     * Carregar os jCboTipoContrato e Status
     */
    private void loadCombTipoStatus() {
        jCboTipoContrato.addItem("KM-RODADO");
        jCboTipoContrato.addItem("DI??RIA");
        
        jCboStatusContrato.addItem("ABERTO");
        jCboStatusContrato.addItem("ANDAMENTO");
        jCboStatusContrato.addItem("FINALIZADO");
        
    }
    
    /**
     * Solicita a atualiza o KM do carro no ato da entrega do mesmo
     */
    private void atualizarKmCarro() {
        String km = JOptionPane.showInputDialog("Digite o KM atual do carro no \n ato da entrega para a atualiza????o do cadastro!");
        km = km.replaceAll("[^0-9]", "");
        if (km.equals("")) {
            JOptionPane.showMessageDialog(this, "Digite o KM", "ADVERTENCIA", JOptionPane.INFORMATION_MESSAGE);
            atualizarKmCarro();
        } else if (Integer.parseInt(km) < clsCarros.getKmRodados()) {
            JOptionPane.showMessageDialog(this, "Digite o KM, maior que o KM inicial", "ADVERTENCIA", JOptionPane.INFORMATION_MESSAGE);
            atualizarKmCarro();
        } else {
            jTxtValorKmFinal.setText(km);
            clsContratos.setQuantidadeKmRet(Integer.parseInt(km));
            clsContratos.calcularKmUtil(clsCarros.getKmRodados());
            jTxtKmUtil.setText(""+clsContratos.getQuantidadeKmUtil());
            clsCarros.setKmRodados(Integer.parseInt(km));
           
        }
    }
    /**
     * Utilizado para localizar o contrato e acionar as fun????es auxiliares para carregar a tela por completo
     */
    private void buscaContrato() {
        String idContrato = JOptionPane.showInputDialog("Digite o Codigo Do Contrato para procurar");
        idContrato = idContrato.replaceAll("[^0-9]", "");
        if (idContrato.equals("")) {
            JOptionPane.showMessageDialog(this, "Digite o numero do contrato", "ADVERTENCIA", JOptionPane.INFORMATION_MESSAGE);
            buscaContrato();
        } else {
            clsContratos = contratosDAO.select(Integer.parseInt(idContrato));
            if (contratosDAO.isSucesso()) {               
                loadTxtFull(clsContratos.getIdCarro(), clsContratos.getIdCliente());
                if(clsContratos.getTipoLocacao().equals("DI??RIA")) {
                    tipoContrato = 1;
                } else if (clsContratos.getTipoLocacao().equals("KM-RODADO")) {
                    tipoContrato = 0;
                }
                precionado = false;
                enableControlBusca();
            } else if (!contratosDAO.isSucesso()) {
                JOptionPane.showMessageDialog(this, "Numero de contrato n??o localizado", "ERRO", JOptionPane.ERROR_MESSAGE);
                buscaContrato();
            }

        }

    }
    /**
     * Usado para localizar o veiculo pela placa quando ?? selecionado no JcboPlaca
     * @param placaCarro 
     */
    private void buscaIndiceCarros(String placaCarro) {
        for (int i = 0; i < listaCarros.size(); i++) {
            if (listaCarros.get(i).getPlaca().equals(placaCarro)) {
                indiceCarro = i;
                clsCarros = listaCarros.get(indiceCarro);
                clsContratos.setIdCarro(clsCarros.getId());
                loadTxtCarro();
                break;
            }
        }
    }
    /**
     * Usado para buscar o carro quando utiliza a fun????o de busca
     * @param idCarro 
     */
    private void buscaIndiceCarrosID(int  idCarro) {
        for (int i = 0; i < listaCarrosFull.size(); i++) {
            if (listaCarrosFull.get(i).getId() == idCarro) {
                indiceCarro = i;
                clsCarros = listaCarrosFull.get(indiceCarro);
                clsContratos.setIdCarro(clsCarros.getId());
                loadCombCarro();
                loadTxtCarro();
                break;
            }
        }
    }
    /**
     * Usado para localizar o cliente quando ?? selecionado no jCboNome
     * @param nomeCliente 
     */
    private void buscaIndiceClientes(String nomeCliente){
        boolean encontrado = false;
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).getNome().equals(nomeCliente)) {
                indiceCliente = i;
                clsClientes = listaClientes.get(indiceCliente);
                clsContratos.setIdCliente(clsClientes.getId());
                clsContratos.setIdColaborador(userIdLoged);
                loadTxtCliente(i);
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            clsEnderecos = enderecosDAO.selectId(clsClientes.getId());
            loadTxtEndCLiente();
        }
    }
    /**
     * Usado para localizar o cliente do contrato pelo Id, usado na busca
     * @param idCliente 
     */
    private void buscaIndiceClientesID(int idCliente){
        boolean encontrado = false;
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).getId() == idCliente) {
                indiceCliente = i;
                clsClientes = listaClientes.get(indiceCliente);
                clsContratos.setIdCliente(clsClientes.getId());
                clsContratos.setIdColaborador(userIdLoged);
                loadTxtCliente(i);
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            clsEnderecos = enderecosDAO.selectId(clsClientes.getId());
            loadTxtEndCLiente();
        }
    }
    /**
     * Usado para carregar o bloco de cliente, usado em conjunto com as fun????es "buscaIndiceClientesID" e "buscaIndiceClientes"
     * @param indice 
     */
    private void loadTxtCliente(int indice) {
        
        jTxtObservacoes.setText(listaClientes.get(indice).getObservacoes());
        jTxtEmail.setText(listaClientes.get(indice).getEmail());
        if (listaClientes.get(indice).getNome() == null || listaClientes.get(indice).getNome().equals("")) {
            jCboNome.setSelectedItem(listaClientes.get(indice).getRazaoSocial());
            
        } else if (listaClientes.get(indice).getRazaoSocial() == null || listaClientes.get(indice).getRazaoSocial().equals("")) {
            jCboNome.setSelectedItem(listaClientes.get(indice).getNome());

        }
        if (listaClientes.get(indice).getCpf() == null || listaClientes.get(indice).getCpf().equals("")) {
            jFTxtCpfCnpj.setText(listaClientes.get(indice).getCnpj());

        } else if (listaClientes.get(indice).getCnpj() == null || listaClientes.get(indice).getCnpj().equals("")) {
            jFTxtCpfCnpj.setText(listaClientes.get(indice).getCpf());

        }
        jFtxtCelular.setText(listaClientes.get(indice).getCelular());
        jFtxtFone.setText(listaClientes.get(indice).getTelefone());
        if (listaClientes.get(indice).getRg().equals("") || listaClientes.get(indice).getRg() == null) {
            jFtxtRgIe.setText("" + listaClientes.get(indice).getIe());
        } else if (listaClientes.get(indice).getIe().equals("") || listaClientes.get(indice).getIe() == null) {
            jFtxtRgIe.setText("" + listaClientes.get(indice).getRg());
        }
        jFtxtCnh.setText("" + listaClientes.get(indice).getCnh());

    }
    
    /**
     * Usado para carregar o bloco de endereco do cliente logo ap??s ?? selecionado 
     */
    private void loadTxtEndCLiente() {
 
        jTxtBairro.setText(clsEnderecos.getBairro());
        jTxtNumero.setText(clsEnderecos.getNumero());
        jTxtReferencia.setText(clsEnderecos.getReferencia());
        jTxtRua.setText(clsEnderecos.getRua());
        jFtxtCep.setText(clsEnderecos.getCep());
        jTxtEstado.setText(clsEnderecos.getEstado());
        jTxtTipoEnd.setText(clsEnderecos.getTipoEndereco());
        jTxtCidade.setText(clsEnderecos.getNomeCidade());
    }
    
    /**
     * Usado para carregar o JtextField do bloco 
     * do carro, logo quando a placa ?? selecionada
     */
    private void loadTxtCarro() {
        jCboPlaca.setSelectedItem(clsCarros.getPlaca());
        jTxtNome.setText(clsCarros.getNome());
        jTxtClasse.setText(clsCarros.getClasse());
        jTxtTipo.setText(clsCarros.getTipoVeiculo());
        jTxtValorKmRodado.setText(FormatterMoeda.format(clsCarros.getValorKmRd()));
        jTxtValorDiaria.setText(FormatterMoeda.format(clsCarros.getValorDiariaLoc()));
        jTxtAnoFabricacao.setText(""+clsCarros.getAnoFabricacao());
        jTxtAnoModelo.setText(""+clsCarros.getAnoModelo());
        jTxtChassi.setText(clsCarros.getChassi());
        jTxtCor.setText(clsCarros.getCor());
        jTxtKm.setText(""+clsCarros.getKmRodados());
        jTxtMarca.setText(clsCarros.getMarca());
        jTxtNumeroRenavan.setText(clsCarros.getRenavam());
        jTxtVeiculo.setText(clsCarros.getNome());
 
    }
    
    /**
     * Usado para carregar a tela com os dados encontrados na busca
     * @param idCarro
     * @param idCliente 
     */
    private void loadTxtFull(int idCarro, int idCliente) {
        listaCarros.clear();
        listaCarros = carrosDAO.selectAll();
        buscaIndiceClientesID(idCliente);
        buscaIndiceCarrosID(idCarro);
        jLabelCodigo.setText("Codigo: "+clsContratos.getId());
        jCboStatusContrato.setSelectedItem(clsContratos.getStatus());
        jCboTipoContrato.setSelectedItem(clsContratos.getTipoLocacao());
        JfTxtDataChegada.setText(clsValidacoes.dataFormatoBR(clsContratos.getDataChegada()));
        JfTxtDataSaida.setText(clsValidacoes.dataFormatoBR(clsContratos.getDataSaida()));
        jTxtObservacoes.setText(clsContratos.getObservacoes());
        jTxtValorExtra.setText(FormatterMoeda.format(clsContratos.getValorExtra()));
        jTxtValorTotal.setText(FormatterMoeda.format(clsContratos.getValorTotal()));
        jTxtValorKmFinal.setText(""+clsContratos.getQuantidadeKmRet());
        jTxtKmUtil.setText(""+clsContratos.getQuantidadeKmUtil());
        jTxtQtdDias.setText(""+clsContratos.getQuantidadeDiarias());
    }
    
     /**
     * Usado por lan??ar um MensageBox de campo obrigat??rio
     * @param dado 
     */
    private void msgObgCampo(String dado) {
        JOptionPane.showMessageDialog(this, "Ol?? " + userLoged + " esse dado: " + dado + " "
                + "?? obrigat??rio", "Informa????o", JOptionPane.INFORMATION_MESSAGE);
    }
    
     /**
     * Usado por lan??ar um MensageBox de campo obrigat??rio Advertencia
     * @param dado 
     */
    private void msgAdvCampo(String dado) {
        JOptionPane.showMessageDialog(this, "Ol?? " + userLoged + " esse dado: " + dado + " "
                + "est?? maior ou menor do que o permitido!", "Informa????o", JOptionPane.INFORMATION_MESSAGE);
    }
    
     /**
     * Usado por lan??ar um MensageBox de campo obrigat??rio Erro
     * @param dado 
     */
    private void msgErrCampo(String dado) {
        JOptionPane.showMessageDialog(this, "Ol?? " + userLoged + " esse dado: " + dado + ""
                + " ?? invalido!", "Informa????o", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * Valida os campos, e verifica os dados inseridos se est??o de acordo com o padr??o esperado
     * @return true para VALIDO e false para INVALIDO
     */
    private boolean validaCampos(){
        if (jCboNome.getSelectedIndex() == 0) {
            msgObgCampo("Nome Cliente");
            jCboNome.requestFocus();
            return false;
        } else if (jCboPlaca.getSelectedIndex() == 0) {
            msgAdvCampo("Carro");
            jCboPlaca.requestFocus();
            return false;
        } else  if (jCboTipoContrato.getSelectedIndex() == 0) {
            msgObgCampo("Tipo Contrato");
            jCboTipoContrato.requestFocus();
            return false;
        } else if (jCboStatusContrato.getSelectedIndex() == 0) {
            msgAdvCampo("Status Contrato");
            jCboStatusContrato.requestFocus();
            return false;
        } else if (jTxtValorKmFinal.getText().length() > 7) {
            msgErrCampo("Valor Km Final");
            jTxtValorKmFinal.requestFocus();
            return false;
        } else if (jTxtQtdDias.getText().length() > 3) {
            msgErrCampo("Quantidade de Dias");
            jTxtQtdDias.requestFocus();
            return false;
        } else if (JfTxtDataSaida.getText().length() < 1) {
            msgObgCampo("Data Saida");
            JfTxtDataSaida.requestFocus();
            return false;
        } else if (JfTxtDataChegada.getText().length() < 1) {
            msgErrCampo("Data Chegada");
            JfTxtDataChegada.requestFocus();
            return false;
        } else {
             return true;
        }
           
       
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jBtnNovo = new javax.swing.JButton();
        jBtnEditar = new javax.swing.JButton();
        jBtnSalvar = new javax.swing.JButton();
        jBtnImprimir = new javax.swing.JButton();
        jBtnBuscar = new javax.swing.JButton();
        jBtnExcluir = new javax.swing.JButton();
        jPanDadosGerais = new javax.swing.JPanel();
        jFtxtFone = new javax.swing.JFormattedTextField();
        jFtxtCelular = new javax.swing.JFormattedTextField();
        jTxtEmail = new javax.swing.JTextField();
        jCboNome = new javax.swing.JComboBox<>();
        jFTxtCpfCnpj = new javax.swing.JFormattedTextField();
        jFtxtRgIe = new javax.swing.JFormattedTextField();
        jFtxtCnh = new javax.swing.JFormattedTextField();
        jPanelDadosEnderecos = new javax.swing.JPanel();
        jTxtRua = new javax.swing.JTextField();
        jTxtNumero = new javax.swing.JTextField();
        jTxtBairro = new javax.swing.JTextField();
        jTxtEstado = new javax.swing.JTextField();
        jTxtReferencia = new javax.swing.JTextField();
        jTxtCidade = new javax.swing.JTextField();
        jTxtTipoEnd = new javax.swing.JTextField();
        jFtxtCep = new javax.swing.JFormattedTextField();
        jPaneDadosVeiculos = new javax.swing.JPanel();
        jTxtNome = new javax.swing.JTextField();
        jTxtMarca = new javax.swing.JTextField();
        jTxtCor = new javax.swing.JTextField();
        jTxtChassi = new javax.swing.JTextField();
        jTxtKm = new javax.swing.JTextField();
        jTxtAnoModelo = new javax.swing.JTextField();
        jTxtAnoFabricacao = new javax.swing.JTextField();
        jTxtNumeroRenavan = new javax.swing.JTextField();
        jTxtVeiculo = new javax.swing.JTextField();
        jCboPlaca = new javax.swing.JComboBox<>();
        jTxtTipo = new javax.swing.JTextField();
        jTxtClasse = new javax.swing.JTextField();
        jPanelDadosValores = new javax.swing.JPanel();
        jTxtValorKmRodado = new javax.swing.JTextField();
        jTxtValorTotal = new javax.swing.JTextField();
        jCboTipoContrato = new javax.swing.JComboBox<>();
        jCboStatusContrato = new javax.swing.JComboBox<>();
        jTxtValorKmFinal = new javax.swing.JTextField();
        jTxtQtdDias = new javax.swing.JTextField();
        jTxtObservacoes = new javax.swing.JTextField();
        jTxtValorDiaria = new javax.swing.JTextField();
        jTxtValorExtra = new javax.swing.JTextField();
        JfTxtDataChegada = new javax.swing.JFormattedTextField();
        JfTxtDataSaida = new javax.swing.JFormattedTextField();
        jTxtKmUtil = new javax.swing.JTextField();
        jLabelCodigo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(1055, 640));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jBtnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/add_121935.png"))); // NOI18N
        jBtnNovo.setToolTipText("Clique aqui para novo Contrato");
        jBtnNovo.setBorder(null);
        jBtnNovo.setFocusPainted(false);
        jBtnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNovoActionPerformed(evt);
            }
        });

        jBtnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/new_121792.png"))); // NOI18N
        jBtnEditar.setToolTipText("Clique aqui para editar Contrato");
        jBtnEditar.setBorder(null);
        jBtnEditar.setFocusPainted(false);
        jBtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEditarActionPerformed(evt);
            }
        });

        jBtnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/save_121760.png"))); // NOI18N
        jBtnSalvar.setToolTipText("Clique aqui para salvar Contrato");
        jBtnSalvar.setBorder(null);
        jBtnSalvar.setFocusPainted(false);
        jBtnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnSalvarActionPerformed(evt);
            }
        });

        jBtnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/print_121773.png"))); // NOI18N
        jBtnImprimir.setToolTipText("Clique aqui para imprimir Contrato");
        jBtnImprimir.setBorder(null);
        jBtnImprimir.setFocusPainted(false);
        jBtnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnImprimirActionPerformed(evt);
            }
        });

        jBtnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/search_121759.png"))); // NOI18N
        jBtnBuscar.setToolTipText("Clique aqui para buscar Contrato");
        jBtnBuscar.setBorder(null);
        jBtnBuscar.setFocusPainted(false);
        jBtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBuscarActionPerformed(evt);
            }
        });

        jBtnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/bin_121907.png"))); // NOI18N
        jBtnExcluir.setToolTipText("Clique aqui para excluir Contrato");
        jBtnExcluir.setBorder(null);
        jBtnExcluir.setFocusPainted(false);
        jBtnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnExcluirActionPerformed(evt);
            }
        });

        jPanDadosGerais.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Dados Gerais"), "Dados Gerais"));

        jFtxtFone.setBackground(new java.awt.Color(240, 240, 240));
        jFtxtFone.setBorder(javax.swing.BorderFactory.createTitledBorder("Fone"));
        jFtxtFone.setToolTipText("Exibi????o do numero de telefone fixo do cliente");
        jFtxtFone.setDisabledTextColor(new java.awt.Color(90, 90, 90));
        jFtxtFone.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jFtxtCelular.setBackground(new java.awt.Color(240, 240, 240));
        jFtxtCelular.setBorder(javax.swing.BorderFactory.createTitledBorder("Celular"));
        jFtxtCelular.setToolTipText("Exibi????o do numero de telefone celular do cliente");
        jFtxtCelular.setDisabledTextColor(new java.awt.Color(90, 90, 90));
        jFtxtCelular.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTxtEmail.setBackground(new java.awt.Color(240, 240, 240));
        jTxtEmail.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtEmail.setToolTipText("Exibi????o do Email do Cliente do Cliente");
        jTxtEmail.setBorder(javax.swing.BorderFactory.createTitledBorder("E-mail"));
        jTxtEmail.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jCboNome.setBackground(new java.awt.Color(240, 240, 240));
        jCboNome.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCboNome.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        jCboNome.setToolTipText("Selecione o cliente para iniciar o contrato");
        jCboNome.setBorder(javax.swing.BorderFactory.createTitledBorder("Nome"));
        jCboNome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jCboNomeFocusLost(evt);
            }
        });
        jCboNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCboNomeKeyPressed(evt);
            }
        });

        jFTxtCpfCnpj.setBackground(new java.awt.Color(240, 240, 240));
        jFTxtCpfCnpj.setBorder(javax.swing.BorderFactory.createTitledBorder("CPF/CNPJ"));
        jFTxtCpfCnpj.setToolTipText("Escolha entre CPF ou CNPJ e insira o dado!");
        jFTxtCpfCnpj.setDisabledTextColor(new java.awt.Color(90, 90, 90));
        jFTxtCpfCnpj.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jFtxtRgIe.setBackground(new java.awt.Color(240, 240, 240));
        jFtxtRgIe.setBorder(javax.swing.BorderFactory.createTitledBorder("RG/IE"));
        jFtxtRgIe.setToolTipText("Insira o RG caso seja pessoa fisica ou IE caso seja pessoa juridica");
        jFtxtRgIe.setDisabledTextColor(new java.awt.Color(90, 90, 90));
        jFtxtRgIe.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jFtxtCnh.setBackground(new java.awt.Color(240, 240, 240));
        jFtxtCnh.setBorder(javax.swing.BorderFactory.createTitledBorder("CNH"));
        jFtxtCnh.setToolTipText("Numero da CNH do cliente");
        jFtxtCnh.setDisabledTextColor(new java.awt.Color(90, 90, 90));
        jFtxtCnh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanDadosGeraisLayout = new javax.swing.GroupLayout(jPanDadosGerais);
        jPanDadosGerais.setLayout(jPanDadosGeraisLayout);
        jPanDadosGeraisLayout.setHorizontalGroup(
            jPanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanDadosGeraisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCboNome, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanDadosGeraisLayout.createSequentialGroup()
                        .addComponent(jFtxtFone, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jFtxtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanDadosGeraisLayout.createSequentialGroup()
                        .addComponent(jFTxtCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jFtxtRgIe, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jFtxtCnh, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTxtEmail))
                .addContainerGap())
        );
        jPanDadosGeraisLayout.setVerticalGroup(
            jPanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanDadosGeraisLayout.createSequentialGroup()
                .addGroup(jPanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jFTxtCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jFtxtRgIe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jFtxtCnh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCboNome, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFtxtFone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFtxtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanelDadosEnderecos.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados Endere??os"));

        jTxtRua.setBackground(new java.awt.Color(240, 240, 240));
        jTxtRua.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtRua.setToolTipText("Exibi????o do nome da rua");
        jTxtRua.setBorder(javax.swing.BorderFactory.createTitledBorder("Rua"));
        jTxtRua.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtNumero.setBackground(new java.awt.Color(240, 240, 240));
        jTxtNumero.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtNumero.setToolTipText("Exibi????o do Numero");
        jTxtNumero.setBorder(javax.swing.BorderFactory.createTitledBorder("Numero"));
        jTxtNumero.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtBairro.setBackground(new java.awt.Color(240, 240, 240));
        jTxtBairro.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtBairro.setToolTipText("Exibi????o do nome do bairro");
        jTxtBairro.setBorder(javax.swing.BorderFactory.createTitledBorder("Bairro"));
        jTxtBairro.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtEstado.setBackground(new java.awt.Color(240, 240, 240));
        jTxtEstado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtEstado.setToolTipText("Exibi????o do Estado");
        jTxtEstado.setBorder(javax.swing.BorderFactory.createTitledBorder("Estado"));
        jTxtEstado.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtReferencia.setBackground(new java.awt.Color(240, 240, 240));
        jTxtReferencia.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtReferencia.setToolTipText("Exibi????o da referencia");
        jTxtReferencia.setBorder(javax.swing.BorderFactory.createTitledBorder("Referencia"));
        jTxtReferencia.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtCidade.setBackground(new java.awt.Color(240, 240, 240));
        jTxtCidade.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtCidade.setToolTipText("Exibi????o da cidade ");
        jTxtCidade.setBorder(javax.swing.BorderFactory.createTitledBorder("Cidade"));
        jTxtCidade.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtTipoEnd.setBackground(new java.awt.Color(240, 240, 240));
        jTxtTipoEnd.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtTipoEnd.setToolTipText("Exibi????o do tipo de endere??o");
        jTxtTipoEnd.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de Endere??o"));
        jTxtTipoEnd.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jFtxtCep.setBackground(new java.awt.Color(240, 240, 240));
        jFtxtCep.setBorder(javax.swing.BorderFactory.createTitledBorder("CEP"));
        jFtxtCep.setToolTipText("Nuero do CEP do endere??o do cliente");
        jFtxtCep.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        javax.swing.GroupLayout jPanelDadosEnderecosLayout = new javax.swing.GroupLayout(jPanelDadosEnderecos);
        jPanelDadosEnderecos.setLayout(jPanelDadosEnderecosLayout);
        jPanelDadosEnderecosLayout.setHorizontalGroup(
            jPanelDadosEnderecosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDadosEnderecosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDadosEnderecosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDadosEnderecosLayout.createSequentialGroup()
                        .addComponent(jTxtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(jTxtTipoEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTxtReferencia))
                    .addGroup(jPanelDadosEnderecosLayout.createSequentialGroup()
                        .addComponent(jTxtRua, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtCidade)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jFtxtCep, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelDadosEnderecosLayout.setVerticalGroup(
            jPanelDadosEnderecosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDadosEnderecosLayout.createSequentialGroup()
                .addGroup(jPanelDadosEnderecosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTxtRua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFtxtCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDadosEnderecosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTxtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtTipoEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPaneDadosVeiculos.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados Veiculos"));
        jPaneDadosVeiculos.setFocusable(false);

        jTxtNome.setBackground(new java.awt.Color(240, 240, 240));
        jTxtNome.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtNome.setToolTipText("Exibi????o do nome do veiculo");
        jTxtNome.setBorder(javax.swing.BorderFactory.createTitledBorder("Nome"));
        jTxtNome.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtMarca.setBackground(new java.awt.Color(240, 240, 240));
        jTxtMarca.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtMarca.setToolTipText("Exibi????o da marca do veiculo");
        jTxtMarca.setBorder(javax.swing.BorderFactory.createTitledBorder("Marca"));
        jTxtMarca.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtCor.setBackground(new java.awt.Color(240, 240, 240));
        jTxtCor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtCor.setToolTipText("Exibi????o da cor do veiculo");
        jTxtCor.setBorder(javax.swing.BorderFactory.createTitledBorder("Cor"));
        jTxtCor.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtChassi.setBackground(new java.awt.Color(240, 240, 240));
        jTxtChassi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtChassi.setToolTipText("Exibi????o do numero do chassi do veiculo");
        jTxtChassi.setBorder(javax.swing.BorderFactory.createTitledBorder("Chassi"));
        jTxtChassi.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtKm.setBackground(new java.awt.Color(240, 240, 240));
        jTxtKm.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtKm.setToolTipText("Exibi????o do KM rodada do veiculo");
        jTxtKm.setBorder(javax.swing.BorderFactory.createTitledBorder("Km Rodado Atual"));
        jTxtKm.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtAnoModelo.setBackground(new java.awt.Color(240, 240, 240));
        jTxtAnoModelo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtAnoModelo.setToolTipText("Exibi????o do ano modelo do veiculo");
        jTxtAnoModelo.setBorder(javax.swing.BorderFactory.createTitledBorder("Ano Modelo"));
        jTxtAnoModelo.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtAnoFabricacao.setBackground(new java.awt.Color(240, 240, 240));
        jTxtAnoFabricacao.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtAnoFabricacao.setToolTipText("Exibi????o do ano fabrica????o do veiculo");
        jTxtAnoFabricacao.setBorder(javax.swing.BorderFactory.createTitledBorder("Ano Fabricacao"));
        jTxtAnoFabricacao.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtNumeroRenavan.setBackground(new java.awt.Color(240, 240, 240));
        jTxtNumeroRenavan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtNumeroRenavan.setToolTipText("Exibi????o do numero RENAVAN do veiculo");
        jTxtNumeroRenavan.setBorder(javax.swing.BorderFactory.createTitledBorder("RENAVAN"));
        jTxtNumeroRenavan.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtVeiculo.setBackground(new java.awt.Color(240, 240, 240));
        jTxtVeiculo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtVeiculo.setToolTipText("Exibi????o do modelo do veiculo");
        jTxtVeiculo.setBorder(javax.swing.BorderFactory.createTitledBorder("Modelo"));
        jTxtVeiculo.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jCboPlaca.setBackground(new java.awt.Color(240, 240, 240));
        jCboPlaca.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCboPlaca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        jCboPlaca.setToolTipText("Selecione a placa do veiculo a ser alugado");
        jCboPlaca.setBorder(javax.swing.BorderFactory.createTitledBorder("Placa"));
        jCboPlaca.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jCboPlacaFocusLost(evt);
            }
        });
        jCboPlaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCboPlacaKeyPressed(evt);
            }
        });

        jTxtTipo.setBackground(new java.awt.Color(240, 240, 240));
        jTxtTipo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtTipo.setToolTipText("Exibi????o do tipo do veiculo");
        jTxtTipo.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo"));
        jTxtTipo.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtClasse.setBackground(new java.awt.Color(240, 240, 240));
        jTxtClasse.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtClasse.setToolTipText("Exibi????o da classe do veiculo");
        jTxtClasse.setBorder(javax.swing.BorderFactory.createTitledBorder("Classe"));
        jTxtClasse.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        javax.swing.GroupLayout jPaneDadosVeiculosLayout = new javax.swing.GroupLayout(jPaneDadosVeiculos);
        jPaneDadosVeiculos.setLayout(jPaneDadosVeiculosLayout);
        jPaneDadosVeiculosLayout.setHorizontalGroup(
            jPaneDadosVeiculosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPaneDadosVeiculosLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPaneDadosVeiculosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPaneDadosVeiculosLayout.createSequentialGroup()
                        .addComponent(jTxtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtVeiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtKm, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtAnoModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtAnoFabricacao, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTxtNumeroRenavan, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPaneDadosVeiculosLayout.createSequentialGroup()
                        .addComponent(jCboPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTxtChassi, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(jTxtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTxtCor, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtTipo)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtClasse, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPaneDadosVeiculosLayout.setVerticalGroup(
            jPaneDadosVeiculosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPaneDadosVeiculosLayout.createSequentialGroup()
                .addGroup(jPaneDadosVeiculosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTxtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtCor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtChassi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCboPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtClasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPaneDadosVeiculosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTxtKm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtAnoModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtAnoFabricacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtVeiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtNumeroRenavan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanelDadosValores.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados Valores e Finaliza????o Contrato"));
        jPanelDadosValores.setToolTipText("Selecionar informa????es do tipo de contrato, valores, diarias, status do contrato!");

        jTxtValorKmRodado.setBackground(new java.awt.Color(240, 240, 240));
        jTxtValorKmRodado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtValorKmRodado.setToolTipText("Exibi????o do valor de cada KM rodado desse veiculo");
        jTxtValorKmRodado.setBorder(javax.swing.BorderFactory.createTitledBorder("Valor Km Rodado"));
        jTxtValorKmRodado.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtValorTotal.setBackground(new java.awt.Color(240, 240, 240));
        jTxtValorTotal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtValorTotal.setToolTipText("Exibi????o do valor total calculado para esse contrato");
        jTxtValorTotal.setBorder(javax.swing.BorderFactory.createTitledBorder("Valor Total"));
        jTxtValorTotal.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jCboTipoContrato.setBackground(new java.awt.Color(240, 240, 240));
        jCboTipoContrato.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCboTipoContrato.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        jCboTipoContrato.setToolTipText("Selecione o tipo de contrato, escolha entre diaria ou km rodado precione Tab para selecionar");
        jCboTipoContrato.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo Contrato"));
        jCboTipoContrato.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCboTipoContratoKeyPressed(evt);
            }
        });

        jCboStatusContrato.setBackground(new java.awt.Color(240, 240, 240));
        jCboStatusContrato.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCboStatusContrato.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        jCboStatusContrato.setToolTipText("Selecione o status do contrato do cliente e precione enter para selecionar");
        jCboStatusContrato.setBorder(javax.swing.BorderFactory.createTitledBorder("Status Contrato"));
        jCboStatusContrato.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCboStatusContratoKeyPressed(evt);
            }
        });

        jTxtValorKmFinal.setBackground(new java.awt.Color(240, 240, 240));
        jTxtValorKmFinal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtValorKmFinal.setToolTipText("Digite o KM rodado no ato da entrega do veiculo");
        jTxtValorKmFinal.setBorder(javax.swing.BorderFactory.createTitledBorder("KM Rod Final"));
        jTxtValorKmFinal.setDisabledTextColor(new java.awt.Color(90, 90, 90));
        jTxtValorKmFinal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTxtValorKmFinalMouseClicked(evt);
            }
        });
        jTxtValorKmFinal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTxtValorKmFinalKeyPressed(evt);
            }
        });

        jTxtQtdDias.setBackground(new java.awt.Color(240, 240, 240));
        jTxtQtdDias.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtQtdDias.setToolTipText("Digite a quantidade de dias que ser?? alugado o veiculo");
        jTxtQtdDias.setBorder(javax.swing.BorderFactory.createTitledBorder("Dias Alugados"));
        jTxtQtdDias.setDisabledTextColor(new java.awt.Color(90, 90, 90));
        jTxtQtdDias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTxtQtdDiasMouseClicked(evt);
            }
        });
        jTxtQtdDias.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTxtQtdDiasKeyPressed(evt);
            }
        });

        jTxtObservacoes.setBackground(new java.awt.Color(240, 240, 240));
        jTxtObservacoes.setToolTipText("Breve observa????o caso houver sobre o veiculo");
        jTxtObservacoes.setBorder(javax.swing.BorderFactory.createTitledBorder("Observa????es"));
        jTxtObservacoes.setDisabledTextColor(new java.awt.Color(90, 90, 90));
        jTxtObservacoes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtObservacoesFocusLost(evt);
            }
        });
        jTxtObservacoes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTxtObservacoesKeyPressed(evt);
            }
        });

        jTxtValorDiaria.setBackground(new java.awt.Color(240, 240, 240));
        jTxtValorDiaria.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtValorDiaria.setToolTipText("Exibi????o do valor da di??ria desse veiculo");
        jTxtValorDiaria.setBorder(javax.swing.BorderFactory.createTitledBorder("Valor Diaria"));
        jTxtValorDiaria.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        jTxtValorExtra.setBackground(new java.awt.Color(240, 240, 240));
        jTxtValorExtra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtValorExtra.setToolTipText("Campo para digita????o de despesas extras, como multas, avarias e etc.");
        jTxtValorExtra.setBorder(javax.swing.BorderFactory.createTitledBorder("Valores Extras"));
        jTxtValorExtra.setDisabledTextColor(new java.awt.Color(90, 90, 90));
        jTxtValorExtra.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTxtValorExtraFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtValorExtraFocusLost(evt);
            }
        });
        jTxtValorExtra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTxtValorExtraMouseClicked(evt);
            }
        });

        JfTxtDataChegada.setBackground(new java.awt.Color(240, 240, 240));
        JfTxtDataChegada.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Chegada"));
        JfTxtDataChegada.setToolTipText("Inserir data de chegada do Veiculo!");
        JfTxtDataChegada.setDisabledTextColor(new java.awt.Color(90, 90, 90));
        JfTxtDataChegada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        JfTxtDataChegada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JfTxtDataChegadaKeyPressed(evt);
            }
        });

        JfTxtDataSaida.setBackground(new java.awt.Color(240, 240, 240));
        JfTxtDataSaida.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Saida"));
        JfTxtDataSaida.setToolTipText("Inserir data de saida do Veiculo!");
        JfTxtDataSaida.setDisabledTextColor(new java.awt.Color(90, 90, 90));
        JfTxtDataSaida.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        JfTxtDataSaida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JfTxtDataSaidaKeyPressed(evt);
            }
        });

        jTxtKmUtil.setBackground(new java.awt.Color(240, 240, 240));
        jTxtKmUtil.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtKmUtil.setBorder(javax.swing.BorderFactory.createTitledBorder("KM Utilizada"));
        jTxtKmUtil.setDisabledTextColor(new java.awt.Color(90, 90, 90));

        javax.swing.GroupLayout jPanelDadosValoresLayout = new javax.swing.GroupLayout(jPanelDadosValores);
        jPanelDadosValores.setLayout(jPanelDadosValoresLayout);
        jPanelDadosValoresLayout.setHorizontalGroup(
            jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDadosValoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCboTipoContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCboStatusContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDadosValoresLayout.createSequentialGroup()
                        .addComponent(jTxtValorKmRodado, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTxtValorDiaria, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(jTxtValorKmFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTxtKmUtil, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTxtQtdDias, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelDadosValoresLayout.createSequentialGroup()
                        .addGap(0, 4, Short.MAX_VALUE)
                        .addComponent(jTxtObservacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JfTxtDataSaida, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .addComponent(jTxtValorExtra))
                .addGap(18, 18, 18)
                .addGroup(jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(JfTxtDataChegada, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                    .addComponent(jTxtValorTotal))
                .addGap(15, 15, 15))
        );
        jPanelDadosValoresLayout.setVerticalGroup(
            jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDadosValoresLayout.createSequentialGroup()
                .addGroup(jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTxtValorKmRodado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCboTipoContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JfTxtDataChegada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JfTxtDataSaida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTxtQtdDias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTxtKmUtil)
                        .addComponent(jTxtValorKmFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTxtValorDiaria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jCboStatusContrato)
                    .addGroup(jPanelDadosValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTxtObservacoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTxtValorExtra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTxtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 18, Short.MAX_VALUE))
        );

        jLabelCodigo.setText("Codigo:");
        jLabelCodigo.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jBtnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jBtnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelCodigo)
                .addGap(41, 41, 41))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanDadosGerais, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelDadosEnderecos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPaneDadosVeiculos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelDadosValores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBtnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelCodigo)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanDadosGerais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDadosEnderecos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPaneDadosVeiculos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDadosValores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        models.ClsLogin clslogin = new ClsLogin();
        clslogin.setUserLoged(userLoged);
        clslogin.setId(userIdLoged);
        clslogin.setCpfUserLoged(CpfUserLoged);
        clslogin.setNivel(userNivel);
        views.JfrmPrincipal telaprincipal = new views.JfrmPrincipal(clslogin);
        telaprincipal.setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void jBtnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnImprimirActionPerformed

          JFileChooser fileChooser = new JFileChooser();
          fileChooser.setDialogTitle("Gravar contrato");
          fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
          FileNameExtensionFilter filtro = new FileNameExtensionFilter("Arquivos Microsoft Word", "DOC", "DOCX");
          fileChooser.setFileFilter(filtro);
          fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
          int retorno = fileChooser.showSaveDialog(this);
        if (retorno == JFileChooser.APPROVE_OPTION) {
            File arquivoSalvo = fileChooser.getSelectedFile();
            String caminhoArquivo = fileChooser.getCurrentDirectory().toString();
            models.ClsImpressao clsImp = new ClsImpressao(clsEnderecos, clsClientes, clsCarros, clsContratos);
            try {
                if (tipoContrato == 0) {
                    clsImp.criarContratoKM(caminhoArquivo + "\\" + arquivoSalvo.getName());
                } else if (tipoContrato == 1) {
                    clsImp.criarContratoDiaria(caminhoArquivo + "\\" + arquivoSalvo.getName());
                }

            } catch (Exception ex) {
                System.out.println("N??o gerou o arquivo");
            }
            System.out.println(caminhoArquivo + "\\" + arquivoSalvo.getName());
//              try {
//               String arquivo = "cmd.exe /C start WINWORD.exe "+caminhoArquivo+"\\"+arquivoSalvo.getName()+".docx";
//               Runtime.getRuntime().exec(arquivo);// .getRuntime().exec("cmd.exe /C start WINWORD.exe c:\seu_arquivo.doc");
//                  System.out.println(arquivo);
//              } catch (IOException ex) {
//                  System.out.println("Erro aqui: "+ex);
//              }
        } else {
            System.out.println("Canlecou aqui.");
        }
    }//GEN-LAST:event_jBtnImprimirActionPerformed

    private void jBtnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNovoActionPerformed
        if (precionado == false) {
            listaCarros.clear();
            listaCarros = carrosDAO.selectAllStatus();
            setIconBtnNv(true);
            enableControl();
            clearTxt();
            precionado = true;
            editando = false;
            jTxtNome.requestFocus();

        } else {
            setIconBtnNv(false);
            disableControl();
            clearTxt();
            precionado = false;
            editando = false;
        }

    }//GEN-LAST:event_jBtnNovoActionPerformed

    private void jCboNomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCboNomeFocusLost

            buscaIndiceClientes(jCboNome.getSelectedItem().toString());

    }//GEN-LAST:event_jCboNomeFocusLost

    private void jCboPlacaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCboPlacaFocusLost

            buscaIndiceCarros(jCboPlaca.getSelectedItem().toString());
    
    }//GEN-LAST:event_jCboPlacaFocusLost

    private void jTxtValorExtraFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtValorExtraFocusLost
        if ("".equals(jTxtValorExtra.getText())) {
            clsContratos.setValorExtra(0);
        } else if (jTxtValorExtra.getText().length() > 14) {
            JOptionPane.showMessageDialog(this, "O valor inserido ?? maior que o permitido", "ADVERTENCIA", JOptionPane.INFORMATION_MESSAGE);
        } else if (jTxtValorExtra.getText().length() > 1) {
            try {
                clsContratos.setValorExtra(clsValidacoes.formataMoeda(jTxtValorExtra.getText()));
                jTxtValorTotal.setText(FormatterMoeda.format(clsContratos.calcularValorExtra()));
                jTxtValorExtra.setText(FormatterMoeda.format(clsContratos.getValorExtra()));

            } catch (ParseException ex) {
                System.out.println("" + ex);
            }
        }

    }//GEN-LAST:event_jTxtValorExtraFocusLost

    private void jTxtValorExtraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTxtValorExtraMouseClicked
        jTxtValorExtra.setText("");
    }//GEN-LAST:event_jTxtValorExtraMouseClicked

    private void jTxtValorKmFinalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTxtValorKmFinalMouseClicked
        jTxtValorKmFinal.setText("");
    }//GEN-LAST:event_jTxtValorKmFinalMouseClicked

    private void jTxtQtdDiasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTxtQtdDiasMouseClicked
        jTxtQtdDias.setText("");
    }//GEN-LAST:event_jTxtQtdDiasMouseClicked

    private void jBtnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSalvarActionPerformed
        boolean validado = validaCampos();
        if (editando == false && validado == true) {
            clsContratos.setIdColaborador(userIdLoged);
            clsCarros.setStatus(1);
            contratosDAO.save(clsContratos);
            carrosDAO.update(clsCarros);
            if (contratosDAO.isSucesso() && carrosDAO.isSucesso()) {
                JOptionPane.showMessageDialog(this, contratosDAO.getRetorno(), "INFORMA????O", JOptionPane.INFORMATION_MESSAGE);
                clsContratos.setId(contratosDAO.getIdGerado());
                jLabelCodigo.setText("Codigo: "+clsContratos.getId());
                setIconBtnNv(false);
                precionado = false;
                editando = false;
                disableControl();
                jBtnEditar.setEnabled(true);
                jBtnImprimir.setEnabled(true);
                

            } else if (contratosDAO.isSucesso() == false || carrosDAO.isSucesso() == false) {
                JOptionPane.showMessageDialog(this, contratosDAO.getRetorno(), "INFORMA????O", JOptionPane.INFORMATION_MESSAGE);
                setIconBtnNv(false);
                precionado = false;
                editando = false;
                disableControl();
                clearTxt();
            }
        } else if (editando && validado) {
            if(clsContratos.getStatus().equals("FINALIZADO")){
                clsCarros.setStatus(0);
                clsCarros.setKmRodados(clsContratos.getQuantidadeKmRet());
            }else{
                clsCarros.setStatus(1);
            }
            carrosDAO.update(clsCarros);
            contratosDAO.update(clsContratos);
            if (contratosDAO.isSucesso() && carrosDAO.isSucesso()) {
                JOptionPane.showMessageDialog(this, contratosDAO.getRetorno(), "INFORMA????O", JOptionPane.INFORMATION_MESSAGE);
                userNv = userNivel;
                setIconBtnNv(false);
                precionado = false;
                editando = false;
                disableControl();
                jBtnEditar.setEnabled(true);
                jBtnImprimir.setEnabled(true);
            } else if (contratosDAO.isSucesso() == false || carrosDAO.isSucesso() == false) {
                JOptionPane.showMessageDialog(this, contratosDAO.getRetorno(), "INFORMA????O", JOptionPane.INFORMATION_MESSAGE);
                userNv = userNivel;
                setIconBtnNv(false);
                precionado = false;
                editando = false;
                disableControl();
                clearTxt();
               } 
    }//GEN-LAST:event_jBtnSalvarActionPerformed
    }
    
    private void jBtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnEditarActionPerformed
        if (nivelUserLoged() || userNv.equals("GERENTE") && clsContratos.getStatus().equals("FINALIZADO")) {
            precionado = true;
            editando = true;
            setIconBtnNv(true);
            enableControl();
        } else if (nivelUserLoged() == false && !"FINALIZADO".equals(clsContratos.getStatus())) {
            precionado = true;
            editando = true;
            setIconBtnNv(true);
            enableControl();
        } else if (!nivelUserLoged() && clsContratos.getStatus().equals("FINALIZADO")) {
            int confirma = JOptionPane.showConfirmDialog(this, "Ol?? " + userLoged + ", o contrato s?? pode ser editado por um GERENTE ap??s concluido"
                    + "\n Clique em SIM para pedir acesso ou N??O para cancelar", "INFOMA????O", JOptionPane.YES_NO_OPTION);
            if (confirma == 0) {
                JfrmLogin login = new JfrmLogin(true, this);
                login.setVisible(true);
                if (userNv.equals("GERENTE")) {
                    precionado = true;
                    editando = true;
                    setIconBtnNv(true);
                    enableControl();
                }
            } else if (!nivelUserLoged()) {
                JOptionPane.showMessageDialog(this, "Ol?? " + userLoged + ", o contrato s?? pode ser editado por um GERENTE ap??s concluido", "INFOMA????O", JOptionPane.INFORMATION_MESSAGE);
            } else if (confirma == 1) {
                //jBtnEditar.requestFocus();
            }

        }
    }//GEN-LAST:event_jBtnEditarActionPerformed

    private void jBtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBuscarActionPerformed
         buscaContrato();
    }//GEN-LAST:event_jBtnBuscarActionPerformed

    private void jBtnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExcluirActionPerformed
        if (!"ABERTO".equals(clsContratos.getStatus())) {
            JOptionPane.showMessageDialog(this, "Ol?? " + userLoged + ""
                    + " n??o pode deletar contrato \n "
                    + "com Status diferente de ABERTO!", "Erro", JOptionPane.ERROR_MESSAGE);
        } else if (clsContratos.getStatus().equals("ABERTO") && !nivelUserLoged()) {
            JOptionPane.showMessageDialog(this, "Ol?? " + userLoged + ""
                    + " somente GERENTE tem essa permiss??o!", "INFORMA????O", JOptionPane.INFORMATION_MESSAGE);
        } else if (clsContratos.getStatus().equals("ABERTO") && nivelUserLoged()) {
            int deletar = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o contrato?", "Aten????o", JOptionPane.YES_NO_OPTION);
            if (deletar == 0) {
                clsCarros.setStatus(0);
                carrosDAO.update(clsCarros);
                contratosDAO.delete(clsContratos.getId());
                if (contratosDAO.isSucesso() == true) {
                    JOptionPane.showMessageDialog(this, contratosDAO.getRetorno(), "Mensagem", JOptionPane.INFORMATION_MESSAGE);
                    clearTxt();
                    disableControl();
                    setIconBtnNv(false);
                    precionado = false;
                } else if (contratosDAO.isSucesso() == false) {
                    JOptionPane.showMessageDialog(this, contratosDAO.getRetorno() + "Ol?? " + userLoged + ""
                            + " n??o conseguiu excluir erro:\n"
                            + " " + contratosDAO.getRetorno() + "", "Erro", JOptionPane.ERROR_MESSAGE);
                    setIconBtnNv(true);
                    precionado = true;
                }

            }
        }
    }//GEN-LAST:event_jBtnExcluirActionPerformed

    private void jTxtValorExtraFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtValorExtraFocusGained
        jTxtValorExtra.setText("");
    }//GEN-LAST:event_jTxtValorExtraFocusGained

    private void jTxtValorKmFinalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtValorKmFinalKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
         clsContratos.setValorExtra(0);
            jTxtValorExtra.setText(FormatterMoeda.format(clsContratos.getValorExtra()));
            if (Integer.parseInt(jTxtValorKmFinal.getText()) < clsCarros.getKmRodados()) {
                JOptionPane.showMessageDialog(this, "O campo deve ser preenchido com um valor \n maior que o KM atual do Veiculo!", "ADVERTENCIA", JOptionPane.INFORMATION_MESSAGE);
            } else if (jTxtValorKmFinal.getText().length() < 1) {
                JOptionPane.showMessageDialog(this, "O campo deve ser preenchido", "ADVERTENCIA", JOptionPane.INFORMATION_MESSAGE);
            } else {
                clsContratos.setQuantidadeKmRet(Integer.parseInt(jTxtValorKmFinal.getText()));
                jTxtValorTotal.setText(FormatterMoeda.format(clsContratos.calcularValorTotalKM(clsCarros.getValorKmRd(), clsCarros.getKmRodados())));
                jTxtKmUtil.setText(""+clsContratos.getQuantidadeKmUtil());
            }

            JfTxtDataSaida.setEnabled(true);
            JfTxtDataChegada.setEnabled(true);
            JfTxtDataSaida.requestFocus();
        }
    }//GEN-LAST:event_jTxtValorKmFinalKeyPressed

    private void JfTxtDataSaidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JfTxtDataSaidaKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            if (JfTxtDataSaida.getText().length() < 1) {
            } else if (clsValidacoes.validaDataFormatoBR(JfTxtDataSaida.getText()) == true) {
                clsContratos.setDataSaida(clsValidacoes.dataFormatoUS(JfTxtDataSaida.getText()));
                clsContratos.setDataContrato(clsValidacoes.dataFormatoUS(JfTxtDataSaida.getText()));
                JfTxtDataChegada.setEnabled(true);
                JfTxtDataChegada.requestFocus();
            } else {
                msgErrCampo("Data Saida");
            }
        }
    }//GEN-LAST:event_JfTxtDataSaidaKeyPressed

    private void JfTxtDataChegadaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JfTxtDataChegadaKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            if (JfTxtDataChegada.getText().length() < 1) {
            } else if (clsValidacoes.validaDataFormatoBR(JfTxtDataChegada.getText()) == true) {
                clsContratos.setDataChegada(clsValidacoes.dataFormatoUS(JfTxtDataChegada.getText()));
                clsContratos.setDataContrato(clsValidacoes.dataFormatoUS(JfTxtDataSaida.getText()));
                jCboStatusContrato.requestFocus();
            } else {
                msgErrCampo("Data Saida");
            }
        }
    }//GEN-LAST:event_JfTxtDataChegadaKeyPressed

    private void jTxtQtdDiasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtQtdDiasKeyPressed
        clsContratos.setValorExtra(0);
        jTxtValorExtra.setText(FormatterMoeda.format(clsContratos.getValorExtra()));
        clsContratos.setQuantidadeDiarias(Integer.parseInt(jTxtQtdDias.getText()));
        jTxtValorTotal.setText(FormatterMoeda.format(clsContratos.calcularValorTotalDIA(clsCarros.getValorDiariaLoc())));
        JfTxtDataSaida.setEnabled(true);
        JfTxtDataChegada.setEnabled(true);
        JfTxtDataSaida.requestFocus();
    }//GEN-LAST:event_jTxtQtdDiasKeyPressed

    private void jCboNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCboNomeKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            buscaIndiceClientes(jCboNome.getSelectedItem().toString());
            jCboPlaca.requestFocus();
        }
    }//GEN-LAST:event_jCboNomeKeyPressed

    private void jCboPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCboPlacaKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            buscaIndiceCarros(jCboPlaca.getSelectedItem().toString());
            jCboTipoContrato.requestFocus();
        }
    }//GEN-LAST:event_jCboPlacaKeyPressed

    private void jCboTipoContratoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCboTipoContratoKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            if (jCboTipoContrato.getSelectedItem().equals("KM-RODADO")) {
            enableTipoKM(true);
            tipoContrato = 0;
            clsContratos.setTipoLocacao(jCboTipoContrato.getSelectedItem().toString());
            clsContratos.setQuantidadeDiarias(0);
            jTxtQtdDias.setText("");
            
        } else if (jCboTipoContrato.getSelectedItem().equals("DI??RIA")) {
            enableTipoKM(false);
            tipoContrato = 1;
            clsContratos.setTipoLocacao(jCboTipoContrato.getSelectedItem().toString());
            clsContratos.setQuantidadeKmRet(0);
            jTxtValorKmFinal.setText("");
            
        }
        }
    }//GEN-LAST:event_jCboTipoContratoKeyPressed

    private void jTxtObservacoesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtObservacoesFocusLost
            if(jTxtObservacoes.getText().length() > 999 ){
                msgErrCampo("Observa????es");
            }else {
                 clsContratos.setObservacoes(jTxtObservacoes.getText());
            }
    }//GEN-LAST:event_jTxtObservacoesFocusLost

    private void jTxtObservacoesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtObservacoesKeyPressed
        if(evt.getKeyChar() == KeyEvent.VK_ENTER){
            if(jTxtObservacoes.getText().length() > 999 ){
                msgErrCampo("Observa????es");
            }else {
                 clsContratos.setObservacoes(jTxtObservacoes.getText());
            }
        }
               
       
    }//GEN-LAST:event_jTxtObservacoesKeyPressed

    private void jCboStatusContratoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCboStatusContratoKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            clsContratos.setStatus(jCboStatusContrato.getSelectedItem().toString());
            if (jCboStatusContrato.getSelectedItem().toString().equals("FINALIZADO")) {
                clsCarros.setStatus(0);
                clsContratos.setStatus(jCboStatusContrato.getSelectedItem().toString());
                if (jCboTipoContrato.getSelectedItem() == "DI??RIA") {
                    if (jTxtQtdDias.getText().length() > 0) {
                        atualizarKmCarro();
                    }
                }
            }
            jTxtObservacoes.requestFocus();
        }
    }//GEN-LAST:event_jCboStatusContratoKeyPressed
    
    
   
    
    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField JfTxtDataChegada;
    private javax.swing.JFormattedTextField JfTxtDataSaida;
    private javax.swing.JButton jBtnBuscar;
    private javax.swing.JButton jBtnEditar;
    private javax.swing.JButton jBtnExcluir;
    private javax.swing.JButton jBtnImprimir;
    private javax.swing.JButton jBtnNovo;
    private javax.swing.JButton jBtnSalvar;
    private javax.swing.JComboBox<String> jCboNome;
    private javax.swing.JComboBox<String> jCboPlaca;
    private javax.swing.JComboBox<String> jCboStatusContrato;
    private javax.swing.JComboBox<String> jCboTipoContrato;
    private javax.swing.JFormattedTextField jFTxtCpfCnpj;
    private javax.swing.JFormattedTextField jFtxtCelular;
    private javax.swing.JFormattedTextField jFtxtCep;
    private javax.swing.JFormattedTextField jFtxtCnh;
    private javax.swing.JFormattedTextField jFtxtFone;
    private javax.swing.JFormattedTextField jFtxtRgIe;
    private javax.swing.JLabel jLabelCodigo;
    private javax.swing.JPanel jPanDadosGerais;
    private javax.swing.JPanel jPaneDadosVeiculos;
    private javax.swing.JPanel jPanelDadosEnderecos;
    private javax.swing.JPanel jPanelDadosValores;
    private javax.swing.JTextField jTxtAnoFabricacao;
    private javax.swing.JTextField jTxtAnoModelo;
    private javax.swing.JTextField jTxtBairro;
    private javax.swing.JTextField jTxtChassi;
    private javax.swing.JTextField jTxtCidade;
    private javax.swing.JTextField jTxtClasse;
    private javax.swing.JTextField jTxtCor;
    private javax.swing.JTextField jTxtEmail;
    private javax.swing.JTextField jTxtEstado;
    private javax.swing.JTextField jTxtKm;
    private javax.swing.JTextField jTxtKmUtil;
    private javax.swing.JTextField jTxtMarca;
    private javax.swing.JTextField jTxtNome;
    private javax.swing.JTextField jTxtNumero;
    private javax.swing.JTextField jTxtNumeroRenavan;
    private javax.swing.JTextField jTxtObservacoes;
    private javax.swing.JTextField jTxtQtdDias;
    private javax.swing.JTextField jTxtReferencia;
    private javax.swing.JTextField jTxtRua;
    private javax.swing.JTextField jTxtTipo;
    private javax.swing.JTextField jTxtTipoEnd;
    private javax.swing.JTextField jTxtValorDiaria;
    private javax.swing.JTextField jTxtValorExtra;
    private javax.swing.JTextField jTxtValorKmFinal;
    private javax.swing.JTextField jTxtValorKmRodado;
    private javax.swing.JTextField jTxtValorTotal;
    private javax.swing.JTextField jTxtVeiculo;
    // End of variables declaration//GEN-END:variables

    private void setIcon() {
       setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/imagens/icone_contrato2.png")));
    }
}
