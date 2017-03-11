/*
 * Vorhang Kontrolle - Server
 * Autor: Moritz Höwer
 */
package vtag.vorhangkontrolle.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author Moritz Höwer
 * @version 1.0 - 12.03.2017
 */
public class DiscoveryTestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Find the server using UDP broadcast
		try {
			// Open a random port to send the package
			DatagramSocket sock = new DatagramSocket();
			sock.setBroadcast(true);

			byte[] sendData = "VORHANGKONTROLLE_REQUEST".getBytes();

			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
					InetAddress.getByName("255.255.255.255"), 9001);
			sock.send(sendPacket);
			System.out.println("Request packet sent to: 255.255.255.255 (DEFAULT)");

			// Wait for a response
			byte[] recvBuf = new byte[4096];
			DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
			sock.setSoTimeout(1000);
			sock.receive(receivePacket);

			// We have a response
			System.out.println("Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

			// Check if the message is correct
			String message = new String(receivePacket.getData()).trim();
			if (message.equals("VORHANGKONTROLLE_RESPONSE")) {
				// DO SOMETHING WITH THE SERVER'S IP (for example, store it in
				// your controller)
				System.out.println("======\nServer IP is " + receivePacket.getAddress().getHostAddress() + "\n======");
			}

			// Close the port!
			sock.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
