package tblcheck.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.tablecheck.service.ServiceApplication;
import java.net.Inet4Address;
import java.net.InterfaceAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.ConfigurableApplicationContext;

import tblcheck.model.Config;

public class HttpHelper {

	public static final int TIMEOUT = 3 * 1000;
	static final Gson gson = new Gson();

	public static String sendPost(String url, List<NameValuePair> data, boolean getHost) {
		System.out.println("Executing entity " + gson.toJson(data));
		try {
			return sendPost(url, new UrlEncodedFormEntity(data), getHost);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String sendPost(String url, List<NameValuePair> data) throws Exception {
		return sendPost(url, new UrlEncodedFormEntity(data), true);
	}

	public static String sendPost(String url, String data) throws Exception {
		// return sendPost(url, new ByteArrayEntity(data.getBytes("UTF-8")));
		return sendPost(url, new StringEntity(data), true);
	}

	private static String sendPost(String url, HttpEntity entity, boolean getHost) throws Exception {
		String result = null;
		CloseableHttpClient httpclient = HttpClients.createSystem();
		try {
			url = getHost ? String.format("http://%s:%d/%s", Config.getInstance().getServerHost(),
					Config.getInstance().getServerPort(), url) : url;
			HttpPost httpPost = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT).setSocketTimeout(TIMEOUT)
					.setConnectTimeout(TIMEOUT).setConnectionRequestTimeout(TIMEOUT).build();
			httpPost.setConfig(requestConfig);

			httpPost.setEntity(entity);
			System.out.println("Executing request " + httpPost.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				result = parseEntity(response.getEntity());
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return result;
	}

	public static String sendGet(String url) throws Exception {
		return sendGetUseHttpClient(url);
	}

	private static String sendGetUseHttpClient(String url) throws Exception {
		String result = null;
		CloseableHttpClient httpclient = HttpClients.createSystem();
		HttpGet httpGet = new HttpGet(Config.getInstance().getServerHost() + url);
		RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT).setSocketTimeout(TIMEOUT)
				.setConnectTimeout(TIMEOUT).setConnectionRequestTimeout(TIMEOUT).build();
		httpGet.setConfig(requestConfig);

		CloseableHttpResponse response = httpclient.execute(httpGet);
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the
		// network socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally
		// clause.
		// Please note that if response content is not fully consumed the
		// underlying
		// connection cannot be safely re-used and will be shut down and
		// discarded
		// by the connection manager.
		try {
			result = parseEntity(response.getEntity());
		} finally {
			response.close();
		}
		return result;
	}

	private static String parseEntity(HttpEntity entity) throws Exception {
		String result = null;
		if (entity != null) {
			try {
				result = EntityUtils.toString(entity);
			} catch (Exception ex) {
				// In case of an IOException the connection will be released
				// back to the connection manager automatically
				throw ex;
			} finally {
				// Closing the input stream will trigger connection release
				EntityUtils.consume(entity);
			}
		}
		return result;
	}

	public static String post(String url, List<NameValuePair> data) throws CheckException {
		return post(url, data, true);
	}

	public static String post(String url, Object entity) throws CheckException {
		return post(url, entity, false);
	}

	public static String post(String url, Object entity, boolean hasParams) throws CheckException {
		String result = null;

		try (CloseableHttpClient httpclient = HttpClients.createSystem()) {
			HttpPost httpPost = new HttpPost(getUrl(null, url));
			RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT).setSocketTimeout(TIMEOUT)
					.setConnectTimeout(TIMEOUT).setConnectionRequestTimeout(TIMEOUT).build();
			httpPost.setConfig(requestConfig);
			System.out.println(gson.toJson(entity));

			if (hasParams) {
				httpPost.setEntity(new UrlEncodedFormEntity((List<NameValuePair>) entity));
			} else {
				httpPost.setEntity(new StringEntity(gson.toJson(entity)));
				httpPost.addHeader("Content-Type", "application/json");
			}
			System.out.println("Executing request " + httpPost.getRequestLine());
			CloseableHttpResponse response = processExec(url, httpclient, null, httpPost);
			try {
				System.out.println("----------------------------------------");
				System.out.println("Status :: " + response.getStatusLine());
				result = parseEntity(response.getEntity());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				response.close();
			}

		} catch (Exception e) {
			if (e instanceof CheckException) {
				throw (CheckException) e;
			}
			e.printStackTrace();
		}
		System.out.println("Result " + result);
		return result;
	}

	public static String get(String url) throws CheckException {
            String result = null;
            try (CloseableHttpClient httpclient = HttpClients.createSystem()) {
                HttpGet httpGet = new HttpGet(getUrl(null, url));
                RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT).setSocketTimeout(TIMEOUT)
                                .setConnectTimeout(TIMEOUT).setConnectionRequestTimeout(TIMEOUT).build();
                httpGet.setConfig(requestConfig);
                System.out.println("Executing request " + httpGet.getRequestLine());

                CloseableHttpResponse response = processExec(url, httpclient, httpGet, null);
                try {
                    result = parseEntity(response.getEntity());
                }
                catch (Exception e) {
                    result = null;
                }
                finally {
                    response.close();
                }
            } catch (Exception e) {
                if (e instanceof CheckException) {
                    throw (CheckException) e;
                }
                e.printStackTrace();
            }
            return result;
	}

