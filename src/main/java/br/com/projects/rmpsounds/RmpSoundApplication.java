package br.com.projects.rmpsounds;

import br.com.projects.rmpsounds.principal.Principal;
import br.com.projects.rmpsounds.repository.ArtistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RmpSoundApplication implements CommandLineRunner {
    @Autowired
    private ArtistaRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(RmpSoundApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(repository);
        principal.exibirMenu();
    }
}
