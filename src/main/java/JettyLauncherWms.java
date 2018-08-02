
import org.mortbay.http.SocketListener;
import org.mortbay.jetty.Server;

public class JettyLauncherWms {
	protected int port = 8088;
	protected String contextName = "tcl_wms";
	protected String deployPath = "";
	
	public static void main(String[] args) throws Exception {
		JettyLauncherWms jl = new JettyLauncherWms();
		jl.init();
		jl.run();
	}
	
	protected void init() {
	}
	
	protected void run() throws Exception {
		Server server = new Server();
		SocketListener listener = new SocketListener();
		listener.setPort(getPort()); 
		server.addListener(listener);
		server.addWebApplication("/" + getContextName() , getDeployPath());
		server.start();
	}
	
	protected int getPort() {
		return port;
	}
	
	protected String getContextName() {
		return contextName;
	}
	
	protected String getDeployPath() {
		if (deployPath.length() == 0) {
			deployPath = "./target/" + contextName + "/";
		}
		return deployPath;
	}
}
