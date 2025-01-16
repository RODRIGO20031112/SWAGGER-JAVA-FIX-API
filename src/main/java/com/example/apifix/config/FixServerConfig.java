package com.example.apifix.config;

import com.example.apifix.fixserver.FixServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import quickfix.SessionSettings;
import quickfix.ConfigError;
import java.io.InputStream;
import java.io.IOException;

@Configuration
public class FixServerConfig {

    private final FixServer fixServer;

    public FixServerConfig(FixServer fixServer) {
        this.fixServer = fixServer;
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            try {
                // Carregar configurações do FIX a partir do classpath
                ClassPathResource resource = new ClassPathResource("server.cfg");
                InputStream configFile = resource.getInputStream(); // Acessando o arquivo dentro do classpath
                SessionSettings settings = new SessionSettings(configFile);

                // Iniciar o servidor FIX
                fixServer.startServer(settings);
            } catch (IOException | ConfigError e) {
                e.printStackTrace();
            }
        };
    }
}
