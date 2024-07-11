package clientlogic;


import commands.MovieGenerator;
import commands.client.*;
import commands.client.logreg.Disconnect;
import datapacks.RequestPackage;
import datapacks.ResponsePackage;
import gui.frames.LoadingFrame;
import gui.frames.LogRegFrame;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ClientProgram {
	protected Scanner reader;
	protected SocketChannel socketChannel;
	protected int fileCallsCount = 0;
	protected String username;
	protected String password;
	protected final ResourceBundle resourceBundle;
	protected Socket socket;
	protected final LogRegFrame lrf;
	protected final ClientCommand logOrRegCommand;

	public ClientProgram(LogRegFrame lrf, ClientCommand clientCommand, String username, String password) {
		this.password = password;
		this.username = username;
		this.logOrRegCommand = clientCommand;
		this.lrf = lrf;
		this.resourceBundle = ResourceBundle.getBundle("connection_details_en");
		for (int i = 0; true; i++) {
			try {
				socketChannel = SocketChannel.open();
				socketChannel.connect(new InetSocketAddress(resourceBundle.getString("host"), Integer.parseInt(resourceBundle.getString("port"))));
				break;
			} catch (IOException e) {
				System.err.println("Server is unavailable right now, try again later");
			}
			if (i == 3) System.exit(1);
			System.out.println("Retrying in 5 seconds");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException ignored) {
			}
		}
	}

	public void run() {
		try {

			login();

			while (true) {
				System.out.print("=# ");
				if (!reader.hasNext() || fileCallsCount > 100) {
					reader = new Scanner(System.in);
					fileCallsCount = 0;
				}
				String[] input = reader.nextLine().trim().split(" ");
				if (input.length == 0) continue;
				if (checkLocal(input)) continue;
//				ClientCommand clientCommand = clientCommandHashMap.get(input[0]);
//				if (clientCommand == null) {
//					System.err.println("No such command");
//					continue;
//				}
//				RequestPackage requestPackage = generateRequest(input, clientCommand);
				ResponsePackage responsePackage;
//				try {
//					if (!send(requestPackage)) continue;
//					responsePackage = receive();
//					process(responsePackage);
//				} catch (IOException | ClassNotFoundException e) {
//					System.err.println(e.getMessage());
//					continue;
//				}
//				if (responsePackage.movie() != null) {
//					Movie movie = MovieGenerator.generateMovie(responsePackage.movie(), reader);
//					try {
//						if (!send(new RequestPackage(
//								username,
//								password,
//								new Update(null),
//								movie
//						))) {
//							System.err.println("Failed to send");
//							continue;
//						}
//						responsePackage = receive();
//						process(responsePackage);
//					} catch (IOException | ClassNotFoundException e) {
//						System.err.println(e.getMessage());
//					}
//				}
			}
		} catch (NoSuchElementException e) {
			System.out.println("Shutting down...");
		} finally {
			try (
					Socket socket = socketChannel.socket();
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())
			) {
				objectOutputStream.writeObject(new RequestPackage(
						username,
						password,
						new Disconnect(),
						null
				));
				objectOutputStream.flush();
				objectInputStream.readObject();
			} catch (IOException | ClassNotFoundException ignored) {
			}
		}
	}

	public boolean login() {
		socket = socketChannel.socket();
		RequestPackage requestPackage;
		requestPackage = new RequestPackage(
				username,
				password,
				logOrRegCommand,
				null
		);
		try {
			if (!send(requestPackage)) return false;
			ResponsePackage responsePackage = receive();
			process(responsePackage);
			if (!responsePackage.errorsOccurred()) {
				LoadingFrame.getInstance().setVisible(false);
				lrf.setVisible(false);
				return true;
			}
		} catch (IOException | ClassNotFoundException ignored) {}
		return false;
	}

	public boolean send(RequestPackage requestPackage) throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		objectOutputStream.writeObject(requestPackage);
		objectOutputStream.flush();
		return true;
	}

	public ResponsePackage receive() throws IOException, ClassNotFoundException {
		ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
		return (ResponsePackage) objectInputStream.readObject();
	}

	protected boolean checkLocal(String[] input) {
		if (input[0].equals("exit") && input.length == 1) System.exit(0);
		if (input[0].equals("exit")) {
			if (input.length == 2) {
				try {
					int code = Integer.parseInt(input[1]);
					System.exit(code);
				} catch (NumberFormatException e) {
					System.err.println("Argument should be an integer");
				}
			} else System.err.println("This command takes 1 or 0 arguments");
			return true;
		}
		if (input[0].equals("execute_script")) {
			if (input.length == 2) {
				try {
					reader = new Scanner(new FileReader(input[1]));
					fileCallsCount++;
				} catch (FileNotFoundException e) {
					System.err.println("No such file");
				}
			} else System.err.println("This command takes exactly one argument");
			return true;
		}
		return false;
	}

	protected void process(ResponsePackage responsePackage) {
		if (responsePackage.errorsOccurred()) System.err.println(responsePackage.response());
		else System.out.println(responsePackage.response());
	}

	protected boolean checkForMovie(String[] input) {
		switch (input[0]) {
			case "add", "add_if_max", "remove_lower" -> {
				return true;
			}
			default -> {
				return false;
			}
		}
	}

	protected RequestPackage generateRequest(String[] input, ClientCommand command) {
		Object o;
		if (checkForMovie(input)) {
			o = MovieGenerator.generateMovie(reader);
		} else {
			if (input.length == 1) o = null;
			else {
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < input.length; i++) {
					sb.append(input[i]).append(' ');
				}
				o = sb.toString().trim();
			}
		}
		return new RequestPackage(
				username,
				password,
				command,
				o
		);
	}

}