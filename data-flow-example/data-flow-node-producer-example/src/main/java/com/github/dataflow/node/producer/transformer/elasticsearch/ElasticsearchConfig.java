package com.github.dataflow.node.producer.transformer.elasticsearch;

/**
 * @author kevin
 * @date 2017-09-05 11:11 PM.
 */
public class ElasticsearchConfig {
    public static final String CLUSTER_NAME                                            = "cluster.name";
    public static final String CLUSTER_HOST                                            = "cluster.host";
    public static final String CLIENT_TRANSPORT_SNIFF                                  = "client.transport.sniff";
    public static final String PATH_HOME                                               = "path.home";
    public static final String PATH_CONF                                               = "path.conf";
    public static final String SEARCHGUARD_ENABLE                                      = "searchguard.enable";
    public static final String SEARCHGUARD_SSL_TRANSPORT_ENABLED                       = "searchguard.ssl.transport.enabled";
    public static final String SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_FILEPATH             = "searchguard.ssl.transport.keystore_filepath";
    public static final String SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_FILEPATH           = "searchguard.ssl.transport.truststore_filepath";
    public static final String SEARCHGUARD_SSL_TRANSPORT_ENFORCE_HOSTNAME_VERIFICATION = "searchguard.ssl.transport.enforce_hostname_verification";
    public static final String SEARCHGUARD_SSL_TRANSPORT_ENABLE_OPENSSL_IF_AVAILABLE   = "searchguard.ssl.transport.enable_openssl_if_available";
    public static final String SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_PASSWORD             = "searchguard.ssl.transport.keystore_password";
    public static final String SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_PASSWORD           = "searchguard.ssl.transport.truststore_password";
}
