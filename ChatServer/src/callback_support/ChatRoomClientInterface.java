package callback_support;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatRoomClientInterface extends Remote
{
    public void newQuoteNotification(String newQuote) throws RemoteException; 
}