	private static CloseableHttpResponse processExec(String url, CloseableHttpClient httpclient, HttpGet httpGet,
			HttpPost httpPost) throws InterruptedException, CheckException, IOException {
		CloseableHttpResponse response = null;
		int cnt = 0;
		String server = Config.getInstance().getServerHost();
		do {
			try {
				if (cnt > 0) {
                                    // server =
                                    // changeServer(Config.getInstance().getServerPort(), 10);
                                    server = Config.getInstance().getServerHost();
                                    // System.out.println(getUrl(server, url));
                                    if (server != null) {
                                        if (httpPost != null) {
                                                httpPost.setURI(getUrl(server, url));
                                        } else {
                                                httpGet.setURI(getUrl(server, url));
                                        }
                                    }
				}
				if (server != null) {
                                    if (httpPost != null) {
                                        response = httpclient.execute(httpPost);
                                        //if (response!=null) httpPost.releaseConnection();
                                    } else {
                                        response = httpclient.execute(httpGet);
                                    }
				}
			} catch (Exception e) {
				// e.printStackTrace();
				System.err.println(e.getMessage() + "  - " + server);
			}
			cnt++;
		} while (cnt > 0 && cnt <= 2 && response == null);
		if (response == null && cnt > 0) {
			if (httpPost != null) {
                            recovarables.add(httpPost);
			} else {
                            // recovarables.add(httpGet);
			}
			throw new CheckException(ExceptionType.NOSERVER, "");
		} else if (!recovarables.isEmpty()) {
			// String server =
			// changeServer(Config.getInstance().getServerPort(), 10);
			// Config.getInstance().setServerHost(server);
			for (Object rec : recovarables) {
				if (rec instanceof HttpPost) {
					try {
						httpPost = (HttpPost) rec;
						URI uri = httpPost.getURI();
						String host = uri.getHost();
						httpPost.setURI(new URI(uri.toString().replace(host, server)));
						response = httpclient.execute(httpPost);
						httpPost.releaseConnection();
					} catch (Exception e) {
						System.err.println(e.getMessage() + "  - " + server);
					}
				}
			}
			recovarables.clear();
		}
		return response;
	}

	private static List<Object> recovarables = new ArrayList<>();

	public static Object parseResponse(String json, Type type) {
		return gson.fromJson(json, type);
	}

