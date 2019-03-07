package com.techonologies.developer.alves.projcadbusimageviewfirebase.model;

/**
 * @author  ALVESDEVTEC - André Luiz Alves
 * @since 07/03/2019 - 12H05
 * @version 2.0 turboMax
 */

public class Usuario {
    private String nome;
    private String urlImagem;

    public Usuario() {
    }

    /**
     * @param nome recebe o nome do usuário
     * @param urlImagem recebe a URL de Upload da imagem
     */
    public Usuario(String nome, String urlImagem) {
        //Validando campo nome!
        if (nome.trim().equals("")) {
            nome = "Sem Nome";
        }
        this.nome = nome;
        this.urlImagem = urlImagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
