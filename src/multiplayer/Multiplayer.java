package multiplayer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidParameterException;
import java.util.InvalidPropertiesFormatException;

import javax.swing.JOptionPane;

public class Multiplayer implements Runnable {
	
	//Attribute
	private String ip;
	private int port;
	private String key;
	
	private Socket socket;
	private ServerSocket serverSocket;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	private int errors = 0;
	
	private boolean connected = false;
	private boolean problem = false;
	private boolean isClient = false;
	
	private Console console;
	
	private Thread thread;
	
	//Konstruktor
	
	public Multiplayer(String ip, int port) throws InvalidParameterException, InvalidPropertiesFormatException {
		console = new Console(this);
		this.ip = ip;
		this.port = port;
		if (!ip.matches("\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b")) {
			throw new InvalidPropertiesFormatException("Thas not a valid IPv4 address: " + ip);
		}
		if (!availablePort(port)) {
			throw new InvalidParameterException("The port " + port + " is not available!");
		}
		
		Object[] options = {"Host", "Join"};
		int choose = JOptionPane.showOptionDialog(null, "Choose", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
			null, options, options[1]);
		
		
		if (choose == 1) {
			this.key = JOptionPane.showInputDialog(this.console, "Input key here:");
			generateIP();
			console.setTitle("Client");
			boolean joined = join();
			if (joined) {
				isClient = true;
				console.print("Joined Sucessfully\n");
				console.allowInput(true);
			} else {
				System.exit(0);
			}
		} else if (choose == 0) {
			console.setTitle("Server");
			host();
			generateKey();
			console.print("Key to join: " + key + "\n");
		}
		
		
		thread = new Thread(this, "TicTacToe");
		thread.start();
	}
	
	public void run() {
		while(true) {
			tick();
			listenForServerRequest();
			if (problem) {
				thread.interrupt();
				break;
			}
		}
		System.exit(0);
		
	}
	
	//Methoden
	
	public boolean host() {
		return initializeServer();
	}
	
	public boolean join() {
		return connect();
	}
	
	private boolean connect() {
		try {
			socket = new Socket(ip, port);
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			connected = true;
		} catch (IOException e) {
			console.print("Unable to connect to the address: " + ip + ":" + port + "\n");
			return false;
		}
		console.print("Successfully connected!\n");
		return true;
	}
	
	private boolean initializeServer() {
		try {
			serverSocket = new ServerSocket(port, 8, InetAddress.getByName(ip));
			console.print("Started Server!\n" + ip + " | " + port + "\n");
			console.allowInput(true);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void tick() {
		if (errors >= 10) {
			System.out.println("Error");
			problem = true;
		}
		try {
			if (connected) {
				String message = dis.readUTF();
				console.print(message+"\n");
			}
		} catch (IOException e) {
			errors++;
			e.printStackTrace();
		}
	}
	
	private void listenForServerRequest() {
		Socket socket = null;
		try {
			if (!connected) {
				socket = serverSocket.accept();
				dos = new DataOutputStream(socket.getOutputStream());
				dis = new DataInputStream(socket.getInputStream());
				connected = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static boolean availablePort(int port) {
	    try (Socket ignored = new Socket("localhost", port)) {
	        return false;
	    } catch (IOException ignored) {
	        return true;
	    }
	}
	
	public boolean send(String message) {
		try {
			if (dos != null) {
				dos.writeUTF(message);
				dos.flush();
				return true;
			} else return false;
		} catch (IOException e) {
			errors++;
			return false;
		}
	}
	
	private void generateKey() {
		String[] ipPart = this.ip.split("\\.");
		String toInt = "";
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3 - ipPart[i].length();) {
				ipPart[i] = 0 + ipPart[i];
			}
			toInt += ipPart[i];
		}
		try {
			this.key = Long.toString(Long.parseLong(toInt+""+port), 32);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void generateIP() {
		String[] ipPart = new String[4];
		String toIp = Long.parseLong(key, 32) + "";
		System.out.println("toIp");
		for (int i = 0; i < 4; i++) {
			ipPart[i] = toIp.substring(i*3, i*3+3);
		}
		this.ip = ipPart[0] + "." + ipPart[1] + "." + ipPart[2] + "." + ipPart[3];
		try {
			this.port = Integer.parseInt(toIp.substring(12, toIp.length()));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InvalidParameterException, InvalidPropertiesFormatException {
		try {
			Multiplayer mp = new Multiplayer(Inet4Address.getLocalHost().getHostAddress(), 22222);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
