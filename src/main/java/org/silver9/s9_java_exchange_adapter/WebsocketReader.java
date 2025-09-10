package org.silver9.s9_java_exchange_adapter;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.net.URI;
import java.time.Duration;

@Component
public class WebsocketReader {

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
                .option(io.netty.channel.ChannelOption.SO_KEEPALIVE, true);

        // Create the WebSocket client
        WebSocketClient client = new ReactorNettyWebSocketClient(httpClient);

        client.execute(URI.create("wss://stream.binance.com:9443/ws?streams=btcusdt@trade&timeUnit=MICROSECOND"), webSocketSession -> {
            webSocketSession
                    .send(subscriber -> subscriber.onNext(WebSocketMessage.TextMessage.of("Hello, WebSocket!")))
                    .receive()
                    .map(WebSocketMessage::getNativeMessage)
                    .subscribe(System.out::println);
            return webSocketSession.close();
        });

    }
}
