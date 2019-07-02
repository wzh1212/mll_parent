package com.xmcc.mll_es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public TransportClient client() throws UnknownHostException {
        //注意这儿是tcp链接 是9300
        InetSocketTransportAddress transportAddress = new InetSocketTransportAddress(InetAddress.getByName("192.168.1.186"), 9300);
        //设置集群名称
        Settings settings = Settings.builder().put("cluster.name", "xmcc").build();
        PreBuiltTransportClient preBuiltTransportClient = new PreBuiltTransportClient(settings);
        preBuiltTransportClient.addTransportAddress(transportAddress);
        return preBuiltTransportClient;

    }

}
