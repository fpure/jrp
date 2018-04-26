/**
 * 监听Ngrok的连接请求
 */
package jrp.server.listener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import jrp.log.Logger;
import jrp.server.Context;
import jrp.server.server.ClientServer;
import jrp.socket.SocketHelper;

public class ClientListener implements Runnable
{
	private Context context;
	private Logger log;

	public ClientListener(Context context) throws IOException
	{
		this.context = context;
		this.log = context.getLog();
	}

	@Override
	public void run()
	{
		try(ServerSocket ssocket = SocketHelper.newSSLServerSocket(context.getPort()))
		{
			log.log("监听建立成功：[%s]", context.getPort());
			while(true)
			{
				Socket socket = ssocket.accept();
				Thread thread = new Thread(new ClientServer(socket, context));
				thread.setDaemon(true);
				thread.start();
			}
		}
		catch(IOException e)
		{
			log.err(e.getMessage());
		}
	}
}
