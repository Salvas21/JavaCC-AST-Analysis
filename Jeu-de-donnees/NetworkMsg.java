import java.util.LinkedList;
import java.util.List;

public class NetworkMsg
{
	public enum msgHeader
	{
		OK,					// Reponse du serveur
		KO,					// Reponse du serveur
		PlayerJoinRoom,		// 1 Joueur c'est connecté à la room
		ListPlayerRoom,	 	// Liste des joueurs présent dans la room avec leur équipe, envoyé après la connection/déconnexion d'un joueur à la room, changement nom/team
		KickPlayerRoom,		// depuis le client : suppression d'un joueur dans la room, uniquement par l'hote. depuis serveur : joueur à quitter la room
		DeletedRoom,		// Supression de la room.
		ChangeTeamPlayer,	// Changement de l'équipe du joueur (uniquement dans la room).
		ChangeNamePlayer,	// Changement du nom du joueur
		RunGame,			// Lancement de la partie (depuis la room)
		Shoot,				// depuis le client : tir avec posX, posY
		MvtTarget,			// Mvt du pigeon d'argile avec id et nouvelle position du joueur
		KillTarget,			// Mort du pigeon suite à un tir, id du pigeon et position de la mort
		OutTarget,			// Pigeon survivant, id du pigeon.
		ChangeScore,		// Changement du score d'une team. Id de la team + son nouveau score
		PlayerTurn,			// Nom du joueur allant jouer.
		EndGame,			// Fin de la partie avec : scoreTeam1, scoreTeam2, idTeamWinner
		PosForShoot,		// Changement du stand de tir
		Pull,				// Declanchement pull.
		Mark,				// Dechanchement mark.
		Double,				// Declanchement du double
		Fire,				// Lancement du pigeon.
	};
	
	msgHeader _header;
	byte[] _data;
	
	public NetworkMsg(msgHeader header, byte[] data)
	{
		_header = header;
		_data =  data;
	}
	
	public byte[] arrayByteToSend()
	{
		byte[] headerOrdinal = intToBytes(_header.ordinal());
		byte[] length = intToBytes( _data.length);
		byte[] send = new byte[headerOrdinal.length + length.length + _data.length];

		System.arraycopy(headerOrdinal, 0, send, 0, headerOrdinal.length);
		System.arraycopy(length, 0, send, headerOrdinal.length, length.length);
		System.arraycopy(_data, 0, send, headerOrdinal.length + length.length, _data.length);
		return send;
	}
	
	public void setHeader(msgHeader header)
	{
		_header = header;
	}
	
	public msgHeader getHeader()
	{
		return _header;
	}

	public void setData(byte[] data)
	{
		_data = data;
	}
	
	public byte[] getData()
	{
		return _data;
	}

	public String toString()
	{
		String send = "";
		
		send += _header.toString() + " : ";
		  for (int i = 0; i <  _data.length; i++)
			  send += (char)_data[i];
		return send;
	}

	static public String toString(NetworkMsg msg)
	{
		return msg.toString();
	}

	static public Pair<List<NetworkMsg>, byte[]> fromBytes(byte[] msg, int dataLength)
	{
		List<NetworkMsg> list = new LinkedList<NetworkMsg>();
		byte[] stand;
		int pos = 0;
		
		while (dataLength >= 16)
		{
			int headerOrdinal = bytesToInt(msg, pos);
			pos += 8;
			dataLength -= 8;
			if (headerOrdinal < 0 || headerOrdinal > 20)
			{
				if (dataLength > 0)
				{
					stand = new byte[dataLength];
					System.arraycopy(msg, pos, stand, 0, dataLength);
				}
				else
					stand = new byte[0];
				return new Pair<List<NetworkMsg>, byte[]>(list, stand);
			} 
			int length = bytesToInt(msg, pos);
			pos += 8;
			dataLength -= 8;
			if (length < 0)
			{
				pos -= 8;
				if (dataLength > 0)
				{
					stand = new byte[dataLength];
					System.arraycopy(msg, pos, stand, 0, dataLength);
				}
				else
					stand = new byte[0];
				return new Pair<List<NetworkMsg>, byte[]>(list, stand);
			}
			byte[] data = new byte[length];
	
			if (length <= dataLength)
				System.arraycopy(msg, pos, data, 0, length);
			else
			{
				pos -= 16;
				dataLength += 16;
				if (dataLength > 0)
				{
					stand = new byte[dataLength];
					System.arraycopy(msg, pos, stand, 0, dataLength);
				}
				else
					stand = new byte[0];
				return new Pair<List<NetworkMsg>, byte[]>(list, stand);
			}
			pos += length;
			dataLength -= length;
			list.add(new NetworkMsg(msgHeader.values()[headerOrdinal], data));
		}
		if (dataLength > 0)
		{
			stand = new byte[dataLength];
			System.arraycopy(msg, pos, stand, 0, dataLength);
		}
		else
			stand = new byte[0];
		return new Pair<List<NetworkMsg>, byte[]>(list, stand);
	}

	public static int bytesToInt(byte[] bytes, int pos)
    {
        int value = 0;

        for(int i = pos; i >= 0 && i < bytes.length && i < (pos + 8); i++)
        {
            value = value * 10;
            value += (bytes[i] - '0');
        }
        return value;
    }
	
	public static byte[] intToBytes(int value)
    {
		byte[] send = Integer.toString(value).getBytes();
		if (send.length < 8)
		{
			byte[] newSend = new byte[8];
			
			for (int i = 0; i < 8; i++)
				newSend[i] = '0';
			for (int i = 1; i <= send.length; i++)
				newSend[8 - i] = (send[send.length - i]);
			send = newSend;
		}
        return send;
    }
}
