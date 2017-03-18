/*
 * Vorhang Kontrolle - Server
 * Autor: Moritz Höwer
 */
package vtag.vorhangkontrolle;

import vtag.vorhangkontrolle.networking.TCPServer;
import vtag.vorhangkontrolle.networking.UDPDiscoveryServer;
import vtag.vorhangkontrolle.views.CommandView;
import vtag.vorhangkontrolle.views.StandbyView;

/**
 * @author Moritz Höwer
 * @version 1.0 - 19.03.2017
 */
public class Controller {
	
	private UDPDiscoveryServer udpServer;
	private TCPServer tcpServer;
	
	private StandbyView standbyView;
	private CommandView commandView;
	
	public Controller(){
		
	}
}