	private static URI getUrl(String server, String url) {
		if (server != null) {
			// Config.getInstance()
			// .setServerHost(String.format("http://%s:%d/", server,
			// Config.getInstance().getServerPort()));
			Config.getInstance().setServerHost(server);
		}
		try {
			return new URI(String.format("http://%s:%d/%s", Config.getInstance().getServerHost(),
					Config.getInstance().getServerPort(), url));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getLocalHost() {
            try {
                for (
                        final Enumeration< NetworkInterface > interfaces =
                                NetworkInterface.getNetworkInterfaces();
                        interfaces.hasMoreElements( );
                        )
                {
                    final NetworkInterface cur = interfaces.nextElement( );
                    
                    if ( cur.isLoopback() )
                    {
                        continue;
                    }
                    
                    for ( final InterfaceAddress addr : cur.getInterfaceAddresses( ) )
                    {
                        final InetAddress inet_addr = addr.getAddress( );
                        
                        if ( !( inet_addr instanceof Inet4Address ) )
                        {
                            continue;
                        }
                        
                        System.out.println( "Using interface " + cur.getName( ) + " (" + inet_addr.getHostAddress( ) + ")" );
                        
                        return inet_addr.getHostAddress( );
                    }
                }
            } catch (SocketException ex) {
                
            }
            return "localhost";
	}

	public static void changeServer(int port, int t) {
            String localIP = null;
            try {
                localIP = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }
            try {
                if (localIP!=null && !localIP.equals("127.0.0.1") && !localIP.equals("localhost")) {
                    System.out.println(">>>>> Trying me as Server");
                    scanStoredIP(localIP, port, t);                    
                }
                else {
                    Thread th = new Thread(new Runnable() {
                        public void run() {
                            String ip = null;
                            do {
                                try {
                                    ip = InetAddress.getLocalHost().getHostAddress();
                                    Thread.sleep(3000);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } while (ip==null || ip.equals("127.0.0.1") || ip.equals("localhost"));
                            scanStoredIP(ip, port, t);
                        }
                    });
                    th.start();
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
	}

	static List<InetAddress> listAllBroadcastAddresses() {
		List<InetAddress> broadcastList = new ArrayList<>();
		Enumeration<NetworkInterface> interfaces;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
			System.out.println(interfaces);
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				System.out.println(networkInterface.getName());
				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue;
				}

				broadcastList.addAll(networkInterface.getInterfaceAddresses().stream()
						.filter(address -> address.getBroadcast() != null).map(address -> address.getBroadcast())
						.collect(Collectors.toList()));
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(broadcastList);
		return broadcastList;
	}
	private static void scanStoredIP(String ip, int port, int t) {
            //nStatus: 0-ip is not available, 1-spring server(8080) is not running, 2-spring server is running on
            if (ip != null && ip.length() > 0) {
                Config.getInstance().setServerChanging(true); 
                
                Thread th = new Thread(new Runnable() {
                    public void run() {
                        try {
                            InetAddress address = InetAddress.getByName(ip);
                            if (address.isReachable(1 * t)) {
                               
                               System.out.println("farhan lines here");
                               System.out.println(">>>>> is Reached ip - " + ip+" : "+port);
                                
                            	try (Socket socket = new Socket(ip, port)) {
                                    System.out.println(">>>>> spring port is already created! port: " + port);
                                    socket.close();
                                    Config.getInstance().setServer(true);
                                    Config.getInstance().setServerConnected(true);
                                    Config.getInstance().setServerHost(ip);
                                    Config.getInstance().setServerChanging(false);
                                    Config.getInstance().saveConfig(true, "spring port is already");                                    
                                    
                                    MulticastPublisher publisher = new MulticastPublisher();
                                    new Thread(publisher).start();
                                    
                                } catch (Exception e) {
                                    System.out.println(">>>>> new spring is created! " + port);
                                    Config.getInstance().setServer(true);
                                    Config.getInstance().setServerConnected(true);
                                    Config.getInstance().setServerHost(ip);
                                    Config.getInstance().setServerChanging(false);
                                    Config.getInstance().saveConfig(true, "new spring");
                                    
                                    String[] arguments = new String[] { ip, String.valueOf(port)};
                                    ServiceApplication.main(arguments);
                                    
                                    MulticastPublisher publisher = new MulticastPublisher();
                                    new Thread(publisher).start();
                                }
                            }
                            else {
                                Config.getInstance().setServerChanging(false);                                    
                            }
                        } catch (Exception e) {
                            Config.getInstance().setServerChanging(false);
                            System.err.println(e.getMessage());
                        }
                    }
                });
                th.start();
            }
	}

	public static Thread getThreadByName(String threadName) {
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName().equals(threadName))
				return t;
		}
		return null;
	}

	public final static byte[] asBytes(String addr) {

		int ipInt = parseNumericAddress(addr);
		if (ipInt == 0)
			return null;

		byte[] ipByts = new byte[4];

		ipByts[3] = (byte) (ipInt & 0xFF);
		ipByts[2] = (byte) ((ipInt >> 8) & 0xFF);
		ipByts[1] = (byte) ((ipInt >> 16) & 0xFF);
		ipByts[0] = (byte) ((ipInt >> 24) & 0xFF);

		return ipByts;
	}

	public final static int parseNumericAddress(String ipaddr) {

		if (ipaddr == null || ipaddr.length() < 7 || ipaddr.length() > 15)
			return 0;

		StringTokenizer token = new StringTokenizer(ipaddr, ".");
		if (token.countTokens() != 4)
			return 0;

		int ipInt = 0;

		while (token.hasMoreTokens()) {

			String ipNum = token.nextToken();

			try {
				int ipVal = Integer.valueOf(ipNum).intValue();
				if (ipVal < 0 || ipVal > 255)
					return 0;

				ipInt = (ipInt << 8) + ipVal;
			} catch (NumberFormatException ex) {
				return 0;
			}
		}
		return ipInt;
	}
	// @Deprecated
	// private static String sendGetUseURLConn(String url) throws Exception {
	// HttpURLConnection con = null;
	// String data = null;
	//
	// try {
	// con = (HttpURLConnection) new URL(url).openConnection();
	// con.setConnectTimeout(TIMEOUT);
	// con.setReadTimeout(TIMEOUT);
	// con.connect();
	// try (BufferedReader br = new BufferedReader(new
	// InputStreamReader(con.getInputStream()))) {
	// data = br.readLine();
	// br.close();
	// con.disconnect();
	// } catch (Exception ex) {
	// }
	// } catch (Exception e) {
	// if (con != null)
	// con.disconnect();
	// }
	//
	// return data;
	// }
}
