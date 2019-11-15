package tg.licorne.entraideagro.config;

/**
 * Created by Admin on 12/06/2018.
 */

public class ServerConfig {
    private static String  ip = "54.38.78.69";
    private static String port = "80";
    private static String path = "/GEST_FARM/public/api/";
    private static String path_file = "/GEST_FARM/public";
    public static String url_sever = "http://"+ip+":"+port+path;
    public static String url_file = "http://"+ip+":"+port+path_file;
    public static String url_port_ip= "http://"+ip+":"+port+"/";
}
