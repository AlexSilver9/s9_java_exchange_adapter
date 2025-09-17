package org.silver9.s9_java_exchange_adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.net.URI;
import java.time.Duration;

@Configuration
public class WebsocketReader {

    @Autowired
    private Shovel shovel;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> read();
    }

    public void read() {
        // Configure connection pool for optimal performance
        ConnectionProvider provider = ConnectionProvider.builder("websocket-pool")
                .maxConnections(100)
                .maxIdleTime(Duration.ofSeconds(20))
                .maxLifeTime(Duration.ofSeconds(60))
                .pendingAcquireTimeout(Duration.ofSeconds(60))
                .evictInBackground(Duration.ofSeconds(120))
                .build();

        // Create optimized HTTP client
        HttpClient httpClient = HttpClient.create(provider)
                .option(io.netty.channel.ChannelOption.TCP_NODELAY, true)
                .option(io.netty.channel.ChannelOption.SO_KEEPALIVE, true)
                .secure(); // Uses system's default SSL context with proper certificate validation
                /*.secure(sslContextSpec -> {
                    try {
                        sslContextSpec.sslContext(SslContextBuilder.forClient()
                                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                .build());
                    } catch (SSLException e) {
                        throw new RuntimeException(e);
                    }
                });
                 */

        // Create the WebSocket client
        WebSocketClient client = new ReactorNettyWebSocketClient(httpClient);

        client.execute(URI.create("wss://fstream.binance.com:443/stream?streams=btcusdt@trade"), webSocketSession ->
        //client.execute(URI.create("wss://stream.binance.com:9443/stream?streams=btcusdt@trade"), webSocketSession ->
                        webSocketSession
                                .receive()
                                .map(webSocketMessage -> webSocketMessage.getPayloadAsText())
                                //.doOnNext(s -> System.out.println("Received: " + s))
                                .doOnNext(s -> shovel.onNextMessage(s))
                                .doOnError(e -> System.err.println("Error: " + e.getMessage()))
                                .then())
                .doOnError(e -> System.err.println("Connection Error: " + e.getMessage()))
                .subscribe();
    }
}
