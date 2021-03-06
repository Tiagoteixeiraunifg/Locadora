
package views;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultFormatterFactory;
import models.ClsColaborador;
import models.ClsImpressao;
import models.ClsLogin;
import models.ClsValidacoes;
import net.sf.jasperreports.engine.JRException;



/**
 *  
 * @author Tiago Teixeira
 */
public class JfrmColaborador extends javax.swing.JFrame {
    
    //Declaração de objetos a serem usados na tela
    ClsColaborador clscolaborador;
    controls.ColaboradorDAO colaboradorDAO;
    models.ClsMascaraCampos clsMascaraCampos;
    List<ClsColaborador> listColaboradorBD;
    //declaração de variaveis de controle da tela
    private String userLoged;
    private int userIdLoged;
    private String CpfUserLoged;
    private String userNivel;
    private boolean editando;
    private boolean precionado;
    
    //metodos construtores da tela
    public JfrmColaborador(){
        initComponents();
        editando = false;
        clscolaborador = new models.ClsColaborador();
        colaboradorDAO = new controls.ColaboradorDAO();
        clsMascaraCampos = new models.ClsMascaraCampos();
        getIcon();
        disableControls(); 
        try {
            initMascaraCpfTelefone();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao iniciar as mascaras" + ex +"", "ERRO", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public JfrmColaborador(ClsLogin clslogin){
        userLoged = clslogin.getUserLoged();
        userIdLoged = clslogin.getId();
        CpfUserLoged = clslogin.getCpfUserLoged();
        userNivel = clslogin.getNivel();
        
        initComponents();
        editando = false;
        precionado = false;
        clscolaborador = new models.ClsColaborador();
        colaboradorDAO = new controls.ColaboradorDAO();
        clsMascaraCampos = new models.ClsMascaraCampos();
        listColaboradorBD = colaboradorDAO.selectAll();
        getIcon();
        disableControls(); 
        try {
            initMascaraCpfTelefone();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao iniciar as mascaras" + ex +"", "ERRO", JOptionPane.ERROR_MESSAGE);
        }
        loadCboNivel();
    }
    
    private void loadCboNivel(){
        jCboNivel.addItem("OPERADOR");
        jCboNivel.addItem("GERENTE");
    }
    
    //funções e eventos da tela
    private boolean listBuscaCPF(String cpf){
        boolean encontrado = false;
        for (int i = 0; i < listColaboradorBD.size(); i++) {
            if (listColaboradorBD.get(i).getCpf().equals(cpf)) {
                encontrado = true;
                break;
            }
        }
        return encontrado;
    }
    
    private void initMascaraCpfTelefone() throws ParseException {
       // iniciando a mascara com os formatos contidos na classe MascaraCampos//
       jTxtFone.setFormatterFactory(new DefaultFormatterFactory(clsMascaraCampos.mascaraCelular(jTxtFone))); 
        
       jTxtCpf.setFormatterFactory(new DefaultFormatterFactory(clsMascaraCampos.mascaraCpf(jTxtCpf))); 
        
    }
    
    private void buscaColaborador(){
        String cpf = JOptionPane.showInputDialog("Digite o CPF para procurar");
        if (cpf.equals("")) {
            JbtnNovo.requestFocus();
        } else {
            boolean valido = new ClsValidacoes().isValid(cpf);
            if (valido == true) {
                List<models.ClsColaborador> ResultSet = colaboradorDAO.select(cpf);
                if (ResultSet.size() < 1) {
                    JOptionPane.showMessageDialog(this, "Erro: CPF não Cadastrado!", "ERRO", JOptionPane.ERROR_MESSAGE);
                    buscaColaborador();
                } else {
                    for (models.ClsColaborador clb : ResultSet) {
                        jTxtNome.setText(clb.getNome());
                        clscolaborador.setNome(clb.getNome());
                        jTxtCpf.setText(clb.getCpf());
                        clscolaborador.setCpf(clb.getCpf());
                        jTxtNmlogin.setText(clb.getNomeLogin());
                        clscolaborador.setNomeLogin(clb.getNomeLogin());
                        clscolaborador.setCpf_funCadastro(new ClsValidacoes().replaceDado(clb.getCpf_funCadastro()));
                        jTxtFone.setText(clb.getTelefone());
                        clscolaborador.setTelefone(clb.getTelefone());
                        jTxtSenha.setText(clb.getSenha());
                        clscolaborador.setSenha(clb.getSenha());
                        jCboNivel.setSelectedItem(clb.getNivel());
                        clscolaborador.setNivel(clb.getNivel());
                        clscolaborador.setId(clb.getId());

                    }
                    disableCtrlBusca();
                    precionado = false;
                    setIconBtnNv(false);
                    JbtnNovo.setEnabled(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "O CPF digitado é invalido!", "ERRO", JOptionPane.ERROR_MESSAGE);
                buscaColaborador();
            }
        }
    }
    
    private void getIcon() {
        // setando o icone principal do Jframe //
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/imagens/Icone_colaborador.png")));
    }

    private void disableControls() {
        //desabilitando os controles jTextBox
        jTxtCpf.setEnabled(false);
        jTxtFone.setEnabled(false);
        jTxtNmlogin.setEnabled(false);
        jTxtNome.setEnabled(false);
        jTxtSenha.setEnabled(false);
        jCboNivel.setEnabled(false);
        //desabilitando os controles Jbutton
        JbtnSalvar.setEnabled(false);
        JbtnImprimir.setVisible(false);
        JbtnBuscar.setEnabled(true);
        JbtnExcluir.setEnabled(false);
        JbtnEditar.setEnabled(false);
        JbtnNovo.setEnabled(true);
        
    }
    
    private void disableCtrlBusca() {
        //desabilitando os controles jTextBox
        jTxtCpf.setEnabled(false);
        jTxtFone.setEnabled(false);
        jTxtNmlogin.setEnabled(false);
        jTxtNome.setEnabled(false);
        jTxtSenha.setEnabled(false);
        jCboNivel.setEnabled(false);
        //desabilitando os controles Jbutton
        JbtnSalvar.setEnabled(false);
        JbtnImprimir.setVisible(true);
        JbtnBuscar.setEnabled(true);
        JbtnExcluir.setEnabled(true);
        JbtnEditar.setEnabled(true);
        JbtnNovo.setEnabled(false);
        
    }
    
    private void enableControls() {
        //habilitando os controles apos o clieque no botão novo e editar
        jTxtCpf.setEnabled(true);
        jTxtFone.setEnabled(true);
        jTxtNmlogin.setEnabled(true);
        jTxtNome.setEnabled(true);
        jTxtSenha.setEnabled(true);
        jCboNivel.setEnabled(true);
        //desabilitando os botes e habilitando apenas o Jbutton salvar
        JbtnNovo.setEnabled(true);
        JbtnExcluir.setEnabled(false);
        JbtnEditar.setEnabled(false);
        JbtnBuscar.setEnabled(false);
        JbtnSalvar.setEnabled(true);
    }
    
    private void clearTextBox() {
        //limpando os TxtBox
        jTxtCpf.setText("");
        jTxtFone.setText("");
        jTxtNmlogin.setText("");
        jTxtNome.setText("");
        jTxtSenha.setText("");
    }
    
    private void setIconBtnNv(boolean funcao){
        if (funcao == true) {
            JbtnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icone_cancelar.png"))); // NOI18N
            JbtnNovo.setToolTipText("Clique aqui para cancelar a operacao");
        } else {
            JbtnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/add_121935.png"))); // NOI18N
            JbtnNovo.setToolTipText("Clique aqui para novo Colaborador");
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JbtnExcluir = new javax.swing.JButton();
        JbtnBuscar = new javax.swing.JButton();
        JbtnSalvar = new javax.swing.JButton();
        JbtnEditar = new javax.swing.JButton();
        JbtnNovo = new javax.swing.JButton();
        JpanDadosGerais = new javax.swing.JPanel();
        jTxtNome = new javax.swing.JTextField();
        jTxtNmlogin = new javax.swing.JTextField();
        jTxtSenha = new javax.swing.JTextField();
        jTxtCpf = new javax.swing.JFormattedTextField();
        jTxtFone = new javax.swing.JFormattedTextField();
        jCboNivel = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        JbtnImprimir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro Colaboraores");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        JbtnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/bin_121907.png"))); // NOI18N
        JbtnExcluir.setToolTipText("Clique aqui para excluir Colaborador");
        JbtnExcluir.setBorder(null);
        JbtnExcluir.setFocusPainted(false);
        JbtnExcluir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JbtnExcluirMouseClicked(evt);
            }
        });

        JbtnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/search_121759.png"))); // NOI18N
        JbtnBuscar.setToolTipText("Clique aqui para buscar Colaborador usando o seu CPF");
        JbtnBuscar.setBorder(null);
        JbtnBuscar.setFocusPainted(false);
        JbtnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JbtnBuscarMouseClicked(evt);
            }
        });
        JbtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JbtnBuscarActionPerformed(evt);
            }
        });

        JbtnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/save_121760.png"))); // NOI18N
        JbtnSalvar.setToolTipText("Clique aqui para salvar Colaborador");
        JbtnSalvar.setBorder(null);
        JbtnSalvar.setFocusPainted(false);
        JbtnSalvar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JbtnSalvarMouseClicked(evt);
            }
        });

        JbtnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/new_121792.png"))); // NOI18N
        JbtnEditar.setToolTipText("Clique aqui para editar Colaborador");
        JbtnEditar.setBorder(null);
        JbtnEditar.setFocusPainted(false);
        JbtnEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JbtnEditarMouseClicked(evt);
            }
        });

        JbtnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/add_121935.png"))); // NOI18N
        JbtnNovo.setToolTipText("Clique aqui para novo Colaborador");
        JbtnNovo.setBorder(null);
        JbtnNovo.setFocusPainted(false);
        JbtnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JbtnNovoActionPerformed(evt);
            }
        });

        JpanDadosGerais.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados Gerais"));

        jTxtNome.setBackground(new java.awt.Color(240, 240, 240));
        jTxtNome.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtNome.setToolTipText("Digite o nome completo do colaborador");
        jTxtNome.setBorder(javax.swing.BorderFactory.createTitledBorder("Nome"));
        jTxtNome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtNomeFocusLost(evt);
            }
        });
        jTxtNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTxtNomeKeyPressed(evt);
            }
        });

        jTxtNmlogin.setBackground(new java.awt.Color(240, 240, 240));
        jTxtNmlogin.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtNmlogin.setToolTipText("Digite o nomedo login do colaborador");
        jTxtNmlogin.setBorder(javax.swing.BorderFactory.createTitledBorder("Nome Login"));
        jTxtNmlogin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtNmloginFocusLost(evt);
            }
        });

        jTxtSenha.setBackground(new java.awt.Color(240, 240, 240));
        jTxtSenha.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtSenha.setToolTipText("Digite a senha do colaborador");
        jTxtSenha.setBorder(javax.swing.BorderFactory.createTitledBorder("Senha"));
        jTxtSenha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtSenhaFocusLost(evt);
            }
        });

        jTxtCpf.setBackground(new java.awt.Color(240, 240, 240));
        jTxtCpf.setBorder(javax.swing.BorderFactory.createTitledBorder("CPF"));
        jTxtCpf.setToolTipText("Digite o CPF do colaborador");
        jTxtCpf.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtCpf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtCpfFocusLost(evt);
            }
        });

        jTxtFone.setBackground(new java.awt.Color(240, 240, 240));
        jTxtFone.setBorder(javax.swing.BorderFactory.createTitledBorder("Telefone"));
        jTxtFone.setToolTipText("Digite o Telefone do colaborador");
        jTxtFone.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTxtFone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtFoneFocusLost(evt);
            }
        });
        jTxtFone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTxtFoneKeyPressed(evt);
            }
        });

        jCboNivel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCboNivel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        jCboNivel.setToolTipText("Selecione o nivel de acesso do colaborador");
        jCboNivel.setBorder(javax.swing.BorderFactory.createTitledBorder("Nivel"));
        jCboNivel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jCboNivelFocusLost(evt);
            }
        });

        jLabel2.setText("Selecione o nível de acesso que deseja atribuir para o colaborador:");

        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("Caso selecione GERENTE terá possibilidade de EXCLUIR CADASTROS");

        javax.swing.GroupLayout JpanDadosGeraisLayout = new javax.swing.GroupLayout(JpanDadosGerais);
        JpanDadosGerais.setLayout(JpanDadosGeraisLayout);
        JpanDadosGeraisLayout.setHorizontalGroup(
            JpanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpanDadosGeraisLayout.createSequentialGroup()
                .addGroup(JpanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(JpanDadosGeraisLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(JpanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTxtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JpanDadosGeraisLayout.createSequentialGroup()
                                .addComponent(jTxtNmlogin, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTxtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(JpanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTxtCpf, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                            .addComponent(jTxtFone)))
                    .addGroup(JpanDadosGeraisLayout.createSequentialGroup()
                        .addGroup(JpanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JpanDadosGeraisLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JpanDadosGeraisLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)))
                        .addComponent(jCboNivel, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JpanDadosGeraisLayout.setVerticalGroup(
            JpanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpanDadosGeraisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JpanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTxtNome)
                    .addComponent(jTxtCpf))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(JpanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTxtNmlogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtFone))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(JpanDadosGeraisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCboNivel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(JpanDadosGeraisLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)))
                .addGap(25, 25, 25))
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/3592854-add-user-business-man-employee-general-human-member-office_107767.png"))); // NOI18N

        JbtnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/print_121773.png"))); // NOI18N
        JbtnImprimir.setToolTipText("Clique aqui para imprimir Relatorio Colaborador");
        JbtnImprimir.setBorder(null);
        JbtnImprimir.setFocusPainted(false);
        JbtnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JbtnImprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(JbtnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(JbtnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JbtnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JbtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JbtnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JbtnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(JpanDadosGerais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 13, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JbtnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JbtnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JbtnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JbtnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JbtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JbtnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JpanDadosGerais, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        ClsLogin clslogin = new ClsLogin();
        clslogin.setUserLoged(userLoged);
        clslogin.setId(userIdLoged);
        clslogin.setNivel(userNivel);
        views.JfrmPrincipal telaprincipal = new views.JfrmPrincipal(clslogin);
        telaprincipal.setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void JbtnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JbtnNovoActionPerformed
       if(precionado == false){
           enableControls();
           setIconBtnNv(true);
           editando = false;
           precionado = true;
           if(CpfUserLoged == null){
                clscolaborador.setCpf_funCadastro("00000000000");
           }else{
                clscolaborador.setCpf_funCadastro(new ClsValidacoes().replaceDado(CpfUserLoged));
           }           
           clearTextBox();
       }else{
           clearTextBox();
           disableControls();
           setIconBtnNv(false);
           precionado = false;
       }
        
       
    }//GEN-LAST:event_JbtnNovoActionPerformed

    private void jTxtNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtNomeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB) {
            if (jTxtNome.getText().length() < 10) {
                JOptionPane.showMessageDialog(this, "O nome está muito pequeno", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
            } else {
                clscolaborador.setNome(jTxtNome.getText());
            }
        }
    }//GEN-LAST:event_jTxtNomeKeyPressed

    private void jTxtNomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtNomeFocusLost
       if (jTxtNome.getText().length() < 10) {
                JOptionPane.showMessageDialog(this, "O nome está muito pequeno", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
            } else {
                clscolaborador.setNome(jTxtNome.getText());
            }
    }//GEN-LAST:event_jTxtNomeFocusLost

    private void jTxtNmloginFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtNmloginFocusLost
       if(jTxtNmlogin.getText().length() > 15){
           JOptionPane.showMessageDialog(this, "Apelido maior que o permitido!", "ERRO", JOptionPane.ERROR_MESSAGE);
       }else{
           clscolaborador.setNomeLogin(jTxtNmlogin.getText());
       }
    }//GEN-LAST:event_jTxtNmloginFocusLost

    private void jTxtSenhaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtSenhaFocusLost
        if(jTxtSenha.getText().length() > 15){
            JOptionPane.showMessageDialog(this, "Senha muito extensa!", "ERRO", JOptionPane.ERROR_MESSAGE);
        }else{
            clscolaborador.setSenha(jTxtSenha.getText());
        }
    }//GEN-LAST:event_jTxtSenhaFocusLost

    private void JbtnSalvarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JbtnSalvarMouseClicked
        if (!editando) {
            colaboradorDAO.save(clscolaborador);
            if (colaboradorDAO.isSucesso()) {
                JOptionPane.showMessageDialog(this, colaboradorDAO.getRetorno(), "Informação", JOptionPane.INFORMATION_MESSAGE);
                disableControls();
                setIconBtnNv(false);
                listColaboradorBD.clear();
                listColaboradorBD = colaboradorDAO.selectAll();
                JbtnNovo.setEnabled(true);
                precionado = false;
                JbtnExcluir.setEnabled(true);
                JbtnEditar.setEnabled(true);
                JbtnBuscar.setEnabled(true);
                JbtnImprimir.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(this, colaboradorDAO.getRetorno(), "Informação", JOptionPane.INFORMATION_MESSAGE);
                disableControls();
                setIconBtnNv(false);
                JbtnNovo.setEnabled(true);
                precionado = false;
                clearTextBox();               
                JbtnExcluir.setEnabled(true);
                JbtnEditar.setEnabled(true);
                JbtnBuscar.setEnabled(true);
                JbtnImprimir.setVisible(true);
            }
            
        }else{
            colaboradorDAO.update(clscolaborador);
            JOptionPane.showMessageDialog(this, colaboradorDAO.getRetorno(), "Informação", JOptionPane.INFORMATION_MESSAGE);
            disableControls();
            precionado = false;
            setIconBtnNv(false);
            JbtnNovo.setEnabled(true);
            JbtnExcluir.setEnabled(true);
            JbtnEditar.setEnabled(true);
            JbtnBuscar.setEnabled(true);
            JbtnImprimir.setVisible(true);
        }
        
        
        
    }//GEN-LAST:event_JbtnSalvarMouseClicked

    private void JbtnBuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JbtnBuscarMouseClicked
        buscaColaborador();
    }//GEN-LAST:event_JbtnBuscarMouseClicked

    private void JbtnEditarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JbtnEditarMouseClicked
       enableControls();
       editando = true;
       precionado = true;
       setIconBtnNv(precionado);
    }//GEN-LAST:event_JbtnEditarMouseClicked

    private void JbtnExcluirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JbtnExcluirMouseClicked
        int deletar = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir?", "Atenção", JOptionPane.YES_NO_OPTION);
        if(deletar == 0){
            colaboradorDAO.delete(clscolaborador.getId());
            if(colaboradorDAO.isSucesso() == true){
                clearTextBox();
                disableControls();
                setIconBtnNv(false);
                precionado = false;
            }else{
                JOptionPane.showMessageDialog(this, colaboradorDAO.getRetorno() + "O usuario "+userLoged+""
                                              + " não tem permissao para deletar!", "Erro", JOptionPane.ERROR_MESSAGE);
                setIconBtnNv(true);
                precionado = true;
            }
            
        }
        
    }//GEN-LAST:event_JbtnExcluirMouseClicked

    private void jTxtFoneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTxtFoneKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB) {
            if (jTxtFone.getText().length() < 11) {
                JOptionPane.showMessageDialog(this, "O numero do Celular é invalido, tamanho menor que 11 digitos!", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
            } else {
                clscolaborador.setTelefone(jTxtFone.getText());
            }
        }
    }//GEN-LAST:event_jTxtFoneKeyPressed

    private void jTxtCpfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtCpfFocusLost
            
        if (jTxtCpf.getText().length() < 11) {
            JOptionPane.showMessageDialog(this, "O numero do CPF é invalido, tamanho menor que 11 digitos!", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
            jTxtCpf.setText("");
            jTxtCpf.requestFocus();
        } else if (jTxtCpf.getText().length() < 11) {
            JOptionPane.showMessageDialog(this, "O numero do CPF é invalido, tamanho não pode ser maior que 11 digitos!", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
            jTxtCpf.setText("");
            jTxtCpf.requestFocus();
        } else if (new ClsValidacoes().isValid(jTxtCpf.getText()) == false) {
            JOptionPane.showMessageDialog(this, "O numero do CPF é invalido!", "ERRO", JOptionPane.ERROR_MESSAGE);
            jTxtCpf.setText("");
            jTxtCpf.requestFocus();
        } else if (listBuscaCPF(new ClsValidacoes().replaceDado(jTxtCpf.getText()))) {
            JOptionPane.showMessageDialog(this, "Numero do CPF já cadastrado!", "ERRO", JOptionPane.ERROR_MESSAGE);
            jTxtCpf.setText("");
            jTxtCpf.requestFocus();
        } else {
            clscolaborador.setCpf(new ClsValidacoes().replaceDado(jTxtCpf.getText()));
        }
    }//GEN-LAST:event_jTxtCpfFocusLost

    private void jTxtFoneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtFoneFocusLost
        if (jTxtFone.getText().length() < 11) {
            JOptionPane.showMessageDialog(this, "O numero do Celular é invalido, tamanho menor que 11 digitos!", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
        } else {
            clscolaborador.setTelefone(jTxtFone.getText());
        }
    }//GEN-LAST:event_jTxtFoneFocusLost

    private void JbtnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JbtnImprimirActionPerformed
        File directory = new File("./src/relatorios/relColaborador.jrxml");
        // passa o caminho do relatorio e o parametro para carregar o relatorio. 
        try {
           ClsImpressao clsimpressao = new ClsImpressao();
            clsimpressao.ClsImpressao(directory.getAbsolutePath(), "Cpf", clscolaborador.getCpf(), "Colaboradores");
        } catch (ClassNotFoundException | SQLException | JRException e) {
            JOptionPane.showMessageDialog(this, "Erro foi aqui" + e, "Erro", JOptionPane.ERROR_MESSAGE);
        }
        clearTextBox();
        disableControls();
        setIconBtnNv(false);
        precionado = false;
    }//GEN-LAST:event_JbtnImprimirActionPerformed

    private void JbtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JbtnBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JbtnBuscarActionPerformed

    private void jCboNivelFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCboNivelFocusLost
        if(jCboNivel.getSelectedItem() == "Selecione"){
            JOptionPane.showConfirmDialog(this, "Por favor selecione o Nivel do Colaborador", "INFORMAÇÃO", JOptionPane.INFORMATION_MESSAGE);
        }else if (jCboNivel.getSelectedItem() != "Selecione") {
                clscolaborador.setNivel(jCboNivel.getSelectedItem().toString());
        }  
    }//GEN-LAST:event_jCboNivelFocusLost

    /**
     * @param args the command line arguments
     */
      

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JbtnBuscar;
    private javax.swing.JButton JbtnEditar;
    private javax.swing.JButton JbtnExcluir;
    private javax.swing.JButton JbtnImprimir;
    private javax.swing.JButton JbtnNovo;
    private javax.swing.JButton JbtnSalvar;
    private javax.swing.JPanel JpanDadosGerais;
    private javax.swing.JComboBox<String> jCboNivel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JFormattedTextField jTxtCpf;
    private javax.swing.JFormattedTextField jTxtFone;
    private javax.swing.JTextField jTxtNmlogin;
    private javax.swing.JTextField jTxtNome;
    private javax.swing.JTextField jTxtSenha;
    // End of variables declaration//GEN-END:variables

 


   
}
