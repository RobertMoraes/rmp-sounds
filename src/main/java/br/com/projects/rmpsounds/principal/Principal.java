package br.com.projects.rmpsounds.principal;

import br.com.projects.rmpsounds.enums.TipoArtista;
import br.com.projects.rmpsounds.model.Artista;
import br.com.projects.rmpsounds.model.Musica;
import br.com.projects.rmpsounds.repository.ArtistaRepository;
import br.com.projects.rmpsounds.service.ConsultaChatGPT;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private final Scanner teclado = new Scanner(System.in);
    private final ArtistaRepository repositorio;

    public Principal(ArtistaRepository repositorio){
        this.repositorio = repositorio;
    }
    int opcao = -1;
    public void exibirMenu() {

        while (opcao != 0) {
            var menu = """
                    ***** RMP Sounds *****
                                        
                    1 - Cadastrar artistas
                    2 - Cadastrar músicas
                    3 - Listar músicas
                    4 - Buscar músicas por artistas
                    5 - Pesquisar dados sobre um artista
                                        
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = Integer.parseInt(teclado.nextLine());

            switch (opcao) {
                case 1 -> this.cadastrarArtistas();
                case 2 -> this.cadastrarMusicas();
                case 3 -> this.listarMusicas();
                case 4 -> this.buscarMusicasPorArtistas();
                case 5 -> this.pesquisarSobreArtistas();
                case 0 -> System.out.println("Encerrando a aplicação!");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void pesquisarSobreArtistas() {
        System.out.println("Pesquisar dados sobre qual artista?");
        var nome = teclado.nextLine();
        var resposta = ConsultaChatGPT.obterInformacao(nome);
        System.out.println(resposta.trim());
    }

    private void buscarMusicasPorArtistas() {
        System.out.println("Buscar musicar de que artista?");
        var nome = teclado.nextLine();
        List<Musica> musicas = repositorio.buscaMusicaPorArtista(nome);
        musicas.forEach(System.out::println);
    }

    private void listarMusicas() {
        List<Artista> lsArtistas = repositorio.findAll();
        lsArtistas.forEach(a -> a.getMusicas().forEach(System.out::println));
    }

    private void cadastrarMusicas() {
        var cadastrarMusica = "S";
        while (cadastrarMusica.equalsIgnoreCase("S")) {
            System.out.println("Cadastrar música de que artista?");
            var nome = teclado.nextLine();
            Optional<Artista> artista = repositorio.findByNomeContainingIgnoreCase(nome);
            if (artista.isPresent()) {
                System.out.println("Informe o título da música: ");
                var nomeMusica = teclado.nextLine();
                Musica musica = new Musica(nomeMusica);
                musica.setArtista(artista.get());
                artista.get().getMusicas().add(musica);
                repositorio.save(artista.get());
            } else {
                System.out.println("Artista não encontrado");
                System.out.println("Deseja cadastrar artista? (S/N)");
                var cadastrarNovo = "S";
                cadastrarNovo = teclado.nextLine();
                if (cadastrarNovo.equalsIgnoreCase("S")) {
                    cadastrarArtistas();
                } else {
                    System.out.println("Encerrando.....");
                    opcao = 0;
                }
            }
            System.out.println("Deseja cadastrar mais uma musica? (S/N)");
            cadastrarMusica = teclado.nextLine();
        }
    }

    private void cadastrarArtistas() {
        var cadastrarNovo = "S";

        while (cadastrarNovo.equalsIgnoreCase("S")) {
            System.out.println("Informe o nome desse artista: ");
            var nome = teclado.nextLine();
            System.out.println("Informe o tipo desse artista: (solo, duplo ou banda)");
            var tipo = teclado.nextLine();
            TipoArtista tipoArtista = TipoArtista.valueOf(tipo);
            Artista artista = new Artista(nome, tipoArtista);
            repositorio.save(artista);
            System.out.println("Cadastrar novo artista? (S/N)");
            cadastrarNovo = teclado.nextLine();
        }
    }
}
